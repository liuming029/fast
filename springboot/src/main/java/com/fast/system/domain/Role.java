package com.fast.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * 角色表 role
 *
 * @author fast
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long roleId;

    /** 角色名称 */
    private String roleName;

    /** 角色排序 */
    private Integer roleSort;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 菜单组 */
    private Long[] menuIds;

    /** 角色菜单权限 */
    private Set<String> permissions;
}
