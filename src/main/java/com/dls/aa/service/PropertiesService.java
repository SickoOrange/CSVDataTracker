package com.dls.aa.service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class PropertiesService {

    private static final String propName = "config.properties";


    private static File getResourceFile() {
        URL resource = PropertiesService.class.getClassLoader().getResource(propName);
        try {
            return Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void writeProperties(String key, String value) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream(Objects.requireNonNull(getResourceFile()).getAbsoluteFile());

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
            properties.load(new FileReader(Objects.requireNonNull(getResourceFile())));
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
