package logic;

import model.Instruction;
import model.Registers;
import model.VirtualAddress;

import java.util.ArrayList;
import java.util.List;

public class ScenariosManager {
    private SimulationManager simulationManager;
    private List<Instruction> instructions = new ArrayList<>();
    private Registers registers;
    public ScenariosManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
        this.registers = new Registers();
    }
    private List<Integer> generateAddresses(int typeInstruction) {
        List<Integer> addresses = new ArrayList<>();

        if(typeInstruction == 0) {
            addresses.add(simulationManager.generateMappedAddress(true));
            addresses.add(simulationManager.generateMappedAddress(true));
        } else if(typeInstruction == 1) {
            addresses.add(simulationManager.generateMappedAddress(false));
            addresses.add(simulationManager.generateMappedAddress(false));
        } else if(typeInstruction == 2) {
            addresses.add(simulationManager.generateUnmappedAddress());
            addresses.add(simulationManager.generateUnmappedAddress());
        }

        return addresses;
    }

    public String generateInstructions(int typeInstruction) {
        List<Integer> addresses = generateAddresses(typeInstruction);
        boolean isLoadOperation = true;

        StringBuilder sb = new StringBuilder();
        for(Integer address: addresses) {
            String addressHex = String.format("0x%02X", address);
            if(isLoadOperation) {
                sb.append("load R0, ").append(addressHex).append("\n");
                isLoadOperation = false;
            } else {
                sb.append("store R0, ").append(addressHex).append("\n");
                isLoadOperation = true;
            }
        }

        return sb.toString();
    }

    public void setInstructions(String instructions) {
        this.instructions.clear();

        String[] listInstructions = instructions.split("\n");
        for(String instruction: listInstructions) {
            this.instructions.add(Instruction.parseInstruction(instruction, simulationManager.getPageSize()));
        }
    }

    public VirtualAddress loadInstruction() {
        VirtualAddress virtualAddress = null;

        if(!this.instructions.isEmpty()) {
            Instruction firstInstruction = this.instructions.getFirst();
            virtualAddress = firstInstruction.getVirtualAddress();
        }

        return virtualAddress;
    }

    public String processAndRemoveInstruction(String value, int pageNr) {
        Instruction currentInstruction = this.instructions.getFirst();
        VirtualAddress virtualAddress = currentInstruction.getVirtualAddress();

        if(currentInstruction.getType().equals("LOAD")) {
            registers.setRegisterValue(currentInstruction.getRegister(), value);
        } else if(currentInstruction.getType().equals("STORE")) {
           String regValue = registers.getRegisterValue(currentInstruction.getRegister());
           simulationManager.storeValueToMemory(pageNr, virtualAddress.getOffset(), regValue);
        }

        this.instructions.removeFirst();

        StringBuilder sb = new StringBuilder();
        for(Instruction instruction: instructions) {
            sb.append(instruction.toString()).append("\n");
        }
        return String.valueOf(sb);
    }
}
