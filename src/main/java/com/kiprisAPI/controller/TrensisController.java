package com.kiprisAPI.controller;

import com.kiprisAPI.model.dto.Patent;
import com.kiprisAPI.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST})
public class TrensisController {

    private KiprisService kiprisService;
    private TodayKiprisService todayKiprisService;
    private NaverService naverService;
    private GoogleTrendsService googleTrendsService;

    @Autowired
    public TrensisController(KiprisService kiprisService, TodayKiprisService todayKipris,
                             NaverService naverService, GoogleTrendsService googleTrendsService) {
        this.kiprisService = kiprisService;
        this.todayKiprisService = todayKipris;
        this.naverService = naverService;
        this.googleTrendsService = googleTrendsService;
        System.out.println("API : Controller 생성");
    }

    @RequestMapping(value = "/getAuthorityTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Integer> getAuthorityTotal(@RequestParam String searchWord, @RequestParam String authority){
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> authDocuments = kiprisService.makeAuthorityDocuments(authority);
        kiprisService.documentThreadRun(authDocuments);

        Map<String, Integer> authTotal = new HashMap<>();
        for(int i=0; i<authDocuments.size(); i++){
            authTotal.put(authDocuments.get(i).getStateName(), kiprisService.findTotal(authDocuments.get(i).getStateName()));
        }
        System.out.println("API : /getAuthorityTotal - 각 권리(특허,실용) 갯수");
        return authTotal;
    }

    @RequestMapping(value = "/getDocumentsTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Integer> getDocumentsTotal(@RequestParam String searchWord){
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> allDocuments = kiprisService.makeAllDocuments();
        kiprisService.documentThreadRun(allDocuments);

        Map<String, Integer> documentsTotal = new HashMap<>();
        for(int i=0; i<allDocuments.size(); i++){
            documentsTotal.put(allDocuments.get(i).getStateName(), kiprisService.findTotal(allDocuments.get(i).getStateName()));
        }
        System.out.println("API : /getDocumentsTotal - 각 권리(특허,실용) 갯수");
        return documentsTotal;
    }

    @RequestMapping(value = "/getDocumentsYearTotal", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Integer> getDocumentsYearTotal(@RequestParam String searchWord, @RequestParam String authority,
                                                      @RequestParam String administration, @RequestParam String classify) {
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> yearDocuments = kiprisService.makeClassifyYearDocuments(authority, administration, classify);
        kiprisService.documentThreadRun(yearDocuments);

        Map<String, Integer> yearTotal = new HashMap<>();
        for(int i=0; i<yearDocuments.size(); i++){
            yearTotal.put(yearDocuments.get(i).getStateName(), kiprisService.findTotal(yearDocuments.get(i).getStateName()));
        }
        System.out.println("API : /getDocumentsYearTotal - 각 권리(특허,실용)를 진행상태로 분류한 년도별 갯수");
        return yearTotal;
    }

    @RequestMapping(value = "/getTodayWord", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, String> getTodayWord(){
        System.out.println("API : /getTodayword - Kipris 실시간 특허 검색어");
        return todayKiprisService.getTodayWord();
    }

    @RequestMapping(value = "/getTodayPatent", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Patent> getTodayPatent(){
        System.out.println("API : /getTodayPatent - Kipris 오늘의 관심 특허");
        return todayKiprisService.getTodayPatent();
    }

    @RequestMapping(value = "/getRelationWord", method = {RequestMethod.GET, RequestMethod.POST})
    public List<String> getRelationWord(@RequestParam String searchWord){
        naverService.setSearchWord(searchWord);
        System.out.println("API : /getRelationword - Naver 연관검색어");
        return naverService.getRelationWord();
    }

    @RequestMapping(value = "/getTrendsValue", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, List> getTrendsValue(@RequestParam String searchWord, @RequestParam String date){
        googleTrendsService.browserSetRun();
        googleTrendsService.setSearchWord(searchWord, date);
        System.out.println("API : /getTrendsValue - GoogleTrends 각 시간별 검색량");
        return googleTrendsService.getTrendsValue();
    }

    @RequestMapping(value = "/getTrendsCompareValue", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, List> getTrendsCompareValue(@RequestParam String searchWord, @RequestParam String compareWord, @RequestParam String date){
        googleTrendsService.browserSetRun();
        googleTrendsService.setSearchWord(searchWord, compareWord, date);
        System.out.println("API : /getTrendsCompareValue - GoogleTrends 비교 단어 각 시간별 검색량");
        return googleTrendsService.getTrendsValue();
    }

}
