package com.deepesh.finalproject.Model;

/**
 * Created by Deepesh on 09-11-2017.
 */

public class UserData {
    String name;
    String uname;
    String pass;
    String email;
    String city;
    String addr;
    String mob;
    String subj;
    double lang;
    double lati;

    public UserData() {
    }

    public UserData(String name, String uname, String pass, String email, String city, String addr, String mob, String subj, double lang, double lati) {
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.email = email;
        this.city = city;
        this.addr = addr;
        this.mob = mob;
        this.subj = subj;
        this.lang = lang;
        this.lati = lati;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", uname='" + uname + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", addr='" + addr + '\'' +
                ", mob='" + mob + '\'' +
                ", subj='" + subj + '\'' +
                ", lang=" + lang +
                ", lati=" + lati +
                '}';
    }
}

