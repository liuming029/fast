package com.fast.take.service;

import java.util.List;
import com.fast.take.domain.Rider;

/**
 * 配送员Service接口
 *
 * @author fast
 * @date 2025-12-31
 */
public interface IRiderService
{
    /**
     * 查询配送员
     *
     * @param riderId 配送员主键
     * @return 配送员
     */
    public Rider selectRiderByRiderId(String riderId);

    /**
     * 查询配送员列表
     *
     * @param rider 配送员
     * @return 配送员集合
     */
    public List<Rider> selectRiderList(Rider rider);

    /**
     * 新增配送员
     *
     * @param rider 配送员
     * @return 结果
     */
    public int insertRider(Rider rider);

    /**
     * 修改配送员
     *
     * @param rider 配送员
     * @return 结果
     */
    public int updateRider(Rider rider);

    /**
     * 批量删除配送员
     *
     * @param riderIds 需要删除的配送员主键集合
     * @return 结果
     */
    public int deleteRiderByRiderIds(String[] riderIds);

    /**
     * 查询用户有没有提交过审核，如果有返回认证状态，没有返回无
     * @return 认证状态，没有返回无
     */

    String selectIsAuthToStatus();

    /**
     * 当用户重新提交认证后，将之前的认证信息删除
     * @return 是否删除成功
     */

    int deleteOldAuth();
}
