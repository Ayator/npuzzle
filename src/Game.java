import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JFrame {
    public class MoveAction extends AbstractAction {
        Move move;
    
        public MoveAction(Move move){
            this.move = move;
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            if(move != null && move.isValid(currentState)){
                currentState = move.nextState(currentState, goalState, true);
                currentState.setMoveSequence("");
                solution = null;
                updateBoard();
            }
        }
    }
    
    private JPanel panel;
    private Rectangle panelRect;
    private Dimension windowDimension;

    private State currentState;
    private State goalState;

    private JPanel content;
    private JLabel[] squares;
    private int puzzleSize;
    private int squareWidth;
    private ArrayList<JButton> buttons;
    private JTextArea outputTextArea;
    private Pair<State, Integer> solution;
    private boolean actionsEnabled;

    public Game(State s, State goal){
        this.currentState = s;
        this.goalState = goal;
        this.solution = null;
        this.puzzleSize = s.getPuzzleSize();
        this.actionsEnabled = true;

        setTitle(((int)Math.pow(s.getPuzzleSize(), 2) - 1) + "-PUZZLE");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(100, 0);
        windowDimension = new Dimension(630, 600);
        setSize(windowDimension);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        content = new JPanel();
        content.setBackground(Color.BLACK);
        content.setLayout(null);

        titleSetUp();

        squaresSetUp();
        updateBoard();
        buttonsSetUp(content);

        setInput();
        setContentPane(content);
    }

    private void titleSetUp(){
        JLabel title = new JLabel(getTitle());
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setBounds(0, 0, (int) windowDimension.getWidth(), 50);
        title.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(!actionsEnabled)
                    return;
                String[] puzzles = new String[20];
                for(int i = 0; i < puzzles.length; i++)
                    puzzles[i] = ((int) Math.pow(i + 1, 2) - 1) + "-Puzzle";
                String input = (String) JOptionPane.showInputDialog(null, "Puzzle:", "[n^2-1-Puzzle!]",
                    JOptionPane.QUESTION_MESSAGE, null, puzzles, puzzles[2]);
                if(input == null)
                    return;
                int n = Integer.parseInt(input.substring(0, input.indexOf('-')));
                setTitle(n + "-PUZZLE");
                title.setText(getTitle());
                StringBuilder newState = new StringBuilder("");
                for(int i = 1; i <= n; i++)
                    newState.append((char) ('0' + i));
                newState.append((char) ('-'));
                
                currentState = new State(newState.toString(), (int) Math.sqrt(n + 1));
                goalState = new State(newState.toString(), (int) Math.sqrt(n + 1));
                currentState.setF(goalState, 0, true);
                // restart
                content.remove(panel);
                repaint();
                solution = null;
                puzzleSize = currentState.getPuzzleSize();
                squareWidth = (int) (panelRect.getWidth() / currentState.getPuzzleSize());
                squaresSetUp();
                setInput();
                updateBoard();
            }
        });
        content.add(title);
    }

    private void squaresSetUp(){
        // panel set up
        panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panelRect = new Rectangle(10, 60, 400, 400);
        panel.setBounds(panelRect);
        panel.setLayout(null);
        content.add(panel);

        squareWidth = (int) (panelRect.getWidth() / currentState.getPuzzleSize());

        PuzzleSymbol[] symbols = currentState.getPuzzleSymbols();
        squares = new JLabel[symbols.length];
        for(int i = 0; i < squares.length; i++){
            squares[i] = new JLabel("");
            squares[i].setName(i + "");
            squares[i].setBounds(squareWidth * (i % currentState.getPuzzleSize()), squareWidth * (i / currentState.getPuzzleSize()), squareWidth, squareWidth);
            squares[i].setFont(new Font("Monospaced", Font.BOLD, squareWidth / 2));
            squares[i].setForeground(Color.WHITE);
            squares[i].setBackground(Color.WHITE);
            squares[i].setOpaque(false);
            squares[i].setHorizontalAlignment(SwingConstants.CENTER);
            squares[i].setVerticalAlignment(SwingConstants.CENTER);
            squares[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
            squares[i].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    if(!actionsEnabled)
                        return;
                    JLabel label = (JLabel) e.getSource();
                    int name = Integer.parseInt(label.getName());
                    PuzzleSymbol clicked = PuzzleSymbol.FromIndex(name, currentState.getPuzzleSize());
                    int deltaX = currentState.getDash().x - clicked.x;
                    int deltaY = currentState.getDash().y - clicked.y;
                    if(Math.abs(deltaX + deltaY) == 1){
                        // DLRU
                        Move m = null;
                        if(deltaY == -1)
                            m = Move.D;
                        else if(deltaX == 1)
                            m = Move.L;
                        else if(deltaX == -1)
                            m = Move.R;
                        else if(deltaY == 1)
                            m = Move.U;
                        if(m != null && m.isValid(currentState)){
                            currentState = m.nextState(currentState, goalState, true);
                            currentState.setMoveSequence("");
                            solution = null;
                            updateBoard();
                        }
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e){
                    JLabel label = (JLabel) e.getSource();
                    label.setForeground(Color.BLACK);
                    label.setOpaque(true);
                }
                @Override
                public void mouseExited(MouseEvent e){
                    JLabel label = (JLabel) e.getSource();
                    label.setForeground(Color.WHITE);
                    label.setOpaque(false);
                }
            });
            panel.add(squares[i]);
        }
    }

    private void buttonsSetUp(JPanel content){
        // buttons
        buttons = new ArrayList<JButton>();
        JButton shuffleButton = new JButton("Shuffle");
        JButton solveButton = new JButton("Solve");
        JButton setStateButton = new JButton("Set State");
        JButton setGoalButton = new JButton("Set Goal");
        JButton giveUpButton = new JButton("Check Solution");
        buttons.add(shuffleButton);
        buttons.add(solveButton);
        buttons.add(setStateButton);
        buttons.add(setGoalButton);
        buttons.add(giveUpButton);
        // shuffle
        shuffleButton.setSize(200, 100);
        shuffleButton.setLocation(
            (int) (panelRect.x + panelRect.getWidth()),
            (int) (panelRect.y)
        );
        shuffleButton.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 6));
        shuffleButton.setHorizontalAlignment(SwingConstants.CENTER);
        shuffleButton.setForeground(Color.WHITE);
        shuffleButton.setBackground(Color.BLACK);
        shuffleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                scrambleBoard();
            }
        });
        // solve
        solveButton.setSize(200, 100);
        solveButton.setLocation(shuffleButton.getX(), shuffleButton.getY() + shuffleButton.getHeight());
        solveButton.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 6));
        solveButton.setHorizontalAlignment(SwingConstants.CENTER);
        solveButton.setForeground(Color.WHITE);
        solveButton.setBackground(Color.BLACK);
        solveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int useIt = JOptionPane.showOptionDialog(null, "What heuristic do you want to use to compute the A* Score?", "Heuristic", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Total distance of each piece from the goal", "Number of pieces out of place"}, JOptionPane.YES_OPTION);
                if(useIt == 0)
                    solveBoard(true);
                else if(useIt == 1)
                    solveBoard(false);
            }
        });
        // set current state
        setStateButton.setSize(200, 100);
        setStateButton.setLocation(solveButton.getX(), solveButton.getY() + solveButton.getHeight());
        setStateButton.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 6));
        setStateButton.setHorizontalAlignment(SwingConstants.CENTER);
        setStateButton.setForeground(Color.WHITE);
        setStateButton.setBackground(Color.BLACK);
        setStateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String input = JOptionPane.showInputDialog("Set current state:");
                while(input != null && !State.isValidState(input, puzzleSize)){
                    input = JOptionPane.showInputDialog(null, "Set current state:", "Wrong Input", JOptionPane.ERROR_MESSAGE);
                }
                if(input != null){
                    currentState = new State(input, puzzleSize);
                    currentState.setF(goalState, 0, true);
                    updateBoard();
                }
            }
        });
        // set goal state
        setGoalButton.setSize(200, 100);
        setGoalButton.setLocation(setStateButton.getX(), setStateButton.getY() + setStateButton.getHeight());
        setGoalButton.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 6));
        setGoalButton.setHorizontalAlignment(SwingConstants.CENTER);
        setGoalButton.setForeground(Color.WHITE);
        setGoalButton.setBackground(Color.BLACK);
        setGoalButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String input = JOptionPane.showInputDialog("Set goal state:");
                while(input != null && !State.isValidState(input, puzzleSize)){
                    input = JOptionPane.showInputDialog(null, "Set goal state:", "Wrong Input", JOptionPane.ERROR_MESSAGE);
                }
                if(input != null){
                    goalState = new State(input, puzzleSize);
                    currentState.setF(goalState, 0, true);
                    updateBoard();
                }
            }
        });
        // generate solution
        giveUpButton.setSize(200, 100);
        giveUpButton.setLocation(setGoalButton.getX(), setGoalButton.getY() + setGoalButton.getHeight());
        giveUpButton.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 6));
        giveUpButton.setHorizontalAlignment(SwingConstants.CENTER);
        giveUpButton.setForeground(Color.WHITE);
        giveUpButton.setBackground(Color.BLACK);
        giveUpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                generateSolution(true);
            }
        });
        // info text area
        outputTextArea = new JTextArea();
        outputTextArea.setSize((int) panelRect.getWidth(), 100);
        outputTextArea.setLocation((int) panelRect.getX(), (int) (panelRect.getY() + panelRect.getHeight()));
        outputTextArea.setFont(new Font("Monospaced", Font.PLAIN, squareWidth / 10));
        outputTextArea.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        outputTextArea.setForeground(Color.WHITE);
        outputTextArea.setBackground(Color.BLACK);
        outputTextArea.setEditable(false);
        content.add(outputTextArea);

        for(JButton b : buttons){
            content.add(b);
        }
    }

    private void setInput(){
        InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();
        // DLRU
        HashMap<KeyStroke, Move> controlMap = new HashMap<KeyStroke, Move>();
        controlMap.put(KeyStroke.getKeyStroke("S"), Move.D);
        controlMap.put(KeyStroke.getKeyStroke("A"), Move.L);
        controlMap.put(KeyStroke.getKeyStroke("D"), Move.R);
        controlMap.put(KeyStroke.getKeyStroke("W"), Move.U);
        controlMap.put(KeyStroke.getKeyStroke("DOWN"), Move.D);
        controlMap.put(KeyStroke.getKeyStroke("LEFT"), Move.L);
        controlMap.put(KeyStroke.getKeyStroke("RIGHT"), Move.R);
        controlMap.put(KeyStroke.getKeyStroke("UP"), Move.U);
        controlMap.forEach((k, v) -> inputMap.put(k, v));
        for(Move m : Move.values())// foreach doesn't work on arrays
            actionMap.put(m, new MoveAction(m));
    }

    public void scrambleBoard(){
        // generate a random number relatively big for n-puzzle
        int n = (int) (Math.random() * (31 - 25 + 1) + Math.pow(puzzleSize, 3));
        if(puzzleSize == 1)
            return;
        for(JButton b : buttons)
            b.setEnabled(false);
        actionsEnabled = false;
        outputTextArea.setText("Scrambled.");
        // set up a timer to execute 10 times per second
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int i;
            String previous;
            @Override
            public void run() {
                ArrayList<State> next = currentState.getNextStates(goalState, true);
                int rn;
                do rn = (int) (Math.random() * next.size());
                while(next.get(rn).getStateId().equals(previous));
                previous = currentState.getStateId();
                currentState = next.get(rn);
                updateBoard();
                i++;
                // make sure to leave the loop
                if(i >= n){
                    currentState.setF(goalState, 0, true);
                    currentState.setMoveSequence("");
                    updateBoard();
                    solution = null;
                    for(JButton b : buttons)
                        b.setEnabled(true);
                    actionsEnabled = true;
                    this.cancel();
                }
            }
        }, new Date(), (int) (1000 / Math.sqrt(n)));
    }

    private boolean generateSolution(boolean useManhattan){
        if(currentState.isSolvable(goalState)){
            actionsEnabled = false;
            solution = App.AStar(currentState, goalState, this, useManhattan);
            actionsEnabled = true;
            outputTextArea.setText(
                "Solution:" + solution.t1.getMoveSequence() +
                "\nStates visited: " + solution.t2 +
                "\nInitial state: " + currentState.getStateId() +
                "\nFinal state: " + solution.t1.getStateId() +
                "\nA-Star score: " + solution.t1.getF()
            );
            System.out.println(
                "\nSolution:" + solution.t1.getMoveSequence() +
                "\nStates visited: " + solution.t2 +
                "\nInitial state: " + currentState.getStateId() +
                "\nFinal state: " + solution.t1.getStateId() +
                "\nA-Star score: " + solution.t1.getF()
            );
            return true;
        }
        else{
            outputTextArea.setText("Not solvable.");
            return false;
        }
    }

    private void solveBoard(boolean useManhattan){
        if(solution == null)
            if(!generateSolution(useManhattan))
                return;
        if(solution.t1.getMoveSequence().length() == 0)
            return;
        for(JButton b : buttons)
            b.setEnabled(false);
        actionsEnabled = false;
        currentState.setF(goalState, 0, useManhattan);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int i;//default 0
            
            @Override
            public void run() {
                // can I do something better than + "" ?
                Move nextMove = Move.valueOf(solution.t1.getMoveSequence().charAt(i) + "");
                currentState = nextMove.nextState(currentState, goalState, useManhattan);
                updateBoard();
                i++;
                if(i >= solution.t1.getMoveSequence().length()){
                    currentState.setF(goalState, 0, useManhattan);
                    currentState.setMoveSequence("");
                    updateBoard();
                    solution = null;
                    for(JButton b : buttons)
                        b.setEnabled(true);
                    actionsEnabled = true;
                    this.cancel();
                }
            }
        }, new Date(), 100);
    }

    public void updateBoard(State s){
        currentState = s;
        updateBoard();
    }
    
    private void updateBoard(){
        PuzzleSymbol[] symbols = currentState.getPuzzleSymbols();
        for(int i = 0; i < symbols.length; i++)
            squares[symbols[i].index].setText(i + "");
        squares[symbols[0].index].setText("");
        // check if it's solvable
        if(currentState.isSolvable(goalState)){
            // check if it's solved
            if(currentState.getH() == 0)
                panel.setBackground(new Color(0, 127, 0));
            else
                panel.setBackground(Color.BLACK);
        }
        else
            panel.setBackground(Color.RED);
    }
}
