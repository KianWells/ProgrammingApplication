package com.kxw959.programmingapplication.utils;

import com.google.gson.JsonObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
}
