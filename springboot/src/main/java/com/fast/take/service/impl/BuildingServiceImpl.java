package com.fast.take.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fast.take.mapper.BuildingMapper;
import com.fast.take.domain.Building;
import com.fast.take.service.IBuildingService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 宿舍楼Service业务层处理
 *
 * @author fast
 * @date 2025-12-30
 */
@Service
public class BuildingServiceImpl implements IBuildingService {
    @Resource
    private BuildingMapper buildingMapper;

    /**
     * 查询宿舍楼
     *
     * @param buildingId 宿舍楼主键
     * @return 宿舍楼
     */
    @Override
    public Building selectBuildingByBuildingId(String buildingId) {
        return buildingMapper.selectBuildingByBuildingId(buildingId);
    }

    /**
     * 查询宿舍楼列表
     *
     * @param building 宿舍楼
     * @return 宿舍楼
     */
    @Override
    public List<Building> selectBuildingList(Building building) {
        return buildingMapper.selectBuildingList(building);
    }

    /**
     * 新增宿舍楼
     *
     * @param building 宿舍楼
     * @return 结果
     */
    @Override
    public int insertBuilding(Building building) {
        //生成主键UUID并插入对象中
        building.setBuildingId(String.valueOf(UUID.randomUUID()));
        return buildingMapper.insertBuilding(building);
    }

    /**
     * 修改宿舍楼
     *
     * @param building 宿舍楼
     * @return 结果
     */
    @Override
    public int updateBuilding(Building building) {
        return buildingMapper.updateBuilding(building);
    }

    /**
     * 批量删除宿舍楼
     *
     * @param buildingIds 需要删除的宿舍楼主键
     * @return 结果
     */
    @Override
    public int deleteBuildingByBuildingIds(String[] buildingIds) {
        return buildingMapper.deleteBuildingByBuildingIds(buildingIds);
    }
}
