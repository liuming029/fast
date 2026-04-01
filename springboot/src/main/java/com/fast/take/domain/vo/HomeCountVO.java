package com.fast.take.domain.vo;

import lombok.Data;

/**
 * 数据统计VO
 */
@Data
public class HomeCountVO {
    //总订单数
    private Integer orderCount;
    //用户总数
    private Integer userCount;
    //配送员数
    private Integer riderCount;
    //通知公告
    private Integer noticeCount;
}
