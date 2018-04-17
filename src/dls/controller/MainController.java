package dls.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dls.loader.CSVLoader;
import dls.loader.ChainPathLoader;
import dls.model.ChainPath;
import dls.model.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
  private JFXTextArea chainResultTA;

  @FXML
  private JFXTextField destinationText;

  private List<Connection> connections;
  private ChainPathLoader chainPathLoader;


  @FXML
  void loadConnection(ActionEvent event) throws IOException {

    int sourceAfiId = Integer.parseInt(sourceText.getText());
    int destinationAfiId = Integer.parseInt(destinationText.getText());

    List<ChainPath> chainPaths = chainPathLoader
        .loadChainPaths(sourceAfiId, destinationAfiId, connections);

    StringBuffer buffer = new StringBuffer();
    chainPaths.forEach(chainPath -> {
      buffer.append("Chain Info: ").append(chainPath.getDestinationId()).append(" -> ");
      chainPath.getInterIds().forEach(id -> buffer.append(id).append(" -> "));
      buffer.append(chainPath.getSourceId()).append("\r\n");
    });

    chainResultTA.setText(buffer.toString());
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    CSVLoader csvLoader = new CSVLoader();
    try {
      connections = csvLoader.loadConnections().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    chainPathLoader = new ChainPathLoader();

  }
}
