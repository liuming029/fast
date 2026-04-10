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

  onRegister() {
    const { username, password, confirmPassword } = this.data;

    if (!username || !password || !confirmPassword) {
      wx.showToast({
        title: '请填写完整信息',
        icon: 'none'
      });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({
        title: '两次密码不一致',
        icon: 'none'
      });
      return;
    }

    if (username.length < 2 || username.length > 20) {
      wx.showToast({
        title: '用户名长度2-20位',
        icon: 'none'
      });
      return;
    }

    if (password.length < 5 || password.length > 20) {
      wx.showToast({
        title: '密码长度5-20位',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '注册中...' });
    
    wx.request({
      url: 'http://172.20.10.6:8080/register',
      method: 'POST',
      data: {
        username: username,
        password: password
      },
      header: {
        'Content-Type': 'application/json'
      },
      success: (res) => {
        wx.hideLoading();
        
        if (res.statusCode === 200 && res.data && res.data.code === 200) {
          wx.showToast({
            title: '注册成功',
            icon: 'success'
          });
          setTimeout(() => {
            wx.redirectTo({
              url: '/pages/login-form/login-form'
            });
          }, 1500);
        } else {
          wx.showToast({
            title: res.data?.msg || '注册失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '注册失败',
          icon: 'none'
        });
      }
    });
  },

  goToLogin() {
    wx.redirectTo({
      url: '/pages/login-form/login-form'
    });
  }
})
