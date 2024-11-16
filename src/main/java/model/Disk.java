package model;

import java.util.*;

public class Disk {
    private int pageSize;
    private int dataSize;
    private List<Integer> virtualPages;
    private Map<Integer, List<String>> memory;

    public Disk(int pageSize, int dataSize, List<Integer> virtualPages) {
        this.dataSize = dataSize;
        this.pageSize = pageSize;
        this.virtualPages = virtualPages;
        this.memory = initializeMemory();
    }

    private Map<Integer, List<String>> initializeMemory() {
        Random random = new Random();
        Map<Integer, List<String>> memory = new HashMap<>();

        for(Integer nr: this.virtualPages) {
            List<String> content = new ArrayList<>();
            for(int i = 0; i < this.pageSize; i++) {
                int randomValue = random.nextInt(this.dataSize);
                content.add(String.format("0x%02X", randomValue));
            }

            memory.put(nr, content);
        }

        return memory;
    }

    public Map<Integer, List<String>> getMemory() {
        return memory;
    }
}
