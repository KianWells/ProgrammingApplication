package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    Label responseText;

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
                User.isTeacher = false;
                User.username = usernameField.getText();
                User.url = new URL(NetworkManager.STUDENT+usernameField.getText());
                SceneManager.switchScene("student-homepage.fxml");
                break;
            case 2:
                User.isTeacher = true;
                User.username = usernameField.getText();
                User.url = new URL(NetworkManager.TEACHER+usernameField.getText());
                SceneManager.switchScene("teacher-home page.fxml");
                break;
        }
    }

    public void onClickBack(ActionEvent event) {
        SceneManager.switchScene("main-menu.fxml");
    }
}
