package com.kxw959.programmingapplication;

import com.itextpdf.text.DocumentException;
import com.kxw959.programmingapplication.JUNITSTUFF.jpa2021_Example_test;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException, DocumentException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        scene.getStylesheets().add(HelloApplication.class.getResource("DarkTheme.css").toString());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        SceneManager.initialise(stage, scene);
    }

    public static void main(String[] args) {
        launch();
    }
}