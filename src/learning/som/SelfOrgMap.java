package learning.som;

import core.Duple;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import learning.handwriting.core.FloatDrawing;
import java.util.function.ToDoubleBiFunction;

public class SelfOrgMap<V> {
    private V[][] map;
    private ToDoubleBiFunction<V, V> distance;
    private WeightedAverager<V> averager;

    public SelfOrgMap(int side, Supplier<V> makeDefault, ToDoubleBiFunction<V, V> distance, WeightedAverager<V> averager) {
        this.distance = distance;
        this.averager = averager;
        map = (V[][])new Object[side][side];
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                map[i][j] = makeDefault.get();
            }
        }
    }

    // TODO: Return a SOMPoint corresponding to the map square which has the
    //  smallest distance compared to example.
    // NOTE: The unit tests assume that the map is traversed in row-major order,
    //  that is, the y-coordinate is updated in the outer loop, and the x-coordinate
    //  is updated in the inner loop.
    public SOMPoint bestFor(V example) {
        int n = map.length;
        double bestSoFar = Double.MAX_VALUE;
        SOMPoint output = new SOMPoint(0,0);
        for (int y = 0; y < getMapHeight(); y++) {
            for (int x = 0; x < getMapWidth(); x++) {
                if (distance.applyAsDouble(map[x][y], example) < bestSoFar) {
                    bestSoFar = distance.applyAsDouble(map[x][y], example);
                    output = new SOMPoint(x, y);
                }
            }
        }
        return output;
    }

    // TODO: Train this SOM with example.
    //  1. Find the best matching node.
    //  2. Update the best matching node with the average of itself and example,
    //     using a learning rate of 0.9.
    //  3. Update each neighbor of the best matching node that is in the map,
    //     using a learning rate of 0.4.
    public void train(V example) {
        SOMPoint best = bestFor(example);
        V curr = map[best.x()][best.y()];
        map[best.x()][best.y()] = averager.weightedAverage(example,curr,0.9);
        for (SOMPoint som : best.neighbors()) {
            int x = som.x();
            int y = som.y();
            if (y >= 0 && x >= 0 && x < getMapWidth() && y < getMapHeight()) {
                    map[x][y] = averager.weightedAverage(example,map[x][y],0.4);
            }
        }
    }

    public V getNode(int x, int y) {
        return map[x][y];
    }

    public int getMapWidth() {
        return map.length;
    }

    public int getMapHeight() {
        return map[0].length;
    }

    public int numMapNodes() {
        return getMapHeight() * getMapWidth();
    }

    public boolean inMap(SOMPoint point) {
        return point.x() >= 0 && point.x() < getMapWidth() && point.y() >= 0 && point.y() < getMapHeight();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SelfOrgMap that) {
            if (this.getMapHeight() == that.getMapHeight() && this.getMapWidth() == that.getMapWidth()) {
                for (int x = 0; x < getMapWidth(); x++) {
                    for (int y = 0; y < getMapHeight(); y++) {
                        if (!map[x][y].equals(that.map[x][y])) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                result.append(String.format("(%d, %d):\n", x, y));
                result.append(getNode(x, y));
            }
        }
        return result.toString();
    }
}
