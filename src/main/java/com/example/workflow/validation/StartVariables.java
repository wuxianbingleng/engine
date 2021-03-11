package com.example.workflow.validation;


import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class StartVariables {
    @NotNull
    private String processDefinitionKey;
    private Map variables;
    private List<Map<String,String>> mapping;

    public Map getVariables() {
        return variables;
    }

    public void setVariables(Map variables) {
        this.variables = variables;
    }

    public List<Map<String,String>> getMapping() {
        return mapping;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }
}
