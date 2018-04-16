/*
 * Copyright (c) Siemens AG 2017 ALL RIGHTS RESERVED.
 *
 * Digital Lifecycle Service (DLS)
 */

package dls.parser;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanFilter;
import com.opencsv.bean.opencsvUtils;
import dls.model.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Locale;
import java.util.function.Function;

import static com.google.common.base.Strings.emptyToNull;
import static org.apache.commons.collections4.IteratorUtils.transformedIterator;
import static org.apache.commons.collections4.IteratorUtils.filteredIterator;
import static org.apache.commons.lang3.math.NumberUtils.toInt;

/**
 * Class that parses CSV files in a simple manner, it only checks for occurrences of ; and splits
 * the line at the found positions. It implements methods for splitting modules, ports, connections
 * and analogTrend
 */
public class DlsCsvParser {

    public static final char SEPARATOR = ';';

    /**
     * Reads a Module CSV from AmazonS3 and filters it according to the filter, then returns it as an
     * iterator
     *
     * @param s3Reader The reader of the CSV file
     * @param filter   how should the csv fle be filtered
     * @return An iterator over the filtered modules
     */
    public Iterator<Module> loadModules(Reader s3Reader, CsvToBeanFilter filter)
            throws IOException {
        return objectsIterator(s3Reader, filter, DlsCsvParser::lineToModule);
    }

    private static Module lineToModule(String[] input) {
        Module m = new Module();
        m.setId(Module.extractId(input));
        m.setAfiTypeId(Module.extractAfiType(input));
        m.setSymbol(Module.extractSymbol(input));
        m.setNode(Module.extractNode(input));
        return m;
    }

    /**
     * Reads a Port CSV from AmazonS3 and filters it according to the filter, then returns it as an
     * iterator
     *
     * @param s3Reader The reader of the CSV file
     * @param filter   how should the csv fle be filtered
     * @return An iterator over the filtered ports
     */
    public Iterator<Port> loadPorts(Reader s3Reader, CsvToBeanFilter filter) throws IOException {
        return objectsIterator(s3Reader, filter, DlsCsvParser::lineToPort);
    }

    private <T> Iterator objectsIterator(Reader reader, CsvToBeanFilter filter,
                                         Function<String[], T> transformer) throws IOException {
        CSVReader csvReader = getSimpleCsvReader(reader);
        CSVIterator iter = new CSVIterator(csvReader);


        Iterator filteredIterator = filteredIterator(iter,
                l -> filter == null || filter.allowLine((String[]) l));
        return transformedIterator(filteredIterator, input -> transformer.apply((String[]) input));
    }

    private static Port lineToPort(String[] line) {
        Port p = new Port();
        p.setAfiId(Port.extractAfiId(line));
        p.setId(Port.extractId(line));
        p.setName(emptyToNull(Port.extractName(line)));
        p.setDirection(Port.extractDirection(line));
        p.setParameter(emptyToNull(Port.extractParameter(line)));
        p.setArchive(CsvStringToBooleanConverter.toBoolean(Port.extractIsArchive(line)));
        p.setAlarm(CsvStringToBooleanConverter.toBoolean(Port.extractIsAlarm(line)));
        p.setAlarmTypeId(Port.extractAlarmTypeId(line));
        p.setActive(emptyToNull(Port.extractActive(line)));
        p.setMinValue(Port.extractMinValue(line));
        p.setMaxValue(Port.extractMaxValue(line));
        p.setEngineeringUnit(emptyToNull(Port.extractEngineeringUnit(line)));
        p.setUniqueName(emptyToNull(Port.extractUniqueName(line)));
        p.setConnAfiId(Port.extractConnAfiid(line));
        p.setConnPortId(Port.extractConnPortid(line));
        return p;
    }

    /**
     * Reads a Connections CSV from AmazonS3 and filters it according to the filter, then returns it
     * as an iterator
     *
     * @param reader The reader of the CSV file
     * @param filter how should the csv fle be filtered
     * @return the connections of all fitting PortKeys
     * @throws IOException if the file could not be read
     */
    public Iterator<Connection> loadConnections(Reader reader, CsvToBeanFilter filter)
            throws IOException {
        return objectsIterator(reader, filter, DlsCsvParser::lineToConnection);
    }

    private static Connection lineToConnection(String[] line) {
        return new Connection(new PortKey(toInt(line[Connection.AFI1_COLUMN_INDEX]),
                toInt(line[Connection.PORT1_COLUMN_INDEX])),
                new PortKey(toInt(line[Connection.AFI2_COLUMN_INDEX]),
                        toInt(line[Connection.PORT2_COLUMN_INDEX])));
    }

    /**
     * Reads a Trend CSV from AmazonS3 of a specific Type and filters it according to the filter, then
     * returns it as an iterator
     *
     * @param s3Reader    The reader of the CSV file
     * @param filter      how should the csv fle be filtered
     * @param transformer
     * @return An iterator over the filtered analogTrend
     */
    public <T extends AbstractTrend<T>> Iterator<T> loadTrends(Reader s3Reader,
                                                               CsvToBeanFilter filter, Function<String[], T> transformer) throws IOException {
        return objectsIterator(s3Reader, filter, transformer);
    }

    /**
     * Reads a Alarm CSV from AmazonS3 and filters it according to the filter, then returns it
     * as an iterator
     *
     * @param s3Reader The reader of the CSV file
     * @param filter   how should the csv fle be filtered
     * @return An iterator over the filtered analogTrend
     */
    public Iterator<Alarm> loadAlarms(Reader s3Reader,
                                      CsvToBeanFilter filter) {
        CsvToBean<Alarm> csvParser = prepareCsvToBean(s3Reader, Alarm.class, filter);
        return csvParser.iterator();
    }

    /**
     * Reads a AlarmType CSV from AmazonS3 and filters it according to the filter, then returns it
     * as an iterator
     *
     * @param reader The reader of the CSV file
     * @return An iterator over the filtered analogTrend
     */
    public Iterator<AlarmType> loadAlarmTypes(Reader reader) {
        CsvToBean<AlarmType> csvParser = prepareCsvToBean(reader, AlarmType.class, null);
        return csvParser.iterator();
    }

    /**
     * Reads a OperatorAction CSV from AmazonS3 and filters it according to the filter, then returns it
     * as an iterator
     *
     * @param reader The reader of the CSV file
     * @param filter how should the csv fle be filtered
     * @return An iterator over the filtered operAct
     */
    public Iterator<OperatorAction> loadOperatorActions(Reader reader,
                                                        CsvToBeanFilter filter) {
        CsvToBean<OperatorAction> csvParser = prepareCsvToBean(reader, OperatorAction.class, filter);
        return csvParser.iterator();
    }


    private <T> CsvToBean<T> prepareCsvToBean(Reader reader, Class<T> clazz,
                                              CsvToBeanFilter filter) {
        CSVReader csvReader = getSimpleCsvReader(reader);
        CsvToBean<T> csvParser = new CsvToBean<>();
        csvParser.setFilter(filter);
        csvParser
                .setMappingStrategy(opencsvUtils.determineMappingStrategy(clazz, Locale.getDefault()));
        csvParser.setCsvReader(csvReader);
        return csvParser;
    }

    /**
     * Simple reader that splits every line at ;
     *
     * @param reader
     */
    private CSVReader getSimpleCsvReader(Reader reader) {
        ICSVParser parser = new SimpleSplittingCsvParser(SEPARATOR);
        return new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();
    }
}
