package model;

import java.util.ArrayList;
import java.util.List;

public class TLB {
    private int nrOfEntries;
    private List<PageTableEntry> entries;

    public TLB(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        entries.add(entry);
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
