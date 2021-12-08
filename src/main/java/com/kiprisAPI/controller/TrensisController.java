package com.kiprisAPI.controller;

import com.kiprisAPI.service.KiprisService;
import com.kiprisAPI.service.MakeKipris;
import com.kiprisAPI.service.NaverService;
import com.kiprisAPI.service.TodayKiprisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class TrensisController {

    private KiprisService kiprisService;
    private TodayKiprisService todayKiprisService;
    private NaverService naverService;

    @Autowired
    public TrensisController(KiprisService kiprisService, TodayKiprisService todayKipris, NaverService naverService) {
        this.kiprisService = kiprisService;
        this.todayKiprisService = todayKipris;
        this.naverService = naverService;
    }

    @GetMapping(value = "/getAuthorityTotal")
    public Map<String, Integer> getAuthorityTotal(@RequestParam String searchWord, @RequestParam String authority){
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> authDocuments = kiprisService.makeAuthorityDocuments(authority);
        kiprisService.documentThreadRun(authDocuments);

        Map<String, Integer> authTotal = new HashMap<>();
        for(int i=0; i<authDocuments.size(); i++){
            authTotal.put(authDocuments.get(i).getStateName(), kiprisService.findTotal(authDocuments.get(i).getStateName()));
        }

        return authTotal;
    }

    @GetMapping(value = "/getDocumentsTotal")
    public Map<String, Integer> getDocumentsTotal(@RequestParam String searchWord){
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> allDocuments = kiprisService.makeAllDocuments();
        kiprisService.documentThreadRun(allDocuments);

        Map<String, Integer> documentsTotal = new HashMap<>();
        for(int i=0; i<allDocuments.size(); i++){
            documentsTotal.put(allDocuments.get(i).getStateName(), kiprisService.findTotal(allDocuments.get(i).getStateName()));
        }

        return documentsTotal;
    }

    @GetMapping(value = "/getDocumentsYearTotal")
    public Map<String, Integer> getDocumentsYearTotal(@RequestParam String searchWord, @RequestParam String authority,
                                                      @RequestParam String administration, @RequestParam String classify) {
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> yearDocuments = kiprisService.makeClassifyYearDocuments(authority, administration, classify);
        kiprisService.documentThreadRun(yearDocuments);

        Map<String, Integer> yearTotal = new HashMap<>();
        for(int i=0; i<yearDocuments.size(); i++){
            yearTotal.put(yearDocuments.get(i).getStateName(), kiprisService.findTotal(yearDocuments.get(i).getStateName()));
        }

        return yearTotal;
    }

    @GetMapping(value = "/getTodayWord")
    public Map<String, String> getTodayWord(){
        return todayKiprisService.getTodayWord();
    }

    @GetMapping(value = "/getTodayPatent")
    public Map<String, String> getTodayPatent(){
        return todayKiprisService.getTodayPatent();
    }

    @GetMapping(value = "/getRelationWord")
    public List<String> getRelationWord(@RequestParam String searchWord){
        naverService.setSearchWord(searchWord);
        return naverService.getRelationWord();
    }

}
