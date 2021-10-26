package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.entity.Teacher;
import com.kxw959.ServerManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeacherDetailsRepo {
    @Autowired
    DynamoDBMapper dynamoDBMapper;


    public Teacher save(Teacher teacher){
        dynamoDBMapper.save(teacher);
        return teacher;
    }

    public Teacher getTeacherByUsername(String username){
        return dynamoDBMapper.load(Teacher.class, username);
    }

    public String delete(String username){
        dynamoDBMapper.delete(getTeacherByUsername(username));
        return "Teacher deleted";
    }

    public String update(String username, Teacher teacher){
        dynamoDBMapper.save(teacher,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("username",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));
        return username;
    }
}
