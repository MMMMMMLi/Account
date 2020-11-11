const REQUEST = require('../../../utils/request');

// pages/manage/user/user.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 搜索
    inputShowed: false,
    inputVal: "",
    // 排序按钮
    clickButton: '',
    // 分页
    page: 0,
    size: 10
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserList('', '')
  },
  // 打开搜索input
  showInput: function () {
    this.setData({
      inputShowed: true
    });
  },
  // 禁用搜索input
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
    // 获取数据
    if (this.data.clickButton) {
      this.getUserList('order', this.data.clickButton);
    } else {
      this.getUserList('search', '');
    }
  },
  // 清空搜索框内容
  clearInput: function () {
    this.setData({
      inputVal: "",
    });
    // 获取数据
    if (this.data.clickButton) {
      this.getUserList('order', this.data.clickButton);
    } else {
      this.getUserList('search', '');
    }
  },
  // 输入内容
  inputTyping: function (e) {
    let inputVal = e.detail.value;
    this.setData({
      inputVal
    });
    // 获取数据
    if (this.data.clickButton) {
      if (inputVal) {
        this.getUserList(this.data.clickButton, inputVal);
      } else {
        this.getUserList('order', this.data.clickButtonVal);
      }
    } else {
      this.getUserList('search', inputVal);
    }
  },
  // 点击排序Button
  clickButton(e) {
    let clickButtonValue = e.currentTarget.dataset.value;
    if (this.data.clickButton == clickButtonValue) {
      clickButtonValue = '';
    }
    this.setData({
      clickButton: clickButtonValue,
      page: 0
    })
    // 获取数据
    if (this.data.inputVal) {
      if (clickButtonValue) {
        this.getUserList(clickButtonValue, this.data.inputVal);
      } else {
        this.getUserList('search', this.data.inputVal);
      }
    } else {
      this.getUserList('order', clickButtonValue);
    }
  },
  // 获取订单列表
  getUserList(searchType, searchValue) {
    let that = this;
    REQUEST.request('manage/getUserList', 'POST', {
      token: wx.getStorageSync('token'),
      searchType,
      searchValue,
      page: this.data.page,
      size: this.data.size
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
          userList: res.data.pageInfo.list
        })
      } // 校验Token失败，等待1S之后重新拉取数据
      if (res.data.code === 40002) {
        setTimeout(() => {
          if (wx.getStorageSync('token')) {
            setTimeout(() => {
              that.getUserList(searchType, searchValue);
            }, 1000)
          }
        }, 1000)
        return;
      }
    })
  },
  // 手动新增用户
  addUser() {
    wx.showModal({
      content: '暂未实现此功能',
      showCancel: false
    })
  },
  onHide() {
    // this.setData({
    //   clickButton:'',
    // })
  },

})