package com.example.workflow.entity;

public class Topic<T> {

    private String topic;
    private String name;
    private T input;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getInput() {
        return input;
    }

    public void setInput(T input) {
        this.input = input;
    }


}
