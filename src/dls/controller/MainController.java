package dls.controller;

import com.google.common.collect.ImmutableList;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
  private static final int MAX_DEPTH = 7;


  @FXML
  void loadConnection(ActionEvent event) throws IOException {

    int sourceAfiId = Integer.parseInt(sourceText.getText());
    int destinationAfiId = Integer.parseInt(destinationText.getText());

    System.out.println("start to find chain path");

    List<ChainPath> chainReasons = searchChains(sourceAfiId, sourceAfiId, destinationAfiId);

    for (ChainPath chainPath : chainReasons) {
      System.out.println(chainPath.getInterModules().toString());
    }
    System.out.println("finish");

  }

  private List<ChainPath> searchChains(int sourceAfiId, int inputId, int destinationAfiId)
      throws IOException {
    if (sourceAfiId == destinationAfiId) {
      throw new IllegalArgumentException(String
          .format("source %d and destination %d can't be identical", sourceAfiId,
              destinationAfiId));
    }
    return searchChainsRecursive(sourceAfiId, inputId, destinationAfiId, 0,
        new LinkedList<>());
  }

  private List<ChainPath> searchChainsRecursive(int sourceAfiId, int inputId,
      int destinationAfiId, int depth,
      LinkedList<Integer> interIds) throws IOException {

    System.out.println(inputId);

    if (depth > MAX_DEPTH) {
      return Collections.emptyList();
    }
    //start recursive
    interIds.add(0, inputId);

    if (!isDestinationAfiId(inputId, destinationAfiId)) {

      return csvLoader.loadConnections(l -> Connection.extractAfi2(l) == inputId)
          .map(Connection::getOut)
          .map(PortKey::getAfiId)
          .map(
              id -> {
                try {
                  return searchChainsRecursive(sourceAfiId, id, destinationAfiId, depth + 1,
                      new LinkedList<>(interIds));
                } catch (IOException e) {
                  e.printStackTrace();
                  return null;
                }
              })
          .filter(Objects::nonNull)
          .flatMap(Collection::stream)
          .collect(Collectors.toList());

    }

    //find destination
    return Collections.singletonList(
        new ChainPath(sourceAfiId, destinationAfiId, ImmutableList.copyOf(interIds)));

  }

  private boolean isDestinationAfiId(int inputAfiId, int destinationAfiId) throws IOException {
    Optional<Integer> targetId = csvLoader
        .loadConnections(line -> Connection.extractAfi2(line) == inputAfiId)
        .map(Connection::getOut)
        .map(PortKey::getAfiId)
        .filter(afiId -> afiId == destinationAfiId)
        .findAny();

    return targetId.isPresent();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    csvLoader = new CSVLoader();
  }
}
