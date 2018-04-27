package com.dls.aa;

import javafx.concurrent.Task;

import java.util.function.Function;

public class AsyncTaskContainer<I, O> extends Task<O> {

    private Function<I, O> loadingCSV;
    private I i;
    public AsyncTaskContainer(I i, Function<I, O> loadingCSV) {
        this.loadingCSV = loadingCSV;
        this.i = i;

    }

    @Override
    protected O call() throws Exception {
        return loadingCSV.apply(i);
    }
}
