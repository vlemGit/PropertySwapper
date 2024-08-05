module com.vlemgit {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vlemgit.controller to javafx.fxml;
    opens com.vlemgit.model to javafx.base;
    opens com.vlemgit.service to javafx.base;
    exports com.vlemgit;
}
