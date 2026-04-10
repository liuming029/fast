const request = require('../../utils/request.js');

Page({
  data: {
    orderList: []
  },

  onLoad() {
    console.log('管理员订单页面 onLoad');
    this.loadOrderList();
  },

  onShow() {
    console.log('管理员订单页面 onShow');
    this.loadOrderList();
  },

  async loadOrderList() {
    try {
      const res = await request({
        url: '/take/order/list',
        method: 'GET'
      });
      
      console.log('管理员订单返回数据:', res);
      
      if (res && res.rows) {
        const orderList = res.rows.map(order => {
          const statusText = order.status || '未知';
          let statusClass = '';
          if (statusText === '待接单') statusClass = 'status-pending';
          else if (statusText === '配送中') statusClass = 'status-delivering';
          else if (statusText === '已完成') statusClass = 'status-completed';
          else if (statusText === '已取消') statusClass = 'status-cancelled';

          return {
            orderId: order.orderId,
            orderNumber: order.orderId,
            status: statusText,
            statusText: statusText,
            statusClass: statusClass,
            pickupAddress: order.station || '快递站点',
            deliveryAddress: (order.building || '宿舍楼') + ' ' + (order.room || ''),
            createTime: order.createTime,
            price: order.totalPrice ? order.totalPrice.toFixed(2) : '0.00',
            riderCommission: order.riderCommission ? order.riderCommission.toFixed(2) : '0.00',
            platformCommission: order.platformCommission ? order.platformCommission.toFixed(2) : '0.00'
          };
        });

        this.setData({ orderList });
      }
    } catch (err) {
      console.log('获取订单列表失败', err);
    }
  },

  async deleteOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/order/${orderId}`,
              method: 'DELETE'
            });
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadOrderList();
          } catch (err) {
            console.log('删除订单失败', err);
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