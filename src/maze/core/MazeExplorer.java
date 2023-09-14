package maze.core;

import java.util.*;

import core.Direction;
import core.Pos;

public class MazeExplorer {
	private final Maze m;
	private final Pos location;
	private final TreeSet<Pos> treasureFound;
	private MazeExplorer goal;

	public MazeExplorer(Maze m, Pos location) {
		this.m = m;
		this.location = location;
		this.treasureFound = new TreeSet<>();
	}

	public Pos getLocation() {return location;}

	public Set<Pos> getAllTreasureFromMaze() {
		return m.getTreasures();
	}

	public Set<Pos> getAllTreasureFound() {
		return treasureFound;
	}

	public int getNumTreasuresFound() {
		return treasureFound.size();
	}

	public MazeExplorer getGoal() {
		if (goal == null) {
			goal = m.getGoal();
		}
		return goal;
	}

	public ArrayList<MazeExplorer> getSuccessors() {
		ArrayList<MazeExplorer> result = new ArrayList<>();
		ArrayList<Pos> posse = m.getNeighbors(location);
		for (Pos pos : posse) {
			MazeExplorer mazeExp = new MazeExplorer(m, pos);
			TreeSet<Pos> tF2 = new TreeSet<>(treasureFound);
			if (m.isTreasure(pos)) {
				tF2.add(pos);
			}
			if (!m.blocked(location, pos)) {
				mazeExp.addTreasures(tF2);
				result.add(mazeExp);
			}
		} System.out.println(location); return result;
	}

	public void addTreasures(Collection<Pos> treasures) {
		treasureFound.addAll(treasures);
	}

	public String toString() {
		StringBuilder treasures = new StringBuilder();
		for (Pos t: treasureFound) {
			treasures.append(";");
			treasures.append(t.toString());
		}
		return "@" + location.toString() + treasures;
	}

	@Override
	public int hashCode() {return toString().hashCode();}

	@Override
	public boolean equals(Object other) {
		if (other instanceof MazeExplorer that) {
			return this.location.equals(that.location) && this.treasureFound.equals(that.treasureFound);
		} else {
			return false;
		}
	}

	public boolean achievesGoal() {
		return this.equals(getGoal());
	}

	public Maze getM() {
		return m;
	}
}
