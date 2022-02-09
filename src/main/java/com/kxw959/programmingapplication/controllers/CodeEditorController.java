package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.CompilerUtil;
import com.kxw959.programmingapplication.utils.JUNITRunner;
import com.kxw959.programmingapplication.utils.PDFUtil;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import jeliot.Jeliot;
import jeliot.gui.JeliotWindow;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CodeEditorController {

    @FXML
    public BorderPane jeliotWindow;
    @FXML
    public TextArea instructionArea;
    @FXML
    public Button testButton;
    @FXML
    public Label junitOutput;

    private JeliotWindow gui;
    public TextArea output = new TextArea();
    public Thread outputThread;
    JTextArea outputConsole = new JTextArea();

    @FXML
    void initialize() throws IOException {
        if(User.selectedTask.instructions != null){
            if(User.selectedTask.instructions.getValue().endsWith(".txt")){
                String content = new String ( Files.readAllBytes( Paths.get(User.selectedTask.instructions.getValue()) ) );
                instructionArea.setText(content);
            }
            else{
                PDFUtil util = new PDFUtil();
                util.loadPDFAsText(instructionArea, User.selectedTask.instructions.getValue());
            }
        }

        Jeliot jeliot = Jeliot.start(new String[0]);

        jeliot.getGUI().getFrame().dispose();

        gui = jeliot.getGUI();
        gui.setProgram(new File(User.selectedTask.start.getValue()));

        SwingNode codePane = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                codePane.setContent(gui.getCodeNest());
            }
        });
        jeliotWindow.setCenter(codePane);

        SwingNode conPan = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                conPan.setContent(gui.getConPan());
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outputConsole = gui.getOutputConsole();
            }
        });
        final String[] ocText = {outputConsole.getText()};

        outputThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            if(!Objects.equals(ocText[0], outputConsole.getText())){
                                output.setText(outputConsole.getText());
                                ocText[0] =output.getText();
                            }
                        }
                    }
                }
        );
        HBox bottom = new HBox();
        output.setMaxWidth(1000);
        output.setPrefWidth(1000);
        bottom.getChildren().add(conPan);
        bottom.getChildren().add(output);
        jeliotWindow.setBottom(bottom);
        outputThread.start();
    }

    @FXML
    public void onCLickTest(ActionEvent actionEvent) throws IOException {
        if(User.selectedTask.test != null){
            outputThread.interrupt();
            gui.getEditor().saveProgram();
            File classDir = new File("classes/com");
            if(classDir.exists()){
                System.out.println(classDir.delete());
            }
            try {
                CompilerUtil compilerUtil = new CompilerUtil();
                compilerUtil.setSourceDir(new File("src/main/java/com/kxw959/programmingapplication/tasks"));
                compilerUtil.setClassesDir(new File("classes/"));

                try {
                    compilerUtil.compile(false);
                    JUNITRunner runner = new JUNITRunner();
                    Pair<Integer, String> result = runner.runJunit(User.selectedTask.test.getKey(), compilerUtil.loadClassesFromCompiledDirectory());
                    output.setText(result.getValue()+
                            "\nTests Passed: "+
                            result.getKey() +
                            " out of " +
                            User.selectedTask.totalTests);
                    User.selectedTask.testsPassed = result.getKey();
                    NetworkManager.increaseScore(User.username, User.selectedTask.testsPassed * 10, User.selectedTask.taskID);
                    List<File> fileToUpload = new ArrayList<>();
                    fileToUpload.add(new File(User.selectedTask.start.getValue()));
                    NetworkManager.uploadFiles(fileToUpload, User.username);
                }catch (Exception e){
                    output.setText(e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<File> fileToUpload = new ArrayList<>();
            fileToUpload.add(new File(User.selectedTask.start.getValue()));
            NetworkManager.uploadFiles(fileToUpload, User.username);
        }
        else{
            System.out.println("Could not find a test for this");
        }
    }

    public void onClickBack(ActionEvent event) {
        if(!User.isTeacher) {
            gui.getEditor().saveProgram();
            SceneManager.switchScene("student-homepage.fxml");
        }
        else SceneManager.switchScene("see-progress.fxml");
    }
}
