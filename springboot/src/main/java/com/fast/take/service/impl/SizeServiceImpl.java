package com.fast.take.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fast.take.mapper.SizeMapper;
import com.fast.take.domain.Size;
import com.fast.take.service.ISizeService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 包裹规格Service业务层处理
 *
 * @author fast
 * @date 2025-12-30
 */
@Service
public class SizeServiceImpl implements ISizeService
{
    @Resource
    private SizeMapper sizeMapper;

    /**
     * 查询包裹规格
     *
     * @param sizeId 包裹规格主键
     * @return 包裹规格
     */
    @Override
    public Size selectSizeBySizeId(String sizeId)
    {
        return sizeMapper.selectSizeBySizeId(sizeId);
    }

    /**
     * 查询包裹规格列表
     *
     * @param size 包裹规格
     * @return 包裹规格
     */
    @Override
    public List<Size> selectSizeList(Size size)
    {
        return sizeMapper.selectSizeList(size);
    }

    /**
     * 新增包裹规格
     *
     * @param size 包裹规格
     * @return 结果
     */
    @Override
    public int insertSize(Size size)
    {
        size.setSizeId(String.valueOf(UUID.randomUUID()));
        return sizeMapper.insertSize(size);
    }

    /**
     * 修改包裹规格
     *
     * @param size 包裹规格
     * @return 结果
     */
    @Override
    public int updateSize(Size size)
    {
        return sizeMapper.updateSize(size);
    }

    /**
     * 批量删除包裹规格
     *
     * @param sizeIds 需要删除的包裹规格主键
     * @return 结果
     */
    @Override
    public int deleteSizeBySizeIds(String[] sizeIds)
    {
        return sizeMapper.deleteSizeBySizeIds(sizeIds);
    }
}
