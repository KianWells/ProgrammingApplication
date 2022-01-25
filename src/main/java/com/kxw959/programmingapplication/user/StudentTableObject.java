package com.kxw959.programmingapplication.user;

import javafx.scene.control.Button;

public class StudentTableObject {

    private String taskName;
    private String testsPassed;
    private Button seeCode;

    public StudentTableObject(String taskName, String testsPassed){
        this.taskName = taskName;
        this.testsPassed = testsPassed;
        this.seeCode = new Button("See Code");
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
