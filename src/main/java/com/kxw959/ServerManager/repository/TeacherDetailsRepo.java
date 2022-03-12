package com.kxw959.ServerManager.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kxw959.ServerManager.config.CanvasConfig;
import com.kxw959.ServerManager.entity.*;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.AssignmentGroupWriter;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.interfaces.UserReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.User;
import edu.ksu.canvas.model.assignment.AssignmentGroup;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.requestOptions.ListUserCoursesOptions;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class TeacherDetailsRepo {
    @Autowired
    DynamoDBMapper dynamoDBMapper;

    @Autowired
    StudentDetailsRepo studentDetailsRepo;

    CanvasConfig canvas = new CanvasConfig();

    public Teacher save(Teacher teacher) throws IOException {
        //get teacher id
        CanvasApiFactory factory = canvas.getApiFactory();
        UserReader userReader = factory.getReader(UserReader.class, new NonRefreshableOauthToken(teacher.getOauth()));
        Optional<User> user = userReader.showUserDetails("self");
        teacher.setId(Long.toString(user.get().getId()));

        //get teacher courses
        CourseReader courseReader = factory.getReader(CourseReader.class, new NonRefreshableOauthToken(teacher.getOauth()));
        ListUserCoursesOptions options = new ListUserCoursesOptions(teacher.getId());
        List<Course> courses  = courseReader.listUserCourses(options);
        CourseObject course = new CourseObject();
        for(Course c: courses){
            if(c.getName().equals(teacher.getCourseName())){
                course.setCourseID(Long.toString(c.getId()));
                course.setCourseName(c.getName());
                List<StudentPair> idsInCourse = getIDs("courses/"+course.getCourseID()+"/users", teacher.getOauth());
                course.setStudents(idsInCourse);
                teacher.setCourse(course);
                teacher.setClassNames(new ArrayList<String>());
                addAssignmentGroup(teacher, "Class A");
                for(StudentPair pair: idsInCourse){
                    Student student;
                    student = new Student();
                    student.setName(pair.getStudentName());
                    student.setId(pair.getStudentID());
                    student.setPassword(RandomStringUtils.randomAlphanumeric(6));
                    student.setUsername(student.getName().replaceAll(" ", "").toLowerCase(Locale.ROOT)+RandomStringUtils.randomAlphanumeric(3));
                    student.setTasks(new ArrayList<>());
                    student.setCourseName(course.getCourseName());
                    student.setCourseID(course.getCourseID());
                    student.setClassName("Group A");
                    student.setTeacherID(Long.toString(user.get().getId()));
                    dynamoDBMapper.save(student);
                }
            }
        }
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

    public List<StudentPair> getIDs(String urlStr, String oauth) throws IOException {
        String baseURL = canvas.getCanvasBaseUrl();
        URL url = new URL(baseURL+"/api/v1/"+urlStr);
        System.out.println(url);
        HttpURLConnection con;
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer "+oauth);
        con.connect();
        try{
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) con.getContent()));
            JsonArray user = root.getAsJsonArray(); //May be an array, may be an object
            con.disconnect();
            List<StudentPair> ids = new ArrayList<>();
            for(JsonElement element: user){
                JsonObject object = (JsonObject) element;
                String id = object.get("id").getAsString();
                String name = object.get("name").getAsString();
                ids.add(new StudentPair(id, name));
            }
            return ids;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String addAssignmentGroup(Teacher teacher, String groupName) throws IOException {
        CanvasApiFactory factory = canvas.getApiFactory();
        AssignmentGroupWriter assignmentGroupWriter = factory.getWriter(AssignmentGroupWriter.class, new NonRefreshableOauthToken(teacher.getOauth()));
        AssignmentGroup assignmentGroup = new AssignmentGroup();
        assignmentGroup.setName(groupName);
        assignmentGroupWriter.createAssignmentGroup(teacher.getCourse().getCourseID(), assignmentGroup);
        teacher.getClassNames().add(groupName);
        return "Group Added";
    }
}
