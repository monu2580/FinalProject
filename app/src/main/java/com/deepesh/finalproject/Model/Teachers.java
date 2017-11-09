package com.deepesh.finalproject.Model;

/**
 * Created by Deepesh on 24-10-2017.
 */

public class Teachers {


    int tid;
    String name;
    String uname;
    String pass;
    String email;
    String addr;
    String mob;
    String subj;

    public Teachers() {

    }

    public Teachers(int tid, String name, String uname, String pass, String email, String addr, String mob, String subj) {
        this.tid = tid;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.email = email;
        this.addr = addr;
        this.mob = mob;
        this.subj = subj;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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

    @Override
    public String toString() {
        return "Teachers{" +
                "tid=" + tid +
                ", name='" + name + '\'' +
                ", uname='" + uname + '\'' +
                ", pass='" + pass + '\'' +
                ", email='" + email + '\'' +
                ", addr='" + addr + '\'' +
                ", mob='" + mob + '\'' +
                ", subj='" + subj + '\'' +
                '}';
    }
}
