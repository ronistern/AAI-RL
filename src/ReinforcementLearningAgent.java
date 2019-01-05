public interface ReinforcementLearningAgent {
    public CoffeeWorldAction chooseAction(CoffeeWorldState state);
    public void learn(CoffeeWorldState state,
                      CoffeeWorldAction action,
                      CoffeeWorldState newState,
                      double reward);
}
