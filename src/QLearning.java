public class QLearning {

    private CoffeeEnvironment environment;
    private double discountFactor;
    private QValues qValues;
    private double learningRate;
    private int iterations;
    private CoffeeWorldPolicy policy;

    public QLearning(CoffeeEnvironment environment,
                     double learningRate,
                     double discountFactor){
        this.environment=environment;
        this.qValues = new QValues();
        this.learningRate = learningRate; // TODO: Decay the learning factor
        this.discountFactor = discountFactor;

        this.iterations = 0;
    }

    public QValues getQValues(){
        return this.qValues;
    }

    /**
     * Update the Q value of a given state-action pair.
     */
    private void updateQValue(CoffeeWorldState state, CoffeeWorldAction action,
                              CoffeeEnvironment.ActionOutcome outcome){
        double oldQ = this.qValues.getQValue(state,action);

        // Get the best Q value for the actions in the next state
        double maxQ = 0;
        double nextActionQ;
        for(CoffeeWorldAction nextAction : environment.getLegalActions(outcome.newState)){
            nextActionQ = this.qValues.getQValue(outcome.newState,nextAction);
            if(maxQ<nextActionQ)
                maxQ = nextActionQ;
        }

        // The Q-learning update rule
        double newQ = this.learningRate *oldQ+ (1-this.learningRate)*
                (outcome.reward + this.discountFactor*maxQ);
        this.qValues.setQValue(state,action,newQ);

        // Update learning rate
        this.iterations=this.iterations+1;
        this.learningRate = 1/(java.lang.Math.pow(this.iterations,0.75));
    }

    /**
     * Run an episode, return the collected reward, discounted by the given discount factor (<=1).
     */
    public double runEpisode(CoffeeWorldPolicy policy){
        CoffeeWorldAction action;
        CoffeeEnvironment.ActionOutcome outcome;
        CoffeeWorldState currentState = environment.getInitialState();
        double collectedReward = 0;
        double discount = 1;
        // Perform action, get new state and accumulate reward
        while (environment.isTerminal(currentState)==false){
            action = policy.getAction(currentState);
            outcome = environment.apply(currentState,action);

            this.updateQValue(currentState,action, outcome);

            // Reward gets discounted after every step
            collectedReward += discount*outcome.reward;
            discount = discount * discountFactor;

            currentState = outcome.newState;
        }
        return collectedReward;
    }


}
