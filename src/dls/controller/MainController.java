package dls.controller;

import dls.loader.CSVLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainController {
    @FXML
    private Button loadConnection;

    @FXML
    void loadConnection(ActionEvent event) throws IOException {
        CSVLoader csvLoader = new CSVLoader();
    }
}
