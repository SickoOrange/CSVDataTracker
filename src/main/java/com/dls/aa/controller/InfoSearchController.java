package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.dls.aa.LoaderServiceContainer;
import com.dls.aa.WizardController;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Connection;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;

import javax.annotation.PostConstruct;

@ViewController("/fxml/ui/module_connection_content_layout.fxml")
public class InfoSearchController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    private JFXTreeTableView<?> moduleTreeTableView;

    @FXML
    private JFXTreeTableColumn<?, ?> module_afiidColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_nameColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_portColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_portNameColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_typeColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_isAlarmColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_alarmColumn;

    @FXML
    private JFXTreeTableColumn<?, ?> module_descColumn;

    @FXML
    private JFXTreeTableView<?> connectionTreeTableView;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_afiid1Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_portid1Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_portName1Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_type1Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_afiid2Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_portid2Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_portName2Column;

    @FXML
    private JFXTreeTableColumn<?, ?> connection_type2Column;

    @FXML
    private JFXTextField searchModuleIdAfiTypeIdButton;

    @FXML
    private JFXTextField searchConnectionAfiid1Button;

    @FXML
    private JFXTextField searchConnectionAfiid2Button;

    @FXML
    private JFXTextField searchModuleNameButton;

    private HashMap<Object, Object> servicesContains;

    public InfoSearchController() {
    }

    @PostConstruct
    public void init() {
        servicesContains = LoaderServiceContainer.getInstance().getServicesContains();
        CSVLoader csvLoader = (CSVLoader) servicesContains.get(WizardController.LOADER);
        searchModuleIdAfiTypeIdButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
            }
        });


        searchModuleNameButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

            }
        });

        searchConnectionAfiid2Button.setOnKeyPressed(event -> {
            int afiid2 = Integer.parseInt(searchConnectionAfiid2Button.getText());
            try {
                List<Connection> connections = csvLoader.loadConnections(line -> Connection.extractAfi2(line) == afiid2)
                        .collect(Collectors.toList());
                loadConnectionsToTabelView(connections, connectionTreeTableView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        searchConnectionAfiid1Button.setOnKeyPressed(event -> {
            int afiid1 = Integer.parseInt(searchConnectionAfiid1Button.getText());
            try {
                List<Connection> connections = csvLoader.loadConnections(line -> Connection.extractAfi1(line) == afiid1)
                        .collect(Collectors.toList());
                loadConnectionsToTabelView(connections, connectionTreeTableView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadConnectionsToTabelView(List<Connection> connections, JFXTreeTableView<?> connectionTreeTableView) {

    }


}
