package model;

import java.util.*;

public class Disk {
    private final int pageSize;
    private final int dataSize;
    private final List<Integer> virtualPages;
    private final Map<Integer, List<String>> memory;

    public Disk(int pageSize, int dataSize, List<Integer> virtualPages) {
        this.dataSize = dataSize;
        this.pageSize = pageSize;
        this.virtualPages = virtualPages;
        this.memory = initializeMemory();
    }

    private Map<Integer, List<String>> initializeMemory() {
        Random random = new Random();
        Map<Integer, List<String>> memory = new HashMap<>();

        for (Integer nr : this.virtualPages) {
            List<String> content = new ArrayList<>();
            for (int i = 0; i < this.pageSize; i++) {
                int randomValue = random.nextInt(this.dataSize - 1);
                content.add(String.format("0x%02X", randomValue));
            }

            memory.put(nr, content);
        }

        return memory;
    }

    public List<String> retrieveData(int pageNr) {
        return memory.get(pageNr);
    }

    public void writeData(int page, List<String> data) {
        memory.put(page, data);
    }

    public void deleteEntry(int page) {
        memory.remove(page);
    }

    public Map<Integer, List<String>> getMemory() {
        return memory;
    }
}
