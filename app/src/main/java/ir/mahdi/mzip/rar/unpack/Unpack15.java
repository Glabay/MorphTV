package ir.mahdi.mzip.rar.unpack;

import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.vm.BitInput;
import java.io.IOException;
import java.util.Arrays;
import org.mozilla.universalchardet.prober.CharsetProber;

public abstract class Unpack15 extends BitInput {
    private static int[] DecHf0 = new int[]{32768, 49152, 57344, 61952, 61952, 61952, 61952, 61952, 65535};
    private static int[] DecHf1 = new int[]{8192, 49152, 57344, 61440, 61952, 61952, 63456, 65535};
    private static int[] DecHf2 = new int[]{4096, 9216, 32768, 49152, 64000, 65535, 65535, 65535};
    private static int[] DecHf3 = new int[]{2048, 9216, 60928, 65152, 65535, 65535, 65535};
    private static int[] DecHf4 = new int[]{MotionEventCompat.ACTION_POINTER_INDEX_MASK, 65535, 65535, 65535, 65535, 65535};
    private static int[] DecL1 = new int[]{32768, 40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 61952, 65535};
    private static int[] DecL2 = new int[]{40960, 49152, 53248, 57344, 59904, 60928, 61440, 61952, 62016, 65535};
    private static int[] PosHf0 = new int[]{0, 0, 0, 0, 0, 8, 16, 24, 33, 33, 33, 33, 33};
    private static int[] PosHf1 = new int[]{0, 0, 0, 0, 0, 0, 4, 44, 60, 76, 80, 80, 127};
    private static int[] PosHf2 = new int[]{0, 0, 0, 0, 0, 0, 2, 7, 53, 117, 233, 0, 0};
    private static int[] PosHf3 = new int[]{0, 0, 0, 0, 0, 0, 0, 2, 16, 218, 251, 0, 0};
    private static int[] PosHf4 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 0, 0, 0};
    private static int[] PosL1 = new int[]{0, 0, 0, 2, 3, 5, 7, 11, 16, 20, 24, 32, 32};
    private static int[] PosL2 = new int[]{0, 0, 0, 0, 5, 7, 9, 13, 18, 22, 26, 34, 36};
    private static final int STARTHF0 = 4;
    private static final int STARTHF1 = 5;
    private static final int STARTHF2 = 5;
    private static final int STARTHF3 = 6;
    private static final int STARTHF4 = 8;
    private static final int STARTL1 = 2;
    private static final int STARTL2 = 3;
    static int[] ShortLen1 = new int[]{1, 3, 4, 4, 5, 6, 7, 8, 8, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortLen2 = new int[]{2, 3, 3, 3, 4, 4, 5, 6, 6, 4, 4, 5, 6, 6, 4, 0};
    static int[] ShortXor1 = new int[]{0, 160, 208, 224, 240, 248, 252, 254, 255, 192, 128, 144, 152, 156, 176};
    static int[] ShortXor2 = new int[]{0, 64, 96, 160, 208, 224, 240, 248, 252, 192, 128, 144, 152, 156, 176};
    protected int AvrLn1;
    protected int AvrLn2;
    protected int AvrLn3;
    protected int AvrPlc;
    protected int AvrPlcB;
    protected int Buf60;
    protected int[] ChSet = new int[256];
    protected int[] ChSetA = new int[256];
    protected int[] ChSetB = new int[256];
    protected int[] ChSetC = new int[256];
    protected int FlagBuf;
    protected int FlagsCnt;
    protected int LCount;
    protected int MaxDist3;
    protected int[] NToPl = new int[256];
    protected int[] NToPlB = new int[256];
    protected int[] NToPlC = new int[256];
    protected int Nhfb;
    protected int Nlzb;
    protected int NumHuf;
    protected int[] Place = new int[256];
    protected int[] PlaceA = new int[256];
    protected int[] PlaceB = new int[256];
    protected int[] PlaceC = new int[256];
    protected int StMode;
    protected long destUnpSize;
    protected int lastDist;
    protected int lastLength;
    protected int[] oldDist = new int[4];
    protected int oldDistPtr;
    protected int readBorder;
    protected int readTop;
    protected boolean suspended;
    protected boolean unpAllBuf;
    protected ComprDataIO unpIO;
    protected int unpPtr;
    protected boolean unpSomeRead;
    protected byte[] window;
    protected int wrPtr;

    protected abstract void unpInitData(boolean z);

    protected void unpack15(boolean z) throws IOException, RarException {
        if (this.suspended) {
            this.unpPtr = this.wrPtr;
        } else {
            unpInitData(z);
            oldUnpInitData(z);
            unpReadBuf();
            if (z) {
                this.unpPtr = this.wrPtr;
            } else {
                initHuff();
                this.unpPtr = false;
            }
            this.destUnpSize--;
        }
        if (this.destUnpSize >= 0) {
            getFlagsBuf();
            this.FlagsCnt = true;
        }
        while (this.destUnpSize >= 0) {
            this.unpPtr &= true;
            if (this.inAddr > this.readTop - 30 && !unpReadBuf()) {
                break;
            }
            if (((this.wrPtr - this.unpPtr) & true) < true && this.wrPtr != this.unpPtr) {
                oldUnpWriteBuf();
                if (this.suspended) {
                    return;
                }
            }
            if (this.StMode) {
                huffDecode();
            } else {
                z = this.FlagsCnt - 1;
                this.FlagsCnt = z;
                if (z >= false) {
                    getFlagsBuf();
                    this.FlagsCnt = 7;
                }
                if (this.FlagBuf & 128) {
                    this.FlagBuf <<= 1;
                    if (this.Nlzb > this.Nhfb) {
                        longLZ();
                    } else {
                        huffDecode();
                    }
                } else {
                    this.FlagBuf <<= 1;
                    z = this.FlagsCnt - 1;
                    this.FlagsCnt = z;
                    if (z >= false) {
                        getFlagsBuf();
                        this.FlagsCnt = 7;
                    }
                    if (this.FlagBuf & 128) {
                        this.FlagBuf <<= 1;
                        if (this.Nlzb > this.Nhfb) {
                            huffDecode();
                        } else {
                            longLZ();
                        }
                    } else {
                        this.FlagBuf <<= 1;
                        shortLZ();
                    }
                }
            }
        }
        oldUnpWriteBuf();
    }

    protected boolean unpReadBuf() throws IOException, RarException {
        int i = this.readTop - this.inAddr;
        boolean z = false;
        if (i < 0) {
            return false;
        }
        if (this.inAddr > 16384) {
            if (i > 0) {
                System.arraycopy(this.inBuf, this.inAddr, this.inBuf, 0, i);
            }
            this.inAddr = 0;
            this.readTop = i;
        } else {
            i = this.readTop;
        }
        i = this.unpIO.unpRead(this.inBuf, i, (32768 - i) & -16);
        if (i > 0) {
            this.readTop += i;
        }
        this.readBorder = this.readTop - 30;
        if (i != -1) {
            z = true;
        }
        return z;
    }

    private int getShortLen1(int i) {
        return i == 1 ? this.Buf60 + 3 : ShortLen1[i];
    }

    private int getShortLen2(int i) {
        return i == 3 ? this.Buf60 + 3 : ShortLen2[i];
    }

    protected void shortLZ() {
        int i;
        this.NumHuf = 0;
        int fgetbits = fgetbits();
        if (this.LCount == 2) {
            faddbits(1);
            if (fgetbits >= 32768) {
                oldCopyString(this.lastDist, this.lastLength);
                return;
            } else {
                fgetbits <<= 1;
                this.LCount = 0;
            }
        }
        fgetbits >>>= 8;
        if (this.AvrLn1 < 37) {
            i = 0;
            while (((ShortXor1[i] ^ fgetbits) & ((255 >>> getShortLen1(i)) ^ -1)) != 0) {
                i++;
            }
            faddbits(getShortLen1(i));
        } else {
            i = 0;
            while (((ShortXor2[i] ^ fgetbits) & ((255 >> getShortLen2(i)) ^ -1)) != 0) {
                i++;
            }
            faddbits(getShortLen2(i));
        }
        int decodeNum;
        int i2;
        if (i < 9) {
            this.LCount = 0;
            this.AvrLn1 += i;
            this.AvrLn1 -= this.AvrLn1 >> 4;
            decodeNum = decodeNum(fgetbits(), 5, DecHf2, PosHf2) & 255;
            fgetbits = this.ChSetA[decodeNum];
            decodeNum--;
            if (decodeNum != -1) {
                int[] iArr = this.PlaceA;
                iArr[fgetbits] = iArr[fgetbits] - 1;
                i2 = this.ChSetA[decodeNum];
                int[] iArr2 = this.PlaceA;
                iArr2[i2] = iArr2[i2] + 1;
                this.ChSetA[decodeNum + 1] = i2;
                this.ChSetA[decodeNum] = fgetbits;
            }
            i += 2;
            int[] iArr3 = this.oldDist;
            i2 = this.oldDistPtr;
            this.oldDistPtr = i2 + 1;
            fgetbits++;
            iArr3[i2] = fgetbits;
            this.oldDistPtr &= 3;
            this.lastLength = i;
            this.lastDist = fgetbits;
            oldCopyString(fgetbits, i);
        } else if (i == 9) {
            this.LCount++;
            oldCopyString(this.lastDist, this.lastLength);
        } else if (i == 14) {
            this.LCount = 0;
            decodeNum = decodeNum(fgetbits(), 3, DecL2, PosL2) + 5;
            fgetbits = (fgetbits() >> 1) | 32768;
            faddbits(15);
            this.lastLength = decodeNum;
            this.lastDist = fgetbits;
            oldCopyString(fgetbits, decodeNum);
        } else {
            this.LCount = 0;
            decodeNum = this.oldDist[(this.oldDistPtr - (i - 9)) & 3];
            fgetbits = decodeNum(fgetbits(), 2, DecL1, PosL1) + 2;
            if (fgetbits == 257 && i == 10) {
                this.Buf60 ^= 1;
                return;
            }
            if (decodeNum > 256) {
                fgetbits++;
            }
            if (decodeNum >= this.MaxDist3) {
                fgetbits++;
            }
            int[] iArr4 = this.oldDist;
            i2 = this.oldDistPtr;
            this.oldDistPtr = i2 + 1;
            iArr4[i2] = decodeNum;
            this.oldDistPtr &= 3;
            this.lastLength = fgetbits;
            this.lastDist = decodeNum;
            oldCopyString(decodeNum, fgetbits);
        }
    }

    protected void longLZ() {
        int i;
        int i2;
        int i3;
        int[] iArr;
        int i4 = 0;
        this.NumHuf = 0;
        this.Nlzb += 16;
        if (this.Nlzb > 255) {
            this.Nlzb = 144;
            this.Nhfb >>>= 1;
        }
        int i5 = this.AvrLn2;
        int fgetbits = fgetbits();
        if (this.AvrLn2 >= CharsetProber.ASCII_Z) {
            fgetbits = decodeNum(fgetbits, 3, DecL2, PosL2);
        } else if (this.AvrLn2 >= 64) {
            fgetbits = decodeNum(fgetbits, 2, DecL1, PosL1);
        } else if (fgetbits < 256) {
            faddbits(16);
        } else {
            while (((fgetbits << i4) & 32768) == 0) {
                i4++;
            }
            faddbits(i4 + 1);
            fgetbits = i4;
        }
        this.AvrLn2 += fgetbits;
        this.AvrLn2 -= this.AvrLn2 >>> 5;
        i4 = fgetbits();
        if (this.AvrPlcB > 10495) {
            i4 = decodeNum(i4, 5, DecHf2, PosHf2);
        } else if (this.AvrPlcB > 1791) {
            i4 = decodeNum(i4, 5, DecHf1, PosHf1);
        } else {
            i4 = decodeNum(i4, 4, DecHf0, PosHf0);
        }
        this.AvrPlcB += i4;
        this.AvrPlcB -= this.AvrPlcB >> 8;
        while (true) {
            i = this.ChSetB[i4 & 255];
            int[] iArr2 = this.NToPlB;
            i2 = i + 1;
            i &= 255;
            i3 = iArr2[i];
            iArr2[i] = i3 + 1;
            if ((i2 & 255) != 0) {
                break;
            }
            corrHuff(this.ChSetB, this.NToPlB);
        }
        this.ChSetB[i4] = this.ChSetB[i3];
        this.ChSetB[i3] = i2;
        i4 = ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i2) | (fgetbits() >>> 8)) >>> 1;
        faddbits(7);
        i = this.AvrLn3;
        if (!(fgetbits == 1 || fgetbits == 4)) {
            if (fgetbits == 0 && i4 <= this.MaxDist3) {
                this.AvrLn3++;
                this.AvrLn3 -= this.AvrLn3 >> 8;
            } else if (this.AvrLn3 > 0) {
                this.AvrLn3--;
            }
        }
        fgetbits += 3;
        if (i4 >= this.MaxDist3) {
            fgetbits++;
        }
        if (i4 <= 256) {
            fgetbits += 8;
        }
        if (i <= 176) {
            if (this.AvrPlc < 10752 || i5 >= 64) {
                this.MaxDist3 = 8193;
                iArr = this.oldDist;
                i = this.oldDistPtr;
                this.oldDistPtr = i + 1;
                iArr[i] = i4;
                this.oldDistPtr &= 3;
                this.lastLength = fgetbits;
                this.lastDist = i4;
                oldCopyString(i4, fgetbits);
            }
        }
        this.MaxDist3 = 32512;
        iArr = this.oldDist;
        i = this.oldDistPtr;
        this.oldDistPtr = i + 1;
        iArr[i] = i4;
        this.oldDistPtr &= 3;
        this.lastLength = fgetbits;
        this.lastDist = i4;
        oldCopyString(i4, fgetbits);
    }

    protected void huffDecode() {
        int decodeNum;
        int fgetbits = fgetbits();
        int i = 4;
        if (this.AvrPlc > 30207) {
            decodeNum = decodeNum(fgetbits, 8, DecHf4, PosHf4);
        } else if (this.AvrPlc > 24063) {
            decodeNum = decodeNum(fgetbits, 6, DecHf3, PosHf3);
        } else if (this.AvrPlc > 13823) {
            decodeNum = decodeNum(fgetbits, 5, DecHf2, PosHf2);
        } else if (this.AvrPlc > 3583) {
            decodeNum = decodeNum(fgetbits, 5, DecHf1, PosHf1);
        } else {
            decodeNum = decodeNum(fgetbits, 4, DecHf0, PosHf0);
        }
        decodeNum &= 255;
        if (this.StMode != 0) {
            if (decodeNum == 0 && fgetbits > 4095) {
                decodeNum = 256;
            }
            decodeNum--;
            if (decodeNum == -1) {
                fgetbits = fgetbits();
                faddbits(1);
                if ((32768 & fgetbits) != 0) {
                    this.StMode = 0;
                    this.NumHuf = 0;
                    return;
                }
                if ((fgetbits & 16384) == 0) {
                    i = 3;
                }
                faddbits(1);
                fgetbits = (decodeNum(fgetbits(), 5, DecHf2, PosHf2) << 5) | (fgetbits() >>> 11);
                faddbits(5);
                oldCopyString(fgetbits, i);
                return;
            }
        }
        fgetbits = this.NumHuf;
        this.NumHuf = fgetbits + 1;
        if (fgetbits >= 16 && this.FlagsCnt == 0) {
            this.StMode = 1;
        }
        this.AvrPlc += decodeNum;
        this.AvrPlc -= this.AvrPlc >>> 8;
        this.Nhfb += 16;
        if (this.Nhfb > 255) {
            this.Nhfb = 144;
            this.Nlzb >>>= 1;
        }
        byte[] bArr = this.window;
        i = this.unpPtr;
        this.unpPtr = i + 1;
        bArr[i] = (byte) (this.ChSet[decodeNum] >>> 8);
        this.destUnpSize--;
        while (true) {
            fgetbits = this.ChSet[decodeNum];
            int[] iArr = this.NToPl;
            int i2 = fgetbits + 1;
            fgetbits &= 255;
            int i3 = iArr[fgetbits];
            iArr[fgetbits] = i3 + 1;
            if ((i2 & 255) > 161) {
                corrHuff(this.ChSet, this.NToPl);
            } else {
                this.ChSet[decodeNum] = this.ChSet[i3];
                this.ChSet[i3] = i2;
                return;
            }
        }
    }

    protected void getFlagsBuf() {
        int decodeNum = decodeNum(fgetbits(), 5, DecHf2, PosHf2);
        while (true) {
            int i = this.ChSetC[decodeNum];
            this.FlagBuf = i >>> 8;
            int[] iArr = this.NToPlC;
            int i2 = i + 1;
            i &= 255;
            int i3 = iArr[i];
            iArr[i] = i3 + 1;
            if ((i2 & 255) != 0) {
                this.ChSetC[decodeNum] = this.ChSetC[i3];
                this.ChSetC[i3] = i2;
                return;
            }
            corrHuff(this.ChSetC, this.NToPlC);
        }
    }

    protected void oldUnpInitData(boolean z) {
        if (!z) {
            this.Buf60 = 0;
            this.NumHuf = 0;
            this.AvrLn3 = 0;
            this.AvrLn2 = 0;
            this.AvrLn1 = 0;
            this.AvrPlcB = 0;
            this.AvrPlc = true;
            this.MaxDist3 = true;
            this.Nlzb = 128;
            this.Nhfb = 128;
        }
        this.FlagsCnt = 0;
        this.FlagBuf = 0;
        this.StMode = 0;
        this.LCount = 0;
        this.readTop = 0;
    }

    protected void initHuff() {
        for (int i = 0; i < 256; i++) {
            int[] iArr = this.Place;
            int[] iArr2 = this.PlaceA;
            this.PlaceB[i] = i;
            iArr2[i] = i;
            iArr[i] = i;
            int i2 = ((i ^ -1) + 1) & 255;
            this.PlaceC[i] = i2;
            iArr = this.ChSet;
            int i3 = i << 8;
            this.ChSetB[i] = i3;
            iArr[i] = i3;
            this.ChSetA[i] = i;
            this.ChSetC[i] = i2 << 8;
        }
        Arrays.fill(this.NToPl, 0);
        Arrays.fill(this.NToPlB, 0);
        Arrays.fill(this.NToPlC, 0);
        corrHuff(this.ChSetB, this.NToPlB);
    }

    protected void corrHuff(int[] iArr, int[] iArr2) {
        int i = 7;
        int i2 = 0;
        while (i >= 0) {
            int i3 = i2;
            i2 = 0;
            while (i2 < 32) {
                iArr[i3] = (iArr[i3] & InputDeviceCompat.SOURCE_ANY) | i;
                i2++;
                i3++;
            }
            i--;
            i2 = i3;
        }
        Arrays.fill(iArr2, 0);
        for (iArr = 6; iArr >= null; iArr--) {
            iArr2[iArr] = (7 - iArr) * 32;
        }
    }

    protected void oldCopyString(int i, int i2) {
        this.destUnpSize -= (long) i2;
        while (true) {
            int i3 = i2 - 1;
            if (i2 != 0) {
                this.window[this.unpPtr] = this.window[(this.unpPtr - i) & Compress.MAXWINMASK];
                this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                i2 = i3;
            } else {
                return;
            }
        }
    }

    protected int decodeNum(int i, int i2, int[] iArr, int[] iArr2) {
        i &= 65520;
        int i3 = 0;
        int i4 = i2;
        i2 = 0;
        while (iArr[i2] <= i) {
            i4++;
            i2++;
        }
        faddbits(i4);
        if (i2 != 0) {
            i3 = iArr[i2 - 1];
        }
        return ((i - i3) >>> (16 - i4)) + iArr2[i4];
    }

    protected void oldUnpWriteBuf() throws IOException {
        if (this.unpPtr != this.wrPtr) {
            this.unpSomeRead = true;
        }
        if (this.unpPtr < this.wrPtr) {
            this.unpIO.unpWrite(this.window, this.wrPtr, (-this.wrPtr) & Compress.MAXWINMASK);
            this.unpIO.unpWrite(this.window, 0, this.unpPtr);
            this.unpAllBuf = true;
        } else {
            this.unpIO.unpWrite(this.window, this.wrPtr, this.unpPtr - this.wrPtr);
        }
        this.wrPtr = this.unpPtr;
    }
}
