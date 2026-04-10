const request = require('../../utils/request.js');

Page({
  data: {
    riderList: []
  },

  onLoad() {
    this.loadRiderList();
  },

  onShow() {
    this.loadRiderList();
  },

  async loadRiderList() {
    try {
      const res = await request({
        url: '/take/rider/list',
        method: 'GET'
      });
      
      if (res && res.rows) {
        const riderList = res.rows.map(rider => {
          const statusText = rider.status || '未知';
          let statusClass = '';
          if (statusText === '待审核') statusClass = 'status-pending';
          else if (statusText === '已通过') statusClass = 'status-approved';
          else if (statusText === '已拒绝') statusClass = 'status-rejected';

          return {
            riderId: rider.riderId,
            name: rider.name,
            idCard: rider.idCard,
            phone: rider.phone,
            status: statusText,
            statusText: statusText,
            statusClass: statusClass,
            createTime: rider.createTime
          };
        });

        this.setData({ riderList });
      }
    } catch (err) {
      console.log('获取骑手列表失败', err);
    }
  },

  async approveRider(e) {
    const riderId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要通过这个骑手的认证吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: '/take/rider',
              method: 'PUT',
              data: {
                riderId: riderId,
                status: '已通过'
              }
            });
            wx.showToast({
              title: '审核通过',
              icon: 'success'
            });
            this.loadRiderList();
          } catch (err) {
            console.log('审核失败', err);
            wx.showToast({
              title: '审核失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  async rejectRider(e) {
    const riderId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要拒绝这个骑手的认证吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: '/take/rider',
              method: 'PUT',
              data: {
                riderId: riderId,
                status: '已拒绝'
              }
            });
            wx.showToast({
              title: '已拒绝',
              icon: 'success'
            });
            this.loadRiderList();
          } catch (err) {
            console.log('拒绝失败', err);
            wx.showToast({
              title: '操作失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  async deleteRider(e) {
    const riderId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个骑手吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/rider/${riderId}`,
              method: 'DELETE'
            });
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadRiderList();
          } catch (err) {
            console.log('删除骑手失败', err);
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