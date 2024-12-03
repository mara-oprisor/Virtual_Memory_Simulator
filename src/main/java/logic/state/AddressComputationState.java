package logic.state;

import view.UIController;

import java.awt.*;

public class AddressComputationState implements State {
    private int pageNr;
    private int physicalPageNumber;
    private int offset;

    public AddressComputationState(int physicalPageNumber, int pageNr, int offset) {
        this.physicalPageNumber = physicalPageNumber;
        this.pageNr = pageNr;
        this.offset = offset;
    }

    public void execute(UIController context) {
        context.getUi().getVirtualOffset().setBackground(Color.WHITE);
        context.getUi().getInfoArea().append("\n Physical address = page_number * page_size + offset\n");

        int physicalAddress = context.getSimulationManager().calculatePhysicalAddress(physicalPageNumber, offset);
        context.getUi().getInfoArea().append("Physical address = " + String.valueOf(physicalAddress) + "\n");

        context.getUi().getPhysicalAddressHex().setBackground(Color.CYAN);
        context.getUi().getPhysicalAddressHex().setText(String.format("0x%02X", physicalAddress));

        context.setCurrentState(new ExtractValueState(physicalPageNumber, offset, physicalAddress));
    }
}
