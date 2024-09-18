package com.vlemgit.service;

import javafx.scene.control.Alert.AlertType;

public class Alert {

    public AlertType alertType;
    public String title;
    public String message;
    public Alert(AlertType alertType, String title, String message){
        this.alertType=alertType;
        this.title= title;
        this.message = message;
        showAlert(alertType,title,message);

    }

    public void showAlert(AlertType alertType, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
