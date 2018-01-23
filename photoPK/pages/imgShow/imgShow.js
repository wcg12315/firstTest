var util = require('../../utils/util.js')
//获取应用实例
const app = getApp()
Page({
  data: {
  },
  onLoad: function () {
    wx.setNavigationBarTitle({
      title: '门户PK小程序',
    })
  },
  gotoShow: function () {
    var _this = this
    wx.chooseImage({
      count: 9, // 最多可以选择的图片张数，默认9
      sizeType: ['original', 'compressed'], // original 原图，compressed 压缩图，默认二者都有
      sourceType: ['album', 'camera'], // album 从相册选图，camera 使用相机，默认二者都有
      success: function (res) {
        // success
        console.log(res)
        _this.setData({
          src: res.tempFilePaths
        });
        var userInfo = app.globalData.userInfo;
        console.log(userInfo)
        wx.uploadFile({
          url: app.globalData.REST_SERVICE + 'user/upload',
          filePath: res.tempFilePaths[0],　//待上传的图片，由 chooseImage获得
          name: 'pk_image',
          formData: {
            userId: app.globalData.OPEN_ID
          }, // HTTP 请求中其他额外的 form data
          success: function (res) {
            console.log("save success", res);
          },
          fail: function (res) {
            console.log("save fail", res);
          },
        });
      },
      fail: function () {
        // fail
      },
      complete: function () {
        // complete
      }
    })
  }
})