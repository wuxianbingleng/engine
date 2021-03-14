package com.example.workflow.mapper;

import com.example.workflow.entity.ProcessVariable;
import org.apache.ibatis.annotations.*;


@Mapper
public interface ProcessVariableMapper {

    @Select("SELECT * FROM ACT_RE_PROC_VARIABLE WHERE `processDefinitionKey` = #{processDefinitionKey}")
    @Results()
    ProcessVariable getOne(String processDefinitionKey);

    @Insert("insert into ACT_RE_PROC_VARIABLE values(#{processDefinitionKey}, #{variables}) on duplicate key update variables = #{variables}")
    Integer findOrCreate(@Param("processDefinitionKey")String processDefinitionKey, @Param("variables")String variables);
}
