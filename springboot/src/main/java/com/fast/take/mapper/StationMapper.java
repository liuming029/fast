package com.fast.take.mapper;

import com.fast.take.domain.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 快递站点 Mapper接口
 */
@Mapper
public interface StationMapper {

    /**
     * @param station
     * @return
     */
    List<Station> selectStationList(Station station);
}