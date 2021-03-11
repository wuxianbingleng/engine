package com.example.workflow.controller;


import com.example.workflow.entity.ProcessInstanceHistory;
import com.example.workflow.service.ProcessService;
import com.example.workflow.entity.ProcessDefinition;
import com.example.workflow.exception.DefinitionException;
import com.example.workflow.exception.Result;
import com.example.workflow.validation.StartVariables;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;


@RestController
@Api("process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @ApiOperation(value = "流程部署")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "文件固定key值",required = true, paramType = "form-data"),
    })
    @RequestMapping(path = "/process/deploy", method = RequestMethod.POST)
    public Result<ProcessDefinition> deploy(@RequestParam("file") MultipartFile file) throws Exception{
        if (file.isEmpty()) {
            throw new DefinitionException(400, "文件为空");
        }
        ProcessDefinition processDefinition = processService.publish(file);

        Result<ProcessDefinition> result = new Result<>();
        result.setData(processDefinition);
        return result;
    }

    @ApiOperation(value = "流程启动")
    @RequestMapping(path = "/process/start", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey",value = "流程定义key",required = true, paramType = "String"),
            @ApiImplicitParam(name = "variables",value = "启动变量", required = true, paramType = "Map"),
            @ApiImplicitParam(name = "mapping",value = "外部任务变量映射", required = false, paramType = "List<Map>"),
    })
    public Result<Map> runProcess(@Valid @RequestBody() StartVariables startVariables) {

        String processDefinitionKey = startVariables.getProcessDefinitionKey();
        Map variables = startVariables.getVariables();
        List<Map<String,String>> mappingList = startVariables.getMapping();

        // 遍历外部任务映射
        for (Map<String,String> mapping : mappingList){
            for (String k : mapping.keySet()){
                String value = mapping.get(k);
                Object v = variables.get(value);
                if(v == null){
                    throw new DefinitionException(400, "表单不存在外部任务映射键");
                }
                variables.put(k, v);
            }
        }

        List<String> necessaryVarList = processService.getVariablesByKey(processDefinitionKey);

        List<String> provideVarList = new ArrayList<>(variables.keySet());


        for (String necessaryVar : necessaryVarList){
            if(!provideVarList.contains(necessaryVar)){
                throw new DefinitionException(400, "缺少必须参数:" + necessaryVar);
            }
        }
        System.out.println("variables =" + variables.toString());

        Map map = new HashMap();
        String instanceId = processService.startProcess(processDefinitionKey, variables);
        Result<Map> result = new Result<>();
        map.put("processDefinitionKey", processDefinitionKey);
        map.put("processInstanceId", instanceId);
        map.put("variables", variables);

        result.setData(map);
        return result;
    }


    @ApiOperation(value = "流程实例日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId",value = "实例Id",required = true, paramType = "String"),
    })
    @RequestMapping(path = "/process/log", method = RequestMethod.GET)
    public Result<ProcessInstanceHistory> getProcessLog(@RequestParam("processInstanceId") String processInstanceId){
        if(processInstanceId == null){
            throw new DefinitionException(400, "processInstanceId 参数为空");
        }

        ProcessInstanceHistory log = processService.getProcessLog(processInstanceId);
        Result<ProcessInstanceHistory> result = new Result<>();
        result.setData(log);
        return result;
    }

}
