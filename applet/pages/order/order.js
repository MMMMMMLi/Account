const REQUEST = require('../../utils/request');
const APP = getApp();

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
    page: 0,
    size: 5,
    hasNextPage: false,
    oldOrderList: []
  },
  // 按钮切换的操作。
  swichNav: function (e) {
    let that = this;
    let current = e.target.dataset.current;
    if (this.data.currentTab === current) {
      return false;
    } else {
      that.setData({
        currentTab: current,
        page: 0,
        orderList: []
      })
      //动态获取数据
      that.getData(current, true);
    }
  },
  getData(status, showLoading) {
    let that = this;
    if (showLoading) {
      wx.showLoading({
        title: '加载中',
      })
    }
    let oldOrderList = that.data.orderList ? that.data.orderList : [];
    // 根据传入的状态码，去后台获取订单列表
    REQUEST.request('order/getMyOrderList', 'POST', {
      token: wx.getStorageSync('token'),
      status,
      page: that.data.page,
      size: that.data.size
    }).then(res => {
      if (res.data.code === 20000) {
        that.setData({
          orderList: oldOrderList.concat(res.data.data),
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
        })
      } else {
        // 校验Token失败，等待1S之后重新拉取数据
        if (res.data.code === 40002) {
          setTimeout(() => {
            if (wx.getStorageSync('token')) {
              that.getData(that.data.currentTab, true);
            }
          }, 1000)
          return;
        }
        wx.showModal({
          content: res.data.msg,
          showCancel: false
        })
      }
    })
    if (showLoading) {
      wx.hideLoading();
    }
  },
  onLoad() {
    
    REQUEST.request('user/authUserInfo', 'POST', {
      token: wx.getStorageSync('token'),
    }).then(res => {
      const userInfo = res.data.data || {
        'subMsgNum': 1
      };
      if (userInfo.subMsgNum <= 0) {
        wx.showModal({
          title: '提示',
          content: '是否接收新订单的通知？',
          success(res) {
            if (res.confirm) {
              // 在订单页面,需要获取用户是否接收消息通知的权限
              wx.requestSubscribeMessage({
                tmplIds: APP.globalData.tmplIds,
                success(res) {
                  // 如果对接正常,则数据库提示次数+1
                  REQUEST.request('user/userSubMsgAdd', 'POST', {
                    userId: userInfo.id
                  }).then(res => {
                    if (res.data.code == 20000) {
                      APP.globalData.userInfos = res.data.data;
                    }
                  })
                }
              })
            }
          }
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onShow() {
    // 获取数据
    this.getData(this.data.currentTab, false);
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    setTimeout(() => {
      this.hideAsync()
    }, 1000);
  },
  hideAsync() {
    let that = this;
    return new Promise((resolve, reject) => {
      // 离开的时候，默认选择 今日 tab栏。
      that.setData({
        currentTab: 0,
        page: 0,
        orderList: []
      })
      // 每次打开页面,都需要回到顶部。
      if (wx.pageScrollTo) {
        wx.pageScrollTo({
          scrollTop: 0,
        })
      }
    })
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getData(this.data.currentTab, false);
    }
  },
})