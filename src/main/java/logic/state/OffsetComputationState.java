package logic.state;

import view.UIController;

import java.awt.*;

public class OffsetComputationState implements State {
    private int pageNr;
    private int physicalPageNumber;

    public OffsetComputationState(int pageNr, int physicalPageNumber) {
        this.pageNr = pageNr;
        this.physicalPageNumber = physicalPageNumber;
    }

    public void execute(UIController context) {
        context.getUi().resetHighlights(context.getUi().getPageTable());
        context.getUi().resetHighlights(context.getUi().getTlbTable());
        context.getUi().getTlbTable().setBackground(Color.WHITE);
        context.getUi().getPageTable().setBackground(Color.WHITE);
        context.getUi().getVirtualPageNumber().setBackground(Color.WHITE);
        context.getUi().getVirtualOffset().setBackground(Color.YELLOW);

        context.getUi().getInfoArea().append("Offset of the virtual memory remains the same for the physical memory.\n");
        int offset = Integer.parseInt(context.getUi().getVirtualOffset().getText());
        context.getUi().getPhysicalOffset().setBackground(Color.CYAN);
        context.getUi().getPhysicalOffset().setText(String.valueOf(offset));

        context.setCurrentState(new AddressComputationState(physicalPageNumber, pageNr, offset));
    }
}
