package com.dls.aa.controller;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
  @ActionTrigger("searchModuleIdAfiTypeIdButton")
  private JFXTextField searchModuleIdAfiTypeIdButton;

  @FXML
  private JFXTextField searchConnectionAfiid1Button;

  @FXML
  private JFXTextField searchConnectionAfiid2Button;

  @FXML
  private JFXTextField searchModuleNameButton;

  @PostConstruct
  public void init() {
    searchModuleIdAfiTypeIdButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        if (event.getCode()== KeyCode.ENTER) {
          System.out.println("hello world");
          Object contentFlowHandler = context.getRegisteredObject("ContentFlowHandler");
          System.out.println(Objects.isNull(contentFlowHandler));
        }
      }
    });
  }


  @ActionMethod("searchModuleIdAfiTypeIdButton")
  public void onsearchModuleIdAfiTypeIdButtonClick() {

  }

}
