package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;

public class RegisterController {
    @FXML
    void onClickRegister(){
        SceneManager.switchScene("main-menu.fxml");
    }
}
