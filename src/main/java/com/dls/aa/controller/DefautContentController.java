package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.Main;
import com.dls.aa.service.PropertiesService;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DefautContentController implements Initializable {

    @FXML
    private JFXButton sdBtn;

    @FXML
    private Hyperlink hyperLink;

    @FXML
    private Label sourceDirectoryLabel;
    private String SOURCE_PREFIX = "Source directory";

    @FXML
    void onStBtnClick(ActionEvent event) {
        System.out.println("pick source directory");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(Main.stage);

        if (selectedDirectory != null) {
            //write into properties
            PropertiesService.writeProperties("path", selectedDirectory.getAbsolutePath());
            sourceDirectoryLabel.setText(SOURCE_PREFIX + ":" + selectedDirectory.getAbsolutePath());
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hyperLink.setTooltip(new Tooltip("https://github.com/SickoOrange/CSVDataTracker/blob/master/AfiType.csv"));
        hyperLink.setOnAction((ActionEvent event) -> {
            Hyperlink h = (Hyperlink) event.getTarget();
            String s = h.getTooltip().getText();
            Main.hostServices.showDocument(s);
            event.consume();
        });

        sourceDirectoryLabel.setText(SOURCE_PREFIX + ":" + PropertiesService.readPropValue("path"));


    }
}
