const REQUEST = require('../../utils/request');
const APP = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    statusType: [{
        status: 0,
        label: '今日'
      },
      {
        status: 3,
        label: '三天'
      },
      {
        status: 7,
        label: '一周'
      },
      {
        status: 999,
        label: '全部'
      },
    ],
    // 当前选中的 列表值
    currentTab: 0,
    // 分页信息
    page: 0,
    // 每页显示的条目数
    size: 5,
    // 是否包含下一页
    hasNextPage: false,
    // 订单列表
    oldOrderList: [],
    // 筛选页面的是否显示
    needFilter: false,
    // 订单页面是否开启筛选
    filterFalg: false,
    // 筛选条件
    filterCriteria: [],
    // 筛选选择框
    filterPicker: [],
  },
  // 每个订单 点击 修改订单 按钮
  updateOrderInfo(e) {
    wx.setStorageSync('orderInfo', e.currentTarget.dataset.index);
    // 跳转到  添加页面
    wx.switchTab({
      url: '/pages/add/add',
    })
  },
  // 每个订单 点击 确认收款 按钮
  confirmCollection(e) {
    let that = this;
    wx.showModal({
      title: "提示",
      content: "确认收到此货款？",
      showCancel: false,
      success(res) {
        if (res.confirm) {
          REQUEST.request('order/confirmCollection', 'POST', {
            token: wx.getStorageSync('token'),
            orderId: e.currentTarget.dataset.index
          }).then(res => {
            if (res.data.code == 20008) {
              wx.showToast({
                title: '收款成功！',
              })
              setTimeout(() => {
                // 清空订单列表
                that.setData({
                  page: 0,
                  orderList: []
                })
                that.getData(that.data.currentTab, true);
                wx.hideToast()
              }, 700)

            } else {
              wx.showToast({
                icon: 'none',
                title: '收款失败，请稍后重试！',
              })
            }
          })
        }
      }
    })
  },
  // 点击是否开启筛选按钮
  switchBoxChange(e) {
    let data = {};
    // 如果取消筛选的时候,需要将筛选条件数组清空
    if (!e.detail.value) {
      // 筛选条件数据
      data.filterCriteria = [];
      data.firstFilterPickerValue = 0;
      data.secondtFilterPickerValue = 1;
      data.threetFilterPickerValue = 2;
      // 清空订单列表
      data.orderList = [];
    }
    // 切换筛选条件
    data.needFilter = e.detail.value;
    data.filterFalg = e.detail.value;
    this.setData(data);
    // 如果关掉了筛选,则根据当前标志位重新获取数据
    if (!e.detail.value) {
      this.getData(this.data.currentTab, true);
    }
  },
  // 打开筛选界面
  openFilter() {
    this.setData({
      needFilter: true
    })
  },
  // 筛选界面返回按钮
  closeFilter() {
    this.setData({
      needFilter: false,
      // 清空订单列表
      orderList: []
    })
    //动态获取数据
    this.getData(this.data.currentTab, true);
  },
  // 筛选列表的选择框点击操作
  categoryPickerChange(e) {
    let data = {};
    // 设置一下当前选择框的选择内容
    data[e.currentTarget.dataset.type] = e.detail.value;
    // 修改一下,筛选条件
    let filterCriteria = this.data.filterCriteria;
    // 如果没有筛选条件,则不操作.
    if (filterCriteria.length > 0) {
      // 看看当前选择框的位置是否已经添加了筛选条件
      let oldFilter = filterCriteria[e.currentTarget.dataset.index];
      if (oldFilter != null) {
        // 修改数据
        filterCriteria[e.currentTarget.dataset.index] = {
          key: this.data.filterPicker[e.detail.value].value,
          value: filterCriteria[e.currentTarget.dataset.index].value
        };
        data.filterCriteria = filterCriteria;
      }
    }
    // 设置数据
    this.setData(data);
  },
  // 筛选列表的输入框失去焦点操作
  overInput(e) {
    // 获取当前data数据
    let data = this.data;
    // 获取筛选列表的条件集合
    let filterCriteria = data.filterCriteria;
    // 根据当前传入的type获取当前筛选的条件
    let key = data.filterPicker[data[e.currentTarget.dataset.type]];
    // 创建新的对象
    let minedata = {
      // 填充筛选条件
      key: key.value,
      value: e.detail.value
    };
    // 修改对应的筛选条件
    filterCriteria[e.currentTarget.dataset.index] = minedata;
    this.setData({
      filterCriteria
    })
  },
  // 按钮切换的操作。
  swichNav: function (e) {
    let that = this;
    let current = e.target.dataset.current;
    if (this.data.currentTab === current) {
      return false;
    } else {
      that.setData({
        currentTab: current,
        page: 0,
        orderList: []
      })
      //动态获取数据
      that.getData(current, true);
    }
  },
  // 获取数据
  getData(status, showLoading) {
    let that = this;
    if (showLoading) {
      wx.showLoading({
        title: '加载中',
      })
    }
    let oldOrderList = that.data.orderList ? that.data.orderList : [];
    // 根据传入的状态码，去后台获取订单列表
    REQUEST.request('order/getOrderList', 'POST', {
      token: wx.getStorageSync('token'),
      status,
      page: that.data.page,
      size: that.data.size,
      filterList: JSON.stringify(that.data.filterFalg ? that.data.filterCriteria : [])
    }).then(res => {
      if (res.data.code === 20000) {
        that.setData({
          orderList: oldOrderList.concat(res.data.data),
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
        })
      } else {
        if (res.data.code === 40002) {
          // do nothing
          return;
        }
        wx.showModal({
          content: res.data.msg,
          showCancel: false
        })
      }
    })
    if (showLoading) {
      wx.hideLoading();
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      filterPicker: [{
          key: '品种',
          value: 'detail.categoryValue'
        },
        {
          key: '大小',
          value: 'detail.sizeValue'
        },
        // {
        //   key: '订单总价钱',
        //   value: 'order.totalPrice'
        // },
        // {
        //   key: '订单总重量',
        //   value: 'order.totalWeight'
        // },
        {
          key: '姓名',
          value: 'user.userName,user.userNameCode'
        },
        {
          key: '手机号',
          value: 'user.phoneNumber'
        },
      ],
      firstFilterPickerValue: 0,
      secondtFilterPickerValue: 1,
      threetFilterPickerValue: 2
    })
  },
  onShow: function (options) {
    // 获取数据
    this.getData(this.data.currentTab, true);

  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {
    setTimeout(() => {
      this.hideAsync();
    }, 1000)
  },
  hideAsync() {
    let that = this;
    return new Promise((resolve, reject) => {
      // 离开的时候，默认选择 今日 tab栏。
      that.setData({
        currentTab: 0,
        page: 0,
        orderList: []
      })
      // 每次打开页面,都需要回到顶部。
      if (wx.pageScrollTo) {
        wx.pageScrollTo({
          scrollTop: 0,
        })
      }
      // // 获取数据
      // that.getData(that.data.currentTab, false);
    })
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getData(this.data.currentTab, false);
    }
  },
})