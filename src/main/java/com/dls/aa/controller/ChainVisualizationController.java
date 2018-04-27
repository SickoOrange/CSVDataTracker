package com.dls.aa.controller;

import static com.dls.aa.WizardController.CP_LOADER;
import static com.dls.aa.WizardController.LOADER;
import static com.dls.aa.tableview.TableViewFactory.setupTableCellValueFactory;

import com.dls.aa.LoaderServiceContainer;
import com.dls.aa.loader.CSVLoader;
import com.dls.aa.model.ChainPath;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.dls.aa.model.Port;
import com.dls.aa.service.ChainPathLoader;
import com.dls.aa.service.OnChainPathVertexClickListener;
import com.dls.aa.tableview.AfiTableViewModule;
import com.dls.aa.tableview.TableViewFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.mxgraph.swing.mxGraphComponent;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.action.ActionMethod;
import io.datafx.controller.flow.action.ActionTrigger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;

import javax.annotation.PostConstruct;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;


@ViewController("/fxml/ui/chain_visualization_layout.fxml")
public class ChainVisualizationController implements OnChainPathVertexClickListener {

    public static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
            .getLogger(ChainVisualizationController.class);

    @FXML
    @ActionTrigger("loadConnection")
    private JFXButton loadConnection;

    @FXML
    private JFXTextField alertText;


    @FXML
    private JFXTextField sourceText;

    @FXML
    private StackPane visualizationPane;

    @FXML
    private JFXRadioButton radio_1;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private JFXRadioButton radio_2;


    //read only table view
    @FXML
    private JFXTreeTableView<AfiTableViewModule> moduleTreeTableView;
    @FXML
    private JFXTreeTableColumn<AfiTableViewModule, Integer> afiidColumn;
    @FXML
    private JFXTreeTableColumn<AfiTableViewModule, Integer> nodeidColumn;
    @FXML
    private JFXTreeTableColumn<AfiTableViewModule, Integer> afitypeidColumn;
    @FXML
    private JFXTreeTableColumn<AfiTableViewModule, String> symbolColumn;
    @FXML
    private JFXTreeTableColumn<AfiTableViewModule, String> nameColumn;


    private List<Connection> connections;
    private ChainPathLoader chainPathLoader;
    private CSVLoader csvLoader;

    private Map<Integer, Module> modules;


    @ActionMethod("loadConnection")
    public void loadConnection() {
        System.out.println("load connection for alert and source afi id");

        String alertInfo = alertText.getText();
        String sourceInfo = this.sourceText.getText();

        Task<List<ChainPath>> searchChainPaths = new Task<List<ChainPath>>() {
            @Override
            protected List<ChainPath> call() throws IOException {

                int alertAfi = 0;
                int sourceAfi = 0;
                if (radio_1.isSelected()) {
                    alertAfi = Integer.parseInt(alertInfo);
                    sourceAfi = Integer.parseInt(sourceInfo);
                }

                if (radio_2.isSelected()) {

                    //convert port unique name to afiid, if necessary
                    List<String> notParsableList = Lists.newArrayList();
                    notParsableList.add(alertInfo);
                    notParsableList.add(sourceInfo);
                    Map<String, Integer> result = Maps.transformValues(chainPathLoader
                            .findPortsFromPortUniqueNames(csvLoader, notParsableList), Port::getAfiId);

                    alertAfi = result.get(alertInfo);
                    sourceAfi = result.get(sourceInfo);
                }

                if (Objects.isNull(connections)) {
                    connections = csvLoader.loadConnections(null).collect(Collectors.toList());

                }
                return chainPathLoader
                        .loadChainPaths(alertAfi, sourceAfi, connections);
            }
        };

        searchChainPaths.setOnSucceeded(e -> {
            //data visualization
            visualization(searchChainPaths.getValue());
            //query module info
//            QueryModuleTask queryModuleTask = new QueryModuleTask(searchChainPaths.getValue(), csvLoader);
//            queryModuleTask.setOnSucceeded(e1 -> {
//                System.out.println("loading module finish");
//                modules = queryModuleTask.getValue();
//                System.out.println(modules);
//                //init table
//                setUpReadOnlyTableView(modules);
//            });
//            new Thread(queryModuleTask).start();

            Set<Integer> relevantIds = searchChainPaths.getValue().stream()
                    .map(ChainPath::toList)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toSet());
            try {
                Map<Integer, Module> modules = csvLoader
                        .loadModules(line -> relevantIds.contains(Module.extractId(line)));
                setUpReadOnlyTableView(modules);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        });
        new Thread(searchChainPaths).start();


    }

    private void setUpReadOnlyTableView(Map<Integer, Module> modules) {
        System.out.println("start set up tableview ");
        setupTableCellValueFactory(afiidColumn, m -> m.afiId.asObject());
        setupTableCellValueFactory(nodeidColumn, p -> p.nodeId.asObject());
        setupTableCellValueFactory(afitypeidColumn, p -> p.afiTypeId.asObject());
        setupTableCellValueFactory(symbolColumn, AfiTableViewModule::symbolProperty);
        setupTableCellValueFactory(nameColumn, AfiTableViewModule::nameProperty);

        ObservableList<AfiTableViewModule> entities = TableViewFactory.collectAfiEntities().apply(modules);
        System.out.println(entities.size());

        moduleTreeTableView
                .setRoot(new RecursiveTreeItem<>(entities, RecursiveTreeObject::getChildren));

        moduleTreeTableView.setShowRoot(false);
        moduleTreeTableView.getSelectionModel().select(2);


    }

    private void visualization(List<ChainPath> chainPaths) {
        visualizationPane.getChildren().clear();
        SwingNode swingNode = new SwingNode();
        mxGraphComponent gComponent = chainPathLoader.chainVisualization(chainPaths);
        SwingUtilities
                .invokeLater(() -> swingNode.setContent(gComponent));
        visualizationPane.getChildren().add(swingNode);
    }


    @PostConstruct
    public void init() {
        csvLoader = (CSVLoader) LoaderServiceContainer.getInstance().getServicesContains().get(LOADER);
        chainPathLoader = (ChainPathLoader) LoaderServiceContainer.getInstance().getServicesContains().get(CP_LOADER);
        chainPathLoader.setOnChainPathVertexClickListener(this);
    }

    @Override
    public void onChainPathVertexClick(String vertexString) {
        if (StringUtils.isNumeric(vertexString)) {
            updateSelectedIndexInTable(moduleTreeTableView, Integer.parseInt(vertexString));
        }

    }

    class QueryModuleTask extends Task<Map<Integer, Module>> {

        private final Set<Integer> relevantIds;
        private CSVLoader csvLoader;

        QueryModuleTask(List<ChainPath> chainPaths, CSVLoader csvLoader) {
            relevantIds = chainPaths.stream()
                    .map(ChainPath::toList)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toSet());


            this.csvLoader = csvLoader;
        }

        @Override
        protected Map<Integer, Module> call() throws Exception {
            return csvLoader
                    .loadModules(line -> relevantIds.contains(Module.extractId(line)));
        }
    }


//    private <T> void setupCellValueFactory(JFXTreeTableColumn<AfiTableViewModule, T> column,
//                                           Function<AfiTableViewModule, ObservableValue<T>> mapper) {
//        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<AfiTableViewModule, T> param) -> {
//            if (column.validateValue(param)) {
//                return mapper.apply(param.getValue().getValue());
//            } else {
//                return column.getComputedValue(param);
//            }
//        });
//    }

    private void updateSelectedIndexInTable(JFXTreeTableView<AfiTableViewModule> moduleTreeTableView,
                                            int afiid) {

        ObservableList<TreeItem<AfiTableViewModule>> children = moduleTreeTableView.getRoot()
                .getChildren();
        Optional<TreeItem<AfiTableViewModule>> treeItem = children.stream()
                .filter(child -> child.getValue().afiId.isEqualTo(afiid).get()).findAny();
        treeItem.ifPresent(treeTableViewModuleTreeItem -> {
            moduleTreeTableView.getSelectionModel()
                    .clearAndSelect(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
            moduleTreeTableView.scrollTo(moduleTreeTableView.getRow(treeTableViewModuleTreeItem));
        });


    }
}
