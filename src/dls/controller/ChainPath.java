package dls.controller;

import java.util.List;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPath {

  private int sourceId;
  private int destinationId;
  private List<Integer> interModules;

  public ChainPath(int sourceId, int destinationId, List<Integer> interModules) {
    this.sourceId = sourceId;
    this.destinationId = destinationId;
    this.interModules = interModules;
  }

  public int getSourceId() {
    return sourceId;
  }

  public void setSourceId(int sourceId) {
    this.sourceId = sourceId;
  }

  public int getDestinationId() {
    return destinationId;
  }

  public void setDestinationId(int destinationId) {
    this.destinationId = destinationId;
  }

  public List<Integer> getInterModules() {
    return interModules;
  }

  public void setInterModules(List<Integer> interModules) {
    this.interModules = interModules;
  }
}
