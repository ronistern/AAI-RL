import java.util.List;
import java.util.Random;

/**
 * A policy that chooses a random legal action.
 */
public class RandomPolicy implements CoffeeWorldPolicy {

    private Random random;
    private CoffeeEnvironment environment;

    public RandomPolicy(CoffeeEnvironment environment){
        this.environment = environment;
        this.random = new Random();
    }

    @Override
    public CoffeeWorldAction getAction(CoffeeWorldState state) {
        List<CoffeeWorldAction> legalActions = this.environment.getLegalActions(state);
        int actionIndex = this.random.nextInt(legalActions.size());
        return legalActions.get(actionIndex);
    }
}
