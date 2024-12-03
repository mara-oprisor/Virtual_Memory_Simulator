package logic;

import java.util.List;

public class ReplacementManager {
    private SimulationManager simulationManager;

    public ReplacementManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    public Integer[] getLRUPage() {
        return simulationManager.getPageTable().getLRUPage();
    }

    public void changeDataFromDisk(int pageNr, int virtualPageNr, List<String> dataToBeReplaced) {
        simulationManager.deleteValuesFromDisk(pageNr);
        simulationManager.storeValuesToDisk(virtualPageNr, dataToBeReplaced);
        simulationManager.createDisk();
    }

    public void changeDataFromPhysMem(int physicalPageNr, List<String> dataToReplace, int pageNr, int virtualPageNr) {
        simulationManager.replaceDataInPhysicalMemory(physicalPageNr, dataToReplace);
        simulationManager.changePageTable(pageNr, physicalPageNr, virtualPageNr);
    }
}
