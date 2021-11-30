package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.utils.JAVAUtil;
import com.kxw959.programmingapplication.utils.PDFUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;

public class CodeEditorController {
    @FXML
    public WebView instructionView;
    @FXML
    public TextArea codeArea;

    @FXML
    void initialize(){
        PDFUtil util = new PDFUtil();
        util.loadPDF(instructionView, "jpa2021_Example_instructions.pdf");

        JAVAUtil jUtil = new JAVAUtil();
        jUtil.loadJavaFile(codeArea, "jpa2021_Example_start.java");
    }
}
