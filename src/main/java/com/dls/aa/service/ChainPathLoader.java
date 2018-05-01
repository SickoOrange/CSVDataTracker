package com.dls.aa.service;

import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.ChainPath;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Port;
import com.dls.aa.model.PortKey;
import com.google.common.collect.ImmutableList;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPathLoader {

    private static final int MAX_DEPTH = 7;

    private OnChainPathVertexClickListener onChainPathVertexClickListener;


    public List<ChainPath> loadChainPaths(int sourceAfiId, int destinationAfiId,
                                          List<Connection> connections) {
        if (sourceAfiId == destinationAfiId) {
            throw new IllegalArgumentException(String
                    .format("source %d and destination %d can't be identical", sourceAfiId,
                            destinationAfiId));
        }
        return searchChainsRecursive(sourceAfiId, sourceAfiId, destinationAfiId, 0,
                new LinkedList<>(), connections);
    }

    private List<ChainPath> searchChainsRecursive(int sourceAfiId, int inputId,
                                                  int destinationAfiId, int depth,
                                                  LinkedList<Integer> interIds, List<Connection> connections) {

        if (depth > MAX_DEPTH) {
            return Collections.emptyList();
        }
        Optional<ChainPath> chainPath = findChainPath(inputId, sourceAfiId,
                destinationAfiId,
                interIds);

        if (!chainPath.isPresent()) {
            //start recursive

            if (inputId != sourceAfiId) {
                interIds.add(0, inputId);
            }

            List<Connection> connectionsConnectedToInputId = connections.stream()
                    .filter(connection -> connection.getIn().getAfiId() == inputId)
                    .collect(Collectors.toList());

            return connectionsConnectedToInputId
                    .stream()
                    .map(Connection::getOut)
                    .map(PortKey::getAfiId)
                    .map(id -> searchChainsRecursive(sourceAfiId, id, destinationAfiId, depth + 1,
                            new LinkedList<>(interIds), connections))
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

        }

        return Collections.singletonList(chainPath.get());

    }

    private Optional<ChainPath> findChainPath(int inputId,
                                              int sourceAfiId,
                                              int destinationAfiId, LinkedList<Integer> interIds) {

        if (inputId == destinationAfiId) {
            return Optional
                    .of(new ChainPath(sourceAfiId, destinationAfiId, ImmutableList.copyOf(interIds)));
        }
        return Optional.empty();
    }


    public mxGraphComponent chainVisualization(List<ChainPath> chainPaths) {

        //create the trace graph
        mxGraph graph = new mxGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        //graph setting
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        if (chainPaths == null) {
            prepareMockChainVisualization(graph, parent);
        } else {
            for (int i = 0; i < chainPaths.size(); i++) {
                prepareChainPath(chainPaths.get(i), graph, parent, i);
            }

        }

        graph.getModel().endUpdate();

        //autoLayout(graph, graphComponent);
        graphComponent.setEnabled(false);
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                onChainPathVertexClickListener.onChainPathVertexClick(graph.convertValueToString(cell));
            }
        });
        return graphComponent;
    }

    private void prepareChainPath(ChainPath chainPath, mxGraph graph, Object parent, int i) {
        int sourceId = chainPath.getAlertId();
        int destinationId = chainPath.getSourceId();
        List<Integer> interIds = chainPath.getInterIds();

        List<Integer> chainsId = new LinkedList<>();
        chainsId.add(0, sourceId);
        for (int j = interIds.size() - 1; j >= 0; j--) {
            chainsId.add(0, interIds.get(j));
        }
        chainsId.add(0, destinationId);

        List<Object> chainVertexs = new ArrayList<>();
        for (int k = 0; k < chainsId.size(); k++) {
            Object vertex = graph
                    .insertVertex(parent, null, chainsId.get(k), 20 + 120 * k, 20 + 60 * i, 80, 30);
            chainVertexs.add(vertex);
        }

        for (int m = 0; m < chainVertexs.size() - 1; m++) {
            graph.insertEdge(parent, null, null, chainVertexs.get(m), chainVertexs.get(m + 1));
        }
    }

    private void prepareMockChainVisualization(mxGraph graph, Object parent) {
        List<ChainPath> mockIds = ImmutableList
                .of(new ChainPath(10005, 10001, ImmutableList.of(10002, 10003, 10004)));
        for (int i = 0; i < mockIds.size(); i++) {
            prepareChainPath(mockIds.get(i), graph, parent, i);
        }
    }

    private void autoLayout(mxGraph graph, mxGraphComponent graphComponent) {
        //define layout
        mxIGraphLayout layout = new mxCompactTreeLayout(graph);

        //using morphing
        graph.getModel().beginUpdate();

        //execute the layout algorithm
        layout.execute(graph.getDefaultParent());

        mxMorphing morphing = new mxMorphing(graphComponent, 20, 1.5, 20);

        morphing.addListener(mxEvent.DONE, (o, mxEventObject) -> {
            //when the morphing finish, then call this method
            System.out.println("visualization done");
            graph.getModel().endUpdate();
        });

        //execute morphing
        morphing.startAnimation();

    }

    public String toString(List<ChainPath> chainPaths) {
        StringBuffer buffer = new StringBuffer();
        chainPaths.forEach(chainPath -> {
            buffer.append("Chain Info: ").append(chainPath.getSourceId()).append(" -> ");
            chainPath.getInterIds().forEach(id -> buffer.append(id).append(" -> "));
            buffer.append(chainPath.getAlertId()).append("\r\n");
        });
        return buffer.toString();
    }

    public Map<String, Port> findPortFromPortUniqueName(CSVLoader csvLoader, String uniqueName)
            throws IOException {
        return findPortsFromPortUniqueNames(csvLoader, ImmutableList.of(uniqueName));
    }


    public Map<String, Port> findPortsFromPortUniqueNames(CSVLoader csvLoader,
                                                          List<String> names) throws IOException {
        return csvLoader.loadPorts(line -> names.contains(Port.extractUniqueName(line)))
                .collect(Collectors.toMap(Port::getUniqueName, Function.identity()));
    }

    public void setOnChainPathVertexClickListener(
            OnChainPathVertexClickListener onChainPathVertexClickListener) {
        this.onChainPathVertexClickListener = onChainPathVertexClickListener;
    }
}
