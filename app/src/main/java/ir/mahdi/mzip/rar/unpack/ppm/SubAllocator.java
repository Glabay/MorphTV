package ir.mahdi.mzip.rar.unpack.ppm;

import java.util.Arrays;

public class SubAllocator {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int FIXED_UNIT_SIZE = 12;
    public static final int N1 = 4;
    public static final int N2 = 4;
    public static final int N3 = 4;
    public static final int N4 = 26;
    public static final int N_INDEXES = 38;
    public static final int UNIT_SIZE = Math.max(PPMContext.size, 12);
    private int fakeUnitsStart;
    private final RarNode[] freeList = new RarNode[38];
    private int freeListPos;
    private int glueCount;
    private byte[] heap;
    private int heapEnd;
    private int heapStart;
    private int hiUnit;
    private int[] indx2Units = new int[38];
    private int loUnit;
    private int pText;
    private int subAllocatorSize;
    private int tempMemBlockPos;
    private RarMemBlock tempRarMemBlock1 = null;
    private RarMemBlock tempRarMemBlock2 = null;
    private RarMemBlock tempRarMemBlock3 = null;
    private RarNode tempRarNode = null;
    private int[] units2Indx = new int[128];
    private int unitsStart;

    public SubAllocator() {
        clean();
    }

    public void clean() {
        this.subAllocatorSize = 0;
    }

    private void insertNode(int i, int i2) {
        RarNode rarNode = this.tempRarNode;
        rarNode.setAddress(i);
        rarNode.setNext(this.freeList[i2].getNext());
        this.freeList[i2].setNext(rarNode);
    }

    public void incPText() {
        this.pText++;
    }

    private int removeNode(int i) {
        int next = this.freeList[i].getNext();
        RarNode rarNode = this.tempRarNode;
        rarNode.setAddress(next);
        this.freeList[i].setNext(rarNode.getNext());
        return next;
    }

    private int U2B(int i) {
        return UNIT_SIZE * i;
    }

    private int MBPtr(int i, int i2) {
        return i + U2B(i2);
    }

    private void splitBlock(int i, int i2, int i3) {
        i2 = this.indx2Units[i2] - this.indx2Units[i3];
        i += U2B(this.indx2Units[i3]);
        i3 = this.indx2Units;
        int i4 = this.units2Indx[i2 - 1];
        if (i3[i4] != i2) {
            i4--;
            insertNode(i, i4);
            i3 = this.indx2Units[i4];
            i += U2B(i3);
            i2 -= i3;
        }
        insertNode(i, this.units2Indx[i2 - 1]);
    }

    public void stopSubAllocator() {
        if (this.subAllocatorSize != 0) {
            this.subAllocatorSize = 0;
            this.heap = null;
            this.heapStart = 1;
            this.tempRarNode = null;
            this.tempRarMemBlock1 = null;
            this.tempRarMemBlock2 = null;
            this.tempRarMemBlock3 = null;
        }
    }

    public int GetAllocatedMemory() {
        return this.subAllocatorSize;
    }

    public boolean startSubAllocator(int i) {
        i <<= 20;
        if (this.subAllocatorSize == i) {
            return true;
        }
        stopSubAllocator();
        int i2 = ((i / 12) * UNIT_SIZE) + UNIT_SIZE;
        int i3 = (i2 + 1) + 152;
        this.tempMemBlockPos = i3;
        this.heap = new byte[(i3 + 12)];
        this.heapStart = 1;
        this.heapEnd = (this.heapStart + i2) - UNIT_SIZE;
        this.subAllocatorSize = i;
        this.freeListPos = this.heapStart + i2;
        i = 0;
        i2 = this.freeListPos;
        while (i < this.freeList.length) {
            this.freeList[i] = new RarNode(this.heap);
            this.freeList[i].setAddress(i2);
            i++;
            i2 += 4;
        }
        this.tempRarNode = new RarNode(this.heap);
        this.tempRarMemBlock1 = new RarMemBlock(this.heap);
        this.tempRarMemBlock2 = new RarMemBlock(this.heap);
        this.tempRarMemBlock3 = new RarMemBlock(this.heap);
        return true;
    }

    private void glueFreeBlocks() {
        RarMemBlock rarMemBlock = this.tempRarMemBlock1;
        rarMemBlock.setAddress(this.tempMemBlockPos);
        RarMemBlock rarMemBlock2 = this.tempRarMemBlock2;
        RarMemBlock rarMemBlock3 = this.tempRarMemBlock3;
        int i = 0;
        if (this.loUnit != this.hiUnit) {
            this.heap[this.loUnit] = (byte) 0;
        }
        rarMemBlock.setPrev(rarMemBlock);
        rarMemBlock.setNext(rarMemBlock);
        while (i < 38) {
            while (this.freeList[i].getNext() != 0) {
                rarMemBlock2.setAddress(removeNode(i));
                rarMemBlock2.insertAt(rarMemBlock);
                rarMemBlock2.setStamp(65535);
                rarMemBlock2.setNU(this.indx2Units[i]);
            }
            i++;
        }
        rarMemBlock2.setAddress(rarMemBlock.getNext());
        while (rarMemBlock2.getAddress() != rarMemBlock.getAddress()) {
            rarMemBlock3.setAddress(MBPtr(rarMemBlock2.getAddress(), rarMemBlock2.getNU()));
            while (rarMemBlock3.getStamp() == 65535 && rarMemBlock2.getNU() + rarMemBlock3.getNU() < 65536) {
                rarMemBlock3.remove();
                rarMemBlock2.setNU(rarMemBlock2.getNU() + rarMemBlock3.getNU());
                rarMemBlock3.setAddress(MBPtr(rarMemBlock2.getAddress(), rarMemBlock2.getNU()));
            }
            rarMemBlock2.setAddress(rarMemBlock2.getNext());
        }
        rarMemBlock2.setAddress(rarMemBlock.getNext());
        while (rarMemBlock2.getAddress() != rarMemBlock.getAddress()) {
            rarMemBlock2.remove();
            int nu = rarMemBlock2.getNU();
            while (nu > 128) {
                insertNode(rarMemBlock2.getAddress(), 37);
                nu -= 128;
                rarMemBlock2.setAddress(MBPtr(rarMemBlock2.getAddress(), 128));
            }
            int[] iArr = this.indx2Units;
            int i2 = this.units2Indx[nu - 1];
            if (iArr[i2] != nu) {
                i2--;
                int i3 = nu - this.indx2Units[i2];
                insertNode(MBPtr(rarMemBlock2.getAddress(), nu - i3), i3 - 1);
            }
            insertNode(rarMemBlock2.getAddress(), i2);
            rarMemBlock2.setAddress(rarMemBlock.getNext());
        }
    }

    private int allocUnitsRare(int i) {
        if (this.glueCount == 0) {
            this.glueCount = 255;
            glueFreeBlocks();
            if (this.freeList[i].getNext() != 0) {
                return removeNode(i);
            }
        }
        int i2 = i;
        do {
            i2++;
            if (i2 == 38) {
                this.glueCount--;
                i2 = U2B(this.indx2Units[i]);
                i = this.indx2Units[i] * 12;
                if (this.fakeUnitsStart - this.pText <= i) {
                    return 0;
                }
                this.fakeUnitsStart -= i;
                this.unitsStart -= i2;
                return this.unitsStart;
            }
        } while (this.freeList[i2].getNext() == 0);
        int removeNode = removeNode(i2);
        splitBlock(removeNode, i2, i);
        return removeNode;
    }

    public int allocUnits(int i) {
        i = this.units2Indx[i - 1];
        if (this.freeList[i].getNext() != 0) {
            return removeNode(i);
        }
        int i2 = this.loUnit;
        this.loUnit += U2B(this.indx2Units[i]);
        if (this.loUnit <= this.hiUnit) {
            return i2;
        }
        this.loUnit -= U2B(this.indx2Units[i]);
        return allocUnitsRare(i);
    }

    public int allocContext() {
        if (this.hiUnit != this.loUnit) {
            int i = this.hiUnit - UNIT_SIZE;
            this.hiUnit = i;
            return i;
        } else if (this.freeList[0].getNext() != 0) {
            return removeNode(0);
        } else {
            return allocUnitsRare(0);
        }
    }

    public int expandUnits(int i, int i2) {
        int i3 = i2 - 1;
        int i4 = this.units2Indx[i3];
        if (i4 == this.units2Indx[i3 + 1]) {
            return i;
        }
        i3 = allocUnits(i2 + 1);
        if (i3 != 0) {
            System.arraycopy(this.heap, i, this.heap, i3, U2B(i2));
            insertNode(i, i4);
        }
        return i3;
    }

    public int shrinkUnits(int i, int i2, int i3) {
        i2 = this.units2Indx[i2 - 1];
        int i4 = this.units2Indx[i3 - 1];
        if (i2 == i4) {
            return i;
        }
        if (this.freeList[i4].getNext() != 0) {
            i4 = removeNode(i4);
            System.arraycopy(this.heap, i, this.heap, i4, U2B(i3));
            insertNode(i, i2);
            return i4;
        }
        splitBlock(i, i2, i4);
        return i;
    }

    public void freeUnits(int i, int i2) {
        insertNode(i, this.units2Indx[i2 - 1]);
    }

    public int getFakeUnitsStart() {
        return this.fakeUnitsStart;
    }

    public void setFakeUnitsStart(int i) {
        this.fakeUnitsStart = i;
    }

    public int getHeapEnd() {
        return this.heapEnd;
    }

    public int getPText() {
        return this.pText;
    }

    public void setPText(int i) {
        this.pText = i;
    }

    public void decPText(int i) {
        setPText(getPText() - i);
    }

    public int getUnitsStart() {
        return this.unitsStart;
    }

    public void setUnitsStart(int i) {
        this.unitsStart = i;
    }

    public void initSubAllocator() {
        Arrays.fill(this.heap, this.freeListPos, this.freeListPos + sizeOfFreeList(), (byte) 0);
        this.pText = this.heapStart;
        int i = (((this.subAllocatorSize / 8) / 12) * 7) * 12;
        int i2 = (i / 12) * UNIT_SIZE;
        int i3 = this.subAllocatorSize - i;
        i = ((i3 / 12) * UNIT_SIZE) + (i3 % 12);
        this.hiUnit = this.heapStart + this.subAllocatorSize;
        int i4 = this.heapStart + i;
        this.unitsStart = i4;
        this.loUnit = i4;
        this.fakeUnitsStart = this.heapStart + i3;
        this.hiUnit = this.loUnit + i2;
        i2 = 0;
        i3 = 1;
        while (i2 < 4) {
            this.indx2Units[i2] = i3 & 255;
            i2++;
            i3++;
        }
        i3++;
        while (i2 < 8) {
            this.indx2Units[i2] = i3 & 255;
            i2++;
            i3 += 2;
        }
        i3++;
        while (i2 < 12) {
            this.indx2Units[i2] = i3 & 255;
            i2++;
            i3 += 3;
        }
        i3++;
        while (i2 < 38) {
            this.indx2Units[i2] = i3 & 255;
            i2++;
            i3 += 4;
        }
        this.glueCount = 0;
        int i5 = 0;
        int i6 = 0;
        while (i5 < 128) {
            i3 = i5 + 1;
            i6 += this.indx2Units[i6] < i3 ? 1 : 0;
            this.units2Indx[i5] = i6 & 255;
            i5 = i3;
        }
    }

    private int sizeOfFreeList() {
        return this.freeList.length * 4;
    }

    public byte[] getHeap() {
        return this.heap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SubAllocator[");
        stringBuilder.append("\n  subAllocatorSize=");
        stringBuilder.append(this.subAllocatorSize);
        stringBuilder.append("\n  glueCount=");
        stringBuilder.append(this.glueCount);
        stringBuilder.append("\n  heapStart=");
        stringBuilder.append(this.heapStart);
        stringBuilder.append("\n  loUnit=");
        stringBuilder.append(this.loUnit);
        stringBuilder.append("\n  hiUnit=");
        stringBuilder.append(this.hiUnit);
        stringBuilder.append("\n  pText=");
        stringBuilder.append(this.pText);
        stringBuilder.append("\n  unitsStart=");
        stringBuilder.append(this.unitsStart);
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
