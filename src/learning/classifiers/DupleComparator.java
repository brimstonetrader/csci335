package learning.classifiers;

import core.Duple;

import java.util.Comparator;

public class DupleComparator<Integer, T2> implements Comparator<Duple<Integer, T2>> {
    @Override
    public int compare(Duple<Integer, T2> duple1, Duple<Integer, T2> duple2) {
        return ((Comparable<Integer>) duple1.getFirst()).compareTo(duple2.getFirst());
    }
}
