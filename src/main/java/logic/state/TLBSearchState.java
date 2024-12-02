package logic.state;

import view.UIController;

import java.awt.*;

public class TLBSearchState implements State{
    public void execute(UIController context) {
        context.getUi().getInfoArea().setText("\nWe search for the page in the TLB.\n");
        context.getUi().getTlbTable().setBackground(Color.YELLOW);
        context.getUi().getVirtualPageNumber().setBackground(Color.YELLOW);

        context.setCurrentState(new TLBSearchResultState());
    }
}
