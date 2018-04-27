package com.dls.aa.tableview;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnectionTableViewModel extends RecursiveTreeObject<ConnectionTableViewModel> {

  public final SimpleIntegerProperty afiid1;
  public final SimpleIntegerProperty afiid2;
  public final SimpleIntegerProperty portid1;
  public final SimpleIntegerProperty portid2;

  private final StringProperty portName1;
  private final StringProperty portName2;
  private final StringProperty type1;
  private final StringProperty type2;

  ConnectionTableViewModel(int afiid1,
      int afiid2,
      int portid1,
      int portid2,
      String portName1,
      String portName2,
      String type1,
      String type2) {
    this.afiid1 = new SimpleIntegerProperty(afiid1);
    this.afiid2 = new SimpleIntegerProperty(afiid2);
    this.portid1 = new SimpleIntegerProperty(portid1);
    this.portid2 = new SimpleIntegerProperty(portid2);
    this.portName1 = new SimpleStringProperty(portName1);
    this.portName2 = new SimpleStringProperty(portName2);
    this.type1 = new SimpleStringProperty(type1);
    this.type2 = new SimpleStringProperty(type2);
  }


  public StringProperty portName1Property() {
    return portName1;
  }


  public StringProperty portName2Property() {
    return portName2;
  }


  public StringProperty type1Property() {
    return type1;
  }


  public StringProperty type2Property() {
    return type2;
  }
}
