package com.fast.system.controller;

import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.Menu;
import com.fast.system.domain.User;
import com.fast.system.domain.LoginBody;
import com.fast.system.domain.LoginUser;
import com.fast.system.utils.SecurityUtils;
import com.fast.system.configure.TokenService;
import com.fast.system.service.IMenuService;
import com.fast.system.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author fast
 */
@RestController
public class SysLoginController {
    @Resource
    private IMenuService menuService;

    @Resource
    private TokenService tokenService;

    @Resource
    private IUserService userService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 用户名或密码为空 错误
        if (loginBody.getUsername().isEmpty() || loginBody.getPassword().isEmpty()) {
            throw new RuntimeException("用户名或密码为空");
        }

        // 验证用户名和密码
        User user = userService.selectUserByUserName(loginBody.getUsername());
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 密码比较
        if (!loginBody.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 构造LoginUser对象
        LoginUser loginUser = new LoginUser(user.getUserId(), user, null);

        // 生成令牌
        String token = tokenService.createToken(loginUser);
        ajax.put("token", token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = userService.selectUserById(loginUser.getUserId());
        return AjaxResult.success(user);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
