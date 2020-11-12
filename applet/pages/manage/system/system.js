const REQUEST = require('../../../utils/request');

// pages/manage/system/system.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getSysInfo();
  },
  getSysInfo() {
    let that =this;
    REQUEST.request('manage/getSysInfo', 'POST', {
      token: wx.getStorageSync('token'),
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
         sysInfo: res.data.data
        })
      }
    })
  },
})