package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;

public class LoginController {
    @FXML
    TextInputControl usernameField;

    @FXML
    void onClickLogin(){
        if (usernameField.getText().equals("student")){
            SceneManager.switchScene("student-homepage.fxml");
        }
        if(usernameField.getText().equals("teacher")){
            SceneManager.switchScene("teacher-homepage.fxml");
        }
    }
}
