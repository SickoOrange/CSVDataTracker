package com.dls.aa.tableview;/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AfiTypeTableViewModel extends RecursiveTreeObject<AfiTypeTableViewModel> {

  public final SimpleIntegerProperty typeid;
  public final StringProperty name;
  public final SimpleIntegerProperty port;
  public final StringProperty portName;
  public final StringProperty type;
  public final StringProperty isAlarm;
  public final SimpleIntegerProperty alarm;
  public final StringProperty description;


  public AfiTypeTableViewModel(int typeid,
      String name, int port, String portName,
      String type, String isAlarm, int alarm,
      String description) {
    this.typeid = new SimpleIntegerProperty(typeid);
    this.name = new SimpleStringProperty(name);
    this.port = new SimpleIntegerProperty(port);
    this.portName = new SimpleStringProperty(portName);
    this.type = new SimpleStringProperty(type);
    this.isAlarm = new SimpleStringProperty(isAlarm);
    this.alarm = new SimpleIntegerProperty(alarm);
    this.description = new SimpleStringProperty(description);
  }


  public StringProperty nameProperty() {
    return name;
  }


  public StringProperty portNameProperty() {
    return portName;
  }


  public StringProperty typeProperty() {
    return type;
  }


  public StringProperty isAlarmProperty() {
    return isAlarm;
  }

  public StringProperty descriptionProperty() {
    return description;
  }
}
