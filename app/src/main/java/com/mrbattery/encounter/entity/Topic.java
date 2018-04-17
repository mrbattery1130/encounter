package com.mrbattery.encounter.entity;

public class Topic {
    private int topicID;
    private String topicName;

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return "TopicEntity{" +
                "topicID=" + topicID +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
