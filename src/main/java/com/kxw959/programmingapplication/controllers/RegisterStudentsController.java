package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.sceneManager.SceneManager;
import com.kxw959.programmingapplication.utils.JSONUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RegisterStudentsController {
    @FXML
    ListView<String> classList;
    @FXML
    Button addClass;
    @FXML
    TextField className;
    @FXML
    Button registerCSV;
    @FXML
    Button getPDF;
    @FXML
    TextInputControl nameText;
    @FXML
    Button registerIndividual;

    String selectedClass = "";

    @FXML
    public void initialize() throws IOException {
        try {
            JsonElement jsonElement = NetworkManager.getJSONElementFromURL(new URL(NetworkManager.TEACHER+User.username), "classes");
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i<jsonArray.size(); i++){
                classList.getItems().add(jsonArray.get(i).getAsString());
            }
            selectedClass = classList.getItems().get(0);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickIndividual(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        if(!Objects.equals(nameText.getText(), "")){
            NetworkManager.registerStudent(nameText.getText(), selectedClass);
        }
    }
    @FXML
    public void onClickCSV(ActionEvent actionEvent) throws IOException, NoSuchAlgorithmException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a .csv File");
        File file = fc.showOpenDialog(SceneManager.stage);
        if(file != null){
            List<List<String>> records = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    records.add(Arrays.asList(values));
                }
            }
            System.out.println(records);
            for(List<String> person: records){
                NetworkManager.registerStudent(person.get(0), person.get(1)==null ? selectedClass : person.get(1));
            }
        }
    }
    @FXML
    public void onClickPDF(ActionEvent actionEvent) {
        //Get Students names, usernames, password, class
        //put it into a table on a pdf
    }
    @FXML
    public void onClickAddClass(ActionEvent actionEvent) throws IOException {
        if(!Objects.equals(className.getText(), "")){
            classList.getItems().add(className.getText());
        }
        NetworkManager.addClass(User.username, className.getText());
    }
    @FXML
    public void onClickItem(MouseEvent mouseEvent) {
        selectedClass = classList.getSelectionModel().getSelectedItem();
    }
}
