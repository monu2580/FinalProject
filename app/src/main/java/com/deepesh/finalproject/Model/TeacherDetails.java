package com.deepesh.finalproject.Model;

import java.io.Serializable;

/**
 * Created by Deepesh on 11-11-2017.
 */

public class TeacherDetails implements Serializable{

    String name;
    String uname;
    String pass;
    String email;
    String city;
    String addr;
    String mob;
    String subj;
    Double lang;
    Double lati;
    String token;
    String uid;
    String imageUrl;


    public TeacherDetails() {
    }

    public TeacherDetails(String name, String uname, String pass, String email, String city, String addr, String mob, String subj, Double lang, Double lati, String token, String uid, String imageUrl) {
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
        this.token = token;
        this.uid = uid;
        this.imageUrl = imageUrl;
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

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public Double getLati() {
        return lati;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "TeacherDetails{" +
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
                ", token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
