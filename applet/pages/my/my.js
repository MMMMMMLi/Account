const app = getApp()
const AUTH = require('../../utils/auth')
const CONFIG = require('../../config.js')
const REQUEST = require('../../utils/request');

Page({
	data: {
    wxlogin: true,

    balance:0.00, //可用余额
    freeze:0,//冻结金额
    score:0,//可用积分
    growth:0,//当前成长值
    score_sign_continuous:0,
    rechargeOpen: false, // 是否开启充值[预存]功能
  },
	onLoad() {
    
	},
  onShow() {
    const _this = this
    this.setData({
      // TODO: 暂时不知道这个有什么用?
      version: CONFIG.version,
    })
    // 校验用户是否登陆
    AUTH.checkHasLogined().then(isLogined => {
      this.setData({
        wxlogin: isLogined
      })
      // 如果已经登陆了话,获取一些基本信息.
      if (isLogined) {
        _this.getUserApiInfo();
        _this.getUserAmount();
      }
    })
  },
  // TODO: 可以修改一下展示信息。
  aboutUs : function () {
    wx.showModal({
      title: '关于我们',
      content: '本系统是兴隆薯业的展示订单平台，祝大家使用愉快！',
      showCancel:false
    })
  },
  loginOut(){
    AUTH.loginOut()
    wx.reLaunch({
      url: '/pages/my/index'
    })
  },
  getPhoneNumber: function(e) {
    if (!e.detail.errMsg || e.detail.errMsg != "getPhoneNumber:ok") {
      wx.showModal({
        title: '提示',
        content: e.detail.errMsg,
        showCancel: false
      })
      return;
    }
    // WXAPI.bindMobileWxa(wx.getStorageSync('token'), e.detail.encryptedData, e.detail.iv).then(res => {
    //   if (res.code === 10002) {
    //     this.setData({
    //       wxlogin: false
    //     })
    //     return
    //   }
    //   if (res.code == 0) {
    //     wx.showToast({
    //       title: '绑定成功',
    //       icon: 'success',
    //       duration: 2000
    //     })
    //     this.getUserApiInfo();
    //   } else {
    //     wx.showModal({
    //       title: '提示',
    //       content: res.msg,
    //       showCancel: false
    //     })
    //   }
    // })
  },
  // 获取用户信息
  getUserApiInfo: function () {
    var that = this;
    const token = wx.getStorageSync('token');
    REQUEST.request('user/authUserInfo', 'POST', {
      token: token
    }).then(res => {
      if (res.data.code == 20005) {
        that.setData({
          apiUserInfoMap: res.data.data
        });
      }
    })
  },
  // 获取资产信息（余额、可用积分）
  getUserAmount: function () {
    var that = this;
    // TODO: 后续完善，待定。
  },
  handleOrderCount: function (count) {
    return count > 99 ? '99+' : count;
  },
  goAsset: function () {
    wx.navigateTo({
      url: "/pages/asset/index"
    })
  },
  goScore: function () {
    wx.navigateTo({
      url: "/pages/score/index"
    })
  },
  goOrder: function (e) {
    wx.navigateTo({
      url: "/pages/order-list/index?type=" + e.currentTarget.dataset.type
    })
  },
  cancelLogin() {
    this.setData({
      wxlogin: true
    })
  },
  goLogin() {
    this.setData({
      wxlogin: false
    })
  },
  processLogin(e) {
    if (!e.detail.userInfo) {
      wx.showToast({
        title: '已取消',
        icon: 'none',
      })
      return;
    }
    AUTH.register(this);
  },
  scanOrderCode(){
    wx.scanCode({
      onlyFromCamera: true,
      success(res) {
        wx.navigateTo({
          url: '/pages/order-details/scan-result?hxNumber=' + res.result,
        })
      },
      fail(err) {
        console.error(err)
        wx.showToast({
          title: err.errMsg,
          icon: 'none'
        })
      }
    })
  },
  clearStorage(){
    wx.clearStorageSync()
    wx.showToast({
      title: '已清除',
      icon: 'success'
    })
  },
})