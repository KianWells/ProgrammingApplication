module com.kxw959.programmingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires aws.java.sdk.dynamodb;
    requires aws.java.sdk.core;
    requires aws.java.sdk.kms;
    requires aws.java.sdk.s3;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.kxw959.programmingapplication to javafx.fxml;
    exports com.kxw959.programmingapplication;
    exports com.kxw959.programmingapplication.controllers;
    opens com.kxw959.programmingapplication.controllers to javafx.fxml;
}