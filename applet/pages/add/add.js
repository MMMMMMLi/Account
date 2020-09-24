const REQUEST = require('../../utils/request');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    inputShowed: false,
    inputVal: "",
    inputUserInfo: "",
    categoryArray: ['西瓜红', '济薯26', '烟薯25'],
    sizeArray: ['大', '中', '小'],
    orderListId:0,
    orderList: []
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
      inputUserInfo: [],
      inputShowed: false
    });
  },
  // 清空搜索框内容
  clearInput: function () {
    this.setData({
      inputVal: "",
      inputUserInfo: []
    });
  },
  // 当输入框输入的时候触发的操作
  inputTyping: function (e) {
    this.setData({
      inputVal: e.detail.value
    });
    const that = this;
    // 触发后台请求，获取用户信息
    REQUEST.request('user/getUserInfoBySearch', 'POST', {
      token: wx.getStorageSync('token'),
      searchValue: e.detail.value,
      size: 20
    }).then(res => {
      if (res.data.code == 20007) {
        that.setData({
          inputUserInfo: res.data.data,
        });
      }
    })
  },
  searchClick(e) {
    let oldOrderList = this.data.orderList;
    const clickUserInfo = e.currentTarget.dataset.info;
    clickUserInfo.orderDate = new Date().toLocaleString();
    if(oldOrderList.filter(order => order.userInfo.id == clickUserInfo.id).length == 0) {
      oldOrderList.push({
        orderListId:this.data.orderListId++,
        userInfo:clickUserInfo,
        orders:[]
      })
      this.setData({
        orderList:oldOrderList
      })
      this.clearInput();
    }else {
      wx.showModal({
        content: '当前操作用户存在订单，请勿重复操作！',
        showCancel: false
      })
    }

  },
  testAdd(e) {
    console.log(e.currentTarget.dataset.name)
  },

})