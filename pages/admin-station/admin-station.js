const request = require('../../utils/request.js');

Page({
  data: {
    stationList: [],
    showAddModal: false,
    showEditModal: false,
    currentStation: {},
    addForm: {
      name: ''
    },
    editForm: {
      stationId: '',
      name: ''
    }
  },

  onLoad() {
    this.loadStationList();
  },

  onShow() {
    this.loadStationList();
  },

  async loadStationList() {
    try {
      const res = await request({
        url: '/take/station/list',
        method: 'GET'
      });
      
      console.log('快递站点返回数据:', res);
      
      if (res && res.rows) {
        this.setData({
          stationList: res.rows
        });
      } else if (res && Array.isArray(res)) {
        this.setData({
          stationList: res
        });
      }
    } catch (err) {
      console.log('获取快递站点列表失败', err);
    }
  },

  showAddModal() {
    this.setData({
      showAddModal: true,
      addForm: {
        name: '',
        sort: 0
      }
    });
  },

  hideAddModal() {
    this.setData({
      showAddModal: false
    });
  },

  onAddNameInput(e) {
    this.setData({
      'addForm.name': e.detail.value
    });
  },

  async addStation() {
    const { addForm } = this.data;
    
    if (!addForm.name) {
      wx.showToast({
        title: '请输入站点名称',
        icon: 'none'
      });
      return;
    }
    
    try {
      await request({
        url: '/take/station',
        method: 'POST',
        data: addForm
      });
      
      wx.showToast({
        title: '添加成功',
        icon: 'success'
      });
      
      this.hideAddModal();
      this.loadStationList();
    } catch (err) {
      console.log('添加快递站点失败', err);
      wx.showToast({
        title: '添加失败',
        icon: 'none'
      });
    }
  },

  showEditModal(e) {
    const station = e.currentTarget.dataset.station;
    this.setData({
      showEditModal: true,
      currentStation: station,
      editForm: {
        stationId: station.stationId,
        name: station.name,
        sort: station.sort || 0
      }
    });
  },

  hideEditModal() {
    this.setData({
      showEditModal: false
    });
  },

  onEditNameInput(e) {
    this.setData({
      'editForm.name': e.detail.value
    });
  },

  async editStation() {
    const { editForm } = this.data;
    
    if (!editForm.name) {
      wx.showToast({
        title: '请输入站点名称',
        icon: 'none'
      });
      return;
    }
    
    try {
      await request({
        url: '/take/station',
        method: 'PUT',
        data: editForm
      });
      
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
      
      this.hideEditModal();
      this.loadStationList();
    } catch (err) {
      console.log('修改快递站点失败', err);
      wx.showToast({
        title: '修改失败',
        icon: 'none'
      });
    }
  },

  deleteStation(e) {
    const stationId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个站点吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/station/${stationId}`,
              method: 'DELETE'
            });
            
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            
            this.loadStationList();
          } catch (err) {
            console.log('删除快递站点失败', err);
            wx.showToast({
              title: '删除失败',
              icon: 'none'
            });
          }
        }
      }
    });
  }
});
