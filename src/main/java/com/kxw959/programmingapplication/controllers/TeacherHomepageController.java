package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TeacherHomepageController {
    @FXML
    void onClickTask(){
        SceneManager.switchScene("upload-task.fxml");
    }

    @FXML
    void onClickProgress(){
        SceneManager.switchScene("see-progress.fxml");
    }

    @FXML
    void onClickRegister(){
        SceneManager.switchScene("register-students.fxml");
    }

    public void onClickLogOut(ActionEvent event) {
        SceneManager.switchScene("main-menu.fxml");
    }
}
