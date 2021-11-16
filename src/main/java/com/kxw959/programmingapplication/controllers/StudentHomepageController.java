package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class StudentHomepageController {
    @FXML
    public VBox taskList;
    int numCards = 2;

    @FXML
    void initialize(){
        try {
            JsonObject student = NetworkManager.getJSONObjectFromURL(User.url);
            assert student != null;
            JsonArray tasks = (JsonArray) student.get("tasks");
            for(JsonElement j : tasks){
                JsonObject obj = (JsonObject) j;
                addTask(obj.get("taskID").getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0; i<19; i++){
            addTask("New Task "+i);
        }
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

    @FXML
    void onCLickEditor(){
        SceneManager.switchScene("code-editor.fxml");
    }
}
