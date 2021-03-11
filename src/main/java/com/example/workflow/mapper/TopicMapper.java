package com.example.workflow.mapper;
import com.example.workflow.entity.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TopicMapper {

    @Select("select * from act_re_topic where topic = #{topic}")
    Topic<String> getOne(String topic);

    @Select("select * from act_re_topic")
    List<Topic<String>> getAll();

    @Insert("insert into act_re_topic values(#{topic},#{name},#{input}) on duplicate key update `name` = #{name},`input` = #{input}")
    Integer insert(@Param("topic")String topic,@Param("name")String name,@Param("input")String input);
}
