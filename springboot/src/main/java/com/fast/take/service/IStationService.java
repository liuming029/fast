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

    /**
     * 获得快递站点详细信息
     * @param stationId 站点Id
     * @return快递站点详细信息
     */
    Station selectStationByStationId(String stationId);




    /** 新增快递站点
     * @param station 表单参数
     * @return 是否新增成功
     */
    int insertStation(Station station);


    /**
     * 修改快递站点
     * @param station 表单参数
     * @return 是否修改成功
     */
    int updateStation(Station station);
}
