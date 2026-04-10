Page({
  data: {
    username: '',
    password: '',
    confirmPassword: ''
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

  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  onResetPassword() {
    const { username, password, confirmPassword } = this.data;

    if (!username) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!password) {
      wx.showToast({
        title: '请输入新密码',
        icon: 'none'
      });
      return;
    }

    if (!confirmPassword) {
      wx.showToast({
        title: '请再次输入新密码',
        icon: 'none'
      });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({
        title: '两次密码输入不一致',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '重置中...'
    });

    wx.request({
      url: 'http://localhost:8080/system/user/forgotPwd',
      method: 'POST',
      data: {
        userName: username,
        password: password
      },
      success: (res) => {
        wx.hideLoading();
        console.log('重置密码响应:', res);
        
        if (res.statusCode === 200 && res.data.code === 200) {
          wx.showToast({
            title: '重置成功',
            icon: 'success'
          });
          
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({
            title: res.data.msg || '重置失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        console.log('重置密码失败', err);
        wx.showToast({
          title: '重置失败',
          icon: 'none'
        });
      }
    });
  },

  goToLogin() {
    wx.navigateBack();
  }
});