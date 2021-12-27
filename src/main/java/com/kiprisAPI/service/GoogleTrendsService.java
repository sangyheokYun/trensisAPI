package com.kiprisAPI.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleTrendsService {

    private WebDriver driver;

    private final static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private final static String WEB_DRIVER_PATH = "/usr/lib/chromium-browser/chromedriver";
    //private final static String WEB_DRIVER_PATH = "E:\\library\\chromedriver.exe";

    private String TRENDS_URL;

    public void setSearchWord(String searchWord, String date){
        setURL(searchWord, date);
    }

    public void setSearchWord(String searchWord, String compareWord, String date){
        String word = searchWord + "," + compareWord;
        setURL(word, date);
    }

    public void setURL(String searchWord, String date){
        String time = null;
        switch(date){
            case "지난1시간":
                time = "date=now%201-H";
                break;
            case "지난4시간":
                time = "date=now%204-H";
                break;
            case "지난1일":
                time = "date=now%201-d";
                break;
            case "지난7일":
                time = "date=now%207-d";
                break;
            case "지난30일":
                time = "date=today%201-m";
                break;
            case "지난90일":
                time = "date=today%203-m";
                break;
            case "지난12개월":
                time = "";
                break;
            case "지난5년":
                time = "date=today%205-y";
                break;
            case "2004년부터현재":
                time = "date=all";
                break;
        }

        TRENDS_URL = "https://trends.google.co.kr/trends/explore?" + time + "&geo=KR&q=" + searchWord;
    }

    public String delSpace(String strNum){
        strNum = strNum.replace(" ", "");
        return strNum;
    }

    public void browserSetRun(){
        try{
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        } catch (Exception e){
            e.printStackTrace();
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
    }

    public Map<String, List> getTrendsValue(){

        /* Selenium html 가져오기 */
        String html = null;

        try{
            driver.get(TRENDS_URL);
            driver.get(driver.getCurrentUrl());
            Thread.sleep(2000);
            html = driver.getPageSource();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            driver.close();
        }

        /* Jsoup 태그 선택 후 값 추출 */

        String date = null;
        String value = null;
        List<Integer> valueList = null;
        Map<String, List> trendsValue = new HashMap<>();

        Document trendsDocument = Jsoup.parse(html);

        Elements valueElement = trendsDocument.select("div.line-chart div[aria-label=A tabular representation of the data in the chart.]")
                .select("tbody > tr");

        for(int i=0; i<valueElement.size(); i++){
            date = (i+1) + "_" + this.delSpace(valueElement.get(i).select("td").get(0).text());
            valueList = new ArrayList<>();

            for(int j=1; j<valueElement.get(i).select("td").size(); j++){
                value = valueElement.get(i).select("td").get(j).text();
                valueList.add(Integer.valueOf(value));


            }
            System.out.println(date);
            trendsValue.put(date, valueList);
        }

        return trendsValue;
    }

}
