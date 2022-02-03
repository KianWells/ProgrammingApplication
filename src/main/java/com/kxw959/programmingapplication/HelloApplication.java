package com.kxw959.programmingapplication;

import com.itextpdf.text.DocumentException;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
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

        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("java.class.path").contains("junit"));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //delete files in tasks folder
        File tasks = new File("src/main/java/com/kxw959/programmingapplication/tasks");
        if(tasks.isDirectory()){
            File[] files = tasks.listFiles();
            if(files != null && files.length != 0){
                for(File f : files){
                    if(!f.getName().equals("Empty.java")){
                        f.delete();
                    }
                }
            }
        }
        else System.out.println("Doesnt exist");

        File classDir = new File("classes/com");
        if(classDir.exists()){
            System.out.println(classDir.delete());
        }

        if(User.uploadFiles != null && !User.uploadFiles.isEmpty()){
            for(File f : User.uploadFiles){
                f.delete();
            }
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}