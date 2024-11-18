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

    public boolean isInTLB(int pageNr) {
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
}
