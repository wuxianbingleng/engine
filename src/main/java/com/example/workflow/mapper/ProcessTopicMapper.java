package com.example.workflow.mapper;

import com.example.workflow.entity.ProcessTopic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessTopicMapper {

    @Select("SELECT * FROM ACT_RE_PROC_TOPIC WHERE `processDefinitionKey` = #{processDefinitionKey}")
    @Results()
    List<ProcessTopic> getAll(String processDefinitionKey);

    @Insert("insert into ACT_RE_PROC_TOPIC values(#{processDefinitionKey},#{topic}) on duplicate key update topic = #{topic}")
    Integer findOrCreate(@Param("processDefinitionKey")String processDefinitionKey, @Param("topic")String topic);
}
