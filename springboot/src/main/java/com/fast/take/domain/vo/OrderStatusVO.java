package com.fast.take.domain.vo;

import lombok.Data;

/**
 * 订单状态统计
 */
@Data
public class OrderStatusVO {
    //状态名称
    private String status;
    //数量
    private Integer count;
}
