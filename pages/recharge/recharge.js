const request = require('../../utils/request.js');

Page({
  data: {
    balance: 0,
    balanceFormatted: '0.00',
    presetAmounts: [10, 30, 50, 100, 200],
    selectedAmount: 50,
    selectedAmountFormatted: '50.00',
    loading: false
  },

  onLoad() {
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

  selectPreset(e) {
    const amount = e.currentTarget.dataset.amount;
    this.setData({ 
      selectedAmount: amount,
      selectedAmountFormatted: amount.toFixed(2)
    });
  },

  async handleRecharge() {
    const amount = this.data.selectedAmount;
    
    if (amount <= 0) {
      wx.showToast({
        title: '请选择充值金额',
        icon: 'none'
      });
      return;
    }

    this.setData({ loading: true });

    try {
      await request({
        url: `/system/user/recharge/${amount}`,
        method: 'PUT'
      });

      wx.showToast({
        title: '充值成功',
        icon: 'success',
        duration: 2000
      });

      setTimeout(() => {
        this.loadBalance();
      }, 2000);

    } catch (err) {
      console.log('充值失败', err);
      wx.showToast({
        title: '充值失败，请重试',
        icon: 'none'
      });
    } finally {
      this.setData({ loading: false });
    }
  }
});