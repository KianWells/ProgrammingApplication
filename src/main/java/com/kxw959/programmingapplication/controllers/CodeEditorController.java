package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.HelloApplication;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.CompilerUtil;
import com.kxw959.programmingapplication.utils.PDFUtil;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import jeliot.Jeliot;
import jeliot.gui.JeliotWindow;
import org.codehaus.janino.JavaSourceClassLoader;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;

public class CodeEditorController {

    @FXML
    public BorderPane jeliotWindow;
    @FXML
    public TextArea instructionArea;
    @FXML
    public Button testButton;
    JTextArea outputConsole = new JTextArea();

    @FXML
    void initialize(){
        PDFUtil util = new PDFUtil();
        util.loadPDFAsText(instructionArea, User.instructionsPath);

        Jeliot jeliot = Jeliot.start(new String[0]);

        jeliot.getGUI().getFrame().dispose();

        JeliotWindow gui = jeliot.getGUI();
        gui.setProgram(new File(User.startPath));

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

        TextArea ocfx = new TextArea();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                outputConsole = gui.getOutputConsole();
            }
        });
        final String[] ocText = {outputConsole.getText()};

        Thread outputThread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            if(!Objects.equals(ocText[0], outputConsole.getText())){
                                ocfx.setText(outputConsole.getText());
                                ocText[0] =ocfx.getText();
                            }
                        }
                    }
                }
        );
        HBox bottom = new HBox();
        ocfx.setMaxWidth(1000);
        ocfx.setPrefWidth(1000);
        bottom.getChildren().add(conPan);
        bottom.getChildren().add(ocfx);
        jeliotWindow.setBottom(bottom);
        outputThread.start();
/*
        SwingNode theatre = new SwingNode();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                theatre.setContent(jeliot.getGUI().getTheater());
            }
        });
        jeliotTheatre.getChildren().add(theatre);*/
    }

    @FXML
    public void onCLickTest(ActionEvent actionEvent) {
        try {
            CompilerUtil compilerUtil = new CompilerUtil();
            compilerUtil.setSourceDir(new File("src/main/java/com/kxw959/programmingapplication/tasks"));
            compilerUtil.setClassesDir(new File("target/classes/"));

            try {
                compilerUtil.compile();
            }catch (Exception e){
                e.printStackTrace();
            }
            //load class from url
            ClassLoader loader = HelloApplication.class.getClassLoader();
            Class<?> testClass = Class.forName("com.kxw959.programmingapplication.tasks.Example_test", true, loader);
            JUnitCore junit = new JUnitCore();
            junit.addListener(new TextListener(System.out));
            junit.run(testClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
