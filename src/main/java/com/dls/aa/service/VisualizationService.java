package com.dls.aa.service;

import com.google.common.collect.Lists;
import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import sun.reflect.ReflectionFactory;

import java.util.List;

public class VisualizationService {

    public mxGraphComponent netWorkVisualization() {


        //create the trace graph
        mxGraph graph = new mxGraph();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);


        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();


        Object o1 = graph
                .insertVertex(parent, null, "o1", 20, 20, 80, 30);
        Object o2 = graph
                .insertVertex(parent, null, "o2", 20, 20, 80, 30);
        Object o3 = graph
                .insertVertex(parent, null, "o3", 20, 20, 80, 30);
        Object o4 = graph
                .insertVertex(parent, null, "o4", 20, 20, 80, 30);
        Object o5 = graph
                .insertVertex(parent, null, "o5", 20, 20, 80, 30);

        Object o6 = graph
                .insertVertex(parent, null, "o6", 20, 20, 80, 30);

        Object o7 = graph
                .insertVertex(parent, null, "o7", 20, 20, 80, 30);

        graph.insertEdge(parent, null, null, o1, o2);
        graph.insertEdge(parent, null, null, o1, o3);
        graph.insertEdge(parent, null, null, o1, o6);
        graph.insertEdge(parent, null, null, o1, o7);
        graph.insertEdge(parent, null, null, o3, o4);
        graph.insertEdge(parent, null, null, o3, o5);
        graph.insertEdge(parent, null, null, o2, o3);


        graph.getModel().endUpdate();

        //define layout
        mxIGraphLayout layout = new mxHierarchicalLayout(graph);

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


        return graphComponent;
    }


}
