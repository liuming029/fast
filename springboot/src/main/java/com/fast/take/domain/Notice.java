package com.fast.take.domain;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 通知公告对象 notice
 *
 * @author fast
 * @date 2026-01-06
 */

@Data
public class Notice
{

    /** 通知公告ID */
    private String noticeId;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
