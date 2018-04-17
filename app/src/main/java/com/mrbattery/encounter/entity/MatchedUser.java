package com.mrbattery.encounter.entity;

public class MatchedUser extends User {

    private double matchingDegree;

    public double getMatchingDegree() {
        return matchingDegree;
    }

    public void setMatchingDegree(double matchingDegree) {
        this.matchingDegree = matchingDegree;
    }

    @Override
    public String toString() {
        return "MatchedUser{" +
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
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", matchingDegree=" + matchingDegree +
                '}';
    }
}
