const request = require('../../utils/request.js');

Page({
  data: {
    stats: {
      orderCount: 0,
      userCount: 0,
      riderCount: 0,
      noticeCount: 0
    },
    orderTrend: [],
    orderStatus: []
  },

  onLoad() {
    this.loadStats();
    this.loadOrderTrend();
    this.loadOrderStatus();
  },

  onShow() {
    this.loadStats();
    this.loadOrderTrend();
    this.loadOrderStatus();
  },

  async loadStats() {
    try {
      const res = await request({
        url: '/home/page/selectHomeCount',
        method: 'GET'
      });
      
      if (res && res.data) {
        this.setData({
          stats: res.data
        });
      }
    } catch (err) {
      console.log('获取统计数据失败', err);
    }
  },

  async loadOrderTrend() {
    try {
      const res = await request({
        url: '/home/page/selectOrderTrend',
        method: 'GET'
      });
      
      if (res && res.data) {
        const trendList = res.data.dates ? res.data.dates.map((date, index) => ({
          date: date,
          count: res.data.counts ? res.data.counts[index] : 0
        })) : [];
        
        const maxCount = trendList.length > 0 ? Math.max(...trendList.map(t => t.count)) : 1;
        
        this.setData({
          orderTrend: trendList.map(item => ({
            ...item,
            barWidth: item.count > 0 ? (item.count / maxCount * 100) : 0
          }))
        });
      }
    } catch (err) {
      console.log('获取订单趋势失败', err);
    }
  },

  async loadOrderStatus() {
    try {
      const res = await request({
        url: '/home/page/selectOrderStatusChart',
        method: 'GET'
      });
      
      if (res && res.data) {
        const statusList = res.data;
        const totalCount = statusList.length > 0 ? statusList.reduce((total, s) => total + s.count, 1) : 1;
        
        this.setData({
          orderStatus: statusList.map(item => ({
            ...item,
            barWidth: item.count > 0 ? (item.count / totalCount * 100) : 0
          }))
        });
      }
    } catch (err) {
      console.log('获取订单状态分布失败', err);
    }
  },

  goToOrderManage() {
    wx.navigateTo({
      url: '/pages/admin-order/admin-order'
    });
  },

  goToUserManage() {
    wx.navigateTo({
      url: '/pages/admin-user/admin-user'
    });
  },

  goToRiderManage() {
    wx.navigateTo({
      url: '/pages/admin-rider/admin-rider'
    });
  },

  goToNoticeManage() {
    wx.navigateTo({
      url: '/pages/admin-notice/admin-notice'
    });
  },

  goToSizeManage() {
    wx.navigateTo({
      url: '/pages/admin-size/admin-size'
    });
  },

  goToStationManage() {
    wx.navigateTo({
      url: '/pages/admin-station/admin-station'
    });
  },

  goToBuildingManage() {
    wx.navigateTo({
      url: '/pages/admin-building/admin-building'
    });
  }
});