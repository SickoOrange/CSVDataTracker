package com.dls.aa.controller;

import com.dls.aa.ServiceContainer;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.VBox;

import static com.dls.aa.ServiceContainer.getNodeMapping;

public class SlideMenuController implements Initializable {


  @FXML
  private JFXButton afiTypeBtn;

  @FXML
  private JFXButton afiBtn;


  @FXML
  private JFXButton portsBtn;

  @FXML
  private JFXButton connectionsBtn;


  @FXML
  private JFXButton chainPathBtn;

  @FXML
  private JFXButton naviToAfiAndConnection;

  @FXML
  private JFXButton moduleStructureBtn;

  @FXML
  private JFXButton homeBtn;


  private void setNode(Node node) {
    StackPane centerContainerPane = ServiceContainer.getCenterContainerPane();
    if (centerContainerPane == null) {
      throw new IllegalArgumentException("center container pane is not initialized");
    }
    centerContainerPane.getChildren().clear();
    centerContainerPane.getChildren().add(node);
  }


  @FXML
  void onAfiTypeBtnClick(ActionEvent event) {
    System.out.println("start AfiType Pane");
    setNode(getNodeMapping().get(AfiTypeController.class.getSimpleName()));
  }

  @FXML
  void onAfiBtnClick(ActionEvent event) {
    System.out.println("Start Afi Pane");
    setNode(getNodeMapping().get(AfiController.class.getSimpleName()));
  }

  @FXML
  void onPortsBtnClick(ActionEvent event) {
    System.out.println("Start Ports Pane");
    setNode(getNodeMapping().get(PortsController.class.getSimpleName()));

  }


  @FXML
  void onConnectionsBtnClick(ActionEvent event) {
    System.out.println("start connections Pane");
    setNode(getNodeMapping().get(ConnectionsController.class.getSimpleName()));

  }

  @FXML
  void onChainPathBtnClick(ActionEvent event) {
    System.out.println("start chainVisualizationPane");
    setNode(getNodeMapping().get(ChainVisualizationController.class.getSimpleName()));
  }

  @FXML
  void onModuleStructureBtnClick(ActionEvent event) {
    System.out.println("start moduleStructurePane");
    setNode(getNodeMapping().get(ModuleStructureController.class.getSimpleName()));
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }

  @FXML
  public void onHomeBtnClick(ActionEvent actionEvent) {
    System.out.println("Home");
    setNode(getNodeMapping().get(DefautContentController.class.getSimpleName()));
  }
}
