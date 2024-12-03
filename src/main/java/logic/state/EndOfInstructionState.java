package logic.state;

import view.UIController;

import java.awt.*;

public class EndOfInstructionState implements State {
    private int pageNr;
    private String value;

    public EndOfInstructionState(String value, int pageNr) {
        this.value = value;
        this.pageNr = pageNr;
    }

    public void execute(UIController context) {
        String newInstructions = context.getScenariosManager().processAndRemoveInstruction(value, pageNr);
        context.getUi().getInstructions().setText(newInstructions);
        context.getUi().resetHighlights(context.getUi().getPageTable());
        context.getUi().resetHighlights(context.getUi().getTlbTable());
        context.getUi().resetHighlights(context.getUi().getMemoryTable());
        context.getUi().getTlbTable().setBackground(Color.WHITE);
        context.getUi().getVirtualPageNumber().setBackground(Color.WHITE);
        context.getUi().getPageTable().setBackground(Color.WHITE);
        context.getUi().getPhysicalAddressHex().setBackground(Color.WHITE);
        context.getUi().getPhysicalPageNumber().setBackground(Color.WHITE);
        context.getUi().getPhysicalOffset().setBackground(Color.WHITE);

        context.setCurrentState(new IdleState());
    }
}
