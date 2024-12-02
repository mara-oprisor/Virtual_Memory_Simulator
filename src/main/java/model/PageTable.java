package model;

import java.util.ArrayList;
import java.util.Date;
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

    public boolean isInPrincipalMemory(int pageNr) {
        for(int i = 0; i < nrOfEntries; i++) {
            if(entries.get(i).getVirtualPageNr() == pageNr && entries.get(i).getPhysicalPageNr() != -1) {
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

    public Integer[] getLRUPage() {
        Date lruDate = new Date(Long.MAX_VALUE);
        int indexLRU = -1;
        for(int i = 0; i < nrOfEntries; i++) {
            if(entries.get(i).getPhysicalPageNr() != -1) {
                if(lruDate.after(entries.get(i).getEnterTime())) {
                    lruDate = entries.get(i).getEnterTime();
                    indexLRU = i;
                }
            }
        }

        PageTableEntry lruPage = entries.get(indexLRU);

        return new Integer[]{lruPage.getVirtualPageNr(), lruPage.getPhysicalPageNr()};
    }

    public void replacePage(int virtualPage, int physicalPage, int virtualPageRemoved) {
        for(PageTableEntry entry: entries) {
            if(entry.getVirtualPageNr() == virtualPageRemoved) {
                entry.setPhysicalPageNr(-1);
            }
        }

        for(PageTableEntry entry: entries) {
            if (entry.getVirtualPageNr() == virtualPage) {
                entry.setPhysicalPageNr(physicalPage);
            }
        }
    }
}
