package com.hld.stockmanagerbusiness.controller;

import com.hld.stockmanagerbusiness.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class MarketController {
    @Autowired
    MarketService marketService;

    //查询股指
    @RequestMapping(value="/loadMarketIndex",method = RequestMethod.POST)
    @ResponseBody
    public String loadMarketIndex(HttpServletRequest request){
        return marketService.loadMarketIndex(request);
    }

    @RequestMapping(value="/queryStockRankDown",method = RequestMethod.POST)
    @ResponseBody
    public String queryStockRankDown(HttpServletRequest request){
        return marketService.queryStockRankDown(request);
    }

    @RequestMapping(value="/queryStockRankUp",method = RequestMethod.POST)
    @ResponseBody
    public String queryStockRankUp(HttpServletRequest request){
        return marketService.queryStockRankUp(request);
    }

    @RequestMapping(value="/queryStockNameByCodes",method = RequestMethod.POST)
    @ResponseBody
    public String queryStockNameByCodes(HttpServletRequest request){
        return marketService.queryStockNameByCodes(request);
    }

}
