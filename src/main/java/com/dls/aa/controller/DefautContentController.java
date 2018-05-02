package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.Main;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;

public class DefautContentController implements Initializable {

  @FXML
  private JFXButton sdBtn;

  @FXML
  private Label sourceDirectoryLabel;

  @FXML
  void onStBtnClick(ActionEvent event) {
    System.out.println("pick source directory");
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory =
        directoryChooser.showDialog(Main.stage);

    if (selectedDirectory == null) {
      sourceDirectoryLabel
          .setText("Source directory: D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer");
    } else {
      sourceDirectoryLabel.setText("Source directory "+selectedDirectory.getAbsolutePath());
    }
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // TODO: 02.05.2018 should detect whether the csv files complete!
  }
}
