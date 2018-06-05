package com.dls.aa.loader;

import com.dls.aa.model.ChainPath;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.dls.aa.service.ChainVisualizationService;
import com.dls.aa.service.ModuleStructureVisualizationService;
import com.dls.aa.service.OnGraphClickListener;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class VisualizationLoader {

  private OnGraphClickListener onGraphClickListener;

  public void setOnChainPathVertexClickListener(
      OnGraphClickListener onGraphClickListener) {
    this.onGraphClickListener = onGraphClickListener;
  }


  public mxGraphComponent chainVisualization(List<ChainPath> chainPaths,
      Map<Integer, Module> modules) {
    //create the trace graph
    mxGraph graph = initialGraph();
    return ChainVisualizationService.load(chainPaths, modules,graph, onGraphClickListener);
  }


  public mxGraphComponent moduleStructureVisualization(Set<Connection> conns) {
    mxGraph graph = initialGraph();
   return ModuleStructureVisualizationService.load(conns,graph,onGraphClickListener);
  }

  private mxGraph initialGraph() {
    //create the trace graph
    return new mxGraph() {

      @Override
      public boolean isCellMovable(Object cell) {
        return isCellsMovable() && !isCellLocked(cell) && getModel()
            .isVertex(getModel().getParent(cell));
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
  }
}
