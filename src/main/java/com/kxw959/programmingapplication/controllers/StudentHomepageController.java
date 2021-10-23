package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;

public class StudentHomepageController {
    @FXML
    void onCLickEditor(){
        SceneManager.switchScene("code-editor.fxml");
    }
}
