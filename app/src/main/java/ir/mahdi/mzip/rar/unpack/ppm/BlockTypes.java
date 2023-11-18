package ir.mahdi.mzip.rar.unpack.ppm;

public enum BlockTypes {
    BLOCK_LZ(0),
    BLOCK_PPM(1);
    
    private int blockType;

    private BlockTypes(int i) {
        this.blockType = i;
    }

    public static BlockTypes findBlockType(int i) {
        if (BLOCK_LZ.equals(i)) {
            return BLOCK_LZ;
        }
        return BLOCK_PPM.equals(i) != 0 ? BLOCK_PPM : 0;
    }

    public int getBlockType() {
        return this.blockType;
    }

    public boolean equals(int i) {
        return this.blockType == i;
    }
}
