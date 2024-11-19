package logic;

import model.*;
import util.JSONUtil;

import java.util.*;

public class SimulationManager {
    private int vmSize;
    private int pmSize;
    private int offsetBits;
    private int tlbEntries;
    private int pageSize;
    private int nrPhysicalPages;
    private int nrVirtualPages;
    private PageTable pageTable;
    private TLB tlb;
    private PhysicalMemory physicalMemory;
    private Disk disk;

    public String initialiseSimulation(HashMap<String, Integer> inputData) {
        this.vmSize = (int) Math.pow(2, inputData.get("vmSize"));
        this.pmSize = (int) Math.pow(2, inputData.get("physMem"));
        this.offsetBits = inputData.get("offsetBits");
        this.tlbEntries = inputData.get("tlbEntries");
        this.pageSize = (int) Math.pow(2, this.offsetBits);
        this.nrPhysicalPages = this.pmSize / this.pageSize;
        this.nrVirtualPages = this.vmSize / this.pageSize;

        this.pageTable = new PageTable(this.nrVirtualPages);
        populatePageTable();

        this.tlb = new TLB(this.tlbEntries);
        populateTLB();

        this.physicalMemory = new PhysicalMemory(this.nrPhysicalPages, this.pageSize, this.pmSize);

        this.disk = new Disk(this.pageSize, this.pmSize, extractUnmappedPages());
        JSONUtil.saveToJSON("disk.json", JSONUtil.formatDiskToJSON(disk));

        StringBuilder sb = new StringBuilder("Simulation is ready to start!\n\n");
        sb.append("Number of offset bits = ").append(this.offsetBits).append(" bits\n");
        sb.append("Size of pages (physical and virtual) = 2^").append(this.offsetBits).append(" = ").append(this.pageSize).append("\n");
        sb.append("Physical memory size = 2^").append(inputData.get("physMem")).append(" = ").append(this.pmSize).append("\n");
        sb.append("Number of physical pages = ").append(this.pmSize).append(" / ").append(this.pageSize).append(" = ").append(this.nrPhysicalPages).append("\n");
        sb.append("Virtual memory size = 2^").append(inputData.get("vmSize")).append(" = ").append(this.vmSize).append("\n");
        sb.append("Number of virtual pages = ").append(this.vmSize).append(" / ").append(this.pageSize).append(" = ").append(this.nrVirtualPages).append("\n");
        sb.append("\nPlease select a scenario.");

        return String.valueOf(sb);
    }

    private void populatePageTable() {
        HashSet<Integer> physicalMappings = new HashSet<>();
        Random rand = new Random();
        List<Integer> virtualPageIndices = new ArrayList<>();

        for (int i = 0; i < this.nrVirtualPages; i++) {
            virtualPageIndices.add(i);
        }
        Collections.shuffle(virtualPageIndices, rand);

        for(int i = 0; i < this.nrVirtualPages; i++) {
            int virtualPageNr = virtualPageIndices.get(i);
            int physicalPageNr;
            boolean isMapped;

            if(i < this.nrPhysicalPages) {
                do {
                    physicalPageNr = rand.nextInt(this.nrPhysicalPages);
                } while(physicalMappings.contains(physicalPageNr));

                physicalMappings.add(physicalPageNr);
                isMapped = true;
            } else {
                physicalPageNr = -1;
                isMapped = false;
            }

            PageTableEntry entry = new PageTableEntry(i, virtualPageNr, physicalPageNr, isMapped);
            this.pageTable.addEntry(entry);
        }

        int index = 1;
        Collections.shuffle(this.pageTable.getEntries());
        for(PageTableEntry entry: this.pageTable.getEntries()) {
            entry.setIndex(index++);
        }
    }

    private void populateTLB() {
        Random rand = new Random();

        List<PageTableEntry> validEntries = new ArrayList<>();
        for (PageTableEntry entry : this.pageTable.getEntries()) {
            if (entry.getPhysicalPageNr() != -1) {
                validEntries.add(entry);
            }
        }

        Collections.shuffle(validEntries, rand);

        int entriesToAdd = Math.min(this.tlbEntries, validEntries.size());
        for (int i = 0; i < entriesToAdd; i++) {
            PageTableEntry entry = validEntries.get(i);
            this.tlb.addEntry(entry);
        }

        int index = 1;
        Collections.shuffle(this.tlb.getEntries());
        for (PageTableEntry entry : this.tlb.getEntries()) {
            entry.setIndex(index++);
        }
    }

    private List<Integer> extractUnmappedPages() {
        List<Integer> pages = new ArrayList<>();
        for(PageTableEntry entry: pageTable.getEntries()) {
            if(entry.getPhysicalPageNr() == -1) {
                pages.add(entry.getVirtualPageNr());
            }
        }

        return pages;
    }

    private List<Integer> extractMappedPages() {
        List<Integer> pages = new ArrayList<>();
        for(PageTableEntry entry: pageTable.getEntries()) {
            if(entry.getPhysicalPageNr() != -1) {
                pages.add(entry.getVirtualPageNr());
            }
        }

        return pages;
    }

    public int generateMappedAddress(boolean fromTLB) {
        List<Integer> mappedAddresses = new ArrayList<>();
        Random random = new Random();

        List<Integer> TLBPages = new ArrayList<>();
        for(PageTableEntry entry: tlb.getEntries()) {
            int pageNr = entry.getVirtualPageNr();
            TLBPages.add(pageNr);
        }

        if(fromTLB) {
            for(Integer pageNr: TLBPages) {
                for(int offset = 0; offset < pageSize; offset++) {
                    int address = pageNr * pageSize + offset;
                    mappedAddresses.add(address);
                }
            }
        } else {
            List<Integer> pages = extractMappedPages();
            for(Integer pageNr: pages) {
                if(!TLBPages.contains(pageNr)) {
                    for(int offset = 0; offset < pageSize; offset++) {
                        int address = pageNr * pageSize + offset;
                        mappedAddresses.add(address);
                    }
                }
            }
        }

        return mappedAddresses.get(random.nextInt(mappedAddresses.size()));
    }

    public int generateUnmappedAddress() {
        List<Integer> unmappedAddresses = new ArrayList<>();
        Random random = new Random();

        List<Integer> pages = extractUnmappedPages();
        for(Integer pageNr: pages) {
            for(int offset = 0; offset < pageSize; offset++) {
                int address = pageNr * pageSize + offset;
                unmappedAddresses.add(address);
            }
        }

        return unmappedAddresses.get(random.nextInt(unmappedAddresses.size() - 1));
    }


    public PageTable getPageTable() {
        return pageTable;
    }

    public TLB getTlb() {
        return tlb;
    }

    public PhysicalMemory getPhysicalMemory() {
        return physicalMemory;
    }


    public boolean checkPageInTLB(int pageNr) {
        return tlb.isInTLB(pageNr);
    }

    public boolean checkPageInPageTable(int pageNr) {
        return pageTable.isInPageTable(pageNr);
    }

    public int getPhysicalPagePageTable(int pageNr) {
        return pageTable.getPhysicalPage(pageNr);
    }

    public int calculatePhysicalAddress(int pageNr, int offset) {
        return pageNr * pageSize + offset;
    }

    public String getValueFromPhysicalMemory(int physicalPageNumber, int offset) {
        return physicalMemory.retrieveData(physicalPageNumber, offset);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void storeValueToMemory(int page, int offset, String value) {
        physicalMemory.writeValue(page, offset, value);
    }
}
