package com.example.workflow.entity;

public class Variable {


    private String type;
    private String name;
    private String processInstanceId;
    private Long longValue;
    private Double doubleValue;
    private String textValue;

    public String getType() {
        return type;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public Long getLongValue() {
        return longValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public String getTextValue() {
        return textValue;
    }


    public String getName() {
        return name;
    }
}


