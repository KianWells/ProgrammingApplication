package com.kxw959.programmingapplication;

import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        scene.getStylesheets().add(HelloApplication.class.getResource("DarkTheme.css").toString());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        SceneManager.initialise(stage, scene);
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            System.out.println(url.getFile());
        }

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

        File examples = new File("src/main/java/com/kxw959/programmingapplication/examples");
        if(examples.isDirectory()){
            File[] files = examples.listFiles();
            if(files != null && files.length != 0){
                for(File f : files){
                    if(!f.getName().equals("Example.java") && !f.getName().equals("ExampleTest.java")){
                        f.delete();
                    }
                }
            }
        }
        else System.out.println("Doesnt exist");

        File classDir = new File("classes/com");
        if(classDir.exists()){
            FileUtils.deleteDirectory(classDir);
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