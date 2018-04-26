package com.dls.aa.tableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ConnectionTableViewModel {

    public final SimpleIntegerProperty afiid1;
    public final SimpleIntegerProperty afiid2;
    public final SimpleIntegerProperty portid1;
    public final SimpleIntegerProperty portid2;
    public final SimpleStringProperty portName1;
    public final SimpleStringProperty portName2;
    public final SimpleStringProperty type1;
    public final SimpleStringProperty type2;

    public ConnectionTableViewModel(SimpleIntegerProperty afiid1,
                                    SimpleIntegerProperty afiid2,
                                    SimpleIntegerProperty portid1,
                                    SimpleIntegerProperty portid2,
                                    SimpleStringProperty portName1,
                                    SimpleStringProperty portName2,
                                    SimpleStringProperty type1,
                                    SimpleStringProperty type2) {
        this.afiid1 = afiid1;
        this.afiid2 = afiid2;
        this.portid1 = portid1;
        this.portid2 = portid2;
        this.portName1 = portName1;
        this.portName2 = portName2;
        this.type1 = type1;
        this.type2 = type2;
    }

    public String getPortName1() {
        return portName1.get();
    }

    public SimpleStringProperty portName1Property() {
        return portName1;
    }

    public String getPortName2() {
        return portName2.get();
    }

    public SimpleStringProperty portName2Property() {
        return portName2;
    }

    public String getType1() {
        return type1.get();
    }

    public SimpleStringProperty type1Property() {
        return type1;
    }

    public String getType2() {
        return type2.get();
    }

    public SimpleStringProperty type2Property() {
        return type2;
    }
}
