package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Locale;

public final class UrlTemplate {
    private static final String BANDWIDTH = "Bandwidth";
    private static final int BANDWIDTH_ID = 3;
    private static final String DEFAULT_FORMAT_TAG = "%01d";
    private static final String ESCAPED_DOLLAR = "$$";
    private static final String NUMBER = "Number";
    private static final int NUMBER_ID = 2;
    private static final String REPRESENTATION = "RepresentationID";
    private static final int REPRESENTATION_ID = 1;
    private static final String TIME = "Time";
    private static final int TIME_ID = 4;
    private final int identifierCount;
    private final String[] identifierFormatTags;
    private final int[] identifiers;
    private final String[] urlPieces;

    public static UrlTemplate compile(String str) {
        String[] strArr = new String[5];
        int[] iArr = new int[4];
        String[] strArr2 = new String[4];
        return new UrlTemplate(strArr, iArr, strArr2, parseTemplate(str, strArr, iArr, strArr2));
    }

    private UrlTemplate(String[] strArr, int[] iArr, String[] strArr2, int i) {
        this.urlPieces = strArr;
        this.identifiers = iArr;
        this.identifierFormatTags = strArr2;
        this.identifierCount = i;
    }

    public String buildUri(String str, long j, int i, long j2) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < this.identifierCount; i2++) {
            stringBuilder.append(this.urlPieces[i2]);
            if (this.identifiers[i2] == 1) {
                stringBuilder.append(str);
            } else if (this.identifiers[i2] == 2) {
                stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Long.valueOf(j)}));
            } else if (this.identifiers[i2] == 3) {
                stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Integer.valueOf(i)}));
            } else if (this.identifiers[i2] == 4) {
                stringBuilder.append(String.format(Locale.US, this.identifierFormatTags[i2], new Object[]{Long.valueOf(j2)}));
            }
        }
        stringBuilder.append(this.urlPieces[this.identifierCount]);
        return stringBuilder.toString();
    }

    private static int parseTemplate(String str, String[] strArr, int[] iArr, String[] strArr2) {
        strArr[0] = "";
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            int indexOf = str.indexOf("$", i);
            Object obj = -1;
            StringBuilder stringBuilder;
            if (indexOf == -1) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(strArr[i2]);
                stringBuilder.append(str.substring(i));
                strArr[i2] = stringBuilder.toString();
                i = str.length();
            } else {
                if (indexOf != i) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(strArr[i2]);
                    stringBuilder2.append(str.substring(i, indexOf));
                    strArr[i2] = stringBuilder2.toString();
                } else if (str.startsWith(ESCAPED_DOLLAR, i)) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(strArr[i2]);
                    stringBuilder.append("$");
                    strArr[i2] = stringBuilder.toString();
                    i += 2;
                } else {
                    i++;
                    indexOf = str.indexOf("$", i);
                    String substring = str.substring(i, indexOf);
                    if (substring.equals(REPRESENTATION)) {
                        iArr[i2] = 1;
                    } else {
                        int indexOf2 = substring.indexOf("%0");
                        String str2 = DEFAULT_FORMAT_TAG;
                        if (indexOf2 != -1) {
                            str2 = substring.substring(indexOf2);
                            if (!str2.endsWith("d")) {
                                StringBuilder stringBuilder3 = new StringBuilder();
                                stringBuilder3.append(str2);
                                stringBuilder3.append("d");
                                str2 = stringBuilder3.toString();
                            }
                            substring = substring.substring(0, indexOf2);
                        }
                        indexOf2 = substring.hashCode();
                        if (indexOf2 != -1950496919) {
                            if (indexOf2 != 2606829) {
                                if (indexOf2 == 38199441) {
                                    if (substring.equals(BANDWIDTH)) {
                                        obj = 1;
                                    }
                                }
                            } else if (substring.equals(TIME)) {
                                obj = 2;
                            }
                        } else if (substring.equals(NUMBER)) {
                            obj = null;
                        }
                        switch (obj) {
                            case null:
                                iArr[i2] = 2;
                                break;
                            case 1:
                                iArr[i2] = 3;
                                break;
                            case 2:
                                iArr[i2] = 4;
                                break;
                            default:
                                iArr = new StringBuilder();
                                iArr.append("Invalid template: ");
                                iArr.append(str);
                                throw new IllegalArgumentException(iArr.toString());
                        }
                        strArr2[i2] = str2;
                    }
                    i2++;
                    strArr[i2] = "";
                    indexOf++;
                }
                i = indexOf;
            }
        }
        return i2;
    }
}
