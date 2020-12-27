const AUTH = require('utils/auth');

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
    /**
     * 设置屏幕是否保持常量
     */
    wx.getBatteryInfo({
      success(res) {
        // 如果在充电或者电量大于50就设置为常量
        if (res.isCharging || res.level > 50) {
          wx.setKeepScreenOn({
            keepScreenOn: true
          })
        }
      }
    })
  },
  onShow(param) {
    // 此处逻辑说明
    // 1. 用户进入页面的时候,是走的start页面,里面需要获取用户权限信息,参数为Token
    // 2. 由于页面的加载为异步加载,当start加载的时候,
    //        这个地方校验用户是否登陆成功的方法还没有执行完,就会导致start页面数据获取失败
    // 3. 修改逻辑为: 第一次进入小程序的时候,由start页面先校验用户信息,在后续的页面中由此校验
    if (wx.getStorageSync('token') && param.path.indexOf('start') < 0) {
      this.authLogin();
    }
    // 判断当前电量是多少,如果少于30就取消屏幕常量的设置
    wx.getBatteryInfo({
      success(res) {
        if (!res.isCharging) {
          // 如果没有充电,则看看当前电量
          if (res.level < 30) {
            wx.setKeepScreenOn({
              keepScreenOn: false
            })
          }
        }
      }
    })
  },
  // 校验用户是否登陆,如果没有登陆,则登陆
  async authLogin() {
    if (!await AUTH.checkHasLogined()) {
      await AUTH.login()
    }

  },
  globalData: {
    // 校验是否登陆
    isConnected: true,
    // 是否需要更新用户信息
    needUpdateUserInfo: false,
    // 当前用户信息列表
    userInfos: [],
    // 消息订阅模板
    tmplIds: ['SOzXhOVHt9X1NQAOJPvB5M1refAxCBVxIW_IKz8HGmw'],
  }
})