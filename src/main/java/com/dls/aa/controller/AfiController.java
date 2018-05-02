package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import static com.dls.aa.controller.DashBoardController.LOADER;
import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

import com.dls.aa.AsyncTaskContainer;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Module;
import com.dls.aa.tableview.AfiTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang3.math.NumberUtils;

public class AfiController implements Initializable {

  @FXML
  private TextField afiTf;

  @FXML
  private TextField nodeIdTf;

  @FXML
  private TextField afiTypeTf;

  @FXML
  private TextField symbolTf;

  @FXML
  private JFXTreeTableView<AfiTableViewModel> AfiTableView;

  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> column1;

  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> column2;

  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, Integer> column3;

  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, String> column4;

  @FXML
  private JFXTreeTableColumn<AfiTableViewModel, String> column5;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    CSVLoader csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesMapping()
        .get(LOADER);

    afiTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        int afiid = NumberUtils.toInt(afiTf.getText());
        AsyncTaskContainer<Integer, Map<Integer, Module>> collectByAfiid = new AsyncTaskContainer<>(
            afiid,
            id -> {
              try {
                return csvLoader
                    .loadModules(line -> Module.extractId(line) == id);
              } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyMap();
              }
            });
        collectByAfiid
            .setOnSucceeded(e -> loadAfiToTableView(collectByAfiid.getValue()));
        new Thread(collectByAfiid).start();
      }
    });

    afiTypeTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        int afitypeid = NumberUtils.toInt(afiTypeTf.getText());
        AsyncTaskContainer<Integer, Map<Integer, Module>> collectByAfitypeId = new AsyncTaskContainer<>(
            afitypeid,
            id -> {
              try {
                return csvLoader
                    .loadModules(line -> Module.extractAfiType(line) == id);
              } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyMap();
              }
            });
        collectByAfitypeId
            .setOnSucceeded(e -> loadAfiToTableView(collectByAfitypeId.getValue()));
        new Thread(collectByAfitypeId).start();
      }
    });

    nodeIdTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        int nodeid = NumberUtils.toInt(nodeIdTf.getText());
        AsyncTaskContainer<Integer, Map<Integer, Module>> collectByNodeId = new AsyncTaskContainer<>(
            nodeid,
            id -> {
              try {
                return csvLoader
                    .loadModules(line -> Module.extractNode(line) == id);
              } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyMap();
              }
            });
        collectByNodeId
            .setOnSucceeded(e -> loadAfiToTableView(collectByNodeId.getValue()));
        new Thread(collectByNodeId).start();
      }
    });

    symbolTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String symbol = symbolTf.getText().toUpperCase();
        AsyncTaskContainer<String, Map<Integer, Module>> collectBySymbol = new AsyncTaskContainer<>(
            symbol,
            symbolName -> {
              try {
                return csvLoader
                    .loadModules(line -> Module.extractSymbol(line).equals(symbolName));
              } catch (IOException e) {
                e.printStackTrace();
                return Collections.emptyMap();
              }
            });
        collectBySymbol
            .setOnSucceeded(e -> loadAfiToTableView(collectBySymbol.getValue()));
        new Thread(collectBySymbol).start();
      }
    });


  }

  private void loadAfiToTableView(Map<Integer, Module> modules) {

    System.out.println("start set up tableview ");
    setupTableCellValueFactory(column1, module -> module.afiId.asObject());
    setupTableCellValueFactory(column2, module -> module.nodeId.asObject());
    setupTableCellValueFactory(column3, module -> module.afiTypeId.asObject());
    setupTableCellValueFactory(column4, AfiTableViewModel::symbolProperty);
    setupTableCellValueFactory(column5, AfiTableViewModel::nameProperty);

    ObservableList<AfiTableViewModel> entities = TableViewFactory.collectAfiEntities()
        .apply(modules);

    AfiTableView
        .setRoot(new RecursiveTreeItem<>(entities, RecursiveTreeObject::getChildren));

    AfiTableView.setShowRoot(false);


  }
}
