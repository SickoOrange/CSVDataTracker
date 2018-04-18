package dls.loader;

import com.google.common.collect.ImmutableMap;
import com.opencsv.bean.CsvToBeanFilter;
import dls.model.AbstractTrend;
import dls.model.AnalogTrend;
import dls.model.BinaryTrend;
import dls.model.Connection;
import dls.model.Module;
import dls.model.Port;
import dls.parser.DlsCsvParser;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;

public class CSVLoader {

  public static final String AFI_CSV = "Afi.csv";
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

  public static final Logger LOGGER = Logger.getLogger(CSVLoader.class);


  /**
   * Load modules from AFI.csv and provide them as a {@link Map} of id-indexed {@link Module} instances.
   *
   * @param filter A validation filter for the CSV-format
   * @return An id-indexed {@link Map} of {@link Module}s
   */
  public Map<Integer, Module> loadModules(CsvToBeanFilter filter) throws IOException {
    String afiCsvPath = "D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer\\" + AFI_CSV;
    Reader afiReader = new FileReader(afiCsvPath);
    return Utils.iteratorAsStream(getDlsCsvParser().loadModules(afiReader, filter))
        .collect(Collectors.toMap(Module::getId, m -> m));
  }


  public Stream<Connection> loadConnections() throws IOException {
    return loadConnections(null);
  }

  private Stream<Connection> loadConnections(CsvToBeanFilter filter) throws IOException {
    String connectionCsvPath =
        "D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer\\" + CONNECTIONS_CSV;
    //String connectionCsvPath = "C:\\Users\\yinya\\Desktop\\ProducerCVS\\Connections.csv";
    Reader connReader = new FileReader(connectionCsvPath);
    return Utils.iteratorAsStream(getDlsCsvParser().loadConnections(connReader, filter));
  }


  public Stream<Port> loadPorts() throws IOException {
    return loadPorts(null);
  }

  public Stream<Port> loadPorts(CsvToBeanFilter filter) throws IOException {
    String PortCsvPath = "D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer\\" + PORTS_CSV;
    //String PortCsvPath = "C:\\Users\\yinya\\Desktop\\ProducerCVS\\Ports.csv";
    Reader connReader = new FileReader(PortCsvPath);
    return Utils.iteratorAsStream(getDlsCsvParser().loadPorts(connReader, filter));
  }


  private DlsCsvParser getDlsCsvParser() {
    return new DlsCsvParser();
  }

}
