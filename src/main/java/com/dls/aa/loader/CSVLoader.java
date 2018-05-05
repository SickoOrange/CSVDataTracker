package com.dls.aa.loader;

import com.dls.aa.model.AbstractTrend;
import com.dls.aa.model.AfiType;
import com.dls.aa.model.AnalogTrend;
import com.dls.aa.model.BinaryTrend;
import com.dls.aa.model.Connection;
import com.dls.aa.model.Module;
import com.dls.aa.model.Port;
import com.dls.aa.parser.DlsCsvParser;
import com.dls.aa.service.PropertiesService;
import com.google.common.collect.ImmutableMap;
import com.opencsv.bean.CsvToBeanFilter;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVLoader {

    public static final String AFI_CSV = "Afi.csv";
    public static final String AFI_TYPE_CSV = "AfiType.csv";
    public static final String PORTS_CSV = "Ports.csv";
    public static final String CONNECTIONS_CSV = "Connections.csv";
    protected static final String ALARM_CSV = "Alarm.csv";
    protected static final String OPER_ACT_CSV = "OperAct.csv";
    protected static final String ALARM_TYPE_CSV = "AlarmType.csv";
    protected static final String ANALOG_TREND_CSV = "AnalogTrend.csv";
    protected static final String BINARY_TREND_CSV = "BinaryTrend.csv";
    private static final ImmutableMap<Class<? extends AbstractTrend>, String> TREND_FILE_NAMES =
            ImmutableMap.of(
                    AnalogTrend.class, ANALOG_TREND_CSV,
                    BinaryTrend.class, BINARY_TREND_CSV
            );

    private String getSourceDirectory() {
        return PropertiesService.readPropValue("path");
    }


    // TODO: 26.04.2018 module info+ module bean
    public Stream<AfiType> loadAfiType(CsvToBeanFilter filter) throws IOException {
        String afiTypeCsvPath = getSourceDirectory() + File.separator + AFI_TYPE_CSV;
        Reader afiTypeReader = new FileReader(afiTypeCsvPath);
        return Utils.iteratorAsStream(getDlsCsvParser().loadAfiType(afiTypeReader, filter));
    }


    /**
     * Load modules from AFI.csv and provide them as a {@link Map} of id-indexed {@link Module} instances.
     *
     * @param filter A validation filter for the CSV-format
     * @return An id-indexed {@link Map} of {@link Module}s
     */
    public Map<Integer, Module> loadModules(CsvToBeanFilter filter) throws IOException {
        String afiCsvPath = getSourceDirectory() + File.separator + AFI_CSV;
        Reader afiReader = new FileReader(afiCsvPath);
        return Utils.iteratorAsStream(getDlsCsvParser().loadModules(afiReader, filter))
                .collect(Collectors.toMap(Module::getId, m -> m));
    }


    public Stream<Connection> loadConnections() throws IOException {
        return loadConnections(null);
    }

    public Stream<Connection> loadConnections(CsvToBeanFilter filter) throws IOException {
        String connectionCsvPath = getSourceDirectory() + File.separator + CONNECTIONS_CSV;
        Reader connReader = new FileReader(connectionCsvPath);
        return Utils.iteratorAsStream(getDlsCsvParser().loadConnections(connReader, filter));
    }


    public Stream<Port> loadPorts() throws IOException {
        return loadPorts(null);
    }

    public Stream<Port> loadPorts(CsvToBeanFilter filter) throws IOException {
        String PortCsvPath = getSourceDirectory() + File.separator + PORTS_CSV;
        Reader connReader = new FileReader(PortCsvPath);
        return Utils.iteratorAsStream(getDlsCsvParser().loadPorts(connReader, filter));
    }


    public Map<String, BinaryTrend> loadBinaryTrends(Set<String> uniquePortNames) throws IOException {
        System.out.println("reading binary trends from CSV ");
        return loadTrends(BinaryTrend.class, uniquePortNames, BinaryTrend::lineToTrend);

    }

    private <T extends AbstractTrend<T>> Map<String, T> loadTrends(Class<T> trendClazz, Set<String> uniquePortNames,
                                                                   Function<String[], T> transformer) throws IOException {
        System.out.println("reading trends from CSV");
        String trendsPath = getSourceDirectory() + File.separator + TREND_FILE_NAMES.get(trendClazz);
        Reader trendsReader = new FileReader(trendsPath);
        Map<String, T> trendsByUniqueName = Utils.iteratorAsStream(getDlsCsvParser().loadTrends(trendsReader,
                line -> uniquePortNames.contains(AbstractTrend.extractTagname(line)),
                transformer
        )).collect(Collectors.toMap(T::getUniqueName, m -> m));
        System.out.println("read and indexed trends from csv" + trendsByUniqueName.size());
        return trendsByUniqueName;
    }

    private DlsCsvParser getDlsCsvParser() {
        return new DlsCsvParser();
    }

}
