import java.util.List;

/**
 * This class should implement an epsilon-greedy agent.
 * Specifically, all you need to do is implement the two function marked below with "TODO:"
 * - chooseAction()
 * - computeNewQValue()
 *
 * In the
 */
public class EpsilonGreedyAgent extends AbstractQLearningAgent {
    private double epsilon = 0.1;

    public EpsilonGreedyAgent(CoffeeEnvironment environment, double discountFactor, double epsilon) {
        super(environment, discountFactor);
        this.epsilon = epsilon;
    }


    /**
     * Update the Q values with the observed (state, action, new state, reward) tuple,
     * using the Q-learning update rule.
     */
    @Override
    protected double computeNewQValue(CoffeeWorldState state, CoffeeWorldAction action, CoffeeWorldState newState, double reward) {
        double oldQ = this.getQValue(state,action);

        // Get the best Q value for the actions in the next state
        List<CoffeeWorldAction> actions = this.environment.getLegalActions(state);
        double maxQ = 0;
        double nextActionQ;
        for(CoffeeWorldAction nextAction : environment.getLegalActions(newState)){
            nextActionQ = this.getQValue(newState,nextAction);
            if(maxQ<nextActionQ)
                maxQ = nextActionQ;
        }

        // Helper values
        double discountFactor = this.getDiscountFactor();
        double learningRate = this.getLearningRate();

        // The Q-learning update rule
        return 0; // TODO: Replace this with the Q-learning update rule
    }


    /**
     * Choose the action the agent should perform in the current state.
     */
    @Override
    public CoffeeWorldAction chooseAction(CoffeeWorldState state) {


        // TODO: Explore: choose a random action with epsilon probability

        // TODO: Exploit: get the best (highest Q value) action legal in this state

        return environment.getLegalActions(state).get(0);
    }
}
