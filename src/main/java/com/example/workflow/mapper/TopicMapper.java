package com.example.workflow.mapper;
import com.example.workflow.entity.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TopicMapper {

    @Select("select * from ACT_RE_TOPIC where topic = #{topic}")
    Topic<String> getOne(String topic);

    @Select("select * from ACT_RE_TOPIC")
    List<Topic<String>> getAll();

    @Insert("insert into ACT_RE_TOPIC values(#{topic},#{name},#{input}) on duplicate key update `name` = #{name},`input` = #{input}")
    Integer insert(@Param("topic")String topic,@Param("name")String name,@Param("input")String input);
}
