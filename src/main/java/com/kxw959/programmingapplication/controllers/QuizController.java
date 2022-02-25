package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.HelloApplication;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.QuizTab;
import com.kxw959.programmingapplication.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.List;

public class QuizController {
    private Label label;
    public TabPane quizPane;
    public int finalScore = 0;
    public int totalQuestions = 0;
    List<String[]> questions;

    @FXML
    public void initialize() throws IOException {
        if(User.questions != null){
            questions = User.questions;
            totalQuestions = questions.size();
            int i=1;
            for(String[] question : questions){
                quizPane.getTabs().add(new QuizTab(question, i));
                i++;
            }
        }
        Tab control = new Tab("Finish");
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setPrefHeight(200.0);
        content.setPrefWidth(100.0);
        content.setSpacing(100.0);
        label = new Label();
        label.setWrapText(true);
        Button finish = new Button("Finish Quiz");
        finish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onClickFinish(event);
            }
        });
        content.getChildren().add(finish);
        content.getChildren().add(label);
        control.setContent(content);
        quizPane.getTabs().add(control);
    }

    public void onClickFinish(ActionEvent e){
        finalScore = 0;
        for(int i=0; i<quizPane.getTabs().size()-1; i++){
            QuizTab qt = (QuizTab) quizPane.getTabs().get(i);
            if(qt.completed){
                finalScore++;
            }
        }
        label.setText("Your final score is "+ finalScore+"/"+totalQuestions);
    }
}
