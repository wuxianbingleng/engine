package com.example.workflow.service;

import com.alibaba.fastjson.JSONArray;
import com.example.workflow.camunda.Tool;
import com.example.workflow.entity.*;
import com.example.workflow.exception.DefinitionException;
import com.example.workflow.mapper.*;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class ProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

    @Autowired
    private ProcessVariableMapper processVariableMapper;

    @Autowired
    private ProcessTopicMapper processTopicMapper;

    @Autowired
    private TaskVariableMapper taskVariableMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private ProcessInstanceHistoryMapper processInstanceHistoryMapper;


    public ProcessDefinition publish(MultipartFile file) throws Exception{
        String resourceName = file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();
        // todo: resourceName modify to unique value;
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath()+ "/" + resourceName;

        System.out.println("path =" + path);
        File tempFile = new File(path);

        try {
            file.transferTo(tempFile);

        } catch (IOException e){
            e.printStackTrace();
            throw new DefinitionException(500, "文件创建错误");
        }
        Map<String, Object> map = null;
        try {
            map = Tool.parseBpmnParams(tempFile);
        } catch (Exception e){
            e.printStackTrace();
            throw new DefinitionException(500, "xml 解析出错");
        }
        assert resourceName != null;
        String deployName = resourceName.substring(0, resourceName.lastIndexOf("."));

        Deployment deployment = repositoryService.createDeployment()
                .name(deployName)
                .enableDuplicateFiltering(true)
                .addInputStream(resourceName, inputStream)
                .deploy();
        ProcessDefinition processDefinition = processDefinitionMapper.getOneByDeploymentId(deployment.getId());

        processVariableMapper.findOrCreate(processDefinition.getKey(), (String) map.get("variables"));

        Map<String, Integer> topics = (Map<String, Integer>) map.get("topics");

        for(String k: topics.keySet()){
            processTopicMapper.findOrCreate(processDefinition.getKey(), k);
        }

        Map<String,String> taskVariableMap = (Map<String, String>) map.get("taskVariableMap");

        for(String k: taskVariableMap.keySet()){
            taskVariableMapper.findOrCreate(k, taskVariableMap.get(k));
        }
        return processDefinition;

    }


    public List<String> getVariablesByKey(String processDefinitionKey){
        // 表单参数list
        List<String> list = new ArrayList<>();
        ProcessVariable processVariable = processVariableMapper.getOne(processDefinitionKey);
        String variables = processVariable.getVariables();
        String[] variableList = variables.split(",");
        list.addAll(Arrays.asList(variableList));
        // 外部任务参数list
        List<ProcessTopic> processTopics = processTopicMapper.getAll(processDefinitionKey);

        for(ProcessTopic processTopic : processTopics){
            Topic<String> topic = topicMapper.getOne(processTopic.getTopic());
            if(topic == null){
                return list;
            }
            String input = topic.getInput();
            List<HashMap> inputs = JSONArray.parseArray(input, HashMap.class);
            for(HashMap item: inputs){
                String paramName = (String) item.get("name");
                list.add(paramName);
            }
        }
        System.out.println(list.toString());
        return  list;
    }

    public String startProcess(String processDefinitionKey, Map<String,Object> variables){

        ProcessInstance instance = runtimeService.
                startProcessInstanceByKey(processDefinitionKey,variables);

        if (instance == null) {
            throw new DefinitionException(500, "启动错误");
        }

        return instance.getId();
    }


    public ProcessInstanceHistory getProcessLog(String processInstanceId){
        ProcessInstanceHistory log = processInstanceHistoryMapper.getOne(processInstanceId);
        return log;
    }
}
