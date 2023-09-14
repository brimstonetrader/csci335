package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.ToIntFunction;


public class DistanceToFurthestTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        ArrayList<Pos> treasures        = new ArrayList<>(node.getAllTreasureFromMaze());
        Set<Pos> foundTreasures   = node.getAllTreasureFound();
        ArrayList<Pos> availableTreasures = new ArrayList<>();
        for (Pos treasure : treasures) {
            if (!foundTreasures.contains(treasure)) {
                availableTreasures.add(treasure);
            }
        }
        int minTreasureDist = Integer.MAX_VALUE;
        int maxTreasureDist = 0;
        for (Pos treasure : availableTreasures) {
            int thisTreasureDist = treasure.getManhattanDist(node.getLocation());
            if (thisTreasureDist > maxTreasureDist) {
                maxTreasureDist = thisTreasureDist;
            }
            if (thisTreasureDist < minTreasureDist) {
                minTreasureDist = thisTreasureDist;
            }
        }
        return maxTreasureDist;
    }
}
