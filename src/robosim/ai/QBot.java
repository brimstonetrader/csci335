package robosim.ai;

import robosim.core.*;
import robosim.reinforcement.QTable;
import robosim.core.Simulator;
import java.util.LinkedHashMap;

public class QBot implements Controller {
    private QTable qtable = new QTable(87,3,0,8,64,0.5);
    private double turns = 1.0;
    private double reward = 1.0;
    private int lMove = 0;
    private int llMove = 0;
    private int lllMove = 0;
    private int llllMove = 0;
    private int dirtCount = 0;
    @Override
    public void control(Simulator sim) {
        int s = 0;
        if (sim.dirtExistsOnTheRight()) { s=85; }
        if (sim.dirtExistsOnTheLeft())  { s=84; }
        if (sim.dirtExistsHere()) {
            s=83;
            if (lMove==0) { reward++; }
        }
        if (sim.getDirt() > dirtCount) {
            s=86;
            reward+=100;
            dirtCount = sim.getDirt();
        }
        if (lMove == 0) {
            reward++;
            turns=1;
        }
        if (lMove >  0) {
            reward=1;
            turns++;
        }
        if (sim.findClosestProblem() < 5) {
            reward =- 100;
            s = 81;
        }
        if (sim.wasHit()) {
            reward=-200;
            s = 83;
        }
        int state = Math.max(s,lMove+llMove+lllMove+llllMove);
        int action = qtable.senseActLearn(state, reward-turns);
        llllMove = 3*lllMove;
        lllMove = 3*llMove;
        llMove = 3*lMove;
        if (action == 2) {
            Action.RIGHT.applyTo(sim);
            lMove = 2;
        }
        if (action == 1) {
            Action.LEFT.applyTo(sim);
            lMove = 1;
        }
        else {
            Action.FORWARD.applyTo(sim);
            lMove = 0;
        }
    }
}