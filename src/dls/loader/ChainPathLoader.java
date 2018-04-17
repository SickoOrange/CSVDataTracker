package dls.loader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import dls.model.ChainPath;
import dls.model.Connection;
import dls.model.PortKey;
import javafx.geometry.HorizontalDirection;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPathLoader {

    private static final int MAX_DEPTH = 7;


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


    public mxGraphComponent chainVisualization(ChainPath chainPath) {

        //create the trace graph
        mxGraph graph = new mxGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);

        //graph setting
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        int sourceId = chainPath.getSourceId();
        int destinationId = chainPath.getDestinationId();
        List<Integer> interIds = chainPath.getInterIds();


        List<Integer> chainsId = new LinkedList<>();
        chainsId.add(0, sourceId);
        for (int i = interIds.size() - 1; i >= 0; i--) {
            chainsId.add(0, interIds.get(i));
        }
        chainsId.add(0, destinationId);

        List<Object> chainVertexs = new ArrayList<>();
        for (int i = 0; i < chainsId.size(); i++) {
            Object vertex = graph.insertVertex(parent, null, chainsId.get(i), 20 + 120 * i, 20, 80, 30);
            chainVertexs.add(vertex);
        }


        for (int i = 0; i < chainVertexs.size() - 1; i++) {
            graph.insertEdge(parent, null, null, chainVertexs.get(i), chainVertexs.get(i + 1));
        }
        graph.getModel().endUpdate();

        //autoLayout(graph, graphComponent);
        return graphComponent;
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
            System.out.println("done");
            graph.getModel().endUpdate();
        });

        //execute morphing
        morphing.startAnimation();

    }

}
