package maze.heuristics;

import core.Pos;
import maze.core.MazeExplorer;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.ToIntFunction;


public class ManhattanPlusMaxTreasure implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        ArrayList<Pos> treasures        = new ArrayList<>(node.getAllTreasureFromMaze());
        Set<Pos> foundTreasures   = node.getAllTreasureFound();
        ArrayList<Integer> availableTreasures = new ArrayList<>();
        int maxTreasureDistInWrongDirection = 0;
        for (Pos treasure : treasures) {
            if (treasure.getX() >= node.getLocation().getX() && treasure.getY() <= node.getLocation().getY()) {
                maxTreasureDistInWrongDirection = treasure.getManhattanDist(node.getLocation());
            }
            if (!foundTreasures.contains(treasure)) {
                availableTreasures.add(treasure.getManhattanDist(node.getLocation()));
            }
        }
        if (node.getNumTreasuresFound() < node.getAllTreasureFromMaze().size()) {
            return (int) (availableTreasures.stream().mapToDouble(d -> d).average().orElse(0.0)) - node.getNumTreasuresFound();
        }
        return maxTreasureDistInWrongDirection + node.getLocation().getManhattanDist(node.getM().getGoal().getLocation());
    }
}
