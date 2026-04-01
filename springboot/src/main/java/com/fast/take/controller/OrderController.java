package com.fast.take.controller;

import com.fast.system.controller.BaseController;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.TableDataInfo;
import com.fast.take.domain.Order;
import com.fast.take.service.IOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单Controller
 *
 * @author fast
 * @date 2026-01-02
 */
@RestController
@RequestMapping("/take/order")
public class OrderController extends BaseController {
    @Resource
    private IOrderService orderService;

    /**
     * 查询订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Order order) {
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 获取订单详细信息
     */
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") String orderId) {
        return success(orderService.selectOrderByOrderId(orderId));
    }

    /**
     * 新增订单
     */
    @PostMapping
    public AjaxResult add(@RequestBody Order order) {
        return toAjax(orderService.insertOrder(order));
    }

    /**
     * 修改订单
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Order order) {
        return toAjax(orderService.updateOrder(order));
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds) {
        return toAjax(orderService.deleteOrderByOrderIds(orderIds));
    }

    /**
     * 查询用户个人订单列表
     */
    @GetMapping("/selectMyOrderList")
    public TableDataInfo selectMyOrderList(Order order){
        // 🌟 看这里！打印拿到的 userId
        System.out.println("当前登录用户ID：" + getUserId());
        order.setUserId(getUserId());
        startPage();
        List<Order> list = orderService.selectOrderList(order);
        System.out.println("查询到的订单列表：" + list); // 看是否有数据
        return getDataTable(list);
    }
/**
 * 取消订单
 */
@PutMapping("/cancelOrder/{orderId}")
    public AjaxResult cancelOrder(@PathVariable String orderId){
    System.out.println(orderId);
    return toAjax(orderService.cancelOrder(orderId));

}
/**
 * 接单
 *
 */
@PutMapping("/accept/{orderId}")
    public AjaxResult accept(@PathVariable String orderId){

    return toAjax(orderService.accept(orderId));
}

/**
 * 查询配送员个人的接单记录列表
 */
@GetMapping("/selectOrderListByRiderToUserId")
    public TableDataInfo selectOrderListByRiderToUserId(Order order){
    order.setRiderToUserId(getUserId());
    startPage();
    List<Order> list = orderService.selectOrderList(order);
    return getDataTable(list);

}
/**
 * 确认送达
 */
@PutMapping("/receive/{orderId}")
    public AjaxResult receive(@PathVariable String orderId) {
    return toAjax(orderService.receive(orderId));
}

}


