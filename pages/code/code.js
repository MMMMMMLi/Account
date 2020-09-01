if (isLogined) {
  this.doneShow();
} else {
  wx.showModal({
    title: '提示',
    content: '本次操作需要您的登录授权',
    cancelText: '暂不登录',
    confirmText: '前往登录',
    success(res) {
      if (res.confirm) {
        wx.switchTab({
          url: "/pages/my/index"
        })
      } else {
        wx.navigateBack()
      }
    }
  })
}