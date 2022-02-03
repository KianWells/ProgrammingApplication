package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.JAVAUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import jeliot.Jeliot;
import jeliot.gui.JeliotWindow;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public MenuItem registerTest;
    public MenuItem registerStart;
    public MenuItem registerInstruction;
    public MenuItem registerQuiz;
    public MenuButton registerAs;
    public Pane createOrUploadPane;
    public TextArea instructionArea;
    public BorderPane codeArea;
    public BorderPane testArea;
    public SplitPane createPane;

    private List<String> selectedFiles = new ArrayList<>();
    private List<File> files = new ArrayList<>();
    private Map<String, String> taskMap = new HashMap<>();
    private String taskName;
    private int taskType;

    Jeliot jeliot1,jeliot2;

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
        createOrUploadPane.setVisible(true);
    }

    public void onClickConfirmProgrammingTask(ActionEvent actionEvent) {
        taskType = 1;
        typePane.setVisible(false);
        createOrUploadPane.setVisible(true);
    }

    public void onClickConfirmBoth(ActionEvent actionEvent) {
        taskType = 2;
        typePane.setVisible(false);
        createOrUploadPane.setVisible(true);
    }

    public void onClickBrowse(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a folder");
        switch (taskType) {
            case 0:
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv type", "*.csv"));
            case 1:
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".java type, .pdf and .txt", "*.java", "*.pdf", "*.txt"));
            case 2:
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".java type, .pdf, .csv, .txt", "*.java", "*.pdf", "*.csv", "*.txt"));
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
        System.out.println(taskMap.toString());
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
                NetworkManager.addTaskToClass(cb.getText(), taskName, taskMap);
            }
        }
        SceneManager.switchScene("teacher-homepage.fxml");
    }

    @FXML
    public void onClickRegisterAs(ActionEvent actionEvent) {
        MenuItem item = (MenuItem) actionEvent.getSource();
        String key = "";
        switch (item.getId()){
            case "registerTest":
                key = "test";
                break;
            case "registerStart":
                key = "start";
                break;
            case "registerInstruction":
                key = "instructions";
                break;
            case "registerQuiz":
                key = "quiz";
                break;
        }
        if(selectedFiles.size()>1){
            selectedFiles.clear();
            errorLabel.setText("Cannot register more than 1 file as "+key);
        }
        else if(selectedFiles.size() == 0){
            errorLabel.setText("Select a file to register as "+key);
        }
        else{
            String fileName = selectedFiles.get(0);
            taskMap.put(key, fileName);
            infoLabel.setText(fileName+" selected as the "+key);
            selectedFiles.clear();
        }
    }

    public void onClickCreateTask(ActionEvent actionEvent) {
        createPane.setVisible(true);
        createOrUploadPane.setVisible(false);
        startCreate();
    }

    public void onClickUploadTask(ActionEvent actionEvent) {
        createOrUploadPane.setVisible(false);
        filePane.setVisible(true);
    }

    public void onClickSave(ActionEvent actionEvent) {
        saveAll();
    }

    public void onClickUpload(ActionEvent actionEvent) {
        createPane.setVisible(false);
        uploadAll(saveAll());

        filePane.setVisible(true);
    }

    void startCreate(){
        JAVAUtil javaUtil = new JAVAUtil();
        jeliot1 = Jeliot.start(new String[0]);

        jeliot1.getGUI().getFrame().dispose();

        JeliotWindow gui = jeliot1.getGUI();
        gui.setProgram(new File("src/main/java/com/kxw959/programmingapplication/examples/Example.java"));

        SwingNode codePane = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                codePane.setContent((JComponent) gui.getCodeNest().getLeftComponent());
            }
        });
        codeArea.setCenter(codePane);

        jeliot2 = Jeliot.start(new String[0]);
        jeliot2.getGUI().getFrame().dispose();
        JeliotWindow gui2 = jeliot2.getGUI();
        gui2.setProgram(new File("src/main/java/com/kxw959/programmingapplication/examples/ExampleTest.java"));
        SwingNode codePane2 = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                codePane2.setContent((JComponent) gui2.getCodeNest().getLeftComponent());
            }
        });
        testArea.setCenter(codePane2);
    }

    List<File> saveAll(){
        String instructions = instructionArea.getText();
        String test = jeliot2.getGUI().getProgram();
        String start = jeliot1.getGUI().getProgram();

        try {
            File instructionsFile = new File(taskName+"Instructions.txt");
            File testFile;
            File startFile;

            Files.write(Paths.get(instructionsFile.getName()), instructions.getBytes(StandardCharsets.UTF_8));

            JAVAUtil util = new JAVAUtil();

            testFile = util.createJavaFile(test);
            startFile = util.createJavaFile(start);

            return Arrays.asList(instructionsFile, testFile, startFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    void uploadAll(List<File> files){
        taskMap.put("instructions", files.get(0).getName());
        taskMap.put("test", files.get(1).getName());
        taskMap.put("start", files.get(2).getName());

        this.files = files;
        User.uploadFiles = files;

        for(File f : files){
            CheckBox cb = new CheckBox();
            cb.setText(f.getName());
            taskList.getItems().add(cb);
        }
    }

    public void onClickBack(ActionEvent event) {
        SceneManager.switchScene("teacher-homepage.fxml");
    }
}
