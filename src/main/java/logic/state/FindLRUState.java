package logic.state;

import view.UIController;

import java.awt.*;
import java.util.List;

public class FindLRUState implements State{
    private int pageNr;
    private List<String> dataFromDisk;
    public FindLRUState(int pageNr, List<String> dataFromDisk) {
        this.pageNr = pageNr;
        this.dataFromDisk = dataFromDisk;
    }
    public void execute(UIController context) {
        context.getUi().getInfoArea().append("\nThe least recently used page is: ");

        Integer[] pair = context.getReplacementManager().getLRUPage();
        int virtualPageNr = pair[0];
        int physicalPageNr = pair[1];
        context.getUi().getInfoArea().append(String.valueOf(physicalPageNr));
        context.getUi().highlightPhysicalMemoryPage(physicalPageNr, Color.PINK);

        context.setCurrentState(new InterchangeDiskAndPhysMemState(pageNr, dataFromDisk, physicalPageNr, virtualPageNr));
    }
}
