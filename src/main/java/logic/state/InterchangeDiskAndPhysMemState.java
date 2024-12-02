package logic.state;

import view.UIController;

import java.awt.*;
import java.util.List;

public class InterchangeDiskAndPhysMemState implements State{
    private int pageNrFromDisk;
    private List<String> dataFromDisk;
    private int physicalPageNr;
    private int virtualPageNr;

    public InterchangeDiskAndPhysMemState(int pageNrFromDisk, List<String> dataFromDisk, int physicalPageNr, int virtualPageNr) {
        this.pageNrFromDisk = pageNrFromDisk;
        this.dataFromDisk = dataFromDisk;
        this.physicalPageNr = physicalPageNr;
        this.virtualPageNr = virtualPageNr;
    }

    public void execute(UIController context) {
        context.getUi().getInfoArea().append("\n\nThe data from the disk is brought into the main memory, replacing the one that is there.\n");
        context.getUi().getInfoArea().append("The data from the physical memory is moved to the disk.\n");

        List<String> dataToBeReplaced = context.getSimulationManager().getValuesFromPhysicalMemory(physicalPageNr);
        context.getReplacementManager().changeDataFromDisk(pageNrFromDisk, virtualPageNr, dataToBeReplaced);
        context.getReplacementManager().changeDataFromPhysMem(physicalPageNr, dataFromDisk, pageNrFromDisk, virtualPageNr);

        context.fillPageTable();
        context.fillPhysicalMemoryTable();

        context.getUi().highlightPhysicalMemoryPage(physicalPageNr, Color.PINK);
        context.getUi().highlightPageTableEntry(pageNrFromDisk, Color.PINK);

        context.setCurrentState(new PageTableSearchState(pageNrFromDisk));
    }
}
