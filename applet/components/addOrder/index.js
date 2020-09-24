// components/addOrder/index.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    categoryArray: {
      type: Array,
      value: []
    },
    sizeArray: {
      type: Map,
      value: []
    },
  },

  /**
   * 组件的初始数据
   */
  data: {
    categoryValue: 0,
    sizeValue: 0,
  },

  /**
   * 组件的方法列表
   */
  methods: {
    // 选择框
    categoryPickerChange: function (e) {
      this.setData({
        categoryValue: e.detail.value
      })
    },
    // 选择框
    sizePickerChange: function (e) {
      this.setData({
        sizeValue: e.detail.value
      })
    },
  }
})