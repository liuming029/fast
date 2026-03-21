package com.fast.take.domain;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 配送员对象 rider
 *
 * @author fast
 * @date 2025-12-31
 */

@Data
public class Rider
{

    /** 配送员ID */
    private String riderId;

    /** 真实姓名 */
    private String realName;

    /** 身份证号 */
    private String idCard;

    /** 手机号码 */
    private String phone;

    /** 学号 */
    private String studentId;

    /** 学生证 */
    private String studentCardImage;

    /** 身份证 */
    private String idCardImage;

    /** 认证状态 */
    private String status;

    /** 用户ID */
    private Long userId;

    /** 认证提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 认证用户 */
    private String userName;


}
