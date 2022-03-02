package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.Task;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.CSVUtil;
import com.kxw959.programmingapplication.utils.JAVAUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class StudentHomepageController {

    @FXML
    public VBox taskList;

    @FXML
    public VBox leaderboard;
    int numCards = 2;
    String selectedTask = "";
    Image badge, first, second, third;

    @FXML
    void initialize(){
        badge = new Image("com/kxw959/programmingapplication/badge.png");
        first =  new Image("com/kxw959/programmingapplication/1st.png");
        second =  new Image("com/kxw959/programmingapplication/2nd.png");
        third =  new Image("com/kxw959/programmingapplication/3rd.png");
        System.out.println(badge.getHeight());
        JAVAUtil javaUtil = new JAVAUtil();
        try {
            JsonObject student = NetworkManager.getJSONObjectFromURL(User.url);
            assert student != null;
            initLeaderboard(student.get("className").getAsString());
            if(!student.get("tasks").getClass().equals(JsonNull.class)){
                JsonArray tasks = (JsonArray) student.get("tasks");
                System.out.println(tasks);
                for(JsonElement j : tasks){
                    boolean isQuiz = false;
                    Task task = new Task();
                    JsonObject obj = (JsonObject) j;
                    task.taskID = obj.get("taskID").getAsString();
                    task.totalTests = obj.get("totalTests").getAsInt();
                    task.testsPassed = obj.get("score").getAsInt()/10;
                    task.completed = obj.get("completed").getAsBoolean();
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
                            String startPath = "task/"+name;
                            String startName = name.replaceAll("[.]java", "");
                            javaUtil.changePackageName(startPath, "package com.kxw959.programmingapplication.tasks;");
                            task.start = new Pair<>(startName, startPath);
                        }
                        if(type.equals("instructions")){
                            String instructionsName = name.replaceAll("[.]txt", "");
                            instructionsName = instructionsName.replaceAll("[.]pdf", "");
                            String instructionsPath = "task/"+name;
                            task.instructions = new Pair<>(instructionsName, instructionsPath);
                        }
                        if(type.equals("test")){
                            String testName = name.replaceAll("[.]java", "");
                            String testPath = "task/"+name;
                            task.test = new Pair<>(testName, testPath);
                        }
                        if(type.equals("quiz")){
                            isQuiz = true;
                            String quizPath = "task/"+name;
                            String quizName = name.replaceAll("[.]csv", "");
                            task.quiz = new Pair<>(quizName, quizPath);
                        }
                    }
                    User.taskList.add(task);
                    addTask(task.taskID, task.completed, isQuiz);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(User.taskList!=null){
            for(Task task : User.taskList){
                if(task.quiz==null){
                    javaUtil.updateImports(task.test.getValue(), "import com.kxw959.programmingapplication.tasks."+task.start.getKey(), task.start.getKey());
                    task.junitVersion = javaUtil.changePackageName(task.test.getValue(), "package com.kxw959.programmingapplication.tasks;");
                }
            }
        }
        //taskList.getChildren().add(iv);
    }

    private void addTask(String name, boolean completed, boolean isQuiz){
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
        if(isQuiz) {
            taskBtn.setOnAction(event -> {
                selectedTask = taskBtn.getText();
                try {
                    onClickQuiz();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        else{
            taskBtn.setOnAction(event -> {
                selectedTask = taskBtn.getText();
                onCLickEditor();
            });
        }
        if(completed) {
            taskBtn.setStyle("-fx-text-fill: green");
            ImageView iv = new ImageView(badge);
            iv.setFitWidth(60);
            iv.setFitHeight(60);
            iv.setPickOnBounds(true);
            iv.setPreserveRatio(true);
            iv.setCache(true);
            taskBtn.setGraphic(iv);
            taskBtn.setContentDisplay(ContentDisplay.BOTTOM);
        }
        hbox.getChildren().add(taskBtn);
    }

    private void initLeaderboard(String className){
        try {
            List<JsonObject> students = NetworkManager.getClassData(className);
            List<Pair<String, Integer>> orderedScore = new ArrayList<>();
            System.out.println(students);
            for(JsonObject s : students){
                String name  = s.get("name").getAsString();
                Integer score = addUpScores(s);
                orderedScore.add(new Pair<>(name, score));
            }
            orderedScore.sort(Collections.reverseOrder(Comparator.comparing(Pair::getValue)));
            int place =  1;
            for(Pair <String, Integer> p : orderedScore){
                GridPane gp = new GridPane();
                ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
                column1.setHgrow(Priority.ALWAYS);
                column1.setHalignment(HPos.LEFT);
                ColumnConstraints column2 = new ColumnConstraints(100);
                column2.setHalignment(HPos.LEFT);
                ColumnConstraints column3 = new ColumnConstraints(100);
                column3.setHalignment(HPos.LEFT);
                gp.getColumnConstraints().addAll(column1, column2, column3); // first column gets any extra width
                Label name = new Label(p.getKey());
                name.setFont(Font.font(30));
                Label score = new Label(Integer.toString(p.getValue()));
                score.setFont(Font.font(30));
                gp.add(name, 0, 0);
                gp.add(score, 1, 0);
                if(place  < 4){
                    Image image;
                    switch (place){
                        case 1:
                            image = first;
                            break;
                        case 2:
                            image = second;
                            break;
                        case 3:
                            image = third;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + place);
                    }
                    ImageView iv  = new ImageView(image);
                    iv.setFitWidth(40);
                    iv.setFitHeight(40);
                    iv.setPickOnBounds(true);
                    iv.setPreserveRatio(true);
                    iv.setCache(true);
                    gp.add(iv, 2,  0);
                }
                leaderboard.getChildren().add(gp);
                place++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int addUpScores(JsonObject student){
        try{
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
        catch (Exception e){
            return 0;
        }
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

    @FXML
    private void onClickQuiz() throws IOException {
        CSVUtil csvUtil = new CSVUtil();
        for(Task t : User.taskList){
            if(Objects.equals(t.taskID, selectedTask)){
                User.selectedTask = User.taskList.get(User.taskList.indexOf(t));
            }
        }
        User.questions = csvUtil.getTextFromCSV(User.selectedTask.quiz.getValue());
        SceneManager.switchScene("quiz-page.fxml");
    }
}
