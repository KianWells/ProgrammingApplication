package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kxw959.programmingapplication.User;
import com.kxw959.programmingapplication.network.NetworkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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

    String selectedClass;

    @FXML
    public void initialize() throws IOException {
        try {
            JsonElement jsonElement = NetworkManager.getJSONElementFromURL(new URL(NetworkManager.TEACHER+User.username), "classes");
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i<jsonArray.size(); i++){
                classList.getItems().add(jsonArray.get(i).getAsString());
            }
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
    public void onClickCSV(ActionEvent actionEvent) throws IOException {
        JsonElement classesAsJSON = NetworkManager.getJSONElementFromURL(new URL(NetworkManager.TEACHER + User.username), "classes");
        try {
            JsonArray classesAsJArr = classesAsJSON.getAsJsonArray();
            for(int i = 0; i < classesAsJArr.size(); i++){
                System.out.println(classesAsJArr.get(i).getAsString());
            }
        }
        catch(IllegalStateException e){
            System.out.println("No Classes");
        }
    }
    @FXML
    public void onClickPDF(ActionEvent actionEvent) {
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
