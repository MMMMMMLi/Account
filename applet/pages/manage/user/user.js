const REQUEST = require('../../../utils/request');

// pages/manage/user/user.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    clickButton: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserList()
  },
  clickButton(e) {
    let clickButtonValue = e.currentTarget.dataset.value;
    if (this.data.clickButton == clickButtonValue) {
      clickButtonValue = '';
    }
    this.setData({
      clickButton: clickButtonValue,
    })
  },
  getUserList() {
    let that = this;
    REQUEST.request('manage/getUserList', 'POST', {
      token: wx.getStorageSync('token'),
      page: 0,
      size: 10
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
          userList : res.data.pageInfo.list
        })
        console.log(res.data);
      }
    })
  },
  onHide() {
    // this.setData({
    //   clickButton:'',
    // })
  },

})