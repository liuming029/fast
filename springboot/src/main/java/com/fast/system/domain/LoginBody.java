package com.fast.system.domain;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author fast
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 唯一标识
     */
    private String uuid;
}
