package dls.controller;

import static com.google.common.collect.ComparisonChain.start;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.JFXTreeView;
import dls.loader.CSVLoader;
import dls.loader.ChainPathLoader;
import dls.model.ChainPath;
import dls.model.Connection;
import dls.model.Module;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import javax.swing.*;

public class MainController implements Initializable {

  @FXML
  private Button loadConnection;

  @FXML
  private JFXTextField sourceText;


  @FXML
  private JFXTextField destinationText;

  @FXML
  private StackPane visualizationPane;

  @FXML
  private JFXTreeTableView moduleTreeTableView;


  private List<Connection> connections;
  private ChainPathLoader chainPathLoader;
  private CSVLoader csvLoader;

  private Map<Integer, Module> modules;


  @FXML
  void loadConnection(ActionEvent event) throws IOException {

    int sourceAfiId = Integer.parseInt(sourceText.getText());
    int destinationAfiId = Integer.parseInt(destinationText.getText());

    Task<List<ChainPath>> searchChainPaths = new Task<List<ChainPath>>() {
      @Override
      protected List<ChainPath> call() {
        return chainPathLoader
            .loadChainPaths(sourceAfiId, destinationAfiId, connections);
      }
    };
    searchChainPaths.setOnSucceeded(e -> {
      visualization(searchChainPaths.getValue());
      QueryModuleTask queryModuleTask = new QueryModuleTask(searchChainPaths.getValue(), csvLoader);
      queryModuleTask.setOnSucceeded(e1 -> {
        System.out.println("loading module finish");
        modules = queryModuleTask.getValue();
        System.out.println(modules);
      });
      new Thread(queryModuleTask).start();

    });
    new Thread(searchChainPaths).start();


  }

  private void visualization(List<ChainPath> chainPaths) {
    visualizationPane.getChildren().clear();
    SwingNode swingNode = new SwingNode();
    SwingUtilities
        .invokeLater(() -> swingNode.setContent(chainPathLoader.chainVisualization(chainPaths)));
    visualizationPane.getChildren().add(swingNode);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    csvLoader = new CSVLoader();
    try {
      connections = csvLoader.loadConnections().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    chainPathLoader = new ChainPathLoader();
    visualization(null);

  }

  class QueryModuleTask extends Task<Map<Integer, Module>> {

    private final Set<Integer> relevantIds;
    private CSVLoader csvLoader;

    QueryModuleTask(List<ChainPath> chainPaths, CSVLoader csvLoader) {
      relevantIds = chainPaths.stream()
          .map(ChainPath::toList)
          .flatMap(Collection::stream)
          .distinct()
          .collect(Collectors.toSet());

      this.csvLoader = csvLoader;
    }

    @Override
    protected Map<Integer, Module> call() throws Exception {
      return csvLoader
          .loadModules(line -> relevantIds.contains(Module.extractId(line)));
    }
  }
}
