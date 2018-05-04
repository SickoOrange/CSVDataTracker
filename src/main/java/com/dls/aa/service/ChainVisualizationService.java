/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

package com.dls.aa.service;

import com.dls.aa.model.ChainPath;
import com.dls.aa.model.Module;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraph;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChainVisualizationService {

  public static mxGraphComponent load(List<ChainPath> chainPaths,
      Map<Integer, Module> modules, mxGraph graph,
      OnGraphClickListener onGraphClickListener) {

    mxGraphComponent graphComponent = new mxGraphComponent(graph);

    //graph setting
    Object parent = graph.getDefaultParent();
    graph.getModel().beginUpdate();

    for (int i = 0; i < chainPaths.size(); i++) {
      prepareChainPath(chainPaths.get(i), modules, graph, parent, i);
    }

    graph.getModel().endUpdate();

    //autoLayout(graph, graphComponent);
    graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Object cell = graphComponent.getCellAt(e.getX(), e.getY());
        Objects.requireNonNull(onGraphClickListener);
        onGraphClickListener.onChainGraphClick(graph.convertValueToString(cell));
      }
    });

    autoLayout(graph, graphComponent);
    return graphComponent;
  }

  private static void prepareChainPath(ChainPath chainPath,
      Map<Integer, Module> modules, mxGraph graph,
      Object parent, int i) {
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
      Integer afiid = chainsId.get(k);
      String symbol = modules.get(afiid).getSymbol();
      Object vertex = graph
          .insertVertex(parent, null, chainsId.get(k) + "\r\n" + symbol, 20 + 120 * k, 20 + 60 * i,
              80, 30);
      chainVertexs.add(vertex);
    }

    for (int m = 0; m < chainVertexs.size() - 1; m++) {
      graph.insertEdge(parent, null, null, chainVertexs.get(m), chainVertexs.get(m + 1));
    }
  }


  private static void autoLayout(mxGraph graph, mxGraphComponent graphComponent) {
    //define layout
    mxIGraphLayout layout = new mxCompactTreeLayout(graph);

    //using morphing
    graph.getModel().beginUpdate();

    //execute the layout algorithm
    layout.execute(graph.getDefaultParent());

    mxMorphing morphing = new mxMorphing(graphComponent, 20, 1.5, 20);

    morphing.addListener(mxEvent.DONE, (o, mxEventObject) -> {
      //when the morphing finish, then call this method
      System.out.println("Chain Visualization done");
      graph.getModel().endUpdate();
    });

    //execute morphing
    morphing.startAnimation();

  }
}
