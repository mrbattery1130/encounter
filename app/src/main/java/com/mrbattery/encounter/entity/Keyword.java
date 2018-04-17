package com.mrbattery.encounter.entity;

public class Keyword {
    private int keywordID;
    private String keywordName;
    private int parentTopicID;

    public int getKeywordID() {
        return keywordID;
    }

    public void setKeywordID(int keywordID) {
        this.keywordID = keywordID;
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName;
    }

    public int getParentTopicID() {
        return parentTopicID;
    }

    public void setParentTopicID(int parentTopicID) {
        this.parentTopicID = parentTopicID;
    }

    @Override
    public String toString() {
        return "KeywordEntity{" +
                "keywordID=" + keywordID +
                ", keywordName='" + keywordName + '\'' +
                ", parentTopicID=" + parentTopicID +
                '}';
    }

}
