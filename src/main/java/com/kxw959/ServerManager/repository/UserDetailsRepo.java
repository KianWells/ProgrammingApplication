package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.entity.Teacher;
import com.kxw959.ServerManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDetailsRepo {

    @Autowired
    DynamoDBMapper dynamoDBMapper;


    public User save(User user){
        dynamoDBMapper.save(user);
        return user;
    }

    public User getUserByUsername(String username){
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":username", new AttributeValue().withS(username));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("username = :username")
                .withExpressionAttributeValues(eav);
        List<User> ls = dynamoDBMapper.scan(User.class, scanExpression);
        return dynamoDBMapper.load(User.class, ls.get(0).getId());
    }

    public String delete(String username){
        dynamoDBMapper.delete(getUserByUsername(username));
        return "User deleted";
    }

    public String update(String username, User user){
        dynamoDBMapper.save(user,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("username",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));
        return username;
    }
}
