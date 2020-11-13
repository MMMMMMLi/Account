const REQUEST = require('../../../../utils/request');

// pages/manage/user/detail/detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 分页信息
    page: 0,
    size: 10,
    hasNextPage: false,
    // 展示信息
    userName: '',
    phoneNumber: '',
    address: '',
    gender: '',
    updateTime: '',
    orders: 0,
    detailsMap: {}
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    this.setData({
      userId: params.userId,
      orders: params.orders,
      money: params.money
    })
    this.getUserDetails();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getUserDetails();
    }
  },
  getUserDetails() {
    let that = this;
    REQUEST.request('manage/getUserDetails', 'POST', {
      token: wx.getStorageSync('token'),
      userId: this.data.userId,
      page: this.data.page,
      size: this.data.size
    }).then(res => {
      if (res.data.code == 20000) {
        let details = res.data.pageInfo.list;
        let detailsMap = that.data.detailsMap;
        // 处理数据
        details.forEach(deatil => {
          if (detailsMap.hasOwnProperty(deatil.createDate)) {
            detailsMap[deatil.createDate].push(deatil);
          } else {
            detailsMap[deatil.createDate] = [deatil];
          }
        })
        that.setData({
          userName: details[0].userName || 'Error',
          phoneNumber: details[0].phoneNumber || 'Error',
          address: details[0].address || 'Error',
          gender: details[0].gender,
          updateTime: details[0].updateTime || 'Error',
          detailsMap,
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
        })
      } // 校验Token失败，等待1S之后重新拉取数据
      if (res.data.code === 40002) {
        setTimeout(() => {
          if (wx.getStorageSync('token')) {
            setTimeout(() => {
              that.getUserDetails();
            }, 1000)
          }
        }, 1000)
        return;
      }
    })
  },
})