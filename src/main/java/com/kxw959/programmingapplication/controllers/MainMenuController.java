package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController {
    @FXML
    private Label label;

    @FXML
    void onClickStudentLogin(){
        SceneManager.switchScene("login-page.fxml");
    }

    @FXML
    void onClickTeacherLogin(){
        SceneManager.switchScene("login-page.fxml");
    }

    @FXML
    void onClickRegisterTeacher(){
        SceneManager.switchScene("register-page.fxml");
    }

    @FXML
    void onClickExit() {
        SceneManager.exit();
    }
}
