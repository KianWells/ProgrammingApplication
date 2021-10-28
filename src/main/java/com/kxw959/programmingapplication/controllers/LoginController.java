package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.User;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    TextInputControl usernameField;

    @FXML
    TextInputControl passwordField;

    @FXML
    Text responseText;

    @FXML
    void onClickLogin() throws IOException {
        //get username from database
        //if it matches username in the textfield then let them in
        switch(NetworkManager.checkLogin(usernameField.getText(), passwordField.getText())){
            case -1:
                SceneManager.switchScene("register-page.fxml");
                break;
            case 0:
                responseText.setText("Password Incorrect");
                break;
            case 1:
                SceneManager.switchScene("student-homepage.fxml");
                break;
            case 2:
                SceneManager.switchScene("teacher-homepage.fxml");
                User.isTeacher = true;
                User.username = usernameField.getText();
                User.url = new URL(NetworkManager.TEACHER+usernameField.getText());
                break;
        }
    }
}
