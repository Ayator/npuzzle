enum Move{
    D("Down"){
        @Override
        boolean isValid(State s){
            if(s.getDash().y < s.getPuzzleSize() - 1)
                return true;
            return false;
        }

        @Override
        State nextState(State s, State goal, boolean useManhattan) {
            PuzzleSymbol a = s.getDash();
            PuzzleSymbol b = PuzzleSymbol.FromXY(s.getDash().x, s.getDash().y + 1, s.getPuzzleSize());
            StringBuilder newId = new StringBuilder(s.getStateId());
            newId.setCharAt(a.index, b.getSymbolFromState(s));
            newId.setCharAt(b.index, '-');
            State nextState = new State(newId.toString(), s.getPuzzleSize());
            nextState.setF(nextHValue(s, goal, a, b, useManhattan), s.getG() + 1);
            nextState.setMoveSequence(s.getMoveSequence() + this.toString());
            return nextState;
        }

        @Override
        protected int nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan) {
            char symbolAtB = b.getSymbolFromState(s);
            int indexOfBAtGoal = goal.getStateId().indexOf(symbolAtB);
            PuzzleSymbol g = PuzzleSymbol.FromIndex(indexOfBAtGoal, goal.getPuzzleSize());
            int deltaH = 0;// it remains the same by default
            if(useManhattan)
                deltaH = PuzzleSymbol.yDistance(a, g) - PuzzleSymbol.yDistance(b, g);
            // if the objective was already in a good position, but it's being moved
            else if(b.index == g.index)
                deltaH = 1;
            // if the objective is being moved to it's correct position
            else if(a.index == g.index)
                deltaH = -1;
            return s.getH() + deltaH;
        }
    },
    L("Left"){
        @Override
        boolean isValid(State s){
            if(s.getDash().x > 0)
                return true;
            return false;
        }

        @Override
        State nextState(State s, State goal, boolean useManhattan) {
            PuzzleSymbol a = s.getDash();
            PuzzleSymbol b = PuzzleSymbol.FromXY(s.getDash().x - 1, s.getDash().y, s.getPuzzleSize());
            StringBuilder newId = new StringBuilder(s.getStateId());
            newId.setCharAt(a.index, b.getSymbolFromState(s));
            newId.setCharAt(b.index, '-');
            State nextState = new State(newId.toString(), s.getPuzzleSize());
            nextState.setF(nextHValue(s, goal, a, b, useManhattan), s.getG() + 1);
            nextState.setMoveSequence(s.getMoveSequence() + this.toString());
            return nextState;
        }

        @Override
        protected int nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan) {
            char symbolAtB = b.getSymbolFromState(s);
            int indexOfBAtGoal = goal.getStateId().indexOf(symbolAtB);
            PuzzleSymbol g = PuzzleSymbol.FromIndex(indexOfBAtGoal, goal.getPuzzleSize());
            int deltaH = 0;// it remains the same by default
            if(useManhattan)
                deltaH = PuzzleSymbol.xDistance(a, g) - PuzzleSymbol.xDistance(b, g);
            // if the objective was already in a good position, but it's being moved
            else if(b.index == g.index)
                deltaH = 1;
            // if the objective is being moved to it's correct position
            else if(a.index == g.index)
                deltaH = -1;
            return s.getH() + deltaH;
        }
    },
    R("Right"){
        @Override
        boolean isValid(State s){
            if(s.getDash().x < s.getPuzzleSize() - 1)
                return true;
            return false;
        }

        @Override
        State nextState(State s, State goal, boolean useManhattan) {
            PuzzleSymbol a = s.getDash();
            PuzzleSymbol b = PuzzleSymbol.FromXY(s.getDash().x + 1, s.getDash().y, s.getPuzzleSize());
            StringBuilder newId = new StringBuilder(s.getStateId());
            newId.setCharAt(a.index, b.getSymbolFromState(s));
            newId.setCharAt(b.index, '-');
            State nextState = new State(newId.toString(), s.getPuzzleSize());
            nextState.setF(nextHValue(s, goal, a, b, useManhattan), s.getG() + 1);
            nextState.setMoveSequence(s.getMoveSequence() + this.toString());
            return nextState;
        }

        @Override
        protected int nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan) {
            char symbolAtB = b.getSymbolFromState(s);
            int indexOfBAtGoal = goal.getStateId().indexOf(symbolAtB);
            PuzzleSymbol g = PuzzleSymbol.FromIndex(indexOfBAtGoal, goal.getPuzzleSize());
            int deltaH = 0;// it remains the same by default
            if(useManhattan)
                deltaH = PuzzleSymbol.xDistance(a, g) - PuzzleSymbol.xDistance(b, g);
            // if the objective was already in a good position, but it's being moved
            else if(b.index == g.index)
                deltaH = 1;
            // if the objective is being moved to it's correct position
            else if(a.index == g.index)
                deltaH = -1;
            return s.getH() + deltaH;
        }
    },
    U("Up"){
        @Override
        boolean isValid(State s){
            if(s.getDash().y > 0)
                return true;
            return false;
        }

        @Override
        State nextState(State s, State goal, boolean useManhattan) {
            PuzzleSymbol a = s.getDash();
            PuzzleSymbol b = PuzzleSymbol.FromXY(s.getDash().x, s.getDash().y - 1, s.getPuzzleSize());
            StringBuilder newId = new StringBuilder(s.getStateId());
            newId.setCharAt(a.index, b.getSymbolFromState(s));
            newId.setCharAt(b.index, '-');
            State nextState = new State(newId.toString(), s.getPuzzleSize());
            nextState.setF(nextHValue(s, goal, a, b, useManhattan), s.getG() + 1);
            nextState.setMoveSequence(s.getMoveSequence() + this.toString());
            return nextState;
        }

        @Override
        protected int nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan) {
            char symbolAtB = b.getSymbolFromState(s);
            int indexOfBAtGoal = goal.getStateId().indexOf(symbolAtB);
            PuzzleSymbol g = PuzzleSymbol.FromIndex(indexOfBAtGoal, goal.getPuzzleSize());
            int deltaH = 0;// it remains the same by default
            if(useManhattan)
                deltaH = PuzzleSymbol.yDistance(a, g) - PuzzleSymbol.yDistance(b, g);
            // if the objective was already in a good position, but it's being moved
            else if(b.index == g.index)
                deltaH = 1;
            // if the objective is being moved to it's correct position
            else if(a.index == g.index)
                deltaH = -1;
            return s.getH() + deltaH;
        }
    };

    private String name;

    Move(final String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    abstract boolean isValid(State s);
    abstract State nextState(State s, State goal, boolean useManhattan);
    protected abstract int nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan);
}