import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoffeeEnvironment {

    private Random randomGenerator;

    public int rows; // Number of rows in the grid
    public int cols; // Numver of columns in the grid
    private double spillProbability[][]; // The probability to spill the coffee in each grid cell

    // All the non-goal states
    private CoffeeWorldState states[][];

    // The special states
    private CoffeeWorldState initialState; // This is where the coffee machine is
    private CoffeeWorldState coffeeAtHandState; // This is where the coffee is in my hand

    private CoffeeWorldState goalState; // This is a dummy state representing the happiness after drinking the coffee

    public CoffeeEnvironment(int rows, int cols, double[][] spillProbability){
        this.randomGenerator = new Random();
        this.rows = rows;
        this.cols = cols;
        this.spillProbability = spillProbability;

        // Create the states
        this.states = new CoffeeWorldState[this.rows][this.cols];
        for(int i =0; i<this.rows;i++)
            for (int j = 0; j < this.cols; j++)
                this.states[i][j] = new CoffeeWorldState(i,j);

        // Set special states
        this.initialState = this.states[0][0];
        this.coffeeAtHandState = this.states[rows-1][cols-1];
        this.goalState = new CoffeeWorldState(rows, cols);
    }

    /**
     * Return a world state representing that the robot is at location (i,j) with coffee.
     */
    public CoffeeWorldState getState(int row,int col){
        return this.states[row][col];
    }

    /**
     * Check if an action can be performed at a given state.
     */
    public boolean isLegal(CoffeeWorldState state, CoffeeWorldAction action){
        // If the coffee is in my hand, I will drink it
        if (this.coffeeAtHandState.equals(state))
            return CoffeeWorldAction.DRINK_COFFEE.equals(action);

        // Else, the robot needs to move, so every move action that is in the grid is Ok
        int newRow = state.robotAtRow + action.deltaRows;
        int newCol = state.robotAtCol + action.deltaCols;
        if ((newRow<0)||(newRow>=rows))
            return false;
        if ((newCol<0)||(newCol>=cols))
            return false;
        return true;
    }

    /**
     * The last state is after drinking the coffee
     */
    public boolean isTerminal(CoffeeWorldState state){
        if (state.equals(this.goalState))
            return true;
        else return false;
    }

    /**
     * Drinking coffee gives reward. All other actions - no.
     */
    public double getReward(CoffeeWorldState state, CoffeeWorldAction action){
        if (this.coffeeAtHandState.equals(state)
                && (CoffeeWorldAction.DRINK_COFFEE.equals(action)))
            return 1.0;
        else return 0.0;
    }
    /**
     * Get all the legal actions for a given state.
     */
    public List<CoffeeWorldAction> getLegalActions(CoffeeWorldState state){
        List<CoffeeWorldAction> legalActions = new ArrayList<>(4);

        if(this.coffeeAtHandState.equals(state)){
            legalActions.add(CoffeeWorldAction.DRINK_COFFEE);
        }
        else {
            for (CoffeeWorldAction action : CoffeeWorldAction.MOVE_ACTIONS) {
                if (this.isLegal(state, action))
                    legalActions.add(action);
            }
        }
        return legalActions;
    }


    /**
     * Apply an action to a state. Spill the coffee and return the the start with probability
     * according to this.spillFactor.
     */
    public ActionOutcome apply(CoffeeWorldState state, CoffeeWorldAction action){
        // Check if this is the last action -- drink coffee
        double reward = this.getReward(state,action);
        if (CoffeeWorldAction.DRINK_COFFEE.equals(action)) {
            assert (this.coffeeAtHandState.equals(state));
            return new ActionOutcome(this.goalState,reward);
        }

        // Else, this is regular move action.
        int newRow = state.robotAtRow + action.deltaRows;
        int newCol = state.robotAtCol + action.deltaCols;

        assert(this.isLegal(state,action));
        if(this.randomGenerator.nextFloat()>this.spillProbability[newRow][newCol]){
            return new ActionOutcome(this.states[newRow][newCol],reward);
        }
        else{ // Spilled the coffee
            return new ActionOutcome(this.goalState,reward);
        }
    }

    // A getter for the initial state
    public CoffeeWorldState getInitialState() {
        return this.initialState;
    }

    /**
     * Internal class, representing action outcomes.
     */
    public class ActionOutcome {
        public CoffeeWorldState newState;
        public double reward;

        private ActionOutcome(CoffeeWorldState newState, double reward){
            this.newState=newState;
            this.reward=reward;
        }
    }

    public void print(){
        // Print the environment. For debug purposes
        StringBuilder stringBuilder;
        DecimalFormat stringFormat = new DecimalFormat("0.00");
        for(int i=0; i<this.rows;i++) {
            stringBuilder = new StringBuilder();
            for (int j = 0; j < this.cols;j++) {
                stringBuilder.append(stringFormat.format(this.spillProbability[i][j]));
                stringBuilder.append("\t");
            }
            System.out.println(stringBuilder.toString());
        }
    }
}
