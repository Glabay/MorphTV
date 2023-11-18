package org.apache.commons.lang3;

import java.util.Random;
import org.mozilla.universalchardet.prober.distributionanalysis.Big5DistributionAnalysis;

public class RandomStringUtils {
    private static final Random RANDOM = new Random();

    public static String random(int i) {
        return random(i, false, false);
    }

    public static String randomAscii(int i) {
        return random(i, 32, 127, false, false);
    }

    public static String randomAscii(int i, int i2) {
        return randomAscii(RandomUtils.nextInt(i, i2));
    }

    public static String randomAlphabetic(int i) {
        return random(i, true, false);
    }

    public static String randomAlphabetic(int i, int i2) {
        return randomAlphabetic(RandomUtils.nextInt(i, i2));
    }

    public static String randomAlphanumeric(int i) {
        return random(i, true, true);
    }

    public static String randomAlphanumeric(int i, int i2) {
        return randomAlphanumeric(RandomUtils.nextInt(i, i2));
    }

    public static String randomGraph(int i) {
        return random(i, 33, Big5DistributionAnalysis.LOWBYTE_END_1, false, false);
    }

    public static String randomGraph(int i, int i2) {
        return randomGraph(RandomUtils.nextInt(i, i2));
    }

    public static String randomNumeric(int i) {
        return random(i, false, true);
    }

    public static String randomNumeric(int i, int i2) {
        return randomNumeric(RandomUtils.nextInt(i, i2));
    }

    public static String randomPrint(int i) {
        return random(i, 32, Big5DistributionAnalysis.LOWBYTE_END_1, false, false);
    }

    public static String randomPrint(int i, int i2) {
        return randomPrint(RandomUtils.nextInt(i, i2));
    }

    public static String random(int i, boolean z, boolean z2) {
        return random(i, 0, 0, z, z2);
    }

    public static String random(int i, int i2, int i3, boolean z, boolean z2) {
        return random(i, i2, i3, z, z2, null, RANDOM);
    }

    public static String random(int i, int i2, int i3, boolean z, boolean z2, char... cArr) {
        return random(i, i2, i3, z, z2, cArr, RANDOM);
    }

    public static String random(int i, int i2, int i3, boolean z, boolean z2, char[] cArr, Random random) {
        if (i == 0) {
            return "";
        }
        if (i < 0) {
            i3 = new StringBuilder();
            i3.append("Requested random string length ");
            i3.append(i);
            i3.append(" is less than 0.");
            throw new IllegalArgumentException(i3.toString());
        } else if (cArr == null || cArr.length != 0) {
            if (i2 == 0 && i3 == 0) {
                if (cArr != null) {
                    i3 = cArr.length;
                } else if (z || z2) {
                    i3 = 123;
                    i2 = 32;
                } else {
                    i3 = Integer.MAX_VALUE;
                }
            } else if (i3 <= i2) {
                z = new StringBuilder();
                z.append("Parameter end (");
                z.append(i3);
                z.append(") must be greater than start (");
                z.append(i2);
                z.append(")");
                throw new IllegalArgumentException(z.toString());
            }
            char[] cArr2 = new char[i];
            i3 -= i2;
            while (true) {
                int i4 = i - 1;
                if (i == 0) {
                    return new String(cArr2);
                }
                if (cArr == null) {
                    i = (char) (random.nextInt(i3) + i2);
                } else {
                    i = cArr[random.nextInt(i3) + i2];
                }
                if (!(z && Character.isLetter(i)) && (!(z2 && Character.isDigit(i)) && (z || z2))) {
                    i4++;
                } else if (i < 56320 || i > 57343) {
                    if (i < 55296 || i > 56191) {
                        if (i < 56192 || i > 56319) {
                            cArr2[i4] = i;
                        } else {
                            i4++;
                        }
                    } else if (i4 == 0) {
                        i4++;
                    } else {
                        cArr2[i4] = (char) (random.nextInt(128) + 56320);
                        i4--;
                        cArr2[i4] = i;
                    }
                } else if (i4 == 0) {
                    i4++;
                } else {
                    cArr2[i4] = i;
                    i4--;
                    cArr2[i4] = (char) (random.nextInt(128) + 55296);
                }
                i = i4;
            }
        } else {
            throw new IllegalArgumentException("The chars array must not be empty");
        }
    }

    public static String random(int i, String str) {
        if (str != null) {
            return random(i, str.toCharArray());
        }
        return random(i, 0, 0, false, false, null, RANDOM);
    }

    public static String random(int i, char... cArr) {
        if (cArr == null) {
            return random(i, 0, 0, false, false, null, RANDOM);
        }
        return random(i, 0, cArr.length, false, false, cArr, RANDOM);
    }
}
