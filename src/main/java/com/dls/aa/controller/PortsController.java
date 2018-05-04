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
import com.dls.aa.model.Port;
import com.dls.aa.tableview.AfiTableViewModel;
import com.dls.aa.tableview.PortTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.apache.commons.lang3.math.NumberUtils;

public class PortsController implements Initializable {

  @FXML
  private TextField uniqueNameTf;

  @FXML
  private TextField symbolTf;

  @FXML
  private TextField afiidTf;


  @FXML
  private JFXTreeTableView<PortTableViewModel> portsTableView;

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
  private JFXTreeTableColumn<PortTableViewModel, Integer> column30;

  @FXML
  private JFXTreeTableColumn<PortTableViewModel, String> column31;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    CSVLoader csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesMapping()
        .get(LOADER);
    uniqueNameTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        if (NumberUtils.isParsable(uniqueNameTf.getText())) {
          throw new IllegalArgumentException("argument must be String");
        }
        String portName = uniqueNameTf.getText();
        AsyncTaskContainer<String, List<Port>> collectPortsWithUniqueName = new AsyncTaskContainer<>(
            portName, name -> {
          try {
            return csvLoader.loadPorts(line -> Port.extractUniqueName(line).equals(name))
                .collect(Collectors.toList());
          } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
          }
        });
        collectPortsWithUniqueName
            .setOnSucceeded(e -> loadingPortToTableView(collectPortsWithUniqueName.getValue()));
        new Thread(collectPortsWithUniqueName).start();
      }
    });

    symbolTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        if (NumberUtils.isParsable(symbolTf.getText())) {
          throw new IllegalArgumentException("argument must be String");
        }
        String symbol = symbolTf.getText();
        AsyncTaskContainer<String, List<Port>> collectPortsWithSymbol = new AsyncTaskContainer<>(
            symbol, name -> {
          try {
            return csvLoader.loadPorts(line -> Port.extractSymbol(line).equals(name))
                .collect(Collectors.toList());
          } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
          }
        });
        collectPortsWithSymbol
            .setOnSucceeded(e -> loadingPortToTableView(collectPortsWithSymbol.getValue()));
        new Thread(collectPortsWithSymbol).start();
      }
    });

    afiidTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        if (!NumberUtils.isParsable(afiidTf.getText())) {
          throw new IllegalArgumentException("argument must be Integer");
        }
        int afiid = NumberUtils.toInt(afiidTf.getText());
        AsyncTaskContainer<Integer, List<Port>> collectPortsWithAfiid = new AsyncTaskContainer<>(
            afiid, id -> {
          try {
            return csvLoader.loadPorts(line -> Port.extractAfiId(line) == id).collect(Collectors.toList());
          } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
          }
        });
        collectPortsWithAfiid
            .setOnSucceeded(e -> loadingPortToTableView(collectPortsWithAfiid.getValue()));
        new Thread(collectPortsWithAfiid).start();
      }
    });


  }

  private void loadingPortToTableView(List<Port> ports) {
    System.out.println("load ports success" + ports.size());
    setupTableCellValueFactory(column21, p -> p.id.asObject());
    setupTableCellValueFactory(column22, p -> p.afiid.asObject());
    setupTableCellValueFactory(column23, PortTableViewModel::nameProperty);
    setupTableCellValueFactory(column24, PortTableViewModel::symbolProperty);
    setupTableCellValueFactory(column25, PortTableViewModel::parameterProperty);
    setupTableCellValueFactory(column26, PortTableViewModel::uniqueNameProperty);
    setupTableCellValueFactory(column27, PortTableViewModel::directionProperty);
    setupTableCellValueFactory(column28, PortTableViewModel::isArchiveProperty);
    setupTableCellValueFactory(column29, PortTableViewModel::isAlarmProperty);
    setupTableCellValueFactory(column30, p -> p.alarmtypeid.asObject());
    setupTableCellValueFactory(column31, PortTableViewModel::abbrevProperty);

    ObservableList<PortTableViewModel> portEntities = TableViewFactory
        .collectPortEntities()
        .apply(ports);

    portsTableView.setRoot(new RecursiveTreeItem<>(portEntities,
        RecursiveTreeObject::getChildren));
    portsTableView.setShowRoot(false);
  }


}
