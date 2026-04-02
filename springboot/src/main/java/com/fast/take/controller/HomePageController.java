package com.fast.take.controller;

import com.fast.system.controller.BaseController;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.User;
import com.fast.system.service.IUserService;
import com.fast.take.domain.Notice;
import com.fast.take.domain.Order;
import com.fast.take.domain.Rider;
import com.fast.take.domain.vo.HomeCountVO;
import com.fast.take.domain.vo.OrderStatusVO;
import com.fast.take.domain.vo.OrderTrendItemVO;
import com.fast.take.domain.vo.OrderTrendVO;
import com.fast.take.service.INoticeService;
import com.fast.take.service.IOrderService;
import com.fast.take.service.IRiderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台首页接口层
 */
@RestController
@RequestMapping("/home/page")
public class HomePageController extends BaseController {
    @Resource
    private IOrderService orderService;
    @Resource
    private IUserService userService;
    @Resource
    private IRiderService riderService;
    @Resource
    private INoticeService noticeService;
    /**
     * 总订单数 用户总数 配送员数 通知公告数
     */
    @GetMapping("/selectHomeCount")
    public AjaxResult selectHomeCount() {
        //总订单数
        int orderCount = orderService.selectOrderList(new Order()).size();
        //用户总数
        int userCount = userService.selectUserList(new User()).size();
        //配送员数
        int riderCount = riderService.selectRiderList(new Rider()).size();
        //通知公告数
        int noticeCount = noticeService.selectNoticeList(new Notice()).size();

        //赋值
        HomeCountVO homeCountVO = new HomeCountVO();
        homeCountVO.setOrderCount(orderCount);
        homeCountVO.setUserCount(userCount);
        homeCountVO.setRiderCount(riderCount);
        homeCountVO.setNoticeCount(noticeCount);
        return  success(homeCountVO);
    }
    /**
     * 查询订单趋势统计
     */
@GetMapping("/selectOrderTrend")
public AjaxResult selectOrderTrend(){
    List<OrderTrendItemVO> orderTrendList = orderService.selectOrderTrend();

    //分离日期和数量列表
    List<String> dates = orderTrendList.stream().map(OrderTrendItemVO::getDates).collect(Collectors.toList());
    List<Integer> counts = orderTrendList.stream().map(OrderTrendItemVO::getCounts).collect(Collectors.toList());

    OrderTrendVO result = new OrderTrendVO();
    result.setDates(dates);
    result.setCounts(counts);
return success(result);
}

/**
 * 查询订单状态分布统计
 *
 */

@GetMapping("/selectOrderStatusChart")
    public AjaxResult selectOrderStatusChart(){
    List<OrderStatusVO> orderStatusVChartList = orderService.selectOrderStatusChart();
    return success(orderStatusVChartList);

}
}
