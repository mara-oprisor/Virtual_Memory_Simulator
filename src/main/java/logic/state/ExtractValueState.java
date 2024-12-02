package logic.state;

import view.UIController;

import java.awt.*;

public class ExtractValueState implements State {
    private int physicalPageNumber;
    private int offset;
    private int physicalAddress;

    public ExtractValueState(int physicalPageNumber, int offset, int physicalAddress) {
        this.physicalPageNumber = physicalPageNumber;
        this.offset = offset;
        this.physicalAddress = physicalAddress;
    }

    public void execute(UIController context) {
        String value = context.getSimulationManager().getValueFromPhysicalMemory(physicalPageNumber, offset);
        context.getUi().getInfoArea().setForeground(Color.MAGENTA);
        context.getUi().getInfoArea().append("\nValue at address " + String.format("0x%02X", physicalAddress) + " is " + value + ".");

        context.setCurrentState(new EndOfInstructionState(value, physicalPageNumber));
    }
}
