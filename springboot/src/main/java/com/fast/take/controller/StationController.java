package com.fast.take.controller;

import com.fast.system.controller.BaseController;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.TableDataInfo;
import com.fast.take.domain.Station;
import com.fast.take.service.IStationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 快递站点 Controller
 */
@RestController
@RequestMapping("/take/station")
public class StationController extends BaseController {
   @Resource
   private IStationService stationService;
    /**
     *
     */
@GetMapping("list")
    public TableDataInfo list(Station station){
    startPage();
    List<Station> list = stationService.selectStationList(station);
    return getDataTable(list);
}

    /**
     * 获取快递站点详细信息
     */
    @GetMapping("/{stationId}")
    public AjaxResult selectStationByStationId(@PathVariable String stationId) {

        return success(stationService.selectStationByStationId(stationId));
    }

    /**
     * 新增快递站点
     */
    @PostMapping
    public AjaxResult insertStation(@RequestBody Station station) {
        return toAjax(stationService.insertStation(station));
    }

}