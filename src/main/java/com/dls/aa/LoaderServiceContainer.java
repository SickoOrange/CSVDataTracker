package com.dls.aa;

import com.google.common.collect.Maps;

import java.util.HashMap;

public class LoaderServiceContainer {
    private static LoaderServiceContainer ourInstance = new LoaderServiceContainer();
    private final HashMap<Object, Object> servicesContains;

    public static LoaderServiceContainer getInstance() {
        return ourInstance;
    }

    private LoaderServiceContainer() {
        servicesContains = Maps.newHashMap();
    }

    public HashMap<Object, Object> getServicesContains() {
        return servicesContains;
    }

    public void addService(String name, Object service) {
        servicesContains.put(name, service);
    }

}
