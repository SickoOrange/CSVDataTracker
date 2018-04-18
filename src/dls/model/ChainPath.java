package dls.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPath {

  private int alertId;
  private int sourceId;
  private List<Integer> interIds;

  public ChainPath(int alertId, int sourceId, List<Integer> interIds) {
    this.alertId = alertId;
    this.sourceId = sourceId;
    this.interIds = interIds;
  }

  public int getAlertId() {
    return alertId;
  }

  public void setAlertId(int alertId) {
    this.alertId = alertId;
  }

  public int getSourceId() {
    return sourceId;
  }

  public void setSourceId(int sourceId) {
    this.sourceId = sourceId;
  }

  public List<Integer> getInterIds() {
    return interIds;
  }

  public void setInterIds(List<Integer> interIds) {
    this.interIds = interIds;
  }

  public List<Integer> toList() {
    ArrayList<Integer> list = Lists.newArrayList();
    list.add(alertId);
    list.add(sourceId);
    list.addAll(interIds);
    return list;
  }
}
