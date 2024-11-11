package model;

import java.sql.Time;

public class PageTableEntry {
    private int index;
    private int virtualPageNr;
    private int physicalPageNr;
    private Time enterTime;
}
