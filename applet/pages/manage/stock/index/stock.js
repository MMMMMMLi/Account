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
    addNumber: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  // --------------------------------首页加载
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
    REQUEST.request('manage/insertStock', 'POST', {
      stockKey: data.keysValue,
      category: data.categoryArray[data.categoryValue],
      number: data.addNumber,
      token: wx.getStorageSync('token'),
    }).then(res => {

    })

  },
  // --------------------------------库存校准
  // --------------------------------框信息
})