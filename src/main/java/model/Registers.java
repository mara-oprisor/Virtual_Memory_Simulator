package model;

public class Registers {
    private final String[] values;

    public Registers() {
        this.values = new String[4];
        for(int i = 0; i < 4; i++) {
            this.values[0] = "0x0";
        }
    }

    public String getRegisterValue(int registerIndex) {
        if(registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        return values[registerIndex];
    }

    public void setRegisterValue(int registerIndex, String value) {
        if(registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        this.values[registerIndex] = value;
    }
}
