public interface QLearningAgent {
    public double getQValue(CoffeeWorldState state, CoffeeWorldAction action);
    public double setQValue(CoffeeWorldState state, CoffeeWorldAction action, double newValue);
    public void updateQValue(CoffeeWorldState state, CoffeeWorldAction action, CoffeeEnvironment.ActionOutcome outcome);
    public CoffeeWorldAction chooseAction(CoffeeWorldState state);
}
