package ir.mahdi.mzip.rar.unpack.vm;

public enum VMStandardFilters {
    VMSF_NONE(0),
    VMSF_E8(1),
    VMSF_E8E9(2),
    VMSF_ITANIUM(3),
    VMSF_RGB(4),
    VMSF_AUDIO(5),
    VMSF_DELTA(6),
    VMSF_UPCASE(7);
    
    private int filter;

    private VMStandardFilters(int i) {
        this.filter = i;
    }

    public static VMStandardFilters findFilter(int i) {
        if (VMSF_NONE.equals(i)) {
            return VMSF_NONE;
        }
        if (VMSF_E8.equals(i)) {
            return VMSF_E8;
        }
        if (VMSF_E8E9.equals(i)) {
            return VMSF_E8E9;
        }
        if (VMSF_ITANIUM.equals(i)) {
            return VMSF_ITANIUM;
        }
        if (VMSF_RGB.equals(i)) {
            return VMSF_RGB;
        }
        if (VMSF_AUDIO.equals(i)) {
            return VMSF_AUDIO;
        }
        return VMSF_DELTA.equals(i) != 0 ? VMSF_DELTA : 0;
    }

    public int getFilter() {
        return this.filter;
    }

    public boolean equals(int i) {
        return this.filter == i;
    }
}
