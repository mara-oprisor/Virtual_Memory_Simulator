package model;


public class Instruction {
    private String type;
    private int register;
    private VirtualAddress virtualAddress;

    public Instruction(String type, int register, VirtualAddress virtualAddress) {
        this.type = type;
        this.register = register;
        this.virtualAddress = virtualAddress;
    }

    public static Instruction parseInstruction(String instruction, int pageSize){
        String[] parts = instruction.split(" ");
        String type = parts[0].toUpperCase();
        int register = Integer.parseInt(parts[1].substring(1, 2));
        VirtualAddress virtualAddress = VirtualAddress.parseVirtualAddress(parts[2], pageSize);

        return new Instruction(type, register, virtualAddress);

    }

    public VirtualAddress getVirtualAddress() {
        return virtualAddress;
    }
}


