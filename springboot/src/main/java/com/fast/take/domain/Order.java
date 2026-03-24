package com.fast.take.domain;

import java.math.BigDecimal;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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


}
