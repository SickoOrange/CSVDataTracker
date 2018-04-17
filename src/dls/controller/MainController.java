package dls.controller;

import com.google.common.collect.ImmutableList;
import com.jfoenix.controls.JFXTextField;
import dls.loader.CSVLoader;
import dls.model.Connection;
import dls.model.PortKey;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainController implements Initializable {

  @FXML
  private Button loadConnection;

  @FXML
  private JFXTextField sourceText;

  @FXML
  private JFXTextField destinationText;
  private CSVLoader csvLoader;

  private List<Connection> connections;


  @FXML
  void loadConnection(ActionEvent event) throws IOException {

    int sourceAfiId = Integer.parseInt(sourceText.getText());
    int destinationAfiId = Integer.parseInt(destinationText.getText());

    System.out.println("start to find chain path");

  }



  @Override
  public void initialize(URL location, ResourceBundle resources) {
    csvLoader = new CSVLoader();
    try {
      connections = csvLoader.loadConnections().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
