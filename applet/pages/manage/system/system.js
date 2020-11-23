const REQUEST = require('../../../utils/request');

// pages/manage/system/system.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 是否启用编辑
    isDisabled: true,

    // 参数列表
    sysInfo: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getSysInfo();
  },
  getSysInfo() {
    let that = this;
    REQUEST.request('manage/getSysInfo', 'POST', {
      token: wx.getStorageSync('token'),
    }).then(res => {
      if (res.data.code == 20000) {
        that.setData({
          sysInfo: res.data.data
        })
      }
    })
  },
  // 修改参数启用状态
  switchBoxChange(e) {
    let sysInfo = this.data.sysInfo;
    sysInfo[e.currentTarget.dataset.key][e.currentTarget.dataset.index].status = e.detail.value;
    sysInfo[e.currentTarget.dataset.key][e.currentTarget.dataset.index].flag = 1;
    this.setData({
      sysInfo
    })
  },
  // 添加参数
  addOrDelInfo(e) {
    let param = e.currentTarget.dataset;
    let sysInfo = this.data.sysInfo;
    if (param.value == "add") {
      sysInfo[param.type].push({
        id: "",
        key: param.type,
        type: "web",
        value: "新增",
        status: false
      })
    } else {
      if (sysInfo[param.type].length > 1) {
        let delInfo = sysInfo[param.type].pop();
        if (delInfo.id) {
          wx.showModal({
            content: '只能删除新增数据！',
            showCancel: false
          })
          sysInfo[param.type].push(delInfo);
        }
      } else {
        wx.showModal({
          content: '参数列表不可为空！',
          showCancel: false
        })
      }
    }
    this.setData({
      sysInfo
    })
  },
  // 开启编辑
  inputDisable() {
    this.setData({
      isDisabled: !this.data.isDisabled
    })
  },
  // 输入框失去焦点
  updateInfo(e) {
    let sysInfo = this.data.sysInfo;
    if (sysInfo[e.currentTarget.dataset.key][e.currentTarget.dataset.index].value != e.detail.value) {
      sysInfo[e.currentTarget.dataset.key][e.currentTarget.dataset.index].flag = 1;
    }
    sysInfo[e.currentTarget.dataset.key][e.currentTarget.dataset.index].value = e.detail.value;
    this.setData({
      sysInfo
    })
  },
  // 返回
  rollback() {
    wx.navigateBack()
  },
  // 提交
  submitInfo() {
    let that = this;
    // 提交
    wx.showModal({
      title: '确认提交？',
      success(res) {
        if (res.confirm) {
          REQUEST.request('manage/updateSystemInfo', 'POST', {
            sysInfo: JSON.stringify(that.data.sysInfo)
          }).then(res => {
            if (res.data.code == 20000) {
              wx.navigateBack()
            }
          })
        }
      }
    })
  }
})