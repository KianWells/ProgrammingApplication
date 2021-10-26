package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDetailsRepo {
    @Autowired
    DynamoDBMapper dynamoDBMapper;


    public Student save(Student student){
        dynamoDBMapper.save(student);
        return student;
    }

    public Student getStudentByUsername(String username){
        return dynamoDBMapper.load(Student.class, username);
    }

    public String delete(String username){
        dynamoDBMapper.delete(getStudentByUsername(username));
        return "Student deleted";
    }

    public String update(String username, Student student){
        dynamoDBMapper.save(student,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("username",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));
        return username;
    }
}
