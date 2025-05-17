package com.project.newsfeed.newsfeed_aggregator.model;

public class DeferredRequest {
    private String topic;

    public DeferredRequest() {}

    public DeferredRequest(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
