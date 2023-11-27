package lib.dijkstra;

import java.util.Map;

public interface DijkstraNode<T extends DijkstraNode<T>> extends Comparable<T> {
    Map<T, Integer> getNeighbours();
}
