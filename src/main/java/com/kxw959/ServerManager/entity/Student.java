package com.kxw959.ServerManager.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "LoginTable")
public class Student {
    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String password;

    @DynamoDBAttribute
    private String name;

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private boolean teacher = false;

    @DynamoDBAttribute
    private String courseName;

    @DynamoDBAttribute
    private String courseID;

    @DynamoDBAttribute
    private String className;

    @DynamoDBAttribute
    private String teacherID;

    @DynamoDBAttribute
    private List<StudentTask> tasks;
}
