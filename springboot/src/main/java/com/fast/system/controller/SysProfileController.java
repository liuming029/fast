package com.fast.system.controller;

import com.fast.system.config.fastConfig;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.User;
import com.fast.system.domain.LoginUser;
import com.fast.system.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * 个人信息 业务处理
 *
 * @author fast
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Resource
    private IUserService userService;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        User user = userService.selectUserById(getUserId());
        return AjaxResult.success(user);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public AjaxResult updateProfile(@RequestBody User user) {
        LoginUser loginUser = getLoginUser();
        User currentUser = loginUser.getUser();

        // 设置要更新的用户信息
        currentUser.setNickName(user.getNickName());
        currentUser.setSex(user.getSex());

        // 调用服务层更新用户信息
        if (userService.updateUser(currentUser) > 0) {
            return success();
        }

        return error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();

        // 从数据库获取用户信息，确保密码字段正确
        User user = userService.selectUserByUserName(userName);
        String password = user.getPassword();

        if (!oldPassword.equals(password)) {
            return error("修改密码失败，旧密码错误");
        }
        if (newPassword.equals(password)) {
            return error("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(userName, newPassword) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(newPassword);
            return success();
        }
        return error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();

            // 获取上传路径
            String uploadDir = fastConfig.getProfile() + "/avatar";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String uniqueFilename = UUID.randomUUID().toString().replaceAll("-", "") + extension;

            // 保存文件
            Path filePath = Paths.get(uploadDir, uniqueFilename);
            Files.write(filePath, file.getBytes());

            String avatar = "/profile/avatar/" + uniqueFilename;
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                return ajax;
            }
        }
        return error("上传图片异常，请联系管理员");
    }
}