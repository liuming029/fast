package com.fast.take.domain.vo;

import lombok.Data;

/**
 * 订单趋势统计项
 */
@Data
public class OrderTrendItemVO {
    //日期
    private String dates;
    //订单数量
    private Integer counts;
}
