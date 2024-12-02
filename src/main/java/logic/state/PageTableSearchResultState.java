package logic.state;

import view.UIController;

import java.awt.*;

public class PageTableSearchResultState implements State{
    private int pageNr;

    public PageTableSearchResultState(int pageNr) {
        this.pageNr = pageNr;
    }
    public void execute(UIController context) {
        boolean pageInPageTable = context.getSimulationManager().checkPageInPageTable(pageNr);
        if(pageInPageTable) {
            context.getUi().getInfoArea().append("\nPage was found in the Physical Memory.");
            context.getUi().getInfoArea().setForeground(Color.GREEN);
            context.getUi().highlightPageTableEntry(pageNr, Color.GREEN);

            context.setCurrentState(new PhysicalPageExtractionState(pageNr));
        } else {
            context.getUi().getInfoArea().append("\nPage was not found in the Physical Memory.");
            context.getUi().getInfoArea().setForeground(Color.RED);

            context.setCurrentState(new DiskSearchState(pageNr));
        }
    }
}
