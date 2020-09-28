const REQUEST = require('../../utils/request');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    statusType: [{
        status: 0,
        label: '今日'
      },
      {
        status: 3,
        label: '三天'
      },
      {
        status: 7,
        label: '一周'
      },
      {
        status: 999,
        label: '全部'
      },
    ],
    // 当前选中的 列表值
    currentTab: 0,
  },
  // 按钮切换的操作。
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current,
      })
      // 此处改成动态获取数据


    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function (options) {
    this.getData(this.data.currentTab);
  },
  getData(status) {
    let that = this;
    wx.showLoading({
      title: '加载中',
    })
    // 根据传入的状态码，去后台获取订单列表
    REQUEST.request('order/getMyOrderList', 'POST', {
      token: wx.getStorageSync('token'),
      status,
      page:0,
      size:10
    }).then(res => {
      
      if(res.data.code === 20000) {
        that.setData({
          orderInfo : res.data.data
        })
      }else {
        wx.showModal({
          content: res.data.msg,
          showCancel: false
        })
      }
    })
    wx.hideLoading();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },
})