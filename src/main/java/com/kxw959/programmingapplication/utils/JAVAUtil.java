package com.kxw959.programmingapplication.utils;

import com.kxw959.programmingapplication.user.User;
import javafx.scene.control.TextArea;
import org.apache.commons.io.FileUtils;

import java.io.*;
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
            File file = new File(fileName);
            content = FileUtils.readFileToString(file, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(content.contains("package")){
            content = content.replaceAll("^package.*", packageName);
        }
        else{
            content = packageName+"\n"+content;
        }
        if(content.contains("jupiter")){
            User.JunitVersion = 5;
            System.out.println("YE");
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
            File file = new File(fileName);
            content = FileUtils.readFileToString(file, "UTF-8");
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
    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
