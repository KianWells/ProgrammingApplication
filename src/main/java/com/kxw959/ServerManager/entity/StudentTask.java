package com.kxw959.ServerManager.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class StudentTask {
    @DynamoDBAttribute
    String taskID;

    @DynamoDBAttribute
    List<TaskFiles> fileNames;

    @DynamoDBAttribute
    int score;

    @DynamoDBAttribute
    int totalTests;

    @DynamoDBAttribute
    boolean completed;
}
