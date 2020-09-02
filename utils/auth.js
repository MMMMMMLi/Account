// const WXAPI = require('apifm-wxapi')

// async function checkSession(){
//   return new Promise((resolve, reject) => {
//     wx.checkSession({
//       success() {
//         return resolve(true)
//       },
//       fail() {
//         return resolve(false)
//       }
//     })
//   })
// }

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
  console.log("auth checkHasLogined code : ",code)
  if (!code) {
    return false
  }
  const userInfo = wx.getStorageSync('userInfo');
  console.log("auth checkHasLogined userInfo : ",userInfo)
  if (!userInfo) {
    return false
  }
  return true;
}

// async function wxaCode(){
//   return new Promise((resolve, reject) => {
//     wx.login({
//       success(res) {
//         return resolve(res.code)
//       },
//       fail() {
//         wx.showToast({
//           title: '获取code失败',
//           icon: 'none'
//         })
//         return resolve('获取code失败')
//       }
//     })
//   })
// }

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
         console.log(this.getUserInfo());
         this.getUserInfo().then(res => {
          console.log(res);
         })
      }
    }
  })
}

// async function register(page) {
//   let _this = this;
//   wx.login({
//     success: function (res) {
//       let code = res.code; // 微信登录接口返回的 code 参数，下面注册接口需要用到
//       wx.getUserInfo({
//         success: function (res) {
//           let iv = res.iv;
//           let encryptedData = res.encryptedData;
//           let referrer = '' // 推荐人
//           let referrer_storge = wx.getStorageSync('referrer');
//           if (referrer_storge) {
//             referrer = referrer_storge;
//           }
//           // 下面开始调用注册接口
//           WXAPI.register_complex({
//             code: code,
//             encryptedData: encryptedData,
//             iv: iv,
//             referrer: referrer
//           }).then(function (res) {
//             _this.login(page);
//           })
//         }
//       })
//     }
//   })
// }

// function loginOut(){
//   wx.removeStorageSync('token')
//   wx.removeStorageSync('uid')
// }

// async function checkAndAuthorize (scope) {
//   return new Promise((resolve, reject) => {
//     wx.getSetting({
//       success(res) {
//         if (!res.authSetting[scope]) {
//           wx.authorize({
//             scope: scope,
//             success() {
//               resolve() // 无返回参数
//             },
//             fail(e){
//               console.error(e)
//               // if (e.errMsg.indexof('auth deny') != -1) {
//               //   wx.showToast({
//               //     title: e.errMsg,
//               //     icon: 'none'
//               //   })
//               // }
//               wx.showModal({
//                 title: '无权操作',
//                 content: '需要获得您的授权',
//                 showCancel: false,
//                 confirmText: '立即授权',
//                 confirmColor: '#e64340',
//                 success(res) {
//                   wx.openSetting();
//                 },
//                 fail(e){
//                   console.error(e)
//                   reject(e)
//                 },
//               })
//             }
//           })
//         } else {
//           resolve() // 无返回参数
//         }
//       },
//       fail(e){
//         console.error(e)
//         reject(e)
//       }
//     })
//   })  
// }


module.exports = {
  checkHasLogined: checkHasLogined,
  // wxaCode: wxaCode,
  getUserInfo: getUserInfo,
  login: login,
  // register: register,
  // loginOut: loginOut,
  // checkAndAuthorize: checkAndAuthorize
}