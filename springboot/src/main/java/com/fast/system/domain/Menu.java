package com.fast.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单权限表 menu
 *
 * @author fast
 */
@Data
public class Menu {
    private static final long serialVersionUID = 1L;

    /** 菜单ID */
    private Long menuId;

    /** 菜单名称 */
    private String menuName;

    /** 父菜单名称 */
    private String parentName;

    /** 父菜单ID */
    private Long parentId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 类型（M目录 C菜单） */
    private String menuType;

    /** 菜单图标 */
    private String icon;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 子菜单 */
    private List<Menu> children = new ArrayList<Menu>();

    /** 用户ID */
    private Long userId;
}
