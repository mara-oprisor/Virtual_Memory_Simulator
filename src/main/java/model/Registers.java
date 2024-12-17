package model;

import util.FileUtil;

public class Registers {
    private final String[] values;

    public Registers() {
        this.values = new String[4];
        for (int i = 0; i < 4; i++) {
            this.values[i] = "0x0";
        }

        FileUtil.writeToFile(FileUtil.formatRegisters(values), "registers.txt");
    }

    public String getRegisterValue(int registerIndex) {
        if (registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        return values[registerIndex];
    }

    public void setRegisterValue(int registerIndex, String value) {
        if (registerIndex < 0 || registerIndex > 4) {
            throw new IllegalArgumentException("Invalid register index");
        }

        this.values[registerIndex] = value;
    }

    public void resetRegisters() {
        for (int i = 0; i < 4; i++) {
            this.values[i] = "0x0";
        }
    }

    public String[] getValues() {
        return values;
    }
}
