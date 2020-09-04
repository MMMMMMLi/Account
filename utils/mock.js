async function mockData() {
  return { orderList: [{
    "id": 5298,
    "index": 1,
    "orderTime": " 2020-09-03 11:15:17",
    "orderState": "未支付",
    "goodsNumber": 3,
    "backFrame": 3,
    "useFrame": 18,
    "totalWeight": 570,
    "totalPrice": 8000,
  },
  {
    "id": 5297,
    "index": 2,
    "orderTime": " 2020-09-03 11:15:17",
    "orderState": "已支付",
    "goodsNumber": 3,
    "backFrame": 3,
    "useFrame": 18,
    "totalWeight": 570,
    "totalPrice": 8000,
  },
  {
    "id": 5296,
    "index": 3,
    "orderTime": " 2020-09-03 11:15:17",
    "orderState": "已支付",
    "goodsNumber": 3,
    "backFrame": 3,
    "useFrame": 18,
    "totalWeight": 570,
    "totalPrice": 8000,
  }
],
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
    ],
    "5296": [{
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
  ],
    "5297": [{
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
  }}

}


module.exports = {
  mockData :mockData
}