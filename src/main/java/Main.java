import logic.SimulationManager;
import view.UI;
import view.UIController;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        SimulationManager simulationManager = new SimulationManager();
        new UIController(ui, simulationManager);
    }
}
