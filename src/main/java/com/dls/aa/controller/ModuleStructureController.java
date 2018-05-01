package com.dls.aa.controller;


import com.dls.aa.LoaderServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Connection;
import com.dls.aa.service.VisualizationService;
import com.google.common.collect.Lists;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.mxgraph.swing.mxGraphComponent;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.PostConstruct;
import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.dls.aa.controller.WizardController.LOADER;

@ViewController("/fxml/ui/module_structure_layout.fxml")
public class ModuleStructureController {


    @FXML
    private StackPane visualizationPane;

    @FXML
    private JFXTextField loadTextField;

    @FXML
    @ActionTrigger("startVisualization")
    private JFXButton startVisualization;
    private CSVLoader csvLoader;

    private final static int MAX_DEPTH = 7;
    private List<Connection> connections;


    @PostConstruct
    public void init() {
        csvLoader = (CSVLoader) LoaderServiceContainer.getInstance().getServicesContains().get(LOADER);

    }

    @ActionMethod("startVisualization")
    public void onClick() throws IOException {
        System.out.println("start visualization");
        String s = loadTextField.getText();

        if (NumberUtils.isParsable(s)) {
            int afiid2 = NumberUtils.toInt(s);
            if (Objects.isNull(connections) || connections.isEmpty()) {
                connections = csvLoader.loadConnections().collect(Collectors.toList());
            }
            ArrayList<Connection> conns = Lists.newArrayList();
            collectConnectionByAfiid(connections, conns, afiid2, 0);
            moduleStructureVisualization(conns);
        }


    }

    private void moduleStructureVisualization(ArrayList<Connection> conns) {
        visualizationPane.getChildren().clear();
        VisualizationService service = new VisualizationService();
        SwingNode swingNode = new SwingNode();
        mxGraphComponent gComponent = service.moduleStructureVisualization(conns);
        SwingUtilities
                .invokeLater(() -> swingNode.setContent(gComponent));
        visualizationPane.getChildren().add(swingNode);
    }

    private long collectConnectionByAfiid(List<Connection> connections, ArrayList<Connection> conns, int afiid2, int i) {
        if (i <= MAX_DEPTH) {
            return connections.stream().filter(c -> c.getIn().getAfiId() == afiid2)
                    .peek(c -> {
                        conns.add(c);
                        collectConnectionByAfiid(connections, conns, c.getOut().getAfiId(), i + 1);
                        System.out.println(c);
                    }).count();
        }
        return 0;
    }

}
