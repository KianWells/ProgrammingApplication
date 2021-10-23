package com.kxw959.programmingapplication.sceneManager;

import com.kxw959.programmingapplication.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    static Stage stage;
    static Scene currentScene;

    public static void initialise(Stage _stage, Scene _scene){
        stage = _stage;
        currentScene = _scene;
    }

    public static void switchScene(String url){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(url));
        try{
            currentScene = new Scene(fxmlLoader.load(), 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Hello!");
        stage.setScene(currentScene);
        stage.show();
    }

    public static void exit(){
        stage.close();
    }
}
