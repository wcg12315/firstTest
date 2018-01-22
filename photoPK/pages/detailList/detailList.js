const app = getApp()

Page({
  onLoad: function () {
    wx.request({
      url: 'http://127.0.0.1:8080/ImgPkService/user/all?pageNum=3&pageSize=2',
      method: 'POST',
      data: {},
      header: {
        'content-type': 'application/x-www-form-urlencoded'
      },
      success: function (res) {
        if (app.globalData.userInfo) {
          this.setData({
            userInfo: app.globalData.userInfo,
            hasUserInfo: true
          })
        }
      },
      fail: function (res) {
        console.log(res)
      }
    });
  }
});