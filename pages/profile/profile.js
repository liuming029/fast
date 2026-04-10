const { request } = require('../../utils/request.js');

Page({
  data: {
    userInfo: {},
    avatar: '',
    editNickname: '',
    editPhone: '',
    showEditModal: false
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo') || {};
    const avatar = wx.getStorageSync('userAvatar') || '';
    this.setData({
      userInfo: userInfo,
      avatar: avatar
    });
  },

  chooseAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        this.setData({
          avatar: tempFilePath
        });
        wx.setStorageSync('userAvatar', tempFilePath);
        
        const pages = getCurrentPages();
        if (pages.length >= 2) {
          const prevPage = pages[pages.length - 2];
          if (prevPage && prevPage.route === 'pages/my/my') {
            prevPage.setData({
              avatar: tempFilePath
            });
          }
        }
        
        wx.showToast({
          title: '头像更换成功',
          icon: 'success'
        });
      }
    });
  },

  showEdit() {
    const { userInfo } = this.data;
    this.setData({
      editNickname: userInfo.nickname || '',
      editPhone: userInfo.phone || '',
      showEditModal: true
    });
  },

  hideEdit() {
    this.setData({
      showEditModal: false
    });
  },

  onNicknameInput(e) {
    this.setData({
      editNickname: e.detail.value
    });
  },

  onPhoneInput(e) {
    this.setData({
      editPhone: e.detail.value
    });
  },

  async saveEdit() {
    const { editNickname, editPhone } = this.data;
    
    if (!editNickname) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      });
      return;
    }
    
    try {
      await request({
        url: '/user/update',
        method: 'PUT',
        data: {
          nickname: editNickname,
          phone: editPhone
        }
      });
      
      let userInfo = wx.getStorageSync('userInfo') || {};
      userInfo.nickname = editNickname;
      userInfo.phone = editPhone;
      wx.setStorageSync('userInfo', userInfo);
      
      this.setData({
        userInfo: userInfo,
        showEditModal: false
      });
      
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
    } catch (err) {
      console.log('修改失败', err);
      wx.showToast({
        title: '修改失败',
        icon: 'none'
      });
    }
  }
});
