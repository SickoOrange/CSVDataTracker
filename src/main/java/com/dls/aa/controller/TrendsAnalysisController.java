package com.dls.aa.controller;

import com.dls.aa.AsyncTaskContainer;
import com.dls.aa.CustomJFXChartUtil;
import com.dls.aa.ServiceContainer;
import com.dls.aa.ZoomManager;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.BinaryTrend;
import com.dls.aa.model.Trend;
import com.google.common.collect.ImmutableSet;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.math.NumberUtils;
import org.gillius.jfxutils.chart.AxisConstraint;
import org.gillius.jfxutils.chart.AxisConstraintStrategy;
import org.gillius.jfxutils.chart.ChartInputContext;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

import static com.dls.aa.controller.DashBoardController.LOADER;

public class TrendsAnalysisController implements Initializable {

  @FXML
  private TextField portNameTf;

  @FXML
  private LineChart<Number, Number> trendsChart;


  @FXML
  private StackPane chartContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //disable dots
    trendsChart.setCreateSymbols(false);
    trendsChart.getYAxis().setAutoRanging(false);
    NumberAxis yAxis = (NumberAxis) trendsChart.getYAxis();
    yAxis.setUpperBound(2.0);
    yAxis.setLowerBound(0.0);
    NumberAxis xAxis= (NumberAxis) trendsChart.getXAxis();
    xAxis.setTickUnit(1);

    CSVLoader csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesMapping()
        .get(LOADER);
    portNameTf.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        String uniqueName = portNameTf.getText();
        System.out.println("start to load binary trends by specified port unique name");
        AsyncTaskContainer<Set<String>, Map<String, BinaryTrend>> binaryTrendsTask = new AsyncTaskContainer<>(
            ImmutableSet.of(uniqueName), nameList -> {
          try {
            return csvLoader.loadBinaryTrends(nameList);
          } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
          }
        });
        binaryTrendsTask.setOnSucceeded(e -> {
          Map<String, BinaryTrend> binaryTrendMap = binaryTrendsTask.getValue();
          System.out.println("loading binary trends finish, result size:" + binaryTrendMap.size());
          bindBinaryTrendToChart(trendsChart, binaryTrendMap, uniqueName);

        });
        new Thread(binaryTrendsTask).start();
      }
    });
  }

  private void bindBinaryTrendToChart(LineChart<Number, Number> trendsChart,
      Map<String, BinaryTrend> binaryTrendMap, String uniqueName) {
    BinaryTrend binaryTrend = binaryTrendMap.get(uniqueName);
    Objects.requireNonNull(binaryTrend);
    List<Trend> trends = binaryTrend.getTrends();
    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    series.setName(uniqueName);
    trends.forEach(trend -> {
      if (trend.getValue() == 1) {
        series.getData().add(new XYChart.Data<>(trend.getMillis(), 0));
        series.getData().add(new XYChart.Data<>(trend.getMillis(), 1));
      } else {
        series.getData().add(new XYChart.Data<>(trend.getMillis(), 1));
        series.getData().add(new XYChart.Data<>(trend.getMillis(), 0));
      }
    });
    trendsChart.getData().add(series);

    trendsChart.setOnMouseMoved(mouseEvent -> {
      double xStart = trendsChart.getXAxis().getLocalToParentTransform().getTx();
      double axisXRelativeMousePosition = mouseEvent.getX() - xStart;
      System.out.println("hello: info -> " + xStart + " " + axisXRelativeMousePosition);

    });

    //Panning works via either secondary (right) mouse or primary with ctrl held down
    ChartPanManager panner = new ChartPanManager(trendsChart);
    panner.setAxisConstraintStrategy(new AxisConstraintStrategy() {
      @Override
      public AxisConstraint getConstraint(ChartInputContext context) {
        return AxisConstraint.Horizontal;
      }
    });
    panner.setMouseFilter(mouseEvent -> {
      if (mouseEvent.getButton() == MouseButton.SECONDARY ||
          (mouseEvent.getButton() == MouseButton.PRIMARY &&
              mouseEvent.isShortcutDown())) {
        //let it through
      } else {
        mouseEvent.consume();
      }
    });
    panner.start();

    //Zooming works only via primary mouse button without ctrl held down

    AxisConstraintStrategy axisConstraintStrategy = context -> AxisConstraint.Horizontal;

    AxisConstraintStrategy mouseWheelAxisConstraintStrategy = context -> AxisConstraint.Horizontal;
    CustomJFXChartUtil.setupZooming(trendsChart, mouseEvent -> {
      if (mouseEvent.getButton() != MouseButton.PRIMARY ||
          mouseEvent.isShortcutDown()) {
        mouseEvent.consume();
      }
    }, mouseWheelAxisConstraintStrategy, axisConstraintStrategy);

    CustomJFXChartUtil.addDoublePrimaryClickAutoRangeHandler(trendsChart);


  }


}
