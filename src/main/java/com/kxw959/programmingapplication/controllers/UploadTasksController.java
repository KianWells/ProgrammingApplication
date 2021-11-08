package com.kxw959.programmingapplication.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.kxw959.programmingapplication.network.NetworkManager;
import com.kxw959.programmingapplication.user.User;
import com.kxw959.programmingapplication.utils.ClassTableItem;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UploadTasksController {


    @FXML
    public Button browseButton;
    @FXML
    public Button uploadButton;
    @FXML
    public ListView<String> taskList;
    @FXML
    public TableView<ClassTableItem> classTable;
    @FXML
    public TableColumn<ClassTableItem, String> classColumn;
    @FXML
    public TableColumn<ClassTableItem, CheckBox> selectColumn;

    private List<String> selectedClasses = new ArrayList<>();

    @FXML
    public void initialize(){
        classColumn.setCellValueFactory(new PropertyValueFactory<>("className"));
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));
        try {
            JsonElement jsonElement = NetworkManager.getJSONElementFromURL(new URL(NetworkManager.TEACHER+ User.username), "classes");
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 0; i<jsonArray.size(); i++){
                classTable.getItems().add(new ClassTableItem(jsonArray.get(i).getAsString()));
                System.out.println(jsonArray.get(i).getAsString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void onClickBrowse(ActionEvent actionEvent) {

    }

    @FXML
    public void onClickUpload(ActionEvent actionEvent) {
        FilteredList<ClassTableItem> selected = classTable.getItems().filtered(new Predicate<ClassTableItem>() {
            @Override
            public boolean test(ClassTableItem classTableItem) {
                return classTableItem.getSelect().isSelected();
            }
        });
        selectedClasses.clear();
        for(ClassTableItem i : selected){
            selectedClasses.add(i.getClassName());
        }
        System.out.println(selectedClasses);
    }
}
