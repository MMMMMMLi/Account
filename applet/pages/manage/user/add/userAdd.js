const APP = getApp();
const REQUEST = require('../../../../utils/request');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 表单校验提示
    topTips: false,
    hasError: false,
    tipMsg: '',

    // 是否显示叉号
    // showClearBtn:true,
    // 头像URL
    avatarUrl: '/images/nologin.png'
  },
  /**
   * 生成随机头像
   */
  async refreshAva() {
    let res = await REQUEST.request('user/refreshAva', 'POST', {});
    if (res.statusCode === 200 && res.data.code === 20000) {
      this.setData({
        avatarUrl: res.data.data
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 进来之后获取一个头像
    this.refreshAva()
  },
  // 校验手机号状态
  formSubmit(e) {
    const that = this;
    // 校验一下，不正确的话返回提示信息。
    let userName = e.detail.value.name;
    let phoneNum = e.detail.value.phone;
    if (!userName || !phoneNum) {
      that.setData({
        topTips: true,
        hasError: true,
        tipMsg: "请正确填写个人信息！"
      })
      setTimeout(() => {
        that.setData({
          topTips: false
        });
      }, 1500);
      return;
    }
    if (that.data.hasError) {
      wx.showModal({
        title: '无法提交',
        content: that.data.tipMsg,
        showCancel: false
      })
      return;
    }
    // 编写后台保存
    REQUEST.request('user/updateUserInfo', 'POST', {
      token: wx.getStorageSync('token'),
      name: e.detail.value.name,
      phone: e.detail.value.phone
    }).then(res => {
      if (res.data.code == 20006) {
        wx.showModal({
          content: '提交成功',
          showCancel: false,
          success(res) {
            // 修改主进程状态
            APP.globalData.needUpdateUserInfo = false;
            wx.switchTab({
              url: '/pages/my/my',
            });
          }
        })
      } else {
        wx.showModal({
          content: res.data.msg,
          showCancel: false,
          success(res) {
            wx.switchTab({
              url: '/pages/my/my',
            });
          }
        })
      }
    })
  },
  checkNum(e) {
    if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(e.detail.value))) {
      this.setData({
        hasError: true,
        topTips: true,
        tipMsg: "请填写正确的手机号！"
      })
      setTimeout(() => {
        this.setData({
          topTips: false
        });
      }, 1500);
    } else {
      this.setData({
        hasError: false,
        topTips: false,
        tipMsg: ''
      })
    }
  },
  back() {
    wx.navigateBack()
  },
})