const request = require('../../utils/request.js');

Page({
  data: {
    userInfo: {},
    avatar: '',
    balance: 0,
    balanceFormatted: '0.00',
    riderStatus: '无',
    isRider: false,
    isAdmin: false
  },

  onLoad() {
    console.log('========== 我的页面加载 ==========');
    this.loadUserInfo();
    this.loadBalance();
    this.checkRiderStatus();
  },

  onShow() {
    console.log('========== 我的页面显示 ==========');
    this.loadUserInfo();
    this.loadBalance();
    this.checkRiderStatus();
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo') || {};
    const avatar = wx.getStorageSync('userAvatar') || '';
    console.log('用户信息:', userInfo);
    
    let isAdmin = false;
    if (userInfo.userName === 'admin' || userInfo.role === 'admin') {
      isAdmin = true;
    }
    
    this.setData({ 
      userInfo: userInfo,
      avatar: avatar,
      isAdmin: isAdmin
    });
  },

  async loadBalance() {
    try {
      console.log('========== 开始获取余额 ==========');
      const res = await request({
        url: '/system/user/selectMyBalance',
        method: 'GET'
      });
      console.log('原始响应:', res);
      
      let balance = 0;
      if (res && res.data !== undefined && res.data !== null) {
        balance = res.data;
      } else if (res !== undefined && res !== null && typeof res !== 'object') {
        balance = res;
      }
      
      console.log('解析后的余额:', balance);
      console.log('余额类型:', typeof balance);
      
      this.setData({ 
        balance: balance,
        balanceFormatted: (parseFloat(balance) || 0).toFixed(2)
      });
      
    } catch (err) {
      console.log('获取余额失败', err);
    }
  },

  async checkRiderStatus() {
    try {
      console.log('========== 检查骑手状态 ==========');
      const res = await request({
        url: '/take/rider/selectIsAuthStatus',
        method: 'GET'
      });
      console.log('骑手状态响应:', res);
      
      let status = '无';
      if (res && res.msg !== undefined && res.msg !== null) {
        status = res.msg;
      } else if (res && res.data !== undefined && res.data !== null) {
        status = res.data;
      } else if (res !== undefined && res !== null && typeof res === 'string') {
        status = res;
      }
      
      console.log('骑手认证状态:', status);
      
      this.setData({ 
        riderStatus: status,
        isRider: status === '已通过'
      });
      
    } catch (err) {
      console.log('检查骑手状态失败', err);
    }
  },

  goToOrders() {
    console.log('点击我的订单');
    wx.switchTab({
      url: '/pages/order/order'
    });
  },

  goToRecharge() {
    console.log('点击充值');
    wx.navigateTo({
      url: '/pages/recharge/recharge'
    });
  },

  goToRiderAuth() {
    console.log('点击骑手认证');
    wx.navigateTo({
      url: '/pages/rider-auth/rider-auth'
    });
  },

  goToRiderHome() {
    console.log('点击骑手工作台');
    wx.navigateTo({
      url: '/pages/rider-home/rider-home'
    });
  },

  goToAdminHome() {
    console.log('点击管理后台');
    wx.navigateTo({
      url: '/pages/admin-home/admin-home'
    });
  },

  goToProfile() {
    console.log('点击个人资料');
    wx.navigateTo({
      url: '/pages/profile/profile'
    });
  },

  goToAbout() {
    console.log('点击关于我们');
    wx.navigateTo({
      url: '/pages/about/about'
    });
  },

  logout() {
    console.log('点击退出登录');
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.reLaunch({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});