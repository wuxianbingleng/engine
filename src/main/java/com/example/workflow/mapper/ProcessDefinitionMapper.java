package com.example.workflow.mapper;

import org.apache.ibatis.annotations.*;
import com.example.workflow.entity.ProcessDefinition;

import java.util.List;

@Mapper
public interface ProcessDefinitionMapper {


    @Select("SELECT ID_ `id`,KEY_ `key`,NAME_ `name`,DEPLOYMENT_ID_ `deploymentId` FROM act_re_procdef")
    @Results()
    List<ProcessDefinition> getAll();

    @Select("SELECT ID_ `id`,KEY_ `key`,NAME_ `name`,DEPLOYMENT_ID_ `deploymentId` FROM act_re_procdef WHERE DEPLOYMENT_ID_ = #{deploymentId}")
    @Results()
    ProcessDefinition getOneByDeploymentId(String deploymentId);

    @Select("SELECT ID_ `id`,KEY_ `key`,NAME_ `name`,DEPLOYMENT_ID_ `deploymentId` " +
            "FROM act_re_procdef WHERE KEY_ = #{processDefinitionKey} order by VERSION_ desc limit 1")
    @Results()
    ProcessDefinition getOne(String processDefinitionKey);

}
