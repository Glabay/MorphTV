package ir.mahdi.mzip.rar.unpack.vm;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import ir.mahdi.mzip.rar.crc.RarCRC;
import ir.mahdi.mzip.rar.io.Raw;
import java.util.List;
import java.util.Vector;

public class RarVM extends BitInput {
    private static final long UINT_MASK = -1;
    public static final int VM_FIXEDGLOBALSIZE = 64;
    public static final int VM_GLOBALMEMADDR = 245760;
    public static final int VM_GLOBALMEMSIZE = 8192;
    public static final int VM_MEMMASK = 262143;
    public static final int VM_MEMSIZE = 262144;
    private static final int regCount = 8;
    private int IP;
    /* renamed from: R */
    private int[] f59R = new int[8];
    private int codeSize;
    private int flags;
    private int maxOpCount = 25000000;
    private byte[] mem = null;

    public static int ReadData(BitInput bitInput) {
        int fgetbits = bitInput.fgetbits();
        int i = 49152 & fgetbits;
        if (i == 0) {
            bitInput.faddbits(6);
            return (fgetbits >> 10) & 15;
        } else if (i == 16384) {
            if ((fgetbits & 15360) == 0) {
                fgetbits = ((fgetbits >> 2) & 255) | InputDeviceCompat.SOURCE_ANY;
                bitInput.faddbits(14);
            } else {
                fgetbits = (fgetbits >> 6) & 255;
                bitInput.faddbits(10);
            }
            return fgetbits;
        } else if (i != 32768) {
            bitInput.faddbits(2);
            fgetbits = bitInput.fgetbits() << 16;
            bitInput.faddbits(16);
            fgetbits |= bitInput.fgetbits();
            bitInput.faddbits(16);
            return fgetbits;
        } else {
            bitInput.faddbits(2);
            fgetbits = bitInput.fgetbits();
            bitInput.faddbits(16);
            return fgetbits;
        }
    }

    public void init() {
        if (this.mem == null) {
            this.mem = new byte[262148];
        }
    }

    private boolean isVMMem(byte[] bArr) {
        return this.mem == bArr ? 1 : null;
    }

    private int getValue(boolean z, byte[] bArr, int i) {
        if (z) {
            if (isVMMem(bArr)) {
                return bArr[i];
            }
            return bArr[i] & 255;
        } else if (isVMMem(bArr)) {
            return Raw.readIntLittleEndian(bArr, i);
        } else {
            return Raw.readIntBigEndian(bArr, i);
        }
    }

    private void setValue(boolean z, byte[] bArr, int i, int i2) {
        if (z) {
            if (isVMMem(bArr)) {
                bArr[i] = (byte) i2;
            } else {
                bArr[i] = (byte) ((bArr[i] & 0) | ((byte) (i2 & 255)));
            }
        } else if (isVMMem(bArr)) {
            Raw.writeIntLittleEndian(bArr, i, i2);
        } else {
            Raw.writeIntBigEndian(bArr, i, i2);
        }
    }

    public void setLowEndianValue(byte[] bArr, int i, int i2) {
        Raw.writeIntLittleEndian(bArr, i, i2);
    }

    public void setLowEndianValue(Vector<Byte> vector, int i, int i2) {
        vector.set(i + 0, Byte.valueOf((byte) (i2 & 255)));
        vector.set(i + 1, Byte.valueOf((byte) ((i2 >>> 8) & 255)));
        vector.set(i + 2, Byte.valueOf((byte) ((i2 >>> 16) & 255)));
        vector.set(i + 3, Byte.valueOf((byte) ((i2 >>> 24) & 255)));
    }

    private int getOperand(VMPreparedOperand vMPreparedOperand) {
        if (vMPreparedOperand.getType() == VMOpType.VM_OPREGMEM) {
            return Raw.readIntLittleEndian(this.mem, 262143 & (vMPreparedOperand.getOffset() + vMPreparedOperand.getBase()));
        }
        return Raw.readIntLittleEndian(this.mem, vMPreparedOperand.getOffset());
    }

    public void execute(VMPreparedProgram vMPreparedProgram) {
        int i;
        List altCmd;
        int i2 = 0;
        for (i = 0; i < vMPreparedProgram.getInitR().length; i++) {
            this.f59R[i] = vMPreparedProgram.getInitR()[i];
        }
        long min = (long) (Math.min(vMPreparedProgram.getGlobalData().size(), 8192) & -1);
        if (min != 0) {
            for (int i3 = 0; ((long) i3) < min; i3++) {
                this.mem[i3 + VM_GLOBALMEMADDR] = ((Byte) vMPreparedProgram.getGlobalData().get(i3)).byteValue();
            }
        }
        long min2 = Math.min((long) vMPreparedProgram.getStaticData().size(), PlaybackStateCompat.ACTION_PLAY_FROM_URI - min) & -1;
        if (min2 != 0) {
            for (int i4 = 0; ((long) i4) < min2; i4++) {
                this.mem[(((int) min) + VM_GLOBALMEMADDR) + i4] = ((Byte) vMPreparedProgram.getStaticData().get(i4)).byteValue();
            }
        }
        this.f59R[7] = 262144;
        this.flags = 0;
        if (vMPreparedProgram.getAltCmd().size() != 0) {
            altCmd = vMPreparedProgram.getAltCmd();
        } else {
            altCmd = vMPreparedProgram.getCmd();
        }
        if (!ExecuteCode(altCmd, vMPreparedProgram.getCmdCount())) {
            ((VMPreparedCommand) altCmd.get(0)).setOpCode(VMCommands.VM_RET);
        }
        i = getValue(false, this.mem, 245792) & VM_MEMMASK;
        int value = VM_MEMMASK & getValue(false, this.mem, 245788);
        if (i + value >= 262144) {
            i = 0;
            value = 0;
        }
        vMPreparedProgram.setFilteredDataOffset(i);
        vMPreparedProgram.setFilteredDataSize(value);
        vMPreparedProgram.getGlobalData().clear();
        i = Math.min(getValue(false, this.mem, 245808), 8128);
        if (i != 0) {
            i += 64;
            vMPreparedProgram.getGlobalData().setSize(i);
            while (i2 < i) {
                vMPreparedProgram.getGlobalData().set(i2, Byte.valueOf(this.mem[i2 + VM_GLOBALMEMADDR]));
                i2++;
            }
        }
    }

    public byte[] getMem() {
        return this.mem;
    }

    private boolean setIP(int i) {
        if (i >= this.codeSize) {
            return true;
        }
        int i2 = this.maxOpCount - 1;
        this.maxOpCount = i2;
        if (i2 <= 0) {
            return false;
        }
        this.IP = i;
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean ExecuteCode(java.util.List<ir.mahdi.mzip.rar.unpack.vm.VMPreparedCommand> r19, int r20) {
        /*
        r18 = this;
        r0 = r18;
        r1 = 25000000; // 0x17d7840 float:4.6555036E-38 double:1.2351641E-316;
        r0.maxOpCount = r1;
        r1 = r20;
        r0.codeSize = r1;
        r1 = 0;
        r0.IP = r1;
    L_0x000e:
        r2 = r0.IP;
        r3 = r19;
        r2 = r3.get(r2);
        r2 = (ir.mahdi.mzip.rar.unpack.vm.VMPreparedCommand) r2;
        r4 = r2.getOp1();
        r4 = r0.getOperand(r4);
        r5 = r2.getOp2();
        r5 = r0.getOperand(r5);
        r6 = ir.mahdi.mzip.rar.unpack.vm.RarVM.C13981.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands;
        r7 = r2.getOpCode();
        r7 = r7.ordinal();
        r6 = r6[r7];
        r7 = 8;
        r10 = -2;
        r12 = 262143; // 0x3ffff float:3.6734E-40 double:1.29516E-318;
        r13 = 7;
        r14 = -1;
        r8 = 1;
        switch(r6) {
            case 1: goto L_0x0778;
            case 2: goto L_0x076c;
            case 3: goto L_0x0760;
            case 4: goto L_0x0730;
            case 5: goto L_0x0708;
            case 6: goto L_0x06de;
            case 7: goto L_0x067c;
            case 8: goto L_0x0660;
            case 9: goto L_0x0644;
            case 10: goto L_0x0605;
            case 11: goto L_0x05e9;
            case 12: goto L_0x05cd;
            case 13: goto L_0x05b7;
            case 14: goto L_0x05a1;
            case 15: goto L_0x056c;
            case 16: goto L_0x0559;
            case 17: goto L_0x0546;
            case 18: goto L_0x051b;
            case 19: goto L_0x050a;
            case 20: goto L_0x04f9;
            case 21: goto L_0x04ee;
            case 22: goto L_0x04bc;
            case 23: goto L_0x048a;
            case 24: goto L_0x0458;
            case 25: goto L_0x042f;
            case 26: goto L_0x0419;
            case 27: goto L_0x0403;
            case 28: goto L_0x03ed;
            case 29: goto L_0x03d0;
            case 30: goto L_0x03b3;
            case 31: goto L_0x039d;
            case 32: goto L_0x0383;
            case 33: goto L_0x0369;
            case 34: goto L_0x0349;
            case 35: goto L_0x0332;
            case 36: goto L_0x02ed;
            case 37: goto L_0x02ae;
            case 38: goto L_0x026f;
            case 39: goto L_0x0240;
            case 40: goto L_0x0232;
            case 41: goto L_0x0224;
            case 42: goto L_0x0200;
            case 43: goto L_0x01e5;
            case 44: goto L_0x01cf;
            case 45: goto L_0x01b8;
            case 46: goto L_0x01ab;
            case 47: goto L_0x019d;
            case 48: goto L_0x0175;
            case 49: goto L_0x014b;
            case 50: goto L_0x0129;
            case 51: goto L_0x00cf;
            case 52: goto L_0x0075;
            case 53: goto L_0x0055;
            case 54: goto L_0x0044;
            default: goto L_0x0042;
        };
    L_0x0042:
        goto L_0x078b;
    L_0x0044:
        r2 = r2.getOp1();
        r2 = r2.getData();
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMStandardFilters.findFilter(r2);
        r0.ExecuteStandardFilter(r2);
        goto L_0x078b;
    L_0x0055:
        r2 = r0.f59R;
        r2 = r2[r13];
        r4 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r2 < r4) goto L_0x005e;
    L_0x005d:
        return r8;
    L_0x005e:
        r2 = r0.mem;
        r4 = r0.f59R;
        r4 = r4[r13];
        r4 = r4 & r12;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        r2 = r0.f59R;
        r4 = r2[r13];
        r4 = r4 + 4;
        r2[r13] = r4;
        goto L_0x000e;
    L_0x0075:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r0.flags;
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r9 = r9.getFlag();
        r7 = r7 & r9;
        r9 = (long) r6;
        r11 = r2.isByteMode();
        r12 = r0.mem;
        r5 = r0.getValue(r11, r12, r5);
        r11 = (long) r5;
        r16 = r14 - r11;
        r11 = r9 & r16;
        r9 = (long) r7;
        r16 = r14 - r9;
        r9 = r11 & r16;
        r11 = r9 & r14;
        r5 = (int) r11;
        r9 = r2.isByteMode();
        if (r9 == 0) goto L_0x00a8;
    L_0x00a6:
        r5 = r5 & 255;
    L_0x00a8:
        if (r5 > r6) goto L_0x00c1;
    L_0x00aa:
        if (r5 != r6) goto L_0x00af;
    L_0x00ac:
        if (r7 == 0) goto L_0x00af;
    L_0x00ae:
        goto L_0x00c1;
    L_0x00af:
        if (r5 != 0) goto L_0x00b8;
    L_0x00b1:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x00bf;
    L_0x00b8:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x00bf:
        r6 = r6 | r1;
        goto L_0x00c2;
    L_0x00c1:
        r6 = 1;
    L_0x00c2:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x00cf:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r0.flags;
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r9 = r9.getFlag();
        r7 = r7 & r9;
        r9 = (long) r6;
        r11 = r2.isByteMode();
        r12 = r0.mem;
        r5 = r0.getValue(r11, r12, r5);
        r11 = (long) r5;
        r16 = r11 + r14;
        r11 = r9 & r16;
        r9 = (long) r7;
        r16 = r9 + r14;
        r9 = r11 & r16;
        r11 = r9 & r14;
        r5 = (int) r11;
        r9 = r2.isByteMode();
        if (r9 == 0) goto L_0x0102;
    L_0x0100:
        r5 = r5 & 255;
    L_0x0102:
        if (r5 < r6) goto L_0x011b;
    L_0x0104:
        if (r5 != r6) goto L_0x0109;
    L_0x0106:
        if (r7 == 0) goto L_0x0109;
    L_0x0108:
        goto L_0x011b;
    L_0x0109:
        if (r5 != 0) goto L_0x0112;
    L_0x010b:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x0119;
    L_0x0112:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x0119:
        r6 = r6 | r1;
        goto L_0x011c;
    L_0x011b:
        r6 = 1;
    L_0x011c:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x0129:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r5 = r0.getValue(r6, r7, r5);
        if (r5 == 0) goto L_0x078b;
    L_0x0135:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r6 = r6 / r5;
        r2 = r2.isByteMode();
        r5 = r0.mem;
        r0.setValue(r2, r5, r4, r6);
        goto L_0x078b;
    L_0x014b:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r6 = (long) r6;
        r9 = r2.isByteMode();
        r10 = r0.mem;
        r5 = r0.getValue(r9, r10, r5);
        r9 = (long) r5;
        r9 = r9 * r14;
        r11 = r6 & r9;
        r5 = r11 & r14;
        r9 = r5 & r14;
        r5 = (int) r9;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x0175:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r10 = r2.isByteMode();
        r11 = r0.mem;
        r10 = r0.getValue(r10, r11, r5);
        r0.setValue(r7, r9, r4, r10);
        r2 = r2.isByteMode();
        r4 = r0.mem;
        r0.setValue(r2, r4, r5, r6);
        goto L_0x078b;
    L_0x019d:
        r2 = r0.mem;
        r6 = r0.mem;
        r5 = r0.getValue(r8, r6, r5);
        r5 = (byte) r5;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x01ab:
        r2 = r0.mem;
        r6 = r0.mem;
        r5 = r0.getValue(r8, r6, r5);
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x01b8:
        r2 = r0.mem;
        r4 = r0.f59R;
        r4 = r4[r13];
        r4 = r4 & r12;
        r2 = r0.getValue(r1, r2, r4);
        r0.flags = r2;
        r2 = r0.f59R;
        r4 = r2[r13];
        r4 = r4 + 4;
        r2[r13] = r4;
        goto L_0x078b;
    L_0x01cf:
        r2 = r0.f59R;
        r4 = r2[r13];
        r4 = r4 + -4;
        r2[r13] = r4;
        r2 = r0.mem;
        r4 = r0.f59R;
        r4 = r4[r13];
        r4 = r4 & r12;
        r5 = r0.flags;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x01e5:
        r2 = r0.f59R;
        r2 = r2[r13];
        r4 = r2;
        r2 = 0;
    L_0x01eb:
        if (r2 >= r7) goto L_0x078b;
    L_0x01ed:
        r5 = r0.f59R;
        r6 = 7 - r2;
        r9 = r0.mem;
        r10 = r4 & r12;
        r9 = r0.getValue(r1, r9, r10);
        r5[r6] = r9;
        r2 = r2 + 1;
        r4 = r4 + 4;
        goto L_0x01eb;
    L_0x0200:
        r2 = r0.f59R;
        r2 = r2[r13];
        r2 = r2 + -4;
        r4 = r2;
        r2 = 0;
    L_0x0208:
        if (r2 >= r7) goto L_0x021a;
    L_0x020a:
        r5 = r0.mem;
        r6 = r4 & r12;
        r9 = r0.f59R;
        r9 = r9[r2];
        r0.setValue(r1, r5, r6, r9);
        r2 = r2 + 1;
        r4 = r4 + -4;
        goto L_0x0208;
    L_0x021a:
        r2 = r0.f59R;
        r4 = r2[r13];
        r4 = r4 + -32;
        r2[r13] = r4;
        goto L_0x078b;
    L_0x0224:
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r1, r5, r4);
        r5 = -r5;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x0232:
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r8, r5, r4);
        r5 = -r5;
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x0240:
        r5 = r2.isByteMode();
        r6 = r0.mem;
        r5 = r0.getValue(r5, r6, r4);
        r5 = -r5;
        if (r5 != 0) goto L_0x0254;
    L_0x024d:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x0262;
    L_0x0254:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r6 = r6.getFlag();
        r7 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r7 = r7.getFlag();
        r7 = r7 & r5;
        r6 = r6 | r7;
    L_0x0262:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x026f:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r7 = r6 >> r5;
        if (r7 != 0) goto L_0x028e;
    L_0x0287:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r9 = r9.getFlag();
        goto L_0x0295;
    L_0x028e:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r9 = r9.getFlag();
        r9 = r9 & r7;
    L_0x0295:
        r5 = r5 + -1;
        r5 = r6 >> r5;
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r6 = r6.getFlag();
        r5 = r5 & r6;
        r5 = r5 | r9;
        r0.flags = r5;
        r2 = r2.isByteMode();
        r5 = r0.mem;
        r0.setValue(r2, r5, r4, r7);
        goto L_0x078b;
    L_0x02ae:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r7 = r6 >>> r5;
        if (r7 != 0) goto L_0x02cd;
    L_0x02c6:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r9 = r9.getFlag();
        goto L_0x02d4;
    L_0x02cd:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r9 = r9.getFlag();
        r9 = r9 & r7;
    L_0x02d4:
        r5 = r5 + -1;
        r5 = r6 >>> r5;
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r6 = r6.getFlag();
        r5 = r5 & r6;
        r5 = r5 | r9;
        r0.flags = r5;
        r2 = r2.isByteMode();
        r5 = r0.mem;
        r0.setValue(r2, r5, r4, r7);
        goto L_0x078b;
    L_0x02ed:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r7 = r6 << r5;
        if (r7 != 0) goto L_0x030c;
    L_0x0305:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r9 = r9.getFlag();
        goto L_0x0313;
    L_0x030c:
        r9 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r9 = r9.getFlag();
        r9 = r9 & r7;
    L_0x0313:
        r5 = r5 + -1;
        r5 = r6 << r5;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = r5 & r6;
        if (r5 == 0) goto L_0x0323;
    L_0x031c:
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r5 = r5.getFlag();
        goto L_0x0324;
    L_0x0323:
        r5 = 0;
    L_0x0324:
        r5 = r5 | r9;
        r0.flags = r5;
        r2 = r2.isByteMode();
        r5 = r0.mem;
        r0.setValue(r2, r5, r4, r7);
        goto L_0x078b;
    L_0x0332:
        r5 = r2.isByteMode();
        r6 = r0.mem;
        r2 = r2.isByteMode();
        r7 = r0.mem;
        r2 = r0.getValue(r2, r7, r4);
        r2 = r2 ^ -1;
        r0.setValue(r5, r6, r4, r2);
        goto L_0x078b;
    L_0x0349:
        r2 = r0.f59R;
        r5 = r2[r13];
        r5 = r5 + -4;
        r2[r13] = r5;
        r2 = r0.mem;
        r5 = r0.f59R;
        r5 = r5[r13];
        r5 = r5 & r12;
        r6 = r0.IP;
        r6 = r6 + r8;
        r0.setValue(r1, r2, r5, r6);
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x0369:
        r2 = r0.mem;
        r5 = r0.mem;
        r6 = r0.f59R;
        r6 = r6[r13];
        r6 = r6 & r12;
        r5 = r0.getValue(r1, r5, r6);
        r0.setValue(r1, r2, r4, r5);
        r2 = r0.f59R;
        r4 = r2[r13];
        r4 = r4 + 4;
        r2[r13] = r4;
        goto L_0x078b;
    L_0x0383:
        r2 = r0.f59R;
        r5 = r2[r13];
        r5 = r5 + -4;
        r2[r13] = r5;
        r2 = r0.mem;
        r5 = r0.f59R;
        r5 = r5[r13];
        r5 = r5 & r12;
        r6 = r0.mem;
        r4 = r0.getValue(r1, r6, r4);
        r0.setValue(r1, r2, r5, r4);
        goto L_0x078b;
    L_0x039d:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 != 0) goto L_0x078b;
    L_0x03a8:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x03b3:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r5 = r5.getFlag();
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        r5 = r5 | r6;
        r2 = r2 & r5;
        if (r2 != 0) goto L_0x078b;
    L_0x03c5:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x03d0:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r5 = r5.getFlag();
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        r5 = r5 | r6;
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x078b;
    L_0x03e2:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x03ed:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FC;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x078b;
    L_0x03f8:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x0403:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 != 0) goto L_0x078b;
    L_0x040e:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x0419:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x078b;
    L_0x0424:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x042f:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r4 = r0.getValue(r6, r7, r4);
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r2 = r0.getValue(r2, r6, r5);
        r2 = r2 & r4;
        if (r2 != 0) goto L_0x044d;
    L_0x0446:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        goto L_0x0454;
    L_0x044d:
        r4 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r4 = r4.getFlag();
        r2 = r2 & r4;
    L_0x0454:
        r0.flags = r2;
        goto L_0x078b;
    L_0x0458:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r5 = r5 | r6;
        if (r5 != 0) goto L_0x0476;
    L_0x046f:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x047d;
    L_0x0476:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x047d:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x048a:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r5 = r5 & r6;
        if (r5 != 0) goto L_0x04a8;
    L_0x04a1:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x04af;
    L_0x04a8:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x04af:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x04bc:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r7 = r2.isByteMode();
        r9 = r0.mem;
        r5 = r0.getValue(r7, r9, r5);
        r5 = r5 ^ r6;
        if (r5 != 0) goto L_0x04da;
    L_0x04d3:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x04e1;
    L_0x04da:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x04e1:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x04ee:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x04f9:
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r1, r5, r4);
        r5 = (long) r5;
        r12 = r5 & r10;
        r5 = (int) r12;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x050a:
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r8, r5, r4);
        r5 = (long) r5;
        r12 = r5 & r10;
        r5 = (int) r12;
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x051b:
        r5 = r2.isByteMode();
        r6 = r0.mem;
        r5 = r0.getValue(r5, r6, r4);
        r5 = (long) r5;
        r12 = r5 & r10;
        r5 = (int) r12;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        if (r5 != 0) goto L_0x053b;
    L_0x0534:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        goto L_0x0542;
    L_0x053b:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r2 = r2.getFlag();
        r2 = r2 & r5;
    L_0x0542:
        r0.flags = r2;
        goto L_0x078b;
    L_0x0546:
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r1, r5, r4);
        r5 = (long) r5;
        r9 = 0;
        r11 = r5 & r9;
        r5 = (int) r11;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x0559:
        r9 = 0;
        r2 = r0.mem;
        r5 = r0.mem;
        r5 = r0.getValue(r8, r5, r4);
        r5 = (long) r5;
        r11 = r5 & r9;
        r5 = (int) r11;
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x056c:
        r9 = 0;
        r5 = r2.isByteMode();
        r6 = r0.mem;
        r5 = r0.getValue(r5, r6, r4);
        r5 = (long) r5;
        r11 = r5 & r9;
        r5 = (int) r11;
        r6 = r2.isByteMode();
        if (r6 == 0) goto L_0x0584;
    L_0x0582:
        r5 = r5 & 255;
    L_0x0584:
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        if (r5 != 0) goto L_0x0596;
    L_0x058f:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        goto L_0x059d;
    L_0x0596:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r2 = r2.getFlag();
        r2 = r2 & r5;
    L_0x059d:
        r0.flags = r2;
        goto L_0x078b;
    L_0x05a1:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 != 0) goto L_0x078b;
    L_0x05ac:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x05b7:
        r2 = r0.flags;
        r5 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r5 = r5.getFlag();
        r2 = r2 & r5;
        if (r2 == 0) goto L_0x078b;
    L_0x05c2:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r0.setIP(r2);
        goto L_0x000e;
    L_0x05cd:
        r2 = r0.mem;
        r6 = r0.mem;
        r6 = r0.getValue(r1, r6, r4);
        r6 = (long) r6;
        r9 = r0.mem;
        r5 = r0.getValue(r1, r9, r5);
        r9 = (long) r5;
        r11 = r14 - r9;
        r9 = r6 & r11;
        r5 = r9 & r14;
        r5 = (int) r5;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x05e9:
        r2 = r0.mem;
        r6 = r0.mem;
        r6 = r0.getValue(r8, r6, r4);
        r6 = (long) r6;
        r9 = r0.mem;
        r5 = r0.getValue(r8, r9, r5);
        r9 = (long) r5;
        r11 = r14 - r9;
        r9 = r6 & r11;
        r5 = r9 & r14;
        r5 = (int) r5;
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x0605:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r9 = (long) r6;
        r7 = r2.isByteMode();
        r11 = r0.mem;
        r5 = r0.getValue(r7, r11, r5);
        r11 = (long) r5;
        r16 = r14 - r11;
        r11 = r9 & r16;
        r9 = r11 & r14;
        r5 = (int) r9;
        if (r5 != 0) goto L_0x062b;
    L_0x0624:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x0637;
    L_0x062b:
        if (r5 <= r6) goto L_0x062f;
    L_0x062d:
        r6 = 1;
        goto L_0x0637;
    L_0x062f:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
        r6 = r6 | r1;
    L_0x0637:
        r0.flags = r6;
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x0644:
        r2 = r0.mem;
        r6 = r0.mem;
        r6 = r0.getValue(r1, r6, r4);
        r6 = (long) r6;
        r9 = r0.mem;
        r5 = r0.getValue(r1, r9, r5);
        r9 = (long) r5;
        r11 = r9 + r14;
        r9 = r6 & r11;
        r5 = r9 & r14;
        r5 = (int) r5;
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x0660:
        r2 = r0.mem;
        r6 = r0.mem;
        r6 = r0.getValue(r8, r6, r4);
        r6 = (long) r6;
        r9 = r0.mem;
        r5 = r0.getValue(r8, r9, r5);
        r9 = (long) r5;
        r11 = r9 + r14;
        r9 = r6 & r11;
        r5 = r9 & r14;
        r5 = (int) r5;
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x067c:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r6 = r0.getValue(r6, r7, r4);
        r9 = (long) r6;
        r7 = r2.isByteMode();
        r11 = r0.mem;
        r5 = r0.getValue(r7, r11, r5);
        r11 = (long) r5;
        r16 = r9 + r11;
        r9 = r16 & r14;
        r5 = (int) r9;
        r7 = r2.isByteMode();
        if (r7 == 0) goto L_0x06bc;
    L_0x069d:
        r5 = r5 & 255;
        if (r5 >= r6) goto L_0x06a3;
    L_0x06a1:
        r6 = 1;
        goto L_0x06b9;
    L_0x06a3:
        if (r5 != 0) goto L_0x06ac;
    L_0x06a5:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x06b8;
    L_0x06ac:
        r6 = r5 & 128;
        if (r6 == 0) goto L_0x06b7;
    L_0x06b0:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        goto L_0x06b8;
    L_0x06b7:
        r6 = 0;
    L_0x06b8:
        r6 = r6 | r1;
    L_0x06b9:
        r0.flags = r6;
        goto L_0x06d3;
    L_0x06bc:
        if (r5 >= r6) goto L_0x06c0;
    L_0x06be:
        r6 = 1;
        goto L_0x06d1;
    L_0x06c0:
        if (r5 != 0) goto L_0x06c9;
    L_0x06c2:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r6 = r6.getFlag();
        goto L_0x06d0;
    L_0x06c9:
        r6 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r6 = r6.getFlag();
        r6 = r6 & r5;
    L_0x06d0:
        r6 = r6 | r1;
    L_0x06d1:
        r0.flags = r6;
    L_0x06d3:
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r0.setValue(r2, r6, r4, r5);
        goto L_0x078b;
    L_0x06de:
        r2 = r0.mem;
        r2 = r0.getValue(r1, r2, r4);
        r4 = r0.mem;
        r4 = r0.getValue(r1, r4, r5);
        r4 = r2 - r4;
        if (r4 != 0) goto L_0x06f8;
    L_0x06ee:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        r0.flags = r2;
        goto L_0x078b;
    L_0x06f8:
        if (r4 <= r2) goto L_0x06fc;
    L_0x06fa:
        r2 = 1;
        goto L_0x0704;
    L_0x06fc:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r2 = r2.getFlag();
        r2 = r2 & r4;
        r2 = r2 | r1;
    L_0x0704:
        r0.flags = r2;
        goto L_0x078b;
    L_0x0708:
        r2 = r0.mem;
        r2 = r0.getValue(r8, r2, r4);
        r4 = r0.mem;
        r4 = r0.getValue(r8, r4, r5);
        r4 = r2 - r4;
        if (r4 != 0) goto L_0x0721;
    L_0x0718:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        r0.flags = r2;
        goto L_0x078b;
    L_0x0721:
        if (r4 <= r2) goto L_0x0725;
    L_0x0723:
        r2 = 1;
        goto L_0x072d;
    L_0x0725:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r2 = r2.getFlag();
        r2 = r2 & r4;
        r2 = r2 | r1;
    L_0x072d:
        r0.flags = r2;
        goto L_0x078b;
    L_0x0730:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r4 = r0.getValue(r6, r7, r4);
        r2 = r2.isByteMode();
        r6 = r0.mem;
        r2 = r0.getValue(r2, r6, r5);
        r2 = r4 - r2;
        if (r2 != 0) goto L_0x0751;
    L_0x0748:
        r2 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FZ;
        r2 = r2.getFlag();
        r0.flags = r2;
        goto L_0x078b;
    L_0x0751:
        if (r2 <= r4) goto L_0x0755;
    L_0x0753:
        r2 = 1;
        goto L_0x075d;
    L_0x0755:
        r4 = ir.mahdi.mzip.rar.unpack.vm.VMFlags.VM_FS;
        r4 = r4.getFlag();
        r2 = r2 & r4;
        r2 = r2 | r1;
    L_0x075d:
        r0.flags = r2;
        goto L_0x078b;
    L_0x0760:
        r2 = r0.mem;
        r6 = r0.mem;
        r5 = r0.getValue(r1, r6, r5);
        r0.setValue(r1, r2, r4, r5);
        goto L_0x078b;
    L_0x076c:
        r2 = r0.mem;
        r6 = r0.mem;
        r5 = r0.getValue(r8, r6, r5);
        r0.setValue(r8, r2, r4, r5);
        goto L_0x078b;
    L_0x0778:
        r6 = r2.isByteMode();
        r7 = r0.mem;
        r2 = r2.isByteMode();
        r9 = r0.mem;
        r2 = r0.getValue(r2, r9, r5);
        r0.setValue(r6, r7, r4, r2);
    L_0x078b:
        r2 = r0.IP;
        r2 = r2 + r8;
        r0.IP = r2;
        r2 = r0.maxOpCount;
        r2 = r2 - r8;
        r0.maxOpCount = r2;
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: ir.mahdi.mzip.rar.unpack.vm.RarVM.ExecuteCode(java.util.List, int):boolean");
    }

    public void prepare(byte[] bArr, int i, VMPreparedProgram vMPreparedProgram) {
        int i2;
        InitBitInput();
        int min = Math.min(32768, i);
        for (i2 = 0; i2 < min; i2++) {
            byte[] bArr2 = this.inBuf;
            bArr2[i2] = (byte) (bArr2[i2] | bArr[i2]);
        }
        byte b = (byte) 0;
        for (i2 = 1; i2 < i; i2++) {
            b = (byte) (b ^ bArr[i2]);
        }
        faddbits(8);
        vMPreparedProgram.setCmdCount(0);
        if (b == bArr[0]) {
            bArr = IsStandardFilter(bArr, i);
            if (bArr != VMStandardFilters.VMSF_NONE) {
                i = new VMPreparedCommand();
                i.setOpCode(VMCommands.VM_STANDARD);
                i.getOp1().setData(bArr.getFilter());
                i.getOp1().setType(VMOpType.VM_OPNONE);
                i.getOp2().setType(VMOpType.VM_OPNONE);
                vMPreparedProgram.getCmd().add(i);
                vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
                i = 0;
            }
            bArr = fgetbits();
            faddbits(1);
            if ((bArr & 32768) != null) {
                long ReadData = ((long) ReadData(this)) & 0;
                bArr = null;
                while (this.inAddr < i && ((long) bArr) < ReadData) {
                    vMPreparedProgram.getStaticData().add(Byte.valueOf((byte) (fgetbits() >> 8)));
                    faddbits(8);
                    bArr++;
                }
            }
            while (this.inAddr < i) {
                bArr = new VMPreparedCommand();
                int fgetbits = fgetbits();
                if ((fgetbits & 32768) == 0) {
                    bArr.setOpCode(VMCommands.findVMCommand(fgetbits >> 12));
                    faddbits(4);
                } else {
                    bArr.setOpCode(VMCommands.findVMCommand((fgetbits >> 10) - 24));
                    faddbits(6);
                }
                if ((VMCmdFlags.VM_CmdFlags[bArr.getOpCode().getVMCommand()] & 4) != 0) {
                    bArr.setByteMode((fgetbits() >> 15) == 1);
                    faddbits(1);
                } else {
                    bArr.setByteMode(false);
                }
                bArr.getOp1().setType(VMOpType.VM_OPNONE);
                bArr.getOp2().setType(VMOpType.VM_OPNONE);
                fgetbits = VMCmdFlags.VM_CmdFlags[bArr.getOpCode().getVMCommand()] & 3;
                if (fgetbits > 0) {
                    decodeArg(bArr.getOp1(), bArr.isByteMode());
                    if (fgetbits == 2) {
                        decodeArg(bArr.getOp2(), bArr.isByteMode());
                    } else if (bArr.getOp1().getType() == VMOpType.VM_OPINT && (VMCmdFlags.VM_CmdFlags[bArr.getOpCode().getVMCommand()] & 24) != 0) {
                        fgetbits = bArr.getOp1().getData();
                        if (fgetbits >= 256) {
                            fgetbits += InputDeviceCompat.SOURCE_ANY;
                        } else {
                            if (fgetbits >= 136) {
                                fgetbits -= 264;
                            } else if (fgetbits >= 16) {
                                fgetbits -= 8;
                            } else if (fgetbits >= 8) {
                                fgetbits -= 16;
                            }
                            fgetbits += vMPreparedProgram.getCmdCount();
                        }
                        bArr.getOp1().setData(fgetbits);
                    }
                }
                vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
                vMPreparedProgram.getCmd().add(bArr);
            }
        }
        bArr = new VMPreparedCommand();
        bArr.setOpCode(VMCommands.VM_RET);
        bArr.getOp1().setType(VMOpType.VM_OPNONE);
        bArr.getOp2().setType(VMOpType.VM_OPNONE);
        vMPreparedProgram.getCmd().add(bArr);
        vMPreparedProgram.setCmdCount(vMPreparedProgram.getCmdCount() + 1);
        if (i != 0) {
            optimize(vMPreparedProgram);
        }
    }

    private void decodeArg(VMPreparedOperand vMPreparedOperand, boolean z) {
        int fgetbits = fgetbits();
        if ((32768 & fgetbits) != 0) {
            vMPreparedOperand.setType(VMOpType.VM_OPREG);
            vMPreparedOperand.setData((fgetbits >> 12) & true);
            vMPreparedOperand.setOffset(vMPreparedOperand.getData());
            faddbits(4);
        } else if ((49152 & fgetbits) == 0) {
            vMPreparedOperand.setType(VMOpType.VM_OPINT);
            if (z) {
                vMPreparedOperand.setData((fgetbits >> 6) & 255);
                faddbits(10);
                return;
            }
            faddbits(true);
            vMPreparedOperand.setData(ReadData(this));
        } else {
            vMPreparedOperand.setType(VMOpType.VM_OPREGMEM);
            if (fgetbits & 8192) {
                if (fgetbits & 4096) {
                    vMPreparedOperand.setData(0);
                    faddbits(4);
                } else {
                    vMPreparedOperand.setData((fgetbits >> 9) & true);
                    vMPreparedOperand.setOffset(vMPreparedOperand.getData());
                    faddbits(7);
                }
                vMPreparedOperand.setBase(ReadData(this));
                return;
            }
            vMPreparedOperand.setData((fgetbits >> 10) & true);
            vMPreparedOperand.setOffset(vMPreparedOperand.getData());
            vMPreparedOperand.setBase(0);
            faddbits(6);
        }
    }

    private void optimize(VMPreparedProgram vMPreparedProgram) {
        VMPreparedProgram<VMPreparedCommand> cmd = vMPreparedProgram.getCmd();
        for (VMPreparedCommand vMPreparedCommand : cmd) {
            int i = C13981.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands[vMPreparedCommand.getOpCode().ordinal()];
            if (i == 1) {
                vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_MOVB : VMCommands.VM_MOVD);
            } else if (i == 4) {
                vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_CMPB : VMCommands.VM_CMPD);
            } else if ((VMCmdFlags.VM_CmdFlags[vMPreparedCommand.getOpCode().getVMCommand()] & 64) != 0) {
                Object obj = null;
                int indexOf = cmd.indexOf(vMPreparedCommand) + 1;
                while (indexOf < cmd.size()) {
                    byte b = VMCmdFlags.VM_CmdFlags[((VMPreparedCommand) cmd.get(indexOf)).getOpCode().getVMCommand()];
                    if ((b & 56) != 0) {
                        obj = 1;
                        break;
                    } else if ((b & 64) != 0) {
                        break;
                    } else {
                        indexOf++;
                    }
                }
                if (obj == null) {
                    i = C13981.$SwitchMap$ir$mahdi$mzip$rar$unpack$vm$VMCommands[vMPreparedCommand.getOpCode().ordinal()];
                    if (i == 7) {
                        vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_ADDB : VMCommands.VM_ADDD);
                    } else if (i == 10) {
                        vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_SUBB : VMCommands.VM_SUBD);
                    } else if (i == 15) {
                        vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_INCB : VMCommands.VM_INCD);
                    } else if (i == 18) {
                        vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_DECB : VMCommands.VM_DECD);
                    } else if (i == 39) {
                        vMPreparedCommand.setOpCode(vMPreparedCommand.isByteMode() ? VMCommands.VM_NEGB : VMCommands.VM_NEGD);
                    }
                }
            }
        }
    }

    private VMStandardFilters IsStandardFilter(byte[] bArr, int i) {
        i = new VMStandardFilterSignature[7];
        int i2 = 0;
        i[0] = new VMStandardFilterSignature(53, -1386780537, VMStandardFilters.VMSF_E8);
        i[1] = new VMStandardFilterSignature(57, 1020781950, VMStandardFilters.VMSF_E8E9);
        i[2] = new VMStandardFilterSignature(120, 929663295, VMStandardFilters.VMSF_ITANIUM);
        i[3] = new VMStandardFilterSignature(29, 235276157, VMStandardFilters.VMSF_DELTA);
        i[4] = new VMStandardFilterSignature(149, 472669640, VMStandardFilters.VMSF_RGB);
        i[5] = new VMStandardFilterSignature(216, -1132075263, VMStandardFilters.VMSF_AUDIO);
        i[6] = new VMStandardFilterSignature(40, 1186579808, VMStandardFilters.VMSF_UPCASE);
        int checkCrc = RarCRC.checkCrc(-1, bArr, 0, bArr.length) ^ -1;
        while (i2 < i.length) {
            if (i[i2].getCRC() == checkCrc && i[i2].getLength() == bArr.length) {
                return i[i2].getType();
            }
            i2++;
        }
        return VMStandardFilters.VMSF_NONE;
    }

    private void ExecuteStandardFilter(VMStandardFilters vMStandardFilters) {
        RarVM rarVM = this;
        long j = 3;
        int i;
        long j2;
        int i2;
        int i3;
        int i4;
        int i5;
        byte[] bArr;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int abs;
        switch (vMStandardFilters) {
            case VMSF_E8:
            case VMSF_E8E9:
                i = rarVM.f59R[4];
                j2 = (long) (rarVM.f59R[6] & -1);
                if (i < VM_GLOBALMEMADDR) {
                    i2 = 16777216;
                    byte b = (byte) (vMStandardFilters == VMStandardFilters.VMSF_E8E9 ? 233 : 232);
                    i3 = 0;
                    while (i3 < i - 4) {
                        byte b2;
                        i4 = i3 + 1;
                        byte b3 = rarVM.mem[i3];
                        if (b3 != (byte) -24) {
                            if (b3 != b) {
                                b2 = b;
                                i3 = i4;
                                b = b2;
                                i2 = 16777216;
                            }
                        }
                        long j3 = ((long) i4) + j2;
                        j = (long) getValue(false, rarVM.mem, i4);
                        if ((j & -2147483648L) != 0) {
                            if (((j + j3) & -2147483648L) == 0) {
                                setValue(false, rarVM.mem, i4, ((int) j) + i2);
                            }
                            b2 = b;
                        } else if (((j - ((long) i2)) & -2147483648L) != 0) {
                            b2 = b;
                            setValue(false, rarVM.mem, i4, (int) (j - j3));
                        } else {
                            b2 = b;
                        }
                        i3 = i4 + 4;
                        b = b2;
                        i2 = 16777216;
                    }
                    return;
                }
                return;
            case VMSF_ITANIUM:
                i = rarVM.f59R[4];
                long j4 = (long) (rarVM.f59R[6] & -1);
                if (i < VM_GLOBALMEMADDR) {
                    i5 = 16;
                    bArr = new byte[]{(byte) 4, (byte) 4, (byte) 6, (byte) 6, (byte) 0, (byte) 0, (byte) 7, (byte) 7, (byte) 4, (byte) 4, (byte) 0, (byte) 0, (byte) 4, (byte) 4, (byte) 0, (byte) 0};
                    j4 >>>= 4;
                    i6 = 0;
                    while (i6 < i - 21) {
                        byte[] bArr2;
                        i7 = (rarVM.mem[i6] & 31) - i5;
                        if (i7 >= 0) {
                            byte b4 = bArr[i7];
                            if (b4 != (byte) 0) {
                                i8 = 0;
                                while (i8 <= 2) {
                                    if (((1 << i8) & b4) != 0) {
                                        int i11 = (i8 * 41) + 5;
                                        if (filterItanium_GetBits(i6, i11 + 37, 4) == 5) {
                                            i11 += 13;
                                            bArr2 = bArr;
                                            filterItanium_SetBits(i6, ((int) (((long) filterItanium_GetBits(i6, i11, 20)) - j4)) & 1048575, i11, 20);
                                        } else {
                                            bArr2 = bArr;
                                        }
                                    } else {
                                        bArr2 = bArr;
                                    }
                                    i8++;
                                    bArr = bArr2;
                                }
                                bArr2 = bArr;
                                i6 += 16;
                                j4++;
                                bArr = bArr2;
                                i5 = 16;
                            }
                        }
                        bArr2 = bArr;
                        i6 += 16;
                        j4++;
                        bArr = bArr2;
                        i5 = 16;
                    }
                    return;
                }
                return;
            case VMSF_DELTA:
                i = rarVM.f59R[4] & -1;
                i6 = rarVM.f59R[0] & -1;
                i2 = (i * 2) & -1;
                setValue(false, rarVM.mem, 245792, i);
                if (i < 122880) {
                    i9 = 0;
                    i5 = 0;
                    while (i9 < i6) {
                        i3 = i + i9;
                        i7 = i5;
                        i5 = 0;
                        while (i3 < i2) {
                            i10 = i7 + 1;
                            i5 = (byte) (i5 - rarVM.mem[i7]);
                            rarVM.mem[i3] = i5;
                            i3 += i6;
                            i7 = i10;
                        }
                        i9++;
                        i5 = i7;
                    }
                    return;
                }
                return;
            case VMSF_RGB:
                i = rarVM.f59R[4];
                i2 = 3;
                i6 = rarVM.f59R[0] - 3;
                i5 = rarVM.f59R[1];
                setValue(false, rarVM.mem, 245792, i);
                if (i >= 122880) {
                    return;
                }
                if (i5 >= 0) {
                    i9 = 0;
                    int i12 = 0;
                    while (i9 < i2) {
                        int i13;
                        i3 = i9;
                        long j5 = 0;
                        while (i3 < i) {
                            i4 = i3 - i6;
                            if (i4 >= i2) {
                                i4 += i;
                                j = (long) (rarVM.mem[i4] & 255);
                                long j6 = (long) (rarVM.mem[i4 - i2] & 255);
                                long j7 = (j5 + j) - j6;
                                i4 = Math.abs((int) (j7 - j5));
                                abs = Math.abs((int) (j7 - j));
                                i13 = i5;
                                i2 = Math.abs((int) (j7 - j6));
                                if (i4 > abs || i4 > i2) {
                                    j5 = abs <= i2 ? j : j6;
                                }
                            } else {
                                i13 = i5;
                            }
                            i5 = i12 + 1;
                            long j8 = ((j5 - ((long) rarVM.mem[i12])) & 255) & 255;
                            rarVM.mem[i + i3] = (byte) ((int) (j8 & 255));
                            i3 += 3;
                            i12 = i5;
                            j5 = j8;
                            i5 = i13;
                            i2 = 3;
                        }
                        i13 = i5;
                        i9++;
                        i2 = 3;
                    }
                    i6 = i - 2;
                    for (i9 = i5; i9 < i6; i9 += 3) {
                        i5 = i + i9;
                        byte b5 = rarVM.mem[i5 + 1];
                        bArr = rarVM.mem;
                        bArr[i5] = (byte) (bArr[i5] + b5);
                        bArr = rarVM.mem;
                        i5 += 2;
                        bArr[i5] = (byte) (bArr[i5] + b5);
                    }
                    return;
                }
                return;
            case VMSF_AUDIO:
                i = rarVM.f59R[4];
                i8 = rarVM.f59R[0];
                setValue(false, rarVM.mem, 245792, i);
                if (i < 122880) {
                    i3 = 0;
                    i7 = 0;
                    while (i3 < i8) {
                        int i14;
                        long[] jArr = new long[7];
                        int i15 = i7;
                        j2 = 0;
                        i4 = 0;
                        abs = 0;
                        int i16 = 0;
                        int i17 = 0;
                        int i18 = 0;
                        long j9 = 0;
                        int i19 = 0;
                        i7 = i3;
                        while (i7 < i) {
                            long[] jArr2;
                            i6 = (int) j2;
                            i9 = i6 - i18;
                            long j10 = (((((8 * j9) + ((long) (i16 * i6))) + ((long) (i4 * i9))) + ((long) (i17 * abs))) >>> j) & 255;
                            long[] jArr3 = jArr;
                            j = (long) (rarVM.mem[i15] & 255);
                            int i20 = i15 + 1;
                            long j11 = (j10 - j) & -1;
                            i14 = i;
                            rarVM.mem[i + i7] = (byte) ((int) j11);
                            i10 = ((byte) ((int) j)) << 3;
                            long j12 = (long) ((byte) ((int) (j11 - j9)));
                            jArr3[0] = jArr3[0] + ((long) Math.abs(i10));
                            long j13 = j11;
                            jArr3[1] = jArr3[1] + ((long) Math.abs(i10 - i6));
                            int i21 = i6;
                            jArr3[2] = jArr3[2] + ((long) Math.abs(i10 + i6));
                            jArr3[3] = jArr3[3] + ((long) Math.abs(i10 - i9));
                            jArr3[4] = jArr3[4] + ((long) Math.abs(i10 + i9));
                            jArr3[5] = jArr3[5] + ((long) Math.abs(i10 - abs));
                            jArr3[6] = jArr3[6] + ((long) Math.abs(i10 + abs));
                            if ((i19 & 31) == 0) {
                                long j14 = jArr3[0];
                                long j15 = 0;
                                jArr3[0] = 0;
                                j11 = j14;
                                j = 0;
                                jArr2 = jArr3;
                                i = 1;
                                while (i < jArr2.length) {
                                    if (jArr2[i] < j11) {
                                        j11 = jArr2[i];
                                        j = (long) i;
                                    }
                                    jArr2[i] = j15;
                                    i++;
                                    j15 = 0;
                                }
                                switch ((int) j) {
                                    case 1:
                                        if (i16 < -16) {
                                            break;
                                        }
                                        i16--;
                                        break;
                                    case 2:
                                        if (i16 >= 16) {
                                            break;
                                        }
                                        i16++;
                                        break;
                                    case 3:
                                        if (i4 < -16) {
                                            break;
                                        }
                                        i4--;
                                        break;
                                    case 4:
                                        if (i4 >= 16) {
                                            break;
                                        }
                                        i4++;
                                        break;
                                    case 5:
                                        if (i17 < -16) {
                                            break;
                                        }
                                        i17--;
                                        break;
                                    case 6:
                                        if (i17 >= 16) {
                                            break;
                                        }
                                        i17++;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            jArr2 = jArr3;
                            i7 += i8;
                            i19++;
                            jArr = jArr2;
                            abs = i9;
                            i15 = i20;
                            i = i14;
                            j2 = j12;
                            j9 = j13;
                            i18 = i21;
                            rarVM = this;
                            j = 3;
                        }
                        i14 = i;
                        i3++;
                        i7 = i15;
                        rarVM = this;
                        j = 3;
                    }
                    return;
                }
                return;
            case VMSF_UPCASE:
                i = rarVM.f59R[4];
                if (i < 122880) {
                    i9 = i;
                    i6 = 0;
                    while (i6 < i) {
                        i5 = i6 + 1;
                        byte b6 = rarVM.mem[i6];
                        if (b6 == (byte) 2) {
                            i2 = i5 + 1;
                            b6 = rarVM.mem[i5];
                            if (b6 != (byte) 2) {
                                b6 = (byte) (b6 - 32);
                            }
                        } else {
                            i2 = i5;
                        }
                        i7 = i9 + 1;
                        rarVM.mem[i9] = b6;
                        i6 = i2;
                        i9 = i7;
                    }
                    setValue(false, rarVM.mem, 245788, i9 - i);
                    setValue(false, rarVM.mem, 245792, i);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void filterItanium_SetBits(int i, int i2, int i3, int i4) {
        int i5 = i3 / 8;
        i3 &= 7;
        i4 = ((-1 >>> (32 - i4)) << i3) ^ -1;
        i2 <<= i3;
        for (i3 = 0; i3 < 4; i3++) {
            byte[] bArr = this.mem;
            int i6 = (i + i5) + i3;
            bArr[i6] = (byte) (bArr[i6] & i4);
            bArr = this.mem;
            bArr[i6] = (byte) (bArr[i6] | i2);
            i4 = (i4 >>> 8) | ViewCompat.MEASURED_STATE_MASK;
            i2 >>>= 8;
        }
    }

    private int filterItanium_GetBits(int i, int i2, int i3) {
        int i4 = i2 / 8;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        return ((((this.mem[i + (i6 + 1)] & 255) << 24) | (((this.mem[i4 + i] & 255) | ((this.mem[i5 + i] & 255) << 8)) | ((this.mem[i6 + i] & 255) << 16))) >>> (i2 & 7)) & (-1 >>> (32 - i3));
    }

    public void setMemory(int i, byte[] bArr, int i2, int i3) {
        if (i < 262144) {
            int i4 = 0;
            while (i4 < Math.min(bArr.length - i2, i3)) {
                if (262144 - i >= i4) {
                    this.mem[i + i4] = bArr[i2 + i4];
                    i4++;
                } else {
                    return;
                }
            }
        }
    }
}
