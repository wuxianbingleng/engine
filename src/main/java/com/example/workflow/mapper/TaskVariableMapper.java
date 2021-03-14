package com.example.workflow.mapper;


import com.example.workflow.entity.TaskVariable;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskVariableMapper {

    @Select("select * from ACT_RE_TASK_VARIABLE where taskDefinitionKey = #{taskDefinitionKey}")
    TaskVariable getOne(String taskDefinitionKey);


    @Insert("insert into ACT_RE_TASK_VARIABLE values(#{taskDefinitionKey}, #{variables}) on duplicate key update variables = #{variables}")
    Integer findOrCreate(@Param("taskDefinitionKey")String taskDefinitionKey, @Param("variables")String variables);
}
