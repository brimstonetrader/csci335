package learning.classifiers;

import core.Duple;
import learning.core.Classifier;
import learning.core.Histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.ToDoubleBiFunction;

// KnnTest.test() should pass once this is finished.
public class Knn<V, L> implements Classifier<V, L> {
    private ArrayList<Duple<V, L>> data = new ArrayList<>();
    private ToDoubleBiFunction<V, V> distance;
    private int k;

    public Knn(int k, ToDoubleBiFunction<V, V> distance) {
        this.k = k;
        this.distance = distance;
    }

    @Override
    public L classify(V value) {
        // TODO: Find the distance from value to each element of data. Use Histogram.getPluralityWinner()
        //  to find the most popular label.
        ArrayList<Duple<V,L>> ds = new ArrayList<>();
        Histogram<L> ls = new Histogram<>();
        for (Duple<V,L> t : data) {
            double d = distance.applyAsDouble(t.getFirst(), value);
            Duple<Double,L> dp = new Duple<>(d,t.getSecond());
            ds.add((Duple<V, L>) dp);
        }

        Collections.sort();
        return null;
    }

    @Override
    public void train(ArrayList<Duple<V, L>> training) {
        // TODO: Add all elements of training to data.
        data.addAll(training);
    }
}
