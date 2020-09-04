// components/orderList/index.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    orderList:{type:Array,value:''},
    goodsList:{type:Map,value:''},
    windowHeight:{type:Number,value:'1000'}
  },

  /**
   * 组件的初始数据
   */
  data: {
    orderList:[],
    goodsList:{}
  },

  /**
   * 组件的方法列表
   */
  methods: {

  }
})
