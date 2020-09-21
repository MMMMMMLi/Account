// const preUrl = 'http://localhost:11001/api/'https://imengli.com/
const preUrl = 'https://imengli.com/api/'
let header = {
  'content-type': 'application/x-www-form-urlencoded',
  'token': ''
}
//获取本地缓存的openid 通过heder发送给后台
wx.getStorage({
  key: 'token',
  success(e) {
    header.token = e.data
  }
})
// 主要执行方法
function request(url, method, data) {
  if (!header.token) {
    wx.getStorageSync({
      key: 'token',
      success(e) {
        header.token = e.data;
      }
    })
  }
  let promise = new Promise((resolve, reject) => {
    wx.request({
      url: preUrl + url,
      method: method,
      data: data,
      header: header,
      success: (res => {
        resolve(res);
      }),
      fail: (res => {
        wx.showToast({
          title: '网络出错',
          icon: 'none',
          duration: 1500
        })
        reject('网络出错');
      })
    })
  })
  return promise;
}

module.exports = {
  request: request
}