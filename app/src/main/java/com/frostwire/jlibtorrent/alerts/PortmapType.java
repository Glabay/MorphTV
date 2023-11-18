package com.frostwire.jlibtorrent.alerts;

public enum PortmapType {
    NAT_PMP(0),
    UPNP(1),
    UNKNOWN(-1);
    
    private final int swigValue;

    private PortmapType(int i) {
        this.swigValue = i;
    }

    public int swig() {
        return this.swigValue;
    }

    public static PortmapType fromSwig(int i) {
        for (PortmapType portmapType : (PortmapType[]) PortmapType.class.getEnumConstants()) {
            if (portmapType.swig() == i) {
                return portmapType;
            }
        }
        return UNKNOWN;
    }
}
