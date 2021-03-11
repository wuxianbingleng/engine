package com.example.workflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.example.workflow.entity.Task;
import java.util.List;

@Mapper
public interface TaskMapper {
    @Select("select " +
            "ID_ `id`,PROC_DEF_ID_ processDefinitionId,CREATE_TIME_ createTime, TASK_DEF_KEY_ taskDefinitionKey" +
            ",NAME_ `name`,PROC_INST_ID_ processInstanceId, ASSIGNEE_ userId" +
            " from act_ru_task " +
            " where PROC_DEF_ID_ = #{processDefinitionId} and ASSIGNEE_ = #{userId}")
    List<Task> getAll(@Param("processDefinitionId")String processDefinitionId, @Param("userId")String userId);


    @Select("select " +
            "ID_ `id`,PROC_DEF_ID_ processDefinitionId,CREATE_TIME_ createTime, TASK_DEF_KEY_ taskDefinitionKey" +
            ",NAME_ `name`,PROC_INST_ID_ processInstanceId, ASSIGNEE_ userId" +
            " from act_ru_task " +
            " where ID_ = #{taskId}")
    Task getOne(String taskId);
}
