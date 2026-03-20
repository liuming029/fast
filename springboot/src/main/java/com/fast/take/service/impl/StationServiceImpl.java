
package com.fast.take.service.impl;

import com.fast.take.domain.Station;
import com.fast.take.mapper.StationMapper;
import com.fast.take.service.IStationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
