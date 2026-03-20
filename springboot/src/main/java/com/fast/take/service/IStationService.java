package com.fast.take.service;

import com.fast.take.domain.Station;

import java.util.List;

public interface IStationService {
    /**
     * 查询快递站点列表
     * @param station 快递站点对象(查询参数)
     * @return 快递站点列表
     */
    List<Station> selectStationList(Station station);
}
