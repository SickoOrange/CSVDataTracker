package com.dls.aa.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class PropertiesService {

  private static final String propName = "config.properties";

  private static String getRealPath() {
    try {
      String jarPath = PropertiesService.class.getProtectionDomain().getCodeSource().getLocation()
          .toURI().getPath();
      return new File(jarPath).getParent();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }


  public static void writeProperties(String key, String value) {
    Properties prop = new Properties();
    OutputStream output = null;

    try {
      String path = getRealPath();
      System.out.println(path);
      output = new FileOutputStream(path + "/" + propName);
      // set the properties value
      prop.setProperty(key, value);
      // save properties to project root folder
      prop.store(output, null);

    } catch (IOException e) {
      e.printStackTrace();
    }


  }


  private static Properties getProp() {
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream(getRealPath() + "/" + propName));
      return properties;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static String readPropValue(String key) {
    Properties prop = getProp();
    if (Objects.isNull(prop)) {
      return "No Path";
    }
    return (String) prop.get(key);
  }


}
