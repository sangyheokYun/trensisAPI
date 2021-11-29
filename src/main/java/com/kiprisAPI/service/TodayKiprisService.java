package com.kiprisAPI.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TodayKiprisService {

    public static final String WORD_URL = "http://www.kipris.or.kr/khome/remocon/viewStat/favorSearchWord.jsp";
    public static final String PATENT_URL = "http://www.kipris.or.kr/khome/today/today.jsp";


    public Map<String, String> getTodayPatent(){ //Map<String, String>
        Map<String, String> todayPatent = new HashMap<>();
        Document todayPatentDocument = null;
        Elements patentList = null;
        Element keyPatent = null;
        int patentRank;
        String patent;

        try{
            todayPatentDocument = Jsoup.connect(PATENT_URL).get();
        } catch(IOException e){
            e.printStackTrace();
        }

        patentList = todayPatentDocument.select("#inventListKPAT > li div.dummy a");


        for(int i=0; i<patentList.size(); i++){
            keyPatent = patentList.get(i);
            patentRank = Integer.parseInt(keyPatent.select("a").text().substring(0, 2));
            patent = keyPatent.select("a").text().substring(4);
            todayPatent.put(String.valueOf(patentRank), patent);
        }

        return todayPatent;
    }

    public Map<String, String> getTodayWord(){
        Map<String, String> todayWord = new HashMap<>();
        Document todayWordDocument = null;
        Elements wordList = null;
        Element keyWord = null;
        String wordRank;
        String word;

        try {
            todayWordDocument = Jsoup.connect(WORD_URL)
                    .data("Accept", "application/xml, text/xml, */*; q=0.01")
                    .data("Accept-Encoding","gzip, deflate")
                    .data("Accept-Language","ko-KR,ko;q=0.9")
                    .data("Connection","keep-alive")
                    .data("Content-Length","5")
                    .data("Content-Type","application/x-www-form-urlencoded")
                    .data("Cookie","JSESSIONID=LaY1N7NzPk2Uiz98ayK5SgnARaCiJDETUntGjTSUDBes7XggpwZ3K3lxkSsCw0KI.amV1c19kb21haW4va2hvbWUx; KP_CONFIG=G1111111111111111111111S111111111000000000; DG_CONFIG=G11111111111111111SX11100110011011111; TM_CONFIG=G11111111111111111SX11111110011111100; JM_CONFIG=G11111111111111SX01101111110010; AB_CONFIG=G11001111111111111111111111110S10000111000111100001000; AT_CONFIG=G0000000000000S111110110111; KA_CONFIG=G0000000000000S110111000; K2_CONFIG=G1111111111111111111111S111111111000000000; AD_CONFIG=G11111111111111S1111111111111000; _ga=GA1.3.542199139.1637586494; _gid=GA1.3.131004160.1637586494; KP_TOTAL_HISTRY=db; KPAT_SRCH_HISTORY=C3BC7B133E9FEFA2")
                    .data("DNT","1")
                    .data("Host","www.kipris.or.kr")
                    .data("Origin","http://www.kipris.or.kr")
                    .data("Referer","http://www.kipris.or.kr/khome/today/today.jsp")
                    .data("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
                    .data("X-Requested-With","XMLHttpRequest")
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordList = todayWordDocument.select("keyword");

        for(int i=0; i<wordList.size(); i++){
            keyWord = wordList.get(i);
            wordRank = keyWord.select("wordrank").text();
            word = keyWord.select("word").text();
            todayWord.put(wordRank, word);
        }

        return todayWord;
    }

}
