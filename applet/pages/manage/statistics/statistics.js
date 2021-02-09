const REQUEST = require('../../../utils/request');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    page: 0,
    size: 10,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getData();
  },
  async getData() {
    let res = await REQUEST.request('manage/getStatisticsList', 'POST', {
      token: wx.getStorageSync('token'),
      page: this.data.page,
      size: this.data.size
    })
    let detailsMap = this.data.detailsMap || {};
    res.data.data.list.forEach(info => {
      if (detailsMap.hasOwnProperty(info.dateTime)) {
        detailsMap[info.dateTime].push(info);
      } else {
        detailsMap[info.dateTime] = [info];
      }
    })
    this.setData({
      detailsMap,
      page: res.data.data.nextPage,
      hasNextPage: res.data.data.hasNextPage
    });
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  bindscrolltolower: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getData();
    }
  },
})