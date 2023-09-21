package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.function.ToIntFunction;

public class RandomPlayer extends CheckersSearcher {
    private int numNodes = 0;

    public RandomPlayer(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        ArrayList<Checkerboard> boards = board.getNextBoards();
        Random rand = new Random();
        int lenboards = boards.size();
        Checkerboard choice = lenboards>0 ? boards.get(rand.nextInt(lenboards)) : board;
        return choice != board ? Optional.of(new Duple<>(0, choice.getLastMove())) : Optional.empty();
    }
}
