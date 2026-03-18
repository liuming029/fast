package com.fast.system.controller;

import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.Menu;
import com.fast.system.service.IMenuService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author fast
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {
    @Resource
    private IMenuService menuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public AjaxResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable Long roleId) {
        List<Menu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * 新增菜单
     */
    @PostMapping
    public AjaxResult add(@RequestBody Menu menu) {
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Menu menu) {
        if (menu.getMenuId().equals(menu.getParentId())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn("存在子菜单,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
