package com.kxw959.programmingapplication.utils;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class JAVAUtil {
    public JAVAUtil(){}

    public void changePackageName(String fileName, String packageName){
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = Arrays.toString(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(content.contains("package")){
            content = content.replaceAll("^package.*", packageName);
        }
        else{
            content = packageName+"\n"+content;
        }
        try {
            Files.write(Paths.get(fileName), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateImports(String fileName, String newPath, String className){
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = Arrays.toString(Files.readAllBytes(Paths.get(fileName)));
            //content = content.replaceFirst("\n", "import com.kxw959.programmingapplication.tasks.Example_start;");
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = content.replaceAll("^import.*", "");
        try {
            Files.write(Paths.get(fileName), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
