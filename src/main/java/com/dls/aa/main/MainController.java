package com.dls.aa.main;


import com.dls.aa.controller.ChainVisualizationController;
import com.dls.aa.controller.NetworkVisualizationController;
import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.LinkAction;
import javafx.fxml.FXML;

@ViewController(value = "/Main.fxml", title = "DLS Data Tracker")
public class MainController {

  @FXML
  @LinkAction(ChainVisualizationController.class)
  private JFXButton naviToChainVisualization;

  @FXML
  @LinkAction(NetworkVisualizationController.class)
  private JFXButton naviToNetworkVisualization;


}
