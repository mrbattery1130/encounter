package com.mrbattery.encounter.entity;

import java.io.Serializable;

public class User implements Serializable {
    protected int userID;
    protected String userName;
    protected String password;
    protected int gender;
    protected int constellation;
    protected String script;
    protected double eScore;
    protected double nScore;
    protected double pScore;
    protected double lScore;
    protected String avatar;
    protected String cover;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getConstellation() {
        return constellation;
    }

    public void setConstellation(int constellation) {
        this.constellation = constellation;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public double geteScore() {
        return eScore;
    }

    public void seteScore(double eScore) {
        this.eScore = eScore;
    }

    public double getnScore() {
        return nScore;
    }

    public void setnScore(double nScore) {
        this.nScore = nScore;
    }

    public double getpScore() {
        return pScore;
    }

    public void setpScore(double pScore) {
        this.pScore = pScore;
    }

    public double getlScore() {
        return lScore;
    }

    public void setlScore(double lScore) {
        this.lScore = lScore;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", constellation=" + constellation +
                ", script='" + script + '\'' +
                ", eScore=" + eScore +
                ", nScore=" + nScore +
                ", pScore=" + pScore +
                ", lScore=" + lScore +
                '}';
    }
}
