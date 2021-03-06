//index.js
//获取应用实例
const appParams = require('../../utils/appParams.js')
const httpUtil = require('../../utils/httpUtil.js')
const app = getApp()
var that;
var matchId;
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  bindViewTap: function() {
    
  },
  onLoad: function (options) {
    that = this;
    matchId = options.matchId;
    if (matchId==null){
      matchId=1;
    }
    wx.setStorageSync("matchId", "" + matchId);
    // this.doAuth();
    that.getUserInfo();
  },
  getUserInfo:function(){
    this.doLogin();
  },
  doLogin:function(){
    wx.login({
      success: function (resLogin) {
        wx.getUserInfo({
          withCredentials: true,
          success: res => {
            app.globalData.userInfo = res.userInfo
            that.onLoadUserSuccess(res, resLogin.code);
            that.setData({
              userInfo: res.userInfo,
              hasUserInfo: true
            })
          }
        })
      }
    })
  },
  onLoadUserSuccess: function (detail,code){
    console.log("code:" + code);
    console.log("encryptedData:", detail);


    httpUtil.doPost({
      app: app,
      url: appParams.loginUrl,
      data: {
        encryptedData: '' + detail.encryptedData,
        iv: '' + detail.iv,
        code: '' + code
      },
      fail: function (e) {
        console.log("登录失败:-------------------------------------")
        console.log(e)

      },
      success: function (res) {
        console.log("登录成功:", res.data)
        var reqData = res.data.data;

        app.globalData.tokenUser = reqData;
        
        if (reqData.def_account_id > 0 && (reqData.def_match_id == matchId)){//有默认的比赛，直接进入默认比赛首页
          wx.setStorageSync("userId", "" + reqData.id);
          wx.setStorageSync("accountId", "" + reqData.def_account_id);
          wx.setStorageSync("matchId", "" + matchId);
          wx.switchTab({
            url: '/pages/ranking/ranking'
          });
          // wx.redirectTo({
          //   url: '/pages/study/class2/classOther'
          // });
        }else{//没有默认的比赛，跳转报名页面
          wx.redirectTo({
            url: '../applyMatch/applyMatch?matchId=' + matchId
          });
          // wx.redirectTo({
          //   url: '/pages/study/class2/classOther'
          // });
        }
      }
    });
  }
})