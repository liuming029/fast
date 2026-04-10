package com.fast.take.service.impl;

import com.fast.system.service.IUserService;
import com.fast.take.domain.Order;
import com.fast.take.domain.vo.OrderStatusVO;
import com.fast.take.domain.vo.OrderTrendItemVO;
import com.fast.take.mapper.OrderMapper;
import com.fast.take.mapper.RiderMapper;
import com.fast.take.service.IOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Resource
    private RiderMapper riderMapper;

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

    /**
     * 接单
     * @param orderId  订单Id
     * @return 是否接单成功
     */
    @Override
    public int accept(String orderId) {
//查询当前配送员的 riderId （根据用户Id查询配送员Id）
        String riderId = riderMapper.selectRiderIdByUserId(getUserId());
        //更新订单信息
        Order order = new Order();
        order.setOrderId(orderId);
        order.setRiderId(riderId);
        order.setStatus("配送中");
        return orderMapper.updateOrder(order);
    }
    /**
     * 确认送达
     * @param orderId 订单Id
     * @return 是否执行成功
     */
    @Override
    public int receive(String orderId) {
        //订单信息
        Order order = orderMapper.selectOrderByOrderId(orderId);

        //配送员的用户id
        Long riderToUserId = riderMapper.selectRiderByRiderId(order.getRiderId()).getUserId();

        //计算总价的70%
        //订单的总价
        BigDecimal totalPrice = order.getTotalPrice();
        //配送员提成
        BigDecimal riderCommission = totalPrice.multiply(new BigDecimal("0.7")).setScale(2, RoundingMode.HALF_UP);
        //平台分成
        BigDecimal platformCommission = totalPrice.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
        
        //配送员此前的账户余额
        BigDecimal oldBalancde = userService.selectUserById(riderToUserId).getBalance();
        //配送员完成订单后的账户余额
        BigDecimal newRiderBalance = oldBalancde.add(riderCommission);
        //配送员的账户增加总价的70%
        userService.updateUserBalance(newRiderBalance, riderToUserId);
        
        //admin账号的用户ID（假设admin的userId是1）
        Long adminUserId = 1L;
        //admin账号此前的账户余额
        BigDecimal oldAdminBalance = userService.selectUserById(adminUserId).getBalance();
        //admin账号完成订单后的账户余额
        BigDecimal newAdminBalance = oldAdminBalance.add(platformCommission);
        //admin账号增加总价的30%
        userService.updateUserBalance(newAdminBalance, adminUserId);

        //订单的状态修改为已完成
        order.setStatus("已完成");
        //保存骑手分成和平台分成
        order.setRiderCommission(riderCommission);
        order.setPlatformCommission(platformCommission);

        return orderMapper.updateOrder(order);
    }


    /**
     *查询订单趋势统计
     * @return 订单趋势统计
     */
    @Override
    public List<OrderTrendItemVO> selectOrderTrend() {
        return orderMapper.selectOrderTrend();
    }



    /**
     * 查询订单状态分布统计
     * @return 订单状态分布统计列表数据
     */
    @Override
    public List<OrderStatusVO> selectOrderStatusChart() {
        return orderMapper.selectOrderStatusChart();
    }
}
