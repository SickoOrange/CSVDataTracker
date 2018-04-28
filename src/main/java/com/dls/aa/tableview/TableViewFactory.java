package com.dls.aa.tableview;

import com.dls.aa.model.AfiType;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.dls.aa.model.Port;
import com.jfoenix.controls.JFXTreeTableColumn;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;

import static java.util.stream.Collectors.toList;

public class TableViewFactory {

    public static <U, T> void setupTableCellValueFactory(JFXTreeTableColumn<U, T> column,
                                                         Function<U, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<U, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }


    public static Function<Map<Integer, Module>, ObservableList<AfiTableViewModule>> collectAfiEntities() {
        return modules -> {
            ObservableList<AfiTableViewModule> entities = FXCollections.observableArrayList();
            modules.values().forEach(module -> entities
                    .add(new AfiTableViewModule(module.getId(), module.getNode(), module.getAfiTypeId(),
                            module.getSymbol(), module.getName())));
            return entities;
        };
    }

    public static Function<List<Connection>, ObservableList<ConnectionTableViewModel>> collectConnectionEntities() {

        return connections -> connections.stream().map(connection -> new ConnectionTableViewModel(connection.getOut().getAfiId(),
                connection.getIn().getAfiId(),
                connection.getOut().getPortId(),
                connection.getIn().getPortId(),
                connection.getPortName1(),
                connection.getPortName2(),
                connection.getPortType1(),
                connection.getPortType2())).collect(Collectors.collectingAndThen(toList(), FXCollections::observableArrayList));

    }

    public static Function<List<AfiType>, ObservableList<AfiTypeTableViewModel>> collectAfiTypeEntities() {
        return afiTypes -> afiTypes.stream()
                .map(afiType -> new AfiTypeTableViewModel(afiType.getTypeid(), afiType.getName(), afiType.getPort(),
                        afiType.getPortname(), afiType.getType(),
                        Boolean.toString(afiType.isAlarm()), afiType.getAlarm(), afiType.getDescription()))
                .collect(Collectors.collectingAndThen(toList(), FXCollections::observableArrayList));
    }

    public static Function<List<Port>, ObservableList<PortTableViewModel>> collectPortEntities() {
        return ports -> ports.stream()
                .map(port -> new PortTableViewModel(port.getId(),
                        port.getAfiId(), port.getName(),
                        port.getSymbol(),
                        port.getParameter(),
                        port.getUniqueName(),
                        port.getDirection().name(),
                        Boolean.toString(port.isArchive()),
                        Boolean.toString(port.isAlarm())))
                .collect(Collectors.collectingAndThen(toList(), FXCollections::observableArrayList));
    }
}
