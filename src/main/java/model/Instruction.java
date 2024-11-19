package model;


public class Instruction {
    private final String type;
    private final int register;
    private final VirtualAddress virtualAddress;

    public Instruction(String type, int register, VirtualAddress virtualAddress) {
        this.type = type.toUpperCase();
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(type.equals("LOAD")) {
            sb.append("Load R");
        } else {
            sb.append("Store R");
        }

        sb.append(register).append(", ");
        sb.append(virtualAddress.getValue());

        return String.valueOf(sb);
    }

    public VirtualAddress getVirtualAddress() {
        return virtualAddress;
    }

    public String getType() {
        return type;
    }

    public int getRegister() {
        return register;
    }
}


