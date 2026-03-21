package com.fast.take.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.fast.take.domain.Building;

/**
 * 宿舍楼Mapper接口
 *
 * @author fast
 * @date 2025-12-30
 */
@Mapper
public interface BuildingMapper
{
    /**
     * 查询宿舍楼
     *
     * @param buildingId 宿舍楼主键
     * @return 宿舍楼
     */
    public Building selectBuildingByBuildingId(String buildingId);

    /**
     * 查询宿舍楼列表
     *
     * @param building 宿舍楼
     * @return 宿舍楼集合
     */
    public List<Building> selectBuildingList(Building building);

    /**
     * 新增宿舍楼
     *
     * @param building 宿舍楼
     * @return 结果
     */
    public int insertBuilding(Building building);

    /**
     * 修改宿舍楼
     *
     * @param building 宿舍楼
     * @return 结果
     */
    public int updateBuilding(Building building);

    /**
     * 批量删除宿舍楼
     *
     * @param buildingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuildingByBuildingIds(String[] buildingIds);
}
