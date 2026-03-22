package com.fast.take.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fast.take.mapper.RiderMapper;
import com.fast.take.domain.Rider;
import com.fast.take.service.IRiderService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.fast.system.utils.SecurityUtils.getUserId;

/**
 * 配送员Service业务层处理
 *
 * @author fast
 * @date 2025-12-31
 */
@Service
public class RiderServiceImpl implements IRiderService
{
    @Resource
    private RiderMapper riderMapper;

    /**
     * 查询配送员
     *
     * @param riderId 配送员主键
     * @return 配送员
     */
    @Override
    public Rider selectRiderByRiderId(String riderId)
    {
        return riderMapper.selectRiderByRiderId(riderId);
    }

    /**
     * 查询配送员列表
     *
     * @param rider 配送员
     * @return 配送员
     */
    @Override
    public List<Rider> selectRiderList(Rider rider)
    {
        return riderMapper.selectRiderList(rider);
    }

    /**
     * 新增配送员
     *
     * @param rider 配送员
     * @return 结果
     */
    @Override
    public int insertRider(Rider rider)
    {
        rider.setCreateTime(new Date());
        rider.setRiderId(String.valueOf(UUID.randomUUID()));
        rider.setUserId(getUserId());
        return riderMapper.insertRider(rider);
    }

    /**
     * 修改配送员
     *
     * @param rider 配送员
     * @return 结果
     */
    @Override
    public int updateRider(Rider rider)
    {
        return riderMapper.updateRider(rider);
    }

    /**
     * 批量删除配送员
     *
     * @param riderIds 需要删除的配送员主键
     * @return 结果
     */
    @Override
    public int deleteRiderByRiderIds(String[] riderIds)
    {
        return riderMapper.deleteRiderByRiderIds(riderIds);
    }


    /**
     * 查询用户有没有提交过审核, 如果有就返回认证状态, 没有就返回无
     * @return 认证状态, 没有就返回无
     */
    @Override
    public String selectIsAuthToStatus() {
        Long userId = getUserId();
        String status = riderMapper.selectIsAuthToStatus(userId);
        return status != null ? status : "无" ;
    }

    /**
     * 当用户重新提交认证后，将之前的认证信息删除
     * @return 是否删除成功
     */


    @Override
    public int deleteOldAuth() {
        //查询之前的认证ID
        String riderId = riderMapper.selectOldAuthRiderIdByUserId(getUserId());
        String[] rideIds = new String[]{
                riderId
        };
        //删除之前的认证ID
        return riderMapper.deleteRiderByRiderIds(rideIds);
    }
}
