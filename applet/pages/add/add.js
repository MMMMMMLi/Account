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
    orderListId: 0,
    orderList: []
  },
  onShow() {
    // 页面初始化的时候，获取品种和大小的清单
    // REQUEST.request('manage/getCategoryAndSizeArray', 'POST', {})
    // .then(res => {
    // })
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
      } else {
        wx.showModal({
          content: res.data.msg,
          showCancel: false
        })
      }
    })
  },
  searchClick(e) {
    // 更新一下组件的订单信息到首页
    this.updateOrderInfo();
    let oldOrderList = this.data.orderList;
    const clickUserInfo = e.currentTarget.dataset.info;
    clickUserInfo.orderDate = new Date().toLocaleTimeString();
    if (oldOrderList.filter(order => order.userInfo.id == clickUserInfo.id).length == 0) {
      oldOrderList.push({
        orderListId: this.data.orderListId++,
        userInfo: clickUserInfo,
        orders: [{
          categoryValue: 0,
          sizeValue: 0
        }]
      })
      this.setData({
        orderList: oldOrderList
      })
      this.clearInput();
    } else {
      wx.showModal({
        content: '当前操作用户存在订单，请勿重复操作！',
        showCancel: false
      })
    }

  },
  // 每次搜索获取创建新的订单的时候,都需要拉去一下各个组件最新订单的情况。
  updateOrderInfo() {
    let orderList = this.data.orderList;
    if (orderList.length > 0) {
      orderList = orderList.map(order => {
        let thisOrderId = order.orderListId;
        // 通过组件选择器获取各个组件的实例对象，然后调用其方法。
        let newOrderInfo = this.selectComponent('#my-addOrder' + thisOrderId).getOrderInfo();
        if (newOrderInfo.orderListId == thisOrderId) {
          order = newOrderInfo;
        }
        return order;
      })
      this.setData({
        orderList  
      })
    }
  },
  // 在每个订单组件提交了之后，都调用这个方法删除当前页面订单信息。
  deleteOrderInfo(e) {
    let orderList = this.data.orderList;
    this.setData({
      orderList: orderList.filter(order => order.orderListId != e.detail)
    })
  },
})