package com.fast.take.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.fast.take.domain.Rider;

/**
 * 配送员Mapper接口
 *
 * @author fast
 * @date 2025-12-31
 */
@Mapper
public interface RiderMapper
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
     * @param riderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRiderByRiderIds(String[] riderIds);
}
