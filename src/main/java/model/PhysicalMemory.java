package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhysicalMemory {
    private final int nrOfPages;
    private final int pageSize;
    private final int dataSize;
    private final List<List<String>> memory;

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

    public String retrieveData(int pageNr, int offset) {
        return memory.get(pageNr).get(offset);
    }

    public void writeValue(int pageNr, int offset, String value) {
        memory.get(pageNr).set(offset, value);
    }

    public List<List<String>> getMemory() {
        return memory;
    }
}
