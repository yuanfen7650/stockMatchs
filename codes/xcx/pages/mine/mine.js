// pages/mine/mine.js
const httpUtil = require('../../utils/httpUtil.js')
const appParams = require('../../utils/appParams.js')
const category = require('../../utils/category.js')


const app = getApp()
var that;
var userId;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    info:{},
    openJoin:true,
    openHis:false
  },
  /**
     * 生命周期函数--监听页面显示
     */
  onShow: function () {
    that.onLoadMineInfo(userId);
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    that=this;
    userId= wx.getStorageSync("userId");
  },
  onLoadMineInfo: function (userId){
    httpUtil.doPost({
      app: app,
      url: appParams.queryMinAllInfo,
      data: {
        userId: "" + userId
      },
      success: function (res) {
        var data = res.data.data;



        that.onChanageMatchs(data.joinMathcs, data.userInfo.def_account_id);
        that.onChanageMatchs(data.hisMatchs, data.userInfo.def_account_id);

        that.setData({
          info: data
        });
      }
    });
  },
  onChanageMatchs:function(obj,defAccountId){
    if (obj != null) {
      for (var i = 0; i < obj.length; i++) {
        var item = obj[i];
        if (item.account_id == defAccountId){
          item.selected=true;
        }
        var nowTime = new Date().getTime();
        item.endDay = "结束倒计时:" + category.getTimestampDays((item.end_date - nowTime)/1000)+"天";

        item.end_date = category.transformTimestamp(item.end_date/1000);
        item.total_income = category.transformUnitChart(item.total_income, 2);
        item.total_income_rate = category.transformPercent(item.total_income_rate);
      }
    }
  },
  onJoinBtnClick:function(){
    that.setData({
      openJoin:!that.data.openJoin
    });
  },
  onHisBtnClick: function () {
    that.setData({
      openHis: !that.data.openHis
    });
  },
  //切换比赛
  cutMatch:function(e){
    wx.showLoading({
      title: '加载中',
    })
    console.log(e);
    // info.joinMathcs[e.currentTarget.id].account_id
    var item = that.data.info.joinMathcs[e.currentTarget.id];
    httpUtil.doPost({
      app: app,
      url: appParams.checkMatch,
      data: {
        userId: "" + userId,
        accountId: "" + item.account_id
      },
      success: function (res) {
        wx.hideLoading()
        that.onLoadMineInfo(userId);
        wx.setStorageSync("accountId", "" + item.account_id);
        wx.setStorageSync("matchId", "" + item.match_id);

      },
      fail:function(e){
        wx.hideLoading()
      }
    });
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})