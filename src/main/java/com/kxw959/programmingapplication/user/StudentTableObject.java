package com.kxw959.programmingapplication.user;

import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.util.Pair;
import sun.security.jgss.spnego.NegTokenInit;

public class StudentTableObject {

    private String taskName;
    private String testsPassed;
    private Button seeCode;

    public StudentTableObject(String taskName, String testsPassed, String fileName, String username){
        this.taskName = taskName;
        this.testsPassed = testsPassed;
        Button seeCode = new Button("See Code");
        seeCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(NetworkManager.getFile(username+"/jpa2021_"+fileName, fileName)){
                    User.selectedTask = new Task();
                    User.selectedTask.start = new Pair<>(fileName.replaceAll("[.]java", ""), "src/main/java/com/kxw959/programmingapplication/tasks/"+fileName);
                    SceneManager.switchScene("code-editor.fxml");
                }
                else{
                    System.out.println("Clicked but not found");
                }
            }
        });
        this.seeCode = seeCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTestsPassed() {
        return testsPassed;
    }

    public void setTestsPassed(String testsPassed) {
        this.testsPassed = testsPassed;
    }

    public Button getSeeCode() {
        return seeCode;
    }

    public void setSeeCode(Button seeCode) {
        this.seeCode = seeCode;
    }
}
