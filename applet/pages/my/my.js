const APP = getApp();
const AUTH = require('../../utils/auth');
const CONFIG = require('../../config.js');
const REQUEST = require('../../utils/request');

Page({
  data: {
    wxAuth: true,
    hasUserInfo: false,
    needUpdateUserInfo: false,

    balance: 0.00, //可用余额
    freeze: 0, //冻结金额
    score: 0, //可用积分
    subMsgNum: 0, // 剩余可推送次数
    score_sign_continuous: 0,
    rechargeOpen: false, // 是否开启充值[预存]功能
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
    this.setData({
      // TODO: 暂时不知道这个有什么用?
      version: CONFIG.version,
    })

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
        _this.getUserAmount();
      }
    })
  },
  // 增加 消息通知次数
  addMsgNum(){
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
              apiUserInfoMap:res.data.data
            })
            APP.globalData.userInfos = res.data.data;
          }
        })
      }
    })
  },
  // TODO: 可以修改一下展示信息。
  aboutUs: function () {
    wx.showModal({
      title: '关于我们',
      content: '本系统是兴隆薯业的展示订单平台，祝大家使用愉快！',
      showCancel: false
    })
  },
  loginOut() {
    AUTH.loginOut()
    wx.reLaunch({
      url: '/pages/my/index'
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
  // 获取资产信息（余额、可用积分）
  getUserAmount: function () {
    var that = this;
    // TODO: 后续完善，待定。
  },
  handleOrderCount: function (count) {
    return count > 99 ? '99+' : count;
  },
  cancelLogin() {
    this.setData({
      wxAuth: true,
      hasUserInfo: true
    })
  },
  goLogin() {
    this.setData({
      wxAuth: false
    })
  },
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
  clearStorage() {
    wx.clearStorageSync()
    // 重新将token置为新值
    // wx.setStorageSync('token', 'clearStorage');
    wx.showToast({
      title: '已清除',
      icon: 'success'
    })
  },
})