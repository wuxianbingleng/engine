package com.example.workflow.mapper;


import com.example.workflow.entity.TaskVariable;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskVariableMapper {

    @Select("select * from act_re_task_variable where taskDefinitionKey = #{taskDefinitionKey}")
    TaskVariable getOne(String taskDefinitionKey);


    @Insert("insert into act_re_task_variable values(#{taskDefinitionKey}, #{variables}) on duplicate key update variables = #{variables}")
    Integer findOrCreate(@Param("taskDefinitionKey")String taskDefinitionKey, @Param("variables")String variables);
}
