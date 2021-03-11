package com.example.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.example.workflow.entity.ProcessDefinition;
import com.example.workflow.entity.Task;
import com.example.workflow.entity.TaskVariable;
import com.example.workflow.mapper.ProcessDefinitionMapper;
import com.example.workflow.mapper.TaskMapper;
import com.example.workflow.mapper.TaskVariableMapper;
import com.example.workflow.validation.CompleteTask;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserTaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskVariableMapper taskVariableMapper;

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

    public List<Task> getTaskList(String processDefinitionKeys,String userId, Integer pageIndex,Integer pageSize){
        String[] processDefinitionKeyArr = processDefinitionKeys.split(",");
        List<Task> taskList = new ArrayList<>();

        for (int i = 0; i < processDefinitionKeyArr.length; i++) {
            ProcessDefinition processDefinition = processDefinitionMapper.getOne(processDefinitionKeyArr[i]);
            if(processDefinition != null){
                taskList.addAll(taskMapper.getAll(processDefinition.getId(), userId));
            }
        }

        Task[] tasks = new Task[taskList.size()];
        taskList.toArray(tasks);
        Arrays.sort(tasks,(t1, t2)->{
            if(t1.getCreateTime().compareTo(t2.getCreateTime()) >= 0){
                return 1;
            }else {
                return -1;
            }
        });

        Task[] result = Arrays.copyOfRange(tasks, (pageIndex-1) * pageSize, (pageIndex-1) * pageSize + pageSize);
        List<Task> collect = Arrays.stream(result).filter(v -> v != null).collect(Collectors.toList());

        return  collect;
    }

    public List<HashMap> getTaskVariables(String taskId){

        Task task = taskMapper.getOne(taskId);
        String taskDefinitionKey = task.getTaskDefinitionKey();
        TaskVariable taskVariable = taskVariableMapper.getOne(taskDefinitionKey);

        List<HashMap> list = JSONObject.parseArray(taskVariable.getVariables(), HashMap.class);

        Map taskServiceVariables = taskService.getVariables(taskId);
        for(HashMap variable : list){
            String variableName = (String)variable.get("name");
            variable.put("value", taskServiceVariables.get(variableName));
        }
        return list;

    }

    public Map<String,Object> completeTask(CompleteTask body){
        String taskId = body.getTaskId();
        List<HashMap> variables = body.getVariables();
        Map<String,Object> map = new HashMap<>();
        for(HashMap variable : variables){
            String key = (String) variable.get("name");
            Object value = variable.get("value");
            map.put(key, value);
        }
        // get all variables
        // VariableMap variableMap = taskService.completeWithVariablesInReturn(taskId,map,true);
        taskService.complete(taskId, map);
        return map;
    }
}
