var util = require('../../utils/util.js')
//获取应用实例
const app = getApp()
Page({
  data: {
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
         wx.request({
           url: 'http://127.0.0.1:8080/ImgPkService/user/all?pageNum=3&pageSize=2',
          method: 'POST',
          data: {},
          header: {
            'content-type': 'application/x-www-form-urlencoded'
          },
          success: function (res) {
            console.log(res);
          },
          fail: function (res) {
            console.log(res)
          }
        });
         var userInfo = app.globalData.userInfo;
         console.log(userInfo)
        wx.uploadFile({
          url: 'http://127.0.0.1:8080/ImgPkService/user/upload',
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