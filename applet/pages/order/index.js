const app = getApp();
const AUTH = require('../../utils/auth');
const mock = require('../../utils/mock');

Page({
  data: {
    statusType: [{
        status: 0,
        label: '今日'
      },
      {
        status: 1,
        label: '三天'
      },
      {
        status: 2,
        label: '一周'
      },
      {
        status: 9999,
        label: '全部'
      },
    ],
    status: 0,
    currentTab: 0,
    goodsList: {},
    orderList: [],
    hasMore:true,
    total:0
  },
  swichNav: function (e) {
    console.log(e);
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current,
      })
      // 此处改成动态获取数据
      mock.mockData().then(mockData => {
        this.setData({
          orderList: mockData.orderList,
          goodsList: mockData.goodsList
        })
      });

    }
  },

  onLoad: function () {
    // AUTH.getOrderList().then(res => {
    //   console.log(res)
    // })
  },
  onReady: function () {
    // 生命周期函数--监听页面初次渲染完成
  },
  onShow: function () {
    AUTH.checkHasLogined().then(isLogined => {
      isLogined = true;
      if (isLogined) {
        this.doneShow();
      } else {
        wx.showModal({
          title: '提示',
          content: '本次操作需要您的登录授权',
          cancelText: '暂不登录',
          confirmText: '前往登录',
          success(res) {
            if (res.confirm) {
              this.doneShow();
              wx.switchTab({
                url: "/pages/index/index"
              })
            } else {
              console.log("........")
            }
          }
        })
      }
    })
  },
  doneShow() {
    // 获取订单列表
    var that = this;
    var postData = {
      token: wx.getStorageSync('token')
    };
    // 动态获取数据
    mock.mockData().then(mockData => {
      that.setData({
        orderList: mockData.orderList,
        goodsList: mockData.goodsList
      })
    });
  },
  onHide: function () {
    // 生命周期函数--监听页面隐藏

  },
  onUnload: function () {
    // 生命周期函数--监听页面卸载

  },
  onPullDownRefresh: function () {
    // 页面相关事件处理函数--监听用户下拉动作

  },
  onReachBottom: function () {
     // 页面上拉触底事件的处理函数
    console.log(".................到底了......")
    //加载提示
    wx.showLoading({
      title: '加载中',
    })
    let that = this;
    let total = that.data.total + 1;
    if(total > 3) {
      this.setData({
        hasMore: false,
        total:total
      })
      wx.hideLoading();
      return;
    }
    // 动态获取数据
    mock.mockThreeData().then(mockData => {
      wx.hideLoading();
      that.setData({
        orderList: that.data.orderList.concat(mockData.orderList),
        goodsList: Object.assign(that.data.goodsList, mockData.goodsList),
        total:total
      })
    });
  }
})

// 滑动详情页相关方法
// swiperChange: function (e) {
//   console.log(e);
//   this.setData({
//     currentTab: e.detail.current,
//   })
//   this.setData({
//     goodsList: {},
//     orderList: []
//   })
//   mock.mockData().then(mockData => {
//     this.setData({
//       orderList: mockData.orderList,
//       goodsList: mockData.goodsList
//     })
//   });

// },
// statusTap: function (e) {
//   const status = e.currentTarget.dataset.status;
//   this.setData({
//     status
//   });
//   this.onShow();
// },
// cancelOrderTap: function (e) {
//   const that = this;
//   const orderId = e.currentTarget.dataset.id;
//   wx.showModal({
//     title: '确定要取消该订单吗？',
//     content: '',
//     success: function (res) {
//       if (res.confirm) {
//         console.log("cancelOrderTap....")
//       }
//     }
//   })
// },