package com.dls.aa.service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class PropertiesService {

  private static final String propName = "config.properties";





  public static void writeProperties(String key, String value) {
    Properties prop = new Properties();
    OutputStream output = null;

    try {

      output = new FileOutputStream(String.valueOf(PropertiesService.class.getResource(propName)));

      // set the properties value
      prop.setProperty(key, value);
      // save properties to project root folder
      prop.store(output, null);

    } catch (IOException io) {
      io.printStackTrace();
    } finally {
      if (output != null) {
        try {
          output.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    }
  }


  private static Properties getProp() {
    Properties properties = new Properties();
    try {
      properties.load(PropertiesService.class.getResourceAsStream("/" + propName));
      return properties;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public static String readPropValue(String key) {
    Properties prop = getProp();
    return (String) prop.get(key);
  }


}
