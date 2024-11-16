package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhysicalMemory {
    private int nrOfPages;
    private int pageSize;
    private int dataSize;
    private List<List<String>> memory;

    public PhysicalMemory(int nrPhysicalPages, int pageSize, int dataSize) {
        this.nrOfPages = nrPhysicalPages;
        this.pageSize = pageSize;
        this.dataSize = dataSize;
        this.memory = initializeMemory();
    }

    private List<List<String>> initializeMemory() {
        Random random = new Random();
        List<List<String>> memory = new ArrayList<>();

        for(int i = 0; i < nrOfPages; i++) {
            List<String> page = new ArrayList<>();

            for(int j = 0; j < pageSize; j++) {
                int randomValue = random.nextInt(this.dataSize);
                page.add(String.format("0x%02X", randomValue));
            }

            memory.add(page);
        }

        return memory;
    }

    public List<List<String>> getMemory() {
        return memory;
    }
}
