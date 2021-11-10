package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.StudentTask;
import com.kxw959.ServerManager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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

    public List<Student> getUsersByClass(String className){
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":className", new AttributeValue().withS(className));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("className = :className")
                .withExpressionAttributeValues(eav);
        List<Student> students = dynamoDBMapper.scan(Student.class, scanExpression);
        return students;
    }

    public String addTask(String className, StudentTask task){
        List<Student> students = getUsersByClass(className);
        for (Student student : students) {
            List<StudentTask> tasks = new ArrayList<>();
            if(student.getTasks()!=null){
                tasks.addAll(student.getTasks());
            }
            tasks.add(task);
            student.setTasks(tasks);
            update(student.getUsername(), student);
        }
        return "Tasks Added";
    }
}
