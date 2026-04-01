package com.fast.take.service;

import com.fast.take.domain.Notice;

import java.util.List;

/**
 * 通知公告Service接口
 *
 * @author fast
 * @date 2026-01-06
 */
public interface INoticeService
{
    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    public Notice selectNoticeByNoticeId(String noticeId);

    /**
     * 查询通知公告列表
     *
     * @param notice 通知公告
     * @return 通知公告集合
     */
    public List<Notice> selectNoticeList(Notice notice);

    /**
     * 新增通知公告
     *
     * @param notice 通知公告
     * @return 结果
     */
    public int insertNotice(Notice notice);

    /**
     * 修改通知公告
     *
     * @param notice 通知公告
     * @return 结果
     */
    public int updateNotice(Notice notice);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告主键集合
     * @return 结果
     */
    public int deleteNoticeByNoticeIds(String[] noticeIds);
}
