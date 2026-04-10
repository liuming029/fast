package com.fast.system.controller;

import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.TableDataInfo;
import com.fast.system.domain.User;
import com.fast.system.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户信息
 *
 * @author fast
 */
@RestController
@RequestMapping("/system/user")
public class UserController extends BaseController {
    @Resource
    private IUserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public TableDataInfo list(User user) {
        startPage();
        List<User> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable(required = false) Long userId) {
        User User = userService.selectUserById(userId);
        return AjaxResult.success(User);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public AjaxResult add(@RequestBody User user) {
        user.setPassword(user.getPassword());
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PutMapping
    public AjaxResult edit(@RequestBody User user) {
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }


    /**
     * 查询当前用户的账户余额
     */
    @GetMapping("/selectMyBalance")
    public AjaxResult selectMyBalance(){
        BigDecimal balance = userService.selectUserById(getUserId()).getBalance();
        return success(balance);
    }

    /**
     * 充值接口
     */
    @PutMapping("/recharge/{amount}")
public AjaxResult recharge(@PathVariable BigDecimal amount){
        //用户充值之前的账户余额
        BigDecimal oldBalance = userService.selectUserById(getUserId()).getBalance();
        //计算充值后的余额
        BigDecimal newBalance = oldBalance.add(amount);
        //更新账户余额
        return toAjax(userService.updateUserBalance(newBalance,getUserId()));


    }

    /**
     * 忘记密码-重置密码
     */
    @PostMapping("/forgotPwd")
    public AjaxResult forgotPwd(@RequestBody User user) {
        String userName = user.getUserName();
        String newPassword = user.getPassword();
        
        if (userName == null || userName.isEmpty()) {
            return error("用户名不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return error("新密码不能为空");
        }
        
        User existUser = userService.selectUserByUserName(userName);
        if (existUser == null) {
            return error("用户不存在");
        }
        
        if (userService.resetUserPwd(userName, newPassword) > 0) {
            return success("重置密码成功");
        }
        return error("重置密码失败");
    }

}
