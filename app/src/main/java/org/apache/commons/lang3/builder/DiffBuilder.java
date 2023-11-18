package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class DiffBuilder implements Builder<DiffResult> {
    private final List<Diff<?>> diffs;
    private final Object left;
    private final boolean objectsTriviallyEqual;
    private final Object right;
    private final ToStringStyle style;

    public DiffBuilder(Object obj, Object obj2, ToStringStyle toStringStyle, boolean z) {
        if (obj == null) {
            throw new IllegalArgumentException("lhs cannot be null");
        } else if (obj2 == null) {
            throw new IllegalArgumentException("rhs cannot be null");
        } else {
            this.diffs = new ArrayList();
            this.left = obj;
            this.right = obj2;
            this.style = toStringStyle;
            obj = (!z || (obj != obj2 && obj.equals(obj2) == null)) ? null : true;
            this.objectsTriviallyEqual = obj;
        }
    }

    public DiffBuilder(Object obj, Object obj2, ToStringStyle toStringStyle) {
        this(obj, obj2, toStringStyle, true);
    }

    public DiffBuilder append(String str, final boolean z, final boolean z2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || z == z2)) {
            this.diffs.add(new Diff<Boolean>(str) {
                private static final long serialVersionUID = 1;

                public Boolean getLeft() {
                    return Boolean.valueOf(z);
                }

                public Boolean getRight() {
                    return Boolean.valueOf(z2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final boolean[] zArr, final boolean[] zArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(zArr, zArr2))) {
            this.diffs.add(new Diff<Boolean[]>(str) {
                private static final long serialVersionUID = 1;

                public Boolean[] getLeft() {
                    return ArrayUtils.toObject(zArr);
                }

                public Boolean[] getRight() {
                    return ArrayUtils.toObject(zArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final byte b, final byte b2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || b == b2)) {
            this.diffs.add(new Diff<Byte>(str) {
                private static final long serialVersionUID = 1;

                public Byte getLeft() {
                    return Byte.valueOf(b);
                }

                public Byte getRight() {
                    return Byte.valueOf(b2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final byte[] bArr, final byte[] bArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(bArr, bArr2))) {
            this.diffs.add(new Diff<Byte[]>(str) {
                private static final long serialVersionUID = 1;

                public Byte[] getLeft() {
                    return ArrayUtils.toObject(bArr);
                }

                public Byte[] getRight() {
                    return ArrayUtils.toObject(bArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final char c, final char c2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || c == c2)) {
            this.diffs.add(new Diff<Character>(str) {
                private static final long serialVersionUID = 1;

                public Character getLeft() {
                    return Character.valueOf(c);
                }

                public Character getRight() {
                    return Character.valueOf(c2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final char[] cArr, final char[] cArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(cArr, cArr2))) {
            this.diffs.add(new Diff<Character[]>(str) {
                private static final long serialVersionUID = 1;

                public Character[] getLeft() {
                    return ArrayUtils.toObject(cArr);
                }

                public Character[] getRight() {
                    return ArrayUtils.toObject(cArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, double d, double d2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Double.doubleToLongBits(d) == Double.doubleToLongBits(d2))) {
            final double d3 = d;
            final double d4 = d2;
            this.diffs.add(new Diff<Double>(str) {
                private static final long serialVersionUID = 1;

                public Double getLeft() {
                    return Double.valueOf(d3);
                }

                public Double getRight() {
                    return Double.valueOf(d4);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final double[] dArr, final double[] dArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(dArr, dArr2))) {
            this.diffs.add(new Diff<Double[]>(str) {
                private static final long serialVersionUID = 1;

                public Double[] getLeft() {
                    return ArrayUtils.toObject(dArr);
                }

                public Double[] getRight() {
                    return ArrayUtils.toObject(dArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final float f, final float f2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Float.floatToIntBits(f) == Float.floatToIntBits(f2))) {
            this.diffs.add(new Diff<Float>(str) {
                private static final long serialVersionUID = 1;

                public Float getLeft() {
                    return Float.valueOf(f);
                }

                public Float getRight() {
                    return Float.valueOf(f2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final float[] fArr, final float[] fArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(fArr, fArr2))) {
            this.diffs.add(new Diff<Float[]>(str) {
                private static final long serialVersionUID = 1;

                public Float[] getLeft() {
                    return ArrayUtils.toObject(fArr);
                }

                public Float[] getRight() {
                    return ArrayUtils.toObject(fArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final int i, final int i2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || i == i2)) {
            this.diffs.add(new Diff<Integer>(str) {
                private static final long serialVersionUID = 1;

                public Integer getLeft() {
                    return Integer.valueOf(i);
                }

                public Integer getRight() {
                    return Integer.valueOf(i2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final int[] iArr, final int[] iArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(iArr, iArr2))) {
            this.diffs.add(new Diff<Integer[]>(str) {
                private static final long serialVersionUID = 1;

                public Integer[] getLeft() {
                    return ArrayUtils.toObject(iArr);
                }

                public Integer[] getRight() {
                    return ArrayUtils.toObject(iArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, long j, long j2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || j == j2)) {
            final long j3 = j;
            final long j4 = j2;
            this.diffs.add(new Diff<Long>(str) {
                private static final long serialVersionUID = 1;

                public Long getLeft() {
                    return Long.valueOf(j3);
                }

                public Long getRight() {
                    return Long.valueOf(j4);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final long[] jArr, final long[] jArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(jArr, jArr2))) {
            this.diffs.add(new Diff<Long[]>(str) {
                private static final long serialVersionUID = 1;

                public Long[] getLeft() {
                    return ArrayUtils.toObject(jArr);
                }

                public Long[] getRight() {
                    return ArrayUtils.toObject(jArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final short s, final short s2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || s == s2)) {
            this.diffs.add(new Diff<Short>(str) {
                private static final long serialVersionUID = 1;

                public Short getLeft() {
                    return Short.valueOf(s);
                }

                public Short getRight() {
                    return Short.valueOf(s2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final short[] sArr, final short[] sArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(sArr, sArr2))) {
            this.diffs.add(new Diff<Short[]>(str) {
                private static final long serialVersionUID = 1;

                public Short[] getLeft() {
                    return ArrayUtils.toObject(sArr);
                }

                public Short[] getRight() {
                    return ArrayUtils.toObject(sArr2);
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, final Object obj, final Object obj2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (this.objectsTriviallyEqual || obj == obj2) {
            return this;
        } else {
            Object obj3 = obj != null ? obj : obj2;
            if (obj3.getClass().isArray()) {
                if (obj3 instanceof boolean[]) {
                    return append(str, (boolean[]) obj, (boolean[]) obj2);
                }
                if (obj3 instanceof byte[]) {
                    return append(str, (byte[]) obj, (byte[]) obj2);
                }
                if (obj3 instanceof char[]) {
                    return append(str, (char[]) obj, (char[]) obj2);
                }
                if (obj3 instanceof double[]) {
                    return append(str, (double[]) obj, (double[]) obj2);
                }
                if (obj3 instanceof float[]) {
                    return append(str, (float[]) obj, (float[]) obj2);
                }
                if (obj3 instanceof int[]) {
                    return append(str, (int[]) obj, (int[]) obj2);
                }
                if (obj3 instanceof long[]) {
                    return append(str, (long[]) obj, (long[]) obj2);
                }
                if (obj3 instanceof short[]) {
                    return append(str, (short[]) obj, (short[]) obj2);
                }
                return append(str, (Object[]) obj, (Object[]) obj2);
            } else if (obj != null && obj.equals(obj2)) {
                return this;
            } else {
                this.diffs.add(new Diff<Object>(str) {
                    private static final long serialVersionUID = 1;

                    public Object getLeft() {
                        return obj;
                    }

                    public Object getRight() {
                        return obj2;
                    }
                });
                return this;
            }
        }
    }

    public DiffBuilder append(String str, final Object[] objArr, final Object[] objArr2) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        }
        if (!(this.objectsTriviallyEqual || Arrays.equals(objArr, objArr2))) {
            this.diffs.add(new Diff<Object[]>(str) {
                private static final long serialVersionUID = 1;

                public Object[] getLeft() {
                    return objArr;
                }

                public Object[] getRight() {
                    return objArr2;
                }
            });
        }
        return this;
    }

    public DiffBuilder append(String str, DiffResult diffResult) {
        if (str == null) {
            throw new IllegalArgumentException("Field name cannot be null");
        } else if (diffResult == null) {
            throw new IllegalArgumentException("Diff result cannot be null");
        } else if (this.objectsTriviallyEqual) {
            return this;
        } else {
            for (Diff diff : diffResult.getDiffs()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(".");
                stringBuilder.append(diff.getFieldName());
                append(stringBuilder.toString(), diff.getLeft(), diff.getRight());
            }
            return this;
        }
    }

    public DiffResult build() {
        return new DiffResult(this.left, this.right, this.diffs, this.style);
    }
}
