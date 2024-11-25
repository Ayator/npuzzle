public class PuzzleSymbol {
    public int x, y, index;

    private PuzzleSymbol(int x, int y, int index){
        this.x = x;
        this.y = y;
        this.index = index;
    }

    public static PuzzleSymbol FromIndex(int dashIndex, int puzzleSize){
        return new PuzzleSymbol(dashIndex % puzzleSize, dashIndex / puzzleSize, dashIndex);
    }

    public static PuzzleSymbol FromXY(int x, int y, int puzzleSize){
        return new PuzzleSymbol(x, y, puzzleSize * y + x);
    }

    public static int manhattanDistance(PuzzleSymbol a, PuzzleSymbol b){
        return xDistance(a, b) + yDistance(a, b);
    }

    public static int xDistance(PuzzleSymbol a, PuzzleSymbol b){
        return Math.abs(a.x - b.x);
    }

    public static int yDistance(PuzzleSymbol a, PuzzleSymbol b){
        return Math.abs(a.y - b.y);
    }

    public char getSymbolFromState(State s){
        return s.getStateId().charAt(this.index);
    }
}
