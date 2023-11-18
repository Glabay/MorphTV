package ir.mahdi.mzip.rar.unpack;

import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.InputDeviceCompat;
import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.unpack.decode.AudioVariables;
import ir.mahdi.mzip.rar.unpack.decode.BitDecode;
import ir.mahdi.mzip.rar.unpack.decode.Compress;
import ir.mahdi.mzip.rar.unpack.decode.Decode;
import ir.mahdi.mzip.rar.unpack.decode.DistDecode;
import ir.mahdi.mzip.rar.unpack.decode.LitDecode;
import ir.mahdi.mzip.rar.unpack.decode.LowDistDecode;
import ir.mahdi.mzip.rar.unpack.decode.MultDecode;
import ir.mahdi.mzip.rar.unpack.decode.RepDecode;
import java.io.IOException;
import java.util.Arrays;

public abstract class Unpack20 extends Unpack15 {
    public static final int[] DBits = new int[]{0, 0, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 15, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16};
    public static final int[] DDecode = new int[]{0, 1, 2, 3, 4, 6, 8, 12, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 3072, 4096, 6144, 8192, 12288, 16384, 24576, 32768, 49152, 65536, 98304, 131072, 196608, 262144, 327680, 393216, 458752, 524288, 589824, 655360, 720896, 786432, 851968, 917504, 983040};
    public static final byte[] LBits = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 2, (byte) 2, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 4, (byte) 4, (byte) 4, (byte) 4, (byte) 5, (byte) 5, (byte) 5, (byte) 5};
    public static final int[] LDecode = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 12, 14, 16, 20, 24, 28, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224};
    public static final int[] SDBits = new int[]{2, 2, 3, 4, 5, 6, 6, 6};
    public static final int[] SDDecode = new int[]{0, 4, 8, 16, 32, 64, 128, 192};
    protected AudioVariables[] AudV = new AudioVariables[4];
    protected BitDecode BD = new BitDecode();
    protected DistDecode DD = new DistDecode();
    protected LitDecode LD = new LitDecode();
    protected LowDistDecode LDD = new LowDistDecode();
    protected MultDecode[] MD = new MultDecode[4];
    protected RepDecode RD = new RepDecode();
    protected int UnpAudioBlock;
    protected int UnpChannelDelta;
    protected int UnpChannels;
    protected int UnpCurChannel;
    protected byte[] UnpOldTable20 = new byte[1028];

    protected void unpack20(boolean z) throws IOException, RarException {
        if (this.suspended) {
            this.unpPtr = this.wrPtr;
        } else {
            unpInitData(z);
            if (!unpReadBuf()) {
                return;
            }
            if (z || ReadTables20()) {
                this.destUnpSize--;
            } else {
                return;
            }
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
            byte[] bArr;
            int i;
            if (this.UnpAudioBlock) {
                z = decodeNumber(this.MD[this.UnpCurChannel]);
                if (!z) {
                    bArr = this.window;
                    i = this.unpPtr;
                    this.unpPtr = i + 1;
                    bArr[i] = DecodeAudio(z);
                    z = this.UnpCurChannel + 1;
                    this.UnpCurChannel = z;
                    if (z == this.UnpChannels) {
                        this.UnpCurChannel = false;
                    }
                    this.destUnpSize--;
                } else if (!ReadTables20()) {
                    break;
                }
            } else {
                z = decodeNumber(this.LD);
                if (z < true) {
                    bArr = this.window;
                    i = this.unpPtr;
                    this.unpPtr = i + 1;
                    bArr[i] = (byte) z;
                    this.destUnpSize--;
                } else if (z > true) {
                    z -= 270;
                    r0 = LDecode[z] + 3;
                    z = LBits[z];
                    if (z <= false) {
                        r0 += getbits() >>> (16 - z);
                        addbits(z);
                    }
                    z = decodeNumber(this.DD);
                    i = DDecode[z] + 1;
                    z = DBits[z];
                    if (z <= false) {
                        i += getbits() >>> (16 - z);
                        addbits(z);
                    }
                    if (i >= 8192) {
                        r0++;
                        if (((long) i) >= PlaybackStateCompat.ACTION_SET_REPEAT_MODE) {
                            r0++;
                        }
                    }
                    CopyString20(r0, i);
                } else if (z) {
                    if (!ReadTables20()) {
                        break;
                    }
                } else if (z) {
                    CopyString20(this.lastLength, this.lastDist);
                } else if (z < true) {
                    z = this.oldDist[(this.oldDistPtr - (z + InputDeviceCompat.SOURCE_ANY)) & 3];
                    r0 = decodeNumber(this.RD);
                    i = LDecode[r0] + 2;
                    byte b = LBits[r0];
                    if (b > (byte) 0) {
                        i += getbits() >>> (16 - b);
                        addbits(b);
                    }
                    if (z >= true) {
                        i++;
                        if (z >= true) {
                            i++;
                            if (z >= true) {
                                i++;
                            }
                        }
                    }
                    CopyString20(i, z);
                } else if (z < true) {
                    z -= 261;
                    r0 = SDDecode[z] + 1;
                    z = SDBits[z];
                    if (z <= false) {
                        r0 += getbits() >>> (16 - z);
                        addbits(z);
                    }
                    CopyString20(2, r0);
                }
            }
        }
        ReadLastTables();
        oldUnpWriteBuf();
    }

    protected void CopyString20(int i, int i2) {
        int[] iArr = this.oldDist;
        int i3 = this.oldDistPtr;
        this.oldDistPtr = i3 + 1;
        iArr[i3 & 3] = i2;
        this.lastDist = i2;
        this.lastLength = i;
        this.destUnpSize -= (long) i;
        int i4 = this.unpPtr - i2;
        if (i4 >= 4194004 || this.unpPtr >= 4194004) {
            while (true) {
                i2 = i - 1;
                if (i != 0) {
                    int i5 = i4 + 1;
                    this.window[this.unpPtr] = this.window[i4 & Compress.MAXWINMASK];
                    this.unpPtr = (this.unpPtr + 1) & Compress.MAXWINMASK;
                    i = i2;
                    i4 = i5;
                } else {
                    return;
                }
            }
        }
        i2 = this.window;
        i3 = this.unpPtr;
        this.unpPtr = i3 + 1;
        i5 = i4 + 1;
        i2[i3] = this.window[i4];
        i2 = this.window;
        i4 = this.unpPtr;
        this.unpPtr = i4 + 1;
        int i6 = i5 + 1;
        i2[i4] = this.window[i5];
        while (i > 2) {
            i--;
            i2 = this.window;
            i4 = this.unpPtr;
            this.unpPtr = i4 + 1;
            i5 = i6 + 1;
            i2[i4] = this.window[i6];
            i6 = i5;
        }
    }

    protected void makeDecodeTables(byte[] bArr, int i, Decode decode, int i2) {
        int i3 = i2;
        int[] iArr = new int[16];
        int[] iArr2 = new int[16];
        int i4 = 0;
        Arrays.fill(iArr, 0);
        Arrays.fill(decode.getDecodeNum(), 0);
        int i5 = 0;
        while (true) {
            int i6 = 1;
            if (i5 >= i3) {
                break;
            }
            int i7 = bArr[i + i5] & 15;
            iArr[i7] = iArr[i7] + 1;
            i5++;
        }
        iArr[0] = 0;
        iArr2[0] = 0;
        decode.getDecodePos()[0] = 0;
        decode.getDecodeLen()[0] = 0;
        long j = 0;
        while (i6 < 16) {
            j = (j + ((long) iArr[i6])) * 2;
            long j2 = j << (15 - i6);
            if (j2 > 65535) {
                j2 = 65535;
            }
            decode.getDecodeLen()[i6] = (int) j2;
            int i8 = i6 - 1;
            int i9 = decode.getDecodePos()[i8] + iArr[i8];
            decode.getDecodePos()[i6] = i9;
            iArr2[i6] = i9;
            i6++;
        }
        while (i4 < i3) {
            int i10 = i + i4;
            if (bArr[i10] != (byte) 0) {
                iArr = decode.getDecodeNum();
                i10 = bArr[i10] & 15;
                i5 = iArr2[i10];
                iArr2[i10] = i5 + 1;
                iArr[i5] = i4;
            }
            i4++;
        }
        decode.setMaxNum(i2);
    }

    protected int decodeNumber(Decode decode) {
        Unpack20 unpack20;
        long j = (long) (getbits() & 65534);
        int[] decodeLen = decode.getDecodeLen();
        int i = 13;
        if (j < ((long) decodeLen[8])) {
            if (j < ((long) decodeLen[4])) {
                if (j < ((long) decodeLen[2])) {
                    if (j < ((long) decodeLen[1])) {
                        unpack20 = this;
                        i = 1;
                    } else {
                        unpack20 = this;
                        i = 2;
                    }
                } else if (j < ((long) decodeLen[3])) {
                    unpack20 = this;
                    i = 3;
                } else {
                    unpack20 = this;
                    i = 4;
                }
            } else if (j < ((long) decodeLen[6])) {
                if (j < ((long) decodeLen[5])) {
                    unpack20 = this;
                    i = 5;
                } else {
                    unpack20 = this;
                    i = 6;
                }
            } else if (j < ((long) decodeLen[7])) {
                unpack20 = this;
                i = 7;
            } else {
                unpack20 = this;
                i = 8;
            }
        } else if (j < ((long) decodeLen[12])) {
            if (j < ((long) decodeLen[10])) {
                if (j < ((long) decodeLen[9])) {
                    unpack20 = this;
                    i = 9;
                } else {
                    unpack20 = this;
                    i = 10;
                }
            } else if (j < ((long) decodeLen[11])) {
                unpack20 = this;
                i = 11;
            } else {
                unpack20 = this;
                i = 12;
            }
        } else if (j >= ((long) decodeLen[14])) {
            unpack20 = this;
            i = 15;
        } else if (j < ((long) decodeLen[13])) {
            unpack20 = this;
        } else {
            unpack20 = this;
            i = 14;
        }
        unpack20.addbits(i);
        int i2 = decode.getDecodePos()[i] + ((((int) j) - decodeLen[i - 1]) >>> (16 - i));
        if (i2 >= decode.getMaxNum()) {
            i2 = 0;
        }
        return decode.getDecodeNum()[i2];
    }

    protected boolean ReadTables20() throws IOException, RarException {
        byte[] bArr = new byte[19];
        byte[] bArr2 = new byte[1028];
        int i = 0;
        if (this.inAddr > this.readTop - 25 && !unpReadBuf()) {
            return false;
        }
        int i2;
        int i3 = getbits();
        this.UnpAudioBlock = 32768 & i3;
        if ((i3 & 16384) == 0) {
            Arrays.fill(this.UnpOldTable20, (byte) 0);
        }
        addbits(2);
        if (this.UnpAudioBlock != 0) {
            this.UnpChannels = ((i3 >>> 12) & 3) + 1;
            if (this.UnpCurChannel >= this.UnpChannels) {
                this.UnpCurChannel = 0;
            }
            addbits(2);
            i3 = this.UnpChannels * 257;
        } else {
            i3 = 374;
        }
        for (i2 = 0; i2 < 19; i2++) {
            bArr[i2] = (byte) (getbits() >>> 12);
            addbits(4);
        }
        makeDecodeTables(bArr, 0, this.BD, 19);
        int i4 = 0;
        while (i4 < i3) {
            if (this.inAddr <= this.readTop - 5 || unpReadBuf()) {
                int decodeNumber = decodeNumber(this.BD);
                if (decodeNumber >= 16) {
                    if (decodeNumber != 16) {
                        if (decodeNumber == 17) {
                            decodeNumber = (getbits() >>> 13) + 3;
                            addbits(3);
                        } else {
                            decodeNumber = (getbits() >>> 9) + 11;
                            addbits(7);
                        }
                        while (true) {
                            i2 = decodeNumber - 1;
                            if (decodeNumber <= 0 || i4 >= i3) {
                                break;
                            }
                            decodeNumber = i4 + 1;
                            bArr2[i4] = (byte) 0;
                            i4 = decodeNumber;
                            decodeNumber = i2;
                        }
                    } else {
                        decodeNumber = (getbits() >>> 14) + 3;
                        addbits(2);
                        while (true) {
                            i2 = decodeNumber - 1;
                            if (decodeNumber <= 0 || i4 >= i3) {
                                break;
                            }
                            bArr2[i4] = bArr2[i4 - 1];
                            i4++;
                            decodeNumber = i2;
                        }
                    }
                } else {
                    bArr2[i4] = (byte) ((decodeNumber + this.UnpOldTable20[i4]) & 15);
                    i4++;
                }
            } else {
                return false;
            }
        }
        if (this.inAddr > this.readTop) {
            return true;
        }
        if (this.UnpAudioBlock != 0) {
            for (i4 = 0; i4 < this.UnpChannels; i4++) {
                makeDecodeTables(bArr2, i4 * 257, this.MD[i4], 257);
            }
        } else {
            makeDecodeTables(bArr2, 0, this.LD, Compress.NC20);
            makeDecodeTables(bArr2, Compress.NC20, this.DD, 48);
            makeDecodeTables(bArr2, 346, this.RD, 28);
        }
        while (i < this.UnpOldTable20.length) {
            this.UnpOldTable20[i] = bArr2[i];
            i++;
        }
        return true;
    }

    protected void unpInitData20(boolean z) {
        if (!z) {
            this.UnpCurChannel = 0;
            this.UnpChannelDelta = 0;
            this.UnpChannels = 1;
            Arrays.fill(this.AudV, new AudioVariables());
            Arrays.fill(this.UnpOldTable20, (byte) 0);
        }
    }

    protected void ReadLastTables() throws IOException, RarException {
        if (this.readTop < this.inAddr + 5) {
            return;
        }
        if (this.UnpAudioBlock != 0) {
            if (decodeNumber(this.MD[this.UnpCurChannel]) == 256) {
                ReadTables20();
            }
        } else if (decodeNumber(this.LD) == 269) {
            ReadTables20();
        }
    }

    protected byte DecodeAudio(int i) {
        AudioVariables audioVariables = this.AudV[this.UnpCurChannel];
        audioVariables.setByteCount(audioVariables.getByteCount() + 1);
        audioVariables.setD4(audioVariables.getD3());
        audioVariables.setD3(audioVariables.getD2());
        audioVariables.setD2(audioVariables.getLastDelta() - audioVariables.getD1());
        audioVariables.setD1(audioVariables.getLastDelta());
        int lastChar = ((((((audioVariables.getLastChar() * 8) + (audioVariables.getK1() * audioVariables.getD1())) + ((audioVariables.getK2() * audioVariables.getD2()) + (audioVariables.getK3() * audioVariables.getD3()))) + ((audioVariables.getK4() * audioVariables.getD4()) + (audioVariables.getK5() * this.UnpChannelDelta))) >>> 3) & 255) - i;
        i = ((byte) i) << 3;
        int[] dif = audioVariables.getDif();
        dif[0] = dif[0] + Math.abs(i);
        dif = audioVariables.getDif();
        dif[1] = dif[1] + Math.abs(i - audioVariables.getD1());
        dif = audioVariables.getDif();
        dif[2] = dif[2] + Math.abs(audioVariables.getD1() + i);
        dif = audioVariables.getDif();
        dif[3] = dif[3] + Math.abs(i - audioVariables.getD2());
        int[] dif2 = audioVariables.getDif();
        dif2[4] = dif2[4] + Math.abs(audioVariables.getD2() + i);
        dif2 = audioVariables.getDif();
        dif2[5] = dif2[5] + Math.abs(i - audioVariables.getD3());
        dif2 = audioVariables.getDif();
        dif2[6] = dif2[6] + Math.abs(audioVariables.getD3() + i);
        dif2 = audioVariables.getDif();
        dif2[7] = dif2[7] + Math.abs(i - audioVariables.getD4());
        dif2 = audioVariables.getDif();
        dif2[8] = dif2[8] + Math.abs(audioVariables.getD4() + i);
        int[] dif3 = audioVariables.getDif();
        dif3[9] = dif3[9] + Math.abs(i - this.UnpChannelDelta);
        dif3 = audioVariables.getDif();
        dif3[10] = dif3[10] + Math.abs(i + this.UnpChannelDelta);
        audioVariables.setLastDelta((byte) (lastChar - audioVariables.getLastChar()));
        this.UnpChannelDelta = audioVariables.getLastDelta();
        audioVariables.setLastChar(lastChar);
        if ((audioVariables.getByteCount() & 31) == 0) {
            i = audioVariables.getDif()[0];
            audioVariables.getDif()[0] = 0;
            int i2 = i;
            int i3 = 0;
            for (i = 1; i < audioVariables.getDif().length; i++) {
                if (audioVariables.getDif()[i] < i2) {
                    i2 = audioVariables.getDif()[i];
                    i3 = i;
                }
                audioVariables.getDif()[i] = 0;
            }
            switch (i3) {
                case 1:
                    if (audioVariables.getK1() >= -16) {
                        audioVariables.setK1(audioVariables.getK1() - 1);
                        break;
                    }
                    break;
                case 2:
                    if (audioVariables.getK1() < 16) {
                        audioVariables.setK1(audioVariables.getK1() + 1);
                        break;
                    }
                    break;
                case 3:
                    if (audioVariables.getK2() >= -16) {
                        audioVariables.setK2(audioVariables.getK2() - 1);
                        break;
                    }
                    break;
                case 4:
                    if (audioVariables.getK2() < 16) {
                        audioVariables.setK2(audioVariables.getK2() + 1);
                        break;
                    }
                    break;
                case 5:
                    if (audioVariables.getK3() >= -16) {
                        audioVariables.setK3(audioVariables.getK3() - 1);
                        break;
                    }
                    break;
                case 6:
                    if (audioVariables.getK3() < 16) {
                        audioVariables.setK3(audioVariables.getK3() + 1);
                        break;
                    }
                    break;
                case 7:
                    if (audioVariables.getK4() >= -16) {
                        audioVariables.setK4(audioVariables.getK4() - 1);
                        break;
                    }
                    break;
                case 8:
                    if (audioVariables.getK4() < 16) {
                        audioVariables.setK4(audioVariables.getK4() + 1);
                        break;
                    }
                    break;
                case 9:
                    if (audioVariables.getK5() >= -16) {
                        audioVariables.setK5(audioVariables.getK5() - 1);
                        break;
                    }
                    break;
                case 10:
                    if (audioVariables.getK5() < 16) {
                        audioVariables.setK5(audioVariables.getK5() + 1);
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        return (byte) lastChar;
    }
}
