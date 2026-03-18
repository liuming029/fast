package com.fast.system.controller;

import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.Role;
import com.fast.system.domain.LoginUser;
import com.fast.system.domain.TableDataInfo;
import com.fast.system.service.IRoleService;
import com.fast.system.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色信息
 *
 * @author fast
 */
@RestController
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    @Resource
    private IRoleService roleService;

    @GetMapping("/list")
    public TableDataInfo list(Role role) {
        startPage();
        List<Role> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    /**
     * 获取角色选择框列表
     */
    @GetMapping("/selectAllRole")
    public AjaxResult selectAllRole() {
        return success(roleService.selectRoleList(new Role()));
    }

    /**
     * 根据角色编号获取详细信息
     */
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PostMapping
    public AjaxResult add(@RequestBody Role role) {
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Role role) {
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }
}
