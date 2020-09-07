// pages/add/add.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
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
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})