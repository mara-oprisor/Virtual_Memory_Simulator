package model;

import java.util.ArrayList;
import java.util.List;

public class PageTable {
    private final int nrOfEntries;
    private final List<PageTableEntry> entries;

    public PageTable(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        this.entries.add(entry);
    }

    public boolean isInPageTable(int pageNr) {
        for(int i = 0; i < nrOfEntries; i++) {
            if(entries.get(i).getVirtualPageNr() == pageNr) {
                return true;
            }
        }

        return false;
    }

    public int getPhysicalPage(int pageNr) {
        for(int i = 0; i < nrOfEntries; i++) {
            if(entries.get(i).getVirtualPageNr() == pageNr) {
                return entries.get(i).getPhysicalPageNr();
            }
        }

        return -2;
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
