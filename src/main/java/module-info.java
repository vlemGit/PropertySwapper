module com.vlemgit {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.vlemgit.controller to javafx.fxml;
    opens com.vlemgit.model to javafx.base, com.fasterxml.jackson.databind;
    opens com.vlemgit.service to javafx.base, com.fasterxml.jackson.databind;

    exports com.vlemgit;
}
