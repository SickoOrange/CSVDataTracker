package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.Main;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.loader.ChainPathLoader;
import com.google.common.collect.ImmutableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

  public static final String LOADER = "CSVLoader";
  public static final String CP_LOADER = "ChainPathLoader";

  @FXML
  private BorderPane parent;

  @FXML
  private StackPane centerContainer;

  private double xOffset = 0;
  private double yOffset = 0;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("init Dashboard");

    //makeStageDrageable();

    //init services
    if (ServiceContainer.getInstance().getServicesMapping().isEmpty()) {
      ServiceContainer.getInstance().addService(LOADER, new CSVLoader());
      ServiceContainer.getInstance().addService(CP_LOADER, new ChainPathLoader());
      ServiceContainer.setCenterContainerPane(centerContainer);

      //prepare content
      try {

        Node defaultPane = FXMLLoader
            .load(getClass().getResource("/fxml/ui/default_center_layout.fxml"));
        defaultPane.setId(DefautContentController.class.getSimpleName());

        centerContainer.getChildren().add(defaultPane);

        Node chainVisualizationPane = FXMLLoader
            .load(getClass().getResource("/fxml/ui/chain_layout.fxml"));
        chainVisualizationPane.setId(ChainController.class.getSimpleName());
        Node moduleStructurePane = FXMLLoader
            .load(getClass().getResource("/fxml/ui/module_structure_layout.fxml"));
        moduleStructurePane.setId(ModuleStructureController.class.getSimpleName());

        Node afiTypePane = FXMLLoader.load(getClass().getResource("/fxml/ui/afi_type_layout.fxml"));
        moduleStructurePane.setId(AfiTypeController.class.getSimpleName());
        Node connectionsPane = FXMLLoader
            .load(getClass().getResource("/fxml/ui/connections_layout.fxml"));
        moduleStructurePane.setId(ConnectionsController.class.getSimpleName());

        Node afiPane = FXMLLoader.load(getClass().getResource("/fxml/ui/afi_layout.fxml"));
        moduleStructurePane.setId(AfiController.class.getSimpleName());

        Node portsPane = FXMLLoader.load(getClass().getResource("/fxml/ui/ports_layout.fxml"));
        moduleStructurePane.setId(PortsController.class.getSimpleName());

        Node trendsPane = FXMLLoader
            .load(getClass().getResource("/fxml/ui/trends_analysis_layout.fxml"));
        moduleStructurePane.setId(TrendsAnalysisController.class.getSimpleName());

        Node harmstPane = FXMLLoader.load(getClass().getResource("/fxml/ui/hamster_layout.fxml"));
        moduleStructurePane.setId(HamsterController.class.getSimpleName());

        ImmutableMap<String, Node> nodeMapping = ImmutableMap.<String, Node>builder()
            .put(DefautContentController.class.getSimpleName(), defaultPane)
            .put(ChainController.class.getSimpleName(), chainVisualizationPane)
            .put(ModuleStructureController.class.getSimpleName(), moduleStructurePane)
            .put(AfiTypeController.class.getSimpleName(), afiTypePane)
            .put(ConnectionsController.class.getSimpleName(), connectionsPane)
            .put(AfiController.class.getSimpleName(), afiPane)
            .put(PortsController.class.getSimpleName(), portsPane)
            .put(TrendsAnalysisController.class.getSimpleName(), trendsPane)
            .put(HamsterController.class.getSimpleName(), harmstPane)
            .build();
        ServiceContainer.setNodeMapping(nodeMapping);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void makeStageDrageable() {
    parent.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
      }
    });
    parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        Main.stage.setX(event.getScreenX() - xOffset);
        Main.stage.setY(event.getScreenY() - yOffset);
        Main.stage.setOpacity(0.7f);
      }
    });
    parent.setOnDragDone((e) -> {
      Main.stage.setOpacity(1.0f);
    });
    parent.setOnMouseReleased((e) -> {
      Main.stage.setOpacity(1.0f);
    });

  }
}
