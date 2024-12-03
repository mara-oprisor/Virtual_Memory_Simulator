package logic;

import model.PageTable;
import model.PageTableEntry;
import model.TLB;
import util.JSONUtil;

import java.sql.Time;
import java.util.*;

public class TableManager {
    public void populatePageTable(int nrVirtualPages, int nrPhysicalPages, PageTable pageTable) {
        HashSet<Integer> physicalMappings = new HashSet<>();
        Random rand = new Random();
        List<Integer> virtualPageIndices = new ArrayList<>();

        for (int i = 0; i < nrVirtualPages; i++) {
            virtualPageIndices.add(i);
        }
        Collections.shuffle(virtualPageIndices, rand);

        for (int i = 0; i < nrVirtualPages; i++) {
            int virtualPageNr = virtualPageIndices.get(i);
            int physicalPageNr;
            boolean isMapped;

            if (i < nrPhysicalPages) {
                do {
                    physicalPageNr = rand.nextInt(nrPhysicalPages);
                } while (physicalMappings.contains(physicalPageNr));

                physicalMappings.add(physicalPageNr);
                isMapped = true;
            } else {
                physicalPageNr = -1;
                isMapped = false;
            }

            PageTableEntry entry = new PageTableEntry(i, virtualPageNr, physicalPageNr, isMapped);
            pageTable.addEntry(entry);
        }

        int index = 1;
        Collections.shuffle(pageTable.getEntries());
        for (PageTableEntry entry : pageTable.getEntries()) {
            entry.setIndex(index++);
        }
    }

    public void populateTLB(int tlbEntries, TLB tlb, PageTable pageTable) {
        Random rand = new Random();

        List<PageTableEntry> validEntries = new ArrayList<>();
        for (PageTableEntry entry : pageTable.getEntries()) {
            if (entry.getPhysicalPageNr() != -1) {
                validEntries.add(entry);
            }
        }

        Collections.shuffle(validEntries, rand);

        int entriesToAdd = Math.min(tlbEntries, validEntries.size());
        for (int i = 0; i < entriesToAdd; i++) {
            PageTableEntry entry = validEntries.get(i);
            entry.setEnterTime(new Time(System.currentTimeMillis()));
            tlb.addEntry(entry);
        }

        int index = 1;
        Collections.shuffle(tlb.getEntries());
        for (PageTableEntry entry : tlb.getEntries()) {
            entry.setIndex(index++);
        }
    }

    public List<Integer> extractUnmappedPages(PageTable pageTable) {
        List<Integer> pages = new ArrayList<>();
        for (PageTableEntry entry : pageTable.getEntries()) {
            if (entry.getPhysicalPageNr() == -1) {
                pages.add(entry.getVirtualPageNr());
            }
        }

        return pages;
    }

    public List<Integer> extractMappedPages(PageTable pageTable) {
        List<Integer> pages = new ArrayList<>();
        for (PageTableEntry entry : pageTable.getEntries()) {
            if (entry.getPhysicalPageNr() != -1) {
                pages.add(entry.getVirtualPageNr());
            }
        }

        return pages;
    }
}
