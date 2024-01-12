package com.example.ufcproject;

public class User {
    public String userid,email, name;
    public int bettingScore;
    public User(){

    }
    public User(String userid, String email, String name) {
        this.userid = userid;
        this.email = email;
        this.name = name;
    }

    public int getBettingScore() {
        return bettingScore;
    }

    public void setBettingScore(int bettingScore) {
        this.bettingScore = bettingScore;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}