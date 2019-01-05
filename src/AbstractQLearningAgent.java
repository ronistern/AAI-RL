public abstract class AbstractQLearningAgent implements ReinforcementLearningAgent{

    // The table that contains the Q values
    private QValues qValues;

    // A class that represents the world
    protected CoffeeEnvironment environment;

    // Discount factor (gamma)
    protected double discountFactor;

    // Learning rate (alpha)
    protected double learningRate;

    // A counter that counts the number of calls to the learn() function
    protected int iterations;

    public AbstractQLearningAgent(CoffeeEnvironment environment,
                                  double discountFactor){
        this.environment=environment;
        this.qValues = new QValues();
        this.learningRate = 1;
        this.discountFactor = discountFactor;
        this.iterations = 0;
    }

    @Override
    public void learn(CoffeeWorldState state, CoffeeWorldAction action, CoffeeWorldState newState, double reward) {

        // Update Q value
        double newQ = this.computeNewQValue(state, action, newState,reward);
        this.setQValue(state,action,newQ);

        // Update learning rate
        this.iterations=this.iterations+1;
        this.learningRate = 1/(java.lang.Math.pow(this.iterations,0.75));
    }

    /**
     * Update the Q values with the observed (state, action, new state, reward) tuple.
     */
    protected double computeNewQValue(CoffeeWorldState state, CoffeeWorldAction action, CoffeeWorldState newState, double reward) {
        double oldQ = this.getQValue(state,action);

        // Get the best Q value for the actions in the next state
        double maxQ = 0;
        double nextActionQ;
        for(CoffeeWorldAction nextAction : environment.getLegalActions(newState)){
            nextActionQ = this.getQValue(newState,nextAction);
            if(maxQ<nextActionQ)
                maxQ = nextActionQ;
        }

        // The Q-learning update rule
        return this.learningRate *oldQ+ (1-this.learningRate)*
                (reward + this.discountFactor*maxQ);
    }

    /**
     * Get the learning rate parameter
     */
    protected double getLearningRate(){
        return this.learningRate;
    }

    /**
     * Get the learning rate parameter
     */
    protected double getDiscountFactor(){
        return this.discountFactor;
    }

    /**
     * Q(state,action)
     *
     * Return the Q value of the given state and action.
     */
    protected double getQValue(CoffeeWorldState state, CoffeeWorldAction action){
        return this.qValues.getQValue(state,action);
    }

    /**
     * Set the Q value of a given state-action pair.
     */
    protected void setQValue(CoffeeWorldState state, CoffeeWorldAction action, double newValue){
        this.qValues.setQValue(state,action,newValue);
    }
}
