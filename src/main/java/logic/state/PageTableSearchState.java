package logic.state;

import view.UIController;

import java.awt.*;

public class PageTableSearchState implements State {
    private int pageNr;
    public PageTableSearchState(int pageNr) {
        this.pageNr = pageNr;
    }
    public void execute(UIController context) {
        context.getUi().getInfoArea().setText("\nWe search for the page in the Page Table.");
        context.getUi().getTlbTable().setBackground(Color.WHITE);
        context.getUi().getPageTable().setBackground(Color.YELLOW);
        context.getUi().getPageNumber().setBackground(Color.YELLOW);

        context.setCurrentState(new PageTableSearchResultState(pageNr));
    }
}
