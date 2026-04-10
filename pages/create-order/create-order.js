const request = require('../../utils/request.js');

Page({
  data: {
    loading: false,
    basePrice: 2.00,
    sizePrice: 0.00,
    totalPrice: 2.00,
    balance: 0,
    balanceFormatted: '0.00',
    defaultStationName: '',
    defaultBuildingName: '',
    formData: {
      stationId: '',
      code: '',
      sizeId: '',
      buildingId: '',
      room: '',
      totalPrice: 2.00,
      remark: ''
    },
    stationList: [],
    buildingList: [],
    sizeList: [],
    stationIndex: -1,
    buildingIndex: -1,
    selectedStation: null,
    selectedBuilding: null,
    selectedSizeId: ''
  },

  onLoad(options) {
    console.log('========== 发布订单页面加载 ==========');
    console.log('首页传递的参数:', options);
    
    this.defaultStationName = options.station ? decodeURIComponent(options.station) : '';
    this.defaultBuildingName = options.building ? decodeURIComponent(options.building) : '';
    
    console.log('解码后:', { station: this.defaultStationName, building: this.defaultBuildingName });
    
    this.loadStationList();
    this.loadSizeList();
    this.loadBuildingList();
    this.loadBalance();
  },

  onShow() {
    this.loadBalance();
  },

  async loadBalance() {
    try {
      const res = await request({
        url: '/system/user/selectMyBalance',
        method: 'GET'
      });
      let balance = 0;
      if (res && res.data !== undefined && res.data !== null) {
        balance = res.data;
      } else if (res !== undefined && res !== null && typeof res !== 'object') {
        balance = res;
      }
      this.setData({ 
        balance: balance,
        balanceFormatted: (parseFloat(balance) || 0).toFixed(2)
      });
    } catch (err) {
      console.log('获取余额失败', err);
    }
  },

  async loadStationList() {
    try {
      const res = await request({
        url: '/take/station/list',
        method: 'GET'
      });
      console.log('========== 快递站点列表 ==========');
      console.log('Response:', res);
      
      if (res && res.rows) {
        const stationList = res.rows;
        this.setData({
          stationList: stationList
        });
        
        if (this.defaultStationName) {
          const index = stationList.findIndex(s => s.name === this.defaultStationName);
          console.log('快递站点匹配:', this.defaultStationName, '索引:', index);
          if (index !== -1) {
            const station = stationList[index];
            this.setData({
              stationIndex: index,
              selectedStation: station,
              'formData.stationId': station.stationId
            });
          }
        }
      }
    } catch (err) {
      console.log('========== 获取快递站点失败 ==========');
      console.log('Error:', err);
    }
  },

  async loadSizeList() {
    try {
      const res = await request({
        url: '/take/size/list',
        method: 'GET'
      });
      console.log('========== 包裹规格列表 ==========');
      console.log('Response:', res);
      
      if (res && res.rows) {
        this.setData({
          sizeList: res.rows
        });
      }
    } catch (err) {
      console.log('========== 获取包裹规格失败 ==========');
      console.log('Error:', err);
    }
  },

  async loadBuildingList() {
    try {
      const res = await request({
        url: '/take/building/list',
        method: 'GET'
      });
      console.log('========== 宿舍楼列表 ==========');
      console.log('Response:', res);
      
      if (res && res.rows) {
        const buildingList = res.rows;
        this.setData({
          buildingList: buildingList
        });
        
        if (this.defaultBuildingName) {
          const index = buildingList.findIndex(b => b.name === this.defaultBuildingName);
          console.log('宿舍楼匹配:', this.defaultBuildingName, '索引:', index);
          if (index !== -1) {
            const building = buildingList[index];
            this.setData({
              buildingIndex: index,
              selectedBuilding: building,
              'formData.buildingId': building.buildingId
            });
          }
        }
      }
    } catch (err) {
      console.log('========== 获取宿舍楼失败 ==========');
      console.log('Error:', err);
    }
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [`formData.${field}`]: value
    });
  },

  onStationChange(e) {
    const index = e.detail.value;
    const station = this.data.stationList[index];
    this.setData({
      stationIndex: index,
      selectedStation: station,
      'formData.stationId': station.stationId
    });
  },
  goToRecharge() {
    console.log('点击充值');
    wx.navigateTo({
      url: '/pages/recharge/recharge'
    });
  },

  onBuildingChange(e) {
    const index = e.detail.value;
    const building = this.data.buildingList[index];
    this.setData({
      buildingIndex: index,
      selectedBuilding: building,
      'formData.buildingId': building.buildingId
    });
  },

  selectSize(e) {
    const sizeId = e.currentTarget.dataset.id;
    const size = this.data.sizeList.find(s => s.sizeId === sizeId);
    const sizePrice = size ? parseFloat(size.price) : 0;
    const totalPrice = (this.data.basePrice + sizePrice).toFixed(2);
    
    this.setData({
      selectedSizeId: sizeId,
      sizePrice: sizePrice.toFixed(2),
      totalPrice: totalPrice,
      'formData.sizeId': sizeId,
      'formData.totalPrice': totalPrice
    });
  },

  async submitOrder() {
    const { formData, totalPrice, balance } = this.data;
  
    if (!formData.stationId) {
      wx.showToast({ title: '请选择快递站点', icon: 'none' });
      return;
    }
    if (!formData.code) {
      wx.showToast({ title: '请输入取件码', icon: 'none' });
      return;
    }
    if (!formData.sizeId) {
      wx.showToast({ title: '请选择包裹规格', icon: 'none' });
      return;
    }
    if (!formData.buildingId) {
      wx.showToast({ title: '请选择宿舍楼', icon: 'none' });
      return;
    }
    if (!formData.room) {
      wx.showToast({ title: '请输入寝室号/联系人', icon: 'none' });
      return;
    }
  
    const price = parseFloat(totalPrice);
    if (balance < price) {
      wx.showModal({
        title: '余额不足',
        content: `当前余额：¥${this.data.balanceFormatted}\n订单金额：¥${price.toFixed(2)}\n请先充值后再下单`,
        confirmText: '去充值',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/recharge/recharge'
            });
          }
        }
      });
      return;
    }
    
  
    wx.showModal({
      title: '确认下单',
      content: `预估总价：¥${totalPrice}`,
      success: async (res) => {
        if (res.confirm) {
          this.setData({ loading: true });
          try {
            await request({
              url: '/take/order',
              method: 'POST',
              data: {
                stationId: formData.stationId,
                code: formData.code,
                sizeId: formData.sizeId,
                buildingId: formData.buildingId,
                room: formData.room,
                totalPrice: parseFloat(formData.totalPrice),
                remark: formData.remark
              }
            });
            
            wx.showToast({
              title: '下单成功',
              icon: 'success',
              duration: 1500
            });
            
            setTimeout(() => {
              wx.switchTab({
                url: '/pages/order/order'
              });
            }, 1500);
          } catch (err) {
            console.log('下单失败', err);
            wx.showToast({
              title: err.message || '下单失败，请重试',
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