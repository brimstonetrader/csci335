package checkers.evaluators;

import checkers.core.Checkerboard;

import java.util.function.ToIntFunction;

public class BasicMaterial implements ToIntFunction<Checkerboard> {
    public int applyAsInt(Checkerboard c) {
        return c.numPiecesOf(c.getCurrentPlayer()) - c.numPiecesOf(c.getCurrentPlayer().opponent());
    }
}
