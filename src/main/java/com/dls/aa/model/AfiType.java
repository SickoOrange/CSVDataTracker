/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */
package com.dls.aa.model;

import static org.apache.commons.lang3.math.NumberUtils.toInt;

import com.dls.aa.parser.CsvStringToBooleanConverter;

public class AfiType {
  //TypeId	Name	Port	PortName	Type	IsAlarm	Alarm	Description

  public static final int TYPE_ID_COLUMN_INDEX = 0;
  public static final int NAME_COLUMN_INDEX = 1;
  public static final int PORT_COLUMN_INDEX = 2;
  public static final int PORT_NAME_COLUMN_INDEX = 3;
  public static final int TYPE_COLUMN_INDEX = 4;
  public static final int IS_ALARM_COLUMN_INDEX = 4;
  public static final int ALARM_COLUMN_INDEX = 4;
  public static final int DESCRIPTION_COLUMN_INDEX = 4;

  private int typeid;
  private String name;
  private int port;
  private String portname;
  private String type;
  private boolean isAlarm;
  private int alarm;
  private String description;

  public static int extractTypeId(String[] line) {
    return toInt(line[TYPE_ID_COLUMN_INDEX]);
  }

  public static String extractName(String[] line) {
    return line[NAME_COLUMN_INDEX];
  }

  public static int extractPort(String[] line) {
    return toInt(line[PORT_COLUMN_INDEX]);
  }

  public static String extractPortName(String[] line) {
    return line[PORT_NAME_COLUMN_INDEX];
  }

  public static String extractType(String[] line) {
    return line[TYPE_COLUMN_INDEX];
  }

  public static boolean extractIsAlarm(String[] line) {
    return CsvStringToBooleanConverter.toBoolean(line[IS_ALARM_COLUMN_INDEX]);
  }

  public static int extractAlarm(String[] line) {
    return toInt(line[ALARM_COLUMN_INDEX]);
  }

  public static String extractDescription(String[] line) {
    return line[DESCRIPTION_COLUMN_INDEX];
  }

  public AfiType(int typeid, String name, int port, String portname, String type, boolean isAlarm,
      int alarm, String description) {
    this.typeid = typeid;
    this.name = name;
    this.port = port;
    this.portname = portname;
    this.type = type;
    this.isAlarm = isAlarm;
    this.alarm = alarm;
    this.description = description;
  }
}
