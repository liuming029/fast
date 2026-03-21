
package com.fast.take.service.impl;

import com.fast.take.domain.Station;
import com.fast.take.mapper.StationMapper;
import com.fast.take.service.IStationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 快递站点 Service业务层
 */
@Service
public class StationServiceImpl implements IStationService {
    @Resource
    private StationMapper stationMapper;

    @Override
    public List<Station> selectStationList(Station station) {
             return stationMapper.selectStationList(station);
    }

    @Override
    public Station selectStationByStationId(String stationId) {
        return stationMapper.selectStationByStationId(stationId);
    }
    /**
     * 新增快递站点
     * @param station 表单参数
     * @return 是否新增成功
     */
    @Override
    //生成一个UUID并插入对象中
    public int insertStation(Station station) {
        station.setStationId(String.valueOf(UUID.randomUUID()));
        return stationMapper.insertStation(station);
    }

    /**
     * 修改快递站点
     * @param station 表单参数
     * @return 是否修改成功
     */
    @Override
    public int updateStation(Station station) {
        return stationMapper.updateStation(station);
    }


}

