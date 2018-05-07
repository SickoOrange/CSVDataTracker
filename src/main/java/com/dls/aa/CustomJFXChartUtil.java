package com.dls.aa;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import org.gillius.jfxutils.JFXUtil;
import org.gillius.jfxutils.chart.AxisConstraintStrategy;
import org.gillius.jfxutils.chart.ChartZoomManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

public class CustomJFXChartUtil extends JFXChartUtil {

  public static Region setupZooming(XYChart<?, ?> chart,
      EventHandler<? super MouseEvent> mouseFilter, AxisConstraintStrategy mouseWheelAxisConstrainY,
      AxisConstraintStrategy axisConstraint) {
    StackPane chartPane = new StackPane();

    if (chart.getParent() != null) {
      JFXUtil.replaceComponent(chart, chartPane);
    }

    Rectangle selectRect = new Rectangle(0, 0, 0, 0);
    selectRect.setFill(Color.DODGERBLUE);
    selectRect.setMouseTransparent(true);
    selectRect.setOpacity(0.3);
    selectRect.setStroke(Color.rgb(0, 0x29, 0x66));
    selectRect.setStrokeType(StrokeType.INSIDE);
    selectRect.setStrokeWidth(3.0);
    StackPane.setAlignment(selectRect, Pos.TOP_LEFT);

    chartPane.getChildren().addAll(chart, selectRect);

    ChartZoomManager zoomManager = new ChartZoomManager(chartPane, selectRect, chart);
    zoomManager.setMouseWheelAxisConstraintStrategy(mouseWheelAxisConstrainY);
    zoomManager.setAxisConstraintStrategy(axisConstraint);
    zoomManager.setMouseFilter(mouseFilter);
    zoomManager.start();
    return chartPane;
  }

}
