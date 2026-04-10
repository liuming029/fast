const request = require('../../utils/request.js');

Page({
  data: {
    currentTab: 0,
    orderList: []
  },

  onLoad() {
    console.log('========== 骑手工作台加载 ==========');
    this.loadOrderList();
  },

  onShow() {
    this.loadOrderList();
  },

  switchTab(e) {
    const index = e.currentTarget.dataset.index;
    const numIndex = parseInt(index);
    this.setData({
      currentTab: numIndex
    });
    this.loadOrderList();
  },

  async loadOrderList() {
    try {
      let url = '';
      let status = '';
      
      if (this.data.currentTab === 0) {
        url = '/take/order/list';
        status = '待接单';
      } else if (this.data.currentTab === 1) {
        url = '/take/order/selectOrderListByRiderToUserId';
        status = '配送中';
      } else {
        url = '/take/order/selectOrderListByRiderToUserId';
        status = '已完成';
      }

      console.log('========== 加载订单列表 ==========');
      console.log('URL:', url);
      console.log('Status:', status);

      const res = await request({
        url: url,
        method: 'GET'
      });

      console.log('订单列表响应:', res);

      let orderList = [];

      if (res && res.rows && res.rows.length > 0) {
        orderList = res.rows.map(order => {
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
            price: order.totalPrice ? order.totalPrice.toFixed(2) : '0.00'
          };
        });

        if (this.data.currentTab === 0) {
          orderList = orderList.filter(o => o.statusText === '待接单');
        } else if (this.data.currentTab === 1) {
          orderList = orderList.filter(o => o.statusText === '配送中');
        } else if (this.data.currentTab === 2) {
          orderList = orderList.filter(o => o.statusText === '已完成');
        }
      }

      console.log('处理后的订单列表:', orderList);

      this.setData({
        orderList: orderList
      });
    } catch (err) {
      console.log('========== 获取订单列表失败 ==========');
      console.log('Error:', err);
      this.setData({
        orderList: []
      });
    }
  },

  viewOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id;
    const order = this.data.orderList.find(o => o.orderId === orderId);
    
    if (order) {
      wx.showModal({
        title: '订单详情',
        content: `订单号：${order.orderNumber}\n状态：${order.statusText}\n取件地址：${order.pickupAddress}\n送件地址：${order.deliveryAddress}\n配送费：¥${order.price}`,
        showCancel: false
      });
    }
  },

  async acceptOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    console.log('接单，订单ID:', orderId);

    wx.showModal({
      title: '提示',
      content: '确定要接这个订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/order/accept/${orderId}`,
              method: 'PUT'
            });
            wx.showToast({
              title: '接单成功',
              icon: 'success'
            });
            this.loadOrderList();
          } catch (err) {
            console.log('接单失败', err);
            wx.showToast({
              title: '接单失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  async receiveOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    console.log('确认送达，订单ID:', orderId);

    wx.showModal({
      title: '提示',
      content: '确定已送达吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/order/receive/${orderId}`,
              method: 'PUT'
            });
            wx.showToast({
              title: '确认送达成功',
              icon: 'success'
            });
            this.loadOrderList();
          } catch (err) {
            console.log('确认送达失败', err);
            wx.showToast({
              title: '确认送达失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
  }
});