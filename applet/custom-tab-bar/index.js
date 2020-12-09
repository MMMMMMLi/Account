import {
  developer,
  master,
  averageUser
} from "../utils/tabBarUrl";

Component({
  data: {
    selected: 0,
    "color": "#6e6d6b",
    "selectedColor": "#1296db",
    "borderStyle": "white",
    "backgroundColor": "#fff",
    list: [],
    viewName: '',
    lastViewName: ''
  },
  lifetimes: {
    // 在组件实例进入页面节点树时赋值
    attached() {
      this.switchView();
    }
  },
  methods: {
    switchTab(e) {
      wx.switchTab({
        url: e.currentTarget.dataset.path,
      })
    },
    switchView() {
      let viewName = wx.getStorageSync('viewName');
      let list = averageUser;
      if (viewName == 'developer') {
        list = developer;
      }
      if (viewName == 'averageUser') {
        list = averageUser;
      }
      if (viewName == 'master') {
        list = master;
      }
      this.setData({
        list,
        viewName,
        lastViewName: viewName
      })
    },
  },
  observers: {
    'viewName': function (subfield) {
      if (this.data.lastViewName != this.data.viewName) {
        this.switchView();
      }
    },
  },
})