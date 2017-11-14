package com.deepesh.finalproject.Model;

/**
 * Created by Deepesh on 09-11-2017.
 */

public class SearchData {
    String stCity;
    String stSubj;

    public SearchData() {
    }

    public SearchData(String stCity, String stSubj) {
        this.stCity = stCity;
        this.stSubj = stSubj;
    }

    public String getStCity() {
        return stCity;
    }

    public void setStCity(String stCity) {
        this.stCity = stCity;
    }

    public String getStSubj() {
        return stSubj;
    }

    public void setStSubj(String stSubj) {
        this.stSubj = stSubj;
    }

    @Override
    public String toString() {
        return "SearchData{" +
                "stCity='" + stCity + '\'' +
                ", stSubj='" + stSubj + '\'' +
                '}';
    }
}
