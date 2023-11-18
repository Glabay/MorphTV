package com.tonyodev.fetch2.util;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0013\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\f\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u000eJ\u0006\u0010\u0012\u001a\u00020\u0003J\b\u0010\u0013\u001a\u00020\u000eH\u0002J\u0006\u0010\u0014\u001a\u00020\u0010J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u0003H\u0002J\u0006\u0010\u0017\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u0010J\u0012\u0010\u0019\u001a\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u0003H\u0007J\u0012\u0010\u001b\u001a\u00020\u00102\b\b\u0002\u0010\u001a\u001a\u00020\u0003H\u0007J\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00100\u001dJ\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0005\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003XD¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/tonyodev/fetch2/util/AverageCalculator;", "", "discardLimit", "", "(I)V", "defaultIndexPosition", "defaultValueListSize", "getDiscardLimit", "()I", "endIndex", "startIndex", "valueList", "", "add", "", "value", "", "clear", "count", "expandValueList", "getAverage", "getDenominator", "number", "getFirstInputValue", "getLastInputValue", "getMovingAverageWithWeightOnOlderValues", "inclusionCount", "getMovingAverageWithWeightOnRecentValues", "getValues", "", "hasInputValue", "", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: AverageCalculator.kt */
public final class AverageCalculator {
    private final int defaultIndexPosition;
    private final int defaultValueListSize;
    private final int discardLimit;
    private int endIndex;
    private int startIndex;
    private double[] valueList;

    @JvmOverloads
    public AverageCalculator() {
        this(0, 1, null);
    }

    private final double getDenominator(int i) {
        int i2 = 1;
        double d = 0.0d;
        if (1 <= i) {
            while (true) {
                d += (double) i2;
                if (i2 == i) {
                    break;
                }
                i2++;
            }
        }
        return d;
    }

    @JvmOverloads
    public final double getMovingAverageWithWeightOnOlderValues() {
        return getMovingAverageWithWeightOnOlderValues$default(this, 0, 1, null);
    }

    @JvmOverloads
    public final double getMovingAverageWithWeightOnRecentValues() {
        return getMovingAverageWithWeightOnRecentValues$default(this, 0, 1, null);
    }

    @JvmOverloads
    public AverageCalculator(int i) {
        this.discardLimit = i;
        this.defaultValueListSize = 16;
        this.defaultIndexPosition = -1;
        this.valueList = new double[this.defaultValueListSize];
        this.startIndex = this.defaultIndexPosition;
        this.endIndex = this.defaultIndexPosition;
    }

    @JvmOverloads
    public /* synthetic */ AverageCalculator(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        this(i);
    }

    public final int getDiscardLimit() {
        return this.discardLimit;
    }

    public final void add(double d) {
        if (this.discardLimit > 0 && count() == this.discardLimit) {
            this.startIndex++;
        }
        if (this.endIndex == this.valueList.length - 1) {
            expandValueList();
        }
        this.endIndex++;
        if (this.endIndex == 0) {
            this.startIndex = this.endIndex;
        }
        this.valueList[this.endIndex] = d;
    }

    private final void expandValueList() {
        Object obj = new double[(this.valueList.length * 2)];
        int count = count();
        System.arraycopy(this.valueList, this.startIndex, obj, 0, count);
        this.valueList = obj;
        this.startIndex = 0;
        this.endIndex = count - 1;
    }

    public final void clear() {
        this.valueList = new double[this.defaultValueListSize];
        this.startIndex = this.defaultIndexPosition;
        this.endIndex = this.defaultIndexPosition;
    }

    public final int count() {
        return (this.endIndex - this.startIndex) + 1;
    }

    public final double getLastInputValue() {
        if (count() >= 1) {
            return this.valueList[this.endIndex];
        }
        throw new ArrayIndexOutOfBoundsException("value array is empty");
    }

    public final double getFirstInputValue() {
        if (count() >= 1) {
            return this.valueList[this.startIndex];
        }
        throw new ArrayIndexOutOfBoundsException("value array is empty");
    }

    public final boolean hasInputValue(double d) {
        return ArraysKt___ArraysKt.contains(this.valueList, d);
    }

    @NotNull
    public final List<Double> getValues() {
        return ArraysKt___ArraysKt.toList(this.valueList);
    }

    public final double getAverage() {
        int i = this.startIndex;
        int i2 = this.endIndex;
        double d = 0.0d;
        if (i <= i2) {
            while (true) {
                d += this.valueList[i];
                if (i == i2) {
                    break;
                }
                i++;
            }
        }
        return d / ((double) count());
    }

    @JvmOverloads
    public static /* bridge */ /* synthetic */ double getMovingAverageWithWeightOnRecentValues$default(AverageCalculator averageCalculator, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = averageCalculator.count();
        }
        return averageCalculator.getMovingAverageWithWeightOnRecentValues(i);
    }

    @JvmOverloads
    public final double getMovingAverageWithWeightOnRecentValues(int i) {
        if (i < 1) {
            throw ((Throwable) new IllegalArgumentException("inclusionCount cannot be less than 1."));
        } else if (i > count()) {
            throw ((Throwable) new IllegalArgumentException("inclusionCount cannot be greater than the inserted value count."));
        } else {
            double d = 0.0d;
            double denominator = getDenominator(i);
            int i2 = this.endIndex;
            int i3 = this.endIndex - (i - 1);
            if (i2 >= i3) {
                while (true) {
                    d += this.valueList[i2] * (((double) i) / denominator);
                    i--;
                    if (i2 == i3) {
                        break;
                    }
                    i2--;
                }
            }
            return d;
        }
    }

    @JvmOverloads
    public static /* bridge */ /* synthetic */ double getMovingAverageWithWeightOnOlderValues$default(AverageCalculator averageCalculator, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = averageCalculator.count();
        }
        return averageCalculator.getMovingAverageWithWeightOnOlderValues(i);
    }

    @JvmOverloads
    public final double getMovingAverageWithWeightOnOlderValues(int i) {
        if (i < 1) {
            throw ((Throwable) new IllegalArgumentException("inclusionCount cannot be less than 1."));
        } else if (i > count()) {
            throw ((Throwable) new IllegalArgumentException("inclusionCount cannot be greater than the inserted value count."));
        } else {
            double d = 0.0d;
            double denominator = getDenominator(i);
            int i2 = this.startIndex;
            int i3 = this.startIndex + (i - 1);
            if (i2 <= i3) {
                while (true) {
                    d += this.valueList[i2] * (((double) i) / denominator);
                    i--;
                    if (i2 == i3) {
                        break;
                    }
                    i2++;
                }
            }
            return d;
        }
    }
}
