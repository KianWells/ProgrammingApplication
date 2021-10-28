package com.kxw959.programmingapplication.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;

public class NetworkManager {
    public final String HOST = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/";
    public final static String LOCALHOST = "http://localhost:5000/user/";
    public final static String USER = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/user/";
    public final static String TEACHER = "http://servermanagerclassic-env.eba-mri23ppg.eu-west-2.elasticbeanstalk.com/teacher/";


    public static int checkLogin(String username, String password) throws IOException {

        URL url = new URL(USER+username);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestMethod("GET");
        request.connect();
        System.out.println("Connection finished");
        // Convert to a JSON object to print data
        try{
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject user = root.getAsJsonObject(); //May be an array, may be an object

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
        URL url = new URL(TEACHER);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JsonObject user = new JsonObject();
        user.addProperty("username", username);
        user.addProperty("password", password);
        user.addProperty("email", email);
        System.out.println(user.toString());
        String details = user.toString();

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
            return false;
        }
        return true;
    }

    public static void registerStudent(String name, String className) throws IOException, NoSuchAlgorithmException {
        String username = name.toLowerCase(Locale.ROOT).replaceAll("\\s", "");

        int counter = 1;
        String oldUsername = username;
        while(true){
            if(testExists(new URL(USER+username))) {
                username = oldUsername+counter;
            }
            else{
                break;
            }
        }
        String password = RandomStringUtils.randomAlphanumeric(8);
        System.out.println(username);
        System.out.println(password);
    }
}
