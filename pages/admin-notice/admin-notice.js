const request = require('../../utils/request.js');

function stripHtml(html) {
  return html.replace(/<[^>]+>/g, '');
}

Page({
  data: {
    noticeList: [],
    showAddModal: false,
    newNoticeTitle: '',
    newNoticeContent: ''
  },

  onLoad() {
    this.loadNoticeList();
  },

  onShow() {
    this.loadNoticeList();
  },

  async loadNoticeList() {
    try {
      const res = await request({
        url: '/take/notice/list',
        method: 'GET'
      });
      
      if (res && res.rows) {
        const noticeList = res.rows.map(notice => ({
          noticeId: notice.noticeId,
          title: notice.title ? stripHtml(notice.title) : '无标题',
          content: notice.content ? stripHtml(notice.content) : '无内容',
          createTime: notice.createTime
        }));

        this.setData({ noticeList });
      }
    } catch (err) {
      console.log('获取公告列表失败', err);
    }
  },

  openAddModal() {
    this.setData({
      showAddModal: true,
      newNoticeTitle: '',
      newNoticeContent: ''
    });
  },

  closeAddModal() {
    this.setData({
      showAddModal: false
    });
  },

  onTitleInput(e) {
    this.setData({
      newNoticeTitle: e.detail.value
    });
  },

  onContentInput(e) {
    this.setData({
      newNoticeContent: e.detail.value
    });
  },

  async addNotice() {
    const { newNoticeTitle, newNoticeContent } = this.data;

    if (!newNoticeTitle) {
      wx.showToast({
        title: '请输入标题',
        icon: 'none'
      });
      return;
    }

    if (!newNoticeContent) {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      });
      return;
    }

    try {
      await request({
        url: '/take/notice',
        method: 'POST',
        data: {
          title: newNoticeTitle,
          content: newNoticeContent
        }
      });

      wx.showToast({
        title: '添加成功',
        icon: 'success'
      });

      this.closeAddModal();
      this.loadNoticeList();
    } catch (err) {
      console.log('添加公告失败', err);
      wx.showToast({
        title: '添加失败，请重试',
        icon: 'none'
      });
    }
  },

  async deleteNotice(e) {
    const noticeId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个公告吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/notice/${noticeId}`,
              method: 'DELETE'
            });
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadNoticeList();
          } catch (err) {
            console.log('删除公告失败', err);
            wx.showToast({
              title: '删除失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  }
});