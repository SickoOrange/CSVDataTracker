/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */
package com.dls.aa.controller;

import com.dls.aa.Main;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

public class HamsterController implements Initializable {

  @FXML
  private TextField branchTv;

  @FXML
  private TextField usercaseTv;

  @FXML
  private JFXButton creatBtn;

  @FXML
  private Hyperlink hyperLink1;

  @FXML
  private Hyperlink hyperLink2;

  @FXML
  private Hyperlink hyperLink3;

  @FXML
  private Hyperlink hyperLink4;

  @FXML
  private Hyperlink hyperLink5;

  private String suffix = "_aa";

  private static final String T0001 =
      "https://dls-dev.siemens.cloud/%s/aa.static/hamster-static.html"
          + "?ppid=T0001&startDate=2015-04-01&endDate=2015-04-30&usecase=%s";

  private static final String T0002 =
      "https://dls-dev.siemens.cloud/%s/aa.static/hamster-static.html"
          + "?ppid=T0002&startDate=2016-12-06&endDate=2016-12-20&usecase=%s";

  private static final String T0003 =
      "https://dls-dev.siemens.cloud/%s/aa.static/hamster-static.html"
          + "?ppid=T0003&startDate=2016-09-01&endDate=2016-10-19&usecase=%s";

  private static final String T00XP =
      "https://dls-dev.siemens.cloud/%s/aa.static/hamster-static.html"
          + "?ppid=T00XP&startDate=2016-11-01&endDate=2016-12-13&usecase=%s";

  private static final String T00ME =
      "https://dls-dev.siemens.cloud/%s/aa.static/hamster-static.html"
          + "?ppid=T00ME&startDate=2017-05-01&endDate=2017-07-12&usecase=%s";


  @FXML
  void onCreatBtnClick(ActionEvent event) {
    System.out.printf("creat hamster hyper link");
    String branch = branchTv.getText();
    String userCase = usercaseTv.getText();

     String coptT0001;
     String coptT0002;
     String coptT0003;
     String coptT00XP;
     String coptT00ME;

    if (branch.equals("master")) {
      coptT0001 = String.format(T0001, "aa", userCase);
      coptT0002 = String.format(T0002, "aa", userCase);
      coptT0003 = String.format(T0003, "aa", userCase);
      coptT00XP = String.format(T00XP, "aa", userCase);
      coptT00ME = String.format(T00ME, "aa", userCase);
    }else {
      coptT0001 = String.format(T0001 , branch+ suffix, userCase);
      coptT0002 = String.format(T0002 , branch+ suffix, userCase);
      coptT0003 = String.format(T0003 , branch+ suffix, userCase);
      coptT00XP = String.format(T00XP , branch+ suffix, userCase);
      coptT00ME = String.format(T00ME , branch+ suffix, userCase);
    }

    hyperLink1.setText(coptT0001);
    hyperLink2.setText(coptT0002);
    hyperLink3.setText(coptT0003);
    hyperLink4.setText(coptT00XP);
    hyperLink5.setText(coptT00ME);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hyperLink1.setOnAction((ActionEvent event) -> {
      Hyperlink h = (Hyperlink) event.getTarget();
      String s = h.getText();
      Main.hostServices.showDocument(s);
      event.consume();
    });

    hyperLink2.setOnAction((ActionEvent event) -> {
      Hyperlink h = (Hyperlink) event.getTarget();
      String s = h.getText();
      Main.hostServices.showDocument(s);
      event.consume();
    });

    hyperLink3.setOnAction((ActionEvent event) -> {
      Hyperlink h = (Hyperlink) event.getTarget();
      String s = h.getText();
      Main.hostServices.showDocument(s);
      event.consume();
    });

    hyperLink4.setOnAction((ActionEvent event) -> {
      Hyperlink h = (Hyperlink) event.getTarget();
      String s = h.getText();
      Main.hostServices.showDocument(s);
      event.consume();
    });

    hyperLink5.setOnAction((ActionEvent event) -> {
      Hyperlink h = (Hyperlink) event.getTarget();
      String s = h.getText();
      Main.hostServices.showDocument(s);
      event.consume();
    });

  }
}
