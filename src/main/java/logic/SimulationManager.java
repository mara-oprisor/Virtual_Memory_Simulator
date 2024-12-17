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
    private TableManager tableManager;

    public String initialiseSimulation(HashMap<String, Integer> inputData) {
        this.vmSize = (int) Math.pow(2, inputData.get("vmSize"));
        this.pmSize = (int) Math.pow(2, inputData.get("physMem"));
        this.offsetBits = inputData.get("offsetBits");
        this.tlbEntries = inputData.get("tlbEntries");
        this.pageSize = (int) Math.pow(2, this.offsetBits);
        this.nrPhysicalPages = this.pmSize / this.pageSize;
        this.nrVirtualPages = this.vmSize / this.pageSize;

        this.tableManager = new TableManager();

        this.pageTable = new PageTable(this.nrVirtualPages);
        tableManager.populatePageTable(nrVirtualPages, nrPhysicalPages, pageTable);

        this.tlb = new TLB(this.tlbEntries);
        tableManager.populateTLB(tlbEntries, tlb, pageTable);

        this.physicalMemory = new PhysicalMemory(this.nrPhysicalPages, this.pageSize, this.pmSize);

        this.disk = new Disk(this.pageSize, this.pmSize, tableManager.extractUnmappedPages(pageTable));
        createDisk();

        StringBuilder sb = new StringBuilder("Simulation is ready to start!\n\n");
        sb.append("Number of offset bits = ").append(this.offsetBits).append(" bits\n");
        sb.append("Size of pages (physical and virtual) = 2^").append(this.offsetBits).append(" = ").append(this.pageSize).append("\n");
        sb.append("Physical memory size = 2^").append(inputData.get("physMem")).append(" = ").append(this.pmSize).append("\n");
        sb.append("Number of physical pages = ").append(this.pmSize).append(" / ").append(this.pageSize).append(" = ").append(this.nrPhysicalPages).append("\n");
        sb.append("Virtual memory size = 2^").append(inputData.get("vmSize")).append(" = ").append(this.vmSize).append("\n");
        sb.append("Number of virtual pages = ").append(this.vmSize).append(" / ").append(this.pageSize).append(" = ").append(this.nrVirtualPages).append("\n");
        sb.append("\nPlease select a scenario.");

        return sb.toString();
    }

    public void createDisk() {
        JSONUtil.saveToJSON("disk.json", JSONUtil.formatDiskToJSON(disk));
    }

    public boolean checkPageInTLB(int pageNr) {
        return tlb.isInTLB(pageNr);
    }

    public boolean checkPageInPageTable(int pageNr) {
        return pageTable.isInPrincipalMemory(pageNr);
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

    public List<String> getValuesFromPhysicalMemory(int physicalPageNumber) {
        return physicalMemory.retrieveAllData(physicalPageNumber);
    }

    public List<String> getDataFromDisk(int pageNr) {
        return disk.retrieveData(pageNr);
    }

    public void storeValueToMemory(int page, int offset, String value) {
        physicalMemory.writeValue(page, offset, value);
    }

    public void storeValuesToDisk(int page, List<String> data) {
        disk.writeData(page, data);
    }

    public void deleteValuesFromDisk(int page) {
        disk.deleteEntry(page);
    }

    public void replaceDataInPhysicalMemory(int page, List<String> data) {
        physicalMemory.replaceData(page, data);
    }

    public void changePageTable(int virtualPage, int physicalPage, int virtualPageRemoved, String replacementPolicy) {
        pageTable.replacePage(virtualPage, physicalPage, virtualPageRemoved, replacementPolicy);
    }

    public void updateTLBFIFO() {
        tlb.updateFIFO(pageTable.getEntries());
    }

    public void updateTimeStamp(boolean inTLB, int pageNr) {
        if (inTLB) {
            tlb.updateTimeStamp(pageNr);
        }

        pageTable.updateTimeStamp(pageNr);
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

    public int getPageSize() {
        return pageSize;
    }
}
