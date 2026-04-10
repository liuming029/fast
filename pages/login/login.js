const { login, getInfo } = require('../../utils/auth.js');

Page({
  data: {
    username: '',
    password: ''
  },

  onLoad() {
    console.log('========== 登录页面加载 ==========');
    const token = wx.getStorageSync('token');
    if (token) {
      wx.switchTab({
        url: '/pages/index/index'
      });
    }
  },

  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
  },

  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  async onLogin() {
    const { username, password } = this.data;

    if (!username) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '登录中...'
    });

    try {
      console.log('========== 开始登录 ==========');
      const loginRes = await login(username, password);
      
      console.log('========== 登录响应 ==========');
      console.log('Login Response:', loginRes);

      if (loginRes && loginRes.code === 200) {
        if (loginRes.token) {
          wx.setStorageSync('token', loginRes.token);
          
          console.log('========== 开始获取用户信息 ==========');
          const infoRes = await getInfo();
          console.log('用户信息响应:', infoRes);
          
          let userInfo = {};
          if (infoRes && infoRes.data) {
            userInfo = infoRes.data;
          } else if (infoRes && infoRes.user) {
            userInfo = infoRes.user;
          }
          
          console.log('保存的用户信息:', userInfo);
          wx.setStorageSync('userInfo', userInfo);
          
          wx.hideLoading();
          wx.showToast({
            title: '登录成功',
            icon: 'success'
          });

          setTimeout(() => {
            wx.switchTab({
              url: '/pages/index/index'
            });
          }, 1000);
        } else {
          wx.hideLoading();
          wx.showToast({
            title: '登录成功但token为空',
            icon: 'none'
          });
        }
      } else {
        wx.hideLoading();
        wx.showToast({
          title: loginRes?.msg || '登录失败',
          icon: 'none'
        });
      }
    } catch (err) {
      wx.hideLoading();
      console.log('========== 登录异常 ==========');
      console.log('Error:', err);
    }
  },

  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },

  goToForgotPassword() {
    wx.navigateTo({
      url: '/pages/forgot-password/forgot-password'
    });
  }
});
