import * as echarts from '../../../../components/ec-canvas/echarts';
const UTIL = require('../../../../utils/util');
const REQUEST = require('../../../../utils/request');

Page({

  data: {
    // 下面为四个 图表 信息
    categoryBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        getBarOption('category').then(res => {
          barChart.setOption(res);
          return barChart;
        })
      }
    },
    sizeBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        getBarOption('size').then(res => {
          barChart.setOption(res);
          return barChart;
        })
      }
    },
    personBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        getBarOption('person').then(res => {
          barChart.setOption(res);
          return barChart;
        })
      }
    },
    timeBar: {
      onInit: function (canvas, width, height, dpr) {
        const barChart = echarts.init(canvas, null, {
          width: width,
          height: height,
          devicePixelRatio: dpr // new
        });
        canvas.setChart(barChart);
        getBarOption('time').then(res => {
          barChart.setOption(res);
          return barChart;
        })
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
// 根据不同的type获取不同图表的 Option
async function getBarOption(type) {
  // 显示的标题数组
  const barTitle = {
    category: '各品种销量情况/KG',
    size: '各型号销量情况/KG',
    person: '交易额排行/元',
    time: '时间段销售情况'
  };
  // 颜色列表
  const colorArray = Array('DeepSkyBlue','Orange','LightPink','DarkTurquoise','IndianRed');
  let option = '';
  await REQUEST.request('manage/getReport', 'POST', {
    token: wx.getStorageSync('token'),
    type: type,
    state: 0
  }).then(res => {
    if (res.data.code == 20000) {
      const optionData = res.data.data;
      const optionSeries = [];
      // 拼装数据集
      for (let index = 0; index < optionData.series.length; index++) {
        const data = optionData.series[index];
        let color = colorArray[index];
        if(type == "person") {
          color = colorArray[index + 2];
        }
        if(type == "time") {
          color = colorArray[index + 3];
        }
        optionSeries.push({
          name: data.name,
          type: 'bar',
          data: data.data,
          color: color,
          itemStyle: {
            normal: {
              label: {
                show: true, //开启显示
                position: 'top', //在上方显示
                textStyle: { //数值样式
                  color: color,
                }
              }
            }
          },
        })
      }
      option = {
        title: {
          // 根据不同的type显示不同的标题
          text: barTitle[type],
        },
        tooltip: {},
        legend: {
          data: optionData.legend_data,
          orient: 'vertical', // 布局方式，默认为水平布局 // 'horizontal' ¦ 'vertical
        },
        xAxis: {
          data: optionData.xAxis_data,
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
        series: optionSeries
      }
    }
    if(res.data.code == 10000) {
      option = '';
    }
  })
  return option;
}