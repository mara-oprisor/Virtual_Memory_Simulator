package logic;

import model.Instruction;
import model.PageTableEntry;
import model.Registers;
import model.VirtualAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScenariosManager {
    private SimulationManager simulationManager;
    private TableManager tableManager;
    private List<Instruction> instructions = new ArrayList<>();
    private Registers registers;

    public ScenariosManager(SimulationManager simulationManager) {
        this.simulationManager = simulationManager;
        this.registers = new Registers();
        this.tableManager = new TableManager();
    }

    private int generateMappedAddress(boolean fromTLB) {
        int pageSize = simulationManager.getPageSize();

        List<Integer> mappedAddresses = new ArrayList<>();
        Random random = new Random();

        List<Integer> TLBPages = new ArrayList<>();
        for (PageTableEntry entry : simulationManager.getTlb().getEntries()) {
            int pageNr = entry.getVirtualPageNr();
            TLBPages.add(pageNr);
        }

        if (fromTLB) {
            for (Integer pageNr : TLBPages) {
                for (int offset = 0; offset < pageSize; offset++) {
                    int address = pageNr * pageSize + offset;
                    mappedAddresses.add(address);
                }
            }
        } else {
            List<Integer> pages = tableManager.extractMappedPages(simulationManager.getPageTable());
            for (Integer pageNr : pages) {
                if (!TLBPages.contains(pageNr)) {
                    for (int offset = 0; offset < pageSize; offset++) {
                        int address = pageNr * pageSize + offset;
                        mappedAddresses.add(address);
                    }
                }
            }
        }

        return mappedAddresses.get(random.nextInt(mappedAddresses.size()));
    }

    private int generateUnmappedAddress() {
        int pageSize = simulationManager.getPageSize();

        List<Integer> unmappedAddresses = new ArrayList<>();
        Random random = new Random();

        List<Integer> pages = tableManager.extractUnmappedPages(simulationManager.getPageTable());
        for (Integer pageNr : pages) {
            for (int offset = 0; offset < pageSize; offset++) {
                int address = pageNr * pageSize + offset;
                unmappedAddresses.add(address);
            }
        }

        return unmappedAddresses.get(random.nextInt(unmappedAddresses.size() - 1));
    }

    private List<Integer> generateAddresses(int typeInstruction) {
        List<Integer> addresses = new ArrayList<>();

        if (typeInstruction == 0) {
            addresses.add(generateMappedAddress(true));
            addresses.add(generateMappedAddress(true));
        } else if (typeInstruction == 1) {
            addresses.add(generateMappedAddress(false));
            addresses.add(generateMappedAddress(false));
        } else if (typeInstruction == 2) {
            addresses.add(generateUnmappedAddress());
            addresses.add(generateUnmappedAddress());
        }

        return addresses;
    }

    public String generateInstructions(int typeInstruction) {
        List<Integer> addresses = generateAddresses(typeInstruction);
        boolean isLoadOperation = true;

        StringBuilder sb = new StringBuilder();
        for (Integer address : addresses) {
            String addressHex = String.format("0x%02X", address);
            if (isLoadOperation) {
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
        for (String instruction : listInstructions) {
            this.instructions.add(Instruction.parseInstruction(instruction, simulationManager.getPageSize()));
        }
    }

    public VirtualAddress loadInstruction() {
        VirtualAddress virtualAddress = null;

        if (!this.instructions.isEmpty()) {
            Instruction firstInstruction = this.instructions.getFirst();
            virtualAddress = firstInstruction.getVirtualAddress();
        }

        return virtualAddress;
    }

    public String processAndRemoveInstruction(String value, int pageNr) {
        Instruction currentInstruction = this.instructions.getFirst();
        VirtualAddress virtualAddress = currentInstruction.getVirtualAddress();

        if (currentInstruction.getType().equals("LOAD")) {
            registers.setRegisterValue(currentInstruction.getRegister(), value);
        } else if (currentInstruction.getType().equals("STORE")) {
            String regValue = registers.getRegisterValue(currentInstruction.getRegister());
            simulationManager.storeValueToMemory(pageNr, virtualAddress.getOffset(), regValue);
        }

        this.instructions.removeFirst();

        StringBuilder sb = new StringBuilder();
        for (Instruction instruction : instructions) {
            sb.append(instruction.toString()).append("\n");
        }
        return String.valueOf(sb);
    }
}
