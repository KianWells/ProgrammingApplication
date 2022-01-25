package com.kxw959.programmingapplication.controllers;

import com.kxw959.programmingapplication.user.StudentTableObject;
import com.kxw959.programmingapplication.utils.CopyUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SeeProgressController {
    public MenuButton classMenu;
    public Accordion studentAccordian;

    @FXML
    public void initialize(){
        //add new titled pane with new table

        addStudent();
        addStudent();
    }

    public ObservableList<StudentTableObject> getStudents(){
        ObservableList<StudentTableObject> list = FXCollections.observableArrayList();
        list.add(new StudentTableObject("Task1", "3/4"));
        return list;
    }

    private void addStudent(){
        TableView<StudentTableObject> student1 = new TableView<>();

        TableColumn<StudentTableObject, String> nameCol = new TableColumn<>("Task Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<StudentTableObject, String> testsPassedCol = new TableColumn<>("Tests Passed");
        testsPassedCol.setCellValueFactory(new PropertyValueFactory<>("testsPassed"));

        TableColumn<StudentTableObject, Button> seeCodeCol = new TableColumn<>("See Code");
        seeCodeCol.setCellValueFactory(new PropertyValueFactory<>("seeCode"));

        student1.setItems(getStudents());
        student1.getColumns().addAll(nameCol, testsPassedCol, seeCodeCol);
        TitledPane newStudent = new TitledPane();
        newStudent.setText("Student 1 - 20pts");
        newStudent.setContent(student1);

        studentAccordian.getPanes().add(newStudent);
    }

    @FXML
    public void onClickBack(ActionEvent actionEvent) {
    }
}
