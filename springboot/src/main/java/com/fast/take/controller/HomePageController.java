package com.fast.take.controller;

import com.fast.system.controller.BaseController;
import com.fast.system.domain.AjaxResult;
import com.fast.system.domain.User;
import com.fast.system.service.IUserService;
import com.fast.take.domain.Notice;
import com.fast.take.domain.Order;
import com.fast.take.domain.Rider;
import com.fast.take.domain.vo.HomeCountVO;
import com.fast.take.service.INoticeService;
import com.fast.take.service.IOrderService;
import com.fast.take.service.IRiderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
