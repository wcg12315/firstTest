//app.js
const APP_ID = 'wx0e8e83a02fd4d4de';//输入小程序appid  
const APP_SECRET = '6df8f1a591bac78e205c3aea4c7e2382';//输入小程序app_secret  
var OPEN_ID;
var SESSION_KEY;
App({
  onLaunch: function () {

    wx.setNavigationBarTitle({
      title: '门户PK小程序',
    })

    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          //获取openid接口
          url: 'https://api.weixin.qq.com/sns/jscode2session',
          data: {
            appid: APP_ID,
            secret: APP_SECRET,
            js_code: res.code,
            grant_type: 'authorization_code'
          },
          method: 'GET',
          success: function (res) {
            console.log(res.data)
            OPEN_ID = res.data.openid;//获取到的openid
            SESSION_KEY = res.data.session_key;//获取到session_key
            console.log(OPEN_ID.length)
            console.log(SESSION_KEY.length)
          }
        });
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              this.globalData.OPEN_ID = OPEN_ID;//获取到的openid
              this.globalData.SESSION_KEY = SESSION_KEY;//获取到session_key

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          });
        }
      }
    })
  },
  globalData: {
    userInfo: null,
    OPEN_ID: null,
    SESSION_KEY: null
  }
})