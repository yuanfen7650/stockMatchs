package com.hld.stockmanagerbusiness.service.impl;

import com.hld.stockmanagerbusiness.bean.AccountInfo;
import com.hld.stockmanagerbusiness.bean.AuthCodeInfo;
import com.hld.stockmanagerbusiness.bean.MatchInfo;
import com.hld.stockmanagerbusiness.controller.BaseController;
import com.hld.stockmanagerbusiness.mapper.AccountMapper;
import com.hld.stockmanagerbusiness.mapper.MathMapper;
import com.hld.stockmanagerbusiness.mapper.MineMapper;
import com.hld.stockmanagerbusiness.mapper.SMSMapper;
import com.hld.stockmanagerbusiness.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImpl implements MatchService{
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    MathMapper mathMapper;
    @Autowired
    SMSMapper smsMapper;
    @Autowired
    MineMapper mineMapper;


    @Override
    public int applyMatch(String userId,String matchId, String name, String phoneNum, String authNum, String profession, String stuClass, String stuNum) {
        boolean flag=verifyAuthCode(phoneNum,authNum);
        if(!flag){//验证码错误
            return BaseController.ERROR_CODE_AUTH_CODE;
        }

        List<AccountInfo> list=accountMapper.queryAccountByUserId(userId+"",matchId+"");
        if(list==null|list.size()==0){//没有数据可以报名
            MatchInfo matchInfo=queryApplyMatchInfo(matchId);
            if(matchInfo!=null){
                mathMapper.addMatchUserNum(matchId);//比赛人数加1
                accountMapper.insertAccount( userId, matchId,  name,  phoneNum,  profession,  stuClass,  stuNum,matchInfo.getInit_total_assets(),matchInfo.getInit_total_assets(),matchInfo.getInit_total_assets());
                List<AccountInfo> list2=accountMapper.queryAccountByUserId(userId+"",matchId+"");
                if(list2.size()>0){//有值,报名成功后，将def_account_id改为此账户
                    AccountInfo info=list2.get(0);
                    accountMapper.updateDefAccount(userId+"",info.getId()+"");
                }
            }else{
                return BaseController.ERROR_CODE_PARAMS;
            }
        }else if(list.size()>0){//有数据不能报名
            return BaseController.ERROR_CODE_ALERADY;
        }
        return BaseController.ERROR_CODE_SUCCESS;
    }

    @Override
    public MatchInfo queryApplyMatchInfo(String matchId){
        return mathMapper.queryApplyMatchInfo(matchId);
    }

    @Override
    public List<AccountInfo> queryAcountByMatchAndUser(String userId,String matchId){
        return accountMapper.queryAccountByUserId(userId,matchId);
    }


    @Override
    public boolean checkMatch(String userId,String accountId) {
        mineMapper.checkMyMatch(userId,accountId);
        return true;
    }


    public Map<String,Object> queryMatchRanking(HttpServletRequest request){
        String matchId=request.getParameter("matchId");
        if(matchId==null||"".equals(matchId)){
            List<Map<String,Object>> listrank = mineMapper.queryMatchRanking(matchId,0,20);
            return BaseController.getSuccessMap(listrank);
        }
        return BaseController.getErrorMap(BaseController.ERROR_CODE_OTHER);
    }



    //校验验证码
    private boolean verifyAuthCode(String phoneNum,String authCode){
        AuthCodeInfo info=smsMapper.queryAuthCode(phoneNum,authCode);
        if(info!=null&&authCode.equals(info.getAuth_code())){//不为空，且相等
            //校验成功
            //删除
            smsMapper.deleteAuthCode(""+info.getId());
            return true;
        }
        return false;
    }

}
