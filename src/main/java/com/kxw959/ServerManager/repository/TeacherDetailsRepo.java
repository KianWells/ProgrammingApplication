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
import com.kxw959.ServerManager.entity.CourseObject;
import com.kxw959.ServerManager.entity.GroupObject;
import com.kxw959.ServerManager.entity.Teacher;
import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.interfaces.UserReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.model.User;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.requestOptions.ListUserCoursesOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDetailsRepo {
    @Autowired
    DynamoDBMapper dynamoDBMapper;

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
        List<CourseObject> courseObjects = new ArrayList<>();
        for(Course c: courses){
            String courseID =  Long.toString(c.getId());
            List<String> groupsIDs = getIDs("courses/"+courseID+"/groups", teacher.getOauth());
            List<GroupObject> groupObjects = new ArrayList<>();
            for(String groupID: groupsIDs){
                List<String> idsInGroup = getIDs("groups/"+groupID+"/users", teacher.getOauth());
                GroupObject groupObject = new GroupObject();
                groupObject.setGroupID(groupID);
                groupObject.setStudentIDs(idsInGroup);
                groupObjects.add(groupObject);
            }
            CourseObject courseObject = new CourseObject();
            courseObject.setCourseID(courseID);
            courseObject.setGroups(groupObjects);
            courseObjects.add(courseObject);
        }
        teacher.setCourses(courseObjects);
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

    public List<String> getIDs(String urlStr, String oauth) throws IOException {
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
            List<String> ids = new ArrayList<>();
            for(JsonElement element: user){
                JsonObject object = (JsonObject) element;
                ids.add(object.get("id").getAsString());
            }
            return ids;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
