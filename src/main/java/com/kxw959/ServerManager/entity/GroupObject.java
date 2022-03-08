package com.kxw959.ServerManager.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBDocument
public class GroupObject {
    @DynamoDBAttribute
    String groupID;

    @DynamoDBAttribute
    List<String> studentIDs;
}
