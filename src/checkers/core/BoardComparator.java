package checkers.core;

import checkers.core.Checkerboard;
import checkers.searchers.AlphaBetaPruningPlus;

import java.util.Comparator;
import checkers.evaluators.BasicMaterial;
import java.util.function.ToIntFunction;

public class BoardComparator implements Comparator<Checkerboard> {
    ToIntFunction<Checkerboard> heuristic = new BasicMaterial();
    public int compare(Checkerboard c1, Checkerboard c2) {
        int s1 = heuristic.applyAsInt(c1);
        int s2 = heuristic.applyAsInt(c2);
        if      (s1 > s2) return 1; //reversed, because we want to start with maximum
        else if (s1 < s2) return -1;
        return 0;
    }
}
