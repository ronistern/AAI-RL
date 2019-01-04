import java.text.DecimalFormat;
import java.util.Random;

public class Main
{

    /**
     * Run an episode, return the collected reward, discounted by the given discount factor (<=1).
     */
    public double runEpisode(CoffeeEnvironment environment,
                             CoffeeWorldPolicy policy,
                             double discountFactor){
        CoffeeWorldAction action;
        CoffeeEnvironment.ActionOutcome outcome;
        CoffeeWorldState currentState = environment.getInitialState();
        double collectedReward = 0;
        double discount = 1;
        // Perform action, get new state and accumulate reward
        while (environment.isTerminal(currentState)==false){
            action = policy.getAction(currentState);
            outcome = environment.apply(currentState,action);
            currentState = outcome.newState;

            // Reward gets discounted after every step
            collectedReward += discount*outcome.reward;
            discount = discount * discountFactor;
        }
        return collectedReward;
    }

    /**
     * Create a random environment of the given size.
     */
    private CoffeeEnvironment createEnvironment(int rows, int cols){
        Random random = new Random();
        double[][] spillProbabilities = new double[rows][cols];
        for (int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                spillProbabilities[i][j] = random.nextDouble();

        return new CoffeeEnvironment(rows,cols,spillProbabilities);
    }

    public static void main(String[] args){
        DecimalFormat stringFormat = new DecimalFormat("0.00");
        Main me = new Main();

        CoffeeEnvironment environment = me.createEnvironment(2,4);
        double discountFactor = 0.9;

        environment.print();

        CoffeeWorldPolicy policy = new RandomPolicy(environment);
        double accumulatedRewards=0.0;
        int iterations = 1000000;
        for(int i=0;i<iterations;i++){
            //System.out.println(i+":"+me.runEpisode(environment,policy,discountFactor));
            accumulatedRewards += me.runEpisode(environment,policy,discountFactor);
        }
        System.out.println("Random policy on " + iterations +
                        " obtained "+ stringFormat.format(accumulatedRewards) +
                " on " + iterations + " iterations");

        // Q Learning
        double learningRate = 1.0;
        double epsilon = 0.1;
        QLearning learner = new QLearning(environment,learningRate,discountFactor);
        EpsilonGreedyPolicy epsilonGreedyPolicy = new EpsilonGreedyPolicy(environment,
                learner.getQValues(), epsilon);

        accumulatedRewards=0.0;
        for(int i=0;i<iterations;i++){
            accumulatedRewards += learner.runEpisode(epsilonGreedyPolicy);
        }
        System.out.println("Epsilon greedy policy on " + iterations +
                " obtained "+ stringFormat.format(accumulatedRewards) +
                " on " + iterations + " iterations");
    }
}
