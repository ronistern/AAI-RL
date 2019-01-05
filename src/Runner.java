import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Random;

/**
 * This class enables running the RL agent and measuring its performance.
 */
public class Runner {

    /**
     * Runs a single episode. The agent chooses an action, learns from its outcome,
     * and continues doing so until reaching a terminal state.
     * @return The discounted rewards collected by the agent.
     */
    public double runEpisode(CoffeeEnvironment environment, ReinforcementLearningAgent agent) {

        CoffeeWorldState currentState = environment.getInitialState();
        CoffeeWorldAction action;
        CoffeeEnvironment.ActionOutcome outcome;
        double accumulatedReward = 0;
        double discountFactor = 0.9;
        double discount = 1.0;

        while (environment.isTerminal(currentState) == false) {
            action = agent.chooseAction(currentState); // Choose which action to perform

            outcome = environment.apply(currentState, action);

            // Here is the learning
            agent.learn(currentState, action, outcome.newState, outcome.reward); // Learn from actin outcome

            accumulatedReward = accumulatedReward + discount * outcome.reward;
            discount = discount * discountFactor; // Future rewards get discounted
            currentState = outcome.newState;
        }
        return accumulatedReward;
    }

    /**
     * Run multiple episodes, return the average discounted collected rewards.
     */
    public double runEpisodes(CoffeeEnvironment environment, ReinforcementLearningAgent agent, int iterations) {
        double sumOfRewards = 0.0;
        for (int i = 0; i < iterations; i++) {
            sumOfRewards += this.runEpisode(environment, agent);
        }
        return sumOfRewards / iterations;
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
        Runner runner = new Runner();
        CoffeeEnvironment environment = runner.createEnvironment(3,3);
        double discountFactor = 0.5;
        double epsilon = 0.05;
        int iterations = 100000;
        ReinforcementLearningAgent agent;
        double utility;

        for(int i=0;i<10;i++) {
            agent = new EpsilonGreedyAgent(environment, discountFactor,epsilon);
            utility = runner.runEpisodes(environment, agent, iterations);
            System.out.format("Average utility over %d is %.3f%n", iterations,utility);
        }

    }

}
