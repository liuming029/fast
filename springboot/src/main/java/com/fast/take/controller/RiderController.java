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

import com.fast.take.domain.Rider;
import com.fast.take.service.IRiderService;
import com.fast.system.domain.TableDataInfo;

/**
 * 配送员Controller
 *
 * @author fast
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/take/rider")
public class RiderController extends BaseController {
    @Resource
    private IRiderService riderService;

    /**
     * 查询配送员列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Rider rider) {
        startPage();
        List<Rider> list = riderService.selectRiderList(rider);
        return getDataTable(list);
    }

    /**
     * 获取配送员详细信息
     */
    @GetMapping(value = "/{riderId}")
    public AjaxResult getInfo(@PathVariable("riderId") String riderId) {
        return success(riderService.selectRiderByRiderId(riderId));
    }

    /**
     * 新增配送员
     */
    @PostMapping
    public AjaxResult add(@RequestBody Rider rider) {
        return toAjax(riderService.insertRider(rider));
    }

    /**
     * 修改配送员
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Rider rider) {
        return toAjax(riderService.updateRider(rider));
    }

    /**
     * 删除配送员
     */
    @DeleteMapping("/{riderIds}")
    public AjaxResult remove(@PathVariable String[] riderIds) {
        return toAjax(riderService.deleteRiderByRiderIds(riderIds));
    }

/**
 * 查询用户有没有提交过审核，如果有返回认证状态，没有返回无
 */
    @GetMapping("/selectIsAuthStatus")
    public AjaxResult selectIsAuthToStatus(){
       String status= riderService.selectIsAuthToStatus();
       return success(status);
    }

    /**
     * 当用户重新提交认证后，将之前的认证信息删除
     */
    @DeleteMapping("/deleteOldAuth")
    public AjaxResult deleteOldAuth(){
        return toAjax(riderService.deleteOldAuth());
    }

}
