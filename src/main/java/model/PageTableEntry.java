package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PageTableEntry {
    private int index;
    private int virtualPageNr;
    private int physicalPageNr;
    private LocalTime enterTime;

    public PageTableEntry(int index, int virtualPageNr, int physicalPageNr, boolean isMapped) {
        this.index = index;
        this.virtualPageNr = virtualPageNr;
        this.physicalPageNr = physicalPageNr;

        if (isMapped) {
            this.enterTime = LocalTime.now().plusSeconds(index);
        } else {
            this.enterTime = LocalTime.of(0, 0, 0);
        }
    }

    public int getIndex() {
        return index;
    }

    public int getVirtualPageNr() {
        return virtualPageNr;
    }

    public int getPhysicalPageNr() {
        return physicalPageNr;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LocalTime getEnterTime() {
        return enterTime;
    }

    public String getFormattedEnterTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return enterTime.format(formatter);
    }

    public void setEnterTime(LocalTime enterTime) {
        this.enterTime = enterTime;
    }

    public void setPhysicalPageNr(int physicalPageNr) {
        this.physicalPageNr = physicalPageNr;
    }

    public void setVirtualPageNr(int virtualPageNr) {
        this.virtualPageNr = virtualPageNr;
    }
}
