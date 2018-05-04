package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import static com.dls.aa.controller.DashBoardController.CP_LOADER;
import static com.dls.aa.controller.DashBoardController.LOADER;
import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.ChainPath;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.dls.aa.model.Port;
import com.dls.aa.service.ChainPathLoader;
import com.dls.aa.service.OnChainPathVertexClickListener;
import com.dls.aa.tableview.AfiTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mxgraph.swing.mxGraphComponent;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;
import javax.swing.SwingUtilities;

public class ChainController implements Initializable, OnChainPathVertexClickListener {

  @FXML
  private JFXButton loadConnectionBtn;

  @FXML
  private TextField search_alert;


  @FXML
  private TextField search_source;

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
  private JFXTreeTableView<AfiTableViewModel> moduleTreeTableView;
  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> afiidColumn;
  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> nodeidColumn;
  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> afitypeidColumn;
  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, String> symbolColumn;
  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, String> nameColumn;


  private List<Connection> connections;
  private ChainPathLoader chainPathLoader;
  private CSVLoader csvLoader;

  private Map<Integer, Module> modules;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesMapping().get(LOADER);
    chainPathLoader = (ChainPathLoader) ServiceContainer.getInstance().getServicesMapping().get(CP_LOADER);
    chainPathLoader.setOnChainPathVertexClickListener(this);
  }

  @FXML
  public void onLoadConnectionBtnClick(ActionEvent actionEvent) {
    System.out.println("load connection for alert and source afi id");
    String alertInfo = search_alert.getText();
    String sourceInfo = this.search_source.getText();

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

        if (Objects.isNull(connections)) {
          connections = csvLoader.loadConnections(null).collect(Collectors.toList());

        }
        return chainPathLoader
            .loadChainPaths(alertAfi, sourceAfi, connections);
      }
    };

    searchChainPaths.setOnSucceeded(e -> {
      //data visualization
      visualization(searchChainPaths.getValue());
      Set<Integer> relevantIds = searchChainPaths.getValue().stream()
          .map(ChainPath::toList)
          .flatMap(Collection::stream)
          .distinct()
          .collect(Collectors.toSet());
      try {
        Map<Integer, Module> modules = csvLoader
            .loadModules(line -> relevantIds.contains(Module.extractId(line)));
        setUpReadOnlyTableView(modules);
      } catch (IOException e1) {
        e1.printStackTrace();
      }

    });
    new Thread(searchChainPaths).start();
  }

  private void setUpReadOnlyTableView(Map<Integer, Module> modules) {
    System.out.println("start set up tableview ");
    setupTableCellValueFactory(afiidColumn, m -> m.afiId.asObject());
    setupTableCellValueFactory(nodeidColumn, p -> p.nodeId.asObject());
    setupTableCellValueFactory(afitypeidColumn, p -> p.afiTypeId.asObject());
    setupTableCellValueFactory(symbolColumn, AfiTableViewModel::symbolProperty);
    setupTableCellValueFactory(nameColumn, AfiTableViewModel::nameProperty);

    ObservableList<AfiTableViewModel> entities = TableViewFactory.collectAfiEntities().apply(modules);
    System.out.println(entities.size());

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

  private void updateSelectedIndexInTable(JFXTreeTableView<AfiTableViewModel> moduleTreeTableView,
      int afiid) {

    ObservableList<TreeItem<AfiTableViewModel>> children = moduleTreeTableView.getRoot()
        .getChildren();
    Optional<TreeItem<AfiTableViewModel>> treeItem = children.stream()
        .filter(child -> child.getValue().afiId.isEqualTo(afiid).get()).findAny();
    treeItem.ifPresent(treeTableViewModuleTreeItem -> {
      moduleTreeTableView.getSelectionModel()
          .clearAndSelect(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
      moduleTreeTableView.scrollTo(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
    });


  }

  @Override
  public void onChainPathVertexClick(String vertexName) {

  }
}
