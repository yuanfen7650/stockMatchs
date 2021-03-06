package com.hld.stockserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hld.stockserver.mapper.StockMapper;
import com.hld.stockserver.service.StockManagerService;
import com.hld.stockserver.uitls.ChangeToPinYin;
import com.hld.stockserver.uitls.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockManagerServiceImpl implements StockManagerService {

    @Autowired
    StockMapper stockMapper;
    private ChangeToPinYin changeToPinYin;

    public StockManagerServiceImpl(){
        changeToPinYin=new ChangeToPinYin();
    }

    @Override
    public void doUpdateStock(){
        for(int i=0;i<100;i++){
            String json=HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[\"hq\",\"cyb\",\"\",0,"+i+",500]]");
            boolean flag=updateOrInsertStocks(json);
            if(!flag){
                break;
            }
        }

        for(int i=0;i<100;i++){
            String json=HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[\"hq\",\"zxqy\",\"\",0,"+i+",500]]");
            boolean flag=updateOrInsertStocks(json);
            if(!flag){
                break;
            }
        }

        for(int i=1;i<100;i++){
            String json=HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php?__s=[[\"hq\",\"hs_a\",\"\",0,"+i+",500]]");
            boolean flag=updateOrInsertStocks(json);
            if(!flag){
                break;
            }

        }
        System.out.println("更新完成");
    }


    //更新股票信息
    private boolean updateOrInsertStocks(String json){
        JSONArray ja= JSONObject.parseArray(json);
        if(ja.size()<=0){
            return false;
        }
        JSONArray items = ja.getJSONObject(0).getJSONArray("items");
        if(items.size()<=0){
            return false;
        }
        //进行更新
        for(int j=0;j<items.size();j++){
            JSONArray itemJo=items.getJSONArray(j);
            updateOrInsertStockInfo(itemJo.getString(0),itemJo.getString(1),itemJo.getString(2),itemJo.getString(3),
                    itemJo.getString(9),itemJo.getString(8),itemJo.getString(10),itemJo.getString(11),itemJo.getString(21),
                    itemJo.getString(12),"",itemJo.getString(19));
        }
        return true;
    }
    //更新单个股票信息
    private void updateOrInsertStockInfo(String codeStr,String code,String name,String nowPrice,String openPrice,String yesClosePrice,String maxPrice,String minPrice,
                                         String tunRate,String vol,String peRate,String totalValue){

        int count=stockMapper.queryStockCountByCode(code);

        String tem1=changeToPinYin.getStringPinYin(name+"");
        String tem2=changeToPinYin.getPinYinHeadChar(name+"",1);
        String searchStr=code+" "+name+" "+tem2+" "+tem1;

        if(count>0){//已经存在,执行更新就行了
            System.out.println("执行更新:"+name+"("+code+")");
            stockMapper.updateOneStock(name,code,codeStr,nowPrice,yesClosePrice,"0",openPrice,maxPrice,minPrice,totalValue,tunRate,vol,peRate,searchStr);
        }else{//不存在，需要新增
            System.out.println("新增:"+name+"("+code+")");
            stockMapper.insertOneStock(name,code,codeStr,nowPrice,yesClosePrice,"0",openPrice,maxPrice,minPrice,totalValue,tunRate,vol,peRate,searchStr);
        }
    }


    public static void main(String[] args){
//        String json1=HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[\"hq\",\"cyb\",\"\",0,1,10000]]");
//        String json2=HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php/?__s=[[\"hq\",\"zxqy\",\"\",0,1,10000]]");
//        String json3= HttpUtil.sendPost("http://money.finance.sina.com.cn/d/api/openapi_proxy.php?__s=[[\"hq\",\"hs_a\",\"\",0,1,500]]");
//
//
//        JSONArray ja= JSON.parseArray(""+json3);
//
//        System.out.println(""+ja.getJSONObject(0).getJSONArray("items").size());




















    }
}
