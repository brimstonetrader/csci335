package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class AlphaBetaPruning extends CheckersSearcher {
    private int numNodes = 0;

    public AlphaBetaPruning(ToIntFunction<Checkerboard> e) {
        super(e);
    }

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public int AlphaBeta(Checkerboard c, int depthLimit, int multiplier, int alpha, int beta) {
        if (depthLimit == 0 || c.gameOver()) return multiplier * getEvaluator().applyAsInt(c);
        numNodes++;
        int bestScore = Integer.MIN_VALUE;
        for (Checkerboard nextBoard : c.getNextBoards()) {
            if (nextBoard.getCurrentPlayer() == c.getCurrentPlayer()) { multiplier = 1; } else { multiplier = -1; }
            bestScore = Math.max(bestScore, multiplier * AlphaBeta(nextBoard, depthLimit-1, multiplier, -beta, -alpha));
            alpha = Math.max(alpha, bestScore);
            if (alpha >= beta) { break; }
        }
        return bestScore;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        for (Checkerboard nextBoard: board.getNextBoards()) {
            numNodes += 1;
            int multiplier = board.getCurrentPlayer() != nextBoard.getCurrentPlayer() ? -1 : 1;
            int scoreFor = AlphaBeta(nextBoard, getDepthLimit(), multiplier, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (best.isEmpty() || best.get().getFirst() < scoreFor) {
                best = Optional.of(new Duple<>(scoreFor, nextBoard.getLastMove()));
            }
        }
        return best;
    }
}
