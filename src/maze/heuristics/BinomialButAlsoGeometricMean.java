package maze.heuristics;

import maze.core.MazeExplorer;

import java.util.function.ToIntFunction;


public class BinomialButAlsoGeometricMean implements ToIntFunction<MazeExplorer> {
    @Override
    public int applyAsInt(MazeExplorer node) {
        int a = node.getAllTreasureFromMaze().size()+1;
        int b = node.getNumTreasuresFound()+1;
        int c = node.getLocation().getX();
        int d = node.getLocation().getY();
        int gx = node.getGoal().getLocation().getX();
        int gy = node.getGoal().getLocation().getY();
        int e = (c-gx)*(c-gx) + (d-gy)*(d-gy);
        return (int) Math.pow(xChooseY(a,b) * xChooseY((e+1)%((c^d)+1),avg(c,d)),0.5);
    }
    public int factorial(int n) {
        if (n<1) return 1;
        return n*(factorial(n-1));
    }

    public int xChooseY(int x, int y) {
        if (x == 0 || y==0 || x==y) {return 1;}
        return factorial(x) / (factorial(y) * factorial(x-y));
    }

    public int avg(int a, int b) {return (int) Math.pow(a*b,0.5);}
}
