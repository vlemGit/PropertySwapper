module com.vlemgit {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vlemgit to javafx.fxml;
    exports com.vlemgit;
}
