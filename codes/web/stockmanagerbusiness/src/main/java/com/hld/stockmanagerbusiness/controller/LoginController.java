package com.hld.stockmanagerbusiness.controller;

import com.hld.stockmanagerbusiness.bean.UserInfo;
import com.hld.stockmanagerbusiness.service.LoginService;
import com.hld.stockmanagerbusiness.service.SMSService;
import com.hld.stockmanagerbusiness.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController extends BaseController{
    @Autowired
    LoginService loginService;
    @Autowired
    SMSService smsService;
    @Autowired
    WeChatService weChatService;

    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request,String openid,String nickname,String sex,
                                    String province,String city,String headimgurl,String unionid,String privilege){
        UserInfo userInfo=loginService.doLogin( openid, nickname, sex,province, city, headimgurl, unionid, privilege);
        if(userInfo==null){
            return getErrorMap(ERROR_CODE_LOGIN_FAILD,"授权失败，请您重试!");
        }
        return getSuccessMap(userInfo);
    }

    @RequestMapping(value="/loginWx",method = RequestMethod.POST)
    @ResponseBody
    public Object loginWx(HttpServletRequest request,String encryptedData,String iv,String code){
        UserInfo userInfo=loginService.doLoginWx(encryptedData,iv,code);
        if(userInfo==null){
            return getErrorMap(ERROR_CODE_LOGIN_FAILD,"授权失败，请您重试!");
        }
        return getSuccessMap(userInfo);
    }



    @RequestMapping(value="/uploadWeChatFormId",method = RequestMethod.POST)
    @ResponseBody
    public Object uploadWeChatFormId(HttpServletRequest request){
        return weChatService.uploadWeChatFormId(request);
    }


    //发送验证码

//    @RequestMapping(value="/sendAuthCode",method = RequestMethod.POST)
//    @ResponseBody
//    public Object sendAuthCode(String phoneNum){
////        smsService.sendAuthCode();
//        return null;
//    }

}
