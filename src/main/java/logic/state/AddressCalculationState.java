package logic.state;

import view.UIController;

public class AddressCalculationState implements State {
    private int pageNr;
    private int physicalPageNumber;
    public AddressCalculationState(int physicalPageNumber, int pageNr) {
        this.physicalPageNumber = physicalPageNumber;
        this.pageNr = pageNr;
    }
    public void execute(UIController context) {
        int offset = Integer.parseInt(context.getUi().getOffset().getText());
        int physicalAddress = context.getSimulationManager().calculatePhysicalAddress(physicalPageNumber, offset);
        String value = context.getSimulationManager().getValueFromPhysicalMemory(physicalPageNumber, offset);
        context.getUi().getInfoArea().append("\nValue at address " + String.format("0x%02X", physicalAddress) + " is " + value + ".");

        context.setCurrentState(new EndOfInstructionState(value, physicalPageNumber));
    }
}
