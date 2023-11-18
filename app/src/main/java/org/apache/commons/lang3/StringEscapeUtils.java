package org.apache.commons.lang3;

import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

public class StringEscapeUtils {
    public static final CharSequenceTranslator ESCAPE_CSV = new CsvEscaper();
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;
    public static final CharSequenceTranslator ESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()));
    public static final CharSequenceTranslator ESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()));
    public static final CharSequenceTranslator ESCAPE_JAVA = new LookupTranslator(new String[]{"\"", "\\\""}, new String[]{"\\", "\\\\"}).with(new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())).with(JavaUnicodeEscaper.outsideOf(32, 127));
    public static final CharSequenceTranslator ESCAPE_JSON;
    @Deprecated
    public static final CharSequenceTranslator ESCAPE_XML = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()));
    public static final CharSequenceTranslator ESCAPE_XML10;
    public static final CharSequenceTranslator ESCAPE_XML11;
    public static final CharSequenceTranslator UNESCAPE_CSV = new CsvUnescaper();
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_HTML3 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new OPTION[0]));
    public static final CharSequenceTranslator UNESCAPE_HTML4 = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new OPTION[0]));
    public static final CharSequenceTranslator UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;
    public static final CharSequenceTranslator UNESCAPE_XML = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new OPTION[0]));

    static class CsvEscaper extends CharSequenceTranslator {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR = String.valueOf('\"');
        private static final char[] CSV_SEARCH_CHARS = new char[]{CSV_DELIMITER, '\"', CharUtils.CR, '\n'};

        CsvEscaper() {
        }

        public int translate(CharSequence charSequence, int i, Writer writer) throws IOException {
            if (i != 0) {
                throw new IllegalStateException("CsvEscaper should never reach the [1] index");
            }
            if (StringUtils.containsNone(charSequence.toString(), CSV_SEARCH_CHARS) != 0) {
                writer.write(charSequence.toString());
            } else {
                writer.write(34);
                String charSequence2 = charSequence.toString();
                String str = CSV_QUOTE_STR;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(CSV_QUOTE_STR);
                stringBuilder.append(CSV_QUOTE_STR);
                writer.write(StringUtils.replace(charSequence2, str, stringBuilder.toString()));
                writer.write(34);
            }
            return Character.codePointCount(charSequence, 0, charSequence.length());
        }
    }

    static class CsvUnescaper extends CharSequenceTranslator {
        private static final char CSV_DELIMITER = ',';
        private static final char CSV_QUOTE = '\"';
        private static final String CSV_QUOTE_STR = String.valueOf('\"');
        private static final char[] CSV_SEARCH_CHARS = new char[]{CSV_DELIMITER, '\"', CharUtils.CR, '\n'};

        CsvUnescaper() {
        }

        public int translate(CharSequence charSequence, int i, Writer writer) throws IOException {
            if (i != 0) {
                throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
            }
            if (charSequence.charAt(0) == '\"') {
                if (charSequence.charAt(charSequence.length() - 1) == '\"') {
                    CharSequence charSequence2 = charSequence.subSequence(1, charSequence.length() - 1).toString();
                    if (StringUtils.containsAny(charSequence2, CSV_SEARCH_CHARS)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(CSV_QUOTE_STR);
                        stringBuilder.append(CSV_QUOTE_STR);
                        writer.write(StringUtils.replace(charSequence2, stringBuilder.toString(), CSV_QUOTE_STR));
                    } else {
                        writer.write(charSequence.toString());
                    }
                    return Character.codePointCount(charSequence, 0, charSequence.length());
                }
            }
            writer.write(charSequence.toString());
            return Character.codePointCount(charSequence, 0, charSequence.length());
        }
    }

    static {
        r7 = new CharSequenceTranslator[3];
        r7[0] = new LookupTranslator(new String[]{"'", "\\'"}, new String[]{"\"", "\\\""}, new String[]{"\\", "\\\\"}, new String[]{"/", "\\/"});
        r7[1] = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        r7[2] = JavaUnicodeEscaper.outsideOf(32, 127);
        ESCAPE_ECMASCRIPT = new AggregateTranslator(r7);
        r7 = new CharSequenceTranslator[3];
        r7[0] = new LookupTranslator(new String[]{"\"", "\\\""}, new String[]{"\\", "\\\\"}, new String[]{"/", "\\/"});
        r7[1] = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE());
        r7[2] = JavaUnicodeEscaper.outsideOf(32, 127);
        ESCAPE_JSON = new AggregateTranslator(r7);
        r7 = new CharSequenceTranslator[6];
        r7[2] = new LookupTranslator(new String[]{"\u0000", ""}, new String[]{"\u0001", ""}, new String[]{"\u0002", ""}, new String[]{"\u0003", ""}, new String[]{"\u0004", ""}, new String[]{"\u0005", ""}, new String[]{"\u0006", ""}, new String[]{"\u0007", ""}, new String[]{"\b", ""}, new String[]{"\u000b", ""}, new String[]{"\f", ""}, new String[]{"\u000e", ""}, new String[]{"\u000f", ""}, new String[]{"\u0010", ""}, new String[]{"\u0011", ""}, new String[]{"\u0012", ""}, new String[]{"\u0013", ""}, new String[]{"\u0014", ""}, new String[]{"\u0015", ""}, new String[]{"\u0016", ""}, new String[]{"\u0017", ""}, new String[]{"\u0018", ""}, new String[]{"\u0019", ""}, new String[]{"\u001a", ""}, new String[]{"\u001b", ""}, new String[]{"\u001c", ""}, new String[]{"\u001d", ""}, new String[]{"\u001e", ""}, new String[]{"\u001f", ""}, new String[]{"￾", ""}, new String[]{"￿", ""});
        r7[3] = NumericEntityEscaper.between(127, 132);
        r7[4] = NumericEntityEscaper.between(TsExtractor.TS_STREAM_TYPE_SPLICE_INFO, 159);
        r7[5] = new UnicodeUnpairedSurrogateRemover();
        ESCAPE_XML10 = new AggregateTranslator(r7);
        r7 = new CharSequenceTranslator[8];
        r7[2] = new LookupTranslator(new String[]{"\u0000", ""}, new String[]{"\u000b", "&#11;"}, new String[]{"\f", "&#12;"}, new String[]{"￾", ""}, new String[]{"￿", ""});
        r7[3] = NumericEntityEscaper.between(1, 8);
        r7[4] = NumericEntityEscaper.between(14, 31);
        r7[5] = NumericEntityEscaper.between(127, 132);
        r7[6] = NumericEntityEscaper.between(TsExtractor.TS_STREAM_TYPE_SPLICE_INFO, 159);
        r7[7] = new UnicodeUnpairedSurrogateRemover();
        ESCAPE_XML11 = new AggregateTranslator(r7);
        CharSequenceTranslator[] charSequenceTranslatorArr = new CharSequenceTranslator[4];
        charSequenceTranslatorArr[0] = new OctalUnescaper();
        charSequenceTranslatorArr[1] = new UnicodeUnescaper();
        charSequenceTranslatorArr[2] = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE());
        charSequenceTranslatorArr[3] = new LookupTranslator(new String[]{"\\\\", "\\"}, new String[]{"\\\"", "\""}, new String[]{"\\'", "'"}, new String[]{"\\", ""});
        UNESCAPE_JAVA = new AggregateTranslator(charSequenceTranslatorArr);
    }

    public static final String escapeJava(String str) {
        return ESCAPE_JAVA.translate(str);
    }

    public static final String escapeEcmaScript(String str) {
        return ESCAPE_ECMASCRIPT.translate(str);
    }

    public static final String escapeJson(String str) {
        return ESCAPE_JSON.translate(str);
    }

    public static final String unescapeJava(String str) {
        return UNESCAPE_JAVA.translate(str);
    }

    public static final String unescapeEcmaScript(String str) {
        return UNESCAPE_ECMASCRIPT.translate(str);
    }

    public static final String unescapeJson(String str) {
        return UNESCAPE_JSON.translate(str);
    }

    public static final String escapeHtml4(String str) {
        return ESCAPE_HTML4.translate(str);
    }

    public static final String escapeHtml3(String str) {
        return ESCAPE_HTML3.translate(str);
    }

    public static final String unescapeHtml4(String str) {
        return UNESCAPE_HTML4.translate(str);
    }

    public static final String unescapeHtml3(String str) {
        return UNESCAPE_HTML3.translate(str);
    }

    @Deprecated
    public static final String escapeXml(String str) {
        return ESCAPE_XML.translate(str);
    }

    public static String escapeXml10(String str) {
        return ESCAPE_XML10.translate(str);
    }

    public static String escapeXml11(String str) {
        return ESCAPE_XML11.translate(str);
    }

    public static final String unescapeXml(String str) {
        return UNESCAPE_XML.translate(str);
    }

    public static final String escapeCsv(String str) {
        return ESCAPE_CSV.translate(str);
    }

    public static final String unescapeCsv(String str) {
        return UNESCAPE_CSV.translate(str);
    }
}
