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
        context.getUi().getInfoArea().append("\nThe corresponding physical page is " + physicalPageNumber + ".");
        context.getUi().highlightPhysicalMemoryPage(physicalPageNumber, Color.GREEN);

        context.setCurrentState(new AddressCalculationState(physicalPageNumber, pageNr));
    }
}
