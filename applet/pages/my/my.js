const APP = getApp();
const AUTH = require('../../utils/auth');
const CONFIG = require('../../config.js');
const REQUEST = require('../../utils/request');

Page({
  data: {
    wxAuth: true,
    hasUserInfo: false,
    needUpdateUserInfo: false,

    orderSize: 0, //总订单数
    paid: 0, // 已支付订单
    unPaid: 0, // 未支付订单
    subMsgNum: 0, // 剩余可推送次数
  },
  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 1,
      })
    }

    // 每次打开页面,都需要回到顶部。
    if (wx.pageScrollTo) {
      wx.pageScrollTo({
        scrollTop: 0
      })
    }
    // 执行操作
    const _this = this

    // 校验用户是否登陆
    AUTH.checkHasLogined().then(isLogined => {
      if (!isLogined) {
        this.setData({
          wxAuth: false
        })
      }
      // 如果已经登陆了话,获取一些基本信息.
      if (isLogined) {
        _this.getUserApiInfo();
        _this.getOrderInfo();
      }
    })
  },
  // 获取一些统计信息
  async getOrderInfo() {
    let res = await REQUEST.request('order/getMyOrderList', 'POST', {
      token: wx.getStorageSync('token'),
      status:999,
      page: 1,
      size: 999
    });
    if(res.data.code === 20000) {
      let orderList = res.data.data;
      if(orderList) {
        this.setData({
          orderSize: orderList.length, //总订单数
          paid: orderList.filter(order => order.status == '已付款').length, // 已支付订单
          unPaid: orderList.filter(order => order.status == '待付款').length, // 未支付订单
        });
      }
    }
  },
  // 获取用户信息
  getUserApiInfo: function () {
    var that = this;
    REQUEST.request('user/authUserInfo', 'POST', {
      token: wx.getStorageSync('token'),
    }).then(res => {
      // 用户信息不需要完善
      if (res.data.code == 20005) {
        that.setData({
          apiUserInfoMap: res.data.data,
          wxAuth: true,
          hasUserInfo: true,
          needUpdateUserInfo: false
        });
        // 保存主进程的用户信息
        APP.globalData.userInfos = res.data.data;
        APP.globalData.needUpdateUserInfo = false;
      }
      // 用户信息还需要完善
      if (res.data.code == 40003) {
        that.setData({
          apiUserInfoMap: res.data.data,
          wxAuth: true,
          needUpdateUserInfo: true,
          hasUserInfo: true
        });
        // 保存主进程的用户信息
        APP.globalData.needUpdateUserInfo = true;
      }
      // 用户未授权
      if (res.data.code == 40004) {
        that.setData({
          apiUserInfoMap: res.data.data,
          wxAuth: false,
          needUpdateUserInfo: true,
          hasUserInfo: false
        });
        // 保存主进程的用户信息
        APP.globalData.needUpdateUserInfo = true;
      }
    })
  },
  // 增加 消息通知次数
  addMsgNum() {
    let that = this;
    wx.requestSubscribeMessage({
      tmplIds: APP.globalData.tmplIds,
      success(res) {
        // 如果对接正常,则数据库提示次数+1
        REQUEST.request('user/userSubMsgAdd', 'POST', {
          userId: that.data.apiUserInfoMap.id
        }).then(res => {
          if (res.data.code == 20000) {
            that.setData({
              apiUserInfoMap: res.data.data
            })
            APP.globalData.userInfos = res.data.data;
          }
        })
      }
    })
  },
  // 关于我们
  aboutUs: function () {
    wx.showModal({
      title: '关于我们',
      content: '本系统是兴隆薯业的订单平台，祝大家使用愉快！',
      showCancel: false
    })
  },
  // 联系我们
  callUs() {
    wx.makePhoneCall({
      phoneNumber: '13791921198',
      complete(res){
      }
    })
  },
  // 确认登陆回调
  processLogin(e) {
    if (!e.detail.userInfo) {
      wx.showToast({
        title: '已取消',
        icon: 'none',
      })
      this.setData({
        wxAuth: true,
        hasUserInfo: true
      })
      return;
    }
    let that = this;
    REQUEST.request('user/setUserInfo', 'POST', {
      ...e.detail.userInfo,
      token: wx.getStorageSync('token'),
    }).then(res => {
      if (res.data.code == 20005) {
        that.setData({
          apiUserInfoMap: res.data.data,
          wxAuth: true,
          hasUserInfo: true,
          needUpdateUserInfo: true
        });
        // 保存主进程的用户信息
        APP.globalData.needUpdateUserInfo = true;
      }
    })
  },
  // 取消登陆
  cancelLogin() {
    this.setData({
      wxAuth: true,
      hasUserInfo: true
    })
  },
  // ----------------- 下面这些方法暂时为用到.
  goLogin() {
    this.setData({
      wxAuth: false
    })
  },
  clearStorage() {
    wx.clearStorageSync()
    // 重新将token置为新值
    // wx.setStorageSync('token', 'clearStorage');
    wx.showToast({
      title: '已清除',
      icon: 'success'
    })
  },
  loginOut() {
    AUTH.loginOut()
    wx.reLaunch({
      url: '/pages/my/my'
    })
  },
  getPhoneNumber: function (e) {
    if (!e.detail.errMsg || e.detail.errMsg != "getPhoneNumber:ok") {
      wx.showModal({
        title: '提示',
        content: e.detail.errMsg,
        showCancel: false
      })
      return;
    }
  },
})