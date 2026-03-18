package com.fast.system.domain;

import lombok.Data;

/**
 * 用户和角色关联 user_role
 *
 * @author fast
 */
@Data
public class UserRole
{
    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;
}
