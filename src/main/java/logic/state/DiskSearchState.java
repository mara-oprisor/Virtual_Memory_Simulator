package logic.state;

import view.UIController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiskSearchState implements State{
    private int pageNr;
    public DiskSearchState(int pageNr) {
        this.pageNr = pageNr;
    }
    public void execute(UIController context) {
        context.getUi().getInfoArea().setForeground(Color.BLACK);
        context.getUi().getInfoArea().setText("\nWe search for the page in the Disk.\n");
        context.getUi().getPageTable().setBackground(Color.WHITE);

        List<String> data = context.getSimulationManager().getDataFromDisk(pageNr);

        context.setCurrentState(new FindLRUState(pageNr, data));
    }
}
