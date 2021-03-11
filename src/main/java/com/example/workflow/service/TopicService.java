package com.example.workflow.service;

import com.alibaba.fastjson.JSONArray;
import com.example.workflow.entity.Topic;
import com.example.workflow.exception.DefinitionException;
import com.example.workflow.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicMapper topicMapper;

    public void addTopic(Topic<List<HashMap>> topic){
        String input = "";
        try {
            input = JSONArray.toJSONString(topic.getInput());
        } catch (Exception e){
            throw new DefinitionException(400, "topic入参不符合要求");
        }

        topicMapper.insert(topic.getTopic(),topic.getName(),input);
    }

    public List<Topic<List<HashMap>>> getTopic(){
        List<Topic<String>> topicList = topicMapper.getAll();



        List<Topic<List<HashMap>>> list = new ArrayList<>();
        for (Topic<String> topic : topicList){
            Topic<List<HashMap>> ele = new Topic<>();
            String input = topic.getInput();
            List<HashMap> mapList = JSONArray.parseArray(input, HashMap.class);
            ele.setTopic(topic.getTopic());
            ele.setName(topic.getName());
            ele.setInput(mapList);
            list.add(ele);
        }

        return list;
    }
}
