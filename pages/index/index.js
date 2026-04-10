const request = require('../../utils/request.js');

Page({
  data: {
    selectedStation: '',
    selectedBuilding: '',
    stationList: [],
    buildingList: [],
    noticeList: [],
    isRider: false,
    isAdmin: false,
    pendingCount: 0,
    deliveringCount: 0,
    completedCount: 0,
    stats: {
      orderCount: 0,
      userCount: 0,
      riderCount: 0,
      noticeCount: 0
    }
  },

  onLoad() {
    console.log('========== 首页加载 ==========');
    
    const token = wx.getStorageSync('token');
    if (!token) {
      wx.redirectTo({
        url: '/pages/login/login'
      });
      return;
    }
    
    this.checkUserRole();
    this.loadNoticeList();
  },

  onShow() {
    this.checkUserRole();
  },

  checkUserRole() {
    const userInfo = wx.getStorageSync('userInfo');
    console.log('检查用户角色:', userInfo);
    
    let isAdmin = false;
    let isRider = false;
    
    if (userInfo) {
      if (userInfo.userName === 'admin' || userInfo.role === 'admin') {
        isAdmin = true;
      }
      
      if (userInfo.role === 'rider') {
        isRider = true;
      }
    }
    
    console.log('isAdmin:', isAdmin, 'isRider:', isRider);
    
    this.setData({
      isAdmin: isAdmin,
      isRider: isRider
    });
    
    if (isAdmin) {
      this.loadAdminStats();
    } else if (isRider) {
      this.loadRiderStats();
    } else {
      this.loadStationList();
      this.loadBuildingList();
    }
  },

  async loadNoticeList() {
    try {
      const res = await request({
        url: '/take/notice/list',
        method: 'GET',
        data: {
          pageNum: 1,
          pageSize: 10
        }
      });
      
      console.log('通知列表:', res);
      
      if (res && res.rows) {
        this.setData({
          noticeList: res.rows
        });
      }
    } catch (err) {
      console.log('加载通知失败', err);
    }
  },

  async loadAdminStats() {
    try {
      const res = await request({
        url: '/home/page/selectHomeCount',
        method: 'GET'
      });
      
      console.log('管理后台统计:', res);
      
      if (res && res.data) {
        this.setData({
          stats: res.data
        });
      }
    } catch (err) {
      console.log('加载管理统计失败', err);
    }
  },

  async loadRiderStats() {
    try {
      const res = await request({
        url: '/take/order/list',
        method: 'GET'
      });
      
      console.log('骑手订单列表:', res);
      
      let pendingCount = 0;
      let deliveringCount = 0;
      let completedCount = 0;
      
      if (res && res.rows) {
        res.rows.forEach(order => {
          const status = order.status;
          if (status === '待接单') {
            pendingCount++;
          } else if (status === '配送中') {
            deliveringCount++;
          } else if (status === '已完成') {
            completedCount++;
          }
        });
      }
      
      this.setData({
        pendingCount: pendingCount,
        deliveringCount: deliveringCount,
        completedCount: completedCount
      });
    } catch (err) {
      console.log('加载骑手统计失败', err);
    }
  },

  async loadStationList() {
    try {
      const res = await request({
        url: '/take/station/list',
        method: 'GET',
        data: {
          pageNum: 1,
          pageSize: 100
        }
      });
      
      if (res && res.rows) {
        this.setData({
          stationList: res.rows
        });
      }
    } catch (err) {
      console.log('加载快递站点失败', err);
    }
  },

  async loadBuildingList() {
    try {
      const res = await request({
        url: '/take/building/list',
        method: 'GET',
        data: {
          pageNum: 1,
          pageSize: 100
        }
      });
      
      if (res && res.rows) {
        this.setData({
          buildingList: res.rows
        });
      }
    } catch (err) {
      console.log('加载宿舍楼失败', err);
    }
  },

  selectStation() {
    const stationList = this.data.stationList;
    if (stationList.length === 0) {
      wx.showToast({
        title: '暂无可选站点',
        icon: 'none'
      });
      return;
    }

    const items = stationList.map(s => s.name);
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        this.setData({
          selectedStation: stationList[res.tapIndex].name
        });
      }
    });
  },

  selectBuilding() {
    const buildingList = this.data.buildingList;
    if (buildingList.length === 0) {
      wx.showToast({
        title: '暂无可选宿舍楼',
        icon: 'none'
      });
      return;
    }

    const items = buildingList.map(b => b.name);
    wx.showActionSheet({
      itemList: items,
      success: (res) => {
        this.setData({
          selectedBuilding: buildingList[res.tapIndex].name
        });
      }
    });
  },

  createOrder() {
    const { selectedStation, selectedBuilding } = this.data;
    
    if (!selectedStation) {
      wx.showToast({
        title: '请选择快递驿站',
        icon: 'none'
      });
      return;
    }

    if (!selectedBuilding) {
      wx.showToast({
        title: '请选择宿舍楼',
        icon: 'none'
      });
      return;
    }

    wx.navigateTo({
      url: `/pages/create-order/create-order?station=${encodeURIComponent(selectedStation)}&building=${encodeURIComponent(selectedBuilding)}`
    });
  },



  goToRiderHome() {
    wx.navigateTo({
      url: '/pages/rider-home/rider-home'
    });
  },

  goToAdminHome() {
    wx.navigateTo({
      url: '/pages/admin-home/admin-home'
    });
  },

  goToRiderAuth() {
    wx.navigateTo({
      url: '/pages/rider-auth/rider-auth'
    });
  },

  goToPriceRule() {
    wx.navigateTo({
      url: '/pages/price-rule/price-rule'
    });
  },

  goToOrderGuide() {
    wx.navigateTo({
      url: '/pages/order-guide/order-guide'
    });
  },

  viewNoticeDetail(e) {
    const noticeId = e.currentTarget.dataset.id;
    console.log('点击通知，ID:', noticeId);
    
    if (noticeId) {
      wx.navigateTo({
        url: `/pages/notice-detail/notice-detail?noticeId=${noticeId}`
      });
    }
  }
});