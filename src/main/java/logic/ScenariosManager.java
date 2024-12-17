package logic;

import model.Instruction;
import model.PageTableEntry;
import model.Registers;
import model.VirtualAddress;
import util.FileUtil;

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

    private Integer generateAddress(int typeInstruction) {
        if (typeInstruction == 3) {
            Random random = new Random();
            typeInstruction = Math.abs(random.nextInt()) % 3;
        }

        if (typeInstruction == 0) {
            return generateMappedAddress(true);
        } else if (typeInstruction == 1) {
            return generateMappedAddress(false);
        } else {
            return generateUnmappedAddress();
        }
    }

    public String generateInstructions(int typeInstruction, int nrInstruction) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        if (nrInstruction == 2) {
            int address1 = generateAddress(Math.abs(random.nextInt()) % 3);
            int address2 = generateAddress(Math.abs(random.nextInt()) % 3);
            String addressHex1 = String.format("0x%02X", address1);
            String addressHex2 = String.format("0x%02X", address2);
            sb.append("load R0, ").append(addressHex1).append("\n");
            sb.append("store R0, ").append(addressHex2).append("\n");
        } else {
            for (int i = 0; i <= nrInstruction; i++) {
                Integer address = generateAddress(typeInstruction);
                String addressHex = String.format("0x%02X", address);
                boolean isLoadOperation = random.nextBoolean();
                int register = Math.abs(random.nextInt()) % 4;

                if (isLoadOperation) {
                    sb.append("load R");
                } else {
                    sb.append("store R");
                }

                sb.append(register).append(", ").append(addressHex).append("\n");
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
            FileUtil.writeToFile(FileUtil.formatRegisters(registers.getValues()), "registers.txt");
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

    public void resetRegisters() {
        registers.resetRegisters();
        FileUtil.writeToFile(FileUtil.formatRegisters(registers.getValues()), "registers.txt");
    }

    public void resetInstructions() {
        this.instructions.clear();
    }
}
