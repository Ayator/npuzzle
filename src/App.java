import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static void main(String[] args){
        State goalState = new State("12345678-", 3);
        // State goalState = new State("123456789:;<=>?-", 4);
        State initialState = new State("12345678-", 3);
        // State initialState = new State("4-8;62913:<>=?57", 4);
        // State initialState = new State(":;1>-98?7536<4=2", 4);
        initialState.setF(goalState, 0, true);
        Game game = new Game(initialState, goalState);
        game.setVisible(true);
        game.scrambleBoard();
    }

    public static void newGame(State currentState, State goalState){
        Game game = new Game(currentState, goalState);
        game.setVisible(true);
        game.scrambleBoard();
    }

    public static Pair<State, Integer> AStar(State start, State goal, Game game, boolean useManhattan){
        BinarySearchTree frontier = new BinarySearchTree();
        // empty move sequence just in case
        start.setMoveSequence("");
        start.setF(goal, 0, useManhattan);
        frontier.insert(start);
        // visited states (stateId to a-star score)
        // using HashSet would have saved me a lot of trouble
        HashMap<String, Integer> visited = new HashMap<String, Integer>();
        State front = frontier.leftmostState(true);
        // keep going till the h score is 0
        while(front.getH() != 0){
            System.out.println("Front: " + front.getStateId() + " (" + front.getH() + "+" + front.getG() + ") = " + front.getF());
            // next moves (states list)
            System.out.println();
            // get the list of possible next states
            ArrayList<State> next = front.getNextStates(goal, useManhattan);
            for(State s : next){
                // if the state hasn't been visited yet, or if the a-score is lower, insert to frontiers
                if(!visited.containsKey(s.getStateId()) || s.getF() < visited.get(s.getStateId())){
                    frontier.insert(s);
                    System.out.println(s.getMoveSequence() + ": " + s.getF() + " added to frontier states.");
                }
            }
            // mark state as visited, and save a-score
            visited.put(front.getStateId(), front.getF());
            front = frontier.leftmostState(true);
        }
        return new Pair<State, Integer>(front, new Integer(visited.size()));
    }
}
