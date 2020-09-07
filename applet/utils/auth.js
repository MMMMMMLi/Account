// 检测登录状态，返回 true / false
async function checkHasLogined() {
  // const token = wx.getStorageSync('token')
  // if (!token) {
  //   return false
  // }
  // const loggined = await checkSession()
  // if (!loggined) {
  //   wx.removeStorageSync('token')
  //   return false
  // }

  const code = wx.getStorageSync('code');
  if (!code) {
    return false
  }
  const userInfo = wx.getStorageSync('userInfo');
  if (!userInfo) {
    return false
  }
  return true;
}


async function getUserInfo() {
  return new Promise((resolve, reject) => {
    wx.getUserInfo({
      success: res => {
        // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        // 所以此处加入 callback 以防止这种情况
        if (this.userInfoReadyCallback) {
          this.userInfoReadyCallback(res)
        }
        return resolve(res)
      },
      fail: err => {
        console.error(err)
        return resolve()
      }

    })
  })
}

async function login(page) {
  // wx.login({
  //   success: function (res) {
  //     wx.setStorageSync('code', res.code)
  //   }
  // })
  // 获取用户信息
  wx.getSetting({
      success: res => {
        console.log(res);
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
         this.getUserInfo().then(res => {
          console.log(res);
         })
      }
    }
  })
}


async function getOrderList(page) {
  wx.request({
    url: 'http://localhost:11000/test/order',
    timeout:5000,
    data: {
      page:1,
      size:1
    },
    success:function(res) {
      console.log(res);
    },
    fail:function(res){
      console.log(res);
    },
    complete:function(res){
      console.log(res);
    }
  })


}



module.exports = {
  checkHasLogined: checkHasLogined,
  getUserInfo: getUserInfo,
  login: login,
  getOrderList:getOrderList
}