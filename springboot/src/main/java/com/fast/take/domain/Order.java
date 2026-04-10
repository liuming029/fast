package com.fast.take.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单对象 order
 *
 * @author fast
 * @date 2026-01-02
 */

@Data
public class Order
{

    /** 订单ID */
    private String orderId;

    /** 取件码 */
    private String code;

    /** 包裹规格ID */
    private String sizeId;

    /** 宿舍楼ID */
    private String buildingId;

    /** 快递站点ID */
    private String stationId;

    /** 寝室号/联系人 */
    private String room;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 骑手分成 */
    private BigDecimal riderCommission;

    /** 平台分成 */
    private BigDecimal platformCommission;

    /** 状态 */
    private String status;

    /** 备注 */
    private String remark;

    /** 下单用户ID */
    private Long userId;

    /** 配送员ID */
    private String riderId;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //包裹规格
    private String size;

    //宿舍楼
    private String building;

    //快递站点
    private String station;

    //快递员
    private String rider;

    //下单用户
    private String userName;

    //配送员对应的用户Id
    private long riderToUserId;

}
