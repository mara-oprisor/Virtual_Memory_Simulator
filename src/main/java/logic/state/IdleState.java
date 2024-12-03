package logic.state;

import view.UIController;

import java.awt.*;

public class IdleState implements State {
    public void execute(UIController context) {
        context.getUi().getInfoArea().setForeground(Color.BLACK);

        context.getUi().getInfoArea().setText("");
        context.getUi().getVirtualAddressHex().setText("");
        context.getUi().getVirtualPageNumber().setText("");
        context.getUi().getVirtualOffset().setText("");

        context.getUi().getPhysicalAddressHex().setText("");
        context.getUi().getPhysicalPageNumber().setText("");
        context.getUi().getPhysicalOffset().setText("");
    }
}
