package logic.state;

import view.UIController;

import java.awt.*;

public class PhysicalPageExtractionState implements State {
    private int pageNr;

    public PhysicalPageExtractionState(int pageNr) {
        this.pageNr = pageNr;
    }

    public void execute(UIController context) {
        int physicalPageNumber = context.getSimulationManager().getPhysicalPagePageTable(pageNr);
        context.getUi().getInfoArea().setForeground(Color.BLACK);
        context.getUi().getInfoArea().append("\nThe corresponding physical page is " + physicalPageNumber + ".\n");
        context.getUi().highlightPhysicalMemoryPage(physicalPageNumber, Color.GREEN);

        context.getUi().getPhysicalPageNumber().setText(String.valueOf(physicalPageNumber));
        context.getUi().getPhysicalPageNumber().setBackground(Color.CYAN);

        context.setCurrentState(new OffsetComputationState(pageNr, physicalPageNumber));
    }
}
