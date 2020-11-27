const REQUEST = require('../../../../utils/request');
const APP = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 添加库存相关参数
    addStockFlag: true,
    addStockKeys: ['货', '框'],
    keysValue: 0,
    categoryValue: 0,
    addNumber: '',

    // 校准库存
    alignStockFlag: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 获取数据
    this.getStockInfo()
  },
  // --------------------------------首页加载
  getStockInfo() {
    let that = this;
    REQUEST.request('stock/getStockInfo', 'POST', {
      stockKey: 0,
      token: wx.getStorageSync('token'),
      pageNum: 0,
      pageSize: 8,
    }).then(res => {
      if (res.data.code === 20000) {
        that.setData({
          ...res.data.data
        })
      } else {
        wx.showModal({
          content: res.data.msg,
          showCancel: false,
        })
      }
    })
  },
  // --------------------------------新增库存
  // 打开新增库存页面
  addStock() {
    // 获取品种类型
    this.getServerData();
    this.setData({
      addStockFlag: false
    })
  },
  // 获取一些参数
  getServerData() {
    let that = this;
    // 动态获取一下地瓜品种和大小规格
    REQUEST.request('applet/getConstant', 'POST', {}).then(res => {
      if (res.data.code == 20000) {
        this.setData({
          categoryArray: res.data.data.category,
        })
      } else {
        wx.showModal({
          title: '系统异常，点击重试！',
          success(res) {
            if (res.confirm) {
              that.getServerData()
            }
          }
        })
      }
    })
  },
  // 选择新增类型key
  bindPickerChange(e) {
    this.setData({
      keysValue: e.detail.value
    })
  },
  // 选择品种类型key
  bindPickerCategoryChange(e) {
    this.setData({
      categoryValue: e.detail.value
    })
  },
  // 输入框失去焦点触发
  addNumberInput(e) {
    this.setData({
      addNumber: e.detail.value
    })
  },
  // 关闭新增页面
  cancelAdd() {
    this.setData({
      addStockFlag: true,
      keysValue: 0,
      categoryValue: 0,
      addNumber: ''
    })
  },
  // 添加库存
  processAdd() {
    let that = this;
    let data = this.data;
    if (!data.addNumber) {
      wx.showModal({
        content: '请输入正确数量！',
        showCancel: false
      })
      return;
    }
    REQUEST.request('stock/insertStock', 'POST', {
      stockKey: data.keysValue,
      category: data.categoryArray[data.categoryValue],
      number: data.addNumber,
      token: wx.getStorageSync('token'),
    }).then(res => {
      if (!(res.data.code === 20000)) {
        wx.showModal({
          content: '添加失败！请稍候重试。',
          showCancel: false
        })
      }
      // 关闭新增页面
      that.cancelAdd();
      // 刷新库存信息
      that.getStockInfo();
    })

  },
  // --------------------------------库存校准
  // 打开库存校准
  alignStock() {
    this.setData({
      alignStockFlag: false
    })
  },
  // 输入库存校准信息
  alignNumberInput(e) {
    let category = e.currentTarget.dataset.key;
    let number = e.detail.value;
    // 获取校准过的库存
    let alignNumberList = this.data.alignNumberList || [];
    let alignNumberListTemp = alignNumberList.filter(info => info.category == category);
    // 判断是否更改过
    if (alignNumberListTemp.length == 0) {
      alignNumberList.push({
        category,
        number
      })
    } else {
      alignNumberList.forEach(info => {
        info.number = number;
      })
    }
    this.setData({
      alignNumberList
    })
  },
  // 保存校准信息
  processalign() {
    let that = this;
    let list = that.data.alignNumberList || [];
    if (list.length <= 0) {
      wx.showModal({
        content: '请修改需要校准的库存！',
        showCancel: false
      })
      return;
    }
    REQUEST.request('stock/alignStockInfo', 'POST', {
      jsonList: JSON.stringify(list),
      token: wx.getStorageSync('token'),
    }).then(res => {
      console.log(res)
    })
  },
  // 关闭库存校准
  cancelalign() {
    this.setData({
      alignStockFlag: true,
      alignNumberList: []
    })
  }
  // --------------------------------框信息
})