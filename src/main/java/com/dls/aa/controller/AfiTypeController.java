package com.dls.aa.controller;

import com.dls.aa.AsyncTaskContainer;
import com.dls.aa.ServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.AfiType;
import com.dls.aa.tableview.AfiTypeTableViewModel;
import com.dls.aa.tableview.TableViewFactory;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

public class AfiTypeController implements Initializable {

    @FXML
    private JFXTextField searchModuleIdAfiTypeIdButton;

    @FXML
    private JFXTextField searchModuleNameButton;
    @FXML
    private JFXTreeTableView<AfiTypeTableViewModel> afiTypeTable;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column11;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column12;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column13;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column14;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column15;
    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column16;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, Integer> column17;

    @FXML
    private JFXTreeTableColumn<AfiTypeTableViewModel, String> column18;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CSVLoader csvLoader = (CSVLoader) ServiceContainer.getInstance().getServicesContains().get(DashBoardController.LOADER);

        searchModuleIdAfiTypeIdButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                int typeId = Integer.parseInt(searchModuleIdAfiTypeIdButton.getText());

                AsyncTaskContainer<Integer, List<AfiType>> collectModulesByAfiTypeId = new AsyncTaskContainer<>(typeId, id -> {
                    try {
                        return csvLoader
                                .loadAfiType(line -> AfiType.extractTypeId(line) == id).collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                    }
                });
                collectModulesByAfiTypeId.setOnSucceeded(e -> loadAfiTypesToTableView(collectModulesByAfiTypeId.getValue()));
                new Thread(collectModulesByAfiTypeId).start();

            }
        });
        searchModuleNameButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String moduleName = searchModuleNameButton.getText();
                AsyncTaskContainer<String, List<AfiType>> collectModulesByName = new AsyncTaskContainer<>(moduleName, name -> {
                    try {
                        return csvLoader
                                .loadAfiType(line -> AfiType.extractName(line).equals(name)).collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                    }
                });
                collectModulesByName.setOnSucceeded(e -> loadAfiTypesToTableView(collectModulesByName.getValue()));
                new Thread(collectModulesByName).start();
            }
        });

    }

    private void loadAfiTypesToTableView(List<AfiType> afiTypes) {
        setupTableCellValueFactory(column11, afiType -> afiType.typeid.asObject());
        setupTableCellValueFactory(column12, AfiTypeTableViewModel::nameProperty);
        setupTableCellValueFactory(column13, afiType -> afiType.port.asObject());
        setupTableCellValueFactory(column14, AfiTypeTableViewModel::portNameProperty);

        setupTableCellValueFactory(column15, AfiTypeTableViewModel::typeProperty);
        setupTableCellValueFactory(column16, AfiTypeTableViewModel::isAlarmProperty);
        setupTableCellValueFactory(column17, afiType -> afiType.alarm.asObject());
        setupTableCellValueFactory(column18, AfiTypeTableViewModel::descriptionProperty);

        ObservableList<AfiTypeTableViewModel> afiTypeEntities = TableViewFactory
                .collectAfiTypeEntities()
                .apply(afiTypes);

        afiTypeTable.setRoot(new RecursiveTreeItem<>(afiTypeEntities,
                RecursiveTreeObject::getChildren));
        afiTypeTable.setShowRoot(false);
    }
}
