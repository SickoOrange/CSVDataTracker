package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.Main;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.service.ChainPathLoader;
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

        makeStageDrageable();

        //init services
        if (ServiceContainer.getInstance().getServicesContains().isEmpty()) {
            ServiceContainer.getInstance().addService(LOADER, new CSVLoader());
            ServiceContainer.getInstance().addService(CP_LOADER, new ChainPathLoader());
            ServiceContainer.setCenterContainerPane(centerContainer);

            //prepare content
            try {
                Node chainVisualizationPane = FXMLLoader.load(getClass().getResource("/fxml/ui/chain_visualization_layout.fxml"));
                chainVisualizationPane.setId(ChainVisualizationController.class.getSimpleName());
                Node moduleStructurePane = FXMLLoader.load(getClass().getResource("/fxml/ui/module_structure_layout.fxml"));
                moduleStructurePane.setId(ModuleStructureController.class.getSimpleName());
                Node infoPane = FXMLLoader.load(getClass().getResource("/fxml/ui/info_layout.fxml"));
                moduleStructurePane.setId(InfoController.class.getSimpleName());

                ImmutableMap<String, Node> nodeMapping = ImmutableMap.<String, Node>builder()
                        .put(ChainVisualizationController.class.getSimpleName(), chainVisualizationPane)
                        .put(ModuleStructureController.class.getSimpleName(), moduleStructurePane)
                        .put(InfoController.class.getSimpleName(), infoPane)
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
