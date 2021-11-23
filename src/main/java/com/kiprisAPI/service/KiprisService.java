package com.kiprisAPI.service;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KiprisService {

//    public class NotValueException extends Exception{
//        public NotValueException() {    }
//        public NotValueException(String message){
//            super(message);
//        }
//    }

    public static Map<String, Document> documents = new HashMap<>();

    public static final String URL = "http://kpat.kipris.or.kr/kpat/resulta.do";

    private String searchWord;
    private String stateName;
    private String dbJoin;

    private String[] auth_name = {"특허", "실용"}; //권리구분 이름
    private String[] admin_name = {"공개", "취하", "소멸", "포기", "무효", "거절", "등록"}; //행정상태 이름
    private String[] classify_name = {"등록", "공개", "출원"}; // 분류통계 이름
    private String[] year = {
            "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
            "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
            "2020", "2021",
    };
    private String[] auth_sep = {"P", "U"}; //권리구분 authority separate
    private String[] admin_stat = {"A", "C", "F", "G", "I", "J", "R"}; //행정상태 administration state
    private static String[] classify_stats = {"GDP", "ODP", "ADP"}; // 분류통계 classify stats


    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getStateName() {
        return stateName;
    }
    public void setStateName(String authName, String adminName){
        this.stateName = authName + "_" + adminName;
        this.setDbJoin(this.stateName);
    }
    public void setStateName(String authName, String adminName, String classifyName, String year){
        this.stateName = authName + "_" + adminName + "_" + classifyName + "_" + year;
        this.setDbJoin(this.stateName);
    }

    public String getDbJoin() {
        return dbJoin;
    }
    public void setDbJoin(String statName) {
        String authName; String adminName;
        String classifyName = null;

        String authSep = null;  String adminStat = null;
        String classifyStats = null; String year = null;

        String stateSplit[] = stateName.split("_");
        authName = stateSplit[0];
        adminName = stateSplit[1];
        if(stateSplit.length == 4) {
            classifyName = stateSplit[2];
            year = stateSplit[3];
        }

        // 조인에 들어갈 알파벳 설정
        for(int i=0; i<auth_name.length; i++){
            if(authName.equals(auth_name[i]))
                authSep = auth_sep[i];
        }
        for(int j=0; j<admin_name.length; j++){
            if(adminName.equals(admin_name[j]))
                adminStat = admin_stat[j];
        }
        if(stateSplit.length == 4) { // 분류통계까지
            for(int j=0; j<classify_name.length; j++){
                if(classifyName.equals(classify_name[j]))
                    classifyStats = classify_stats[j];
            }
        }

        if(authSep == null && adminStat == null){ // 둘다 전체
            this.dbJoin = "";
        } else if(authSep == null && adminStat != null){ // 권리구분전체 + 행정상태
            this.dbJoin = "({" + adminStat + "<IN>LST})";
        } else if(authSep != null && adminStat == null){ // 권리구분 + 행정상태전체
            this.dbJoin = "((" + authSep + "<IN>CLS))";
        } else{                                          // 권리구분 + 행정상태
            this.dbJoin = "((" + authSep + "<IN>CLS))*({" + adminStat + "<IN>LST})";
        }
        if(stateSplit.length == 4) {
            this.dbJoin = dbJoin + "-" + classifyStats + ":" + year;
        }
    }

    public List<MakeKipris> makeAllDocuments(){
        List<MakeKipris> threadList = new ArrayList<>();

        for(int i=0; i<auth_name.length; i++){
            for(int j=0; j<admin_name.length; j++){
                setStateName(auth_name[i], admin_name[j]);
                setDbJoin(stateName);
                threadList.add(new MakeKipris(this.searchWord, getStateName(), getDbJoin()));
            }
        }
        return threadList;
    }
    public List<MakeKipris> makeClassifyYearDocuments(String authName, String adminName, String classifyName){
        List<MakeKipris> threadList = new ArrayList<>();

        for(int i=0; i<year.length; i++){
            setStateName(authName, adminName, classifyName, this.year[i]);
            setDbJoin(this.stateName);
            threadList.add(new MakeKipris(this.searchWord, getStateName(), getDbJoin()));
        }
        return threadList;
    }
    public void documentThreadRun(List<MakeKipris> list){
        int lsize = list.size();

        for(int i=0; i<lsize; i++){
            list.get(i).start();

            if(i!=0 && i%15==0){ // 15개 이상 request 요청시 exception 발생되기 때문
                try {
                    Thread.sleep(430);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        for(int j=0; j<lsize; j++){
            if(list.get(j).isAlive()){
                try {
                    list.get(j).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public String findTotal(String stateName){

        String total = documents.get(stateName).select("form[name=listForm] p.articles > span.total").text();
        if(total.equals("")){
            total = "0";
        }
        return total;
    }

}
