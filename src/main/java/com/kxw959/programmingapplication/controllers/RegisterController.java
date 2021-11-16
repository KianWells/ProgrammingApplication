package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.net.URL;

public class RegisterController {
    @FXML
    Label responseText;
    @FXML
    TextInputControl email;

    @FXML
    TextInputControl username;

    @FXML
    TextInputControl password;

    @FXML
    void onClickRegister() throws IOException {
        if(NetworkManager.registerTeacher(email.getText(), username.getText(), password.getText()) == 1){
            User.username = username.getText();
            User.url = new URL(NetworkManager.TEACHER+username.getText());
            User.isTeacher = true;
            SceneManager.switchScene("teacher-homepage.fxml");
        }
        else{
            SceneManager.switchScene("main-menu.fxml");
        }
    }
}
