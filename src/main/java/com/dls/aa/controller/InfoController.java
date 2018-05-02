package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

import com.dls.aa.AsyncTaskContainer;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Port;
import com.dls.aa.tableview.PortTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;

public class InfoController implements Initializable {




    @FXML
    private JFXTreeTableView<PortTableViewModel> portTableView;


    @FXML
    private JFXTreeTableColumn<PortTableViewModel, Integer> column21;
    @FXML
    private JFXTreeTableColumn<PortTableViewModel, Integer> column22;

    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column23;

    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column24;
    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column25;
    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column26;

    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column27;

    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column28;

    @FXML
    private JFXTreeTableColumn<PortTableViewModel, String> column29;


    @FXML
    private JFXTextField searchPortNameButton;







    private HashMap<Object, Object> servicesContains;


    private CSVLoader csvLoader;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        servicesContains = ServiceContainer.getInstance().getServicesMapping();
        csvLoader = (CSVLoader) servicesContains.get(DashBoardController.LOADER);






    }










}
