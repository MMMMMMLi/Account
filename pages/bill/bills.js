// 获取当前程序APP
const app = getApp();
const mock = require('../../utils/mock');
const { mockData } = require('../../utils/mock');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 用户信息相关
    userInfo: {},
    hasUserInfo: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    // tab
    tabs: [],
    activeTab: 0,
    goodsList:{},
    orderList:[]
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    mock.mockData().then(mockData => {
      this.setData({
        orderList : mockData.orderList,
        goodsList : mockData.goodsList
      })
    });
    const titles = ['当日订单', '三天内订单', '七天内订单', '全部订单']
    const tabs = titles.map(item => ({
      title: item
    }))
    this.setData({
      tabs
    })
  },
  onTabCLick(e) {
    const index = e.detail.index
    this.setData({
      activeTab: index
    })
  },

  onChange(e) {
    const index = e.detail.index
    this.setData({
      activeTab: index
    })
  },
  /**
   * 获取用户信息的回调函数
   */
  processLogin(info) {
    console.log(info)
    app.globalData.userInfo = info.detail.userInfo
    this.setData({
      userInfo: info.detail.userInfo,
      hasUserInfo: true
    })
  },
  /**
   * 暂不登录
   */
  cancelLogin() {
    this.setData({
      hasUserInfo: true
    })
  }
})