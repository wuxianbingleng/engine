package com.example.workflow.camunda;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ExternalTask {

    static{
        System.out.println("execute");
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8000/engine-rest")
                .workerId("some-random-id")
                .build();
        client.subscribe("creditScoreChecker")
                .lockDuration(1000)
                .handler((externalTask, externalTaskService) -> {

                    // retrieve a variable from the Workflow Engine
                    // int defaultScore = externalTask.getVariable("defaultScore");

                    List<Integer> creditScores = new ArrayList<>(Arrays.asList(5, 9, 1, 4, 10));

                    // create an object typed variable
                    ObjectValue creditScoresObject = Variables
                            .objectValue(creditScores)
                            .create();

                    // complete the external task
                    externalTaskService.complete(externalTask,
                            Collections.singletonMap("creditScores", creditScoresObject));

                    System.out.println("The External Task " + externalTask.getId() + " has been completed!");

                }).open();
        // subscribe to the topic
        client.subscribe("loanGranter").lockDuration(1000).handler((externalTask, externalTaskService)->{
            System.out.println("The External Task " + externalTask.getId() + " has been completed!");
            externalTaskService.complete(externalTask);
        }).open();
        client.subscribe("send-message").lockDuration(1000).handler((externalTask, externalTaskService)->{
            System.out.println("The External Task " + externalTask.getId() + " has been completed!");
            externalTaskService.complete(externalTask);
        }).open();


        }

}
