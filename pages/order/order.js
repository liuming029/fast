const request = require('../../utils/request.js');

Page({
  data: {
    currentTab: 0,
    orderList: [],
    isRider: false,
    riderStatus: '无'
  },

  onLoad() {
    console.log('========== 订单页面加载 ==========');
    this.checkRiderStatus();
  },

  onShow() {
    this.checkRiderStatus();
  },

  async checkRiderStatus() {
    try {
      console.log('========== 检查骑手状态 ==========');
      const res = await request({
        url: '/take/rider/selectIsAuthStatus',
        method: 'GET'
      });
      console.log('骑手状态响应:', res);
      
      let status = '无';
      if (res && res.msg !== undefined && res.msg !== null) {
        status = res.msg;
      } else if (res && res.data !== undefined && res.data !== null) {
        status = res.data;
      } else if (res !== undefined && res !== null && typeof res === 'string') {
        status = res;
      }
      
      console.log('骑手认证状态:', status);
      
      const isRider = status === '已通过';
      
      this.setData({ 
        riderStatus: status,
        isRider: isRider,
        currentTab: 0
      });
      
      this.loadOrderList();
      
    } catch (err) {
      console.log('检查骑手状态失败', err);
      this.setData({ 
        isRider: false,
        currentTab: 0
      });
      this.loadOrderList();
    }
  },

  switchTab(e) {
    const index = e.currentTarget.dataset.index;
    console.log('========== 切换标签 ==========');
    console.log('切换到:', index);
    console.log('index类型:', typeof index);
    const numIndex = parseInt(index);
    console.log('转成数字后:', numIndex);
    this.setData({
      currentTab: numIndex
    });
    this.loadOrderList();
  },

  async loadOrderList() {
    try {
      console.log('========== 开始加载订单 ==========');
      console.log('isRider:', this.data.isRider);
      console.log('currentTab:', this.data.currentTab);
      console.log('currentTab类型:', typeof this.data.currentTab);
      
      const userInfo = wx.getStorageSync('userInfo') || {};
      const isAdmin = userInfo.userName === 'admin' || userInfo.role === 'admin';
      console.log('isAdmin:', isAdmin);
      
      const tab = parseInt(this.data.currentTab);
      console.log('转成数字后的tab:', tab);
      
      let url = '';
      let filterStatus = '';
      
      if (isAdmin) {
        console.log('=== 进入管理员分支 ===');
        url = '/take/order/list';
        if (tab === 0) {
          filterStatus = '';
        } else if (tab === 1) {
          filterStatus = '待接单';
        } else if (tab === 2) {
          filterStatus = '配送中';
        } else if (tab === 3) {
          filterStatus = '已完成';
        }
      } else if (this.data.isRider === true) {
        console.log('=== 进入骑手分支 ===');
        if (tab === 0) {
          console.log('=== tab 0 ===');
          url = '/take/order/list';
          filterStatus = '待接单';
        } else if (tab === 1) {
          console.log('=== tab 1 ===');
          url = '/take/order/selectOrderListByRiderToUserId';
          filterStatus = '配送中';
        } else if (tab === 2) {
          console.log('=== tab 2 ===');
          url = '/take/order/selectOrderListByRiderToUserId';
          filterStatus = '已完成';
        } else {
          console.log('=== 其他tab，默认tab 0 ===');
          url = '/take/order/list';
          filterStatus = '待接单';
        }
      } else {
        console.log('=== 进入非骑手分支 ===');
        url = '/take/order/selectMyOrderList';
        if (tab === 0) {
          filterStatus = '';
        } else if (tab === 1) {
          filterStatus = '待接单';
        } else if (tab === 2) {
          filterStatus = '配送中';
        } else if (tab === 3) {
          filterStatus = '已完成';
        }
      }

      console.log('=== URL赋值完成 ===');
      console.log('最终URL:', url);
      console.log('过滤状态:', filterStatus);

      if (!url || url === '') {
        console.log('=== URL仍然为空，强制设置 ===');
        url = '/take/order/list';
        filterStatus = '待接单';
        console.log('强制设置后的URL:', url);
      }

      console.log('开始请求:', url);
      const res = await request({
        url: url,
        method: 'GET'
      });

      console.log('订单列表响应:', res);

      let orderList = [];

      if (res && res.rows && Array.isArray(res.rows) && res.rows.length > 0) {
        console.log('有订单数据，开始处理');
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

        if (filterStatus && filterStatus !== '') {
          console.log('过滤前订单数:', orderList.length);
          orderList = orderList.filter(o => o.statusText === filterStatus);
          console.log('过滤后订单数:', orderList.length);
        }
      } else {
        console.log('没有订单数据');
      }

      console.log('最终订单列表:', orderList);

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

  async cancelOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '提示',
      content: '确定要取消这个订单吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/order/cancelOrder/${orderId}`,
              method: 'PUT'
            });
            wx.showToast({
              title: '订单已取消',
              icon: 'success'
            });
            this.loadOrderList();
          } catch (err) {
            console.log('取消订单失败', err);
            wx.showToast({
              title: '取消失败，请重试',
              icon: 'none'
            });
          }
        }
      }
    });
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
            this.setData({
              currentTab: 1
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
            this.setData({
              currentTab: 2
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