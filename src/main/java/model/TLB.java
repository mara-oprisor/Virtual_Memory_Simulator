package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
        Time lruTime = new Time(Long.MAX_VALUE);
        int indexLRU = -1;
        for (int i = 0; i < nrOfEntries; i++) {
            if (lruTime.after(entries.get(i).getEnterTime())) {
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
                entries.get(i).setEnterTime(new Time(System.currentTimeMillis()));
            }
        }
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
