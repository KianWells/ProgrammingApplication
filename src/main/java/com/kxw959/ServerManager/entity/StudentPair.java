package com.kxw959.ServerManager.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBDocument
@AllArgsConstructor
@NoArgsConstructor
public class StudentPair {
    @DynamoDBAttribute
    String studentID;

    @DynamoDBAttribute
    String studentName;
}
