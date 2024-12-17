package model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PageTable {
    private final int nrOfEntries;
    private List<PageTableEntry> entries;

    public PageTable(int nrOfEntries) {
        this.nrOfEntries = nrOfEntries;
        this.entries = new ArrayList<>(nrOfEntries);
    }

    public void addEntry(PageTableEntry entry) {
        this.entries.add(entry);
    }

    public boolean isInPrincipalMemory(int pageNr) {
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getVirtualPageNr() == pageNr && entries.get(i).getPhysicalPageNr() != -1) {
                return true;
            }
        }

        return false;
    }

    public PageTableEntry getEntry(int pageNr) {
        for (PageTableEntry entry : entries) {
            if (entry.getVirtualPageNr() == pageNr) {
                return entry;
            }
        }

        return null;
    }

    public int getPhysicalPage(int pageNr) {
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getVirtualPageNr() == pageNr) {
                return entries.get(i).getPhysicalPageNr();
            }
        }

        return -2;
    }

    public Integer[] getLRUPage() {
        LocalTime lruTime = LocalTime.MAX;
        int indexLRU = -1;
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getPhysicalPageNr() != -1) {
                if (entries.get(i).getEnterTime().isBefore(lruTime)) {
                    lruTime = entries.get(i).getEnterTime();
                    indexLRU = i;
                }
            }
        }

        PageTableEntry lruPage = entries.get(indexLRU);

        return new Integer[]{lruPage.getVirtualPageNr(), lruPage.getPhysicalPageNr()};
    }

    public Integer[] getFirstInPage() {
        for (int i = 0; i < nrOfEntries; i++) {
            if (entries.get(i).getPhysicalPageNr() != -1) {
                return new Integer[]{entries.get(i).getVirtualPageNr(), entries.get(i).getPhysicalPageNr()};
            }
        }

        return new Integer[]{-1, -1};
    }

    public void replacePage(int virtualPage, int physicalPage, int virtualPageRemoved, String replacementPolicy) {
        if (replacementPolicy.equals("LRU")) {
            for (PageTableEntry entry : entries) {
                if (entry.getVirtualPageNr() == virtualPageRemoved) {
                    entry.setPhysicalPageNr(-1);
                    entry.setEnterTime(LocalTime.MIN);
                }
            }

            for (PageTableEntry entry : entries) {
                if (entry.getVirtualPageNr() == virtualPage) {
                    entry.setPhysicalPageNr(physicalPage);
                    entry.setEnterTime(LocalTime.now());
                }
            }
        } else {
            int index = 1;
            List<PageTableEntry> newEntries = new ArrayList<>();

            for (PageTableEntry entry : entries) {
                if (entry.getVirtualPageNr() == virtualPageRemoved) {
                    entry.setPhysicalPageNr(-1);
                    entry.setIndex(index++);
                    newEntries.add(entry);
                } else if (entry.getVirtualPageNr() != virtualPage) {
                    entry.setIndex(index++);
                    newEntries.add(entry);
                }
            }
            newEntries.add(new PageTableEntry(index, virtualPage, physicalPage, true));

            this.entries = newEntries;
        }

    }

    public void updateTimeStamp(int pageNr) {
        for (PageTableEntry entry : entries) {
            if (entry.getVirtualPageNr() == pageNr) {
                entry.setEnterTime(LocalTime.now());
            }
        }
    }

    public List<PageTableEntry> getEntries() {
        return entries;
    }
}
