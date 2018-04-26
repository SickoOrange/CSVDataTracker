package com.dls.aa.tableview;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AfiTableViewModule extends RecursiveTreeObject<AfiTableViewModule> {
    public final SimpleIntegerProperty afiId;
    public final SimpleIntegerProperty nodeId;
    public final SimpleIntegerProperty afiTypeId;

    final StringProperty symbol;
    final StringProperty name;

    public AfiTableViewModule(int afiId,
                              int nodeId, int afiTypeId,
                              String symbol, String name) {
        this.afiId = new SimpleIntegerProperty(afiId);
        this.nodeId = new SimpleIntegerProperty(nodeId);
        this.afiTypeId = new SimpleIntegerProperty(afiTypeId);
        this.symbol = new SimpleStringProperty(symbol);
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty symbolProperty() {
        return symbol;
    }
}
