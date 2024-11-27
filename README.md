# N-Puzzle Solver

This project implements a solution to the sliding puzzle problem using an A* search algorithm. The core of the solution involves representing puzzle states, calculating heuristics, and generating the next possible states based on valid moves. The project also includes checks for puzzle solvability based on inversions and puzzle size.

---


## Classes

### 1. `Move.java`
This class defines an enum for the four valid moves in the sliding puzzle: Up (`U`), Down (`D`), Left (`L`), and Right (`R`). Each move has:
- A validity check (`isValid(State s)`) to determine if the move can be applied to the current puzzle state.
- A method to generate the next state (`nextState(State s, State goal, boolean useManhattan)`).
- A heuristic method (`nextHValue(State s, State goal, PuzzleSymbol a, PuzzleSymbol b, boolean useManhattan)`) to calculate how far the state is from the goal based on either Manhattan distance or a simpler heuristic.

### 2. `Pair.java`
A simple generic class to hold pairs of objects. This is used to store pairs of states or other related data during the puzzle-solving process.

### 3. `PuzzleSymbol.java`
Represents a single tile (or symbol) in the puzzle grid. Each `PuzzleSymbol` contains:
- `x`, `y` coordinates for its position.
- An `index`, representing its position in the puzzle state string.
  
It also includes methods to:
- Create a `PuzzleSymbol` from an index or `x`, `y` coordinates.
- Calculate Manhattan distance between two `PuzzleSymbol` objects.
- Retrieve the symbol from a state string.

### 4. `State.java`
Represents a specific state of the puzzle. It contains:
- A `stateId` string, which is a representation of the current puzzle configuration, including the dash symbol (`'-'`) that represents the empty space.
- `puzzleSize` to represent the dimensions of the puzzle (e.g., 3x3 for a 3x3 puzzle).
- The position of the empty space (`dash`) and the move sequence that led to this state.

Key methods include:
- Heuristic calculation (`setH()`) and total cost calculation (`setF()`).
- `getNextStates(State goalState, boolean useManhattan)` to generate all valid next states.
- `isSolvable(State goal)` to check if a puzzle configuration is solvable based on inversions.
- Utility methods for validating state strings and retrieving puzzle symbols.

### 5. `Main.java`
This class serves as the entry point for the program. It demonstrates how to use the `State` and `Move` classes to solve a puzzle. It includes:
- Initializing a start state and goal state.
- Checking if the puzzle is solvable.
- Running the A* search algorithm to find the solution path.

---

## How It Works

1. **Puzzle Representation**: Each state of the puzzle is represented by a string (e.g., `"1-345678"`) where each character corresponds to a tile, and `'-'` represents the empty space.
2. **Heuristic Calculation**: A* search uses a heuristic to evaluate how close a given state is to the goal. The Manhattan distance is used by default, but a simplified heuristic can be selected.
3. **Valid Moves**: The puzzle's empty space can move in four directions: up, down, left, and right. The program generates all possible next states based on these moves.
4. **Solving the Puzzle**: The A* search algorithm explores the puzzle states, aiming to find the shortest path to the goal state.
5. **Solvability**: Before attempting to solve the puzzle, the program checks if the puzzle is solvable based on inversion counts.

---

## Features

- Configurable N-Puzzle size.
- A* algorithm for solving the 8-Puzzle.
- Puzzle state visualization.

## Usage

1. Clone the repository.
2. Compile the Java files: `javac src/*.java`.
3. Run the main program: `java -cp src App`.
