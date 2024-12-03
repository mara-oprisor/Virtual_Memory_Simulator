package logic.state;

import model.PageTableEntry;
import view.UIController;

import java.awt.*;
import java.sql.Time;

public class UpdateEntryTimeState implements State {
    private int pageNr;
    private boolean isInTLB;

    public UpdateEntryTimeState(int pageNr, boolean isInTLB) {
        this.pageNr = pageNr;
        this.isInTLB = isInTLB;
    }

    public void execute(UIController context) {
        if (isInTLB) {
            context.getSimulationManager().updateTimeStamp(true, pageNr);
        } else {
            context.getSimulationManager().updateTimeStamp(false, pageNr);

            PageTableEntry usedPage = context.getSimulationManager().getPageTable().getEntry(pageNr);
            Object[] lruEntry = context.getSimulationManager().getTlb().getLRUEntry();
            Time entryTime = (Time) lruEntry[1];

            if (usedPage.getEnterTime().after(entryTime)) {
                context.getSimulationManager().getTlb().replaceEntry(usedPage, (int) lruEntry[0]);

                context.fillPageTable();
                context.fillTLB();
                context.getUi().highlightPageTableEntry(pageNr, Color.GREEN);
            }
        }

        context.setCurrentState(new PhysicalPageExtractionState(pageNr));
    }
}
