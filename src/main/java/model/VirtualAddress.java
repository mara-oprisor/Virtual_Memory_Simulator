package model;

public class VirtualAddress {
    private String value;
    private int pageNumber;
    private int offset;

    public VirtualAddress(String value, int pageNumber, int offset) {
        this.value = value;
        this.pageNumber = pageNumber;
        this.offset = offset;
    }

    public static VirtualAddress parseVirtualAddress(String value, int pageSize) {
        int decimalValue = Integer.decode(value);
        int pageNumber = decimalValue / pageSize;
        int offset = decimalValue - pageNumber * pageSize;

        return new VirtualAddress(value, pageNumber, offset);
    }

    public String getValue() {
        return value;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getOffset() {
        return offset;
    }
}
