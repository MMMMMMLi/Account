// 获取当前程序APP
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    // 用户信息相关
    userInfo: {},
    hasUserInfo: true,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    // tab
    tabs: [],
    activeTab: 0,
    orderList: [{
      "id": 5298,
      "index": 1,
      "orderTime": " 2020-09-03 11:15:17",
      "orderState": "未支付",
      "goodsNumber": 3,
      "backFrame": 3,
      "useFrame": 18,
      "totalWeight": 570,
      "totalPrice": 8000,
    }],
    goodsList: {
      "5298": [{
          "imgUrl": "/images/big.png",
          "goods": [{
              "name": "西 瓜 红",
              "mao": 200,
              "shi": 50
            },
            {
              "name": "济 薯 26",
              "mao": 300,
              "shi": 150
            }
          ]
        },
        {
          "imgUrl": "/images/middle.png",
          "goods": [{
              "name": "西 瓜 红",
              "mao": 180,
              "shi": 20
            },
            {
              "name": "济 薯 26",
              "mao": 200,
              "shi": 50
            }
          ]
        },
        {
          "imgUrl": "/images/small.png",
          "goods": [{
              "name": "西 瓜 红",
              "mao": 400,
              "shi": 250
            },
            {
              "name": "济 薯 26",
              "mao": 200,
              "shi": 50
            }
          ]
        }
      ]
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    const titles = ['当日订单', '三天内订单', '七天内订单', '全部订单']
    const tabs = titles.map(item => ({title: item}))
    this.setData({tabs})
  },onTabCLick(e) {
    const index = e.detail.index
    this.setData({activeTab: index})
  },

  onChange(e) {
    const index = e.detail.index
    this.setData({activeTab: index})
  },
  /**
   * 获取用户信息的回调函数
   */
  processLogin(info) {
    console.log(info)
    app.globalData.userInfo = info.detail.userInfo
    this.setData({
      userInfo: info.detail.userInfo,
      hasUserInfo: true
    })
  },
  /**
   * 暂不登录
   */
  cancelLogin() {
    this.setData({
      hasUserInfo: true
    })
  }
})