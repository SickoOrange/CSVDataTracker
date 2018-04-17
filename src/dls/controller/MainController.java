package dls.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import dls.loader.CSVLoader;
import dls.loader.ChainPathLoader;
import dls.model.ChainPath;
import dls.model.Connection;
import dls.model.Port;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javax.swing.*;

public class MainController implements Initializable {

    @FXML
    private Button loadConnection;

    @FXML
    private JFXTextField sourceText;

    @FXML
    private JFXTextArea chainResultTA;

    @FXML
    private JFXTextField destinationText;

    @FXML
    private StackPane visualizationPane;


    private List<Connection> connections;
    private ChainPathLoader chainPathLoader;
    private List<Port> ports;
    private CSVLoader csvLoader;


    @FXML
    void loadConnection(ActionEvent event) throws IOException {

        int sourceAfiId = Integer.parseInt(sourceText.getText());
        int destinationAfiId = Integer.parseInt(destinationText.getText());
//    ports = csvLoader.loadPorts(line -> Port.extractAfiId(line) == 461659)
//        .collect(Collectors.toList());
//    System.out.println(ports.size() + " ports are be loaded");

        List<ChainPath> chainPaths = chainPathLoader
                .loadChainPaths(sourceAfiId, destinationAfiId, connections);

        StringBuffer buffer = new StringBuffer();
        chainPaths.forEach(chainPath -> {
            buffer.append("Chain Info: ").append(chainPath.getDestinationId()).append(" -> ");
            chainPath.getInterIds().forEach(id -> buffer.append(id).append(" -> "));
            buffer.append(chainPath.getSourceId()).append("\r\n");
        });

        chainResultTA.setText(buffer.toString());
        Platform.runLater(() -> {
            visualizationPane.getChildren().clear();
            SwingNode swingNode = new SwingNode();
            SwingUtilities.invokeLater(() -> swingNode.setContent(chainPathLoader.chainVisualization(chainPaths.get(0))));
            visualizationPane.getChildren().add(swingNode);
        });


    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        csvLoader = new CSVLoader();
        try {
            connections = csvLoader.loadConnections().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        chainPathLoader = new ChainPathLoader();

    }
}
