Page({
  data: {
    version: '1.0.0'
  },

  onLoad() {
    
  },

  goToFeedback() {
    wx.showToast({
      title: '感谢您的反馈！',
      icon: 'none'
    });
  },

  goToPrivacy() {
    wx.showToast({
      title: '隐私政策',
      icon: 'none'
    });
  },

  goToAgreement() {
    wx.showToast({
      title: '用户协议',
      icon: 'none'
    });
  },

  makePhoneCall() {
    wx.makePhoneCall({
      phoneNumber: '400-123-4567'
    });
  }
});
