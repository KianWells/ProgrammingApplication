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
import com.kxw959.ServerManager.entity.Teacher;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.*;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.model.assignment.AssignmentGroup;
import edu.ksu.canvas.model.assignment.AssignmentOverride;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.requestOptions.ListAssignmentGroupOptions;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import edu.ksu.canvas.requestOptions.ListUserAssignmentOptions;
import edu.ksu.canvas.requestOptions.MultipleSubmissionsOptions;
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

    public Student getStudentByID(String id){
        return dynamoDBMapper.load(Student.class, id);
    }

    public String delete(String username){
        dynamoDBMapper.delete(getStudentByID(username));
        return "Student deleted";
    }

    public String update(String username, Student student) throws IOException {
        dynamoDBMapper.save(student,
                new DynamoDBSaveExpression()
                        .withExpectedEntry("id",
                                new ExpectedAttributeValue(
                                        new AttributeValue().withS(username)
                                )));

        Teacher teacher = dynamoDBMapper.load(Teacher.class, student.getTeacherID());
        //update task score
        CanvasApiFactory factory = canvas.getApiFactory();
        AssignmentReader assignmentReader = factory.getReader(AssignmentReader.class, new NonRefreshableOauthToken(teacher.getOauth()));
        ListCourseAssignmentsOptions listCourseAssignmentsOptions = new ListCourseAssignmentsOptions(teacher.getCourse().getCourseID());
        List<Assignment> assignments = assignmentReader.listCourseAssignments(listCourseAssignmentsOptions);
        for(StudentTask t : student.getTasks()){
            for(Assignment a : assignments){
                if(a.getName().equals(t.getTaskID())){
                    gradeAssignment(t.getScore()/10,  a.getId(), teacher);
                }
            }
        }
        return username;
    }

    private void gradeAssignment(int grade, Long id, Teacher teacher) throws IOException {
        CanvasApiFactory factory = canvas.getApiFactory();
        SubmissionWriter submissionWriter = factory.getWriter(SubmissionWriter.class, new NonRefreshableOauthToken(teacher.getOauth()));
        Map<String, MultipleSubmissionsOptions.StudentSubmissionOption> studentSubmissionOptionMap = new HashMap<>();
        MultipleSubmissionsOptions multipleSubmissionsOptions = new MultipleSubmissionsOptions(teacher.getCourse().getCourseID(), id, new HashMap<>());
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
        System.out.println(className);
        List<Student> students = getUsersByClass(className).getListOfStudents();
        for (Student student : students) {
            List<StudentTask> tasks = new ArrayList<>();
            if(student.getTasks()!=null){
                tasks.addAll(student.getTasks());
            }
            tasks.add(task);
            student.setTasks(tasks);
            update(student.getId(), student);
        }
        //create new canvas task
        CanvasApiFactory factory = canvas.getApiFactory();
        Assignment assignment = new Assignment();
        assignment.setName(task.getTaskID());
        List<String> subType = new ArrayList<>();
        subType.add("none");
        assignment.setSubmissionTypes(subType);
        assignment.setPointsPossible((double) task.getTotalTests());
        assignment.setPublished(true);
        Teacher teacher = dynamoDBMapper.load(Teacher.class, students.get(0).getTeacherID());
        AssignmentGroupReader reader = factory.getReader(AssignmentGroupReader.class, new NonRefreshableOauthToken(teacher.getOauth()));
        ListAssignmentGroupOptions options = new ListAssignmentGroupOptions(students.get(0).getCourseID());
        List<AssignmentGroup> group = reader.listAssignmentGroup(options);
        for(AssignmentGroup ag: group){
            if(ag.getName().equals(className)){
                assignment.setAssignmentGroupId(ag.getId());
            }
        }

        AssignmentWriter assignmentWriter = factory.getWriter(AssignmentWriter.class, new NonRefreshableOauthToken(teacher.getOauth()));
        assignmentWriter.createAssignment(students.get(0).getCourseID(), assignment);
        return "Tasks Added";
    }
}
