package com.dls.aa.controller;

import com.dls.aa.ServiceContainer;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.dls.aa.ServiceContainer.getNodeMapping;

public class SlideMenuController implements Initializable {

    @FXML
    private JFXButton testBtn;


    @FXML
    private JFXButton chainPathBtn;

    @FXML
    private JFXButton naviToAfiAndConnection;

    @FXML
    private JFXButton moduleStructureBtn;


    private void setNode(Node node) {
        StackPane centerContainerPane = ServiceContainer.getCenterContainerPane();
        if (centerContainerPane == null) {
            throw new IllegalArgumentException("center container pane is not initialized");
        }
        centerContainerPane.getChildren().clear();
        centerContainerPane.getChildren().add(node);
    }

    @FXML
    void onChainPathBtnClick(ActionEvent event) {
        System.out.println("start chainVisualizationPane");
        setNode(getNodeMapping().get(ChainVisualizationController.class.getSimpleName()));
//        new Thread(() -> Platform.runLater(() -> {
//            try {
//                flowHandler.handle("chainPathBtn");
//            } catch (VetoException | FlowException e) {
//                e.printStackTrace();
//            }
//        })).start();
    }

    @FXML
    void onModuleStructureBtnClick(ActionEvent event) {
        System.out.println("start moduleStructurePane");

        setNode(getNodeMapping().get(ModuleStructureController.class.getSimpleName()));

//        new Thread(() -> Platform.runLater(() -> {
//            try {
//                flowHandler.handle("moduleStructureBtn");
//            } catch (VetoException | FlowException e) {
//                e.printStackTrace();
//            }
//        })).start();
    }

    @FXML
    void onTestClick(ActionEvent event) {
//        new Thread(() -> Platform.runLater(() -> {
//            try {
//                flowHandler.handle("naviToAfiAndConnection");
//            } catch (VetoException | FlowException e) {
//                e.printStackTrace();
//            }
//        })).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
