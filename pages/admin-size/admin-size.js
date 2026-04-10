const request = require('../../utils/request.js');

Page({
  data: {
    sizeList: [],
    showAddModal: false,
    showEditModal: false,
    currentSize: {},
    addForm: {
      name: '',
      price: ''
    },
    editForm: {
      sizeId: '',
      name: '',
      price: ''
    }
  },

  onLoad() {
    this.loadSizeList();
  },

  onShow() {
    this.loadSizeList();
  },

  async loadSizeList() {
    try {
      const res = await request({
        url: '/take/size/list',
        method: 'GET'
      });
      
      console.log('包裹规格返回数据:', res);
      
      if (res && res.rows) {
        console.log('包裹规格 rows:', res.rows);
        this.setData({
          sizeList: res.rows
        });
        console.log('包裹规格 sizeList 长度:', res.rows.length);
      } else if (res && Array.isArray(res)) {
        console.log('包裹规格 数组:', res);
        this.setData({
          sizeList: res
        });
        console.log('包裹规格 sizeList 长度:', res.length);
      }
    } catch (err) {
      console.log('获取包裹规格列表失败', err);
    }
  },

  showAddModal() {
    this.setData({
      showAddModal: true,
      addForm: {
        name: '',
        price: '',
        description: '无'
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

  onAddPriceInput(e) {
    this.setData({
      'addForm.price': e.detail.value
    });
  },

  async addSize() {
    const { addForm } = this.data;
    
    console.log('准备新增包裹规格，addForm:', addForm);
    
    if (!addForm.name) {
      wx.showToast({
        title: '请输入规格名称',
        icon: 'none'
      });
      return;
    }
    
    if (!addForm.price) {
      wx.showToast({
        title: '请输入价格',
        icon: 'none'
      });
      return;
    }
    
    try {
      const res = await request({
        url: '/take/size',
        method: 'POST',
        data: addForm
      });
      
      console.log('新增包裹规格返回数据:', res);
      
      wx.showToast({
        title: '添加成功',
        icon: 'success'
      });
      
      this.hideAddModal();
      this.loadSizeList();
    } catch (err) {
      console.log('添加包裹规格失败', err);
      wx.showToast({
        title: '添加失败',
        icon: 'none'
      });
    }
  },

  showEditModal(e) {
    const size = e.currentTarget.dataset.size;
    this.setData({
      showEditModal: true,
      currentSize: size,
      editForm: {
        sizeId: size.sizeId,
        name: size.name,
        price: size.price,
        description: size.description || ''
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

  onEditPriceInput(e) {
    this.setData({
      'editForm.price': e.detail.value
    });
  },

  async editSize() {
    const { editForm } = this.data;
    
    if (!editForm.name) {
      wx.showToast({
        title: '请输入规格名称',
        icon: 'none'
      });
      return;
    }
    
    if (!editForm.price) {
      wx.showToast({
        title: '请输入价格',
        icon: 'none'
      });
      return;
    }
    
    try {
      await request({
        url: '/take/size',
        method: 'PUT',
        data: editForm
      });
      
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
      
      this.hideEditModal();
      this.loadSizeList();
    } catch (err) {
      console.log('修改包裹规格失败', err);
      wx.showToast({
        title: '修改失败',
        icon: 'none'
      });
    }
  },

  deleteSize(e) {
    const sizeId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个规格吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await request({
              url: `/take/size/${sizeId}`,
              method: 'DELETE'
            });
            
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            
            this.loadSizeList();
          } catch (err) {
            console.log('删除包裹规格失败', err);
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
