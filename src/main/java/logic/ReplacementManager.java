package logic;

import java.util.List;

public class ReplacementManager {
    private SimulationManager simulationManager;
    private String replacementPolicy;

    public ReplacementManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
    }

    public Integer[] getLRUPage() {
        return simulationManager.getPageTable().getLRUPage();
    }

    public Integer[] getFirstInPage() {
        return simulationManager.getPageTable().getFirstInPage();
    }

    public void changeDataFromDisk(int pageNr, int virtualPageNr, List<String> dataToBeReplaced) {
        simulationManager.deleteValuesFromDisk(pageNr);
        simulationManager.storeValuesToDisk(virtualPageNr, dataToBeReplaced);
        simulationManager.createDisk();
    }

    public void changeDataFromPhysMem(int physicalPageNr, List<String> dataToReplace, int pageNr, int virtualPageNr, String replacementPolicy) {
        simulationManager.replaceDataInPhysicalMemory(physicalPageNr, dataToReplace);
        simulationManager.changePageTable(pageNr, physicalPageNr, virtualPageNr, replacementPolicy);
    }

    public void updateTLB() {
        simulationManager.updateTLBFIFO();
    }

    public void setReplacementPolicy(String replacementPolicy) {
        this.replacementPolicy = replacementPolicy;
    }

    public String getReplacementPolicy() {
        return replacementPolicy;
    }
}
