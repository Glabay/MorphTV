package org.apache.commons.lang3.math;

import java.math.BigInteger;
import org.apache.commons.lang3.StringUtils;

public final class Fraction extends Number implements Comparable<Fraction> {
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction ZERO = new Fraction(0, 1);
    private static final long serialVersionUID = 65382027393090L;
    private final int denominator;
    private transient int hashCode = 0;
    private final int numerator;
    private transient String toProperString = null;
    private transient String toString = null;

    private Fraction(int i, int i2) {
        this.numerator = i;
        this.denominator = i2;
    }

    public static Fraction getFraction(int i, int i2) {
        if (i2 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (i2 < 0) {
            if (i != Integer.MIN_VALUE) {
                if (i2 != Integer.MIN_VALUE) {
                    i = -i;
                    i2 = -i2;
                }
            }
            throw new ArithmeticException("overflow: can't negate");
        }
        return new Fraction(i, i2);
    }

    public static Fraction getFraction(int i, int i2, int i3) {
        if (i3 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (i3 < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        } else if (i2 < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        } else {
            long j = i < 0 ? (((long) i) * ((long) i3)) - ((long) i2) : (((long) i) * ((long) i3)) + ((long) i2);
            if (j >= Integer.MIN_VALUE) {
                if (j <= Integer.MAX_VALUE) {
                    return new Fraction((int) j, i3);
                }
            }
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
    }

    public static Fraction getReducedFraction(int i, int i2) {
        if (i2 == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (i == 0) {
            return ZERO;
        } else {
            if (i2 == Integer.MIN_VALUE && (i & 1) == 0) {
                i /= 2;
                i2 /= 2;
            }
            if (i2 < 0) {
                if (i != Integer.MIN_VALUE) {
                    if (i2 != Integer.MIN_VALUE) {
                        i = -i;
                        i2 = -i2;
                    }
                }
                throw new ArithmeticException("overflow: can't negate");
            }
            int greatestCommonDivisor = greatestCommonDivisor(i, i2);
            return new Fraction(i / greatestCommonDivisor, i2 / greatestCommonDivisor);
        }
    }

    public static Fraction getFraction(double d) {
        int i = d < 0.0d ? -1 : 1;
        double abs = Math.abs(d);
        if (abs <= 2.147483647E9d) {
            if (!Double.isNaN(abs)) {
                int i2 = (int) abs;
                abs -= (double) i2;
                int i3 = (int) abs;
                double d2 = 1.0d;
                double d3 = abs - ((double) i3);
                double d4 = Double.MAX_VALUE;
                int i4 = i;
                int i5 = 1;
                int i6 = 0;
                int i7 = 0;
                int i8 = 1;
                int i9 = 1;
                while (true) {
                    int i10 = (int) (d2 / d3);
                    double d5 = d4;
                    d2 -= ((double) i10) * d3;
                    i = (i3 * i5) + i6;
                    i3 = (i3 * i7) + i8;
                    int i11 = i10;
                    int i12 = i;
                    d4 = Math.abs(abs - (((double) i) / ((double) i3)));
                    i = i9 + 1;
                    if (d5 <= d4 || i3 > 10000 || i3 <= 0) {
                        break;
                    } else if (i >= 25) {
                        break;
                    } else {
                        i9 = i;
                        i6 = i5;
                        i8 = i7;
                        i5 = i12;
                        i7 = i3;
                        i3 = i11;
                        double d6 = d2;
                        d2 = d3;
                        d3 = d6;
                    }
                }
                if (i != 25) {
                    return getReducedFraction((i5 + (i2 * i7)) * i4, i7);
                }
                throw new ArithmeticException("Unable to convert double to fraction");
            }
        }
        throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
    }

    public static Fraction getFraction(String str) {
        if (str == null) {
            throw new IllegalArgumentException("The string must not be null");
        } else if (str.indexOf(46) >= 0) {
            return getFraction(Double.parseDouble(str));
        } else {
            int indexOf = str.indexOf(32);
            if (indexOf > 0) {
                int parseInt = Integer.parseInt(str.substring(0, indexOf));
                str = str.substring(indexOf + 1);
                indexOf = str.indexOf(47);
                if (indexOf >= 0) {
                    return getFraction(parseInt, Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
                }
                throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
            }
            indexOf = str.indexOf(47);
            if (indexOf < 0) {
                return getFraction(Integer.parseInt(str), 1);
            }
            return getFraction(Integer.parseInt(str.substring(0, indexOf)), Integer.parseInt(str.substring(indexOf + 1)));
        }
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getProperNumerator() {
        return Math.abs(this.numerator % this.denominator);
    }

    public int getProperWhole() {
        return this.numerator / this.denominator;
    }

    public int intValue() {
        return this.numerator / this.denominator;
    }

    public long longValue() {
        return ((long) this.numerator) / ((long) this.denominator);
    }

    public float floatValue() {
        return ((float) this.numerator) / ((float) this.denominator);
    }

    public double doubleValue() {
        return ((double) this.numerator) / ((double) this.denominator);
    }

    public Fraction reduce() {
        if (this.numerator == 0) {
            return equals(ZERO) ? this : ZERO;
        }
        int greatestCommonDivisor = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
        if (greatestCommonDivisor == 1) {
            return this;
        }
        return getFraction(this.numerator / greatestCommonDivisor, this.denominator / greatestCommonDivisor);
    }

    public Fraction invert() {
        if (this.numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        } else if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: can't negate numerator");
        } else if (this.numerator < 0) {
            return new Fraction(-this.denominator, -this.numerator);
        } else {
            return new Fraction(this.denominator, this.numerator);
        }
    }

    public Fraction negate() {
        if (this.numerator != Integer.MIN_VALUE) {
            return new Fraction(-this.numerator, this.denominator);
        }
        throw new ArithmeticException("overflow: too large to negate");
    }

    public Fraction abs() {
        if (this.numerator >= 0) {
            return this;
        }
        return negate();
    }

    public Fraction pow(int i) {
        if (i == 1) {
            return this;
        }
        if (i == 0) {
            return ONE;
        }
        if (i >= 0) {
            Fraction multiplyBy = multiplyBy(this);
            if (i % 2 == 0) {
                return multiplyBy.pow(i / 2);
            }
            return multiplyBy.pow(i / 2).multiplyBy(this);
        } else if (i == Integer.MIN_VALUE) {
            return invert().pow(2).pow(-(i / 2));
        } else {
            return invert().pow(-i);
        }
    }

    private static int greatestCommonDivisor(int i, int i2) {
        if (i != 0) {
            if (i2 != 0) {
                if (Math.abs(i) != 1) {
                    if (Math.abs(i2) != 1) {
                        int i3;
                        if (i > 0) {
                            i = -i;
                        }
                        if (i2 > 0) {
                            i2 = -i2;
                        }
                        int i4 = 0;
                        while (true) {
                            i3 = i & 1;
                            if (i3 == 0 && (i2 & 1) == 0 && i4 < 31) {
                                i /= 2;
                                i2 /= 2;
                                i4++;
                            } else if (i4 != 31) {
                                throw new ArithmeticException("overflow: gcd is 2^31");
                            } else {
                                if (i3 != 1) {
                                    i3 = i2;
                                } else {
                                    i3 = -(i / 2);
                                }
                                while (true) {
                                    if ((i3 & 1) != 0) {
                                        i3 /= 2;
                                    } else {
                                        if (i3 <= 0) {
                                            i = -i3;
                                        } else {
                                            i2 = i3;
                                        }
                                        i3 = (i2 - i) / 2;
                                        if (i3 == 0) {
                                            return (-i) * (1 << i4);
                                        }
                                    }
                                }
                            }
                        }
                        if (i4 != 31) {
                            if (i3 != 1) {
                                i3 = -(i / 2);
                            } else {
                                i3 = i2;
                            }
                            while (true) {
                                if ((i3 & 1) != 0) {
                                    if (i3 <= 0) {
                                        i2 = i3;
                                    } else {
                                        i = -i3;
                                    }
                                    i3 = (i2 - i) / 2;
                                    if (i3 == 0) {
                                        return (-i) * (1 << i4);
                                    }
                                } else {
                                    i3 /= 2;
                                }
                            }
                        } else {
                            throw new ArithmeticException("overflow: gcd is 2^31");
                        }
                    }
                }
                return 1;
            }
        }
        if (i != Integer.MIN_VALUE) {
            if (i2 != Integer.MIN_VALUE) {
                return Math.abs(i) + Math.abs(i2);
            }
        }
        throw new ArithmeticException("overflow: gcd is 2^31");
    }

    private static int mulAndCheck(int i, int i2) {
        long j = ((long) i) * ((long) i2);
        if (j >= Integer.MIN_VALUE) {
            if (j <= Integer.MAX_VALUE) {
                return (int) j;
            }
        }
        throw new ArithmeticException("overflow: mul");
    }

    private static int mulPosAndCheck(int i, int i2) {
        long j = ((long) i) * ((long) i2);
        if (j <= Integer.MAX_VALUE) {
            return (int) j;
        }
        throw new ArithmeticException("overflow: mulPos");
    }

    private static int addAndCheck(int i, int i2) {
        long j = ((long) i) + ((long) i2);
        if (j >= Integer.MIN_VALUE) {
            if (j <= Integer.MAX_VALUE) {
                return (int) j;
            }
        }
        throw new ArithmeticException("overflow: add");
    }

    private static int subAndCheck(int i, int i2) {
        long j = ((long) i) - ((long) i2);
        if (j >= Integer.MIN_VALUE) {
            if (j <= Integer.MAX_VALUE) {
                return (int) j;
            }
        }
        throw new ArithmeticException("overflow: add");
    }

    public Fraction add(Fraction fraction) {
        return addSub(fraction, true);
    }

    public Fraction subtract(Fraction fraction) {
        return addSub(fraction, false);
    }

    private Fraction addSub(Fraction fraction, boolean z) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (this.numerator == 0) {
            if (!z) {
                fraction = fraction.negate();
            }
            return fraction;
        } else if (fraction.numerator == 0) {
            return this;
        } else {
            int greatestCommonDivisor = greatestCommonDivisor(this.denominator, fraction.denominator);
            int mulAndCheck;
            if (greatestCommonDivisor == 1) {
                greatestCommonDivisor = mulAndCheck(this.numerator, fraction.denominator);
                mulAndCheck = mulAndCheck(fraction.numerator, this.denominator);
                return new Fraction(z ? addAndCheck(greatestCommonDivisor, mulAndCheck) : subAndCheck(greatestCommonDivisor, mulAndCheck), mulPosAndCheck(this.denominator, fraction.denominator));
            }
            BigInteger multiply = BigInteger.valueOf((long) this.numerator).multiply(BigInteger.valueOf((long) (fraction.denominator / greatestCommonDivisor)));
            BigInteger multiply2 = BigInteger.valueOf((long) fraction.numerator).multiply(BigInteger.valueOf((long) (this.denominator / greatestCommonDivisor)));
            z = z ? multiply.add(multiply2) : multiply.subtract(multiply2);
            mulAndCheck = z.mod(BigInteger.valueOf((long) greatestCommonDivisor)).intValue();
            if (mulAndCheck == 0) {
                mulAndCheck = greatestCommonDivisor;
            } else {
                mulAndCheck = greatestCommonDivisor(mulAndCheck, greatestCommonDivisor);
            }
            z = z.divide(BigInteger.valueOf((long) mulAndCheck));
            if (z.bitLength() <= 31) {
                return new Fraction(z.intValue(), mulPosAndCheck(this.denominator / greatestCommonDivisor, fraction.denominator / mulAndCheck));
            }
            throw new ArithmeticException("overflow: numerator too large after multiply");
        }
    }

    public Fraction multiplyBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        }
        if (this.numerator != 0) {
            if (fraction.numerator != 0) {
                int greatestCommonDivisor = greatestCommonDivisor(this.numerator, fraction.denominator);
                int greatestCommonDivisor2 = greatestCommonDivisor(fraction.numerator, this.denominator);
                return getReducedFraction(mulAndCheck(this.numerator / greatestCommonDivisor, fraction.numerator / greatestCommonDivisor2), mulPosAndCheck(this.denominator / greatestCommonDivisor2, fraction.denominator / greatestCommonDivisor));
            }
        }
        return ZERO;
    }

    public Fraction divideBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (fraction.numerator != 0) {
            return multiplyBy(fraction.invert());
        } else {
            throw new ArithmeticException("The fraction to divide by must not be zero");
        }
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        Fraction fraction = (Fraction) obj;
        if (getNumerator() != fraction.getNumerator() || getDenominator() != fraction.getDenominator()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((getNumerator() + 629) * 37) + getDenominator();
        }
        return this.hashCode;
    }

    public int compareTo(Fraction fraction) {
        if (this == fraction) {
            return 0;
        }
        if (this.numerator == fraction.numerator && this.denominator == fraction.denominator) {
            return 0;
        }
        long j = ((long) this.numerator) * ((long) fraction.denominator);
        long j2 = ((long) fraction.numerator) * ((long) this.denominator);
        if (j == j2) {
            return 0;
        }
        return j < j2 ? -1 : 1;
    }

    public String toString() {
        if (this.toString == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getNumerator());
            stringBuilder.append("/");
            stringBuilder.append(getDenominator());
            this.toString = stringBuilder.toString();
        }
        return this.toString;
    }

    public String toProperString() {
        if (this.toProperString == null) {
            if (this.numerator == 0) {
                this.toProperString = "0";
            } else if (this.numerator == this.denominator) {
                this.toProperString = "1";
            } else if (this.numerator == this.denominator * -1) {
                this.toProperString = "-1";
            } else {
                if ((this.numerator > 0 ? -this.numerator : this.numerator) < (-this.denominator)) {
                    int properNumerator = getProperNumerator();
                    if (properNumerator == 0) {
                        this.toProperString = Integer.toString(getProperWhole());
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(getProperWhole());
                        stringBuilder.append(StringUtils.SPACE);
                        stringBuilder.append(properNumerator);
                        stringBuilder.append("/");
                        stringBuilder.append(getDenominator());
                        this.toProperString = stringBuilder.toString();
                    }
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(getNumerator());
                    stringBuilder2.append("/");
                    stringBuilder2.append(getDenominator());
                    this.toProperString = stringBuilder2.toString();
                }
            }
        }
        return this.toProperString;
    }
}
