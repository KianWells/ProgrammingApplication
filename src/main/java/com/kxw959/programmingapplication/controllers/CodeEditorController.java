package com.kxw959.programmingapplication.controllers;

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

import javax.swing.*;
import java.io.File;
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

    public TextArea output = new TextArea();
    public Thread outputThread;
    JTextArea outputConsole = new JTextArea();

    @FXML
    void initialize(){
        PDFUtil util = new PDFUtil();
        util.loadPDFAsText(instructionArea, User.selectedTask.instructions.getValue());

        Jeliot jeliot = Jeliot.start(new String[0]);

        jeliot.getGUI().getFrame().dispose();

        JeliotWindow gui = jeliot.getGUI();
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
    public void onCLickTest(ActionEvent actionEvent) {
        outputThread.interrupt();
        File classDir = new File("classes/com/");
        if(classDir.exists()){
            System.out.println(classDir.delete());
        }
        try {
            CompilerUtil compilerUtil = new CompilerUtil();
            compilerUtil.setSourceDir(new File("src/main/java/com/kxw959/programmingapplication/tasks"));
            compilerUtil.setClassesDir(new File("classes/"));

            try {
                compilerUtil.compile();
                JUNITRunner runner = new JUNITRunner();
                Pair<Integer, String> result = runner.runJunit(User.selectedTask.test.getKey(), compilerUtil.loadClassesFromCompiledDirectory());
                output.setText(result.getValue());
                if(result.getKey() > 0){

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
