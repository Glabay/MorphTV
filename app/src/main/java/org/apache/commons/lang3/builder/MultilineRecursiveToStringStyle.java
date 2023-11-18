package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class MultilineRecursiveToStringStyle extends RecursiveToStringStyle {
    private static final long serialVersionUID = 1;
    private int indent = 2;
    private int spaces = 2;

    public MultilineRecursiveToStringStyle() {
        resetIndent();
    }

    private void resetIndent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces));
        setArrayStart(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces));
        setArraySeparator(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces - this.indent));
        stringBuilder.append("}");
        setArrayEnd(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces));
        setContentStart(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(",");
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces));
        setFieldSeparator(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(SystemUtils.LINE_SEPARATOR);
        stringBuilder.append(spacer(this.spaces - this.indent));
        stringBuilder.append("]");
        setContentEnd(stringBuilder.toString());
    }

    private StringBuilder spacer(int i) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuilder.append(StringUtils.SPACE);
        }
        return stringBuilder;
    }

    public void appendDetail(StringBuffer stringBuffer, String str, Object obj) {
        if (ClassUtils.isPrimitiveWrapper(obj.getClass()) || String.class.equals(obj.getClass()) || !accept(obj.getClass())) {
            super.appendDetail(stringBuffer, str, obj);
            return;
        }
        this.spaces += this.indent;
        resetIndent();
        stringBuffer.append(ReflectionToStringBuilder.toString(obj, this));
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, Object[] objArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, objArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void reflectionAppendArrayDetail(StringBuffer stringBuffer, String str, Object obj) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, obj);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, long[] jArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, jArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, int[] iArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, iArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, short[] sArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, sArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, byte[] bArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, bArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, char[] cArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, cArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, double[] dArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, dArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, float[] fArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, fArr);
        this.spaces -= this.indent;
        resetIndent();
    }

    protected void appendDetail(StringBuffer stringBuffer, String str, boolean[] zArr) {
        this.spaces += this.indent;
        resetIndent();
        super.appendDetail(stringBuffer, str, zArr);
        this.spaces -= this.indent;
        resetIndent();
    }
}
