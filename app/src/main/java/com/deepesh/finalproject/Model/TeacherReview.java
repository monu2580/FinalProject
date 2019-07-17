package com.deepesh.finalproject.Model;

import java.io.Serializable;

/**
 * Created by Deepesh on 03-12-2017.
 */

public class TeacherReview implements Serializable{

    String name;
    String rate;
    String review;

    public TeacherReview() {
    }

    public TeacherReview(String name, String rate, String review) {
        this.name = name;
        this.rate = rate;
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "TeacherReview{" +
                "name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}

