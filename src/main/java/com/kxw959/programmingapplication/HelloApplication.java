package com.kxw959.programmingapplication;

import com.itextpdf.text.DocumentException;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.JAVAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.nio.ch.Net;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
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

        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("java.class.path").contains("junit"));

        NetworkManager.getClassData("Class A");
    }

    public static void main(String[] args) {
        launch();
    }
}