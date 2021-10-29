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
    @DynamoDBHashKey
    private String username;

    @DynamoDBAttribute
    private String password;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private boolean teacher = false;

    @DynamoDBAttribute
    private String className;

    @DynamoDBAttribute
    private List<StudentTask> tasks;
}
