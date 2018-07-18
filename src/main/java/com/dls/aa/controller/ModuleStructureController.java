package com.dls.aa.controller;


import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.loader.VisualizationLoader;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.google.common.collect.*;
import com.mxgraph.swing.mxGraphComponent;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.math.NumberUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.dls.aa.controller.DashBoardController.LOADER;


public class ModuleStructureController implements Initializable {


    private final static int MAX_DEPTH = 7;
    @FXML
    private StackPane visualizationPane;
    @FXML
    private TextField search_tf;
    private CSVLoader csvLoader;
    @FXML
    private ImageView default_imageview;
    private List<Connection> connections;


    private void moduleStructureVisualization(Set<Connection> conns) {
        visualizationPane.getChildren().clear();
        VisualizationLoader service = new VisualizationLoader();
        SwingNode swingNode = new SwingNode();
        mxGraphComponent gComponent = service.moduleStructureVisualization(conns);
        SwingUtilities
                .invokeLater(() -> swingNode.setContent(gComponent));
        visualizationPane.getChildren().add(swingNode);
        default_imageview.setVisible(false);

    }

    private long collectConnectionByAfiid(List<Connection> connections, Set<Connection> conns,
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
                System.out.println("start to visualize module structure ");
                String s = search_tf.getText();
                if (NumberUtils.isParsable(s)) {
                    int startNode = NumberUtils.toInt(s);
                    if (Objects.isNull(connections) || connections.isEmpty()) {
                        try {
                            connections = csvLoader.loadConnections().collect(Collectors.toList());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Set<Connection> filteredConns = null;

                    try {
                        filteredConns = searchNetwork(startNode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //collectConnectionByAfiid(connections, filtedConns, startNode, 0);
                    moduleStructureVisualization(filteredConns);

                } else {
                    System.out.println("can't parse the node id string:%d" + s);
                }
            }
        });
    }

    protected Set<Connection> searchNetwork(Integer rootNode) throws IOException {
        System.out.println("start to search network for root node:" + rootNode);
        List<String> allowedInterModules = ImmutableList.of("AND", "OR", "NOT", "BSEL", "BIN"
                , "T2000P_OR", "T2000P_AND", "T2000P_NOT", "T2000P_NAND", "T2000P_NOR");
        Map<Integer, Module> modules = csvLoader.loadModules(null);


        AtomicInteger interValue = new AtomicInteger(-1);

        Set<Connection> filterdConns = Sets.newHashSet();

        //search queue
        LinkedList<Integer> searchQueue = Lists.newLinkedList();

        // collections for visited node with key=visitedNode, value=ancestorNode
        Multimap<Integer, Integer> visitedNodeWithAncestors = HashMultimap.create();

        visitedNodeWithAncestors.put(rootNode, null);
        searchQueue.add(rootNode);
        searchQueue.add(null);

        while (searchQueue.size() > 1) {

            Integer currentNode = searchQueue.poll();

            if (Objects.isNull(currentNode)) {
                int currentInter = interValue.incrementAndGet();
                System.out.println("current inter value: " + currentInter);
                searchQueue.add(null);
            } else {
                List<Integer> extendableChildren = Lists.newArrayList();

                Set<Connection> descendantConns = connections.stream().filter(c -> c.getIn().getAfiId() == currentNode)
                        // //check current node is inter modules or not
                        .filter(c -> allowedInterModules.contains(modules.get(currentNode).getSymbol()))
                        .peek(c -> {
                            Integer descendantNode = c.getOut().getAfiId();
                            if (!visitedNodeWithAncestors.keySet().contains(descendantNode)) {
                                searchQueue.add(descendantNode);
                            } else {
                                System.out.println(String.format("descent node: %d  for current node: %d at root node:%d has already been visited", descendantNode,
                                        currentNode, rootNode));
                            }
                            visitedNodeWithAncestors.put(descendantNode, currentNode);
                        }).collect(Collectors.toSet());
                filterdConns.addAll(descendantConns);

            }

            if (interValue.get() >= MAX_DEPTH) {
                System.out.println(String.format("reached the maximal depth:%s, but the programm should" +
                        "continue to search source signal", interValue.get()));
            }
        }
        System.out.println(String.format("search network finish. totally visited nodes:%d, totally connections:%d", visitedNodeWithAncestors.size(), filterdConns.size()));
        visitedNodeWithAncestors.keySet().stream()
                .forEach(e -> {
                    Collection<Integer> ancestors = visitedNodeWithAncestors.get(e);
                    if (ancestors.size() > 1) {
                        System.out.println(String.format("node %d has multiple ancestors", e));
                    }
                });

        System.out.println("maximal inter value: "+interValue.get());
        return filterdConns;
    }

}
