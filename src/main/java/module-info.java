module com.kxw959.programmingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.json;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires commons.csv;
    requires org.apache.pdfbox;
    requires itextpdf;

    opens com.kxw959.programmingapplication to javafx.fxml;
    exports com.kxw959.programmingapplication;
    exports com.kxw959.programmingapplication.controllers;
    opens com.kxw959.programmingapplication.controllers to javafx.fxml;
    exports com.kxw959.programmingapplication.network;
    opens com.kxw959.programmingapplication.network to javafx.fxml;
    exports com.kxw959.programmingapplication.user;
    opens com.kxw959.programmingapplication.user to javafx.fxml;
}