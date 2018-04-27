package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

import com.dls.aa.LoaderServiceContainer;
import com.dls.aa.WizardController;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.AfiType;
import com.dls.aa.model.Connection;
import com.dls.aa.tableview.AfiTypeTableViewModel;
import com.dls.aa.tableview.ConnectionTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.datafx.controller.ViewController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;

import javax.annotation.PostConstruct;

@ViewController("/fxml/ui/afi_connection_content_layout.fxml")
public class InfoSearchController {

    @FXML
    private JFXTreeTableView<ConnectionTableViewModel> connectionTable;

    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, Integer> column1;
    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, Integer> column2;

    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, String> column3;

    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, String> column4;
    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, Integer> column5;
    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, Integer> column6;

    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, String> column7;

    @FXML
    private JFXTreeTableColumn<ConnectionTableViewModel, String> column8;

    @FXML
    private JFXTreeTableView<AfiTypeTableViewModel> afiTypeTable;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column11;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column12;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column13;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column14;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column15;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column16;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column17;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column18;

    @FXML
    private JFXTextField searchModuleIdAfiTypeIdButton;

    @FXML
    private JFXTextField searchConnectionAfiid1Button;

    @FXML
    private JFXTextField searchConnectionAfiid2Button;

    @FXML
    private JFXTextField searchModuleNameButton;

    private HashMap<Object, Object> servicesContains;


    private CSVLoader csvLoader;

    @PostConstruct
    public void init() {
        servicesContains = LoaderServiceContainer.getInstance().getServicesContains();
        csvLoader = (CSVLoader) servicesContains.get(WizardController.LOADER);
        searchModuleIdAfiTypeIdButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int typeId = Integer.parseInt(searchModuleIdAfiTypeIdButton.getText());
                try {
                    List<AfiType> afiTypes = csvLoader
                            .loadAfiType(line -> AfiType.extractTypeId(line) == typeId).collect(Collectors.toList());
                    System.out.println(afiTypes.size());
                    loadAfiTypesToTableView(afiTypes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
//
        searchModuleNameButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String moduleName = searchModuleNameButton.getText();
                try {
                    List<AfiType> afiTypes = csvLoader
                            .loadAfiType(line -> AfiType.extractName(line).equals(moduleName)).collect(Collectors.toList());
                    System.out.println(afiTypes.size());
                    loadAfiTypesToTableView(afiTypes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        searchConnectionAfiid2Button.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                int afiid2 = Integer.parseInt(searchConnectionAfiid2Button.getText());
                try {
                    List<Connection> connections = csvLoader
                            .loadConnections(line -> Connection.extractAfi2(line) == afiid2)
                            .collect(Collectors.toList());
                    Platform.runLater(() -> loadConnectionsToTableView(connections));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        searchConnectionAfiid1Button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int afiid1 = Integer.parseInt(searchConnectionAfiid1Button.getText());
                try {
                    List<Connection> connections = csvLoader
                            .loadConnections(line -> Connection.extractAfi1(line) == afiid1)
                            .collect(Collectors.toList());
                    loadConnectionsToTableView(connections);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void loadAfiTypesToTableView(List<AfiType> afiTypes) {
        setupTableCellValueFactory(column11, afiType -> afiType.typeid.asObject());
        setupTableCellValueFactory(column12, AfiTypeTableViewModel::nameProperty);
        setupTableCellValueFactory(column13, afiType -> afiType.port.asObject());
        setupTableCellValueFactory(column14, AfiTypeTableViewModel::portNameProperty);

        setupTableCellValueFactory(column15, AfiTypeTableViewModel::typeProperty);
        setupTableCellValueFactory(column16, AfiTypeTableViewModel::isAlarmProperty);
        setupTableCellValueFactory(column17, afiType -> afiType.alarm.asObject());
        setupTableCellValueFactory(column18, AfiTypeTableViewModel::descriptionProperty);

        ObservableList<AfiTypeTableViewModel> afiTypeEntities = TableViewFactory
                .collectAfiTypeEntities()
                .apply(afiTypes);

        afiTypeTable.setRoot(new RecursiveTreeItem<>(afiTypeEntities,
                RecursiveTreeObject::getChildren));
        afiTypeTable.setShowRoot(false);
    }


    private void loadConnectionsToTableView(List<Connection> connections
    ) {

        setupTableCellValueFactory(column1, connection -> connection.afiid1.asObject());
        setupTableCellValueFactory(column2, connection -> connection.portid1.asObject());
        setupTableCellValueFactory(column3, ConnectionTableViewModel::portName1Property);
        setupTableCellValueFactory(column4, ConnectionTableViewModel::type1Property);

        setupTableCellValueFactory(column5, connection -> connection.afiid2.asObject());
        setupTableCellValueFactory(column6, connection -> connection.portid2.asObject());
        setupTableCellValueFactory(column7, ConnectionTableViewModel::portName2Property);
        setupTableCellValueFactory(column8, ConnectionTableViewModel::type2Property);

        ObservableList<ConnectionTableViewModel> connectionEntities = TableViewFactory
                .collectConnectionEntities()
                .apply(connections);

        connectionTable.setRoot(new RecursiveTreeItem<>(connectionEntities,
                RecursiveTreeObject::getChildren));
        connectionTable.setShowRoot(false);

    }


}
