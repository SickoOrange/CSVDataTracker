package com.dls.aa;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import org.gillius.jfxutils.EventHandlerManager;
import org.gillius.jfxutils.chart.AxisConstraint;
import org.gillius.jfxutils.chart.AxisConstraintStrategies;
import org.gillius.jfxutils.chart.AxisConstraintStrategy;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.ChartZoomManager;
import org.gillius.jfxutils.chart.DefaultChartInputContext;
import org.gillius.jfxutils.chart.XYChartInfo;

public class CustomChartPanManager {

  /**
   * The default mouse filter for the {@link ChartPanManager} filters events unless only primary
   * mouse button (usually left) is depressed.
   */
  public static final EventHandler<MouseEvent> DEFAULT_FILTER = ChartZoomManager.DEFAULT_FILTER;

  private final EventHandlerManager handlerManager;

  private final ValueAxis<?> xAxis;
  private final ValueAxis<?> yAxis;
  private final XYChartInfo chartInfo;
  private final XYChart<?, ?> subChart;

  private AxisConstraint panMode = AxisConstraint.None;
  private AxisConstraintStrategy axisConstraintStrategy = AxisConstraintStrategies.getDefault();

  private EventHandler<? super MouseEvent> mouseFilter = DEFAULT_FILTER;

  private boolean dragging = false;

  private boolean wasXAnimated;
  private boolean wasYAnimated;

  private double lastX;
  private double lastY;

  public CustomChartPanManager(XYChart<?, ?> chart, XYChart<?, ?> subChart) {

    this.subChart = subChart;
    //super(chart);
    handlerManager = new EventHandlerManager(chart);
    xAxis = (ValueAxis<?>) chart.getXAxis();
    yAxis = (ValueAxis<?>) chart.getYAxis();
    chartInfo = new XYChartInfo(chart, chart);

    handlerManager.addEventHandler(false, MouseEvent.DRAG_DETECTED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (passesFilter(mouseEvent)) {
          startDrag(mouseEvent);
        }
      }
    });

    handlerManager.addEventHandler(false, MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        drag(mouseEvent);
      }
    });

    handlerManager
        .addEventHandler(false, MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            release();
          }
        });
  }

  /**
   * Returns the current strategy in use.
   *
   * @see #setAxisConstraintStrategy(AxisConstraintStrategy)
   */
  public AxisConstraintStrategy getAxisConstraintStrategy() {
    return axisConstraintStrategy;
  }

  /**
   * Sets the {@link AxisConstraintStrategy} to use, which determines which axis is allowed for panning. The default
   * implementation is {@link AxisConstraintStrategies#getDefault()}.
   *
   * @see AxisConstraintStrategies
   */
  public void setAxisConstraintStrategy(AxisConstraintStrategy axisConstraintStrategy) {
    this.axisConstraintStrategy = axisConstraintStrategy;
  }

  /**
   * Returns the mouse filter.
   *
   * @see #setMouseFilter(EventHandler)
   */
  public EventHandler<? super MouseEvent> getMouseFilter() {
    return mouseFilter;
  }

  /**
   * Sets the mouse filter for starting the pan action. If the filter consumes the event with
   * {@link Event#consume()}, then the event is ignored. If the filter is null, all events are
   * passed through. The default filter is {@link #DEFAULT_FILTER}.
   */
  public void setMouseFilter(EventHandler<? super MouseEvent> mouseFilter) {
    this.mouseFilter = mouseFilter;
  }

  public void start() {
    handlerManager.addAllHandlers();
  }

  public void stop() {
    handlerManager.removeAllHandlers();
    release();
  }

  private boolean passesFilter(MouseEvent event) {
    if (mouseFilter != null) {
      MouseEvent cloned = (MouseEvent) event.clone();
      mouseFilter.handle(cloned);
      if (cloned.isConsumed()) {
        return false;
      }
    }

    return true;
  }

  private void startDrag(MouseEvent event) {
    DefaultChartInputContext context = new DefaultChartInputContext(chartInfo, event.getX(),
        event.getY());
    panMode = axisConstraintStrategy.getConstraint(context);

    if (panMode != AxisConstraint.None) {
      lastX = event.getX();
      lastY = event.getY();

      wasXAnimated = xAxis.getAnimated();
      wasYAnimated = yAxis.getAnimated();

      xAxis.setAnimated(false);
      xAxis.setAutoRanging(false);
      yAxis.setAnimated(false);
      yAxis.setAutoRanging(false);

      dragging = true;
    }
  }

  private void drag(MouseEvent event) {
    if (!dragging) {
      return;
    }

    if (panMode == AxisConstraint.Both || panMode == AxisConstraint.Horizontal) {
      double dX = (event.getX() - lastX) / -xAxis.getScale();
      lastX = event.getX();
      xAxis.setAutoRanging(false);
      xAxis.setLowerBound(xAxis.getLowerBound() + dX);
      xAxis.setUpperBound(xAxis.getUpperBound() + dX);

      //sub chart
      ValueAxis<?> xAxis1 = (ValueAxis<?>) subChart.getXAxis();
      xAxis1.setAutoRanging(false);
      xAxis1.setLowerBound(xAxis.getLowerBound() + dX);
      xAxis1.setUpperBound(xAxis.getUpperBound() + dX);
    }

    if (panMode == AxisConstraint.Both || panMode == AxisConstraint.Vertical) {
      double dY = (event.getY() - lastY) / -yAxis.getScale();
      lastY = event.getY();
      yAxis.setAutoRanging(false);
      yAxis.setLowerBound(yAxis.getLowerBound() + dY);
      yAxis.setUpperBound(yAxis.getUpperBound() + dY);
    }
  }

  private void release() {
    if (!dragging) {
      return;
    }

    dragging = false;

    xAxis.setAnimated(wasXAnimated);
    yAxis.setAnimated(wasYAnimated);
  }
}
