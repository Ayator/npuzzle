import java.util.ArrayList;

// public class State implements Comparable<State>{
public class State {
    private String stateId;
    private int puzzleSize;
    private PuzzleSymbol dash;
    private String moveSequence;

    private int f, g, h;

    // constructors
    public State(String stateId, int puzzleSize){
        this.puzzleSize = puzzleSize;
        this.setStateId(stateId);
        this.setMoveSequence("");
    }

    public State(String stateId){
        this(stateId, 3);
    }

    // getters and setters
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
        this.dash = PuzzleSymbol.FromIndex(stateId.indexOf('-'), puzzleSize);
    }

    public String getMoveSequence() {
        return moveSequence;
    }

    public void setMoveSequence(String moveSequence) {
        this.moveSequence = moveSequence;
    }

    public int getPuzzleSize() {
        return puzzleSize;
    }

    public PuzzleSymbol getDash() {
        return dash;
    }

    // H/G/F
    private int setH(State goal, boolean useManhattan){
        PuzzleSymbol[] stateSymbols = this.getPuzzleSymbols();
        PuzzleSymbol[] goalSymbols = goal.getPuzzleSymbols();
        int h = 0;
        for(int i = 1; i < Math.pow(puzzleSize, 2); i++)
            if(useManhattan)
                h += PuzzleSymbol.manhattanDistance(stateSymbols[i], goalSymbols[i]);
            else if(stateSymbols[i].index != goalSymbols[i].index)
                h++;
        this.h = h;
        return h;
    }

    private int setH(int h){
        this.h = h;
        return h;
    }

    private int setG(int g){
        this.g = g;
        return g;
    }

    public int setF(State goal, int gValue, boolean useManhattan){
        this.f = this.setH(goal, useManhattan) + this.setG(gValue);
        return this.f;
    }

    public int setF(int hValue, int gValue){
        this.f = this.setH(hValue) + this.setG(gValue);
        return this.f;
    }

    public int getH(){
        return this.h;
    }

    public int getG(){
        return this.g;
    }

    public int getF(){
        return this.f;
    }

    // utility functions
    // get n^2 array of puzzle symbols
    public PuzzleSymbol[] getPuzzleSymbols(){
        int size = (int) Math.pow(puzzleSize, 2);
        PuzzleSymbol[] symbols = new PuzzleSymbol[size];
        symbols[0] = this.dash;
        for(int i = 1; i < size; i++)
            symbols[i] = PuzzleSymbol.FromIndex(this.stateId.indexOf('0' + i), this.puzzleSize);
        return symbols;
    }

    // get next states
    public ArrayList<State> getNextStates(State goalState, boolean useManhattan){
        ArrayList<State> states = new ArrayList<State>();
        for(Move m : Move.values())
            if(m.isValid(this)){
                // System.out.println(getH() + " + " + getG() + " = " + getF());
                // System.exit(0);
                states.add(m.nextState(this, goalState, useManhattan));
            }
        return states;
    }

    // check if a state, given a goal, is solvable
    // count inversions of both state and goal states
    // add them up
    public boolean isSolvable(State goal){
        int c = 0;
        int size = (int) Math.pow(this.puzzleSize, 2);
        for(int i = 0; i < size; i++){
            for(int j = i + 1; j < size; j++){
                char a = this.getStateId().charAt(i);
                char b = this.getStateId().charAt(j);
                if(a > '0' && b > '0' && a > b)
                    c++;
                a = goal.getStateId().charAt(i);
                b = goal.getStateId().charAt(j);
                if(a > '0' && b > '0' && a > b)
                    c++;
            }
        }
        // for even puzzle size, the number of inversions plus the dashes rows have to be even
        if(this.getPuzzleSize() % 2 == 0)
            c += this.getDash().y + goal.getDash().y;
        // for odd puzzle size, the number of inversions have to be even
        return c % 2 == 0;
    }

    public static boolean isValidState(String str, int puzzleSize){
        if(str == null)
            return false;
        int size = (int) Math.pow(puzzleSize, 2);
        if(str.length() != size)
            return false;
        if(str.indexOf('-') == -1)
            return false;
        for(int i = 1; i < size; i++)
            if(str.indexOf('0' + i) == -1)
                return false;
        return true;
    }

    // @Override
    // public int compareTo(State o) {
    //     int deltaF = this.getF() - o.getF();
    //     if(deltaF == 0)
    //         return this.getStateId().compareToIgnoreCase(o.getStateId());
    //     return deltaF;
    // }
}
