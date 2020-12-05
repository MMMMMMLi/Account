const AUTH = require('utils/auth');
const REQUEST = require('utils/request');

App({
  // 生命周期回调——监听小程序初始化。
  onLaunch: function () {
    const that = this;
    // 检测新版本
    const updateManager = wx.getUpdateManager()
    updateManager.onUpdateReady(function () {
      wx.showModal({
        title: '更新提示',
        content: '新版本已经准备好，是否重启应用？',
        success(res) {
          if (res.confirm) {
            // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
            updateManager.applyUpdate()
          }
        }
      })
    })
    /**
     * 初次加载判断网络情况
     * 无网络状态下根据实际情况进行调整
     */
    wx.getNetworkType({
      success(res) {
        const networkType = res.networkType
        if (networkType === 'none') {
          that.globalData.isConnected = false
          wx.showToast({
            title: '当前无网络',
            icon: 'loading',
            duration: 2000
          })
        }
      }
    });
    /**
     * 监听网络状态变化
     * 可根据业务需求进行调整
     */
    wx.onNetworkStatusChange(function (res) {
      if (!res.isConnected) {
        that.globalData.isConnected = false
        wx.showToast({
          title: '网络已断开',
          icon: 'loading',
          duration: 2000
        })
      } else {
        that.globalData.isConnected = true
        wx.hideToast()
      }
    })
  },
  onShow(e) {
    this.authLogin();
  },
  authLogin() {
    // 自动登录
    return new Promise((res) => {
      AUTH.checkHasLogined().then(async isLogined => {
        res(isLogined);
      });
    }).then(isLogined => {
      if (!isLogined) {
        AUTH.login()
      }
      this.getUserApiInfo();
    })
  },
  getUserApiInfo: function () {
    REQUEST.request('user/authUserInfo', 'POST', {
      token: wx.getStorageSync('token'),
    }).then(res => {
      // 用户信息不需要完善
      if (res.data.code == 20005) {
        // 保存主进程的用户信息
        this.globalData.userInfos = res.data.data;
        this.globalData.needUpdateUserInfo = false;
      }
      // 用户信息还需要完善
      if (res.data.code == 40003) {
        // 保存主进程的用户信息
        this.globalData.needUpdateUserInfo = true;
      }
      // 用户未授权
      if (res.data.code == 40004) {
        // 保存主进程的用户信息
        this.globalData.needUpdateUserInfo = true;
      }
    })
  },
  globalData: {
    // 校验是否登陆
    isConnected: true,
    // 是否需要更新用户信息
    needUpdateUserInfo: false,
    // 当前用户信息列表
    userInfos: [],
    // 消息订阅模板
    tmplIds:['SOzXhOVHt9X1NQAOJPvB5M1refAxCBVxIW_IKz8HGmw']
  }
})