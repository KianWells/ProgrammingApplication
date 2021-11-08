package com.kxw959.programmingapplication.utils;

import javafx.scene.control.CheckBox;

public class ClassTableItem {
    private String className = "";
    private CheckBox select = new CheckBox();

    public ClassTableItem(){}


    public ClassTableItem(String name){
        className = name;
    }

    public CheckBox getSelect() {
        return select;
    }

    public String getClassName() {
        return className;
    }
}
