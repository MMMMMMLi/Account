import * as echarts from '../../../../components/ec-canvas/echarts';
const UTIL = require('../../../../utils/util');
const REQUEST = require('../../../../utils/request');

Page({

  data: {
    // 下面为四个 图表 信息
    oneBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        barChart.setOption(getBarOption());

        return barChart;
      }
    },
    twoBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        barChart.setOption(getBarOption());

        return barChart;
      }
    },
    threeBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        barChart.setOption(getBarOption());

        return barChart;
      }
    },
    fourBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        barChart.setOption(getBarOption());

        return barChart;
      }
    },
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // 获取页面的 展示时间
    const weekArray = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    var nowDate = new Date();
    var yy = nowDate.getFullYear();
    var mm = nowDate.getMonth() + 1;
    var dd = nowDate.getDate();
    // 设置
    this.setData({
      dateTime: yy + '年' + mm + '月' + dd + '号 ' + weekArray[nowDate.getDay()],
      dateTimeS: UTIL.getLunarDay(yy, mm, dd)
    })
  },
})

function getBarOption() {
  return {
    title: {
      text: '各品种销量情况'
    },
    tooltip: {},
    legend: {
      data: ['上半年', '下半年'],
      orient: 'vertical', // 布局方式，默认为水平布局 // 'horizontal' ¦ 'vertical
      selectedMode: false, //取消图例上的点击事件
    },
    xAxis: {
      data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"]
    },
    yAxis: [{
      splitLine: { //网格线
        lineStyle: {
          type: 'dashed', //设置网格线类型 dotted：虚线   solid:实线
          color: '#ededed' //网格线颜色
        },
        show: true //隐藏或显示   false时隐藏网格线
      },
    }],
    series: [{
        name: '上半年',
        type: 'bar',
        data: [5, 20, 36, 10, 10, 20],
      },
      {
        name: '下半年',
        type: 'bar',
        data: [15, 6, 26, 15, 12, 10],
      }
    ]
  };
}