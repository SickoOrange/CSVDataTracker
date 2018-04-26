package com.dls.aa;

import com.dls.aa.main.MainController;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.svg.SVGGlyph;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.container.DefaultFlowContainer;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

  @FXMLViewFlowContext
  private ViewFlowContext flowContext;

  public static final Logger LOGGER = Logger.getLogger(Main.class);

  @Override
  public void start(Stage primaryStage) throws Exception {
    Flow flow = new Flow(WizardController.class);
    DefaultFlowContainer container = new DefaultFlowContainer();

    flowContext = new ViewFlowContext();
    flowContext.register("Stage", primaryStage);
    flow.createHandler(flowContext).start(container);

    JFXDecorator decorator = new JFXDecorator(primaryStage, container.getView());
    decorator.setCustomMaximize(true);
    decorator.setGraphic(new SVGGlyph(""));

    primaryStage.setTitle("DLS CSV Visualization");
    double width = 1280;
    double height = 800;
//    try {
//      Rectangle2D bounds = Screen.getScreens().get(0).getBounds();
//      width = bounds.getWidth() / 2.5;
//      height = bounds.getHeight() / 1.35;
//    } catch (Exception e) {
//    }

    Scene scene = new Scene(decorator, width, height);
    final ObservableList<String> stylesheets = scene.getStylesheets();
    stylesheets.addAll(Main.class.getResource("/css/jfoenix-fonts.css").toExternalForm(),
                       Main.class.getResource("/css/jfoenix-design.css").toExternalForm(),
                       Main.class.getResource("/css/jfoenix-main-demo.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();

  }


  public static void main(String[] args) {
    launch(args);
  }
}
