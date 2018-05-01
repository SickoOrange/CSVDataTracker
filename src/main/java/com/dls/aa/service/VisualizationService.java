package com.dls.aa.service;

import com.dls.aa.model.Connection;
import com.google.common.collect.Lists;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VisualizationService {


    public mxGraphComponent moduleStructureVisualization(List<Connection> conns) {

        //create the trace graph
        mxGraph graph = new mxGraph() {

            @Override
            public boolean isCellMovable(Object cell) {
                return isCellsMovable() && !isCellLocked(cell) && getModel().isVertex(getModel().getParent(cell));
            }

            @Override
            public boolean isCellConnectable(Object o) {
                //cell not add new edge
                return false;
            }

            @Override
            public boolean isCellEditable(Object o) {
                //cell not editable
                return false;
            }

            @Override
            public boolean isCellBendable(Object o) {
                //edge not bendable
                return false;
            }
        };

        mxGraphComponent graphComponent = new mxGraphComponent(graph);


        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        //create vertexs,
        Map<Integer, Object> vertexsMapping = conns.stream()
                .map(conn -> Lists.newArrayList(conn.getIn().getAfiId(), conn.getOut().getAfiId()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toMap(Function.identity(),
                        id -> graph.insertVertex(parent,
                                null,
                                String.valueOf(id),
                                20,
                                20,
                                60,
                                30)));

        //create connections

        conns.forEach(conn ->
                graph.insertEdge(parent,
                        null,
                        null,
                        vertexsMapping.get(conn.getIn().getAfiId()),
                        vertexsMapping.get(conn.getOut().getAfiId())));


        graph.getModel().endUpdate();


        //define layout
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);

        //using morphing
        graph.getModel().beginUpdate();

        //execute the layout algorithm
        layout.execute(graph.getDefaultParent());
        graph.setDropEnabled(true);


        mxMorphing morphing = new mxMorphing(graphComponent, 20, 1.5, 20);

        morphing.addListener(mxEvent.DONE, (o, mxEventObject) -> {
            //when the morphing finish, then call this method
            System.out.println("done");
            graph.getModel().endUpdate();
        });

        //execute morphing

        morphing.startAnimation();


//graphComponent.getGraphHandler().setRemoveCellsFromParent(false);
        return graphComponent;
    }
}
