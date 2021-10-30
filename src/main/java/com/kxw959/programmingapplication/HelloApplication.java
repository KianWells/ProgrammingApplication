package com.kxw959.programmingapplication;

import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        SceneManager.initialise(stage, scene);
        NetworkManager.getStudentsInClass("Class_B");
    }

    public static void main(String[] args) {
        launch();
    }
}