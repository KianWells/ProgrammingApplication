package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.JAVAUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class StudentHomepageController {

    @FXML
    public VBox taskList;

    @FXML
    public VBox leaderboard;
    int numCards = 2;

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
                JsonObject obj = (JsonObject) j;
                addTask(obj.get("taskID").getAsString());
                JsonArray fileNames = obj.get("fileNames").getAsJsonArray();
                for(JsonElement s: fileNames){
                    JsonObject taskFiles = (JsonObject) s;
                    String name = taskFiles.get("fileName").getAsString();
                    String type = taskFiles.get("type").getAsString();
                    String fileName = "jpa2021_"+name;
                    System.out.println(fileName);
                    NetworkManager.getFile(fileName, name);
                    if(type.equals("start")){
                        User.startPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                        User.start = name.replaceAll("[.]java", "");
                        javaUtil.changePackageName(User.startPath, "package com.kxw959.programmingapplication.tasks;");
                    }
                    if(type.equals("instructions")){
                        User.instructions = name;
                        User.instructionsPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                    }
                    if(type.equals("test")){
                        User.test = name.replaceAll("[.]java", "");
                        User.testPath = "src/main/java/com/kxw959/programmingapplication/tasks/"+name;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        javaUtil.updateImports(User.testPath, "import com.kxw959.programmingapplication.tasks."+User.start, User.start);
        javaUtil.changePackageName(User.testPath, "package com.kxw959.programmingapplication.tasks;");
        System.out.println(User.start);
        System.out.println(User.instructions);
    }

    private void addTask(String name){
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
        taskBtn.setOnAction(event -> onCLickEditor());
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
        SceneManager.switchScene("code-editor.fxml");
    }
}
