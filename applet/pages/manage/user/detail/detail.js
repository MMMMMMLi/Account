const REQUEST = require('../../../../utils/request');
const UTIL = require('../../../../utils/util');

// pages/manage/user/detail/detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 分页信息
    page: 0,
    size: 10,
    hasNextPage: false,
    // 展示信息
    userName: '',
    phoneNumber: '',
    address: '',
    gender: '',
    updateTime: '',
    orders: 0,
    isTemp: false,
    isFrozen:false,
    detailsMap: {},

    // 归并用户页面
    mergeFlag: false,
    // 归并的用户
    checkedUserId: ''
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (params) {
    this.setData({
      userId: params.userId,
      orders: params.orders,
      money: params.money
    })
    this.getUserDetails();
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    // 页面上拉触底事件的处理函数
    if (this.data.hasNextPage) {
      this.getUserDetails();
    }
  },
  getUserDetails() {
    let that = this;
    REQUEST.request('manage/getUserDetails', 'POST', {
      token: wx.getStorageSync('token'),
      userId: this.data.userId,
      page: this.data.page,
      size: this.data.size
    }).then(res => {
      if (res.data.code == 20000) {
        let details = res.data.pageInfo.list;
        let detailsMap = that.data.detailsMap;
        // 处理数据
        details.filter(deatil => deatil.hasOwnProperty('totalPrice'))
          .forEach(deatil => {
            if (detailsMap.hasOwnProperty(deatil.createDate)) {
              detailsMap[deatil.createDate].push(deatil);
            } else {
              detailsMap[deatil.createDate] = [deatil];
            }
          })
        that.setData({
          userName: details[0].userName || '',
          phoneNumber: details[0].phoneNumber || '',
          address: details[0].address || '',
          gender: details[0].gender || '',
          updateTime: details[0].updateTime || '',
          isTemp: details[0].isTemp || false,
          isFrozen: details[0].isFrozen || false,
          detailsMap,
          page: res.data.pageInfo.nextPage,
          hasNextPage: res.data.pageInfo.hasNextPage
        })
      } // 校验Token失败，等待1S之后重新拉取数据
      if (res.data.code === 40002) {
        setTimeout(() => {
          if (wx.getStorageSync('token')) {
            setTimeout(() => {
              that.getUserDetails();
            }, 1000)
          }
        }, 1000)
        return;
      }
    })
  },
  // 呼叫用户
  async callNumber(res) {
    const number = res.currentTarget.dataset.number;
    let ModalRes = await UTIL.showModaling('提示','是否呼叫：'+number,'呼叫');
    if (ModalRes.confirm) {
      wx.makePhoneCall({
        phoneNumber: number,
        complete(res) {}
      })
    }
  },
  // 归并用户
  async mergeUser() {
    let res = await REQUEST.request('manage/getMergeUserInfo', 'POST', {
      token: wx.getStorageSync('token'),
      userId: this.data.userId,
      userName: this.data.userName,
      phoneNumber: this.data.phoneNumber
    });
    let data = res.data.data;
    if (res.data.code === 20000 && data.length > 0) {
      data[0].checked = true;
      this.setData({
        mergeUserList: data,
        mergeFlag: true,
        mergeViewHeight: 330 + 120 * data.length,
        checkedUserId: data[0].id
      })
    } else {
      UTIL.showModaling('提示', '暂无匹配用户！', null, null, false);
    }
  },
  // 切换选择归并用户
  radioChange(e) {
    this.setData({
      checkedUserId: e.detail.value
    })
  },
  // 提交归并
  async processMerge() {
    if (this.data.checkedUserId) {
      // 处理归并
      let res = await REQUEST.request('manage/mergeByUserId','POST',{
        token: wx.getStorageSync('token'),
        userId: this.data.userId,
        mergeUserId: this.data.checkedUserId
      })
      if(res.data.code === 20000) {
        wx.navigateBack()
      }else {
        await UTIL.showToasting('归并失败！', 'error');
      }
    } else {
      this.setData({
        mergeFlag: false,
        checkedUserId: ''
      });
      await UTIL.showToasting('未知错误！', 'error');
    }
  },
  // 暂不归并
  cancelMerge() {
    this.setData({
      mergeFlag: false,
      checkedUserId: ''
    });
    UTIL.showToasting('已取消', 'error');
  },
  // 冻结/解冻 用户
  async frozenUser(e) {
    const state = e.currentTarget.dataset.state;
    let res = await REQUEST.request('manage/frozenByUserId','POST',{
      token: wx.getStorageSync('token'),
      userId: this.data.userId,
      state
    })
    if(res.data.code === 20000) {
      this.setData({
        detailsMap: {}
      })
      this.getUserDetails();
      await UTIL.showToasting('修改成功！', 'success')
      await UTIL.hideToasting();
    }else {
      await UTIL.showToasting('修改失败！', 'error');
    }
  },
})