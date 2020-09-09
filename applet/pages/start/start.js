// 获取静态配置文件
const CONFIG = require('../../config.js')
//获取应用实例
var app = getApp();

Page({
  data: {
    banners: [],
    swiperMaxNumber: 0,
    swiperCurrent: 0
  },
  onLoad: function () {
    const _this = this;
    const app_show_pic_version = wx.getStorageSync('app_show_pic_version');
    // 当前版本如果用户已经看过了,则不需要再看了。
    if (app_show_pic_version && app_show_pic_version == CONFIG.version) {
      wx.switchTab({
        url: '/pages/order/index',
      });
    } else {
      // 展示启动页
      // if (res.code == 700) {
      // wx.switchTab({
      // url: '/pages/order/index',
      // });
      // } else {
      const haha = [{
        id: 1,
        picUrl: "/images/start/xiong.jpg"
      },{
        id: 2,
        picUrl: "/images/start/qiaoba.jpg"
      },{
        id: 3,
        picUrl: "/images/start/lufei.jpg"
      }]
      _this.setData({
        banners: haha,
        swiperMaxNumber: haha.length
      });
      // }
    }
  },
  // 图片切换触发的操作
  swiperchange: function (e) {
    this.setData({
      swiperCurrent: e.detail.current
    })
  },
  // 点击进入小程序按钮触发
  goToIndex: function (e) {
    // 校验网络是否正常
    if (app.globalData.isConnected) {
      wx.setStorage({
        key: 'app_show_pic_version',
        data: CONFIG.version
      })
      wx.switchTab({
        url: '/pages/index/index',
      });
    } else {
      wx.showToast({
        title: '当前无网络',
        icon: 'none',
      })
    }
  },
  // 点击图片的提示
  imgClick() {
    if (this.data.swiperCurrent + 1 != this.data.swiperMaxNumber) {
      wx.showToast({
        title: '左滑进入',
        icon: 'none',
      })
    }
  }
});