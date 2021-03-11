package com.example.workflow.entity;

public class ProcessDefinition {
    private String id;
    private String key;
    private String name;
    private String deploymentId;

    public String getId(){
        return id;
    }

    public String getKey(){
        return key;
    }
    public String getName(){
        return  name;
    }
    public String getDeploymentId(){
        return deploymentId;
    }
}
