package logic.state;

import view.UIController;

import java.awt.*;
import java.util.List;

public class FindReplacementPageState implements State {
    private int pageNr;
    private List<String> dataFromDisk;

    public FindReplacementPageState(int pageNr, List<String> dataFromDisk) {
        this.pageNr = pageNr;
        this.dataFromDisk = dataFromDisk;
    }

    public void execute(UIController context) {
        String replacementPolicy = context.getReplacementManager().getReplacementPolicy();
        int virtualPageNr;
        int physicalPageNr;
        if (replacementPolicy.equals("LRU")) {
            Integer[] pair = context.getReplacementManager().getLRUPage();
            virtualPageNr = pair[0];
            physicalPageNr = pair[1];
            context.getUi().getInfoArea().append("\nThe least recently used page is: ");
        } else {
            Integer[] pair = context.getReplacementManager().getFirstInPage();
            virtualPageNr = pair[0];
            physicalPageNr = pair[1];
            context.getUi().getInfoArea().append("\nThe first entered page is: ");
        }


        context.getUi().getInfoArea().append(String.valueOf(physicalPageNr));
        context.getUi().highlightPhysicalMemoryPage(physicalPageNr, Color.PINK);
        context.getUi().highlightPageTableEntry(virtualPageNr, Color.PINK);

        context.setCurrentState(new InterchangeDiskAndPhysMemState(pageNr, dataFromDisk, physicalPageNr, virtualPageNr, replacementPolicy));
    }
}
