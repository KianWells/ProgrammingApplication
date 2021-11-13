package com.kxw959.programmingapplication.sceneManager;

import com.kxw959.programmingapplication.HelloApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    public static Stage stage;
    static Scene currentScene;

    public static void initialise(Stage _stage, Scene _scene){
        stage = _stage;
        currentScene = _scene;
    }

    public static void switchScene(String url){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(url));
        try{
            currentScene = new Scene(fxmlLoader.load(), 400, 400);
            currentScene.getStylesheets().add(HelloApplication.class.getResource("DarkTheme.css").toString());
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
