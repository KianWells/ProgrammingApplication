package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class UploadTasksController {

    public Pane namePane;
    public TextField nameField;
    public Pane typePane;
    public Label infoLabel;
    public ListView<CheckBox> taskList;
    public Label errorLabel;
    public Pane classPane;
    public ListView<CheckBox> classList;
    public Pane filePane;

    private List<String> selectedFiles = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private String taskName;
    private int taskType;

    @FXML
    public void initialize(){

    }

    public void onClickConfirmName(ActionEvent actionEvent) {
        taskName = nameField.getText();
        namePane.setVisible(false);
        typePane.setVisible(true);
    }

    public void onClickConfirmQuiz(ActionEvent actionEvent) {
        taskType = 0;
        typePane.setVisible(false);
        filePane.setVisible(true);
    }

    public void onClickConfirmProgrammingTask(ActionEvent actionEvent) {
        taskType = 1;
        typePane.setVisible(false);
        filePane.setVisible(true);
    }

    public void onClickConfirmBoth(ActionEvent actionEvent) {
        taskType = 2;
        typePane.setVisible(false);
        filePane.setVisible(true);
    }

    public void onClickBrowse(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a folder");
        switch (taskType) {
            case 0 -> fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv type", "*.csv"));
            case 1 -> fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".java type and .pdf", "*.java", "*.pdf"));
            case 2 -> fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".java type, .pdf, .csv", "*.java", "*.pdf", "*.csv"));
        }
        List<File> tempFiles = fc.showOpenMultipleDialog(SceneManager.stage);
        if(tempFiles != null && !tempFiles.isEmpty()){
            for(File f : tempFiles){
                if(!files.contains(f)) {
                    files.add(f);
                    CheckBox cb = new CheckBox();
                    cb.setText(f.getName());
                    cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if (newValue) {
                                selectedFiles.add(cb.getText());
                            } else {
                                selectedFiles.removeIf(s -> s.equals(cb.getText()));
                            }
                        }
                    });
                    taskList.getItems().add(cb);
                }
            }
        }
    }

    public void onClickDelete(ActionEvent actionEvent) {
        files.removeIf(f -> selectedFiles.contains(f.getName()));
        taskList.getItems().removeIf(i -> selectedFiles.contains(i.getText()));
        selectedFiles.clear();

        System.out.println(files);
    }

    public void onClickContinue(ActionEvent actionEvent) {
        filePane.setVisible(false);
        classPane.setVisible(true);
        initClasses();
    }

    private void initClasses(){
        try {
            JsonElement jsonElement = NetworkManager.getJSONElementFromURL(new URL(NetworkManager.TEACHER+User.username), "classes");
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i<jsonArray.size(); i++){
                CheckBox cb = new CheckBox();
                cb.setText(jsonArray.get(i).getAsString().replaceAll("_", "__"));
                classList.getItems().add(cb);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClickFinish(ActionEvent actionEvent) throws IOException {
        NetworkManager.uploadFiles(files);
        for(CheckBox cb: classList.getItems()){
            if(cb.isSelected()){
                NetworkManager.addTaskToClass(cb.getText(), taskName, files);
            }
        }
        SceneManager.switchScene("teacher-homepage.fxml");
    }
}
