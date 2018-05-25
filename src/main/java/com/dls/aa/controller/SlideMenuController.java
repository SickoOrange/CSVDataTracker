package com.dls.aa.controller;

import com.dls.aa.ServiceContainer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
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
  private JFXButton trendsBtn;

  @FXML
  private JFXButton moduleStructureBtn;

  @FXML
  private JFXButton homeBtn;

  @FXML
  private JFXButton hamsterBtn;
  private ImmutableList<JFXButton> btnList;
  private String styleTextPressedValue = "-fx-text-fill: #E33461";
  private String styleTextReleasedValue = "-fx-text-fill: #98A0A6";


  private void setNode(Node node) {
    StackPane centerContainerPane = ServiceContainer.getCenterContainerPane();
    if (centerContainerPane == null) {
      throw new IllegalArgumentException("center container pane is not initialized");
    }
    centerContainerPane.getChildren().clear();
    centerContainerPane.getChildren().add(node);
  }


  private void setButtonFocus(ActionEvent event) {
    long count = btnList.stream()
        .peek(b -> b.setStyle(styleTextReleasedValue)).peek(b -> {
          if (b == event.getSource()) {
            b.setStyle(styleTextPressedValue);
          }
        }).count();
  }


  @FXML
  void onAfiTypeBtnClick(ActionEvent event) {
    System.out.println("start AfiType Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(AfiTypeController.class.getSimpleName()));
  }

  @FXML
  void onAfiBtnClick(ActionEvent event) {
    System.out.println("Start Afi Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(AfiController.class.getSimpleName()));
  }

  @FXML
  void onPortsBtnClick(ActionEvent event) {
    System.out.println("Start Ports Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(PortsController.class.getSimpleName()));

  }


  @FXML
  void onConnectionsBtnClick(ActionEvent event) {
    System.out.println("start connections Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(ConnectionsController.class.getSimpleName()));

  }

  @FXML
  void onChainPathBtnClick(ActionEvent event) {
    System.out.println("start chainVisualizationPane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(ChainController.class.getSimpleName()));
  }

  @FXML
  void onModuleStructureBtnClick(ActionEvent event) {
    System.out.println("start moduleStructurePane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(ModuleStructureController.class.getSimpleName()));
  }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    homeBtn.setStyle(styleTextPressedValue);
    btnList = ImmutableList.<JFXButton>builder()
        .add(homeBtn, afiBtn, afiTypeBtn, portsBtn, connectionsBtn, moduleStructureBtn,
            chainPathBtn, hamsterBtn, trendsBtn).build();

  }

  @FXML
  public void onHomeBtnClick(ActionEvent event) {
    System.out.println("Home");
    setButtonFocus(event);
    setNode(getNodeMapping().get(DefautContentController.class.getSimpleName()));
  }

  @FXML
  void onTrendsBtnClick(ActionEvent event) {
    System.out.println("Trends Analysis Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(TrendsAnalysisController.class.getSimpleName()));
  }

  @FXML
  void onHamsterBtnClick(ActionEvent event) {
    System.out.println("Hamster Pane");
    setButtonFocus(event);
    setNode(getNodeMapping().get(HamsterController.class.getSimpleName()));
  }
}
