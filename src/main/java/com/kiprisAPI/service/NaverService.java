package com.kiprisAPI.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NaverService {

    private String NAVER_URL;

    public void setSearchWord(String searchWord) {
        this.NAVER_URL = "https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=" + searchWord + "&oquery=" + searchWord + "&tqi=hiznQlprvN8sslNvp70ssssss%2BN-470244";
    }

    public List<String> getRelationWord(){
        List<String> relationWordList = new ArrayList<>();
        Document naverDocument = null;
        Elements relationWordTags = null;
        Element relationWord = null;
        String word;

        try{
            naverDocument = Jsoup.connect(NAVER_URL).get();
        } catch(IOException e){
            e.printStackTrace();
        }

        relationWordTags = naverDocument.select("section.sc_new > div.api_subject_bx > div.related_srch > ul.lst_related_srch > li a div.tit");

        for(int i=0; i<relationWordTags.size(); i++){
            relationWord = relationWordTags.get(i);
            word = relationWord.select("div").text();
            relationWordList.add(word);
        }

        return relationWordList;
    }

}
