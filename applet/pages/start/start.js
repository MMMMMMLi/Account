// 获取静态配置文件
const CONFIG = require('../../config');
const REQUEST = require('../../utils/request');
const AUTH = require('../../utils/auth');
const UTIL = require('../../utils/util');
const APP = getApp();

//获取应用实例
var app = getApp();

Page({
  data: {
    banners: [],
    swiperMaxNumber: 0,
    swiperCurrent: 0,

    // 请求用户的信息
    serverData: [],

    // 重复请求次数
    total: 0
  },
  onLoad: function () {
    UTIL.showLoading('正在登陆');
    // 获取Token
    APP.authLogin().then((res) => {
      UTIL.hideLoading();
      this.getInfo(true);
    })
  },
  // 执行逻辑
  async getInfo(updateFlag) {
    let token = wx.getStorageSync('token');
    const that = this;
    // 获取用户验证信息
    let res = await REQUEST.request('user/authUserInfo', 'POST', {
      token: token,
      updateFlag: updateFlag
    })
    if (res.data.code === 40002) {
      let total = this.data.total;
      this.setData({
        total: total + 1
      })
      if (total <= 10) {
        this.getInfo(false);
      } else {
        UTIL.showLoading('系统异常请稍候');
      }
    }
    let userInfo = res.data.data || [];
    let role = userInfo.role || '';
    if (userInfo) {
      // 校验当前用户是否失效
      if (userInfo.state === 0) {
        await UTIL.showModaling("登陆失败", "当前用户已被冻结，请联系管理员！", null, null, false);
        return;
      }
      if (role) {
        // 保存Tab栏变量值
        if (!wx.getStorageSync('viewNameFlag') || wx.getStorageSync('viewNameFlag') != role.name) {
          wx.setStorageSync('viewName', role.name);
          wx.setStorageSync('viewNameFlag', role.name);
        }
      }
    }
    // 获取用户观看开头图片的版本
    const app_show_pic_version = wx.getStorageSync('app_show_pic_version');
    if (app_show_pic_version && app_show_pic_version == CONFIG.version) {
      // 当前版本如果用户已经看过了,则不需要再看了。
      wx.switchTab({
        url: role.entryPage,
      });
    } else {
      REQUEST.request('applet/getBanners', 'POST', {}).then(bannerRes => {
        if (bannerRes.data.code == 20001) {
          wx.switchTab({
            url: role.entryPage,
          });
        } else {
          that.setData({
            serverData: res.data,
            banners: bannerRes.data.data,
            swiperMaxNumber: bannerRes.data.data.length
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
  goToIndex: async function (e) {
    // 校验网络是否正常
    if (app.globalData.isConnected) {
      let serverData = this.data.serverData;
      let userInfo = serverData.data || [];
      let role = userInfo.role || '';
      if (serverData.code == 20005) {
        wx.setStorage({
          key: 'app_show_pic_version',
          data: CONFIG.version
        })
        // 如果用户信息全换 ，则直接跳转页面即可。
        wx.switchTab({
          url: role.entryPage,
        });
      } else if (serverData.code == 40003 || serverData.code == 40004) {
        wx.setStorage({
          key: 'app_show_pic_version',
          data: CONFIG.version
        })
        // 假如当前用户是第一次使用小程序，则用户信息应该不完善，则跳转到个人信息页
        wx.switchTab({
          url: '/pages/my/my',
        });
      } else if (serverData.code == 40002) {
        // 如果校验失败，则重新login一下
        await AUTH.login();
        // 随后跳转到个人信息页面
        wx.switchTab({
          url: '/pages/my/my',
        });
      } else {
        // 登录错误
        UTIL.showModaling('无法登陆', '服务器错误，请重启!', null, null, false);
        return;
      }
    } else {
      UTIL.showToasting('当前无网络', 'none');
    }
  },
  // 点击图片的提示
  async imgClick() {
    if (this.data.swiperCurrent + 1 != this.data.swiperMaxNumber) {
      await UTIL.showToasting('左滑进入', 'none');
    }
  }
});