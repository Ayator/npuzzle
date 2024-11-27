# N-Puzzle Solver

This project implements a solution to the sliding puzzle problem using an A* search algorithm. The core of the solution involves representing puzzle states, calculating heuristics, and generating the next possible states based on valid moves. The project also includes checks for puzzle solvability based on inversions and puzzle size.

---

## Class Breakdown

### `Move.java`
The `Move` enum represents the four valid moves for the sliding puzzle: Down (`D`), Left (`L`), Right (`R`), and Up (`U`). Each move has:
- A validity check (`isValid(State s)`) to determine if it can be applied to a given state.
- A `nextState(State s, State goal, boolean useManhattan)` method to generate the next state after the move.
- A heuristic calculation (`nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan)`), which computes how far the current state is from the goal state based on the Manhattan distance or a simplified heuristic.

### `Pair.java`
A simple generic class to hold pairs of objects (`T1` and `T2`). This class is used to store pairs of states or other related data in the program.

### `PuzzleSymbol.java`
The `PuzzleSymbol` class represents the position and index of a puzzle tile (or "symbol") within the puzzle. Each tile is associated with:
- `x`, `y` coordinates for its position in the puzzle grid.
- An `index`, which represents its position in the state string.
It also includes methods to:
- Calculate Manhattan distance between two symbols.
- Create a `PuzzleSymbol` from an index or a set of `x` and `y` coordinates.
- Retrieve the symbol from a puzzle state string.

### `State.java`
The `State` class represents a state in the puzzle-solving process. A state is defined by:
- A `stateId` string that represents the current arrangement of puzzle tiles, including the dash (`'-'`) which represents the empty space.
- The `puzzleSize`, representing the size of the puzzle grid (e.g., 3x3 for a 3x3 puzzle).
- A `dash` object, which holds the coordinates and index of the empty space.
- The `moveSequence`, which records the sequence of moves that led to this state.

Key methods in this class include:
- Methods to calculate and set the heuristic (`setH()`) and total cost (`setF()`).
- `getNextStates(State goalState, boolean useManhattan)` to generate all valid next states from the current state.
- `isSolvable(State goal)` to check if a given state is solvable based on inversions and puzzle size.
- Utility methods to validate a state string and retrieve puzzle symbols.

---

## How It Works

1. **Initial Setup**: The puzzle starts with an initial state (a string representing the configuration of the puzzle) and a goal state (the solved configuration).
2. **Heuristic Calculation**: Each state is assigned a heuristic based on how far it is from the goal state, using either Manhattan distance or a simpler heuristic.
3. **Valid Moves**: The puzzle can move the empty space (the dash symbol) in four directions (up, down, left, right), and the program generates new states based on these moves.
4. **A* Search**: The program uses the A* search algorithm to explore the puzzle space, moving towards the goal state while minimizing the cost (`f = g + h`), where `g` is the cost to reach the current state and `h` is the heuristic.
5. **Solvability**: Before starting, the program checks if the puzzle is solvable using the inversion count method.

---


## Features

- Configurable N-Puzzle size.
- A* algorithm for solving the 8-Puzzle.
- Puzzle state visualization.

## Usage

1. Clone the repository.
2. Compile the Java files: `javac src/*.java`.
3. Run the main program: `java -cp src App`.
