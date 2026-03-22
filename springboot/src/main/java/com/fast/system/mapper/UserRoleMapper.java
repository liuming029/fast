package com.fast.system.mapper;

import com.fast.system.domain.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author fast
 */
public interface UserRoleMapper
{
    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserRole(Long[] ids);

    /**
     * 批量新增用户角色信息
     *
     * @param userRoleList 用户角色列表
     * @return 结果
     */
    public int batchUserRole(List<UserRole> userRoleList);

    /**
     * 将用户的角色修改为对应的角色
     * @param roleId 角色ID
     * @param userId 要修改角色的 用户ID
     * @return 是否修改成功
     */
    int updateUserRoleByRoleId(Long roleId, Long userId);





}
