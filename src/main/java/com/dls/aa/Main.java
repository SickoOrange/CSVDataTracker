package com.dls.aa;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static Stage stage;
    public static HostServices hostServices;


    @Override
    public void start(Stage primaryStage) throws Exception {

        Main.stage = primaryStage;
        hostServices = getHostServices();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ui/main_stage.fxml"));
        primaryStage.setTitle("DLS Visualization");
        Scene scene = new Scene(root);
        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(Main.class.getResource("/css/stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
