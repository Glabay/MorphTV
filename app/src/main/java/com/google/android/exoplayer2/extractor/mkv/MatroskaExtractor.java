package com.google.android.exoplayer2.extractor.mkv;

import android.support.annotation.Nullable;
import android.util.SparseArray;
import com.google.android.exoplayer2.C0649C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.SeekMap.Unseekable;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput.CryptoData;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

public final class MatroskaExtractor implements Extractor {
    private static final int BLOCK_STATE_DATA = 2;
    private static final int BLOCK_STATE_HEADER = 1;
    private static final int BLOCK_STATE_START = 0;
    private static final String CODEC_ID_AAC = "A_AAC";
    private static final String CODEC_ID_AC3 = "A_AC3";
    private static final String CODEC_ID_ACM = "A_MS/ACM";
    private static final String CODEC_ID_ASS = "S_TEXT/ASS";
    private static final String CODEC_ID_DTS = "A_DTS";
    private static final String CODEC_ID_DTS_EXPRESS = "A_DTS/EXPRESS";
    private static final String CODEC_ID_DTS_LOSSLESS = "A_DTS/LOSSLESS";
    private static final String CODEC_ID_DVBSUB = "S_DVBSUB";
    private static final String CODEC_ID_E_AC3 = "A_EAC3";
    private static final String CODEC_ID_FLAC = "A_FLAC";
    private static final String CODEC_ID_FOURCC = "V_MS/VFW/FOURCC";
    private static final String CODEC_ID_H264 = "V_MPEG4/ISO/AVC";
    private static final String CODEC_ID_H265 = "V_MPEGH/ISO/HEVC";
    private static final String CODEC_ID_MP2 = "A_MPEG/L2";
    private static final String CODEC_ID_MP3 = "A_MPEG/L3";
    private static final String CODEC_ID_MPEG2 = "V_MPEG2";
    private static final String CODEC_ID_MPEG4_AP = "V_MPEG4/ISO/AP";
    private static final String CODEC_ID_MPEG4_ASP = "V_MPEG4/ISO/ASP";
    private static final String CODEC_ID_MPEG4_SP = "V_MPEG4/ISO/SP";
    private static final String CODEC_ID_OPUS = "A_OPUS";
    private static final String CODEC_ID_PCM_INT_LIT = "A_PCM/INT/LIT";
    private static final String CODEC_ID_PGS = "S_HDMV/PGS";
    private static final String CODEC_ID_SUBRIP = "S_TEXT/UTF8";
    private static final String CODEC_ID_THEORA = "V_THEORA";
    private static final String CODEC_ID_TRUEHD = "A_TRUEHD";
    private static final String CODEC_ID_VOBSUB = "S_VOBSUB";
    private static final String CODEC_ID_VORBIS = "A_VORBIS";
    private static final String CODEC_ID_VP8 = "V_VP8";
    private static final String CODEC_ID_VP9 = "V_VP9";
    private static final String DOC_TYPE_MATROSKA = "matroska";
    private static final String DOC_TYPE_WEBM = "webm";
    private static final int ENCRYPTION_IV_SIZE = 8;
    public static final ExtractorsFactory FACTORY = new C06851();
    public static final int FLAG_DISABLE_SEEK_FOR_CUES = 1;
    private static final int FOURCC_COMPRESSION_VC1 = 826496599;
    private static final int ID_AUDIO = 225;
    private static final int ID_AUDIO_BIT_DEPTH = 25188;
    private static final int ID_BLOCK = 161;
    private static final int ID_BLOCK_DURATION = 155;
    private static final int ID_BLOCK_GROUP = 160;
    private static final int ID_CHANNELS = 159;
    private static final int ID_CLUSTER = 524531317;
    private static final int ID_CODEC_DELAY = 22186;
    private static final int ID_CODEC_ID = 134;
    private static final int ID_CODEC_PRIVATE = 25506;
    private static final int ID_COLOUR = 21936;
    private static final int ID_COLOUR_PRIMARIES = 21947;
    private static final int ID_COLOUR_RANGE = 21945;
    private static final int ID_COLOUR_TRANSFER = 21946;
    private static final int ID_CONTENT_COMPRESSION = 20532;
    private static final int ID_CONTENT_COMPRESSION_ALGORITHM = 16980;
    private static final int ID_CONTENT_COMPRESSION_SETTINGS = 16981;
    private static final int ID_CONTENT_ENCODING = 25152;
    private static final int ID_CONTENT_ENCODINGS = 28032;
    private static final int ID_CONTENT_ENCODING_ORDER = 20529;
    private static final int ID_CONTENT_ENCODING_SCOPE = 20530;
    private static final int ID_CONTENT_ENCRYPTION = 20533;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS = 18407;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE = 18408;
    private static final int ID_CONTENT_ENCRYPTION_ALGORITHM = 18401;
    private static final int ID_CONTENT_ENCRYPTION_KEY_ID = 18402;
    private static final int ID_CUES = 475249515;
    private static final int ID_CUE_CLUSTER_POSITION = 241;
    private static final int ID_CUE_POINT = 187;
    private static final int ID_CUE_TIME = 179;
    private static final int ID_CUE_TRACK_POSITIONS = 183;
    private static final int ID_DEFAULT_DURATION = 2352003;
    private static final int ID_DISPLAY_HEIGHT = 21690;
    private static final int ID_DISPLAY_UNIT = 21682;
    private static final int ID_DISPLAY_WIDTH = 21680;
    private static final int ID_DOC_TYPE = 17026;
    private static final int ID_DOC_TYPE_READ_VERSION = 17029;
    private static final int ID_DURATION = 17545;
    private static final int ID_EBML = 440786851;
    private static final int ID_EBML_READ_VERSION = 17143;
    private static final int ID_FLAG_DEFAULT = 136;
    private static final int ID_FLAG_FORCED = 21930;
    private static final int ID_INFO = 357149030;
    private static final int ID_LANGUAGE = 2274716;
    private static final int ID_LUMNINANCE_MAX = 21977;
    private static final int ID_LUMNINANCE_MIN = 21978;
    private static final int ID_MASTERING_METADATA = 21968;
    private static final int ID_MAX_CLL = 21948;
    private static final int ID_MAX_FALL = 21949;
    private static final int ID_PIXEL_HEIGHT = 186;
    private static final int ID_PIXEL_WIDTH = 176;
    private static final int ID_PRIMARY_B_CHROMATICITY_X = 21973;
    private static final int ID_PRIMARY_B_CHROMATICITY_Y = 21974;
    private static final int ID_PRIMARY_G_CHROMATICITY_X = 21971;
    private static final int ID_PRIMARY_G_CHROMATICITY_Y = 21972;
    private static final int ID_PRIMARY_R_CHROMATICITY_X = 21969;
    private static final int ID_PRIMARY_R_CHROMATICITY_Y = 21970;
    private static final int ID_PROJECTION = 30320;
    private static final int ID_PROJECTION_PRIVATE = 30322;
    private static final int ID_REFERENCE_BLOCK = 251;
    private static final int ID_SAMPLING_FREQUENCY = 181;
    private static final int ID_SEEK = 19899;
    private static final int ID_SEEK_HEAD = 290298740;
    private static final int ID_SEEK_ID = 21419;
    private static final int ID_SEEK_POSITION = 21420;
    private static final int ID_SEEK_PRE_ROLL = 22203;
    private static final int ID_SEGMENT = 408125543;
    private static final int ID_SEGMENT_INFO = 357149030;
    private static final int ID_SIMPLE_BLOCK = 163;
    private static final int ID_STEREO_MODE = 21432;
    private static final int ID_TIMECODE_SCALE = 2807729;
    private static final int ID_TIME_CODE = 231;
    private static final int ID_TRACKS = 374648427;
    private static final int ID_TRACK_ENTRY = 174;
    private static final int ID_TRACK_NUMBER = 215;
    private static final int ID_TRACK_TYPE = 131;
    private static final int ID_VIDEO = 224;
    private static final int ID_WHITE_POINT_CHROMATICITY_X = 21975;
    private static final int ID_WHITE_POINT_CHROMATICITY_Y = 21976;
    private static final int LACING_EBML = 3;
    private static final int LACING_FIXED_SIZE = 2;
    private static final int LACING_NONE = 0;
    private static final int LACING_XIPH = 1;
    private static final int OPUS_MAX_INPUT_SIZE = 5760;
    private static final byte[] SSA_DIALOGUE_FORMAT = Util.getUtf8Bytes("Format: Start, End, ReadOrder, Layer, Style, Name, MarginL, MarginR, MarginV, Effect, Text");
    private static final byte[] SSA_PREFIX = new byte[]{(byte) 68, (byte) 105, (byte) 97, (byte) 108, (byte) 111, (byte) 103, (byte) 117, (byte) 101, (byte) 58, (byte) 32, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44};
    private static final int SSA_PREFIX_END_TIMECODE_OFFSET = 21;
    private static final byte[] SSA_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final String SSA_TIMECODE_FORMAT = "%01d:%02d:%02d:%02d";
    private static final long SSA_TIMECODE_LAST_VALUE_SCALING_FACTOR = 10000;
    private static final byte[] SUBRIP_PREFIX = new byte[]{(byte) 49, (byte) 10, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 32, (byte) 45, (byte) 45, (byte) 62, (byte) 32, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 58, (byte) 48, (byte) 48, (byte) 44, (byte) 48, (byte) 48, (byte) 48, (byte) 10};
    private static final int SUBRIP_PREFIX_END_TIMECODE_OFFSET = 19;
    private static final byte[] SUBRIP_TIMECODE_EMPTY = new byte[]{(byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32, (byte) 32};
    private static final String SUBRIP_TIMECODE_FORMAT = "%02d:%02d:%02d,%03d";
    private static final long SUBRIP_TIMECODE_LAST_VALUE_SCALING_FACTOR = 1000;
    private static final String TAG = "MatroskaExtractor";
    private static final int TRACK_TYPE_AUDIO = 2;
    private static final int UNSET_ENTRY_ID = -1;
    private static final int VORBIS_MAX_INPUT_SIZE = 8192;
    private static final int WAVE_FORMAT_EXTENSIBLE = 65534;
    private static final int WAVE_FORMAT_PCM = 1;
    private static final int WAVE_FORMAT_SIZE = 18;
    private static final UUID WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);
    private long blockDurationUs;
    private int blockFlags;
    private int blockLacingSampleCount;
    private int blockLacingSampleIndex;
    private int[] blockLacingSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private boolean sampleRead;
    private boolean sampleSeenReferenceBlock;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final ParsableByteArray subtitleSample;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;

    /* renamed from: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$1 */
    static class C06851 implements ExtractorsFactory {
        C06851() {
        }

        public Extractor[] createExtractors() {
            return new Extractor[]{new MatroskaExtractor()};
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
        private InnerEbmlReaderOutput() {
        }

        public int getElementType(int i) {
            return MatroskaExtractor.this.getElementType(i);
        }

        public boolean isLevel1Element(int i) {
            return MatroskaExtractor.this.isLevel1Element(i);
        }

        public void startMasterElement(int i, long j, long j2) throws ParserException {
            MatroskaExtractor.this.startMasterElement(i, j, j2);
        }

        public void endMasterElement(int i) throws ParserException {
            MatroskaExtractor.this.endMasterElement(i);
        }

        public void integerElement(int i, long j) throws ParserException {
            MatroskaExtractor.this.integerElement(i, j);
        }

        public void floatElement(int i, double d) throws ParserException {
            MatroskaExtractor.this.floatElement(i, d);
        }

        public void stringElement(int i, String str) throws ParserException {
            MatroskaExtractor.this.stringElement(i, str);
        }

        public void binaryElement(int i, int i2, ExtractorInput extractorInput) throws IOException, InterruptedException {
            MatroskaExtractor.this.binaryElement(i, i2, extractorInput);
        }
    }

    private static final class Track {
        private static final int DEFAULT_MAX_CLL = 1000;
        private static final int DEFAULT_MAX_FALL = 200;
        private static final int DISPLAY_UNIT_PIXELS = 0;
        private static final int MAX_CHROMATICITY = 50000;
        public int audioBitDepth;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public CryptoData cryptoData;
        public int defaultSampleDurationNs;
        public int displayHeight;
        public int displayUnit;
        public int displayWidth;
        public DrmInitData drmInitData;
        public boolean flagDefault;
        public boolean flagForced;
        public boolean hasColorInfo;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int maxContentLuminance;
        public int maxFrameAverageLuminance;
        public float maxMasteringLuminance;
        public float minMasteringLuminance;
        public int nalUnitLengthFieldLength;
        public int number;
        public TrackOutput output;
        public float primaryBChromaticityX;
        public float primaryBChromaticityY;
        public float primaryGChromaticityX;
        public float primaryGChromaticityY;
        public float primaryRChromaticityX;
        public float primaryRChromaticityY;
        public byte[] projectionData;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int stereoMode;
        @Nullable
        public TrueHdSampleRechunker trueHdSampleRechunker;
        public int type;
        public float whitePointChromaticityX;
        public float whitePointChromaticityY;
        public int width;

        private Track() {
            this.width = -1;
            this.height = -1;
            this.displayWidth = -1;
            this.displayHeight = -1;
            this.displayUnit = 0;
            this.projectionData = null;
            this.stereoMode = -1;
            this.hasColorInfo = false;
            this.colorSpace = -1;
            this.colorTransfer = -1;
            this.colorRange = -1;
            this.maxContentLuminance = 1000;
            this.maxFrameAverageLuminance = 200;
            this.primaryRChromaticityX = -1.0f;
            this.primaryRChromaticityY = -1.0f;
            this.primaryGChromaticityX = -1.0f;
            this.primaryGChromaticityY = -1.0f;
            this.primaryBChromaticityX = -1.0f;
            this.primaryBChromaticityY = -1.0f;
            this.whitePointChromaticityX = -1.0f;
            this.whitePointChromaticityY = -1.0f;
            this.maxMasteringLuminance = -1.0f;
            this.minMasteringLuminance = -1.0f;
            this.channelCount = 1;
            this.audioBitDepth = -1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0;
            this.seekPreRollNs = 0;
            this.flagDefault = true;
            this.language = "eng";
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void initializeOutput(com.google.android.exoplayer2.extractor.ExtractorOutput r27, int r28) throws com.google.android.exoplayer2.ParserException {
            /*
            r26 = this;
            r0 = r26;
            r1 = r0.codecId;
            r2 = r1.hashCode();
            r3 = 4;
            r4 = 8;
            r5 = 1;
            r6 = 0;
            r7 = 2;
            r8 = 3;
            r9 = -1;
            switch(r2) {
                case -2095576542: goto L_0x0156;
                case -2095575984: goto L_0x014c;
                case -1985379776: goto L_0x0141;
                case -1784763192: goto L_0x0136;
                case -1730367663: goto L_0x012b;
                case -1482641358: goto L_0x0120;
                case -1482641357: goto L_0x0115;
                case -1373388978: goto L_0x010a;
                case -933872740: goto L_0x00ff;
                case -538363189: goto L_0x00f4;
                case -538363109: goto L_0x00e9;
                case -425012669: goto L_0x00dd;
                case -356037306: goto L_0x00d1;
                case 62923557: goto L_0x00c5;
                case 62923603: goto L_0x00b9;
                case 62927045: goto L_0x00ad;
                case 82338133: goto L_0x00a2;
                case 82338134: goto L_0x0097;
                case 99146302: goto L_0x008b;
                case 444813526: goto L_0x007f;
                case 542569478: goto L_0x0073;
                case 725957860: goto L_0x0067;
                case 738597099: goto L_0x005b;
                case 855502857: goto L_0x0050;
                case 1422270023: goto L_0x0044;
                case 1809237540: goto L_0x0039;
                case 1950749482: goto L_0x002d;
                case 1950789798: goto L_0x0021;
                case 1951062397: goto L_0x0015;
                default: goto L_0x0013;
            };
        L_0x0013:
            goto L_0x0160;
        L_0x0015:
            r2 = "A_OPUS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x001d:
            r1 = 11;
            goto L_0x0161;
        L_0x0021:
            r2 = "A_FLAC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0029:
            r1 = 21;
            goto L_0x0161;
        L_0x002d:
            r2 = "A_EAC3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0035:
            r1 = 16;
            goto L_0x0161;
        L_0x0039:
            r2 = "V_MPEG2";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0041:
            r1 = 2;
            goto L_0x0161;
        L_0x0044:
            r2 = "S_TEXT/UTF8";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x004c:
            r1 = 24;
            goto L_0x0161;
        L_0x0050:
            r2 = "V_MPEGH/ISO/HEVC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0058:
            r1 = 7;
            goto L_0x0161;
        L_0x005b:
            r2 = "S_TEXT/ASS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0063:
            r1 = 25;
            goto L_0x0161;
        L_0x0067:
            r2 = "A_PCM/INT/LIT";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x006f:
            r1 = 23;
            goto L_0x0161;
        L_0x0073:
            r2 = "A_DTS/EXPRESS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x007b:
            r1 = 19;
            goto L_0x0161;
        L_0x007f:
            r2 = "V_THEORA";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0087:
            r1 = 9;
            goto L_0x0161;
        L_0x008b:
            r2 = "S_HDMV/PGS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0093:
            r1 = 27;
            goto L_0x0161;
        L_0x0097:
            r2 = "V_VP9";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x009f:
            r1 = 1;
            goto L_0x0161;
        L_0x00a2:
            r2 = "V_VP8";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00aa:
            r1 = 0;
            goto L_0x0161;
        L_0x00ad:
            r2 = "A_DTS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00b5:
            r1 = 18;
            goto L_0x0161;
        L_0x00b9:
            r2 = "A_AC3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00c1:
            r1 = 15;
            goto L_0x0161;
        L_0x00c5:
            r2 = "A_AAC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00cd:
            r1 = 12;
            goto L_0x0161;
        L_0x00d1:
            r2 = "A_DTS/LOSSLESS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00d9:
            r1 = 20;
            goto L_0x0161;
        L_0x00dd:
            r2 = "S_VOBSUB";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00e5:
            r1 = 26;
            goto L_0x0161;
        L_0x00e9:
            r2 = "V_MPEG4/ISO/AVC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00f1:
            r1 = 6;
            goto L_0x0161;
        L_0x00f4:
            r2 = "V_MPEG4/ISO/ASP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x00fc:
            r1 = 4;
            goto L_0x0161;
        L_0x00ff:
            r2 = "S_DVBSUB";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0107:
            r1 = 28;
            goto L_0x0161;
        L_0x010a:
            r2 = "V_MS/VFW/FOURCC";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0112:
            r1 = 8;
            goto L_0x0161;
        L_0x0115:
            r2 = "A_MPEG/L3";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x011d:
            r1 = 14;
            goto L_0x0161;
        L_0x0120:
            r2 = "A_MPEG/L2";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0128:
            r1 = 13;
            goto L_0x0161;
        L_0x012b:
            r2 = "A_VORBIS";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0133:
            r1 = 10;
            goto L_0x0161;
        L_0x0136:
            r2 = "A_TRUEHD";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x013e:
            r1 = 17;
            goto L_0x0161;
        L_0x0141:
            r2 = "A_MS/ACM";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0149:
            r1 = 22;
            goto L_0x0161;
        L_0x014c:
            r2 = "V_MPEG4/ISO/SP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x0154:
            r1 = 3;
            goto L_0x0161;
        L_0x0156:
            r2 = "V_MPEG4/ISO/AP";
            r1 = r1.equals(r2);
            if (r1 == 0) goto L_0x0160;
        L_0x015e:
            r1 = 5;
            goto L_0x0161;
        L_0x0160:
            r1 = -1;
        L_0x0161:
            r2 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r10 = 0;
            switch(r1) {
                case 0: goto L_0x0324;
                case 1: goto L_0x0321;
                case 2: goto L_0x031e;
                case 3: goto L_0x030d;
                case 4: goto L_0x030d;
                case 5: goto L_0x030d;
                case 6: goto L_0x02f7;
                case 7: goto L_0x02e3;
                case 8: goto L_0x02c4;
                case 9: goto L_0x02c1;
                case 10: goto L_0x02b1;
                case 11: goto L_0x026b;
                case 12: goto L_0x0260;
                case 13: goto L_0x0258;
                case 14: goto L_0x0255;
                case 15: goto L_0x0251;
                case 16: goto L_0x024d;
                case 17: goto L_0x0242;
                case 18: goto L_0x023e;
                case 19: goto L_0x023e;
                case 20: goto L_0x023a;
                case 21: goto L_0x0231;
                case 22: goto L_0x01dc;
                case 23: goto L_0x01a7;
                case 24: goto L_0x01a3;
                case 25: goto L_0x019f;
                case 26: goto L_0x0195;
                case 27: goto L_0x0191;
                case 28: goto L_0x016f;
                default: goto L_0x0167;
            };
        L_0x0167:
            r1 = new com.google.android.exoplayer2.ParserException;
            r2 = "Unrecognized codec identifier.";
            r1.<init>(r2);
            throw r1;
        L_0x016f:
            r1 = "application/dvbsubs";
            r2 = new byte[r3];
            r3 = r0.codecPrivate;
            r3 = r3[r6];
            r2[r6] = r3;
            r3 = r0.codecPrivate;
            r3 = r3[r5];
            r2[r5] = r3;
            r3 = r0.codecPrivate;
            r3 = r3[r7];
            r2[r7] = r3;
            r3 = r0.codecPrivate;
            r3 = r3[r8];
            r2[r8] = r3;
            r2 = java.util.Collections.singletonList(r2);
            goto L_0x0268;
        L_0x0191:
            r1 = "application/pgs";
            goto L_0x0326;
        L_0x0195:
            r1 = "application/vobsub";
            r2 = r0.codecPrivate;
            r2 = java.util.Collections.singletonList(r2);
            goto L_0x0268;
        L_0x019f:
            r1 = "text/x-ssa";
            goto L_0x0326;
        L_0x01a3:
            r1 = "application/x-subrip";
            goto L_0x0326;
        L_0x01a7:
            r1 = "audio/raw";
            r2 = r0.audioBitDepth;
            r2 = com.google.android.exoplayer2.util.Util.getPcmEncoding(r2);
            if (r2 != 0) goto L_0x01d5;
        L_0x01b1:
            r1 = "audio/x-unknown";
            r2 = "MatroskaExtractor";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Unsupported PCM bit depth: ";
            r3.append(r4);
            r4 = r0.audioBitDepth;
            r3.append(r4);
            r4 = ". Setting mimeType to ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.w(r2, r3);
            goto L_0x0326;
        L_0x01d5:
            r12 = r1;
            r18 = r2;
            r2 = r10;
            r15 = -1;
            goto L_0x032b;
        L_0x01dc:
            r1 = "audio/raw";
            r2 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r2.<init>(r3);
            r2 = parseMsAcmCodecPrivate(r2);
            if (r2 == 0) goto L_0x0217;
        L_0x01eb:
            r2 = r0.audioBitDepth;
            r2 = com.google.android.exoplayer2.util.Util.getPcmEncoding(r2);
            if (r2 != 0) goto L_0x01d5;
        L_0x01f3:
            r1 = "audio/x-unknown";
            r2 = "MatroskaExtractor";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Unsupported PCM bit depth: ";
            r3.append(r4);
            r4 = r0.audioBitDepth;
            r3.append(r4);
            r4 = ". Setting mimeType to ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.w(r2, r3);
            goto L_0x0326;
        L_0x0217:
            r1 = "audio/x-unknown";
            r2 = "MatroskaExtractor";
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "Non-PCM MS/ACM is unsupported. Setting mimeType to ";
            r3.append(r4);
            r3.append(r1);
            r3 = r3.toString();
            android.util.Log.w(r2, r3);
            goto L_0x0326;
        L_0x0231:
            r1 = "audio/flac";
            r2 = r0.codecPrivate;
            r2 = java.util.Collections.singletonList(r2);
            goto L_0x0268;
        L_0x023a:
            r1 = "audio/vnd.dts.hd";
            goto L_0x0326;
        L_0x023e:
            r1 = "audio/vnd.dts";
            goto L_0x0326;
        L_0x0242:
            r1 = "audio/true-hd";
            r2 = new com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor$TrueHdSampleRechunker;
            r2.<init>();
            r0.trueHdSampleRechunker = r2;
            goto L_0x0326;
        L_0x024d:
            r1 = "audio/eac3";
            goto L_0x0326;
        L_0x0251:
            r1 = "audio/ac3";
            goto L_0x0326;
        L_0x0255:
            r1 = "audio/mpeg";
            goto L_0x025a;
        L_0x0258:
            r1 = "audio/mpeg-L2";
        L_0x025a:
            r12 = r1;
            r2 = r10;
            r15 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            goto L_0x0329;
        L_0x0260:
            r1 = "audio/mp4a-latm";
            r2 = r0.codecPrivate;
            r2 = java.util.Collections.singletonList(r2);
        L_0x0268:
            r12 = r1;
            goto L_0x0328;
        L_0x026b:
            r1 = "audio/opus";
            r2 = 5760; // 0x1680 float:8.071E-42 double:2.846E-320;
            r3 = new java.util.ArrayList;
            r3.<init>(r8);
            r11 = r0.codecPrivate;
            r3.add(r11);
            r11 = java.nio.ByteBuffer.allocate(r4);
            r12 = java.nio.ByteOrder.nativeOrder();
            r11 = r11.order(r12);
            r12 = r0.codecDelayNs;
            r11 = r11.putLong(r12);
            r11 = r11.array();
            r3.add(r11);
            r4 = java.nio.ByteBuffer.allocate(r4);
            r11 = java.nio.ByteOrder.nativeOrder();
            r4 = r4.order(r11);
            r11 = r0.seekPreRollNs;
            r4 = r4.putLong(r11);
            r4 = r4.array();
            r3.add(r4);
            r12 = r1;
            r2 = r3;
            r15 = 5760; // 0x1680 float:8.071E-42 double:2.846E-320;
            goto L_0x0329;
        L_0x02b1:
            r1 = "audio/vorbis";
            r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            r3 = r0.codecPrivate;
            r3 = parseVorbisCodecPrivate(r3);
            r12 = r1;
            r2 = r3;
            r15 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
            goto L_0x0329;
        L_0x02c1:
            r1 = "video/x-unknown";
            goto L_0x0326;
        L_0x02c4:
            r1 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r2 = r0.codecPrivate;
            r1.<init>(r2);
            r1 = parseFourCcVc1Private(r1);
            if (r1 == 0) goto L_0x02d9;
        L_0x02d1:
            r2 = "video/wvc1";
        L_0x02d3:
            r12 = r2;
            r15 = -1;
            r18 = -1;
            r2 = r1;
            goto L_0x032b;
        L_0x02d9:
            r2 = "MatroskaExtractor";
            r3 = "Unsupported FourCC. Setting mimeType to video/x-unknown";
            android.util.Log.w(r2, r3);
            r2 = "video/x-unknown";
            goto L_0x02d3;
        L_0x02e3:
            r1 = "video/hevc";
            r2 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r2.<init>(r3);
            r2 = com.google.android.exoplayer2.video.HevcConfig.parse(r2);
            r3 = r2.initializationData;
            r2 = r2.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r2;
            goto L_0x030a;
        L_0x02f7:
            r1 = "video/avc";
            r2 = new com.google.android.exoplayer2.util.ParsableByteArray;
            r3 = r0.codecPrivate;
            r2.<init>(r3);
            r2 = com.google.android.exoplayer2.video.AvcConfig.parse(r2);
            r3 = r2.initializationData;
            r2 = r2.nalUnitLengthFieldLength;
            r0.nalUnitLengthFieldLength = r2;
        L_0x030a:
            r12 = r1;
            r2 = r3;
            goto L_0x0328;
        L_0x030d:
            r1 = "video/mp4v-es";
            r2 = r0.codecPrivate;
            if (r2 != 0) goto L_0x0316;
        L_0x0313:
            r2 = r10;
            goto L_0x0268;
        L_0x0316:
            r2 = r0.codecPrivate;
            r2 = java.util.Collections.singletonList(r2);
            goto L_0x0268;
        L_0x031e:
            r1 = "video/mpeg2";
            goto L_0x0326;
        L_0x0321:
            r1 = "video/x-vnd.on2.vp9";
            goto L_0x0326;
        L_0x0324:
            r1 = "video/x-vnd.on2.vp8";
        L_0x0326:
            r12 = r1;
            r2 = r10;
        L_0x0328:
            r15 = -1;
        L_0x0329:
            r18 = -1;
        L_0x032b:
            r1 = r0.flagDefault;
            r1 = r1 | r6;
            r3 = r0.flagForced;
            if (r3 == 0) goto L_0x0333;
        L_0x0332:
            r6 = 2;
        L_0x0333:
            r1 = r1 | r6;
            r3 = com.google.android.exoplayer2.util.MimeTypes.isAudio(r12);
            if (r3 == 0) goto L_0x035b;
        L_0x033a:
            r11 = java.lang.Integer.toString(r28);
            r13 = 0;
            r14 = -1;
            r3 = r0.channelCount;
            r4 = r0.sampleRate;
            r6 = r0.drmInitData;
            r7 = r0.language;
            r16 = r3;
            r17 = r4;
            r19 = r2;
            r20 = r6;
            r21 = r1;
            r22 = r7;
            r1 = com.google.android.exoplayer2.Format.createAudioSampleFormat(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22);
            r8 = 1;
            goto L_0x0457;
        L_0x035b:
            r3 = com.google.android.exoplayer2.util.MimeTypes.isVideo(r12);
            if (r3 == 0) goto L_0x03d5;
        L_0x0361:
            r1 = r0.displayUnit;
            if (r1 != 0) goto L_0x037b;
        L_0x0365:
            r1 = r0.displayWidth;
            if (r1 != r9) goto L_0x036c;
        L_0x0369:
            r1 = r0.width;
            goto L_0x036e;
        L_0x036c:
            r1 = r0.displayWidth;
        L_0x036e:
            r0.displayWidth = r1;
            r1 = r0.displayHeight;
            if (r1 != r9) goto L_0x0377;
        L_0x0374:
            r1 = r0.height;
            goto L_0x0379;
        L_0x0377:
            r1 = r0.displayHeight;
        L_0x0379:
            r0.displayHeight = r1;
        L_0x037b:
            r1 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r3 = r0.displayWidth;
            if (r3 == r9) goto L_0x0397;
        L_0x0381:
            r3 = r0.displayHeight;
            if (r3 == r9) goto L_0x0397;
        L_0x0385:
            r1 = r0.height;
            r3 = r0.displayWidth;
            r1 = r1 * r3;
            r1 = (float) r1;
            r3 = r0.width;
            r4 = r0.displayHeight;
            r3 = r3 * r4;
            r3 = (float) r3;
            r1 = r1 / r3;
            r21 = r1;
            goto L_0x0399;
        L_0x0397:
            r21 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        L_0x0399:
            r1 = r0.hasColorInfo;
            if (r1 == 0) goto L_0x03ac;
        L_0x039d:
            r1 = r26.getHdrStaticInfo();
            r10 = new com.google.android.exoplayer2.video.ColorInfo;
            r3 = r0.colorSpace;
            r4 = r0.colorRange;
            r5 = r0.colorTransfer;
            r10.<init>(r3, r4, r5, r1);
        L_0x03ac:
            r24 = r10;
            r11 = java.lang.Integer.toString(r28);
            r13 = 0;
            r14 = -1;
            r1 = r0.width;
            r3 = r0.height;
            r18 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
            r20 = -1;
            r4 = r0.projectionData;
            r5 = r0.stereoMode;
            r6 = r0.drmInitData;
            r16 = r1;
            r17 = r3;
            r19 = r2;
            r22 = r4;
            r23 = r5;
            r25 = r6;
            r1 = com.google.android.exoplayer2.Format.createVideoSampleFormat(r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25);
            r8 = 2;
            goto L_0x0457;
        L_0x03d5:
            r3 = "application/x-subrip";
            r3 = r3.equals(r12);
            if (r3 == 0) goto L_0x03eb;
        L_0x03dd:
            r2 = java.lang.Integer.toString(r28);
            r3 = r0.language;
            r4 = r0.drmInitData;
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(r2, r12, r1, r3, r4);
            goto L_0x0457;
        L_0x03eb:
            r3 = "text/x-ssa";
            r3 = r3.equals(r12);
            if (r3 == 0) goto L_0x0421;
        L_0x03f3:
            r2 = new java.util.ArrayList;
            r2.<init>(r7);
            r3 = com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.SSA_DIALOGUE_FORMAT;
            r2.add(r3);
            r3 = r0.codecPrivate;
            r2.add(r3);
            r11 = java.lang.Integer.toString(r28);
            r13 = 0;
            r14 = -1;
            r3 = r0.language;
            r17 = -1;
            r4 = r0.drmInitData;
            r19 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
            r15 = r1;
            r16 = r3;
            r18 = r4;
            r21 = r2;
            r1 = com.google.android.exoplayer2.Format.createTextSampleFormat(r11, r12, r13, r14, r15, r16, r17, r18, r19, r21);
            goto L_0x0457;
        L_0x0421:
            r3 = "application/vobsub";
            r3 = r3.equals(r12);
            if (r3 != 0) goto L_0x0442;
        L_0x0429:
            r3 = "application/pgs";
            r3 = r3.equals(r12);
            if (r3 != 0) goto L_0x0442;
        L_0x0431:
            r3 = "application/dvbsubs";
            r3 = r3.equals(r12);
            if (r3 == 0) goto L_0x043a;
        L_0x0439:
            goto L_0x0442;
        L_0x043a:
            r1 = new com.google.android.exoplayer2.ParserException;
            r2 = "Unexpected MIME type.";
            r1.<init>(r2);
            throw r1;
        L_0x0442:
            r11 = java.lang.Integer.toString(r28);
            r13 = 0;
            r14 = -1;
            r3 = r0.language;
            r4 = r0.drmInitData;
            r15 = r1;
            r16 = r2;
            r17 = r3;
            r18 = r4;
            r1 = com.google.android.exoplayer2.Format.createImageSampleFormat(r11, r12, r13, r14, r15, r16, r17, r18);
        L_0x0457:
            r2 = r0.number;
            r3 = r27;
            r2 = r3.track(r2, r8);
            r0.output = r2;
            r2 = r0.output;
            r2.format(r1);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track.initializeOutput(com.google.android.exoplayer2.extractor.ExtractorOutput, int):void");
        }

        public void outputPendingSampleMetadata() {
            if (this.trueHdSampleRechunker != null) {
                this.trueHdSampleRechunker.outputPendingSampleMetadata(this);
            }
        }

        public void reset() {
            if (this.trueHdSampleRechunker != null) {
                this.trueHdSampleRechunker.reset();
            }
        }

        private byte[] getHdrStaticInfo() {
            if (!(this.primaryRChromaticityX == -1.0f || this.primaryRChromaticityY == -1.0f || this.primaryGChromaticityX == -1.0f || this.primaryGChromaticityY == -1.0f || this.primaryBChromaticityX == -1.0f || this.primaryBChromaticityY == -1.0f || this.whitePointChromaticityX == -1.0f || this.whitePointChromaticityY == -1.0f || this.maxMasteringLuminance == -1.0f)) {
                if (this.minMasteringLuminance != -1.0f) {
                    byte[] bArr = new byte[25];
                    ByteBuffer wrap = ByteBuffer.wrap(bArr);
                    wrap.put((byte) 0);
                    wrap.putShort((short) ((int) ((this.primaryRChromaticityX * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.primaryRChromaticityY * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.primaryGChromaticityX * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.primaryGChromaticityY * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.primaryBChromaticityX * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.primaryBChromaticityY * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.whitePointChromaticityX * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) ((this.whitePointChromaticityY * 50000.0f) + 0.5f)));
                    wrap.putShort((short) ((int) (this.maxMasteringLuminance + 0.5f)));
                    wrap.putShort((short) ((int) (this.minMasteringLuminance + 0.5f)));
                    wrap.putShort((short) this.maxContentLuminance);
                    wrap.putShort((short) this.maxFrameAverageLuminance);
                    return bArr;
                }
            }
            return null;
        }

        private static java.util.List<byte[]> parseFourCcVc1Private(com.google.android.exoplayer2.util.ParsableByteArray r5) throws com.google.android.exoplayer2.ParserException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = 16;
            r5.skipBytes(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r0 = r5.readLittleEndianUnsignedInt();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r2 = 826496599; // 0x31435657 float:2.8425313E-9 double:4.08343576E-315;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r4 == 0) goto L_0x0012;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0010:
            r5 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            return r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0012:
            r0 = r5.getPosition();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r0 = r0 + 20;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r5 = r5.data;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x001a:
            r1 = r5.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r1 = r1 + -4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r0 >= r1) goto L_0x0045;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x001f:
            r1 = r5[r0];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r1 != 0) goto L_0x0042;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0023:
            r1 = r0 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r1 = r5[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r1 != 0) goto L_0x0042;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0029:
            r1 = r0 + 2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r1 = r5[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r2 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r1 != r2) goto L_0x0042;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0030:
            r1 = r0 + 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r1 = r5[r1];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r2 = 15;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            if (r1 != r2) goto L_0x0042;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0038:
            r1 = r5.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r5 = java.util.Arrays.copyOfRange(r5, r0, r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r5 = java.util.Collections.singletonList(r5);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            return r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0042:
            r0 = r0 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            goto L_0x001a;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x0045:
            r5 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r0 = "Failed to find FourCC VC1 initialization data";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            r5.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
            throw r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x004d }
        L_0x004d:
            r5 = new com.google.android.exoplayer2.ParserException;
            r0 = "Error parsing FourCC VC1 codec private";
            r5.<init>(r0);
            throw r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track.parseFourCcVc1Private(com.google.android.exoplayer2.util.ParsableByteArray):java.util.List<byte[]>");
        }

        private static java.util.List<byte[]> parseVorbisCodecPrivate(byte[] r8) throws com.google.android.exoplayer2.ParserException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = 0;
            r1 = r8[r0];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r2 = 2;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r1 == r2) goto L_0x000e;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0006:
            r8 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "Error parsing vorbis codec private";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            throw r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x000e:
            r1 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0011:
            r5 = r8[r3];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = -1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r5 != r6) goto L_0x001b;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0016:
            r4 = r4 + 255;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = r3 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            goto L_0x0011;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x001b:
            r5 = r3 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = r8[r3];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = r4 + r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0021:
            r7 = r8[r5];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r7 != r6) goto L_0x002a;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0025:
            r3 = r3 + 255;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = r5 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            goto L_0x0021;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x002a:
            r6 = r5 + 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = r8[r5];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = r3 + r5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = r8[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r5 == r1) goto L_0x003b;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0033:
            r8 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "Error parsing vorbis codec private";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            throw r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x003b:
            r1 = new byte[r4];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            java.lang.System.arraycopy(r8, r6, r1, r0, r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r6 = r6 + r4;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = r8[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r5 = 3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r4 == r5) goto L_0x004e;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0046:
            r8 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "Error parsing vorbis codec private";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            throw r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x004e:
            r6 = r6 + r3;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = r8[r6];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = 5;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            if (r3 == r4) goto L_0x005c;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x0054:
            r8 = new com.google.android.exoplayer2.ParserException;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r0 = "Error parsing vorbis codec private";	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.<init>(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            throw r8;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
        L_0x005c:
            r3 = r8.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = r3 - r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r3 = new byte[r3];	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = r8.length;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r4 = r4 - r6;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            java.lang.System.arraycopy(r8, r6, r3, r0, r4);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8 = new java.util.ArrayList;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.<init>(r2);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.add(r1);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            r8.add(r3);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0071 }
            return r8;
        L_0x0071:
            r8 = new com.google.android.exoplayer2.ParserException;
            r0 = "Error parsing vorbis codec private";
            r8.<init>(r0);
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track.parseVorbisCodecPrivate(byte[]):java.util.List<byte[]>");
        }

        private static boolean parseMsAcmCodecPrivate(com.google.android.exoplayer2.util.ParsableByteArray r8) throws com.google.android.exoplayer2.ParserException {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = r8.readLittleEndianUnsignedShort();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r1 = 1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            if (r0 != r1) goto L_0x0008;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
        L_0x0007:
            return r1;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
        L_0x0008:
            r2 = 65534; // 0xfffe float:9.1833E-41 double:3.2378E-319;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r3 = 0;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            if (r0 != r2) goto L_0x0036;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
        L_0x000e:
            r0 = 24;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r8.setPosition(r0);	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r4 = r8.readLong();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r0 = com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.WAVE_SUBFORMAT_PCM;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r6 = r0.getMostSignificantBits();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            if (r0 != 0) goto L_0x0034;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
        L_0x0023:
            r4 = r8.readLong();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r8 = com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.WAVE_SUBFORMAT_PCM;	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r6 = r8.getLeastSignificantBits();	 Catch:{ ArrayIndexOutOfBoundsException -> 0x0037 }
            r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
            if (r8 != 0) goto L_0x0034;
        L_0x0033:
            goto L_0x0035;
        L_0x0034:
            r1 = 0;
        L_0x0035:
            return r1;
        L_0x0036:
            return r3;
        L_0x0037:
            r8 = new com.google.android.exoplayer2.ParserException;
            r0 = "Error parsing MS/ACM codec private";
            r8.<init>(r0);
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.Track.parseMsAcmCodecPrivate(com.google.android.exoplayer2.util.ParsableByteArray):boolean");
        }
    }

    private static final class TrueHdSampleRechunker {
        private int blockFlags;
        private int chunkSize;
        private boolean foundSyncframe;
        private int sampleCount;
        private final byte[] syncframePrefix = new byte[12];
        private long timeUs;

        public void reset() {
            this.foundSyncframe = false;
        }

        public void startSample(ExtractorInput extractorInput, int i, int i2) throws IOException, InterruptedException {
            if (!this.foundSyncframe) {
                extractorInput.peekFully(this.syncframePrefix, 0, 12);
                extractorInput.resetPeekPosition();
                if (Ac3Util.parseTrueHdSyncframeAudioSampleCount(this.syncframePrefix) != -1) {
                    this.foundSyncframe = true;
                    this.sampleCount = 0;
                } else {
                    return;
                }
            }
            if (this.sampleCount == null) {
                this.blockFlags = i;
                this.chunkSize = 0;
            }
            this.chunkSize += i2;
        }

        public void sampleMetadata(Track track, long j) {
            if (this.foundSyncframe) {
                int i = this.sampleCount;
                this.sampleCount = i + 1;
                if (i == 0) {
                    this.timeUs = j;
                }
                if (this.sampleCount >= 8) {
                    track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                    this.sampleCount = null;
                }
            }
        }

        public void outputPendingSampleMetadata(Track track) {
            if (this.foundSyncframe && this.sampleCount > 0) {
                track.output.sampleMetadata(this.timeUs, this.blockFlags, this.chunkSize, 0, track.cryptoData);
                this.sampleCount = null;
            }
        }
    }

    int getElementType(int i) {
        switch (i) {
            case ID_TRACK_TYPE /*131*/:
            case ID_FLAG_DEFAULT /*136*/:
            case ID_BLOCK_DURATION /*155*/:
            case 159:
            case 176:
            case ID_CUE_TIME /*179*/:
            case ID_PIXEL_HEIGHT /*186*/:
            case ID_TRACK_NUMBER /*215*/:
            case ID_TIME_CODE /*231*/:
            case 241:
            case ID_REFERENCE_BLOCK /*251*/:
            case ID_CONTENT_COMPRESSION_ALGORITHM /*16980*/:
            case ID_DOC_TYPE_READ_VERSION /*17029*/:
            case ID_EBML_READ_VERSION /*17143*/:
            case ID_CONTENT_ENCRYPTION_ALGORITHM /*18401*/:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /*18408*/:
            case ID_CONTENT_ENCODING_ORDER /*20529*/:
            case ID_CONTENT_ENCODING_SCOPE /*20530*/:
            case ID_SEEK_POSITION /*21420*/:
            case ID_STEREO_MODE /*21432*/:
            case ID_DISPLAY_WIDTH /*21680*/:
            case ID_DISPLAY_UNIT /*21682*/:
            case ID_DISPLAY_HEIGHT /*21690*/:
            case ID_FLAG_FORCED /*21930*/:
            case ID_COLOUR_RANGE /*21945*/:
            case ID_COLOUR_TRANSFER /*21946*/:
            case ID_COLOUR_PRIMARIES /*21947*/:
            case ID_MAX_CLL /*21948*/:
            case ID_MAX_FALL /*21949*/:
            case ID_CODEC_DELAY /*22186*/:
            case ID_SEEK_PRE_ROLL /*22203*/:
            case ID_AUDIO_BIT_DEPTH /*25188*/:
            case ID_DEFAULT_DURATION /*2352003*/:
            case ID_TIMECODE_SCALE /*2807729*/:
                return 2;
            case 134:
            case ID_DOC_TYPE /*17026*/:
            case ID_LANGUAGE /*2274716*/:
                return 3;
            case ID_BLOCK_GROUP /*160*/:
            case ID_TRACK_ENTRY /*174*/:
            case ID_CUE_TRACK_POSITIONS /*183*/:
            case ID_CUE_POINT /*187*/:
            case 224:
            case ID_AUDIO /*225*/:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS /*18407*/:
            case ID_SEEK /*19899*/:
            case ID_CONTENT_COMPRESSION /*20532*/:
            case ID_CONTENT_ENCRYPTION /*20533*/:
            case ID_COLOUR /*21936*/:
            case ID_MASTERING_METADATA /*21968*/:
            case ID_CONTENT_ENCODING /*25152*/:
            case ID_CONTENT_ENCODINGS /*28032*/:
            case ID_PROJECTION /*30320*/:
            case ID_SEEK_HEAD /*290298740*/:
            case 357149030:
            case ID_TRACKS /*374648427*/:
            case ID_SEGMENT /*408125543*/:
            case ID_EBML /*440786851*/:
            case ID_CUES /*475249515*/:
            case ID_CLUSTER /*524531317*/:
                return 1;
            case 161:
            case ID_SIMPLE_BLOCK /*163*/:
            case ID_CONTENT_COMPRESSION_SETTINGS /*16981*/:
            case ID_CONTENT_ENCRYPTION_KEY_ID /*18402*/:
            case ID_SEEK_ID /*21419*/:
            case ID_CODEC_PRIVATE /*25506*/:
            case ID_PROJECTION_PRIVATE /*30322*/:
                return 4;
            case ID_SAMPLING_FREQUENCY /*181*/:
            case ID_DURATION /*17545*/:
            case ID_PRIMARY_R_CHROMATICITY_X /*21969*/:
            case ID_PRIMARY_R_CHROMATICITY_Y /*21970*/:
            case ID_PRIMARY_G_CHROMATICITY_X /*21971*/:
            case ID_PRIMARY_G_CHROMATICITY_Y /*21972*/:
            case ID_PRIMARY_B_CHROMATICITY_X /*21973*/:
            case ID_PRIMARY_B_CHROMATICITY_Y /*21974*/:
            case ID_WHITE_POINT_CHROMATICITY_X /*21975*/:
            case ID_WHITE_POINT_CHROMATICITY_Y /*21976*/:
            case ID_LUMNINANCE_MAX /*21977*/:
            case ID_LUMNINANCE_MIN /*21978*/:
                return 5;
            default:
                return 0;
        }
    }

    boolean isLevel1Element(int i) {
        if (!(i == 357149030 || i == ID_CLUSTER || i == ID_CUES)) {
            if (i != ID_TRACKS) {
                return false;
            }
        }
        return true;
    }

    public void release() {
    }

    public MatroskaExtractor() {
        this(0);
    }

    public MatroskaExtractor(int i) {
        this(new DefaultEbmlReader(), i);
    }

    MatroskaExtractor(EbmlReader ebmlReader, int i) {
        this.segmentContentPosition = -1;
        this.timecodeScale = C0649C.TIME_UNSET;
        this.durationTimecode = C0649C.TIME_UNSET;
        this.durationUs = C0649C.TIME_UNSET;
        this.cuesContentPosition = -1;
        this.seekPositionAfterBuildingCues = -1;
        this.clusterTimecodeUs = C0649C.TIME_UNSET;
        this.reader = ebmlReader;
        this.reader.init(new InnerEbmlReaderOutput());
        ebmlReader = true;
        if ((i & 1) != 0) {
            ebmlReader = null;
        }
        this.seekForCuesEnabled = ebmlReader;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subtitleSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
    }

    public boolean sniff(ExtractorInput extractorInput) throws IOException, InterruptedException {
        return new Sniffer().sniff(extractorInput);
    }

    public void init(ExtractorOutput extractorOutput) {
        this.extractorOutput = extractorOutput;
    }

    public void seek(long j, long j2) {
        this.clusterTimecodeUs = C0649C.TIME_UNSET;
        j = null;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetSample();
        while (j < this.tracks.size()) {
            ((Track) this.tracks.valueAt(j)).reset();
            j++;
        }
    }

    public int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws IOException, InterruptedException {
        int i = 0;
        this.sampleRead = false;
        boolean z = true;
        while (z && !this.sampleRead) {
            z = this.reader.read(extractorInput);
            if (z && maybeSeekForCues(positionHolder, extractorInput.getPosition())) {
                return 1;
            }
        }
        if (z) {
            return 0;
        }
        while (i < this.tracks.size()) {
            ((Track) this.tracks.valueAt(i)).outputPendingSampleMetadata();
            i++;
        }
        return -1;
    }

    void startMasterElement(int i, long j, long j2) throws ParserException {
        if (i == ID_BLOCK_GROUP) {
            this.sampleSeenReferenceBlock = false;
        } else if (i == ID_TRACK_ENTRY) {
            this.currentTrack = new Track();
        } else if (i == ID_CUE_POINT) {
            this.seenClusterPositionForCurrentCuePoint = false;
        } else if (i == ID_SEEK) {
            this.seekEntryId = -1;
            this.seekEntryPosition = -1;
        } else if (i == ID_CONTENT_ENCRYPTION) {
            this.currentTrack.hasContentEncryption = true;
        } else if (i == ID_MASTERING_METADATA) {
            this.currentTrack.hasColorInfo = true;
        } else if (i == ID_CONTENT_ENCODING) {
        } else {
            if (i != ID_SEGMENT) {
                if (i == 475249515) {
                    this.cueTimesUs = new LongArray();
                    this.cueClusterPositions = new LongArray();
                } else if (i == 524531317) {
                    if (this.sentSeekMap != 0) {
                        return;
                    }
                    if (this.seekForCuesEnabled == 0 || this.cuesContentPosition == -1) {
                        this.extractorOutput.seekMap(new Unseekable(this.durationUs));
                        this.sentSeekMap = true;
                        return;
                    }
                    this.seekForCues = true;
                }
            } else if (this.segmentContentPosition == -1 || this.segmentContentPosition == j) {
                this.segmentContentPosition = j;
                this.segmentContentSize = j2;
            } else {
                throw new ParserException("Multiple Segment elements not supported");
            }
        }
    }

    void endMasterElement(int i) throws ParserException {
        if (i != ID_BLOCK_GROUP) {
            if (i == ID_TRACK_ENTRY) {
                if (isCodecSupported(this.currentTrack.codecId) != 0) {
                    this.currentTrack.initializeOutput(this.extractorOutput, this.currentTrack.number);
                    this.tracks.put(this.currentTrack.number, this.currentTrack);
                }
                this.currentTrack = 0;
            } else if (i == ID_SEEK) {
                if (this.seekEntryId != -1) {
                    if (this.seekEntryPosition != -1) {
                        if (this.seekEntryId == ID_CUES) {
                            this.cuesContentPosition = this.seekEntryPosition;
                        }
                    }
                }
                throw new ParserException("Mandatory element SeekID or SeekPosition not found");
            } else if (i != ID_CONTENT_ENCODING) {
                if (i != ID_CONTENT_ENCODINGS) {
                    if (i == 357149030) {
                        if (this.timecodeScale == C0649C.TIME_UNSET) {
                            this.timecodeScale = C0649C.MICROS_PER_SECOND;
                        }
                        if (this.durationTimecode != C0649C.TIME_UNSET) {
                            this.durationUs = scaleTimecodeToUs(this.durationTimecode);
                        }
                    } else if (i != ID_TRACKS) {
                        if (i == ID_CUES) {
                            if (this.sentSeekMap == 0) {
                                this.extractorOutput.seekMap(buildSeekMap());
                                this.sentSeekMap = true;
                            }
                        }
                    } else if (this.tracks.size() == 0) {
                        throw new ParserException("No valid tracks were found");
                    } else {
                        this.extractorOutput.endTracks();
                    }
                } else if (!(this.currentTrack.hasContentEncryption == 0 || this.currentTrack.sampleStrippedBytes == 0)) {
                    throw new ParserException("Combining encryption and compression is not supported");
                }
            } else if (this.currentTrack.hasContentEncryption != 0) {
                if (this.currentTrack.cryptoData == 0) {
                    throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                }
                this.currentTrack.drmInitData = new DrmInitData(new SchemeData(C0649C.UUID_NIL, MimeTypes.VIDEO_WEBM, this.currentTrack.cryptoData.encryptionKey));
            }
        } else if (this.blockState == 2) {
            if (this.sampleSeenReferenceBlock == 0) {
                this.blockFlags |= 1;
            }
            commitSampleToOutput((Track) this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
            this.blockState = 0;
        }
    }

    void integerElement(int i, long j) throws ParserException {
        boolean z = false;
        StringBuilder stringBuilder;
        switch (i) {
            case ID_TRACK_TYPE /*131*/:
                this.currentTrack.type = (int) j;
                return;
            case ID_FLAG_DEFAULT /*136*/:
                i = this.currentTrack;
                if (j == 1) {
                    z = true;
                }
                i.flagForced = z;
                return;
            case ID_BLOCK_DURATION /*155*/:
                this.blockDurationUs = scaleTimecodeToUs(j);
                return;
            case 159:
                this.currentTrack.channelCount = (int) j;
                return;
            case 176:
                this.currentTrack.width = (int) j;
                return;
            case ID_CUE_TIME /*179*/:
                this.cueTimesUs.add(scaleTimecodeToUs(j));
                return;
            case ID_PIXEL_HEIGHT /*186*/:
                this.currentTrack.height = (int) j;
                return;
            case ID_TRACK_NUMBER /*215*/:
                this.currentTrack.number = (int) j;
                return;
            case ID_TIME_CODE /*231*/:
                this.clusterTimecodeUs = scaleTimecodeToUs(j);
                return;
            case 241:
                if (this.seenClusterPositionForCurrentCuePoint == 0) {
                    this.cueClusterPositions.add(j);
                    this.seenClusterPositionForCurrentCuePoint = true;
                    return;
                }
                return;
            case ID_REFERENCE_BLOCK /*251*/:
                this.sampleSeenReferenceBlock = true;
                return;
            case ID_CONTENT_COMPRESSION_ALGORITHM /*16980*/:
                if (j != 3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentCompAlgo ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_DOC_TYPE_READ_VERSION /*17029*/:
                if (j < 1 || j > 2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("DocTypeReadVersion ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_EBML_READ_VERSION /*17143*/:
                if (j != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("EBMLReadVersion ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCRYPTION_ALGORITHM /*18401*/:
                if (j != 5) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncAlgo ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /*18408*/:
                if (j != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("AESSettingsCipherMode ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCODING_ORDER /*20529*/:
                if (j != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncodingOrder ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_CONTENT_ENCODING_SCOPE /*20530*/:
                if (j != 1) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("ContentEncodingScope ");
                    stringBuilder.append(j);
                    stringBuilder.append(" not supported");
                    throw new ParserException(stringBuilder.toString());
                }
                return;
            case ID_SEEK_POSITION /*21420*/:
                this.seekEntryPosition = j + this.segmentContentPosition;
                return;
            case ID_STEREO_MODE /*21432*/:
                i = (int) j;
                if (i == 3) {
                    this.currentTrack.stereoMode = 1;
                    return;
                } else if (i != 15) {
                    switch (i) {
                        case 0:
                            this.currentTrack.stereoMode = 0;
                            return;
                        case 1:
                            this.currentTrack.stereoMode = 2;
                            return;
                        default:
                            return;
                    }
                } else {
                    this.currentTrack.stereoMode = 3;
                    return;
                }
            case ID_DISPLAY_WIDTH /*21680*/:
                this.currentTrack.displayWidth = (int) j;
                return;
            case ID_DISPLAY_UNIT /*21682*/:
                this.currentTrack.displayUnit = (int) j;
                return;
            case ID_DISPLAY_HEIGHT /*21690*/:
                this.currentTrack.displayHeight = (int) j;
                return;
            case ID_FLAG_FORCED /*21930*/:
                i = this.currentTrack;
                if (j == 1) {
                    z = true;
                }
                i.flagDefault = z;
                return;
            case ID_COLOUR_RANGE /*21945*/:
                switch ((int) j) {
                    case 1:
                        this.currentTrack.colorRange = 2;
                        return;
                    case 2:
                        this.currentTrack.colorRange = 1;
                        return;
                    default:
                        return;
                }
            case ID_COLOUR_TRANSFER /*21946*/:
                i = (int) j;
                if (i != 1) {
                    if (i == 16) {
                        this.currentTrack.colorTransfer = 6;
                        return;
                    } else if (i != 18) {
                        switch (i) {
                            case 6:
                            case 7:
                                break;
                            default:
                                return;
                        }
                    } else {
                        this.currentTrack.colorTransfer = 7;
                        return;
                    }
                }
                this.currentTrack.colorTransfer = 3;
                return;
            case ID_COLOUR_PRIMARIES /*21947*/:
                this.currentTrack.hasColorInfo = true;
                i = (int) j;
                if (i == 1) {
                    this.currentTrack.colorSpace = 1;
                    return;
                } else if (i != 9) {
                    switch (i) {
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            this.currentTrack.colorSpace = 2;
                            return;
                        default:
                            return;
                    }
                } else {
                    this.currentTrack.colorSpace = 6;
                    return;
                }
            case ID_MAX_CLL /*21948*/:
                this.currentTrack.maxContentLuminance = (int) j;
                return;
            case ID_MAX_FALL /*21949*/:
                this.currentTrack.maxFrameAverageLuminance = (int) j;
                return;
            case ID_CODEC_DELAY /*22186*/:
                this.currentTrack.codecDelayNs = j;
                return;
            case ID_SEEK_PRE_ROLL /*22203*/:
                this.currentTrack.seekPreRollNs = j;
                return;
            case ID_AUDIO_BIT_DEPTH /*25188*/:
                this.currentTrack.audioBitDepth = (int) j;
                return;
            case ID_DEFAULT_DURATION /*2352003*/:
                this.currentTrack.defaultSampleDurationNs = (int) j;
                return;
            case ID_TIMECODE_SCALE /*2807729*/:
                this.timecodeScale = j;
                return;
            default:
                return;
        }
    }

    void floatElement(int i, double d) {
        if (i == ID_SAMPLING_FREQUENCY) {
            this.currentTrack.sampleRate = (int) d;
        } else if (i != ID_DURATION) {
            switch (i) {
                case ID_PRIMARY_R_CHROMATICITY_X /*21969*/:
                    this.currentTrack.primaryRChromaticityX = (float) d;
                    return;
                case ID_PRIMARY_R_CHROMATICITY_Y /*21970*/:
                    this.currentTrack.primaryRChromaticityY = (float) d;
                    return;
                case ID_PRIMARY_G_CHROMATICITY_X /*21971*/:
                    this.currentTrack.primaryGChromaticityX = (float) d;
                    return;
                case ID_PRIMARY_G_CHROMATICITY_Y /*21972*/:
                    this.currentTrack.primaryGChromaticityY = (float) d;
                    return;
                case ID_PRIMARY_B_CHROMATICITY_X /*21973*/:
                    this.currentTrack.primaryBChromaticityX = (float) d;
                    return;
                case ID_PRIMARY_B_CHROMATICITY_Y /*21974*/:
                    this.currentTrack.primaryBChromaticityY = (float) d;
                    return;
                case ID_WHITE_POINT_CHROMATICITY_X /*21975*/:
                    this.currentTrack.whitePointChromaticityX = (float) d;
                    return;
                case ID_WHITE_POINT_CHROMATICITY_Y /*21976*/:
                    this.currentTrack.whitePointChromaticityY = (float) d;
                    return;
                case ID_LUMNINANCE_MAX /*21977*/:
                    this.currentTrack.maxMasteringLuminance = (float) d;
                    return;
                case ID_LUMNINANCE_MIN /*21978*/:
                    this.currentTrack.minMasteringLuminance = (float) d;
                    return;
                default:
                    return;
            }
        } else {
            this.durationTimecode = (long) d;
        }
    }

    void stringElement(int i, String str) throws ParserException {
        if (i == 134) {
            this.currentTrack.codecId = str;
        } else if (i != ID_DOC_TYPE) {
            if (i == ID_LANGUAGE) {
                this.currentTrack.language = str;
            }
        } else if (DOC_TYPE_WEBM.equals(str) == 0 && DOC_TYPE_MATROSKA.equals(str) == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DocType ");
            stringBuilder.append(str);
            stringBuilder.append(" not supported");
            throw new ParserException(stringBuilder.toString());
        }
    }

    void binaryElement(int i, int i2, ExtractorInput extractorInput) throws IOException, InterruptedException {
        MatroskaExtractor matroskaExtractor = this;
        int i3 = i;
        int i4 = i2;
        ExtractorInput extractorInput2 = extractorInput;
        boolean z = false;
        int i5 = 1;
        if (i3 == 161 || i3 == ID_SIMPLE_BLOCK) {
            if (matroskaExtractor.blockState == 0) {
                matroskaExtractor.blockTrackNumber = (int) matroskaExtractor.varintReader.readUnsignedVarint(extractorInput2, false, true, 8);
                matroskaExtractor.blockTrackNumberLength = matroskaExtractor.varintReader.getLastLength();
                matroskaExtractor.blockDurationUs = C0649C.TIME_UNSET;
                matroskaExtractor.blockState = 1;
                matroskaExtractor.scratch.reset();
            }
            Track track = (Track) matroskaExtractor.tracks.get(matroskaExtractor.blockTrackNumber);
            if (track == null) {
                extractorInput2.skipFully(i4 - matroskaExtractor.blockTrackNumberLength);
                matroskaExtractor.blockState = 0;
                return;
            }
            if (matroskaExtractor.blockState == 1) {
                int i6;
                readScratch(extractorInput2, 3);
                int i7 = (matroskaExtractor.scratch.data[2] & 6) >> 1;
                int i8 = 255;
                if (i7 == 0) {
                    matroskaExtractor.blockLacingSampleCount = 1;
                    matroskaExtractor.blockLacingSampleSizes = ensureArrayCapacity(matroskaExtractor.blockLacingSampleSizes, 1);
                    matroskaExtractor.blockLacingSampleSizes[0] = (i4 - matroskaExtractor.blockTrackNumberLength) - 3;
                } else if (i3 != ID_SIMPLE_BLOCK) {
                    throw new ParserException("Lacing only supported in SimpleBlocks.");
                } else {
                    readScratch(extractorInput2, 4);
                    matroskaExtractor.blockLacingSampleCount = (matroskaExtractor.scratch.data[3] & 255) + 1;
                    matroskaExtractor.blockLacingSampleSizes = ensureArrayCapacity(matroskaExtractor.blockLacingSampleSizes, matroskaExtractor.blockLacingSampleCount);
                    if (i7 == 2) {
                        Arrays.fill(matroskaExtractor.blockLacingSampleSizes, 0, matroskaExtractor.blockLacingSampleCount, ((i4 - matroskaExtractor.blockTrackNumberLength) - 4) / matroskaExtractor.blockLacingSampleCount);
                    } else if (i7 == 1) {
                        r10 = 4;
                        i7 = 0;
                        for (r6 = 0; r6 < matroskaExtractor.blockLacingSampleCount - 1; r6++) {
                            matroskaExtractor.blockLacingSampleSizes[r6] = 0;
                            do {
                                r10++;
                                readScratch(extractorInput2, r10);
                                r14 = matroskaExtractor.scratch.data[r10 - 1] & 255;
                                int[] iArr = matroskaExtractor.blockLacingSampleSizes;
                                iArr[r6] = iArr[r6] + r14;
                            } while (r14 == 255);
                            i7 += matroskaExtractor.blockLacingSampleSizes[r6];
                        }
                        matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleCount - 1] = ((i4 - matroskaExtractor.blockTrackNumberLength) - r10) - i7;
                    } else if (i7 == 3) {
                        r6 = 0;
                        r10 = 4;
                        i7 = 0;
                        while (r6 < matroskaExtractor.blockLacingSampleCount - i5) {
                            matroskaExtractor.blockLacingSampleSizes[r6] = z;
                            r10++;
                            readScratch(extractorInput2, r10);
                            int i9 = r10 - 1;
                            if (matroskaExtractor.scratch.data[i9] == (byte) 0) {
                                throw new ParserException("No valid varint length mask found");
                            }
                            long j;
                            int[] iArr2;
                            long j2 = 0;
                            r14 = 0;
                            while (r14 < 8) {
                                int i10 = i5 << (7 - r14);
                                if ((matroskaExtractor.scratch.data[i9] & i10) != 0) {
                                    r10 += r14;
                                    readScratch(extractorInput2, r10);
                                    i6 = i9 + 1;
                                    j2 = (long) ((matroskaExtractor.scratch.data[i9] & i8) & (i10 ^ -1));
                                    while (i6 < r10) {
                                        i6++;
                                        j2 = (j2 << 8) | ((long) (matroskaExtractor.scratch.data[i6] & i8));
                                        i8 = 255;
                                    }
                                    if (r6 > 0) {
                                        j = j2 - ((1 << ((r14 * 7) + 6)) - 1);
                                        if (j >= -2147483648L) {
                                            if (j > 2147483647L) {
                                                i6 = (int) j;
                                                iArr2 = matroskaExtractor.blockLacingSampleSizes;
                                                if (r6 == 0) {
                                                    i6 += matroskaExtractor.blockLacingSampleSizes[r6 - 1];
                                                }
                                                iArr2[r6] = i6;
                                                i7 += matroskaExtractor.blockLacingSampleSizes[r6];
                                                r6++;
                                                z = false;
                                                i5 = 1;
                                                i8 = 255;
                                            }
                                        }
                                        throw new ParserException("EBML lacing sample size out of range.");
                                    }
                                    j = j2;
                                    if (j >= -2147483648L) {
                                        if (j > 2147483647L) {
                                            i6 = (int) j;
                                            iArr2 = matroskaExtractor.blockLacingSampleSizes;
                                            if (r6 == 0) {
                                                i6 += matroskaExtractor.blockLacingSampleSizes[r6 - 1];
                                            }
                                            iArr2[r6] = i6;
                                            i7 += matroskaExtractor.blockLacingSampleSizes[r6];
                                            r6++;
                                            z = false;
                                            i5 = 1;
                                            i8 = 255;
                                        }
                                    }
                                    throw new ParserException("EBML lacing sample size out of range.");
                                }
                                r14++;
                                i5 = 1;
                                i8 = 255;
                            }
                            j = j2;
                            if (j >= -2147483648L) {
                                if (j > 2147483647L) {
                                    i6 = (int) j;
                                    iArr2 = matroskaExtractor.blockLacingSampleSizes;
                                    if (r6 == 0) {
                                        i6 += matroskaExtractor.blockLacingSampleSizes[r6 - 1];
                                    }
                                    iArr2[r6] = i6;
                                    i7 += matroskaExtractor.blockLacingSampleSizes[r6];
                                    r6++;
                                    z = false;
                                    i5 = 1;
                                    i8 = 255;
                                }
                            }
                            throw new ParserException("EBML lacing sample size out of range.");
                        }
                        matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleCount - 1] = ((i4 - matroskaExtractor.blockTrackNumberLength) - r10) - i7;
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unexpected lacing value: ");
                        stringBuilder.append(i7);
                        throw new ParserException(stringBuilder.toString());
                    }
                }
                matroskaExtractor.blockTimeUs = matroskaExtractor.clusterTimecodeUs + scaleTimecodeToUs((long) ((matroskaExtractor.scratch.data[0] << 8) | (matroskaExtractor.scratch.data[1] & 255)));
                Object obj = (matroskaExtractor.scratch.data[2] & 8) == 8 ? 1 : null;
                if (track.type != 2) {
                    if (i3 != ID_SIMPLE_BLOCK || (matroskaExtractor.scratch.data[2] & 128) != 128) {
                        i6 = 0;
                        matroskaExtractor.blockFlags = i6 | (obj == null ? Integer.MIN_VALUE : 0);
                        matroskaExtractor.blockState = 2;
                        matroskaExtractor.blockLacingSampleIndex = 0;
                    }
                }
                i6 = 1;
                if (obj == null) {
                }
                matroskaExtractor.blockFlags = i6 | (obj == null ? Integer.MIN_VALUE : 0);
                matroskaExtractor.blockState = 2;
                matroskaExtractor.blockLacingSampleIndex = 0;
            }
            if (i3 == ID_SIMPLE_BLOCK) {
                while (matroskaExtractor.blockLacingSampleIndex < matroskaExtractor.blockLacingSampleCount) {
                    writeSampleData(extractorInput2, track, matroskaExtractor.blockLacingSampleSizes[matroskaExtractor.blockLacingSampleIndex]);
                    commitSampleToOutput(track, matroskaExtractor.blockTimeUs + ((long) ((matroskaExtractor.blockLacingSampleIndex * track.defaultSampleDurationNs) / 1000)));
                    matroskaExtractor.blockLacingSampleIndex++;
                }
                matroskaExtractor.blockState = 0;
            } else {
                writeSampleData(extractorInput2, track, matroskaExtractor.blockLacingSampleSizes[0]);
            }
        } else if (i3 == ID_CONTENT_COMPRESSION_SETTINGS) {
            matroskaExtractor.currentTrack.sampleStrippedBytes = new byte[i4];
            extractorInput2.readFully(matroskaExtractor.currentTrack.sampleStrippedBytes, 0, i4);
        } else if (i3 == ID_CONTENT_ENCRYPTION_KEY_ID) {
            byte[] bArr = new byte[i4];
            extractorInput2.readFully(bArr, 0, i4);
            matroskaExtractor.currentTrack.cryptoData = new CryptoData(1, bArr, 0, 0);
        } else if (i3 == ID_SEEK_ID) {
            Arrays.fill(matroskaExtractor.seekEntryIdBytes.data, (byte) 0);
            extractorInput2.readFully(matroskaExtractor.seekEntryIdBytes.data, 4 - i4, i4);
            matroskaExtractor.seekEntryIdBytes.setPosition(0);
            matroskaExtractor.seekEntryId = (int) matroskaExtractor.seekEntryIdBytes.readUnsignedInt();
        } else if (i3 == ID_CODEC_PRIVATE) {
            matroskaExtractor.currentTrack.codecPrivate = new byte[i4];
            extractorInput2.readFully(matroskaExtractor.currentTrack.codecPrivate, 0, i4);
        } else if (i3 != ID_PROJECTION_PRIVATE) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Unexpected id: ");
            stringBuilder2.append(i3);
            throw new ParserException(stringBuilder2.toString());
        } else {
            matroskaExtractor.currentTrack.projectionData = new byte[i4];
            extractorInput2.readFully(matroskaExtractor.currentTrack.projectionData, 0, i4);
        }
    }

    private void commitSampleToOutput(Track track, long j) {
        MatroskaExtractor matroskaExtractor = this;
        Track track2 = track;
        if (track2.trueHdSampleRechunker != null) {
            track2.trueHdSampleRechunker.sampleMetadata(track2, j);
        } else {
            long j2 = j;
            if (CODEC_ID_SUBRIP.equals(track2.codecId)) {
                commitSubtitleSample(track2, SUBRIP_TIMECODE_FORMAT, 19, 1000, SUBRIP_TIMECODE_EMPTY);
            } else if (CODEC_ID_ASS.equals(track2.codecId)) {
                commitSubtitleSample(track2, SSA_TIMECODE_FORMAT, 21, 10000, SSA_TIMECODE_EMPTY);
            }
            track2.output.sampleMetadata(j2, matroskaExtractor.blockFlags, matroskaExtractor.sampleBytesWritten, 0, track2.cryptoData);
        }
        matroskaExtractor.sampleRead = true;
        resetSample();
    }

    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = (byte) 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset();
    }

    private void readScratch(ExtractorInput extractorInput, int i) throws IOException, InterruptedException {
        if (this.scratch.limit() < i) {
            if (this.scratch.capacity() < i) {
                this.scratch.reset(Arrays.copyOf(this.scratch.data, Math.max(this.scratch.data.length * 2, i)), this.scratch.limit());
            }
            extractorInput.readFully(this.scratch.data, this.scratch.limit(), i - this.scratch.limit());
            this.scratch.setLimit(i);
        }
    }

    private void writeSampleData(ExtractorInput extractorInput, Track track, int i) throws IOException, InterruptedException {
        if (CODEC_ID_SUBRIP.equals(track.codecId)) {
            writeSubtitleSampleData(extractorInput, SUBRIP_PREFIX, i);
        } else if (CODEC_ID_ASS.equals(track.codecId)) {
            writeSubtitleSampleData(extractorInput, SSA_PREFIX, i);
        } else {
            TrackOutput trackOutput = track.output;
            boolean z = true;
            if (!this.sampleEncodingHandled) {
                if (track.hasContentEncryption) {
                    this.blockFlags &= -1073741825;
                    int i2 = 128;
                    if (!this.sampleSignalByteRead) {
                        extractorInput.readFully(this.scratch.data, 0, 1);
                        this.sampleBytesRead++;
                        if ((this.scratch.data[0] & 128) == 128) {
                            throw new ParserException("Extension bit is set in signal byte");
                        }
                        this.sampleSignalByte = this.scratch.data[0];
                        this.sampleSignalByteRead = true;
                    }
                    if (((this.sampleSignalByte & 1) == 1 ? 1 : null) != null) {
                        Object obj = (this.sampleSignalByte & 2) == 2 ? 1 : null;
                        this.blockFlags |= 1073741824;
                        if (!this.sampleInitializationVectorRead) {
                            extractorInput.readFully(this.encryptionInitializationVector.data, 0, 8);
                            this.sampleBytesRead += 8;
                            this.sampleInitializationVectorRead = true;
                            byte[] bArr = this.scratch.data;
                            if (obj == null) {
                                i2 = 0;
                            }
                            bArr[0] = (byte) (i2 | 8);
                            this.scratch.setPosition(0);
                            trackOutput.sampleData(this.scratch, 1);
                            this.sampleBytesWritten++;
                            this.encryptionInitializationVector.setPosition(0);
                            trackOutput.sampleData(this.encryptionInitializationVector, 8);
                            this.sampleBytesWritten += 8;
                        }
                        if (obj != null) {
                            if (!this.samplePartitionCountRead) {
                                extractorInput.readFully(this.scratch.data, 0, 1);
                                this.sampleBytesRead++;
                                this.scratch.setPosition(0);
                                this.samplePartitionCount = this.scratch.readUnsignedByte();
                                this.samplePartitionCountRead = true;
                            }
                            int i3 = this.samplePartitionCount * 4;
                            this.scratch.reset(i3);
                            extractorInput.readFully(this.scratch.data, 0, i3);
                            this.sampleBytesRead += i3;
                            short s = (short) ((this.samplePartitionCount / 2) + 1);
                            i2 = (s * 6) + 2;
                            if (this.encryptionSubsampleDataBuffer == null || this.encryptionSubsampleDataBuffer.capacity() < i2) {
                                this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(i2);
                            }
                            this.encryptionSubsampleDataBuffer.position(0);
                            this.encryptionSubsampleDataBuffer.putShort(s);
                            i3 = 0;
                            int i4 = 0;
                            while (i3 < this.samplePartitionCount) {
                                int readUnsignedIntToInt = this.scratch.readUnsignedIntToInt();
                                if (i3 % 2 == 0) {
                                    this.encryptionSubsampleDataBuffer.putShort((short) (readUnsignedIntToInt - i4));
                                } else {
                                    this.encryptionSubsampleDataBuffer.putInt(readUnsignedIntToInt - i4);
                                }
                                i3++;
                                i4 = readUnsignedIntToInt;
                            }
                            i3 = (i - this.sampleBytesRead) - i4;
                            if (this.samplePartitionCount % 2 == 1) {
                                this.encryptionSubsampleDataBuffer.putInt(i3);
                            } else {
                                this.encryptionSubsampleDataBuffer.putShort((short) i3);
                                this.encryptionSubsampleDataBuffer.putInt(0);
                            }
                            this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), i2);
                            trackOutput.sampleData(this.encryptionSubsampleData, i2);
                            this.sampleBytesWritten += i2;
                        }
                    }
                } else if (track.sampleStrippedBytes != null) {
                    this.sampleStrippedBytes.reset(track.sampleStrippedBytes, track.sampleStrippedBytes.length);
                }
                this.sampleEncodingHandled = true;
            }
            i += this.sampleStrippedBytes.limit();
            if (!CODEC_ID_H264.equals(track.codecId)) {
                if (!CODEC_ID_H265.equals(track.codecId)) {
                    if (track.trueHdSampleRechunker != null) {
                        if (this.sampleStrippedBytes.limit() != 0) {
                            z = false;
                        }
                        Assertions.checkState(z);
                        track.trueHdSampleRechunker.startSample(extractorInput, this.blockFlags, i);
                    }
                    while (this.sampleBytesRead < i) {
                        readToOutput(extractorInput, trackOutput, i - this.sampleBytesRead);
                    }
                    if (CODEC_ID_VORBIS.equals(track.codecId) != null) {
                        this.vorbisNumPageSamples.setPosition(0);
                        trackOutput.sampleData(this.vorbisNumPageSamples, 4);
                        this.sampleBytesWritten += 4;
                    }
                }
            }
            byte[] bArr2 = this.nalLength.data;
            bArr2[0] = (byte) 0;
            bArr2[1] = (byte) 0;
            bArr2[2] = (byte) 0;
            int i5 = track.nalUnitLengthFieldLength;
            int i6 = 4 - track.nalUnitLengthFieldLength;
            while (this.sampleBytesRead < i) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    readToTarget(extractorInput, bArr2, i6, i5);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    trackOutput.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                } else {
                    this.sampleCurrentNalBytesRemaining -= readToOutput(extractorInput, trackOutput, this.sampleCurrentNalBytesRemaining);
                }
            }
            if (CODEC_ID_VORBIS.equals(track.codecId) != null) {
                this.vorbisNumPageSamples.setPosition(0);
                trackOutput.sampleData(this.vorbisNumPageSamples, 4);
                this.sampleBytesWritten += 4;
            }
        }
    }

    private void writeSubtitleSampleData(ExtractorInput extractorInput, byte[] bArr, int i) throws IOException, InterruptedException {
        int length = bArr.length + i;
        if (this.subtitleSample.capacity() < length) {
            this.subtitleSample.data = Arrays.copyOf(bArr, length + i);
        } else {
            System.arraycopy(bArr, 0, this.subtitleSample.data, 0, bArr.length);
        }
        extractorInput.readFully(this.subtitleSample.data, bArr.length, i);
        this.subtitleSample.reset(length);
    }

    private void commitSubtitleSample(Track track, String str, int i, long j, byte[] bArr) {
        setSampleDuration(this.subtitleSample.data, this.blockDurationUs, str, i, j, bArr);
        track.output.sampleData(this.subtitleSample, this.subtitleSample.limit());
        this.sampleBytesWritten += this.subtitleSample.limit();
    }

    private static void setSampleDuration(byte[] bArr, long j, String str, int i, long j2, byte[] bArr2) {
        Object obj;
        Object obj2;
        if (j == C0649C.TIME_UNSET) {
            obj = bArr2;
            obj2 = obj;
        } else {
            long j3 = j - (((long) (((int) (j / 3600000000L)) * 3600)) * C0649C.MICROS_PER_SECOND);
            long j4 = j3 - (((long) (((int) (j3 / 60000000)) * 60)) * C0649C.MICROS_PER_SECOND);
            int i2 = (int) ((j4 - (((long) ((int) (j4 / C0649C.MICROS_PER_SECOND))) * C0649C.MICROS_PER_SECOND)) / j2);
            obj2 = Util.getUtf8Bytes(String.format(Locale.US, str, new Object[]{Integer.valueOf(r3), Integer.valueOf(r0), Integer.valueOf(r1), Integer.valueOf(i2)}));
            obj = bArr2;
        }
        System.arraycopy(obj2, 0, bArr, i, obj.length);
    }

    private void readToTarget(ExtractorInput extractorInput, byte[] bArr, int i, int i2) throws IOException, InterruptedException {
        int min = Math.min(i2, this.sampleStrippedBytes.bytesLeft());
        extractorInput.readFully(bArr, i + min, i2 - min);
        if (min > 0) {
            this.sampleStrippedBytes.readBytes(bArr, i, min);
        }
        this.sampleBytesRead += i2;
    }

    private int readToOutput(ExtractorInput extractorInput, TrackOutput trackOutput, int i) throws IOException, InterruptedException {
        int bytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (bytesLeft > 0) {
            extractorInput = Math.min(i, bytesLeft);
            trackOutput.sampleData(this.sampleStrippedBytes, extractorInput);
        } else {
            extractorInput = trackOutput.sampleData(extractorInput, i, false);
        }
        this.sampleBytesRead += extractorInput;
        this.sampleBytesWritten += extractorInput;
        return extractorInput;
    }

    private SeekMap buildSeekMap() {
        if (!(this.segmentContentPosition == -1 || this.durationUs == C0649C.TIME_UNSET || this.cueTimesUs == null || this.cueTimesUs.size() == 0 || this.cueClusterPositions == null)) {
            if (this.cueClusterPositions.size() == this.cueTimesUs.size()) {
                int i;
                int size = this.cueTimesUs.size();
                int[] iArr = new int[size];
                long[] jArr = new long[size];
                long[] jArr2 = new long[size];
                long[] jArr3 = new long[size];
                int i2 = 0;
                for (i = 0; i < size; i++) {
                    jArr3[i] = this.cueTimesUs.get(i);
                    jArr[i] = this.segmentContentPosition + this.cueClusterPositions.get(i);
                }
                while (true) {
                    i = size - 1;
                    if (i2 < i) {
                        i = i2 + 1;
                        iArr[i2] = (int) (jArr[i] - jArr[i2]);
                        jArr2[i2] = jArr3[i] - jArr3[i2];
                        i2 = i;
                    } else {
                        iArr[i] = (int) ((this.segmentContentPosition + this.segmentContentSize) - jArr[i]);
                        jArr2[i] = this.durationUs - jArr3[i];
                        this.cueTimesUs = null;
                        this.cueClusterPositions = null;
                        return new ChunkIndex(iArr, jArr, jArr2, jArr3);
                    }
                }
            }
        }
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return new Unseekable(this.durationUs);
    }

    private boolean maybeSeekForCues(PositionHolder positionHolder, long j) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = j;
            positionHolder.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        } else if (this.sentSeekMap == null || this.seekPositionAfterBuildingCues == -1) {
            return false;
        } else {
            positionHolder.position = this.seekPositionAfterBuildingCues;
            this.seekPositionAfterBuildingCues = -1;
            return true;
        }
    }

    private long scaleTimecodeToUs(long j) throws ParserException {
        if (this.timecodeScale == C0649C.TIME_UNSET) {
            throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
        }
        return Util.scaleLargeTimestamp(j, this.timecodeScale, 1000);
    }

    private static boolean isCodecSupported(String str) {
        if (!(CODEC_ID_VP8.equals(str) || CODEC_ID_VP9.equals(str) || CODEC_ID_MPEG2.equals(str) || CODEC_ID_MPEG4_SP.equals(str) || CODEC_ID_MPEG4_ASP.equals(str) || CODEC_ID_MPEG4_AP.equals(str) || CODEC_ID_H264.equals(str) || CODEC_ID_H265.equals(str) || CODEC_ID_FOURCC.equals(str) || CODEC_ID_THEORA.equals(str) || CODEC_ID_OPUS.equals(str) || CODEC_ID_VORBIS.equals(str) || CODEC_ID_AAC.equals(str) || CODEC_ID_MP2.equals(str) || CODEC_ID_MP3.equals(str) || CODEC_ID_AC3.equals(str) || CODEC_ID_E_AC3.equals(str) || CODEC_ID_TRUEHD.equals(str) || CODEC_ID_DTS.equals(str) || CODEC_ID_DTS_EXPRESS.equals(str) || CODEC_ID_DTS_LOSSLESS.equals(str) || CODEC_ID_FLAC.equals(str) || CODEC_ID_ACM.equals(str) || CODEC_ID_PCM_INT_LIT.equals(str) || CODEC_ID_SUBRIP.equals(str) || CODEC_ID_ASS.equals(str) || CODEC_ID_VOBSUB.equals(str) || CODEC_ID_PGS.equals(str))) {
            if (CODEC_ID_DVBSUB.equals(str) == null) {
                return null;
            }
        }
        return true;
    }

    private static int[] ensureArrayCapacity(int[] iArr, int i) {
        if (iArr == null) {
            return new int[i];
        }
        if (iArr.length >= i) {
            return iArr;
        }
        return new int[Math.max(iArr.length * 2, i)];
    }
}
