package com.fast.system.utils;

import com.fast.system.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类
 *
 * @author fast
 */
public class SecurityUtils {
    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 用户ID
     **/
    public static Long getUserId() {
        return getLoginUser().getUserId();
    }

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        return getLoginUser().getUsername();
    }
}
