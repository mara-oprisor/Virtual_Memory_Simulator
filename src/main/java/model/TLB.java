package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TLB {
    private final int nrOfEntries;
    private List<PageTableEntry> entries;

    public TLB(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        entries.add(entry);
    }

    public void replaceEntry(PageTableEntry newEntry, int virtualPage) {
        for (PageTableEntry entry : entries) {
            if (entry.getVirtualPageNr() == virtualPage) {
                entry.setVirtualPageNr(newEntry.getVirtualPageNr());
                entry.setPhysicalPageNr(newEntry.getPhysicalPageNr());
                entry.setEnterTime(newEntry.getEnterTime());
            }
        }
    }

    public boolean isInTLB(int pageNr) {
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getVirtualPageNr() == pageNr) {
                return true;
            }
        }

        return false;
    }

    public Object[] getLRUEntry() {
        LocalTime lruTime = LocalTime.MAX;
        int indexLRU = -1;
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getEnterTime().isBefore(lruTime)) {
                lruTime = entries.get(i).getEnterTime();
                indexLRU = i;
            }
        }

        PageTableEntry lruPage = entries.get(indexLRU);

        return new Object[]{lruPage.getVirtualPageNr(), lruPage.getEnterTime()};
    }

    public void updateTimeStamp(int pageNr) {
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getVirtualPageNr() == pageNr) {
                entries.get(i).setEnterTime(LocalTime.now());
            }
        }
    }

    public void updateFIFO(List<PageTableEntry> pageTableEntries) {
        int i = 1;
        List<PageTableEntry> newEntries = new ArrayList<>();
        for (PageTableEntry entry : pageTableEntries) {
            if (i <= nrOfEntries) {
                if (entry.getPhysicalPageNr() != -1) {
                    entry.setIndex(i++);
                    newEntries.add(entry);
                }
            }
        }

        this.entries = newEntries;
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
