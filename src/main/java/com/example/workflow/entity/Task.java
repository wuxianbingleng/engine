package com.example.workflow.entity;

import java.util.Date;

public class Task {

    private String id;
    private String processDefinitionId;
    private String userId;
    private String name;
    private String processInstanceId;
    private String taskDefinitionKey;
    private String createTime;


    public String getUserId() {
        return userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getId() {
        return id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getName() {
        return name;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

}
