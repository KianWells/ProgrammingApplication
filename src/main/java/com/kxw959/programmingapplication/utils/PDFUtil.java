package com.kxw959.programmingapplication.utils;

import com.google.gson.JsonObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kxw959.programmingapplication.HelloApplication;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PDFUtil {
    public PDFUtil(){

    }

    public void makePDFFromListJSON(List<JsonObject> jsonList, String fileName) {
        Document document = new Document();
        try{
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        document.open();
        PdfPTable table = new PdfPTable(jsonList.get(0).size());
        String[] headers = new String[] {"Name", "Username", "Password"};
        for(String headerString : headers){
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setPhrase(new Phrase(headerString));
            table.addCell(header);
        }
        for(JsonObject student : jsonList){
            table.addCell(student.get("name").getAsString());
            table.addCell(student.get("username").getAsString());
            table.addCell(student.get("password").getAsString());
        }
        try{
            document.add(table);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        document.close();
    }

    public void loadPDFAsWeb(WebView view, String fileName){
        convertPDFtoHTML(fileName);
        WebEngine engine = view.getEngine();
        System.out.println(fileName);
        File file = new File(fileName+".html");
        engine.load(file.toURI().toString());
    }

    public void loadPDFAsText(TextArea area, String fileName){
        area.setWrapText(true);
        area.setWrapText(true);
        area.setText(convertPDFtoText(fileName));
    }

    private void convertPDFtoHTML(String fileName){
        try {
            PDDocument pdf = PDDocument.load(new File(fileName));
            Writer output = new PrintWriter(fileName+".html", String.valueOf(StandardCharsets.UTF_8));
            new PDFDomTree().writeText(pdf, output);

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String convertPDFtoText(String fileName){
        try{
            PDDocument pdf = PDDocument.load(new File(fileName));
            String parsedText;
            PDFTextStripper stripper = new PDFTextStripper();
            parsedText = stripper.getText(pdf);
            pdf.close();
            return parsedText;
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
