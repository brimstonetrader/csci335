package maze.heuristics;

import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;


public class ManhattanToGoal implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        return node.getLocation().getManhattanDist(node.getM().getGoal().getLocation());
    }
}
