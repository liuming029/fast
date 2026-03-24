package com.fast.take.service.impl;

import com.fast.take.domain.Order;
import com.fast.take.mapper.OrderMapper;
import com.fast.take.service.IOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static com.fast.system.utils.SecurityUtils.getUserId;
/**
 * 订单Service 业务层处理
 *
 * @author fast
 * @date 2026-01-02
 */
@Service
public class OrderServiceImpl implements IOrderService
{
    @Resource
    private OrderMapper orderMapper;

    /**
     * 查询订单
     *
     * @param orderId 订单主键
     * @return 订单
     */
    @Override
    public Order selectOrderByOrderId(String orderId)
    {
        return orderMapper.selectOrderByOrderId(orderId);
    }

    /**
     * 查询订单列表
     *
     * @param order 订单
     * @return 订单
     */
    @Override
    public List<Order> selectOrderList(Order order)
    {
        return orderMapper.selectOrderList(order);
    }

    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    public int insertOrder(Order order)
    {
        order.setUserId(getUserId());
        order.setCreateTime(new Date());

       //获取当前时间
        LocalDateTime now = LocalDateTime.now();

        //定义格式器
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        //格式化为字符串
        String formatDateTime = now.format(formatter);
        //订单号 OR+ 当前日期 + 当前用户ID
        order.setOrderId("OR"+formatDateTime + getUserId());

        return orderMapper.insertOrder(order);
    }

    /**
     * 修改订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    public int updateOrder(Order order)
    {
        return orderMapper.updateOrder(order);
    }

    /**
     * 批量删除订单
     *
     * @param orderIds 需要删除的订单主键
     * @return 结果
     */
    @Override
    public int deleteOrderByOrderIds(String[] orderIds)
    {
        return orderMapper.deleteOrderByOrderIds(orderIds);
    }
}
