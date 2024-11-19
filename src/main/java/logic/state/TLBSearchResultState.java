package logic.state;

import view.UIController;

import java.awt.*;

public class TLBSearchResultState implements  State{
    public void execute(UIController context) {
        int pageNr = Integer.parseInt(context.getUi().getPageNumber().getText());
        boolean pageInTLB = context.getSimulationManager().checkPageInTLB(pageNr);

        if (pageInTLB) {
            context.getUi().getInfoArea().append("Page was found in the TLB.\n");
            context.getUi().getInfoArea().setForeground(Color.GREEN);
            context.getUi().highlightTLBEntry(pageNr);

            context.setCurrentState(new PhysicalPageExtractionState(pageNr));
        } else {
            context.getUi().getInfoArea().append("Page was not found in the TLB.\n");
            context.getUi().getInfoArea().setForeground(Color.RED);

            context.setCurrentState(new PageTableSearchState(pageNr));
        }
    }
}
