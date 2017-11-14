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
    String subj;
    String mob;

    public TeacherDetails() {
    }

    public TeacherDetails(String name, String uname, String pass, String email, String city, String addr, String subj, String mob) {
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.email = email;
        this.city = city;
        this.addr = addr;
        this.subj = subj;
        this.mob = mob;
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

    public String getSubj() {
        return subj;
    }

    public void setSubj(String subj) {
        this.subj = subj;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
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
                ", subj='" + subj + '\'' +
                ", mob='" + mob + '\'' +
                '}';
    }
}
