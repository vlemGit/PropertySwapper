module com.vlemgit {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires org.apache.commons.validator;

    opens com.vlemgit.controller to javafx.fxml;
    opens com.vlemgit.model to javafx.base;
    opens com.vlemgit.service to javafx.base;
    exports com.vlemgit;
}
