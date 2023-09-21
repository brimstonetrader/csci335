package checkers.searchers;

import checkers.core.BoardComparator;
import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.ArrayList;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.ToIntFunction;

public class AlphaBetaPruningPlus extends CheckersSearcher {
    private int numNodes = 0;

    public AlphaBetaPruningPlus(ToIntFunction<Checkerboard> e) {
        super(e);
    }
    private boolean first = true;

    @Override
    public int numNodesExpanded() {
        return numNodes;
    }

    public int NegaMax(Checkerboard c, int depthLimit, int multiplier, int alpha, int beta) {
        if (c.gameOver())
            return multiplier * getEvaluator().applyAsInt(c);
        if (depthLimit < 1) {
            if (!c.getLastMove().isCapture()) { //Searching until quiescent
                return multiplier * getEvaluator().applyAsInt(c);
            }
        }
        numNodes++;
        int nextScore = Integer.MIN_VALUE;
        ArrayList<Checkerboard> boards = c.getNextBoards();
        PriorityQueue<Checkerboard> pq = new PriorityQueue<>(boards.size(), new BoardComparator()); //Ordering Heuristic
        pq.addAll(c.getNextBoards());
        while (!pq.isEmpty()) {
            Checkerboard nextBoard = pq.poll();
            if (first) {
                first = false;
                int thisValue = getEvaluator().applyAsInt(nextBoard);
                int nextValue = 1;
                Checkerboard nextNextBoard = pq.peek();
                if (nextNextBoard != null) {
                    nextValue = getEvaluator().applyAsInt(nextNextBoard);
                }
                if (nextValue == 0) { nextValue++; }
                if ((1+nextValue)*2 < thisValue) { //Singular Extension
                    depthLimit += 3;
                }
            }
            if (nextBoard.getCurrentPlayer() == c.getCurrentPlayer()) { multiplier = 1; } else { multiplier = -1; }
            nextScore = Math.max(nextScore, multiplier * NegaMax(nextBoard, depthLimit-1, multiplier, -beta, -alpha));
            alpha = Math.max(alpha, nextScore);
            if (alpha >= beta) { break; }
        }
        return nextScore;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Optional<Duple<Integer, Move>> best = Optional.empty();
        for (Checkerboard nextBoard: board.getNextBoards()) {
            numNodes += 1;
            int multiplier = board.getCurrentPlayer() != nextBoard.getCurrentPlayer() ? -1 : 1;
            int scoreFor = NegaMax(nextBoard, getDepthLimit(), multiplier, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (best.isEmpty() || best.get().getFirst() < scoreFor) {
                best = Optional.of(new Duple<>(scoreFor, nextBoard.getLastMove()));
            }
        }
        return best;
    }
}
