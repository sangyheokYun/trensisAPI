package com.kiprisAPI.controller;

import com.kiprisAPI.service.KiprisService;
import com.kiprisAPI.service.MakeKipris;
import com.kiprisAPI.service.NaverService;
import com.kiprisAPI.service.TodayKipris;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class KiprisController {

    private KiprisService kiprisService;
    private TodayKipris todayKipris;
    private NaverService naverService;

    @Autowired
    public KiprisController(KiprisService kiprisService, TodayKipris todayKipris, NaverService naverService) {
        this.kiprisService = kiprisService;
        this.todayKipris = todayKipris;
        this.naverService = naverService;
    }

    @GetMapping(value = "/getDocumentsTotal")
    public Map<String, String> getDocumentsTotal(@RequestParam String searchWord){
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> allDocuments = kiprisService.makeAllDocuments();
        kiprisService.documentThreadRun(allDocuments);

        Map<String, String> documentsTotal = new HashMap<>();
        for(int i=0; i<allDocuments.size(); i++){
            documentsTotal.put(allDocuments.get(i).getStateName(), kiprisService.findTotal(allDocuments.get(i).getStateName()));
        }

        return documentsTotal;
    }

    @GetMapping(value = "/getDocumentsYearTotal")
    public Map<String, String> getDocumentsYearTotal(@RequestParam String searchWord, @RequestParam String authority,
                                                     @RequestParam String administration, @RequestParam String classify) {
        kiprisService.setSearchWord(searchWord);
        List<MakeKipris> yearDocuments = kiprisService.makeClassifyYearDocuments(authority, administration, classify);
        kiprisService.documentThreadRun(yearDocuments);

        Map<String, String> yearTotal = new HashMap<>();
        for(int i=0; i<yearDocuments.size(); i++){
            yearTotal.put(yearDocuments.get(i).getStateName(), kiprisService.findTotal(yearDocuments.get(i).getStateName()));
        }

        return yearTotal;
    }

    @GetMapping(value = "/getTodayWord")
    public Map<String, String> getTodayWord(){
        return todayKipris.getTodayWord();
    }

    @GetMapping(value = "/getTodayPatent")
    public Map<String, String> getTodayPatent(){
        return todayKipris.getTodayPatent();
    }

    @GetMapping(value = "/getRelationWord")
    public List<String> getRelationWord(@RequestParam String searchWord){
        naverService.setSearchWord(searchWord);
        return naverService.getRelationWord();
    }

}
