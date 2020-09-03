const app = getApp();
const AUTH = require('../../utils/auth');
const mock = require('../../utils/mock');

Page({
  data: {
    statusType: [{
        status: 0,
        label: '今日订单'
      },
      {
        status: 1,
        label: '三天内订单'
      },
      {
        status: 2,
        label: '一周内订单'
      },
      {
        status: 9999,
        label: '全部订单'
      },
    ],
    status: 0,
    hasRefund: false,
    badges: [0, 0, 0, 0, 0]
  },
  statusTap: function (e) {
    const status = e.currentTarget.dataset.status;
    this.setData({
      status
    });
    this.onShow();
  },
  cancelOrderTap: function (e) {
    const that = this;
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确定要取消该订单吗？',
      content: '',
      success: function (res) {
        if (res.confirm) {
          console.log("cancelOrderTap....")
        }
      }
    })
  },
  refundApply(e) {
    // 申请售后
    const orderId = e.currentTarget.dataset.id;
    const amount = e.currentTarget.dataset.amount;
    wx.navigateTo({
      url: "/pages/order/refundApply?id=" + orderId + "&amount=" + amount
    })
  },
  toPayTap: function (e) {
    // 防止连续点击--开始
    if (this.data.payButtonClicked) {
      wx.showToast({
        title: '休息一下~',
        icon: 'none'
      })
      return
    }
    this.data.payButtonClicked = true
    setTimeout(() => {
      this.data.payButtonClicked = false
    }, 3000) // 可自行修改时间间隔（目前是3秒内只能点击一次支付按钮）
    // 防止连续点击--结束
    const that = this;
    const orderId = e.currentTarget.dataset.id;
    let money = e.currentTarget.dataset.money;
    const needScore = e.currentTarget.dataset.score;
    wx.showToast({
      title: '您的积分不足，无法支付',
      icon: 'none'
    })
    return;

  },
  _toPayTap: function (orderId, money) {
    const _this = this

  },
  onLoad: function (options) {
    if (options && options.type) {
      if (options.type == 99) {
        this.setData({
          hasRefund: true
        });
      } else {
        this.setData({
          status: options.type
        });
      }
    }
  },
  onReady: function () {
    // 生命周期函数--监听页面初次渲染完成

  },
  getOrderStatistics() {
    // WXAPI.orderStatistics(wx.getStorageSync('token')).then(res => {
    if (0 == 0) {
      const badges = this.data.badges;
      // badges[1] = res.data.count_id_no_pay
      // badges[2] = res.data.count_id_no_transfer
      // badges[3] = res.data.count_id_no_confirm
      // badges[4] = res.data.count_id_no_reputation
      this.setData({
        badges
      })
    }
    // })
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
    if (this.data.hasRefund) {
      postData.hasRefund = true
    }
    if (!postData.hasRefund) {
      postData.status = that.data.status;
    }
    if (postData.status == 9999) {
      postData.status = ''
    }
    this.getOrderStatistics();
    mock.mockData().then(mockData => {
      that.setData({
        orderList : mockData.orderList,
        goodsList : mockData.goodsList
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

  }
})