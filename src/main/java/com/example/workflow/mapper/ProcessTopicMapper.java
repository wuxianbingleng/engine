package com.example.workflow.mapper;

import com.example.workflow.entity.ProcessTopic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessTopicMapper {

    @Select("SELECT * FROM act_re_proc_topic WHERE `processDefinitionKey` = #{processDefinitionKey}")
    @Results()
    List<ProcessTopic> getAll(String processDefinitionKey);

    @Insert("insert into act_re_proc_topic values(#{processDefinitionKey},#{topic}) on duplicate key update topic = #{topic}")
    Integer findOrCreate(@Param("processDefinitionKey")String processDefinitionKey, @Param("topic")String topic);
}
