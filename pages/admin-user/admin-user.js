const request = require('../../utils/request.js');

Page({
  data: {
    userList: []
  },

  onLoad() {
    this.loadUserList();
  },

  onShow() {
    this.loadUserList();
  },

  async loadUserList() {
    try {
      const res = await request({
        url: '/system/user/list',
        method: 'GET'
      });
      
      console.log('用户管理返回数据:', res);
      
      if (res && res.rows) {
        const userList = res.rows.map(user => ({
          userId: user.userId,
          userName: user.userName,
          nickName: user.nickName || user.userName,
          balance: user.balance ? user.balance.toFixed(2) : '0.00',
          phoneNumber: user.phonenumber || '未填写',
          createTime: user.createTime
        }));

        this.setData({ userList });
      }
    } catch (err) {
      console.log('获取用户列表失败', err);
    }
  },

  async deleteUser(e) {
    const userId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个用户吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/system/user/${userId}`,
              method: 'DELETE'
            });
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadUserList();
          } catch (err) {
            console.log('删除用户失败', err);
            wx.showToast({
              title: '删除失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  }
});