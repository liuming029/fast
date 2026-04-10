const request = require('../../utils/request.js');

Page({
  data: {
    buildingList: [],
    showAddModal: false,
    showEditModal: false,
    currentBuilding: {},
    addForm: {
      name: ''
    },
    editForm: {
      buildingId: '',
      name: ''
    }
  },

  onLoad() {
    this.loadBuildingList();
  },

  onShow() {
    this.loadBuildingList();
  },

  async loadBuildingList() {
    try {
      const res = await request({
        url: '/take/building/list',
        method: 'GET'
      });
      
      console.log('宿舍楼返回数据:', res);
      
      if (res && res.rows) {
        this.setData({
          buildingList: res.rows
        });
      } else if (res && Array.isArray(res)) {
        this.setData({
          buildingList: res
        });
      }
    } catch (err) {
      console.log('获取宿舍楼列表失败', err);
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

  async addBuilding() {
    const { addForm } = this.data;
    
    console.log('准备新增宿舍楼，addForm:', addForm);
    
    if (!addForm.name) {
      wx.showToast({
        title: '请输入宿舍楼名称',
        icon: 'none'
      });
      return;
    }
    
    try {
      const res = await request({
        url: '/take/building',
        method: 'POST',
        data: addForm
      });
      
      console.log('新增宿舍楼返回数据:', res);
      
      wx.showToast({
        title: '添加成功',
        icon: 'success'
      });
      
      this.hideAddModal();
      this.loadBuildingList();
    } catch (err) {
      console.log('添加宿舍楼失败', err);
      wx.showToast({
        title: '添加失败',
        icon: 'none'
      });
    }
  },

  showEditModal(e) {
    const building = e.currentTarget.dataset.building;
    this.setData({
      showEditModal: true,
      currentBuilding: building,
      editForm: {
        buildingId: building.buildingId,
        name: building.name,
        sort: building.sort || 0
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

  async editBuilding() {
    const { editForm } = this.data;
    
    if (!editForm.name) {
      wx.showToast({
        title: '请输入宿舍楼名称',
        icon: 'none'
      });
      return;
    }
    
    try {
      await request({
        url: '/take/building',
        method: 'PUT',
        data: editForm
      });
      
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
      
      this.hideEditModal();
      this.loadBuildingList();
    } catch (err) {
      console.log('修改宿舍楼失败', err);
      wx.showToast({
        title: '修改失败',
        icon: 'none'
      });
    }
  },

  deleteBuilding(e) {
    const buildingId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个宿舍楼吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/building/${buildingId}`,
              method: 'DELETE'
            });
            
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            
            this.loadBuildingList();
          } catch (err) {
            console.log('删除宿舍楼失败', err);
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
