package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainMenuController {
    @FXML
    private Label label;

    @FXML
    void onClickLogin(){SceneManager.switchScene("login-page.fxml");}

    @FXML
    void onClickRegister(){
        SceneManager.switchScene("quiz-page.fxml");
    }

    @FXML
    void onClickOptions() {
        SceneManager.exit();
    }
}
