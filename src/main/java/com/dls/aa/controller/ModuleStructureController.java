package com.dls.aa.controller;


import static com.dls.aa.controller.DashBoardController.LOADER;

import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.Connection;
import com.dls.aa.loader.VisualizationLoader;
import com.google.common.collect.Lists;
import com.mxgraph.swing.mxGraphComponent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.math.NumberUtils;


public class ModuleStructureController implements Initializable {


  @FXML
  private StackPane visualizationPane;

  @FXML
  private TextField search_tf;

  private CSVLoader csvLoader;

  @FXML
  private ImageView default_imageview;


  private final static int MAX_DEPTH = 7;
  private List<Connection> connections;


  private void moduleStructureVisualization(ArrayList<Connection> conns) {
    visualizationPane.getChildren().clear();
    VisualizationLoader service = new VisualizationLoader();
    SwingNode swingNode = new SwingNode();
    mxGraphComponent gComponent = service.moduleStructureVisualization(conns);
    SwingUtilities
        .invokeLater(() -> swingNode.setContent(gComponent));
    visualizationPane.getChildren().add(swingNode);
    default_imageview.setVisible(false);
  }

  private long collectConnectionByAfiid(List<Connection> connections, ArrayList<Connection> conns,
      int afiid2, int i) {
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesMapping().get(LOADER);
    search_tf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        System.out.println("start to load module structure ");
        String s = search_tf.getText();

        if (NumberUtils.isParsable(s)) {
          int afiid2 = NumberUtils.toInt(s);
          if (Objects.isNull(connections) || connections.isEmpty()) {
            try {
              connections = csvLoader.loadConnections().collect(Collectors.toList());
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          ArrayList<Connection> conns = Lists.newArrayList();
          collectConnectionByAfiid(connections, conns, afiid2, 0);
          moduleStructureVisualization(conns);
        }
      }
    });
  }
}
