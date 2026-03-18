package com.fast.system.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由显示信息
 *
 * @author fast
 */
@Data
@NoArgsConstructor
public class MetaVo {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 路由地址
     */
    private String path;

    public MetaVo(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, String path) {
        this.title = title;
        this.icon = icon;
        this.path = path;
    }
}
