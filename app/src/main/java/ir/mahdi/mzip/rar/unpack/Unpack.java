package ir.mahdi.mzip.rar.unpack;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.media.MediaRouter.GlobalMediaRouter.CallbackHandler;
import com.google.common.base.Ascii;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.ppm.BlockTypes;
import ir.mahdi.mzip.rar.unpack.ppm.ModelPPM;
import ir.mahdi.mzip.rar.unpack.ppm.SubAllocator;
import ir.mahdi.mzip.rar.unpack.vm.BitInput;
import ir.mahdi.mzip.rar.unpack.vm.RarVM;
import ir.mahdi.mzip.rar.unpack.vm.VMPreparedProgram;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public final class Unpack extends Unpack20 {
    public static int[] DBitLengthCounts = new int[]{4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 14, 0, 12};
    private boolean externalWindow;
    private boolean fileExtracted;
    private List<UnpackFilter> filters = new ArrayList();
    private int lastFilter;
    private int lowDistRepCount;
    private List<Integer> oldFilterLengths = new ArrayList();
    private final ModelPPM ppm = new ModelPPM();
    private boolean ppmError;
    private int ppmEscChar;
    private int prevLowDist;
    private List<UnpackFilter> prgStack = new ArrayList();
    private RarVM rarVM = new RarVM();
    private boolean tablesRead;
    private BlockTypes unpBlockType;
    private byte[] unpOldTable = new byte[Compress.HUFF_TABLE_SIZE];
    private long writtenFileSize;

    public Unpack(ComprDataIO comprDataIO) {
        this.unpIO = comprDataIO;
        this.window = null;
        this.externalWindow = false;
        this.suspended = false;
        this.unpAllBuf = false;
        this.unpSomeRead = false;
    }

    public void init(byte[] bArr) {
        if (bArr == null) {
            this.window = new byte[4194304];
        } else {
            this.window = bArr;
            this.externalWindow = 1;
        }
        this.inAddr = 0;
        unpInitData(false);
    }

    public void doUnpack(int i, boolean z) throws IOException, RarException {
        if (this.unpIO.getSubHeader().getUnpMethod() == (byte) 48) {
            unstoreFile();
        }
        if (i == 15) {
            unpack15(z);
        } else if (i == 20 || i == 26) {
            unpack20(z);
        } else if (i == 29 || i == 36) {
            unpack29(z);
        }
    }

    private void unstoreFile() throws IOException, RarException {
        byte[] bArr = new byte[65536];
        while (true) {
            int unpRead = this.unpIO.unpRead(bArr, 0, (int) Math.min((long) bArr.length, this.destUnpSize));
            if (unpRead == 0) {
                return;
            }
            if (unpRead != -1) {
                if (((long) unpRead) >= this.destUnpSize) {
                    unpRead = (int) this.destUnpSize;
                }
                this.unpIO.unpWrite(bArr, 0, unpRead);
                if (this.destUnpSize >= 0) {
                    this.destUnpSize -= (long) unpRead;
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void unpack29(boolean r12) throws java.io.IOException, ir.mahdi.mzip.rar.exception.RarException {
        /*
        r11 = this;
        r0 = 60;
        r1 = new int[r0];
        r0 = new byte[r0];
        r2 = 1;
        r3 = r1[r2];
        r4 = 0;
        if (r3 != 0) goto L_0x0030;
    L_0x000c:
        r3 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
    L_0x0010:
        r8 = DBitLengthCounts;
        r8 = r8.length;
        if (r3 >= r8) goto L_0x0030;
    L_0x0015:
        r8 = DBitLengthCounts;
        r8 = r8[r3];
        r9 = r5;
        r5 = 0;
    L_0x001b:
        if (r5 >= r8) goto L_0x002a;
    L_0x001d:
        r1[r7] = r9;
        r10 = (byte) r6;
        r0[r7] = r10;
        r5 = r5 + 1;
        r7 = r7 + 1;
        r10 = r2 << r6;
        r9 = r9 + r10;
        goto L_0x001b;
    L_0x002a:
        r3 = r3 + 1;
        r6 = r6 + 1;
        r5 = r9;
        goto L_0x0010;
    L_0x0030:
        r11.fileExtracted = r2;
        r3 = r11.suspended;
        if (r3 != 0) goto L_0x004d;
    L_0x0036:
        r11.unpInitData(r12);
        r3 = r11.unpReadBuf();
        if (r3 != 0) goto L_0x0040;
    L_0x003f:
        return;
    L_0x0040:
        if (r12 == 0) goto L_0x0046;
    L_0x0042:
        r12 = r11.tablesRead;
        if (r12 != 0) goto L_0x004d;
    L_0x0046:
        r12 = r11.readTables();
        if (r12 != 0) goto L_0x004d;
    L_0x004c:
        return;
    L_0x004d:
        r12 = r11.ppmError;
        if (r12 == 0) goto L_0x0052;
    L_0x0051:
        return;
    L_0x0052:
        r12 = r11.unpPtr;
        r3 = 4194303; // 0x3fffff float:5.87747E-39 double:2.072261E-317;
        r12 = r12 & r3;
        r11.unpPtr = r12;
        r12 = r11.inAddr;
        r5 = r11.readBorder;
        if (r12 <= r5) goto L_0x0068;
    L_0x0060:
        r12 = r11.unpReadBuf();
        if (r12 != 0) goto L_0x0068;
    L_0x0066:
        goto L_0x01c3;
    L_0x0068:
        r12 = r11.wrPtr;
        r5 = r11.unpPtr;
        r12 = r12 - r5;
        r12 = r12 & r3;
        r3 = 260; // 0x104 float:3.64E-43 double:1.285E-321;
        if (r12 >= r3) goto L_0x008b;
    L_0x0072:
        r12 = r11.wrPtr;
        r3 = r11.unpPtr;
        if (r12 == r3) goto L_0x008b;
    L_0x0078:
        r11.UnpWriteBuf();
        r5 = r11.writtenFileSize;
        r7 = r11.destUnpSize;
        r12 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r12 <= 0) goto L_0x0084;
    L_0x0083:
        return;
    L_0x0084:
        r12 = r11.suspended;
        if (r12 == 0) goto L_0x008b;
    L_0x0088:
        r11.fileExtracted = r4;
        return;
    L_0x008b:
        r12 = r11.unpBlockType;
        r3 = ir.mahdi.mzip.rar.unpack.ppm.BlockTypes.BLOCK_PPM;
        r5 = 3;
        r6 = 2;
        r7 = 4;
        if (r12 != r3) goto L_0x0114;
    L_0x0094:
        r12 = r11.ppm;
        r12 = r12.decodeChar();
        r3 = -1;
        if (r12 != r3) goto L_0x00a1;
    L_0x009d:
        r11.ppmError = r2;
        goto L_0x01c3;
    L_0x00a1:
        r8 = r11.ppmEscChar;
        if (r12 != r8) goto L_0x0107;
    L_0x00a5:
        r8 = r11.ppm;
        r8 = r8.decodeChar();
        if (r8 != 0) goto L_0x00b5;
    L_0x00ad:
        r12 = r11.readTables();
        if (r12 != 0) goto L_0x0052;
    L_0x00b3:
        goto L_0x01c3;
    L_0x00b5:
        if (r8 == r6) goto L_0x01c3;
    L_0x00b7:
        if (r8 != r3) goto L_0x00bb;
    L_0x00b9:
        goto L_0x01c3;
    L_0x00bb:
        if (r8 != r5) goto L_0x00c5;
    L_0x00bd:
        r12 = r11.readVMCodePPM();
        if (r12 != 0) goto L_0x0052;
    L_0x00c3:
        goto L_0x01c3;
    L_0x00c5:
        if (r8 != r7) goto L_0x00f3;
    L_0x00c7:
        r12 = 0;
        r6 = 0;
        r8 = 0;
        r9 = 0;
    L_0x00cb:
        if (r12 >= r7) goto L_0x00e6;
    L_0x00cd:
        if (r6 != 0) goto L_0x00e6;
    L_0x00cf:
        r10 = r11.ppm;
        r10 = r10.decodeChar();
        if (r10 != r3) goto L_0x00d9;
    L_0x00d7:
        r6 = 1;
        goto L_0x00e3;
    L_0x00d9:
        if (r12 != r5) goto L_0x00de;
    L_0x00db:
        r8 = r10 & 255;
        goto L_0x00e3;
    L_0x00de:
        r9 = r9 << 8;
        r10 = r10 & 255;
        r9 = r9 + r10;
    L_0x00e3:
        r12 = r12 + 1;
        goto L_0x00cb;
    L_0x00e6:
        if (r6 == 0) goto L_0x00ea;
    L_0x00e8:
        goto L_0x01c3;
    L_0x00ea:
        r8 = r8 + 32;
        r9 = r9 + 2;
        r11.copyString(r8, r9);
        goto L_0x0052;
    L_0x00f3:
        r5 = 5;
        if (r8 != r5) goto L_0x0107;
    L_0x00f6:
        r12 = r11.ppm;
        r12 = r12.decodeChar();
        if (r12 != r3) goto L_0x0100;
    L_0x00fe:
        goto L_0x01c3;
    L_0x0100:
        r12 = r12 + 4;
        r11.copyString(r12, r2);
        goto L_0x0052;
    L_0x0107:
        r3 = r11.window;
        r5 = r11.unpPtr;
        r6 = r5 + 1;
        r11.unpPtr = r6;
        r12 = (byte) r12;
        r3[r5] = r12;
        goto L_0x0052;
    L_0x0114:
        r12 = r11.LD;
        r12 = r11.decodeNumber(r12);
        r3 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r12 >= r3) goto L_0x012b;
    L_0x011e:
        r3 = r11.window;
        r5 = r11.unpPtr;
        r6 = r5 + 1;
        r11.unpPtr = r6;
        r12 = (byte) r12;
        r3[r5] = r12;
        goto L_0x0052;
    L_0x012b:
        r8 = 271; // 0x10f float:3.8E-43 double:1.34E-321;
        r9 = 16;
        if (r12 < r8) goto L_0x01b0;
    L_0x0131:
        r3 = LDecode;
        r12 = r12 + -271;
        r3 = r3[r12];
        r3 = r3 + r5;
        r5 = LBits;
        r12 = r5[r12];
        if (r12 <= 0) goto L_0x0149;
    L_0x013e:
        r5 = r11.getbits();
        r6 = 16 - r12;
        r5 = r5 >>> r6;
        r3 = r3 + r5;
        r11.addbits(r12);
    L_0x0149:
        r12 = r11.DD;
        r12 = r11.decodeNumber(r12);
        r5 = r1[r12];
        r5 = r5 + r2;
        r6 = r0[r12];
        if (r6 <= 0) goto L_0x0195;
    L_0x0156:
        r8 = 9;
        if (r12 <= r8) goto L_0x018b;
    L_0x015a:
        if (r6 <= r7) goto L_0x016a;
    L_0x015c:
        r12 = r11.getbits();
        r8 = 20 - r6;
        r12 = r12 >>> r8;
        r12 = r12 << r7;
        r5 = r5 + r12;
        r6 = r6 + -4;
        r11.addbits(r6);
    L_0x016a:
        r12 = r11.lowDistRepCount;
        if (r12 <= 0) goto L_0x0177;
    L_0x016e:
        r12 = r11.lowDistRepCount;
        r12 = r12 - r2;
        r11.lowDistRepCount = r12;
        r12 = r11.prevLowDist;
        r5 = r5 + r12;
        goto L_0x0195;
    L_0x0177:
        r12 = r11.LDD;
        r12 = r11.decodeNumber(r12);
        if (r12 != r9) goto L_0x0187;
    L_0x017f:
        r12 = 15;
        r11.lowDistRepCount = r12;
        r12 = r11.prevLowDist;
        r5 = r5 + r12;
        goto L_0x0195;
    L_0x0187:
        r5 = r5 + r12;
        r11.prevLowDist = r12;
        goto L_0x0195;
    L_0x018b:
        r12 = r11.getbits();
        r9 = r9 - r6;
        r12 = r12 >>> r9;
        r5 = r5 + r12;
        r11.addbits(r6);
    L_0x0195:
        r12 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        if (r5 < r12) goto L_0x01a5;
    L_0x0199:
        r3 = r3 + 1;
        r6 = (long) r5;
        r8 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        r12 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r12 < 0) goto L_0x01a5;
    L_0x01a3:
        r3 = r3 + 1;
    L_0x01a5:
        r11.insertOldDist(r5);
        r11.insertLastMatch(r3, r5);
        r11.copyString(r3, r5);
        goto L_0x0052;
    L_0x01b0:
        if (r12 != r3) goto L_0x01b9;
    L_0x01b2:
        r12 = r11.readEndOfBlock();
        if (r12 != 0) goto L_0x0052;
    L_0x01b8:
        goto L_0x01c3;
    L_0x01b9:
        r3 = 257; // 0x101 float:3.6E-43 double:1.27E-321;
        if (r12 != r3) goto L_0x01c7;
    L_0x01bd:
        r12 = r11.readVMCode();
        if (r12 != 0) goto L_0x0052;
    L_0x01c3:
        r11.UnpWriteBuf();
        return;
    L_0x01c7:
        r3 = 258; // 0x102 float:3.62E-43 double:1.275E-321;
        if (r12 != r3) goto L_0x01d8;
    L_0x01cb:
        r12 = r11.lastLength;
        if (r12 == 0) goto L_0x0052;
    L_0x01cf:
        r12 = r11.lastLength;
        r3 = r11.lastDist;
        r11.copyString(r12, r3);
        goto L_0x0052;
    L_0x01d8:
        r3 = 263; // 0x107 float:3.69E-43 double:1.3E-321;
        if (r12 >= r3) goto L_0x0218;
    L_0x01dc:
        r12 = r12 + -259;
        r3 = r11.oldDist;
        r3 = r3[r12];
    L_0x01e2:
        if (r12 <= 0) goto L_0x01f1;
    L_0x01e4:
        r5 = r11.oldDist;
        r7 = r11.oldDist;
        r8 = r12 + -1;
        r7 = r7[r8];
        r5[r12] = r7;
        r12 = r12 + -1;
        goto L_0x01e2;
    L_0x01f1:
        r12 = r11.oldDist;
        r12[r4] = r3;
        r12 = r11.RD;
        r12 = r11.decodeNumber(r12);
        r5 = LDecode;
        r5 = r5[r12];
        r5 = r5 + r6;
        r6 = LBits;
        r12 = r6[r12];
        if (r12 <= 0) goto L_0x0210;
    L_0x0206:
        r6 = r11.getbits();
        r9 = r9 - r12;
        r6 = r6 >>> r9;
        r5 = r5 + r6;
        r11.addbits(r12);
    L_0x0210:
        r11.insertLastMatch(r5, r3);
        r11.copyString(r5, r3);
        goto L_0x0052;
    L_0x0218:
        r3 = 272; // 0x110 float:3.81E-43 double:1.344E-321;
        if (r12 >= r3) goto L_0x0052;
    L_0x021c:
        r3 = SDDecode;
        r12 = r12 + -263;
        r3 = r3[r12];
        r3 = r3 + r2;
        r5 = SDBits;
        r12 = r5[r12];
        if (r12 <= 0) goto L_0x0233;
    L_0x0229:
        r5 = r11.getbits();
        r9 = r9 - r12;
        r5 = r5 >>> r9;
        r3 = r3 + r5;
        r11.addbits(r12);
    L_0x0233:
        r11.insertOldDist(r3);
        r11.insertLastMatch(r6, r3);
        r11.copyString(r6, r3);
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.Unpack.unpack29(boolean):void");
    }

    private void UnpWriteBuf() throws IOException {
        int i = this.wrPtr;
        int i2 = (this.unpPtr - i) & Compress.MAXWINMASK;
        int i3 = i;
        i = 0;
        while (i < this.prgStack.size()) {
            UnpackFilter unpackFilter = (UnpackFilter) this.prgStack.get(i);
            if (unpackFilter != null) {
                if (unpackFilter.isNextWindow()) {
                    unpackFilter.setNextWindow(false);
                } else {
                    int blockStart = unpackFilter.getBlockStart();
                    int blockLength = unpackFilter.getBlockLength();
                    if (((blockStart - i3) & Compress.MAXWINMASK) >= i2) {
                        continue;
                    } else {
                        if (i3 != blockStart) {
                            UnpWriteArea(i3, blockStart);
                            i2 = (this.unpPtr - blockStart) & Compress.MAXWINMASK;
                            i3 = blockStart;
                        }
                        if (blockLength <= i2) {
                            VMPreparedProgram prg;
                            VMPreparedProgram prg2;
                            int i4;
                            int filteredDataSize;
                            byte[] bArr;
                            int i5;
                            UnpackFilter unpackFilter2;
                            VMPreparedProgram prg3;
                            int i6;
                            byte[] bArr2;
                            i3 = (blockStart + blockLength) & Compress.MAXWINMASK;
                            if (blockStart >= i3) {
                                if (i3 != 0) {
                                    i2 = 4194304 - blockStart;
                                    this.rarVM.setMemory(0, this.window, blockStart, i2);
                                    this.rarVM.setMemory(i2, this.window, 0, i3);
                                    prg = ((UnpackFilter) this.filters.get(unpackFilter.getParentFilter())).getPrg();
                                    prg2 = unpackFilter.getPrg();
                                    if (prg.getGlobalData().size() > 64) {
                                        prg2.getGlobalData().setSize(prg.getGlobalData().size());
                                        for (blockLength = 0; blockLength < prg.getGlobalData().size() - 64; blockLength++) {
                                            i4 = blockLength + 64;
                                            prg2.getGlobalData().set(i4, prg.getGlobalData().get(i4));
                                        }
                                    }
                                    ExecuteCode(prg2);
                                    if (prg2.getGlobalData().size() <= 64) {
                                        if (prg.getGlobalData().size() < prg2.getGlobalData().size()) {
                                            prg.getGlobalData().setSize(prg2.getGlobalData().size());
                                        }
                                        for (blockLength = 0; blockLength < prg2.getGlobalData().size() - 64; blockLength++) {
                                            i4 = blockLength + 64;
                                            prg.getGlobalData().set(i4, prg2.getGlobalData().get(i4));
                                        }
                                    } else {
                                        prg.getGlobalData().clear();
                                    }
                                    i2 = prg2.getFilteredDataOffset();
                                    filteredDataSize = prg2.getFilteredDataSize();
                                    bArr = new byte[filteredDataSize];
                                    for (i5 = 0; i5 < filteredDataSize; i5++) {
                                        bArr[i5] = this.rarVM.getMem()[i2 + i5];
                                    }
                                    this.prgStack.set(i, null);
                                    while (true) {
                                        i2 = i + 1;
                                        if (i2 >= this.prgStack.size()) {
                                            break;
                                        }
                                        unpackFilter2 = (UnpackFilter) this.prgStack.get(i2);
                                        if (unpackFilter2 == null || unpackFilter2.getBlockStart() != blockStart || unpackFilter2.getBlockLength() != filteredDataSize) {
                                            break;
                                        } else if (unpackFilter2.isNextWindow()) {
                                            break;
                                        } else {
                                            this.rarVM.setMemory(0, bArr, 0, filteredDataSize);
                                            prg3 = ((UnpackFilter) this.filters.get(unpackFilter2.getParentFilter())).getPrg();
                                            prg2 = unpackFilter2.getPrg();
                                            if (prg3.getGlobalData().size() > 64) {
                                                prg2.getGlobalData().setSize(prg3.getGlobalData().size());
                                                for (blockLength = 0; blockLength < prg3.getGlobalData().size() - 64; blockLength++) {
                                                    i6 = blockLength + 64;
                                                    prg2.getGlobalData().set(i6, prg3.getGlobalData().get(i6));
                                                }
                                            }
                                            ExecuteCode(prg2);
                                            if (prg2.getGlobalData().size() <= 64) {
                                                if (prg3.getGlobalData().size() < prg2.getGlobalData().size()) {
                                                    prg3.getGlobalData().setSize(prg2.getGlobalData().size());
                                                }
                                                for (blockLength = 0; blockLength < prg2.getGlobalData().size() - 64; blockLength++) {
                                                    i6 = blockLength + 64;
                                                    prg3.getGlobalData().set(i6, prg2.getGlobalData().get(i6));
                                                }
                                            } else {
                                                prg3.getGlobalData().clear();
                                            }
                                            i = prg2.getFilteredDataOffset();
                                            blockLength = prg2.getFilteredDataSize();
                                            bArr2 = new byte[blockLength];
                                            for (i6 = 0; i6 < blockLength; i6++) {
                                                bArr2[i6] = ((Byte) prg2.getGlobalData().get(i + i6)).byteValue();
                                            }
                                            this.prgStack.set(i2, null);
                                            i = i2;
                                            filteredDataSize = blockLength;
                                            bArr = bArr2;
                                        }
                                    }
                                    this.unpIO.unpWrite(bArr, 0, filteredDataSize);
                                    this.unpSomeRead = true;
                                    this.writtenFileSize += (long) filteredDataSize;
                                    i2 = (this.unpPtr - i3) & Compress.MAXWINMASK;
                                }
                            }
                            this.rarVM.setMemory(0, this.window, blockStart, blockLength);
                            prg = ((UnpackFilter) this.filters.get(unpackFilter.getParentFilter())).getPrg();
                            prg2 = unpackFilter.getPrg();
                            if (prg.getGlobalData().size() > 64) {
                                prg2.getGlobalData().setSize(prg.getGlobalData().size());
                                for (blockLength = 0; blockLength < prg.getGlobalData().size() - 64; blockLength++) {
                                    i4 = blockLength + 64;
                                    prg2.getGlobalData().set(i4, prg.getGlobalData().get(i4));
                                }
                            }
                            ExecuteCode(prg2);
                            if (prg2.getGlobalData().size() <= 64) {
                                prg.getGlobalData().clear();
                            } else {
                                if (prg.getGlobalData().size() < prg2.getGlobalData().size()) {
                                    prg.getGlobalData().setSize(prg2.getGlobalData().size());
                                }
                                for (blockLength = 0; blockLength < prg2.getGlobalData().size() - 64; blockLength++) {
                                    i4 = blockLength + 64;
                                    prg.getGlobalData().set(i4, prg2.getGlobalData().get(i4));
                                }
                            }
                            i2 = prg2.getFilteredDataOffset();
                            filteredDataSize = prg2.getFilteredDataSize();
                            bArr = new byte[filteredDataSize];
                            for (i5 = 0; i5 < filteredDataSize; i5++) {
                                bArr[i5] = this.rarVM.getMem()[i2 + i5];
                            }
                            this.prgStack.set(i, null);
                            while (true) {
                                i2 = i + 1;
                                if (i2 >= this.prgStack.size()) {
                                    unpackFilter2 = (UnpackFilter) this.prgStack.get(i2);
                                    if (unpackFilter2.isNextWindow()) {
                                        break;
                                    }
                                    this.rarVM.setMemory(0, bArr, 0, filteredDataSize);
                                    prg3 = ((UnpackFilter) this.filters.get(unpackFilter2.getParentFilter())).getPrg();
                                    prg2 = unpackFilter2.getPrg();
                                    if (prg3.getGlobalData().size() > 64) {
                                        prg2.getGlobalData().setSize(prg3.getGlobalData().size());
                                        for (blockLength = 0; blockLength < prg3.getGlobalData().size() - 64; blockLength++) {
                                            i6 = blockLength + 64;
                                            prg2.getGlobalData().set(i6, prg3.getGlobalData().get(i6));
                                        }
                                    }
                                    ExecuteCode(prg2);
                                    if (prg2.getGlobalData().size() <= 64) {
                                        prg3.getGlobalData().clear();
                                    } else {
                                        if (prg3.getGlobalData().size() < prg2.getGlobalData().size()) {
                                            prg3.getGlobalData().setSize(prg2.getGlobalData().size());
                                        }
                                        for (blockLength = 0; blockLength < prg2.getGlobalData().size() - 64; blockLength++) {
                                            i6 = blockLength + 64;
                                            prg3.getGlobalData().set(i6, prg2.getGlobalData().get(i6));
                                        }
                                    }
                                    i = prg2.getFilteredDataOffset();
                                    blockLength = prg2.getFilteredDataSize();
                                    bArr2 = new byte[blockLength];
                                    for (i6 = 0; i6 < blockLength; i6++) {
                                        bArr2[i6] = ((Byte) prg2.getGlobalData().get(i + i6)).byteValue();
                                    }
                                    this.prgStack.set(i2, null);
                                    i = i2;
                                    filteredDataSize = blockLength;
                                    bArr = bArr2;
                                } else {
                                    break;
                                }
                            }
                            this.unpIO.unpWrite(bArr, 0, filteredDataSize);
                            this.unpSomeRead = true;
                            this.writtenFileSize += (long) filteredDataSize;
                            i2 = (this.unpPtr - i3) & Compress.MAXWINMASK;
                        } else {
                            while (i < this.prgStack.size()) {
                                UnpackFilter unpackFilter3 = (UnpackFilter) this.prgStack.get(i);
                                if (unpackFilter3 != null && unpackFilter3.isNextWindow()) {
                                    unpackFilter3.setNextWindow(false);
                                }
                                i++;
                            }
                            this.wrPtr = i3;
                            return;
                        }
                    }
                }
            }
            i++;
        }
        UnpWriteArea(i3, this.unpPtr);
        this.wrPtr = this.unpPtr;
    }

    private void UnpWriteArea(int i, int i2) throws IOException {
        if (i2 != i) {
            this.unpSomeRead = true;
        }
        if (i2 < i) {
            UnpWriteData(this.window, i, (-i) & Compress.MAXWINMASK);
            UnpWriteData(this.window, 0, i2);
            this.unpAllBuf = true;
            return;
        }
        UnpWriteData(this.window, i, i2 - i);
    }

    private void UnpWriteData(byte[] bArr, int i, int i2) throws IOException {
        if (this.writtenFileSize < this.destUnpSize) {
            long j = this.destUnpSize - this.writtenFileSize;
            long j2 = (long) i2;
            if (j2 > j) {
                i2 = (int) j;
            }
            this.unpIO.unpWrite(bArr, i, i2);
            this.writtenFileSize += j2;
        }
    }

    private void insertOldDist(int i) {
        this.oldDist[3] = this.oldDist[2];
        this.oldDist[2] = this.oldDist[1];
        this.oldDist[1] = this.oldDist[0];
        this.oldDist[0] = i;
    }

    private void insertLastMatch(int i, int i2) {
        this.lastDist = i2;
        this.lastLength = i;
    }

    private void copyString(int i, int i2) {
        int i3 = this.unpPtr - i2;
        if (i3 < 0 || i3 >= 4194044 || this.unpPtr >= 4194044) {
            while (true) {
                i2 = i - 1;
                if (i != 0) {
                    int i4 = i3 + 1;
                    this.window[this.unpPtr] = this.window[i3 & Compress.MAXWINMASK];
                    this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                    i = i2;
                    i3 = i4;
                } else {
                    return;
                }
            }
        }
        i2 = this.window;
        int i5 = this.unpPtr;
        this.unpPtr = i5 + 1;
        i4 = i3 + 1;
        i2[i5] = this.window[i3];
        while (true) {
            i--;
            if (i > 0) {
                i2 = this.window;
                i3 = this.unpPtr;
                this.unpPtr = i3 + 1;
                int i6 = i4 + 1;
                i2[i3] = this.window[i4];
                i4 = i6;
            } else {
                return;
            }
        }
    }

    protected void unpInitData(boolean z) {
        if (!z) {
            this.tablesRead = false;
            Arrays.fill(this.oldDist, 0);
            this.oldDistPtr = 0;
            this.lastDist = 0;
            this.lastLength = 0;
            Arrays.fill(this.unpOldTable, (byte) 0);
            this.unpPtr = 0;
            this.wrPtr = 0;
            this.ppmEscChar = 2;
            initFilters();
        }
        InitBitInput();
        this.ppmError = false;
        this.writtenFileSize = 0;
        this.readTop = 0;
        this.readBorder = 0;
        unpInitData20(z);
    }

    private void initFilters() {
        this.oldFilterLengths.clear();
        this.lastFilter = 0;
        this.filters.clear();
        this.prgStack.clear();
    }

    private boolean readEndOfBlock() throws IOException, RarException {
        Object obj;
        int i = getbits();
        if ((32768 & i) != 0) {
            addbits(1);
            i = 1;
            obj = null;
        } else {
            i = (i & 16384) != 0 ? 1 : 0;
            addbits(2);
            obj = 1;
        }
        this.tablesRead = i ^ 1;
        if (obj != null) {
            return false;
        }
        if (i == 0 || readTables()) {
            return true;
        }
        return false;
    }

    private boolean readTables() throws IOException, RarException {
        byte[] bArr = new byte[20];
        byte[] bArr2 = new byte[Compress.HUFF_TABLE_SIZE];
        int i = 0;
        if (this.inAddr > this.readTop - 25 && !unpReadBuf()) {
            return false;
        }
        faddbits((8 - this.inBit) & 7);
        long fgetbits = (long) (fgetbits() & -1);
        if ((fgetbits & PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID) != 0) {
            this.unpBlockType = BlockTypes.BLOCK_PPM;
            return this.ppm.decodeInit(this, this.ppmEscChar);
        }
        this.unpBlockType = BlockTypes.BLOCK_LZ;
        this.prevLowDist = 0;
        this.lowDistRepCount = 0;
        if ((fgetbits & PlaybackStateCompat.ACTION_PREPARE) == 0) {
            Arrays.fill(this.unpOldTable, (byte) 0);
        }
        faddbits(2);
        int i2 = 0;
        while (i2 < 20) {
            int fgetbits2 = (fgetbits() >>> 12) & 255;
            faddbits(4);
            if (fgetbits2 == 15) {
                fgetbits2 = (fgetbits() >>> 12) & 255;
                faddbits(4);
                if (fgetbits2 == 0) {
                    bArr[i2] = Ascii.SI;
                } else {
                    fgetbits2 += 2;
                    while (true) {
                        int i3 = fgetbits2 - 1;
                        if (fgetbits2 <= 0 || i2 >= bArr.length) {
                            i2--;
                        } else {
                            fgetbits2 = i2 + 1;
                            bArr[i2] = (byte) 0;
                            i2 = fgetbits2;
                            fgetbits2 = i3;
                        }
                    }
                    i2--;
                }
            } else {
                bArr[i2] = (byte) fgetbits2;
            }
            i2++;
        }
        makeDecodeTables(bArr, 0, this.BD, 20);
        int i4 = 0;
        while (i4 < Compress.HUFF_TABLE_SIZE) {
            if (this.inAddr <= this.readTop - 5 || unpReadBuf()) {
                int decodeNumber = decodeNumber(this.BD);
                if (decodeNumber >= 16) {
                    if (decodeNumber >= 18) {
                        if (decodeNumber == 18) {
                            decodeNumber = (fgetbits() >>> 13) + 3;
                            faddbits(3);
                        } else {
                            decodeNumber = (fgetbits() >>> 9) + 11;
                            faddbits(7);
                        }
                        while (true) {
                            i2 = decodeNumber - 1;
                            if (decodeNumber <= 0 || i4 >= Compress.HUFF_TABLE_SIZE) {
                                break;
                            }
                            decodeNumber = i4 + 1;
                            bArr2[i4] = (byte) 0;
                            i4 = decodeNumber;
                            decodeNumber = i2;
                        }
                    } else {
                        if (decodeNumber == 16) {
                            decodeNumber = (fgetbits() >>> 13) + 3;
                            faddbits(3);
                        } else {
                            decodeNumber = (fgetbits() >>> 9) + 11;
                            faddbits(7);
                        }
                        while (true) {
                            i2 = decodeNumber - 1;
                            if (decodeNumber <= 0 || i4 >= Compress.HUFF_TABLE_SIZE) {
                                break;
                            }
                            bArr2[i4] = bArr2[i4 - 1];
                            i4++;
                            decodeNumber = i2;
                        }
                    }
                } else {
                    bArr2[i4] = (byte) ((decodeNumber + this.unpOldTable[i4]) & 15);
                    i4++;
                }
            } else {
                return false;
            }
        }
        this.tablesRead = true;
        if (this.inAddr > this.readTop) {
            return false;
        }
        makeDecodeTables(bArr2, 0, this.LD, Compress.NC);
        makeDecodeTables(bArr2, Compress.NC, this.DD, 60);
        makeDecodeTables(bArr2, 359, this.LDD, 17);
        makeDecodeTables(bArr2, 376, this.RD, 28);
        while (i < this.unpOldTable.length) {
            this.unpOldTable[i] = bArr2[i];
            i++;
        }
        return true;
    }

    private boolean readVMCode() throws IOException, RarException {
        int i = getbits() >> 8;
        addbits(8);
        int i2 = (i & 7) + 1;
        if (i2 == 7) {
            i2 = (getbits() >> 8) + 7;
            addbits(8);
        } else if (i2 == 8) {
            i2 = getbits();
            addbits(16);
        }
        List arrayList = new ArrayList();
        int i3 = 0;
        while (i3 < i2) {
            if (this.inAddr >= this.readTop - 1 && !unpReadBuf() && i3 < i2 - 1) {
                return false;
            }
            arrayList.add(Byte.valueOf((byte) (getbits() >> 8)));
            addbits(8);
            i3++;
        }
        return addVMCode(i, arrayList, i2);
    }

    private boolean readVMCodePPM() throws IOException, RarException {
        int decodeChar = this.ppm.decodeChar();
        if (decodeChar == -1) {
            return false;
        }
        int i = (decodeChar & 7) + 1;
        if (i == 7) {
            i = this.ppm.decodeChar();
            if (i == -1) {
                return false;
            }
            i += 7;
        } else if (i == 8) {
            i = this.ppm.decodeChar();
            if (i == -1) {
                return false;
            }
            int decodeChar2 = this.ppm.decodeChar();
            if (decodeChar2 == -1) {
                return false;
            }
            i = (i * 256) + decodeChar2;
        }
        List arrayList = new ArrayList();
        for (int i2 = 0; i2 < i; i2++) {
            int decodeChar3 = this.ppm.decodeChar();
            if (decodeChar3 == -1) {
                return false;
            }
            arrayList.add(Byte.valueOf((byte) decodeChar3));
        }
        return addVMCode(decodeChar, arrayList, i);
    }

    private boolean addVMCode(int i, List<Byte> list, int i2) {
        int i3;
        i2 = new BitInput();
        i2.InitBitInput();
        for (i3 = 0; i3 < Math.min(32768, list.size()); i3++) {
            i2.getInBuf()[i3] = ((Byte) list.get(i3)).byteValue();
        }
        this.rarVM.init();
        if ((i & 128) != null) {
            list = RarVM.ReadData(i2);
            if (list == null) {
                initFilters();
            } else {
                list--;
            }
        } else {
            list = this.lastFilter;
        }
        if (list <= this.filters.size()) {
            if (list <= this.oldFilterLengths.size()) {
                UnpackFilter unpackFilter;
                int i4;
                this.lastFilter = list;
                Object obj = list == this.filters.size() ? 1 : null;
                UnpackFilter unpackFilter2 = new UnpackFilter();
                if (obj == null) {
                    unpackFilter = (UnpackFilter) this.filters.get(list);
                    unpackFilter2.setParentFilter(list);
                    unpackFilter.setExecCount(unpackFilter.getExecCount() + 1);
                } else if (list > 1024) {
                    return false;
                } else {
                    unpackFilter = new UnpackFilter();
                    this.filters.add(unpackFilter);
                    unpackFilter2.setParentFilter(this.filters.size() - 1);
                    this.oldFilterLengths.add(Integer.valueOf(0));
                    unpackFilter.setExecCount(0);
                }
                this.prgStack.add(unpackFilter2);
                unpackFilter2.setExecCount(unpackFilter.getExecCount());
                int ReadData = RarVM.ReadData(i2);
                if ((i & 64) != 0) {
                    ReadData += CallbackHandler.MSG_ROUTE_REMOVED;
                }
                unpackFilter2.setBlockStart((this.unpPtr + ReadData) & Compress.MAXWINMASK);
                if ((i & 32) != 0) {
                    unpackFilter2.setBlockLength(RarVM.ReadData(i2));
                } else {
                    unpackFilter2.setBlockLength(list < this.oldFilterLengths.size() ? ((Integer) this.oldFilterLengths.get(list)).intValue() : 0);
                }
                boolean z = this.wrPtr != this.unpPtr && ((this.wrPtr - this.unpPtr) & Compress.MAXWINMASK) <= ReadData;
                unpackFilter2.setNextWindow(z);
                this.oldFilterLengths.set(list, Integer.valueOf(unpackFilter2.getBlockLength()));
                Arrays.fill(unpackFilter2.getPrg().getInitR(), 0);
                unpackFilter2.getPrg().getInitR()[3] = 245760;
                unpackFilter2.getPrg().getInitR()[4] = unpackFilter2.getBlockLength();
                unpackFilter2.getPrg().getInitR()[5] = unpackFilter2.getExecCount();
                if ((i & 16) != null) {
                    list = i2.fgetbits() >>> 9;
                    i2.faddbits(7);
                    for (i4 = 0; i4 < 7; i4++) {
                        if (((1 << i4) & list) != 0) {
                            unpackFilter2.getPrg().getInitR()[i4] = RarVM.ReadData(i2);
                        }
                    }
                }
                if (obj != null) {
                    i3 = RarVM.ReadData(i2);
                    if (i3 < 65536) {
                        if (i3 != 0) {
                            byte[] bArr = new byte[i3];
                            for (int i5 = 0; i5 < i3; i5++) {
                                if (i2.Overflow(3)) {
                                    return false;
                                }
                                bArr[i5] = (byte) (i2.fgetbits() >> 8);
                                i2.faddbits(8);
                            }
                            this.rarVM.prepare(bArr, i3, unpackFilter.getPrg());
                        }
                    }
                    return false;
                }
                unpackFilter2.getPrg().setAltCmd(unpackFilter.getPrg().getCmd());
                unpackFilter2.getPrg().setCmdCount(unpackFilter.getPrg().getCmdCount());
                i3 = unpackFilter.getPrg().getStaticData().size();
                if (i3 > 0 && i3 < 8192) {
                    unpackFilter2.getPrg().setStaticData(unpackFilter.getPrg().getStaticData());
                }
                if (unpackFilter2.getPrg().getGlobalData().size() < 64) {
                    unpackFilter2.getPrg().getGlobalData().clear();
                    unpackFilter2.getPrg().getGlobalData().setSize(64);
                }
                Vector globalData = unpackFilter2.getPrg().getGlobalData();
                for (i4 = 0; i4 < 7; i4++) {
                    this.rarVM.setLowEndianValue(globalData, i4 * 4, unpackFilter2.getPrg().getInitR()[i4]);
                }
                this.rarVM.setLowEndianValue(globalData, 28, unpackFilter2.getBlockLength());
                this.rarVM.setLowEndianValue(globalData, 32, 0);
                this.rarVM.setLowEndianValue(globalData, 36, 0);
                this.rarVM.setLowEndianValue(globalData, 40, 0);
                this.rarVM.setLowEndianValue(globalData, 44, unpackFilter2.getExecCount());
                for (ReadData = 0; ReadData < 16; ReadData++) {
                    globalData.set(ReadData + 48, Byte.valueOf((byte) 0));
                }
                if ((i & 8) != 0) {
                    if (i2.Overflow(3) != 0) {
                        return false;
                    }
                    i = RarVM.ReadData(i2);
                    if (i > 8128) {
                        return false;
                    }
                    i3 = unpackFilter2.getPrg().getGlobalData().size();
                    ReadData = i + 64;
                    if (i3 < ReadData) {
                        unpackFilter2.getPrg().getGlobalData().setSize(ReadData - i3);
                    }
                    globalData = unpackFilter2.getPrg().getGlobalData();
                    for (int i6 = 0; i6 < i; i6++) {
                        if (i2.Overflow(3)) {
                            return false;
                        }
                        globalData.set(64 + i6, Byte.valueOf((byte) (i2.fgetbits() >>> 8)));
                        i2.faddbits(8);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void ExecuteCode(VMPreparedProgram vMPreparedProgram) {
        if (vMPreparedProgram.getGlobalData().size() > 0) {
            vMPreparedProgram.getInitR()[6] = (int) this.writtenFileSize;
            this.rarVM.setLowEndianValue(vMPreparedProgram.getGlobalData(), 36, (int) this.writtenFileSize);
            this.rarVM.setLowEndianValue(vMPreparedProgram.getGlobalData(), 40, (int) (this.writtenFileSize >>> 32));
            this.rarVM.execute(vMPreparedProgram);
        }
    }

    public boolean isFileExtracted() {
        return this.fileExtracted;
    }

    public void setDestSize(long j) {
        this.destUnpSize = j;
        this.fileExtracted = 0;
    }

    public void setSuspended(boolean z) {
        this.suspended = z;
    }

    public int getChar() throws IOException, RarException {
        if (this.inAddr > 32738) {
            unpReadBuf();
        }
        byte[] bArr = this.inBuf;
        int i = this.inAddr;
        this.inAddr = i + 1;
        return bArr[i] & 255;
    }

    public int getPpmEscChar() {
        return this.ppmEscChar;
    }

    public void setPpmEscChar(int i) {
        this.ppmEscChar = i;
    }

    public void cleanUp() {
        if (this.ppm != null) {
            SubAllocator subAlloc = this.ppm.getSubAlloc();
            if (subAlloc != null) {
                subAlloc.stopSubAllocator();
            }
        }
    }
}
