package dls.loader;

import com.google.common.collect.ImmutableList;
import dls.controller.ChainPath;
import dls.model.Connection;
import dls.model.PortKey;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User: Ya Yin
 * Date: 17.04.2018
 */
public class ChainPathLoader {

  private static final int MAX_DEPTH = 7;


  public List<ChainPath> loadChainPaths(int sourceAfiId, int destinationAfiId,
      List<Connection> connections) {
    if (sourceAfiId == destinationAfiId) {
      throw new IllegalArgumentException(String
          .format("source %d and destination %d can't be identical", sourceAfiId,
              destinationAfiId));
    }
    return searchChainsRecursive(sourceAfiId, sourceAfiId, destinationAfiId, 0,
        new LinkedList<>(), connections);
  }

  private List<ChainPath> searchChainsRecursive(int sourceAfiId, int inputId,
      int destinationAfiId, int depth,
      LinkedList<Integer> interIds, List<Connection> connections) {

    if (depth > MAX_DEPTH) {
      return Collections.emptyList();
    }
    Optional<ChainPath> chainPath = findChainPath(inputId, sourceAfiId,
        destinationAfiId,
        interIds);

    if (!chainPath.isPresent()) {
      //start recursive

      if (inputId != sourceAfiId) {
        interIds.add(0, inputId);
      }

      List<Connection> connectionsConnectedToInputId = connections.stream()
          .filter(connection -> connection.getIn().getAfiId() == inputId)
          .collect(Collectors.toList());

      return connectionsConnectedToInputId
          .stream()
          .map(Connection::getOut)
          .map(PortKey::getAfiId)
          .map(id -> searchChainsRecursive(sourceAfiId, id, destinationAfiId, depth + 1,
              new LinkedList<>(interIds), connections))
          .filter(Objects::nonNull)
          .flatMap(Collection::stream)
          .collect(Collectors.toList());

    }

    return Collections.singletonList(chainPath.get());

  }

  private Optional<ChainPath> findChainPath(int inputId,
      int sourceAfiId,
      int destinationAfiId, LinkedList<Integer> interIds) {

    if (isDestinationAfiId(inputId, destinationAfiId)) {
      return Optional
          .of(new ChainPath(sourceAfiId, destinationAfiId, ImmutableList.copyOf(interIds)));
    }

    return Optional.empty();
  }

  private boolean isDestinationAfiId(int inputId, int destinationAfiId) {
    return inputId == destinationAfiId;
//        connections.stream()
//            .anyMatch(connection -> connection.getOut().getAfiId() == destinationAfiId);

  }

}
