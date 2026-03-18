package com.fast.system.controller;

import com.fast.system.domain.User;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.RegisterBody;
import com.fast.system.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author fast
 */
@RestController
public class SysRegisterController extends BaseController {
    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        String msg = "", username = user.getUsername(), password = user.getPassword();
        User User = new User();
        User.setUserName(username);

        if (username.isEmpty()) {
            msg = "用户名不能为空";
        } else if (password.isEmpty()) {
            msg = "用户密码不能为空";
        } else {
            User.setNickName(username);
            User.setPassword(password);
            boolean regFlag = userService.registerUser(User);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            }
        }
        return msg.isEmpty() ? success() : error(msg);
    }
}
