package model;

import java.sql.Time;

public class PageTableEntry {
    private int index;
    private int virtualPageNr;
    private int physicalPageNr;
    private Time enterTime;

    public PageTableEntry(int index, int virtualPageNr, int physicalPageNr, boolean isMapped) {
        this.index = index;
        this.virtualPageNr = virtualPageNr;
        this.physicalPageNr = physicalPageNr;

        if(isMapped) {
            this.enterTime = new Time(System.currentTimeMillis());
        } else {
            this.enterTime = Time.valueOf("00:00:00");
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
}
