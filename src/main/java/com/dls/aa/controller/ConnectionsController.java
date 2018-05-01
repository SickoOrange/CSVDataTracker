package com.dls.aa.controller;

import com.dls.aa.AsyncTaskContainer;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Connection;
import com.dls.aa.tableview.ConnectionTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

public class ConnectionsController implements Initializable {

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
    private JFXTextField searchConnectionAfiid1Button;

    @FXML
    private JFXTextField searchConnectionAfiid2Button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CSVLoader csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesContains().get(DashBoardController.LOADER);
        searchConnectionAfiid2Button.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                int afiid2 = Integer.parseInt(searchConnectionAfiid2Button.getText());

                AsyncTaskContainer<Integer, List<Connection>> collectConnectionsById = new AsyncTaskContainer<>(afiid2, id -> {
                    try {
                        return csvLoader
                                .loadConnections(line -> Connection.extractAfi2(line) == afiid2)
                                .collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                    }
                });
                collectConnectionsById.setOnSucceeded(e -> loadConnectionsToTableView(collectConnectionsById.getValue()));
                new Thread(collectConnectionsById).start();


            }

        });

        searchConnectionAfiid1Button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int afiid1 = Integer.parseInt(searchConnectionAfiid1Button.getText());
                AsyncTaskContainer<Integer, List<Connection>> collectConnectionsById = new AsyncTaskContainer<>(afiid1, id -> {
                    try {
                        return csvLoader
                                .loadConnections(line -> Connection.extractAfi1(line) == afiid1)
                                .collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                    }
                });
                collectConnectionsById.setOnSucceeded(e -> loadConnectionsToTableView(collectConnectionsById.getValue()));
                new Thread(collectConnectionsById).start();
            }
        });
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
