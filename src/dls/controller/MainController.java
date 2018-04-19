package dls.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mxgraph.swing.mxGraphComponent;
import dls.Main;
import dls.loader.CSVLoader;
import dls.model.Port;
import dls.service.ChainPathLoader;
import dls.model.ChainPath;
import dls.model.Connection;
import dls.model.Module;

import dls.service.OnChainPathVertexClickListener;
import java.io.IOException;
import java.net.URL;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class MainController implements Initializable, OnChainPathVertexClickListener {

  public static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
      .getLogger(Main.class);

  @FXML
  private Button loadConnection;

  @FXML
  private JFXTextField alertText;


  @FXML
  private JFXTextField sourceText;

  @FXML
  private StackPane visualizationPane;

  @FXML
  private JFXRadioButton radio_1;

  @FXML
  private ToggleGroup toggleGroup;

  @FXML
  private JFXRadioButton radio_2;


  //read only table view
  @FXML
  private JFXTreeTableView<TreeTableViewModule> moduleTreeTableView;
  @FXML
  private JFXTreeTableColumn<TreeTableViewModule, Integer> afiidColumn;
  @FXML
  private JFXTreeTableColumn<TreeTableViewModule, Integer> nodeidColumn;
  @FXML
  private JFXTreeTableColumn<TreeTableViewModule, Integer> afitypeidColumn;
  @FXML
  private JFXTreeTableColumn<TreeTableViewModule, String> symbolColumn;
  @FXML
  private JFXTreeTableColumn<TreeTableViewModule, String> nameColumn;


  private List<Connection> connections;
  private ChainPathLoader chainPathLoader;
  private CSVLoader csvLoader;

  private Map<Integer, Module> modules;


  @FXML
  void loadConnection(ActionEvent event) {

    String alertInfo = alertText.getText();
    String sourceInfo = this.sourceText.getText();

    Task<List<ChainPath>> searchChainPaths = new Task<List<ChainPath>>() {
      @Override
      protected List<ChainPath> call() throws IOException {

        int alertAfi = 0;
        int sourceAfi = 0;
        if (radio_1.isSelected()) {
          alertAfi = Integer.parseInt(alertInfo);
          sourceAfi = Integer.parseInt(sourceInfo);
        }

        if (radio_2.isSelected()) {

          //convert port unique name to afiid, if necessary
          List<String> notParsableList = Lists.newArrayList();
          notParsableList.add(alertInfo);
          notParsableList.add(sourceInfo);
          Map<String, Integer> result = Maps.transformValues(chainPathLoader
              .findPortsFromPortUniqueNames(csvLoader, notParsableList), Port::getAfiId);

          alertAfi = result.get(alertInfo);
          sourceAfi = result.get(sourceInfo);
        }

        return chainPathLoader
            .loadChainPaths(alertAfi, sourceAfi, connections);
      }
    };

    searchChainPaths.setOnSucceeded(e -> {
      //data visualization
      visualization(searchChainPaths.getValue());
      //query module info
      QueryModuleTask queryModuleTask = new QueryModuleTask(searchChainPaths.getValue(), csvLoader);
      queryModuleTask.setOnSucceeded(e1 -> {
        System.out.println("loading module finish");
        modules = queryModuleTask.getValue();
        System.out.println(modules);
        //init table
        setUpReadOnlyTableView(modules);
      });
      new Thread(queryModuleTask).start();

    });
    new Thread(searchChainPaths).start();


  }

  private void setUpReadOnlyTableView(Map<Integer, Module> modules) {

    setupCellValueFactory(afiidColumn, m -> m.afiId.asObject());
    setupCellValueFactory(nodeidColumn, p -> p.nodeId.asObject());
    setupCellValueFactory(afitypeidColumn, p -> p.afiTypeId.asObject());
    setupCellValueFactory(symbolColumn, TreeTableViewModule::symbolProperty);
    setupCellValueFactory(nameColumn, TreeTableViewModule::nameProperty);

    ObservableList<TreeTableViewModule> entities = FXCollections.observableArrayList();
    modules.values().forEach(module -> entities
        .add(new TreeTableViewModule(module.getId(), module.getNode(), module.getAfiTypeId(),
            module.getSymbol(), module.getName())));

    moduleTreeTableView
        .setRoot(new RecursiveTreeItem<>(entities, RecursiveTreeObject::getChildren));

    moduleTreeTableView.setShowRoot(false);
    moduleTreeTableView.getSelectionModel().select(2);


  }

  private void visualization(List<ChainPath> chainPaths) {
    visualizationPane.getChildren().clear();
    SwingNode swingNode = new SwingNode();
    mxGraphComponent gComponent = chainPathLoader.chainVisualization(chainPaths);
    SwingUtilities
        .invokeLater(() -> swingNode.setContent(gComponent));
    visualizationPane.getChildren().add(swingNode);
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO: 19.04.2018 load connection with asyn task
    csvLoader = new CSVLoader();
    try {
      connections = csvLoader.loadConnections().collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    chainPathLoader = new ChainPathLoader();
    chainPathLoader.setOnVertexClickListener(this);

    visualization(null);

  }

  @Override
  public void onChainPathVertexClick(String vertexString) {
    if (StringUtils.isNumeric(vertexString)) {
      updateSelectedIndexInTable(moduleTreeTableView, Integer.parseInt(vertexString));
    }

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

  // data class
  final class TreeTableViewModule extends RecursiveTreeObject<TreeTableViewModule> {

    final SimpleIntegerProperty afiId;
    final SimpleIntegerProperty nodeId;
    final SimpleIntegerProperty afiTypeId;

    final StringProperty symbol;
    final StringProperty name;

    TreeTableViewModule(int afiId,
        int nodeId, int afiTypeId,
        String symbol, String name) {
      this.afiId = new SimpleIntegerProperty(afiId);
      this.nodeId = new SimpleIntegerProperty(nodeId);
      this.afiTypeId = new SimpleIntegerProperty(afiTypeId);
      this.symbol = new SimpleStringProperty(symbol);
      this.name = new SimpleStringProperty(name);
    }

    StringProperty nameProperty() {
      return name;
    }

    StringProperty symbolProperty() {
      return symbol;
    }
  }

  private <T> void setupCellValueFactory(JFXTreeTableColumn<TreeTableViewModule, T> column,
      Function<TreeTableViewModule, ObservableValue<T>> mapper) {
    column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TreeTableViewModule, T> param) -> {
      if (column.validateValue(param)) {
        return mapper.apply(param.getValue().getValue());
      } else {
        return column.getComputedValue(param);
      }
    });
  }

  private void updateSelectedIndexInTable(JFXTreeTableView<TreeTableViewModule> moduleTreeTableView,
      int afiid) {

    ObservableList<TreeItem<TreeTableViewModule>> children = moduleTreeTableView.getRoot()
        .getChildren();
    Optional<TreeItem<TreeTableViewModule>> treeItem = children.stream()
        .filter(child -> child.getValue().afiId.isEqualTo(afiid).get()).findAny();
    treeItem.ifPresent(treeTableViewModuleTreeItem -> {
      moduleTreeTableView.getSelectionModel()
          .clearAndSelect(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
      moduleTreeTableView.scrollTo(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
    });


  }
}
