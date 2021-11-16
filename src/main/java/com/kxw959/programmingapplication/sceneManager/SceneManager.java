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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class SceneManager {
    public static Stage stage;
    static Scene currentScene;
    static double sceneWidth;
    static double sceneHeight;

    public static void initialise(Stage _stage, Scene _scene){
        stage = _stage;
        currentScene = _scene;
    }

    public static void switchScene(String url){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(url));
        try{
            currentScene = new Scene(fxmlLoader.load());
            currentScene.getStylesheets().add(HelloApplication.class.getResource("DarkTheme.css").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Hello!");
        stage.setScene(currentScene);
        stage.sizeToScene();
        stage.show();
        sceneHeight = stage.getHeight();
        sceneWidth = stage.getWidth();
    }

    public static void exit(){
        stage.close();
    }
}
