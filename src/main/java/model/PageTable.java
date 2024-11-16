package model;

import java.util.ArrayList;
import java.util.List;

public class PageTable {
    private int nrOfEntries;
    private List<PageTableEntry> entries;

    public PageTable(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        this.entries.add(entry);
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
