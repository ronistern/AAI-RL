import java.util.List;
import java.util.Random;

/**
 * An implementation of the epsilon greedy policy.
 */
public class EpsilonGreedyPolicy implements CoffeeWorldPolicy {
    private Random random;

    private double epsilon;
    private QValues qValues;
    private CoffeeEnvironment environment;


    public EpsilonGreedyPolicy(CoffeeEnvironment environment,
            QValues qValues, double epsilon){
        this.epsilon = epsilon;
        this.qValues = qValues;
        this.environment = environment;
    }
    @Override
    public CoffeeWorldAction getAction(CoffeeWorldState state) {
        if(this.random.nextDouble()<=this.epsilon)
            return this.chooseBestAction(state);
        else
            return this.chooseRandomAction(state);
    }

    /**
     * Choose the best action, i.e., the one that maximizes the Q value.
     */
    private CoffeeWorldAction chooseBestAction(CoffeeWorldState state){
        CoffeeWorldAction bestAction=null;
        double bestQValue=0;
        double q;
        for(CoffeeWorldAction action : this.environment.getLegalActions(state)){
            q = qValues.getQValue(state, action);
            if ( q >=bestQValue){
                bestQValue = q;
                bestAction = action;
            }
        }

        if (bestAction == null) // Weird
            return this.environment.getLegalActions(state).get(0);
        else return bestAction;
    }

    /**
     * Choose a random action among all legal actions.
     */
    private CoffeeWorldAction chooseRandomAction(CoffeeWorldState state){
        List<CoffeeWorldAction> legalActions = this.environment.getLegalActions(state);
        int actionIndex = random.nextInt(legalActions.size());
        return legalActions.get(actionIndex);
    }
}
