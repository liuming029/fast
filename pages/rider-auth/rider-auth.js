const request = require('../../utils/request.js');

Page({
  data: {
    loading: false,
    formData: {
      realName: '',
      idCard: '',
      phone: '',
      studentId: ''
    }
  },

  onLoad() {
    this.checkAuthStatus();
  },

  async checkAuthStatus() {
    try {
      const res = await request({
        url: '/take/rider/selectIsAuthStatus',
        method: 'GET'
      });
      console.log('认证状态:', res);
      
      let status = '无';
      if (res && res.msg !== undefined && res.msg !== null) {
        status = res.msg;
      } else if (res && res.data !== undefined && res.data !== null) {
        status = res.data;
      } else if (res !== undefined && res !== null && typeof res === 'string') {
        status = res;
      }
      
      if (status && status !== '无') {
        wx.showModal({
          title: '提示',
          content: `您的认证状态：${status}\n${status === '已通过' ? '可以开始接单了！' : '请等待审核'}`,
          showCancel: false,
          success: () => {
            if (status === '已通过') {
              wx.switchTab({
                url: '/pages/index/index'
              });
            }
          }
        });
      }
    } catch (err) {
      console.log('获取认证状态失败', err);
    }
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  async submitAuth() {
    const { formData } = this.data;

    if (!formData.realName) {
      wx.showToast({ title: '请输入真实姓名', icon: 'none' });
      return;
    }
    if (!formData.idCard) {
      wx.showToast({ title: '请输入身份证号', icon: 'none' });
      return;
    }
    if (!formData.phone) {
      wx.showToast({ title: '请输入手机号码', icon: 'none' });
      return;
    }
    if (!formData.studentId) {
      wx.showToast({ title: '请输入学号', icon: 'none' });
      return;
    }

    wx.showModal({
      title: '确认提交',
      content: '确认提交认证信息吗？提交后请等待审核',
      success: async (res) => {
        if (res.confirm) {
          this.setData({ loading: true });
          try {
            await request({
              url: '/take/rider',
              method: 'POST',
              data: formData
            });
            
            wx.showToast({
              title: '提交成功',
              icon: 'success',
              duration: 2000
            });
            
            setTimeout(() => {
              wx.navigateBack();
            }, 2000);
          } catch (err) {
            console.log('提交认证失败', err);
            wx.showToast({
              title: err.message || '提交失败，请重试',
              icon: 'none'
            });
          } finally {
            this.setData({ loading: false });
          }
        }
      }
    });
  }
});