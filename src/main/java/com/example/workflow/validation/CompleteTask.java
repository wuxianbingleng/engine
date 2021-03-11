package com.example.workflow.validation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

public class CompleteTask {

    @NotNull
    private String taskId;

    @NotNull
    private List<HashMap> variables;

    public String getTaskId() {
        return taskId;
    }

    public List<HashMap> getVariables() {
        return variables;
    }
}
