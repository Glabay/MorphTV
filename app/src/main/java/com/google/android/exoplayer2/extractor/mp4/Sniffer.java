package com.google.android.exoplayer2.extractor.mp4;

import android.support.v4.media.session.PlaybackStateCompat;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = new int[]{Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString("avc1"), Util.getIntegerCodeForString("hvc1"), Util.getIntegerCodeForString("hev1"), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};
    private static final int SEARCH_LENGTH = 4096;

    public static boolean sniffFragmented(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return sniffInternal(extractorInput, false);
    }

    private static boolean sniffInternal(ExtractorInput extractorInput, boolean z) throws IOException, InterruptedException {
        boolean z2;
        ExtractorInput extractorInput2 = extractorInput;
        long length = extractorInput.getLength();
        long j = -1;
        if (length == -1 || length > PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM) {
            length = PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;
        }
        int i = (int) length;
        ParsableByteArray parsableByteArray = new ParsableByteArray(64);
        boolean z3 = false;
        int i2 = 0;
        Object obj = null;
        while (i2 < i) {
            parsableByteArray.reset(8);
            extractorInput2.peekFully(parsableByteArray.data, 0, 8);
            long readUnsignedInt = parsableByteArray.readUnsignedInt();
            int readInt = parsableByteArray.readInt();
            int i3 = 16;
            if (readUnsignedInt == 1) {
                extractorInput2.peekFully(parsableByteArray.data, 8, 8);
                parsableByteArray.setLimit(16);
                readUnsignedInt = parsableByteArray.readUnsignedLongToLong();
            } else {
                if (readUnsignedInt == 0) {
                    long length2 = extractorInput.getLength();
                    if (length2 != j) {
                        readUnsignedInt = (length2 - extractorInput.getPosition()) + ((long) 8);
                    }
                }
                i3 = 8;
            }
            long j2 = (long) i3;
            if (readUnsignedInt >= j2) {
                i2 += i3;
                if (readInt != Atom.TYPE_moov) {
                    if (readInt != Atom.TYPE_moof) {
                        if (readInt != Atom.TYPE_mvex) {
                            if ((((long) i2) + readUnsignedInt) - j2 >= ((long) i)) {
                                break;
                            }
                            int i4 = (int) (readUnsignedInt - j2);
                            i2 += i4;
                            if (readInt == Atom.TYPE_ftyp) {
                                if (i4 < 8) {
                                    return false;
                                }
                                parsableByteArray.reset(i4);
                                extractorInput2.peekFully(parsableByteArray.data, 0, i4);
                                i4 /= 4;
                                for (int i5 = 0; i5 < i4; i5++) {
                                    if (i5 == 1) {
                                        parsableByteArray.skipBytes(4);
                                    } else if (isCompatibleBrand(parsableByteArray.readInt())) {
                                        obj = 1;
                                        break;
                                    }
                                }
                                if (obj == null) {
                                    return false;
                                }
                            } else if (i4 != 0) {
                                extractorInput2.advancePeekPosition(i4);
                            }
                            j = -1;
                        }
                    }
                    z2 = true;
                    break;
                }
            } else {
                return false;
            }
        }
        z2 = false;
        if (obj != null && z == r0) {
            z3 = true;
        }
        return z3;
    }

    private static boolean isCompatibleBrand(int i) {
        if ((i >>> 8) == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        for (int i2 : COMPATIBLE_BRANDS) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private Sniffer() {
    }
}
