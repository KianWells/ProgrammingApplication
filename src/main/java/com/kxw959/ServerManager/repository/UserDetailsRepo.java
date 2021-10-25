package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsRepo {

    @Autowired
    DynamoDBMapper dynamoDBMapper;


    public User save(User user){
        dynamoDBMapper.save(user);
        return user;
    }

    public User getUserByUsername(String username){
        return dynamoDBMapper.load(User.class, username);
    }

    public String delete(String username){
        dynamoDBMapper.delete(getUserByUsername(username));
        return "User deleted";
    }

    public String update(String username, User user){
        dynamoDBMapper.save(user,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("Username",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));
        return username;
    }
}
