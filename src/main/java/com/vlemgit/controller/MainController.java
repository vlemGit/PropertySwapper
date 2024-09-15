package com.vlemgit.controller;

import java.io.IOException;

import com.vlemgit.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainController {
    @FXML
    private BorderPane mainPane;

    @FXML
    public void displayDirectoryLoaderView() {
        loadView("/view/directoryLoader.fxml");
    }

    @FXML
    public void displayConfiguratorView() {
        loadView("/view/configurator.fxml");
    }

    @FXML
    public void displayVisualizerView() {
        loadView("/view/visualizer.fxml");
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Node view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

