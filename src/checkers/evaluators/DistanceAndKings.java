package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.Piece;
import checkers.core.PlayerColor;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class DistanceAndKings implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        PlayerColor i = c.getCurrentPlayer();
        PlayerColor u = c.getCurrentPlayer().opponent();
        int basic       = c.numPiecesOf(i) - c.numPiecesOf(u);
        int kings       = c.numKingsOf(i) - c.numKingsOf(u);
        int rDistance   = 0;
        int bDistance   = 0;
        int bHomeRow    = 0;
        int rHomeRow    = 0;
        int legalMoves = c.getLegalMoves(i).size() - c.getLegalMoves(u).size();
        for (int j = 0; j<64; j++) {
            Optional<Piece> piece = c.pieceAt(j % 8, j / 8);
            if (piece.isPresent()) {
                if (piece.get().getColor() == PlayerColor.BLACK)    {
                    bDistance += 8 - j/8;
                    if (j>55) {bHomeRow++;}
                }
                else {
                    rDistance += j/8;
                    if (j<8) {rHomeRow++;}
                }
            }
        }
        int redHR   = rHomeRow - bHomeRow;
        int redDist = rDistance - bDistance;
        boolean red = i == PlayerColor.RED;
        return legalMoves + 2*basic + kings;
    }
}
