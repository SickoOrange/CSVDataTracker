package com.dls.aa.tableview;

import com.dls.aa.model.Module;
import com.jfoenix.controls.JFXTreeTableColumn;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;

import java.util.Map;
import java.util.function.Function;

public class TableViewFactory {
    public static <T> void setupAfiTableCellValueFactory(JFXTreeTableColumn<AfiTableViewModule, T> column,
                                                         Function<AfiTableViewModule, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<AfiTableViewModule, T> param) -> {
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

}
