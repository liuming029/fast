package com.fast.take.service.impl;

import com.fast.take.domain.Notice;
import com.fast.take.mapper.NoticeMapper;
import com.fast.take.service.INoticeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 通知公告Service业务层处理
 *
 * @author fast
 * @date 2026-01-06
 */
@Service
public class NoticeServiceImpl implements INoticeService
{
    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    @Override
    public Notice selectNoticeByNoticeId(String noticeId)
    {
        return noticeMapper.selectNoticeByNoticeId(noticeId);
    }

    /**
     * 查询通知公告列表
     *
     * @param notice 通知公告
     * @return 通知公告
     */
    @Override
    public List<Notice> selectNoticeList(Notice notice)
    {
        return noticeMapper.selectNoticeList(notice);
    }

    /**
     * 新增通知公告
     *
     * @param notice 通知公告
     * @return 结果
     */
    @Override
    public int insertNotice(Notice notice)
    {
        notice.setNoticeId(String.valueOf(UUID.randomUUID()));
        notice.setCreateTime(new Date());
        return noticeMapper.insertNotice(notice);
    }

    /**
     * 修改通知公告
     *
     * @param notice 通知公告
     * @return 结果
     */
    @Override
    public int updateNotice(Notice notice)
    {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告主键
     * @return 结果
     */
    @Override
    public int deleteNoticeByNoticeIds(String[] noticeIds)
    {
        return noticeMapper.deleteNoticeByNoticeIds(noticeIds);
    }
}
