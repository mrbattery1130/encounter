package com.mrbattery.encounter.entity;

public class Keyword {
    protected int keywordID;
    protected String keywordName;
    protected int parentTopicID;
    protected String parentTopicName;

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

    public String getParentTopicName() {
        return parentTopicName;
    }

    public void setParentTopicName(String parentTopicName) {
        this.parentTopicName = parentTopicName;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "keywordID=" + keywordID +
                ", keywordName='" + keywordName + '\'' +
                ", parentTopicID=" + parentTopicID +
                ", parentTopicName=" + parentTopicName +
                '}';
    }
}
