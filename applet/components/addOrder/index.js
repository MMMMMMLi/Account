const REQUEST = require('../../utils/request');

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    categoryArray: {
      type: Array,
      value: []
    },
    sizeArray: {
      type: Array,
      value: []
    },
    orderInfo: {
      type: Map,
      value: []
    },
  },

  /**
   * 组件的初始数据
   */
  data: {
    categoryValue: 0,
    sizeValue: 0,
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 品种选择框
    categoryPickerChange: function (e) {
      const orderInfo = this.properties.orderInfo;
      const index = e.currentTarget.dataset.index;
      orderInfo.orders[index].categoryValue = e.detail.value;
      this.setData({
        orderInfo,
      })
    },
    // 大小选择框
    sizePickerChange: function (e) {
      const orderInfo = this.properties.orderInfo;
      const index = e.currentTarget.dataset.index;
      orderInfo.orders[index].sizeValue = e.detail.value;
      this.setData({
        orderInfo,
      })
    },
    // 输入框输入结束
    overInput(e) {
      const orderInfo = this.properties.orderInfo;
      const index = e.currentTarget.dataset.index;
      const type = e.currentTarget.dataset.type;
      if (type == 'gross') {
        orderInfo.orders[index].gross = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      if (type == 'tare') {
        orderInfo.orders[index].tare = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      if (type == 'unitPrice') {
        orderInfo.orders[index].unitPrice = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      let thisOrder = orderInfo.orders[index];
      // 判断三个条件是否都存在，存在的话，则生成总价。
      if (thisOrder.hasOwnProperty('gross') && thisOrder.hasOwnProperty('tare') && thisOrder.hasOwnProperty('unitPrice')) {
        // 计算当前订单项的金额
        orderInfo.orders[index].totalPrice = thisOrder.unitPrice * (thisOrder.gross - thisOrder.tare);
        // 计算所有订单项的金额
        orderInfo.totalPrice = orderInfo.orders.map(order => order.totalPrice).reduce((total, num) => total + num);
        this.setData({
          orderInfo,
        })
      }
    },
    // 添加订单列表
    addOrder() {
      const orderInfo = this.properties.orderInfo;
      orderInfo.orders.push({
        categoryValue: 1,
        sizeValue: 1
      })
      this.setData({
        orderInfo,
      })
    },
    // 删除订单列表
    delOrder() {
      const orderInfo = this.properties.orderInfo;
      if (orderInfo.orders.length > 1) {
        orderInfo.orders.pop();
        orderInfo.totalPrice = orderInfo.orders.map(order => order.totalPrice).reduce((total, num) => total + num);
        this.setData({
          orderInfo,
        })
      } else {
        wx.showModal({
          content: '订单列表不可为空！',
          showCancel: false
        })
      }
    },
    formSubmit(e) {
      // 获取框子使用情况
      let formValue = e.detail.value;
      // 校验
      if (formValue.apply == "" || formValue.retreat == "") {
        wx.showModal({
          title: '提示',
          content: '请输入正确的用框和退框数量！',
          showCancel: false
        })
        return;
      }
      // 提交
      const orderInfo = this.properties.orderInfo;
      console.log(orderInfo)
      wx.showModal({
        title: '请核实无误后点击确定',
        content: '客户姓名：' + orderInfo.userInfo.userName + 
             '\r\n用框数量：' + formValue.apply + 
             '个\r\n退框数量：' + formValue.retreat +
             '个\r\n订单总额：￥' + orderInfo.totalPrice,
        success(res) {
          if (res.confirm) {
            REQUEST.request('user/getUserInfoBySearch', 'POST', {
              token: wx.getStorageSync('token'),
              orderFormInfo: ''
            }).then(res => {

            })
          }
        }
      })
      this.bindDeleteOrderInfo();
    },
    // 获取当前组件内的订单信息
    getOrderInfo() {
      return this.properties.orderInfo;
    },
    // 调用父组件的删除订单信息
    bindDeleteOrderInfo() {
      this.triggerEvent('deleteOrderInfo', this.data.orderInfo.orderListId);
    }
  }
})