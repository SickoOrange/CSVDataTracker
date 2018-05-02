package com.dls.aa;

import com.google.common.collect.Maps;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class ServiceContainer {
    private static ServiceContainer ourInstance = new ServiceContainer();


    private static StackPane centerContainerPane;
    private final HashMap<Object, Object> servicesContains;
    private static Map<String, Node> nodeMapping;

    public static ServiceContainer getInstance() {
        return ourInstance;
    }

    private ServiceContainer() {
        servicesContains = Maps.newHashMap();
    }

    public static void setCenterContainerPane(StackPane centerContainer) {
        ServiceContainer.centerContainerPane = centerContainer;
    }

    public static StackPane getCenterContainerPane() {
        return centerContainerPane;
    }

    public static void setNodeMapping(Map<String, Node> nodeMapping) {
        ServiceContainer.nodeMapping = nodeMapping;
    }

    public static Map<String, Node> getNodeMapping() {
        return nodeMapping;
    }

    public HashMap<Object, Object> getServicesMapping() {
        return servicesContains;
    }

    public void addService(String name, Object service) {
        servicesContains.put(name, service);
    }

}
