package lib.bfs;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BFS<T extends BFSNode<T>> {
    private Set<T> explored = new HashSet<>();
    private Set<T> current = new HashSet<>();

    public BFS(T root)
    {
        current.add(root);
    }

    public int shortestDistance(Predicate<T> endPredicate)
    {
        int counter = 0;
        while (!current.isEmpty()) {
            if (current.stream().anyMatch(endPredicate)) {
                return counter;
            }
            explored.addAll(current);
            Set<T> next = new HashSet<>();
            for (T node : current) {
                for (T nextNode : node.getNeighbours()) {
                    if (!explored.contains(nextNode)) {
                        next.add(nextNode);
                    }
                }
            }
            counter++;
            current = next;
        }
        throw new IllegalStateException("End not found");
    }
}
