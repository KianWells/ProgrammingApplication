package com.kxw959.programmingapplication.utils;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JAVAUtil {
    public JAVAUtil(){}

    public void loadJavaFile(TextArea view, String fileName){
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line!=null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String finalString = sb.toString();
            view.setText(finalString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
