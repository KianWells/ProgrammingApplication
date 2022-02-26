package com.kxw959.programmingapplication.user;

import com.kxw959.programmingapplication.HelloApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class QuizTab extends Tab {
    VBox content;
    RadioButton[] radioButtons = new RadioButton[4];
    int answer;
    ToggleGroup toggleGroup = new ToggleGroup();
    int selected;
    public boolean completed = false;

    public QuizTab(String[] s, int num) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("student-tab.fxml"));
        Tab t = fxmlLoader.load();
        content = (VBox) t.getContent();
        for(int i=1; i<5; i++){
            RadioButton rb = (RadioButton) content.lookup("#answer"+i);
            rb.setText(s[i]);
            radioButtons[i-1] = rb;
            toggleGroup.getToggles().add(radioButtons[i-1]);
        }

        Label qLabel = (Label) content.lookup("#questionLabel");
        qLabel.setText(s[0]);

        answer = Integer.parseInt(s[5]);

        this.setContent(content);
        this.setText("Q"+num);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
             public void changed(ObservableValue<? extends Toggle> ov,
                                 Toggle old_toggle, Toggle new_toggle) {
                 if (toggleGroup.getSelectedToggle() != null) {
                     for(int i=0; i<toggleGroup.getToggles().size(); i++){
                         if(toggleGroup.getToggles().get(i).isSelected()){
                             selected = i+1;
                         }
                     }
                     if(selected==answer){
                         completed = true;
                         System.out.println("CORRECT");
                     }
                     else{
                         completed  = false;
                     }
                 }
             }
        });

        System.out.println(Arrays.toString(radioButtons) + ": Answer :"+ answer);
    }

}
