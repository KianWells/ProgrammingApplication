package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class QuizController {
    public Tab tab1;
    public Label questionLabel;
    public RadioButton answer1;
    public RadioButton answer2;
    public RadioButton answer3;
    public RadioButton answer4;

    String questionText = "";

    @FXML
    public void initialize(){
        if(User.isTeacher){
            SceneManager.currentScene.getRoot().requestFocus();
        }
    }

    @FXML
    public void getChar(KeyEvent event){
        if(event.getCode() == KeyCode.BACK_SPACE){
            questionText = questionText.substring(0, questionText.length()-1);
        }
        questionText += event.getText();
        questionLabel.setText(questionText);
    }

    @FXML
    public void onClickSubmit(ActionEvent event) {
    }
}
