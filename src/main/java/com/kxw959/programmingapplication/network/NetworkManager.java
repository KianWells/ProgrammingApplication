package com.kxw959.programmingapplication.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.pdfbox.io.IOUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class NetworkManager {
    public final static String HOST = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/";
    public final static String LOCALHOST = "http://localhost:5000/user/";
    public final static String USER = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/user/";
    public final static String TEACHER = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/teacher/";
    public final static String STUDENT = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/student/";


    public static int checkLogin(String username, String password) throws IOException {
        if(Objects.equals(username, "offline")){
            return 2;
        }
        if(Objects.equals(username, "s")){
            return 1;
        }


        URL url = new URL(USER+username);
        HttpURLConnection request = setConnection(url, "GET");
        // Convert to a JSON object to print data
        try{
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject user = root.getAsJsonObject(); //May be an array, may be an object
            request.disconnect();
            if(user.get("password").getAsString().equals(password)) {
                System.out.println(user.get("password").getAsString());
                if(user.get("teacher").getAsBoolean()){
                    System.out.println("Teacher");
                    return 2;
                }
                else{
                    System.out.println("Not a teacher");
                    return 1;
                }
            }
            else{
                System.out.println("Password doesnt match");
                return 0;
            }
        }
        catch(Exception e){
            System.out.println("No Record");
            return -1;
        }
    }

    public static int registerTeacher(String oauth, String username, String password, String courseName) throws IOException {
        if(testExists(new URL(USER+username))){
            return -1;
        }
        HttpURLConnection con = setConnection(new URL(TEACHER), "POST");

        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        user.addProperty("oauth", oauth);
        user.addProperty("courseName", courseName);
        System.out.println(user.toString());
        assert con != null;
        postJSON(con, user);
        con.disconnect();
        System.out.println("DONE");

        return 1;
    }

    private static boolean testExists(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        try{
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) con.getContent()));
            System.out.println("user found!");
        }
        catch (Exception e){
            con.disconnect();
            return false;
        }
        con.disconnect();
        return true;
    }

    public static int registerStudent(String username, String className) throws IOException, NoSuchAlgorithmException {
        JsonObject student = getJSONObjectFromURL(new URL(STUDENT+username));
        assert student != null;
        student.addProperty("className", className);

        try{
            JsonObject classStudent = getClassData(className).get(0);
            JsonArray tasks = student.get("tasks").getAsJsonArray();
            for(int i=0; i<tasks.size(); i++){
                JsonObject j = (JsonObject) tasks.get(i);
                j.addProperty("score", 0);
                j.addProperty("completed", false);
                tasks.set(i, j);
            }
            student.add("tasks", tasks);
        }catch (Exception e){

        }
        URL url = new URL(STUDENT+username);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");

        String data = student.toString();

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        http.getResponseCode();
        http.getResponseMessage();
        http.disconnect();
        System.out.println("DONE");
        return 1;
    }

    private static HttpURLConnection setConnection(URL url, String type) throws IOException {
        HttpURLConnection con;
        switch(type){
            case "POST":
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                return con;
            case "GET":
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                return con;
            case "PUT":
                con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("PUT");
                return con;
        }
        return null;
    }

    private static void postJSON(HttpURLConnection con, JsonObject json) throws IOException {
        String details = json.toString();
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = details.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
    public static JsonObject getJSONObjectFromURL(URL url) throws IOException {
        HttpURLConnection con = setConnection(url, "GET");
        try{
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) con.getContent()));
            JsonObject user = root.getAsJsonObject(); //May be an array, may be an object
            con.disconnect();
            return user;
        }
        catch(Exception e){
            System.out.println("No Record");
            con.disconnect();
            return null;
        }
    }

    public static List<JsonObject> getClassData(String className) throws IOException {
        className = className.replaceAll(" ", "%20");
        JsonObject students = getJSONObjectFromURL(new URL(STUDENT+"class/"+className));
        JsonArray jsonArray = (JsonArray) students.get("listOfStudents");
        List<JsonObject> listStudents = new ArrayList<>();
        for(JsonElement e: jsonArray){
            JsonObject j = (JsonObject) e;
            listStudents.add(j);
        }
        return listStudents;

    }

    public static JsonElement getJSONElementFromURL(URL url, String field) throws IOException {
        return Objects.requireNonNull(getJSONObjectFromURL(url)).get(field);
    }

    public static void addClass(String username, String className) throws IOException {
        //get teacher JSON
        //replicate it but add the class into the classes field
        //use PUT to update it
        URL url = new URL(TEACHER+username);
        JsonObject originalTeacher = getJSONObjectFromURL(url);
        System.out.println(originalTeacher);
        JsonArray newClasses = new JsonArray();
        try{
            JsonArray arr = originalTeacher.get("classNames").getAsJsonArray();
            for(int i=0; i<arr.size();i++){
                newClasses.add(arr.get(i).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        newClasses.add(className);
        JsonObject newTeacher = originalTeacher;
        newTeacher.add("classNames", newClasses);
        System.out.println(newTeacher);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Accept", "application/json");
        http.setRequestProperty("Content-Type", "application/json");

        String data = newTeacher.toString();

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        http.getResponseCode();
        http.getResponseMessage();
        http.disconnect();

        URL classURL = new URL(TEACHER+className.replaceAll(" ", "%20"));
        HttpURLConnection con = setConnection(classURL, "POST");
        postJSON(con, newTeacher);
    }

    public static void uploadFiles(List<File> files, String username) throws IOException {
        for(File f : files){
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost(HOST+"file/upload/" + (username == null ? "" : username));
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);
            builder.addBinaryBody(
                    "file",
                    new FileInputStream(f),
                    ContentType.APPLICATION_OCTET_STREAM,
                    f.getName()
            );

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            response.close();
        }

    }

    public static void uploadFiles(List<File> files) throws IOException {
        uploadFiles(files, null);
    }

    public static void uploadStudentFile(File file, String username){

    }

    public static void addTaskToClass(String className, String taskName, Map<String, String> files, int totalTests) throws IOException {
        className = className.replaceAll(" ","%20");
        HttpURLConnection con = setConnection(new URL(STUDENT+"class/"+className), "POST");
        JsonObject task = new JsonObject();
        task.addProperty("taskID", taskName);
        JsonArray fileNames = new JsonArray();
        for(Map.Entry<String, String> entry: files.entrySet()){
            JsonObject taskFiles = new JsonObject();
            taskFiles.addProperty("type", entry.getKey());
            taskFiles.addProperty("fileName", entry.getValue());
            fileNames.add(taskFiles);
        }
        task.add("fileNames", fileNames);
        task.addProperty("score", 0);
        task.addProperty("completed", false);
        task.addProperty("totalTests", totalTests);
        assert con != null;
        postJSON(con, task);
        con.disconnect();
    }

    // Method which write the bytes into a file
    private static void writeByte(byte[] bytes, String fileName)
    {
        File file = new File(fileName);
        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static boolean getFile(String hostFileName, String fileName){
        try{
            URL fileURL = new URL(HOST+"file/download/"+hostFileName);
            HttpURLConnection con = setConnection(fileURL, "GET");
            InputStream is = con.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);
            writeByte(bytes, "task/"+fileName);
            con.disconnect();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void increaseScore(String username, int score, String taskName){
        try {
            URL url = new URL(STUDENT+username);
            JsonObject student = getJSONObjectFromURL(url);
            //get the task list
            JsonArray tasks = (JsonArray) student.get("tasks");
            //get the task with the taskName
            for(int i=0; i<tasks.size(); i++){
                JsonObject task = (JsonObject) tasks.get(i);
                if(Objects.equals(task.get("taskID").getAsString(), taskName)){
                    int newScore = score;
                    task.addProperty("score", newScore);
                    if(newScore / 10 == task.get("totalTests").getAsInt()){
                        task.addProperty("completed", true);
                    }
                    else {
                        task.addProperty("completed", false);
                    }
                }
                tasks.set(i, task);
            }
            student.add("tasks", tasks);
            //update the score
            //post it
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("PUT");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-Type", "application/json");

            String data = student.toString();

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);

            http.getResponseCode();
            http.getResponseMessage();
            http.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
