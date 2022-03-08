package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.kxw959.ServerManager.config.CanvasConfig;
import com.kxw959.ServerManager.entity.Student;
import com.kxw959.ServerManager.entity.StudentTask;
import com.kxw959.ServerManager.entity.Students;
import com.kxw959.ServerManager.entity.User;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.impl.AssignmentImpl;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.interfaces.AssignmentWriter;
import edu.ksu.canvas.interfaces.SubmissionWriter;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.model.assignment.AssignmentGroup;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class StudentDetailsRepo {
    @Autowired
    DynamoDBMapper dynamoDBMapper;

    CanvasConfig canvas = new CanvasConfig();

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

    public String update(String username, Student student) throws IOException {
        dynamoDBMapper.save(student,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("username",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));

        //update task score
        CanvasApiFactory factory = canvas.getApiFactory();
        AssignmentReader assignmentReader = factory.getReader(AssignmentReader.class, canvas.getOauthToken());
        ListCourseAssignmentsOptions listCourseAssignmentsOptions = new ListCourseAssignmentsOptions(canvas.getCourseID());
        List<Assignment> assignments = assignmentReader.listCourseAssignments(listCourseAssignmentsOptions);
        for(StudentTask t : student.getTasks()){
            for(Assignment a : assignments){
                if(a.getName().equals(t.getTaskID())){
                    gradeAssignment(t.getScore()/10,  a.getId());
                }
            }
        }
        return username;
    }

    private void gradeAssignment(int grade, Long id) throws IOException {
        CanvasApiFactory factory = canvas.getApiFactory();
        SubmissionWriter submissionWriter = factory.getWriter(SubmissionWriter.class, canvas.getOauthToken());
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> studentSubmissionOptionMap = new HashMap<>();
        MultipleSubmissionsOptions multipleSubmissionsOptions = new MultipleSubmissionsOptions(canvas.getCourseID(), id, new HashMap<>());
        MultipleSubmissionsOptions.StudentSubmissionOption studentSubmissionOption =
                multipleSubmissionsOptions.createStudentSubmissionOption(null, ""+grade+"",
                        null,
                        null,
                        null,
                        null);
        multipleSubmissionsOptions.setAssignmentId(id);
        studentSubmissionOptionMap.put("student id", studentSubmissionOption);
        multipleSubmissionsOptions.setStudentSubmissionOptionMap(studentSubmissionOptionMap);
        submissionWriter.gradeMultipleSubmissionsByCourse(multipleSubmissionsOptions);
    }

    public Students getUsersByClass(String className){
        Students students = new Students();
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":className", new AttributeValue().withS(className));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("className = :className")
                .withExpressionAttributeValues(eav);
        List<Student> ls = dynamoDBMapper.scan(Student.class, scanExpression);
        students.setListOfStudents(ls);
        return students;
    }

    public String addTask(String className, StudentTask task) throws IOException {
        List<Student> students = getUsersByClass(className).getListOfStudents();
        for (Student student : students) {
            List<StudentTask> tasks = new ArrayList<>();
            if(student.getTasks()!=null){
                tasks.addAll(student.getTasks());
            }
            tasks.add(task);
            student.setTasks(tasks);
            update(student.getUsername(), student);
        }
        //create new canvas task
        Assignment assignment = new Assignment();
        assignment.setName(task.getTaskID());
        List<String> subType = new ArrayList<>();
        subType.add("none");
        assignment.setSubmissionTypes(subType);
        assignment.setPointsPossible((double) task.getTotalTests());

        CanvasApiFactory factory = canvas.getApiFactory();
        AssignmentWriter assignmentWriter = factory.getWriter(AssignmentWriter.class, canvas.getOauthToken());
        assignmentWriter.createAssignment(canvas.getCourseID(), assignment);
        return "Tasks Added";
    }
}
