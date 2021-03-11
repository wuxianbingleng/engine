package com.example.workflow.controller;


import com.example.workflow.entity.Topic;
import com.example.workflow.exception.Result;
import com.example.workflow.service.TopicService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@Api("topic")
public class TopicController {
    @Autowired
    TopicService topicService;

    @ApiOperation(value = "获取外部任务列表")
    @RequestMapping(path = "/topic", method = RequestMethod.GET)
    public Result<List<Topic<List<HashMap>>>> getTopic(){
        List<Topic<List<HashMap>>> topicList = topicService.getTopic();
        Result<List<Topic<List<HashMap>>>> result = new Result<>();
        result.setData(topicList);
        return  result;
    }


    @ApiOperation(value = "注册外部任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic",value = "外部任务key",required = true, paramType = "String"),
            @ApiImplicitParam(name = "name",value = "外部任务名称", required = true, paramType = "String"),
            @ApiImplicitParam(name = "input",value = "外部任务入参", required = true, paramType = "List<Map>"),
    })
    @RequestMapping(path = "/topic", method = RequestMethod.POST)
    public Result<Topic> addTopic(@Valid @RequestBody Topic<List<HashMap>> topic){
        topicService.addTopic(topic);
        Result<Topic> result = new Result();
        result.setData(topic);
        return result;
    }
}
