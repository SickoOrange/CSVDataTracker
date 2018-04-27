package com.dls.aa.tableview;

import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.jfoenix.controls.JFXTreeTableColumn;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;

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
    return connections -> {
      ObservableList<ConnectionTableViewModel> entities = FXCollections.observableArrayList();
      connections
          .forEach(
              connection -> entities.add(new ConnectionTableViewModel(connection.getOut().getAfiId(),
                  connection.getIn().getAfiId(),
                  connection.getOut().getPortId(),
                  connection.getIn().getPortId(),
                  connection.getPortName1(),
                  connection.getPortName2(),
                  connection.getPortType1(),
                  connection.getPortType2())));
      return entities;

    };
  }

}
