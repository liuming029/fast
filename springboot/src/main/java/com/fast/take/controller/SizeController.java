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

import com.fast.take.domain.Size;
import com.fast.take.service.ISizeService;
import com.fast.system.domain.TableDataInfo;

/**
 * 包裹规格Controller
 *
 * @author fast
 * @date 2025-12-30
 */
@RestController
@RequestMapping("/take/size")
public class SizeController extends BaseController {
    @Resource
    private ISizeService sizeService;

    /**
     * 查询包裹规格列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Size size) {
        startPage();
        List<Size> list = sizeService.selectSizeList(size);
        return getDataTable(list);
    }

    /**
     * 获取包裹规格详细信息
     */
    @GetMapping(value = "/{sizeId}")
    public AjaxResult getInfo(@PathVariable("sizeId") String sizeId) {
        return success(sizeService.selectSizeBySizeId(sizeId));
    }

    /**
     * 新增包裹规格
     */
    @PostMapping
    public AjaxResult add(@RequestBody Size size) {
        return toAjax(sizeService.insertSize(size));
    }

    /**
     * 修改包裹规格
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Size size) {
        return toAjax(sizeService.updateSize(size));
    }

    /**
     * 删除包裹规格
     */
    @DeleteMapping("/{sizeIds}")
    public AjaxResult remove(@PathVariable String[] sizeIds) {
        return toAjax(sizeService.deleteSizeBySizeIds(sizeIds));
    }
}
