package com.dls.aa;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import static io.datafx.controller.flow.container.ContainerAnimations.FADE;
import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_RIGHT;
import static io.datafx.controller.flow.container.ContainerAnimations.ZOOM_OUT;

import com.dls.aa.controller.ChainVisualizationController;
import com.dls.aa.controller.DefautContentController;
import com.dls.aa.controller.InfoSearchController;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.service.ChainPathLoader;
import com.jfoenix.controls.JFXButton;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import javax.annotation.PostConstruct;

@ViewController(value = "/main_stage.fxml", title = "DLS Visualization")
public class WizardController {

    public static final String LOADER = "CSVLoader";
    public static final String CP_LOADER = "ChainPathLoader";
    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    @ActionTrigger("naviToChainVisualization")
    private JFXButton naviToChainVisualization;

    @FXML
    @ActionTrigger("infoButton")
    private JFXButton infoButton;

    @FXML
    private JFXButton t2;

    @FXML
    private StackPane centerContainer;
    private FlowHandler flowHandler;


    @PostConstruct
    public void init() throws FlowException {

        context = new ViewFlowContext();
        Flow innerFlow = new Flow(DefautContentController.class);
        flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        context.register("ContentPane", centerContainer);

        bindNodeToController(naviToChainVisualization, ChainVisualizationController.class, innerFlow);
        bindNodeToController(infoButton, InfoSearchController.class, innerFlow);
        Duration containerAnimationDuration = Duration.millis(450);
        centerContainer.getChildren()
                .add(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                        ZOOM_OUT)));

        //init services
        if (LoaderServiceContainer.getInstance().getServicesContains().isEmpty()) {
            LoaderServiceContainer.getInstance().addService(LOADER, new CSVLoader());
            LoaderServiceContainer.getInstance().addService(CP_LOADER, new ChainPathLoader());
        }


    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }

    @ActionMethod("naviToChainVisualization")
    public void onNaviToChainVisualizationClick() {
        new Thread(() -> Platform.runLater(() -> {
            try {
                flowHandler.handle("naviToChainVisualization");
            } catch (VetoException | FlowException e) {
                e.printStackTrace();
            }
        })).start();
    }

    @ActionMethod("infoButton")
    public void onInfoButtonClick() {
        System.out.println("hello");
        new Thread(() -> Platform.runLater(() -> {
            try {
                flowHandler.handle("infoButton");
            } catch (VetoException | FlowException e) {
                e.printStackTrace();
            }
        })).start();
    }

}
