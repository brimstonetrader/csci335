package search.bestfirst;

import core.Duple;
import search.SearchNode;
import search.SearchQueue;

import java.util.*;
import java.util.function.ToIntFunction;

public class BestFirstQueue<T> implements SearchQueue<T> {
    private PriorityQueue<SearchNode<T>> pq;
    private HashMap<T, Integer> visited;
    private ToIntFunction<T> heuristic;

    // For each object encountered, this is the lowest total length estimate
    // encountered so far.
    private HashMap<T,Integer> lowestEstimateFor;

    // Use this heuristic to get the estimated distance to the goal node.
    private ToIntFunction<T> heuristic;

    public BestFirstQueue(ToIntFunction<T> heuristic) {
        this.pq = new PriorityQueue<>(new PosComp(heuristic));
        this.visited = new HashMap<>();
        this.heuristic = heuristic;
    }

    class PosComp implements Comparator<SearchNode<T>>{
        ToIntFunction<T> heuristic;
        private PosComp(ToIntFunction<T> heuristic) {
            this.heuristic = heuristic;
        }
        public int compare(SearchNode<T> n1, SearchNode<T> n2) {
            return Integer.compare(heuristic.applyAsInt(n1.getValue()), heuristic.applyAsInt(n2.getValue()));
        }
    }

    @Override
    public void enqueue(SearchNode<T> node) {
        T t = node.getValue();
        if (!visited.containsKey(t) || heuristic.applyAsInt(t) < visited.get(t)) {
            pq.add(node);
        }
    }

    @Override
    public Optional<SearchNode<T>> dequeue() {
        if (pq.isEmpty()) {
            return Optional.empty();
        } else {
            SearchNode<T> node = pq.remove();
            T t = node.getValue();
            visited.put(t, heuristic.applyAsInt(t));
            return Optional.of(node);
        }
    }
}
