package com.example.workflow.mapper;

import com.example.workflow.entity.ProcessInstanceHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProcessInstanceHistoryMapper {
    @Select("select PROC_INST_ID_ processInstanceId,PROC_DEF_ID_ processDefinitionId," +
            "START_TIME_ startTime, END_TIME_ endTime,START_ACT_ID_ startActivityId," +
            "END_ACT_ID_ endActivityId,STATE_ state from ACT_HI_PROCINST where PROC_INST_ID_ = #{processInstanceId}")
    ProcessInstanceHistory getOne(String processInstanceId);
}
