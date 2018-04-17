package dls.model;

import java.util.List;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPath {

  private int sourceId;
  private int destinationId;
  private List<Integer> interIds;

  public ChainPath(int sourceId, int destinationId, List<Integer> interIds) {
    this.sourceId = sourceId;
    this.destinationId = destinationId;
    this.interIds = interIds;
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

  public List<Integer> getInterIds() {
    return interIds;
  }

  public void setInterIds(List<Integer> interIds) {
    this.interIds = interIds;
  }
}
