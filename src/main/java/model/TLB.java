package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TLB {
    private final int nrOfEntries;
    private final List<PageTableEntry> entries;

    public TLB(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        entries.add(entry);
    }

    public boolean isInTLB(int pageNr) {
        for(int i = 0; i < nrOfEntries; i++) {
            if(entries.get(i).getVirtualPageNr() == pageNr) {
                return true;
            }
        }

        return false;
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }

    public void updateTimeStamp(int pageNr) {
        entries.get(pageNr).setEnterTime(new Time(System.currentTimeMillis()));
    }
}
