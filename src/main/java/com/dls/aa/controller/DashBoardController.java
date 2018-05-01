package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.service.ChainPathLoader;
import com.google.common.collect.ImmutableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    public static final String LOADER = "CSVLoader";
    public static final String CP_LOADER = "ChainPathLoader";


    @FXML
    private StackPane centerContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init");
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

                ImmutableMap<String, Node> nodeMapping = ImmutableMap.<String, Node>builder()
                        .put(ChainVisualizationController.class.getSimpleName(), chainVisualizationPane)
                        .put(ModuleStructureController.class.getSimpleName(), moduleStructurePane).build();
                ServiceContainer.setNodeMapping(nodeMapping);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
