package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.utils.JAVAUtil;
import com.kxw959.programmingapplication.utils.PDFUtil;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    JTextArea outputConsole = new JTextArea();

    @FXML
    void initialize(){
        PDFUtil util = new PDFUtil();
        util.loadPDFAsText(instructionArea, "jpa2021_Example_instructions.pdf");

        Jeliot jeliot = Jeliot.start(new String[0]);

        jeliot.getGUI().getFrame().dispose();

        JeliotWindow gui = jeliot.getGUI();
        gui.setProgram(new File("jpa2021_Example_start.java"));

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
}
