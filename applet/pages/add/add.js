const REQUEST = require('../../utils/request');
const UTILS = require('../../utils/util.js');

Page({
  /**
   * 页面的初始数据
   */
  data: {
    inputShowed: false,
    inputVal: "",
    inputUserInfo: "",
    orderListId: 0,
    orderList: [],
    toast: false,
    hideToast: false,
  },
  onLoad() {},
  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      let viewName = wx.getStorageSync('viewName') || this.getTabBar().data.viewName;

      if (viewName == 'master') {
        this.getTabBar().setData({
          selected: 1
        })
      }
      if (viewName == 'developer') {
        this.getTabBar().setData({
          selected: 3
        })
      }
    }

    // 每次打开页面,都需要回到顶部。
    if (wx.pageScrollTo) {
      wx.pageScrollTo({
        scrollTop: 0,
      })
    }
    // 获取类型
    this.getServerData();
    // 查看内存中是否存在 需要修改的 订单信息
    let orderInfo = wx.getStorageSync('orderInfo');
    if (orderInfo) {
      // 获取完毕之后，一定要及时清理
      wx.removeStorageSync('orderInfo')
      // 获取旧的订单列表
      let oldOrderList = this.data.orderList;
      // 判断是否获取完 品种或者大小列表
      if (!this.data.categoryArray) {
        // 没有获取完,则等待1min
        wx.showLoading({
          title: '加载中...',
        })
        setTimeout(() => {
          wx.hideLoading();
          orderInfo.orders.map(order => {
            order.categoryValue = UTILS.getArrayIndex(this.data.categoryArray, order.categoryValue);
            order.sizeValue = UTILS.getArrayIndex(this.data.sizeArray, order.sizeValue);
            return order;
          });
          oldOrderList.push(orderInfo)
          this.setData({
            orderList: oldOrderList
          })
        }, 1000)
      } else {
        // 如果获取完直接操作
        orderInfo.orders.map(order => {
          order.categoryValue = UTILS.getArrayIndex(this.data.categoryArray, order.categoryValue);
          order.sizeValue = UTILS.getArrayIndex(this.data.sizeArray, order.sizeValue);
          return order;
        });
        oldOrderList.push(orderInfo)
        this.setData({
          orderList: oldOrderList
        })
      }
    }

  },
  // 动态获取地瓜品种和大小类别
  getServerData() {
    let that = this;
    // 动态获取一下地瓜品种和大小规格
    REQUEST.request('applet/getConstant', 'POST', {}).then(res => {
      if (res.data.code == 20000) {
        this.setData({
          categoryArray: res.data.data.category,
          sizeArray: res.data.data.size
        })
      } else {
        wx.showModal({
          title: '系统异常，点击重试！',
          success(res) {
            if (res.confirm) {
              that.getServerData()
            }
          }
        })
      }
    })
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
  // 搜索之后，点击 用户信息
  searchClick(e) {
    // 更新一下组件的订单信息到首页
    this.updateOrderInfo();
    let oldOrderList = this.data.orderList;
    const clickUserInfo = e.currentTarget.dataset.info;
    clickUserInfo.orderDate = UTILS.formatTime(new Date());
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
    this.openToast();
    let orderList = this.data.orderList;
    this.setData({
      orderList: orderList.filter(order => order.orderListId != e.detail)
    })
  },
  // 点击 确定 之后成功的回调提示
  openToast: function () {
    this.setData({
      toast: true
    });
    setTimeout(() => {
      this.setData({
        hideToast: true
      });
      setTimeout(() => {
        this.setData({
          toast: false,
          hideToast: false,
        });
      }, 300);
    }, 1500);
  },
})

export function switchView() {
  console.log(this)
  this.getTabBar().setData({
    viewName: wx.getStorageSync('viewName')
  })
}