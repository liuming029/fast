package com.fast.take.service.impl;

import com.fast.system.service.IUserService;
import com.fast.take.domain.Order;
import com.fast.take.mapper.OrderMapper;
import com.fast.take.service.IOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Resource
    private IUserService userService;

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


    /**
     * 新增订单
     *
     * @param order 订单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertOrder(Order order) {
        order.setCreateTime(new Date());
        order.setUserId(getUserId());
        //获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        //定义格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        //格式化为字符串
        String formatDateTime = now.format(formatter);
        //订单号: OR + 当前日期时间 + 当前用户ID
        order.setOrderId("OR" + formatDateTime + getUserId());
        //查询用户在支付前的余额
        BigDecimal oldBalance = userService.selectUserById(getUserId()).getBalance();
        //判断余额是否足够扣费
        if (oldBalance.compareTo(order.getTotalPrice()) < 0) {
            throw new RuntimeException("余额不足, 请充值后再下单");
        }

        //扣除用户余额
        //计算扣费后的金额
        BigDecimal newBalance = oldBalance.subtract(order.getTotalPrice());

        //更新用户余额
        userService.updateUserBalance(newBalance, getUserId());

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

    /**
     * 取消订单
     * @param orderId 订单id
     * @return 是否取消成功
     */
    @Override
    @Transactional
    public int cancelOrder(String orderId) {
        //该订单的数据
        Order order = orderMapper.selectOrderByOrderId(orderId);

        //该订单的用户Id
        Long userId = order.getUserId();

        //该用户在取消订单前的余额
        BigDecimal oldBalance = userService.selectUserById(userId).getBalance();

        //退款给用户账户

        //计算退款之后的账户余额

        BigDecimal newBalance = oldBalance.add(order.getTotalPrice());

        //更新账户余额

        userService.updateUserBalance(newBalance,userId);

        //将状态改为已取消
        order.setStatus("已取消");
        orderMapper.updateOrder(order);


        return orderMapper.updateOrder(order);
    }
}
