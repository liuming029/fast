package com.fast.take.mapper;

import com.fast.take.domain.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 快递站点 Mapper接口
 */
@Mapper
public interface StationMapper {


    List<Station> selectStationList(Station station);
    /**
     * 获取快递站点详细信息
     * @param stationId 站点主键
     * @return 快递站点详细信息
     */
    Station selectStationByStationId(String stationId);

    /**
     * 新增快递站点
     * @param station 表单参数
     * @return 是否新增成功
     */
    int insertStation(Station station);
}