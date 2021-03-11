package com.example.workflow.entity;

public class ProcessInstanceHistory {

    private String processInstanceId;
    private String processDefinitionId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartActivityId() {
        return startActivityId;
    }

    public String getEndActivityId() {
        return endActivityId;
    }

    public String getState() {
        return state;
    }

    private String startTime;
    private String endTime;
    private String startActivityId;
    private String endActivityId;
    private String state;


}
