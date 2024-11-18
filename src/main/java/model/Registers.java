package model;

public class Registers {
    private int[] values;

    public Registers() {
        this.values = new int[4];
        for(int i = 0; i < 4; i++) {
            this.values[0] = 0;
        }
    }

    public int getRegisterValue(int registerIndex) {
        if(registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        return values[registerIndex];
    }

    public void setRegisterValue(int registerIndex, int value) {
        if(registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        this.values[registerIndex] = value;
    }
}
