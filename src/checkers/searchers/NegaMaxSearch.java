package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMaxSearch extends CheckersSearcher {
    private int numNodes = 0;

    public NegaMaxSearch(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public int NegaMax(Checkerboard c, int depthLimit, int multiplier) {
        int bestScore = Integer.MIN_VALUE;
        if (depthLimit == 0 || c.gameOver()) return multiplier * getEvaluator().applyAsInt(c);
        numNodes++;
        for (Checkerboard nextBoard : c.getNextBoards()) {
            if (nextBoard.getCurrentPlayer() == c.getCurrentPlayer()) { multiplier = 1; } else { multiplier = -1; }
            int nextScore = multiplier * NegaMax(nextBoard, depthLimit-1, multiplier);
            if (bestScore < nextScore) { bestScore = nextScore; }
        }
        return bestScore;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        for (Checkerboard nextBoard: board.getNextBoards()) {
            numNodes += 1;
            int multiplier = board.getCurrentPlayer() != nextBoard.getCurrentPlayer() ? -1 : 1;
            int scoreFor = NegaMax(nextBoard, getDepthLimit(), multiplier);
            if (best.isEmpty() || best.get().getFirst() < scoreFor) {
                best = Optional.of(new Duple<>(scoreFor, nextBoard.getLastMove()));
            }
        }
        return best;
    }
}
