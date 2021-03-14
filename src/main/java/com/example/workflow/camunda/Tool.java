package com.example.workflow.camunda;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.workflow.mapper.TaskVariableMapper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Tool {

    public static Map<String, Object> parseBpmnParams(File file) throws Exception {
        Map<String,Integer> startVariables = new HashMap();
        Map<String,Integer> localVariables = new HashMap();
        Map<String,Integer> topics = new HashMap();
        Map<String,String> taskVariableMap = new HashMap();
        //检测要匹配的单词，单词的前面必须是空格或者标点符号
        Pattern pattern = Pattern.compile("([a-zA-Z]+)");
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");

        //1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        Document document = reader.read(file);
        //3.获取根节点
        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator("process");
        while (iterator.hasNext()){
            String processDefinitionKey;
            Element stu = (Element) iterator.next();
            List<Attribute> stuAttributes = stu.attributes();
            for(Attribute attribute : stuAttributes){
                if(attribute.getName() == "id"){
                    processDefinitionKey = attribute.getValue();
                }
            }
            // 遍历userTask; 获取userTask 输出值存为本地变量
            Iterator iterator1 = stu.elementIterator("userTask");
            while (iterator1.hasNext()){
                String taskDefinitionKey = "";
                JSONArray jsonArray = new JSONArray();
                Element stuChild = (Element) iterator1.next();
                List<Attribute> userTaskAttrs = stuChild.attributes();
                for(Attribute userTaskAttr : userTaskAttrs){
                    if(userTaskAttr.getName() == "id"){
                        taskDefinitionKey = userTaskAttr.getValue();
                    }
                }
                Iterator iterator2 = stuChild.elementIterator("extensionElements");
                while (iterator2.hasNext()){
                    Element extensionElement = (Element) iterator2.next();
                    Iterator iterator3 = extensionElement.elementIterator("formData");

                    while (iterator3.hasNext()){
                        Element formData = (Element) iterator3.next();
                        Iterator iterator4 = formData.elementIterator("formField");

                        while (iterator4.hasNext()){
                            Element formField = (Element) iterator4.next();

                            List<Attribute> attributes = formField.attributes();
                            JSONObject jsonObject = new JSONObject();
                            for (Attribute attribute : attributes) {

                                if(attribute.getName() == "defaultValue") {
                                    Matcher matcher = pattern.matcher(attribute.getValue());
                                    while (matcher.find()){
                                       String e = matcher.group(0);
                                       if(e != "gt" && e!= "lt"){
                                           startVariables.put(e, 1);
                                       }
                                    }
                                }
                                if(attribute.getName() == "id"){
                                    jsonObject.put("name", attribute.getValue());
                                    localVariables.put(attribute.getValue(), 1);
                                }
                                if(attribute.getName() == "type"){
                                    jsonObject.put("type", attribute.getValue());
                                }

                            }
                            jsonArray.add(jsonObject);
                        }
                    }

                }
                taskVariableMap.put(taskDefinitionKey, jsonArray.toJSONString());

            }
            // 遍历sequenceFlow; 获取流程判断所需变量
            Iterator iterator6 = stu.elementIterator("sequenceFlow");
            while (iterator6.hasNext()){
                Element conditionExpression = (Element) iterator6.next();
                if(conditionExpression.getStringValue()!= ""){
                    Matcher m = p.matcher(conditionExpression.getStringValue());

                    String dest = m.replaceAll("");
                    Matcher matcher = pattern.matcher(dest);
                    if(matcher.find()){
                        String e = matcher.group(0);
                        if(localVariables.get(e) == null){
                            startVariables.put(e, 1);
                        }
                    }
                }
            }

            // 遍历serviceTask; 获取流程所绑定的所有外部任务topic
            Iterator iterator7 = stu.elementIterator("serviceTask");
            while (iterator7.hasNext()){
                Element serviceTask = (Element) iterator7.next();

                List<Attribute> serviceTaskAttr = serviceTask.attributes();
                for (Attribute attribute : serviceTaskAttr){
                    if(attribute.getName() == "topic"){
                        topics.put(attribute.getValue(), 1);
                    }
                }
            }

        }
        file.delete();
        Map<String, Object> result = new HashMap<>();
        String variables = "";
        for(String key: startVariables.keySet()){
            if(variables == ""){
                variables = variables + key;
            } else {
                variables = variables + "," + key;
            }
        }
        result.put("variables", variables);
        result.put("topics", topics);
        result.put("taskVariableMap", taskVariableMap);
        return result;
    }

}
