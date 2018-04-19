package com.dls.aa;

import com.dls.aa.main.MainController;
import io.datafx.controller.flow.Flow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {


  public static final Logger LOGGER = Logger.getLogger(Main.class);

  @Override
  public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource(
//            "/ChainVisualization.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();

    new Flow(MainController.class).startInStage(primaryStage);

  }


  public static void main(String[] args) {
    launch(args);
  }
}
