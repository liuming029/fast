const request = require('../../utils/request.js');

Page({
  data: {
    noticeId: '',
    notice: {}
  },

  onLoad(options) {
    console.log('========== 通知详情页加载 ==========');
    console.log('参数:', options);
    
    if (options.noticeId) {
      this.setData({
        noticeId: options.noticeId
      });
      this.loadNoticeDetail(options.noticeId);
    }
  },

  removeHtmlTags(html) {
    if (!html) return '';
    return html.replace(/<[^>]+>/g, '');
  },

  async loadNoticeDetail(noticeId) {
    try {
      wx.showLoading({
        title: '加载中...'
      });

      const res = await request({
        url: `/take/notice/${noticeId}`,
        method: 'GET'
      });

      console.log('通知详情响应:', res);

      wx.hideLoading();

      if (res && res.data) {
        const notice = res.data;
        if (notice.content) {
          notice.content = this.removeHtmlTags(notice.content);
        }
        this.setData({
          notice: notice
        });
      }
    } catch (err) {
      wx.hideLoading();
      console.log('加载通知详情失败', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  goBack() {
    wx.navigateBack();
  }
});