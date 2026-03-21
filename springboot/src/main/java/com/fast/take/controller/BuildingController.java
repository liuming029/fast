package com.fast.take.controller;

import java.util.List;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fast.system.controller.BaseController;
import com.fast.system.domain.AjaxResult;

import com.fast.take.domain.Building;
import com.fast.take.service.IBuildingService;
import com.fast.system.domain.TableDataInfo;

/**
 * 宿舍楼Controller
 *
 * @author fast
 * @date 2025-12-30
 */
@RestController
@RequestMapping("/take/building")
public class BuildingController extends BaseController {
    @Resource
    private IBuildingService buildingService;

    /**
     * 查询宿舍楼列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Building building) {
        startPage();
        List<Building> list = buildingService.selectBuildingList(building);
        return getDataTable(list);
    }

    /**
     * 获取宿舍楼详细信息
     */
    @GetMapping(value = "/{buildingId}")
    public AjaxResult getInfo(@PathVariable("buildingId") String buildingId) {
        return success(buildingService.selectBuildingByBuildingId(buildingId));
    }

    /**
     * 新增宿舍楼
     */
    @PostMapping
    public AjaxResult add(@RequestBody Building building) {
        return toAjax(buildingService.insertBuilding(building));
    }

    /**
     * 修改宿舍楼
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Building building) {
        return toAjax(buildingService.updateBuilding(building));
    }

    /**
     * 删除宿舍楼
     */
    @DeleteMapping("/{buildingIds}")
    public AjaxResult remove(@PathVariable String[] buildingIds) {
        return toAjax(buildingService.deleteBuildingByBuildingIds(buildingIds));
    }
}
