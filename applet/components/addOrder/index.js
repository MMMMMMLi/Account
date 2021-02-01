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
    // 每个框的单价
    boxPrice: 30
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
       // 框数 输入框失去焦点
       if (type == 'detailApplyBox') {
         let number = e.detail.value;
        orderInfo.orders[index].detailApplyBox = number;
        orderInfo.orders[index].boxTare = number * 2;
        // 迭代订单信息，自动生成用框数量
        orderInfo.applyBox = orderInfo.orders.map(order => order.detailApplyBox).reduce((total, num) => Number(total) + Number(num));
        this.setData({
          orderInfo,
        })
      }
      // 毛重 输入框失去焦点
      if (type == 'gross') {
        orderInfo.orders[index].gross = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      // 皮重 输入框失去焦点
      if (type == 'tare') {
        orderInfo.orders[index].tare = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      // 单价 输入框失去焦点
      if (type == 'unitPrice') {
        orderInfo.orders[index].unitPrice = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      // 用框 输入框失去焦点
      if (type == 'applyBox') {
        orderInfo.applyBox = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      // 退框 输入框失去焦点
      if (type == 'retreatBox') {
        orderInfo.retreatBox = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      let thisOrder = orderInfo.orders[index];
      // console.log(thisOrder);
      // 判断三个条件是否都存在，存在的话，则生成总价。
      if (thisOrder.hasOwnProperty('gross') && thisOrder.hasOwnProperty('tare') && thisOrder.hasOwnProperty('unitPrice')) {
        // 计算当前订单项的金额
        orderInfo.orders[index].totalPrice = Math.floor(thisOrder.unitPrice * (thisOrder.gross - thisOrder.tare - (thisOrder.boxTare ? thisOrder.boxTare : 0)) * 2);
        // 计算所有订单项的金额
        let totalPrice = orderInfo.orders.map(order => order.totalPrice).reduce((total, num) => total + num) +
          (((orderInfo.applyBox ? orderInfo.applyBox : 0) - (orderInfo.retreatBox ? orderInfo.retreatBox : 0)) * this.data.boxPrice);
          orderInfo.totalPrice = Math.floor(totalPrice);
        this.setData({
          orderInfo,
        })
      }
    },
    // 框子输入框输入结束
    overInputBox(e) {
      const orderInfo = this.properties.orderInfo;
      const type = e.currentTarget.dataset.type;
      // 用框 输入框失去焦点
      if (type == 'applyBox') {
        orderInfo.applyBox = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      // 退框 输入框失去焦点
      if (type == 'retreatBox') {
        orderInfo.retreatBox = e.detail.value;
        this.setData({
          orderInfo,
        })
      }
      orderInfo.totalPrice = ((orderInfo.applyBox ? orderInfo.applyBox : 0) - (orderInfo.retreatBox ? orderInfo.retreatBox : 0)) * this.data.boxPrice +
        orderInfo.orders.map(order => (order.totalPrice ? order.totalPrice : 0)).reduce((total, num) => total + num);
      this.setData({
        orderInfo,
      })
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
        orderInfo.totalPrice = orderInfo.orders.map(order => order.totalPrice).reduce((total, num) => total + num) +
          (((orderInfo.applyBox ? orderInfo.applyBox : 0) - (orderInfo.retreatBox ? orderInfo.retreatBox : 0)) * this.data.boxPrice);
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
    formSubmit() {
      let that = this;
      // 获取框子使用情况
      const orderInfo = this.properties.orderInfo;
      orderInfo.token = wx.getStorageSync('token');
      // 校验
      if ( !orderInfo.applyBox  || !orderInfo.retreatBox ) {
        wx.showModal({
          title: '提示',
          content: '请输入正确的用框和退框数量！',
          showCancel: false
        })
        return;
      }
      // 提交
      wx.showModal({
        title: '请核实无误后点击确定',
        content: '客户姓名：' + orderInfo.userInfo.userName +
          '\r\n用框数量：' + orderInfo.applyBox +
          '个\r\n退框数量：' + orderInfo.retreatBox +
          '个\r\n订单总额：￥' + orderInfo.totalPrice,
        success(res) {
          if (res.confirm) {
            // 修改一下品种和规格的中文字段
            orderInfo.orders = orderInfo.orders.map(order => {
              order.categoryValue = that.data.categoryArray[order.categoryValue]; 
              order.sizeValue = that.data.sizeArray[order.sizeValue];
              return order;
            });
            REQUEST.request('order/insertOrderInfo', 'POST', orderInfo, {
              'content-type': 'application/json;charset=utf-8'
            }).
            then(res => {
              if (res.data.code == 20000) {
                that.bindDeleteOrderInfo();
              } else {
                wx.showModal({
                  title: '提示',
                  content: res.data.msg,
                  showCancel: false
                })
              }
            })
          }
        }
      })
    },
    // 删除订单项
    delThisOrder() {
      let that = this;
      wx.showModal({
        title: '提示',
        content: "确定要删除此订单？",
        success(res) {
          if (res.confirm) {
            that.bindDeleteOrderInfo();
          }
        }
      })
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