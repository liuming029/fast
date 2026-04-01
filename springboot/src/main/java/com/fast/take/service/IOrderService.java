package com.fast.take.service;

import com.fast.take.domain.Order;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author fast
 * @date 2026-01-02
 */
public interface IOrderService
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
     * @param orderIds 需要删除的订单主键集合
     * @return 结果
     */
    public int deleteOrderByOrderIds(String[] orderIds);


    /**
     * 取消订单
     * @param orderId 订单id
     * @return 是否取消成功
     */
    int cancelOrder(String orderId);


    /**
     * 接单
     * @param orderId  订单Id
     * @return 是否接单成功
     */
    int accept(String orderId);

    /**
     * 确认送达
     * @param orderId 订单Id
     * @return 是否执行成功
     */
    int receive(String orderId);
}
