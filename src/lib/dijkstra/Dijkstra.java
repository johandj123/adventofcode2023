package lib.dijkstra;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

public class Dijkstra<T extends DijkstraNode<T>> {
    private Map<T, Integer> distances = new HashMap<>();
    private SortedSet<DijkstraNodeDistance<T>> queue = new TreeSet<>();

    public Dijkstra(T root)
    {
        queue.add(new DijkstraNodeDistance<T>(0, root));
    }

    public int shortestDistance(Predicate<T> endPredicate)
    {
        while (!queue.isEmpty()) {
            DijkstraNodeDistance<T> current = queue.first();
            queue.remove(current);
            if (endPredicate.test(current.getNode())) {
                return current.getDistance();
            }
            for (Map.Entry<T, Integer> entry : current.getNode().getNeighbours().entrySet()) {
                T next = entry.getKey();
                int distance = current.getDistance() + entry.getValue();
                Integer oldDistance = distances.get(next);
                if (oldDistance == null || distance < oldDistance) {
                    if (oldDistance != null) {
                        queue.remove(new DijkstraNodeDistance<>(oldDistance, next));
                    }
                    queue.add(new DijkstraNodeDistance<>(distance, next));
                    distances.put(next, distance);
                }
            }
        }
        throw new IllegalStateException("End node not found");
    }
}
