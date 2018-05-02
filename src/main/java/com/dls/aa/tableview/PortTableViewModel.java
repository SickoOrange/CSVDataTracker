package com.dls.aa.tableview;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PortTableViewModel extends RecursiveTreeObject<PortTableViewModel> {

  public final SimpleIntegerProperty id;
  public final SimpleIntegerProperty afiid;
  public final StringProperty name;
  public final StringProperty symbol;
  public final StringProperty parameter;
  public final StringProperty uniqueName;
  public final StringProperty direction;
  public final StringProperty isArchive;
  public final StringProperty isAlarm;
  public final SimpleIntegerProperty alarmtypeid;
  public final StringProperty abbrev;

  public PortTableViewModel(int id,
      int afiid,
      String name,
      String symbol,
      String parameter,
      String uniqueName,
      String direction,
      String isArchive,
      String isAlarm, Integer alarmtypeid, String abbrev) {
    this.id = new SimpleIntegerProperty(id);
    this.afiid = new SimpleIntegerProperty(afiid);
    this.name = new SimpleStringProperty(name);
    this.symbol = new SimpleStringProperty(symbol);
    this.parameter = new SimpleStringProperty(parameter);
    this.uniqueName = new SimpleStringProperty(uniqueName);
    this.direction = new SimpleStringProperty(direction);
    this.isArchive = new SimpleStringProperty(isArchive);
    this.isAlarm = new SimpleStringProperty(isAlarm);
    this.alarmtypeid = new SimpleIntegerProperty(alarmtypeid);
    this.abbrev = new SimpleStringProperty(abbrev);
  }


  public StringProperty nameProperty() {
    return name;
  }


  public StringProperty symbolProperty() {
    return symbol;
  }


  public StringProperty parameterProperty() {
    return parameter;
  }


  public StringProperty uniqueNameProperty() {
    return uniqueName;
  }


  public StringProperty directionProperty() {
    return direction;
  }


  public StringProperty isArchiveProperty() {
    return isArchive;
  }


  public StringProperty isAlarmProperty() {
    return isAlarm;
  }


  public StringProperty abbrevProperty() {
    return abbrev;
  }
}
