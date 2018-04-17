package dls.loader;

import com.google.common.collect.ImmutableMap;
import com.opencsv.bean.CsvToBeanFilter;
import dls.model.AbstractTrend;
import dls.model.AnalogTrend;
import dls.model.BinaryTrend;
import dls.model.Connection;
import dls.model.Port;
import dls.parser.DlsCsvParser;
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


  public Stream<Connection> loadConnections() throws IOException {
    return loadConnections(null);
  }

  private Stream<Connection> loadConnections(CsvToBeanFilter filter) throws IOException {
    String connectionCsvPath = "D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer\\Connections.csv";
    //String connectionCSVPath = "C:\\Users\\yinya\\Desktop\\ProducerCVS\\Connections.csv";
    Reader connReader = new FileReader(connectionCsvPath);
    DlsCsvParser parser = getDlsCsvParser();
    return Utils.iteratorAsStream(parser.loadConnections(connReader, filter));
  }


  public Stream<Port> loadPorts() throws IOException {
    return loadPorts(null);
  }

  public Stream<Port> loadPorts(CsvToBeanFilter filter) throws IOException {
    String PortCsvPath = "D:\\Project\\DLS\\DLS Dokumentation\\raw\\Producer\\Ports.csv";
    Reader connReader = new FileReader(PortCsvPath);
    DlsCsvParser parser = getDlsCsvParser();
    return Utils.iteratorAsStream(parser.loadPorts(connReader, filter));
  }


  private DlsCsvParser getDlsCsvParser() {
    return new DlsCsvParser();
  }

}
