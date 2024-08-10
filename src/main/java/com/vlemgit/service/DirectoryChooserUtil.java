package com.vlemgit.service;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

public class DirectoryChooserUtil {

        private DirectoryChooserUtil(){
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }

        public static File chooseDirectory(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        return directoryChooser.showDialog(window);
    }
}
