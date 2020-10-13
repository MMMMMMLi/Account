const REQUEST = require('./request');

// 检测登录状态，返回 true / false
async function checkHasLogined() {
  const token = wx.getStorageSync('token')
  if (!token) {
    return false;
  }
  const loggined = await checkSession()
  if (!loggined) {
    wx.removeStorageSync('token');
    return false;
  }
  // 同步校验Token
  let flag = true;
  await REQUEST.request('applet/auth/checkToken', 'POST', {
    token: token
  }).then(async res => {
    if (res.data.code != 20003) {
      wx.removeStorageSync('token')
      flag = false;
    }
  })
  return flag;
}

// 校验当前用户是否登陆
async function checkSession() {
  return new Promise((resolve, reject) => {
    wx.checkSession({
      success() {
        return resolve(true)
      },
      fail() {
        return resolve(false)
      }
    })
  })
}

// 获取用户登陆信息
async function login(page) {
  wx.login({
    success: function (res) {
      REQUEST.request('applet/auth/code2Session', 'POST', {
        code: res.code,
      }).then(res => {
        if (res.data.code != 20004) {
          // 登录错误
          wx.showModal({
            title: '无法登录',
            content: res.data.msg,
            showCancel: false
          })
          return;
        }
        wx.setStorageSync('token', res.data.data);
        if (page) {
          page.onShow()
        }
      })
    }
  })
}

// what?
async function wxaCode() {
  return new Promise((resolve, reject) => {
    wx.login({
      success(res) {
        return resolve(res.code)
      },
      fail() {
        wx.showToast({
          title: '获取code失败',
          icon: 'none'
        })
        return resolve('获取code失败')
      }
    })
  })
}

// 获取用户信息
async function getUserInfo() {
  return new Promise((resolve, reject) => {
    wx.getUserInfo({
      success: res => {
        return resolve(res)
      },
      fail: err => {
        console.error(err)
        return resolve()
      }
    })
  })
}

// 推出登陆
function loginOut() {
  wx.removeStorageSync('token')
}

// 校验并提示用户授权获取登陆信息
async function checkAndAuthorize(scope) {
  return new Promise((resolve, reject) => {
    wx.getSetting({
      success(res) {
        if (!res.authSetting[scope]) {
          wx.authorize({
            scope: scope,
            success() {
              resolve() // 无返回参数
            },
            fail(e) {
              console.error(e)
              // if (e.errMsg.indexof('auth deny') != -1) {
              //   wx.showToast({
              //     title: e.errMsg,
              //     icon: 'none'
              //   })
              // }
              wx.showModal({
                title: '无权操作',
                content: '需要获得您的授权',
                showCancel: false,
                confirmText: '立即授权',
                confirmColor: '#e64340',
                success(res) {
                  wx.openSetting();
                },
                fail(e) {
                  console.error(e)
                  reject(e)
                },
              })
            }
          })
        } else {
          resolve() // 无返回参数
        }
      },
      fail(e) {
        reject(e)
      }
    })
  })
}


module.exports = {
  checkHasLogined: checkHasLogined,
  wxaCode: wxaCode,
  getUserInfo: getUserInfo,
  login: login,
  loginOut: loginOut,
  checkAndAuthorize: checkAndAuthorize
}