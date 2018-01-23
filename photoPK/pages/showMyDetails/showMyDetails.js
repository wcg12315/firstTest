var util = require('../../utils/util.js');
var Charts = require("../../utils/chart.js");


//获取应用实例
const app = getApp()
Page({
  data: {
    windowWidth: wx.getSystemInfoSync().windowWidth
  },
  onLoad: function () {
    wx.setNavigationBarTitle({
      title: '门户PK小程序',
    })
    var that = this
    var userInfo = app.globalData.userInfo;
    wx.request({
      url: 'http://127.0.0.1:8080/ImgPkService/user/showMyBestDetails?userId=' + app.globalData.OPEN_ID,
      method: 'POST',
      data: {},
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        that.setData({
          imgPath: 'http://127.0.0.1:8080/ImgPkService/user/' + res.data.picId
        });
        
        new Charts({
          canvasId: 'myRadarPic',
          type: 'radar',
          categories: ['色彩对比', '光影对比', '大小对比', '动静对比', '题材对比', '多重对比'],
          series: [{
            name: '评分数据',
            data: [res.data.colour, res.data.shadow, res.data.size, res.data.activity, res.data.theme, res.data.multiple]
          }],
          width: 300,
          height: 300,
          extra: {
            radar: {
              max: 10
            }
          }
        });
      },
      fail: function (res) {
        console.log(res)
      }
    });
  }
})