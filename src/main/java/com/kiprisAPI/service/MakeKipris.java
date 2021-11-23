package com.kiprisAPI.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class MakeKipris extends Thread{

    private String searchWord = "";
    private String stateName;
    private String dbJoin;
    private String piSearchYN = "N";
    private String classifyStats = "";
    private String year = "";
    private Document document;

    public MakeKipris(String searchWord, String stateName, String dbJoin) {
        this.searchWord = searchWord;
        this.stateName = stateName;
        this.splitClassifyStats(dbJoin);
        //System.out.println(this.searchWord + "/" + this.stateName + "/" + this.dbJoin);
    }

    public String getStateName() {
        return stateName;
    }

    public void splitClassifyStats(String dbJoin){
        String[] split = null;
        if(dbJoin.contains("-")){
            this.piSearchYN = "Y";
            split = dbJoin.split("-");
            this.dbJoin = split[0];

            //System.out.println(split[0]);

            split = split[1].split(":");
            this.classifyStats = split[0];

            //System.out.println(split[0]);

            this.year = split[1];

            //System.out.println(split[1]);
        } else{
            this.dbJoin = dbJoin;
        }

    }

    @Override
    public String toString() {
        return "MakeKipris{" +
                "stateName='" + stateName + '\'' +
                ", dbJoin='" + dbJoin + '\'' +
                ", classifyStats='" + classifyStats + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    @Override
    public void run() {
        try {
            // kipris 검색 후 화면 html
            document = Jsoup.connect(KiprisService.URL)
                    .data("queryText", searchWord)
                    .data("searchInResultCk", "undefined")
                    .data("next", "MainList")
                    .data("config", "G1111111111111111111111S111111111000000000")
                    .data("sortField", "RANK")
                    .data("sortState", "DESC")
                    .data("configChange", "Y")
                    .data("expression", searchWord)
                    .data("historyQuery", searchWord)
                    .data("numPerPage", "30")
                    .data("numPageLinks", "10")
                    .data("currentPage", "1")
                    .data("beforeExpression", searchWord)
                    .data("prefixExpression", dbJoin) // sideBar DB Join
                    .data("searchInTrans", "N")
                    .data("logFlag", "Y")
                    .data("searchSaveCnt", "0")
                    .data("piField", classifyStats)    // 등록, 출원, 공개
                    .data("piValue",year) // 년도
                    .data("piSearchYN", piSearchYN) // 서치 할거면 Y
                    .data("SEL_PAT", "KPAT")
                    .data("strstat", "TOP|KW")
                    .data("searchInTransCk", "undefined")
                    .post();
            KiprisService.documents.put(stateName, document);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
