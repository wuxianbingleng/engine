package com.example.workflow.mapper;


import com.example.workflow.entity.Variable;
import org.apache.ibatis.annotations.*;

@Mapper
public interface VariableMapper {

    @Select("select TYPE_ `type`,NAME_ `name`,PROC_INST_ID_ processInstanceId," +
            "LONG_ longValue,DOUBLE_ doubleValue,TEXT_ textValue " +
            "from act_ru_variable where PROC_INST_ID_ = #{processInstanceId} and NAME_ = #{name}")
    Variable getOne(@Param("processInstanceId")String processInstanceId,@Param("name")String name);

}
