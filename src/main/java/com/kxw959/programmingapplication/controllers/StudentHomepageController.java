package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.Task;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.JAVAUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class StudentHomepageController {

    @FXML
    public VBox taskList;

    @FXML
    public VBox leaderboard;
    int numCards = 2;
    String selectedTask = "";

    @FXML
    void initialize(){
        JAVAUtil javaUtil = new JAVAUtil();
        try {
            JsonObject student = NetworkManager.getJSONObjectFromURL(User.url);
            assert student != null;
            initLeaderboard(student.get("className").getAsString());
            JsonArray tasks = (JsonArray) student.get("tasks");
            System.out.println(tasks);
            for(JsonElement j : tasks){
                Task task = new Task();
                JsonObject obj = (JsonObject) j;
                task.taskID = obj.get("taskID").getAsString();
                task.totalTests = obj.get("totalTests").getAsInt();
                task.testsPassed = obj.get("score").getAsInt()/10;
                task.completed = obj.get("completed").getAsBoolean();
                addTask(task.taskID, task.completed);
                JsonArray fileNames = obj.get("fileNames").getAsJsonArray();
                for(JsonElement s: fileNames){
                    JsonObject taskFiles = (JsonObject) s;
                    String name = taskFiles.get("fileName").getAsString();
                    String type = taskFiles.get("type").getAsString();
                    String fileName = "jpa2021_"+name;
                    System.out.println(fileName);
                    if(!NetworkManager.getFile(User.username + "/" + fileName, name)){
                        NetworkManager.getFile(fileName, name);
                    }
                    if(type.equals("start")){
                        String startPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                        String startName = name.replaceAll("[.]java", "");
                        javaUtil.changePackageName(startPath, "package com.kxw959.programmingapplication.tasks;");
                        task.start = new Pair<>(startName, startPath);
                    }
                    if(type.equals("instructions")){
                        String instructionsName = name.replaceAll("[.]txt", "");
                        instructionsName = instructionsName.replaceAll("[.]pdf", "");
                        String instructionsPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                        task.instructions = new Pair<>(instructionsName, instructionsPath);
                    }
                    if(type.equals("test")){
                        String testName = name.replaceAll("[.]java", "");
                        String testPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                        task.test = new Pair<>(testName, testPath);
                    }
                }
                User.taskList.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Task task : User.taskList){
            javaUtil.updateImports(task.test.getValue(), "import com.kxw959.programmingapplication.tasks."+task.start.getKey(), task.start.getKey());
            task.junitVersion = javaUtil.changePackageName(task.test.getValue(), "package com.kxw959.programmingapplication.tasks;");
        }
    }

    private void addTask(String name, boolean completed){
        HBox hbox;
        if (numCards == 2){
            numCards = 1;
            hbox = new HBox();
            hbox.setSpacing(20);
            hbox.setPrefHeight(250);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.setPadding(new Insets(20,10,20,10));
            hbox.setFillHeight(true);
            taskList.getChildren().add(hbox);
        }
        else{
            hbox = (HBox) taskList.getChildren().get(taskList.getChildren().size()-1);
            hbox.setAlignment(Pos.CENTER);
            numCards = 2;
        }

        Button taskBtn = new Button(name);
        taskBtn.setMaxSize(30000000, 3000000);
        taskBtn.setPrefWidth(300);
        taskBtn.setOnAction(event -> {
            selectedTask = taskBtn.getText();
            onCLickEditor();
        });
        if(completed) {
            taskBtn.setStyle("-fx-text-fill: green");
        }
        hbox.getChildren().add(taskBtn);
    }

    private void initLeaderboard(String className){
        try {
            List<JsonObject> students = NetworkManager.getClassData(className);
            int i=0;
            System.out.println(students);
            for(JsonObject s : students){
                if(i<10){
                    GridPane gp = (GridPane) leaderboard.getChildren().get(i);
                    gp.add(new Label(s.get("name").getAsString()), 1, 0);
                    gp.add(new Label(Integer.toString(addUpScores(s))), 2, 0);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int addUpScores(JsonObject student){
        JsonArray tasks = (JsonArray) student.get("tasks");
        System.out.println(tasks);
        int finalScore = 0;
        for(JsonElement t: tasks){
            JsonObject task = (JsonObject) t;
            int score = task.get("score").getAsInt();
            finalScore+=score;
        }
        return finalScore;
    }

    @FXML
    void onCLickEditor(){
        for(Task t : User.taskList){
            if(Objects.equals(t.taskID, selectedTask)){
                User.selectedTask = User.taskList.get(User.taskList.indexOf(t));
            }
        }
        SceneManager.switchScene("code-editor.fxml");
    }

    public void onClickLogOut(ActionEvent event) {
        SceneManager.switchScene("main-menu.fxml");
    }
}
