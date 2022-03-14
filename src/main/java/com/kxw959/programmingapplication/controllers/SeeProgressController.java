package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.StudentTableObject;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.CopyUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import sun.nio.ch.Net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeeProgressController {
    public MenuButton classMenu;
    public Accordion studentAccordian;
    private List<String> classes = new ArrayList<>();
    private List<Pair<String, ObservableList<StudentTableObject>>> studentsInClass = new ArrayList<>();

    @FXML
    public void initialize(){
        //get the first class and retreive all students in the class
        //get the students tasks and scores and the URL for their code
        classes = getClasses(User.username);
        for(String c : classes){
            MenuItem m = new MenuItem(c);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadClasses(event);
                }
            });
            classMenu.getItems().add(m);
        }
    }

    private void addStudents(){
        for(Pair<String, ObservableList<StudentTableObject>> student : studentsInClass){
            TableView<StudentTableObject> student1 = new TableView<>();

            TableColumn<StudentTableObject, String> nameCol = new TableColumn<>("Task Name");
            nameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));

            TableColumn<StudentTableObject, String> testsPassedCol = new TableColumn<>("Tests Passed");
            testsPassedCol.setCellValueFactory(new PropertyValueFactory<>("testsPassed"));

            TableColumn<StudentTableObject, Button> seeCodeCol = new TableColumn<>("See Code");
            seeCodeCol.setCellValueFactory(new PropertyValueFactory<>("seeCode"));

            student1.setItems(student.getValue());
            student1.getColumns().addAll(nameCol, testsPassedCol, seeCodeCol);
            TitledPane newStudent = new TitledPane();
            newStudent.setMaxHeight(Double.MAX_VALUE);
            newStudent.setMinHeight(300);
            newStudent.setText(student.getKey());
            newStudent.setContent(student1);

            studentAccordian.getPanes().add(newStudent);
        }
    }

    private List<String> getClasses(String username) {
        JsonObject teacher = null;
        try {
            teacher = NetworkManager.getJSONObjectFromURL(new URL(NetworkManager.TEACHER+username));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonArray classesJSON = teacher.getAsJsonArray("classNames");
        List<String> classes = new ArrayList<>();
        for(JsonElement j : classesJSON){
            classes.add(j.getAsString());
        }
        return classes;
    }

    private void loadClasses(ActionEvent event){
        MenuItem m = (MenuItem) event.getSource();
        try {
            List<JsonObject> students = NetworkManager.getClassData(m.getText());
            for(JsonObject j : students){
                String title = j.get("username").getAsString() + " - " + addUpScores(j) + " pts";
                ObservableList<StudentTableObject> objects = FXCollections.observableArrayList();
                for(JsonElement t : j.get("tasks").getAsJsonArray()){
                    JsonObject task = (JsonObject) t;
                    String taskName = task.get("taskID").getAsString();
                    int testsPassed = task.get("score").getAsInt()/10;
                    int totalTests = task.get("totalTests").getAsInt();
                    String value = testsPassed + " / " + totalTests;
                    JsonArray fileNames = task.getAsJsonArray("fileNames");
                    String fileName = "";
                    for(JsonElement e : fileNames){
                        JsonObject file = (JsonObject) e;
                        if(file.get("type").getAsString().equals("start")){
                            fileName = file.get("fileName").getAsString();
                        }
                    }
                    objects.add(new StudentTableObject(taskName, value, fileName,j.get("username").getAsString()));
                }
                studentsInClass.add(new Pair<>(title, objects));
            }
            addStudents();
            studentsInClass.clear();
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
    public void onClickBack(ActionEvent actionEvent) {
        SceneManager.switchScene("teacher-homepage.fxml");
    }
}
