package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputControl;

import java.io.IOException;

public class RegisterController {
    @FXML
    TextInputControl email;

    @FXML
    TextInputControl username;

    @FXML
    TextInputControl password;

    @FXML
    void onClickRegister() throws IOException {
        NetworkManager.registerTeacher(email.getText(), username.getText(), password.getText());
    }
}
