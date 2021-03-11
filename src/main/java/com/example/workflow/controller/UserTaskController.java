package com.example.workflow.controller;

import com.example.workflow.entity.Task;
import com.example.workflow.exception.DefinitionException;
import com.example.workflow.exception.Result;
import com.example.workflow.service.UserTaskService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.workflow.validation.CompleteTask;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api("task")
public class UserTaskController {

    @Autowired
    private UserTaskService userTaskService;


    @ApiOperation(value = "获取待审核任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户Id",required = true, paramType = "String"),
            @ApiImplicitParam(name = "pageIndex",value = "起始页",required = false, paramType = "String"),
            @ApiImplicitParam(name = "pageSize",value = "页容量",required = false, paramType = "String"),
            @ApiImplicitParam(name = "processDefinitionKeys",value = "任务定义keys", required = true, paramType = "String")
    })
    @RequestMapping(path = "/task", method = RequestMethod.GET)
    public Result<List<Task>> getTaskList(@RequestParam() HashMap<String,String> map){

        int pageIndex = 1;
        int pageSize = 10;

        if(map.get("pageIndex")!= null){
            pageIndex = Integer.parseInt(map.get("pageIndex"));
        }
        if(map.get("pageSize")!= null){
            pageIndex = Integer.parseInt(map.get("pageSize"));
        }
        if(map.get("processDefinitionKeys") == null || map.get("userId") == null){
            throw new DefinitionException(400, "参数错误");
        }

        List<Task> list = userTaskService.getTaskList(map.get("processDefinitionKeys"), map.get("userId"), pageIndex, pageSize);

        Result<List<Task>> result = new Result();

        result.setData(list);

        return result;
    }


    @ApiOperation(value = "获取待审核任务参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId",value = "任务Id",required = true, paramType = "String")
    })
    @RequestMapping(path = "/task/variables", method = RequestMethod.GET)
    public Result<List<HashMap>> getTaskVariables(@RequestParam("taskId") String taskId){
        if(taskId == null){
            throw new DefinitionException(400, "参数错误");
        }
        List<HashMap> taskVariables = userTaskService.getTaskVariables(taskId);
        Result<List<HashMap>> result = new Result<>();
        result.setData(taskVariables);
        return  result;
    }

    @ApiOperation(value = "审核任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId",value = "任务Id",required = true, paramType = "String"),
            @ApiImplicitParam(name = "variables",value = "参数列表", required = true, paramType = "List<Map>",
                    examples = @Example({
                            @ExampleProperty(value = "[{'value':888,'type':'long','name':'amount'}]", mediaType = "application/json")
                    })
            )
    })
    @RequestMapping(path = "/task/complete", method = RequestMethod.POST)
    public Result<Map<String,Object>> completeTask(@Valid @RequestBody() CompleteTask body){
        Map<String,Object> taskVariables = userTaskService.completeTask(body);
        Result<Map<String,Object>> result = new Result<>();
        result.setData(taskVariables);
        return  result;
    }
}
