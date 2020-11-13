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
    size: 10,
    hasNextPage: false,
    // 查询条件
    searchType: '',
    searchValue: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getUserList(this.data.searchType, this.data.searchValue)
  },
  // 打开搜索input
  showInput: function () {
    this.setData({
      inputShowed: true
    });
  },
  // 禁用搜索input
  hideInput: function () {
    let type, value;
    // 获取数据
    if (this.data.clickButton) {
      type = 'order';
      value = this.data.clickButton;
    } else {
      type = 'search';
      value = '';
    }
    this.setData({
      inputVal: "",
      userList:[],
      page: 0,
      inputShowed: false,
      searchType: type,
      searchValue: value
    });
    this.getUserList(type, value);
  },
  // 清空搜索框内容
  clearInput: function () {
    let type, value;
    // 获取数据
    if (this.data.clickButton) {
      type = 'order';
      value = this.data.clickButton;
    } else {
      type = 'search';
      value = '';
    }
    this.setData({
      userList:[],
      page: 0,
      inputVal: "",
      searchType: type,
      searchValue: value
    });
    this.getUserList(type, value);
  },
  // 输入内容
  inputTyping: function (e) {
    let inputVal = e.detail.value;
    let type, value;
    // 组装数据
    if (this.data.clickButton) {
      if (inputVal) {
        type = this.data.clickButton;
        value = inputVal;
      } else {
        type = 'order';
        value = this.data.clickButton;
      }
    } else {
      type = 'search';
      value = inputVal;
    }
    // 更新
    this.setData({
      inputVal,
      userList:[],
      page: 0,
      searchType: type,
      searchValue: value
    });
    // 获取
    this.getUserList(type, value);
  },
  // 点击排序Button
  clickButton(e) {
    let type, value;
    let clickButtonValue = e.currentTarget.dataset.value;
    if (this.data.clickButton == clickButtonValue) {
      clickButtonValue = '';
    }
    // 获取数据
    if (this.data.inputVal) {
      if (clickButtonValue) {
        type = clickButtonValue;
        value = this.data.inputVal;
      } else {
        type = 'search';
        value = this.data.inputVal;
      }
    } else {
      type = 'order';
      value = clickButtonValue;
    }
    // 更新
    this.setData({
      clickButton: clickButtonValue,
      userList:[],
      page: 0,
      searchType: type,
      searchValue: value
    });
    // 获取
    this.getUserList(type, value);
  },
  // 获取订单列表
  getUserList(searchType, searchValue) {
    let that = this;
    let oldUserList = that.data.userList ? that.data.userList : [];
    REQUEST.request('manage/getUserList', 'POST', {
      token: wx.getStorageSync('token'),
      searchType,
      searchValue,
      page: this.data.page,
      size: this.data.size
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
          userList: oldUserList.concat(res.data.pageInfo.list),
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
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
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getUserList(this.data.searchType, this.data.searchValue);
    }
  },
})