package com.fast.take.mapper;

import com.fast.take.domain.Order;
import com.fast.take.domain.vo.OrderStatusVO;
import com.fast.take.domain.vo.OrderTrendItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单Mapper接口
 *
 * @author fast
 * @date 2026-01-02
 */
@Mapper
public interface OrderMapper
{
    /**
     * 查询订单
     *
     * @param orderId 订单主键
     * @return 订单
     */
    public Order selectOrderByOrderId(String orderId);

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单集合
     */
    public List<Order> selectOrderList(Order order);

    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    public int insertOrder(Order order);

    /**
     * 修改订单
     *
     * @param order 订单
     * @return 结果
     */
    public int updateOrder(Order order);

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderByOrderIds(String[] orderIds);


    /**
     *查询订单趋势统计
     * @return 订单趋势统计
     */
    List<OrderTrendItemVO> selectOrderTrend();


    /**
     * 查询订单状态分布统计
     * @return 订单状态分布统计列表数据
     */
    List<OrderStatusVO> selectOrderStatusChart();
}
