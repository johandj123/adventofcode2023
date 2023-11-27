package lib.dijkstra;

import java.util.Objects;

public class DijkstraNodeDistance<T extends DijkstraNode<T>> implements Comparable<DijkstraNodeDistance<T>> {
    private final int distance;
    private final T node;

    public DijkstraNodeDistance(int distance, T node) {
        Objects.requireNonNull(node);
        this.distance = distance;
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DijkstraNodeDistance<?> that = (DijkstraNodeDistance<?>) o;
        return distance == that.distance && node.equals(that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, node);
    }

    @Override
    public int compareTo(DijkstraNodeDistance<T> o) {
        if (distance < o.distance) {
            return -1;
        }
        if (distance > o.distance) {
            return 1;
        }
        return node.compareTo(o.node);
    }

    public int getDistance() {
        return distance;
    }

    public T getNode() {
        return node;
    }
}
