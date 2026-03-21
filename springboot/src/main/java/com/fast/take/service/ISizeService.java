package com.fast.take.service;

import java.util.List;
import com.fast.take.domain.Size;

/**
 * 包裹规格Service接口
 *
 * @author fast
 * @date 2025-12-30
 */
public interface ISizeService
{
    /**
     * 查询包裹规格
     *
     * @param sizeId 包裹规格主键
     * @return 包裹规格
     */
    public Size selectSizeBySizeId(String sizeId);

    /**
     * 查询包裹规格列表
     *
     * @param size 包裹规格
     * @return 包裹规格集合
     */
    public List<Size> selectSizeList(Size size);

    /**
     * 新增包裹规格
     *
     * @param size 包裹规格
     * @return 结果
     */
    public int insertSize(Size size);

    /**
     * 修改包裹规格
     *
     * @param size 包裹规格
     * @return 结果
     */
    public int updateSize(Size size);

    /**
     * 批量删除包裹规格
     *
     * @param sizeIds 需要删除的包裹规格主键集合
     * @return 结果
     */
    public int deleteSizeBySizeIds(String[] sizeIds);
}
