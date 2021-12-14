package com.kxw959.programmingapplication.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.kxw959.programmingapplication.utils.JSONUtil;
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
import java.security.MessageDigest;
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

    public static int registerTeacher(String email, String username, String password) throws IOException {
        if(testExists(new URL(USER+username))){
            return -1;
        }
        HttpURLConnection con = setConnection(new URL(TEACHER), "POST");

        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        user.addProperty("email", email);
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

    public static int registerStudent(String name, String className) throws IOException, NoSuchAlgorithmException {
        String username = name.toLowerCase(Locale.ROOT).replaceAll("\\s", "");

        int counter = 1;
        String oldUsername = username;
        while(true){
            if(testExists(new URL(USER+username))) {
                username = oldUsername+counter;
                counter++;
            }
            else{
                break;
            }
        }
        String password = RandomStringUtils.randomAlphanumeric(8);
        System.out.println(username);
        System.out.println(password);

        HttpURLConnection con = setConnection(new URL(STUDENT), "POST");

        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        user.addProperty("className", className);
        user.addProperty("name", name);
        assert con != null;
        postJSON(con, user);
        con.disconnect();
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


    public static List<JsonObject> getStudentsInClass(String className) throws IOException {
        className = className.replaceAll(" ", "%20");
        HttpURLConnection con = setConnection(new URL(STUDENT+"class/"+className), "GET");
        try{
            assert con != null;
            InputStream is = con.getInputStream();
            JSONUtil jsonUtil = new JSONUtil();
            List<JsonObject> details = jsonUtil.getStudents(is);
            System.out.println(details);
            con.disconnect();
            return details;
        }
        catch(Exception e){
            e.printStackTrace();
            con.disconnect();
            return new ArrayList<>();
        }
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
            JsonArray arr = originalTeacher.get("classes").getAsJsonArray();
            for(int i=0; i<arr.size();i++){
                newClasses.add(arr.get(i).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        newClasses.add(className);
        JsonObject newTeacher = originalTeacher;
        newTeacher.add("classes", newClasses);
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
    }

    public static void uploadFiles(List<File> files) throws IOException {
        for(File f : files){
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost(HOST+"file/upload/");
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

    public static void addTaskToClass(String className, String taskName, Map<String, String> files) throws IOException {
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

    public static void getFile(String hostFileName, String fileName){
        try{
            URL fileURL = new URL(HOST+"file/download/"+hostFileName);
            HttpURLConnection con = setConnection(fileURL, "GET");
            InputStream is = con.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);
            writeByte(bytes, "src/main/java/com/kxw959/programmingapplication/tasks/"+fileName);
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
