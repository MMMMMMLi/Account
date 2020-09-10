// 获取静态配置文件
const CONFIG = require('../../config');
const REQUEST = require('../../utils/request');

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
      REQUEST.request('applet/getBanners', 'POST', '').then(res => {
        if(res.data.code == 40000) {
          return;
        }
        if (res.data.code == 20001) {
          wx.switchTab({
            url: '/pages/order/index',
          });
        } else {
          _this.setData({
            banners: res.data.data,
            swiperMaxNumber: res.data.data.length
          });
        }
      })
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
        url: '/pages/order/index',
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