const REQUEST = require('../../../utils/request');

// pages/manage/user/user.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 欠款时间排序
    clickButton: '999',
    // 分页
    page: 0,
    size: 10,
    hasNextPage: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserList(this.data.clickButton)
  },
  // 点击排序Button
  clickButton(e) {
    let clickButtonValue = e.currentTarget.dataset.value;
    if (this.data.clickButton == clickButtonValue) {
      clickButtonValue = '999';
    }
    // 更新
    this.setData({
      clickButton: clickButtonValue,
      userList:[],
      page: 0
    });
    // 获取
    this.getUserList(clickButtonValue);
  },
  // 获取订单列表
  getUserList(value) {
    let that = this;
    let oldUserList = that.data.userList ? that.data.userList : [];
    REQUEST.request('manage/getWarnList', 'POST', {
      token: wx.getStorageSync('token'),
      value,
      page: this.data.page,
      size: this.data.size
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
          userList: oldUserList.concat(res.data.pageInfo.list),
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
        })
      } // 校验Token失败，等待1S之后重新拉取数据
      if (res.data.code === 40002) {
        setTimeout(() => {
          if (wx.getStorageSync('token')) {
            setTimeout(() => {
              that.getUserList(searchType, searchValue);
            }, 1000)
          }
        }, 1000)
        return;
      }
    })
  },
  // 手动新增用户
  addUser() {
    wx.showModal({
      content: '暂未实现此功能',
      showCancel: false
    })
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getUserList(this.data.searchType, this.data.searchValue);
    }
  },
})