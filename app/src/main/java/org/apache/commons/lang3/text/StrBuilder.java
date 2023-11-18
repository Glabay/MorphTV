package org.apache.commons.lang3.text;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.Builder;

public class StrBuilder implements CharSequence, Appendable, Serializable, Builder<String> {
    static final int CAPACITY = 32;
    private static final long serialVersionUID = 7628716375283629643L;
    protected char[] buffer;
    private String newLine;
    private String nullText;
    protected int size;

    class StrBuilderReader extends Reader {
        private int mark;
        private int pos;

        public void close() {
        }

        public boolean markSupported() {
            return true;
        }

        StrBuilderReader() {
        }

        public int read() {
            if (!ready()) {
                return -1;
            }
            StrBuilder strBuilder = StrBuilder.this;
            int i = this.pos;
            this.pos = i + 1;
            return strBuilder.charAt(i);
        }

        public int read(char[] cArr, int i, int i2) {
            if (i >= 0 && i2 >= 0 && i <= cArr.length) {
                int i3 = i + i2;
                if (i3 <= cArr.length) {
                    if (i3 >= 0) {
                        if (i2 == 0) {
                            return null;
                        }
                        if (this.pos >= StrBuilder.this.size()) {
                            return -1;
                        }
                        if (this.pos + i2 > StrBuilder.this.size()) {
                            i2 = StrBuilder.this.size() - this.pos;
                        }
                        StrBuilder.this.getChars(this.pos, this.pos + i2, cArr, i);
                        this.pos += i2;
                        return i2;
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }

        public long skip(long j) {
            if (((long) this.pos) + j > ((long) StrBuilder.this.size())) {
                j = (long) (StrBuilder.this.size() - this.pos);
            }
            if (j < 0) {
                return 0;
            }
            this.pos = (int) (((long) this.pos) + j);
            return j;
        }

        public boolean ready() {
            return this.pos < StrBuilder.this.size();
        }

        public void mark(int i) {
            this.mark = this.pos;
        }

        public void reset() {
            this.pos = this.mark;
        }
    }

    class StrBuilderTokenizer extends StrTokenizer {
        StrBuilderTokenizer() {
        }

        protected List<String> tokenize(char[] cArr, int i, int i2) {
            if (cArr == null) {
                return super.tokenize(StrBuilder.this.buffer, 0, StrBuilder.this.size());
            }
            return super.tokenize(cArr, i, i2);
        }

        public String getContent() {
            String content = super.getContent();
            return content == null ? StrBuilder.this.toString() : content;
        }
    }

    class StrBuilderWriter extends Writer {
        public void close() {
        }

        public void flush() {
        }

        StrBuilderWriter() {
        }

        public void write(int i) {
            StrBuilder.this.append((char) i);
        }

        public void write(char[] cArr) {
            StrBuilder.this.append(cArr);
        }

        public void write(char[] cArr, int i, int i2) {
            StrBuilder.this.append(cArr, i, i2);
        }

        public void write(String str) {
            StrBuilder.this.append(str);
        }

        public void write(String str, int i, int i2) {
            StrBuilder.this.append(str, i, i2);
        }
    }

    public StrBuilder() {
        this(32);
    }

    public StrBuilder(int i) {
        if (i <= 0) {
            i = 32;
        }
        this.buffer = new char[i];
    }

    public StrBuilder(String str) {
        if (str == null) {
            this.buffer = new char[32];
            return;
        }
        this.buffer = new char[(str.length() + 32)];
        append(str);
    }

    public String getNewLineText() {
        return this.newLine;
    }

    public StrBuilder setNewLineText(String str) {
        this.newLine = str;
        return this;
    }

    public String getNullText() {
        return this.nullText;
    }

    public StrBuilder setNullText(String str) {
        if (str != null && str.isEmpty()) {
            str = null;
        }
        this.nullText = str;
        return this;
    }

    public int length() {
        return this.size;
    }

    public StrBuilder setLength(int i) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i < this.size) {
            this.size = i;
        } else if (i > this.size) {
            ensureCapacity(i);
            this.size = i;
            for (int i2 = this.size; i2 < i; i2++) {
                this.buffer[i2] = '\u0000';
            }
        }
        return this;
    }

    public int capacity() {
        return this.buffer.length;
    }

    public StrBuilder ensureCapacity(int i) {
        if (i > this.buffer.length) {
            Object obj = this.buffer;
            this.buffer = new char[(i * 2)];
            System.arraycopy(obj, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public StrBuilder minimizeCapacity() {
        if (this.buffer.length > length()) {
            Object obj = this.buffer;
            this.buffer = new char[length()];
            System.arraycopy(obj, 0, this.buffer, 0, this.size);
        }
        return this;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public StrBuilder clear() {
        this.size = 0;
        return this;
    }

    public char charAt(int i) {
        if (i >= 0) {
            if (i < length()) {
                return this.buffer[i];
            }
        }
        throw new StringIndexOutOfBoundsException(i);
    }

    public StrBuilder setCharAt(int i, char c) {
        if (i >= 0) {
            if (i < length()) {
                this.buffer[i] = c;
                return this;
            }
        }
        throw new StringIndexOutOfBoundsException(i);
    }

    public StrBuilder deleteCharAt(int i) {
        if (i >= 0) {
            if (i < this.size) {
                deleteImpl(i, i + 1, 1);
                return this;
            }
        }
        throw new StringIndexOutOfBoundsException(i);
    }

    public char[] toCharArray() {
        if (this.size == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        Object obj = new char[this.size];
        System.arraycopy(this.buffer, 0, obj, 0, this.size);
        return obj;
    }

    public char[] toCharArray(int i, int i2) {
        i2 = validateRange(i, i2) - i;
        if (i2 == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        Object obj = new char[i2];
        System.arraycopy(this.buffer, i, obj, 0, i2);
        return obj;
    }

    public char[] getChars(char[] cArr) {
        int length = length();
        if (cArr == null || cArr.length < length) {
            cArr = new char[length];
        }
        System.arraycopy(this.buffer, 0, cArr, 0, length);
        return cArr;
    }

    public void getChars(int i, int i2, char[] cArr, int i3) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i2 >= 0) {
            if (i2 <= length()) {
                if (i > i2) {
                    throw new StringIndexOutOfBoundsException("end < start");
                }
                System.arraycopy(this.buffer, i, cArr, i3, i2 - i);
                return;
            }
        }
        throw new StringIndexOutOfBoundsException(i2);
    }

    public int readFrom(Readable readable) throws IOException {
        int i = this.size;
        int read;
        if (readable instanceof Reader) {
            Reader reader = (Reader) readable;
            ensureCapacity(this.size + 1);
            while (true) {
                read = reader.read(this.buffer, this.size, this.buffer.length - this.size);
                if (read == -1) {
                    break;
                }
                this.size += read;
                ensureCapacity(this.size + 1);
            }
        } else if (readable instanceof CharBuffer) {
            CharBuffer charBuffer = (CharBuffer) readable;
            read = charBuffer.remaining();
            ensureCapacity(this.size + read);
            charBuffer.get(this.buffer, this.size, read);
            this.size += read;
        } else {
            while (true) {
                ensureCapacity(this.size + 1);
                read = readable.read(CharBuffer.wrap(this.buffer, this.size, this.buffer.length - this.size));
                if (read == -1) {
                    break;
                }
                this.size += read;
            }
        }
        return this.size - i;
    }

    public StrBuilder appendNewLine() {
        if (this.newLine != null) {
            return append(this.newLine);
        }
        append(SystemUtils.LINE_SEPARATOR);
        return this;
    }

    public StrBuilder appendNull() {
        if (this.nullText == null) {
            return this;
        }
        return append(this.nullText);
    }

    public StrBuilder append(Object obj) {
        if (obj == null) {
            return appendNull();
        }
        if (obj instanceof CharSequence) {
            return append((CharSequence) obj);
        }
        return append(obj.toString());
    }

    public StrBuilder append(CharSequence charSequence) {
        if (charSequence == null) {
            return appendNull();
        }
        if (charSequence instanceof StrBuilder) {
            return append((StrBuilder) charSequence);
        }
        if (charSequence instanceof StringBuilder) {
            return append((StringBuilder) charSequence);
        }
        if (charSequence instanceof StringBuffer) {
            return append((StringBuffer) charSequence);
        }
        if (charSequence instanceof CharBuffer) {
            return append((CharBuffer) charSequence);
        }
        return append(charSequence.toString());
    }

    public StrBuilder append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            return appendNull();
        }
        return append(charSequence.toString(), i, i2);
    }

    public StrBuilder append(String str) {
        if (str == null) {
            return appendNull();
        }
        int length = str.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            str.getChars(0, length, this.buffer, length2);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(String str, int i, int i2) {
        if (str == null) {
            return appendNull();
        }
        if (i >= 0) {
            if (i <= str.length()) {
                if (i2 >= 0) {
                    int i3 = i + i2;
                    if (i3 <= str.length()) {
                        if (i2 > 0) {
                            int length = length();
                            ensureCapacity(length + i2);
                            str.getChars(i, i3, this.buffer, length);
                            this.size += i2;
                        }
                        return this;
                    }
                }
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        }
        throw new StringIndexOutOfBoundsException("startIndex must be valid");
    }

    public StrBuilder append(String str, Object... objArr) {
        return append(String.format(str, objArr));
    }

    public StrBuilder append(CharBuffer charBuffer) {
        if (charBuffer == null) {
            return appendNull();
        }
        if (charBuffer.hasArray()) {
            int remaining = charBuffer.remaining();
            int length = length();
            ensureCapacity(length + remaining);
            System.arraycopy(charBuffer.array(), charBuffer.arrayOffset() + charBuffer.position(), this.buffer, length, remaining);
            this.size += remaining;
        } else {
            append(charBuffer.toString());
        }
        return this;
    }

    public StrBuilder append(CharBuffer charBuffer, int i, int i2) {
        if (charBuffer == null) {
            return appendNull();
        }
        if (charBuffer.hasArray()) {
            int remaining = charBuffer.remaining();
            if (i >= 0) {
                if (i <= remaining) {
                    if (i2 >= 0) {
                        if (i + i2 <= remaining) {
                            remaining = length();
                            ensureCapacity(remaining + i2);
                            System.arraycopy(charBuffer.array(), (charBuffer.arrayOffset() + charBuffer.position()) + i, this.buffer, remaining, i2);
                            this.size += i2;
                        }
                    }
                    throw new StringIndexOutOfBoundsException("length must be valid");
                }
            }
            throw new StringIndexOutOfBoundsException("startIndex must be valid");
        }
        append(charBuffer.toString(), i, i2);
        return this;
    }

    public StrBuilder append(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            return appendNull();
        }
        int length = stringBuffer.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            stringBuffer.getChars(0, length, this.buffer, length2);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StringBuffer stringBuffer, int i, int i2) {
        if (stringBuffer == null) {
            return appendNull();
        }
        if (i >= 0) {
            if (i <= stringBuffer.length()) {
                if (i2 >= 0) {
                    int i3 = i + i2;
                    if (i3 <= stringBuffer.length()) {
                        if (i2 > 0) {
                            int length = length();
                            ensureCapacity(length + i2);
                            stringBuffer.getChars(i, i3, this.buffer, length);
                            this.size += i2;
                        }
                        return this;
                    }
                }
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        }
        throw new StringIndexOutOfBoundsException("startIndex must be valid");
    }

    public StrBuilder append(StringBuilder stringBuilder) {
        if (stringBuilder == null) {
            return appendNull();
        }
        int length = stringBuilder.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            stringBuilder.getChars(0, length, this.buffer, length2);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StringBuilder stringBuilder, int i, int i2) {
        if (stringBuilder == null) {
            return appendNull();
        }
        if (i >= 0) {
            if (i <= stringBuilder.length()) {
                if (i2 >= 0) {
                    int i3 = i + i2;
                    if (i3 <= stringBuilder.length()) {
                        if (i2 > 0) {
                            int length = length();
                            ensureCapacity(length + i2);
                            stringBuilder.getChars(i, i3, this.buffer, length);
                            this.size += i2;
                        }
                        return this;
                    }
                }
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        }
        throw new StringIndexOutOfBoundsException("startIndex must be valid");
    }

    public StrBuilder append(StrBuilder strBuilder) {
        if (strBuilder == null) {
            return appendNull();
        }
        int length = strBuilder.length();
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            System.arraycopy(strBuilder.buffer, 0, this.buffer, length2, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(StrBuilder strBuilder, int i, int i2) {
        if (strBuilder == null) {
            return appendNull();
        }
        if (i >= 0) {
            if (i <= strBuilder.length()) {
                if (i2 >= 0) {
                    int i3 = i + i2;
                    if (i3 <= strBuilder.length()) {
                        if (i2 > 0) {
                            int length = length();
                            ensureCapacity(length + i2);
                            strBuilder.getChars(i, i3, this.buffer, length);
                            this.size += i2;
                        }
                        return this;
                    }
                }
                throw new StringIndexOutOfBoundsException("length must be valid");
            }
        }
        throw new StringIndexOutOfBoundsException("startIndex must be valid");
    }

    public StrBuilder append(char[] cArr) {
        if (cArr == null) {
            return appendNull();
        }
        int length = cArr.length;
        if (length > 0) {
            int length2 = length();
            ensureCapacity(length2 + length);
            System.arraycopy(cArr, 0, this.buffer, length2, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder append(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return appendNull();
        }
        if (i >= 0) {
            if (i <= cArr.length) {
                if (i2 >= 0) {
                    if (i + i2 <= cArr.length) {
                        if (i2 > 0) {
                            int length = length();
                            ensureCapacity(length + i2);
                            System.arraycopy(cArr, i, this.buffer, length, i2);
                            this.size += i2;
                        }
                        return this;
                    }
                }
                i = new StringBuilder();
                i.append("Invalid length: ");
                i.append(i2);
                throw new StringIndexOutOfBoundsException(i.toString());
            }
        }
        i = new StringBuilder();
        i.append("Invalid startIndex: ");
        i.append(i2);
        throw new StringIndexOutOfBoundsException(i.toString());
    }

    public StrBuilder append(boolean z) {
        int i;
        if (z) {
            ensureCapacity(this.size + 4);
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 't';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 'r';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 'u';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 101;
        } else {
            ensureCapacity(this.size + 5);
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 'f';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 'a';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 'l';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 's';
            z = this.buffer;
            i = this.size;
            this.size = i + 1;
            z[i] = 101;
        }
        return this;
    }

    public StrBuilder append(char c) {
        ensureCapacity(length() + 1);
        char[] cArr = this.buffer;
        int i = this.size;
        this.size = i + 1;
        cArr[i] = c;
        return this;
    }

    public StrBuilder append(int i) {
        return append(String.valueOf(i));
    }

    public StrBuilder append(long j) {
        return append(String.valueOf(j));
    }

    public StrBuilder append(float f) {
        return append(String.valueOf(f));
    }

    public StrBuilder append(double d) {
        return append(String.valueOf(d));
    }

    public StrBuilder appendln(Object obj) {
        return append(obj).appendNewLine();
    }

    public StrBuilder appendln(String str) {
        return append(str).appendNewLine();
    }

    public StrBuilder appendln(String str, int i, int i2) {
        return append(str, i, i2).appendNewLine();
    }

    public StrBuilder appendln(String str, Object... objArr) {
        return append(str, objArr).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringBuffer) {
        return append(stringBuffer).appendNewLine();
    }

    public StrBuilder appendln(StringBuilder stringBuilder) {
        return append(stringBuilder).appendNewLine();
    }

    public StrBuilder appendln(StringBuilder stringBuilder, int i, int i2) {
        return append(stringBuilder, i, i2).appendNewLine();
    }

    public StrBuilder appendln(StringBuffer stringBuffer, int i, int i2) {
        return append(stringBuffer, i, i2).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strBuilder) {
        return append(strBuilder).appendNewLine();
    }

    public StrBuilder appendln(StrBuilder strBuilder, int i, int i2) {
        return append(strBuilder, i, i2).appendNewLine();
    }

    public StrBuilder appendln(char[] cArr) {
        return append(cArr).appendNewLine();
    }

    public StrBuilder appendln(char[] cArr, int i, int i2) {
        return append(cArr, i, i2).appendNewLine();
    }

    public StrBuilder appendln(boolean z) {
        return append(z).appendNewLine();
    }

    public StrBuilder appendln(char c) {
        return append(c).appendNewLine();
    }

    public StrBuilder appendln(int i) {
        return append(i).appendNewLine();
    }

    public StrBuilder appendln(long j) {
        return append(j).appendNewLine();
    }

    public StrBuilder appendln(float f) {
        return append(f).appendNewLine();
    }

    public StrBuilder appendln(double d) {
        return append(d).appendNewLine();
    }

    public <T> StrBuilder appendAll(T... tArr) {
        if (tArr != null && tArr.length > 0) {
            for (Object append : tArr) {
                append(append);
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterable<?> iterable) {
        if (iterable != null) {
            for (Object append : iterable) {
                append(append);
            }
        }
        return this;
    }

    public StrBuilder appendAll(Iterator<?> it) {
        if (it != null) {
            while (it.hasNext()) {
                append(it.next());
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Object[] objArr, String str) {
        if (objArr != null && objArr.length > 0) {
            str = ObjectUtils.toString(str);
            append(objArr[0]);
            for (int i = 1; i < objArr.length; i++) {
                append(str);
                append(objArr[i]);
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterable<?> iterable, String str) {
        if (iterable != null) {
            str = ObjectUtils.toString(str);
            iterable = iterable.iterator();
            while (iterable.hasNext()) {
                append(iterable.next());
                if (iterable.hasNext()) {
                    append(str);
                }
            }
        }
        return this;
    }

    public StrBuilder appendWithSeparators(Iterator<?> it, String str) {
        if (it != null) {
            str = ObjectUtils.toString(str);
            while (it.hasNext()) {
                append(it.next());
                if (it.hasNext()) {
                    append(str);
                }
            }
        }
        return this;
    }

    public StrBuilder appendSeparator(String str) {
        return appendSeparator(str, null);
    }

    public StrBuilder appendSeparator(String str, String str2) {
        if (isEmpty()) {
            str = str2;
        }
        if (str != null) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendSeparator(char c) {
        if (size() > 0) {
            append(c);
        }
        return this;
    }

    public StrBuilder appendSeparator(char c, char c2) {
        if (size() > 0) {
            append(c);
        } else {
            append(c2);
        }
        return this;
    }

    public StrBuilder appendSeparator(String str, int i) {
        if (str != null && i > 0) {
            append(str);
        }
        return this;
    }

    public StrBuilder appendSeparator(char c, int i) {
        if (i > 0) {
            append(c);
        }
        return this;
    }

    public StrBuilder appendPadding(int i, char c) {
        if (i >= 0) {
            ensureCapacity(this.size + i);
            for (int i2 = 0; i2 < i; i2++) {
                char[] cArr = this.buffer;
                int i3 = this.size;
                this.size = i3 + 1;
                cArr[i3] = c;
            }
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(Object obj, int i, char c) {
        if (i > 0) {
            ensureCapacity(this.size + i);
            obj = obj == null ? getNullText() : obj.toString();
            if (obj == null) {
                obj = "";
            }
            int length = obj.length();
            if (length >= i) {
                obj.getChars(length - i, length, this.buffer, this.size);
            } else {
                int i2 = i - length;
                for (int i3 = 0; i3 < i2; i3++) {
                    this.buffer[this.size + i3] = c;
                }
                obj.getChars(0, length, this.buffer, this.size + i2);
            }
            this.size += i;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadLeft(int i, int i2, char c) {
        return appendFixedWidthPadLeft(String.valueOf(i), i2, c);
    }

    public StrBuilder appendFixedWidthPadRight(Object obj, int i, char c) {
        if (i > 0) {
            ensureCapacity(this.size + i);
            obj = obj == null ? getNullText() : obj.toString();
            if (obj == null) {
                obj = "";
            }
            int length = obj.length();
            int i2 = 0;
            if (length >= i) {
                obj.getChars(0, i, this.buffer, this.size);
            } else {
                int i3 = i - length;
                obj.getChars(0, length, this.buffer, this.size);
                while (i2 < i3) {
                    this.buffer[(this.size + length) + i2] = c;
                    i2++;
                }
            }
            this.size += i;
        }
        return this;
    }

    public StrBuilder appendFixedWidthPadRight(int i, int i2, char c) {
        return appendFixedWidthPadRight(String.valueOf(i), i2, c);
    }

    public StrBuilder insert(int i, Object obj) {
        if (obj == null) {
            return insert(i, this.nullText);
        }
        return insert(i, obj.toString());
    }

    public StrBuilder insert(int i, String str) {
        validateIndex(i);
        if (str == null) {
            str = this.nullText;
        }
        if (str != null) {
            int length = str.length();
            if (length > 0) {
                int i2 = this.size + length;
                ensureCapacity(i2);
                System.arraycopy(this.buffer, i, this.buffer, i + length, this.size - i);
                this.size = i2;
                str.getChars(0, length, this.buffer, i);
            }
        }
        return this;
    }

    public StrBuilder insert(int i, char[] cArr) {
        validateIndex(i);
        if (cArr == null) {
            return insert(i, this.nullText);
        }
        int length = cArr.length;
        if (length > 0) {
            ensureCapacity(this.size + length);
            System.arraycopy(this.buffer, i, this.buffer, i + length, this.size - i);
            System.arraycopy(cArr, 0, this.buffer, i, length);
            this.size += length;
        }
        return this;
    }

    public StrBuilder insert(int i, char[] cArr, int i2, int i3) {
        validateIndex(i);
        if (cArr == null) {
            return insert(i, this.nullText);
        }
        if (i2 >= 0) {
            if (i2 <= cArr.length) {
                if (i3 >= 0) {
                    if (i2 + i3 <= cArr.length) {
                        if (i3 > 0) {
                            ensureCapacity(this.size + i3);
                            System.arraycopy(this.buffer, i, this.buffer, i + i3, this.size - i);
                            System.arraycopy(cArr, i2, this.buffer, i, i3);
                            this.size += i3;
                        }
                        return this;
                    }
                }
                cArr = new StringBuilder();
                cArr.append("Invalid length: ");
                cArr.append(i3);
                throw new StringIndexOutOfBoundsException(cArr.toString());
            }
        }
        cArr = new StringBuilder();
        cArr.append("Invalid offset: ");
        cArr.append(i2);
        throw new StringIndexOutOfBoundsException(cArr.toString());
    }

    public StrBuilder insert(int i, boolean z) {
        validateIndex(i);
        int i2;
        if (z) {
            ensureCapacity(this.size + 4);
            System.arraycopy(this.buffer, i, this.buffer, i + 4, this.size - i);
            i2 = i + 1;
            this.buffer[i] = 't';
            z = i2 + 1;
            this.buffer[i2] = 'r';
            i2 = z + 1;
            this.buffer[z] = 'u';
            this.buffer[i2] = 101;
            this.size += 4;
        } else {
            ensureCapacity(this.size + 5);
            System.arraycopy(this.buffer, i, this.buffer, i + 5, this.size - i);
            i2 = i + 1;
            this.buffer[i] = 'f';
            z = i2 + 1;
            this.buffer[i2] = 'a';
            i2 = z + 1;
            this.buffer[z] = 'l';
            z = i2 + 1;
            this.buffer[i2] = 's';
            this.buffer[z] = 101;
            this.size += 5;
        }
        return this;
    }

    public StrBuilder insert(int i, char c) {
        validateIndex(i);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.buffer, i, this.buffer, i + 1, this.size - i);
        this.buffer[i] = c;
        this.size++;
        return this;
    }

    public StrBuilder insert(int i, int i2) {
        return insert(i, String.valueOf(i2));
    }

    public StrBuilder insert(int i, long j) {
        return insert(i, String.valueOf(j));
    }

    public StrBuilder insert(int i, float f) {
        return insert(i, String.valueOf(f));
    }

    public StrBuilder insert(int i, double d) {
        return insert(i, String.valueOf(d));
    }

    private void deleteImpl(int i, int i2, int i3) {
        System.arraycopy(this.buffer, i2, this.buffer, i, this.size - i2);
        this.size -= i3;
    }

    public StrBuilder delete(int i, int i2) {
        i2 = validateRange(i, i2);
        int i3 = i2 - i;
        if (i3 > 0) {
            deleteImpl(i, i2, i3);
        }
        return this;
    }

    public StrBuilder deleteAll(char c) {
        int i = 0;
        while (i < this.size) {
            if (this.buffer[i] == c) {
                int i2 = i;
                do {
                    i2++;
                    if (i2 >= this.size) {
                        break;
                    }
                } while (this.buffer[i2] == c);
                int i3 = i2 - i;
                deleteImpl(i, i2, i3);
                i = i2 - i3;
            }
            i++;
        }
        return this;
    }

    public StrBuilder deleteFirst(char c) {
        for (int i = 0; i < this.size; i++) {
            if (this.buffer[i] == c) {
                deleteImpl(i, i + 1, 1);
                break;
            }
        }
        return this;
    }

    public StrBuilder deleteAll(String str) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int indexOf = indexOf(str, 0);
            while (indexOf >= 0) {
                deleteImpl(indexOf, indexOf + length, length);
                indexOf = indexOf(str, indexOf);
            }
        }
        return this;
    }

    public StrBuilder deleteFirst(String str) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            str = indexOf(str, 0);
            if (str >= null) {
                deleteImpl(str, str + length, length);
            }
        }
        return this;
    }

    public StrBuilder deleteAll(StrMatcher strMatcher) {
        return replace(strMatcher, null, 0, this.size, -1);
    }

    public StrBuilder deleteFirst(StrMatcher strMatcher) {
        return replace(strMatcher, null, 0, this.size, 1);
    }

    private void replaceImpl(int i, int i2, int i3, String str, int i4) {
        int i5 = (this.size - i3) + i4;
        if (i4 != i3) {
            ensureCapacity(i5);
            System.arraycopy(this.buffer, i2, this.buffer, i + i4, this.size - i2);
            this.size = i5;
        }
        if (i4 > 0) {
            str.getChars(0, i4, this.buffer, i);
        }
    }

    public StrBuilder replace(int i, int i2, String str) {
        int i3;
        int validateRange = validateRange(i, i2);
        if (str == null) {
            i3 = 0;
        } else {
            i3 = str.length();
        }
        replaceImpl(i, validateRange, validateRange - i, str, i3);
        return this;
    }

    public StrBuilder replaceAll(char c, char c2) {
        if (c != c2) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == c) {
                    this.buffer[i] = c2;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(char c, char c2) {
        if (c != c2) {
            for (int i = 0; i < this.size; i++) {
                if (this.buffer[i] == c) {
                    this.buffer[i] = c2;
                    break;
                }
            }
        }
        return this;
    }

    public StrBuilder replaceAll(String str, String str2) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int i;
            if (str2 == null) {
                i = 0;
            } else {
                i = str2.length();
            }
            int indexOf = indexOf(str, 0);
            while (indexOf >= 0) {
                replaceImpl(indexOf, indexOf + length, length, str2, i);
                indexOf = indexOf(str, indexOf + i);
            }
        }
        return this;
    }

    public StrBuilder replaceFirst(String str, String str2) {
        int length = str == null ? 0 : str.length();
        if (length > 0) {
            int indexOf = indexOf(str, 0);
            if (indexOf >= 0) {
                int i;
                if (str2 == null) {
                    i = 0;
                } else {
                    i = str2.length();
                }
                replaceImpl(indexOf, indexOf + length, length, str2, i);
            }
        }
        return this;
    }

    public StrBuilder replaceAll(StrMatcher strMatcher, String str) {
        return replace(strMatcher, str, 0, this.size, -1);
    }

    public StrBuilder replaceFirst(StrMatcher strMatcher, String str) {
        return replace(strMatcher, str, 0, this.size, 1);
    }

    public StrBuilder replace(StrMatcher strMatcher, String str, int i, int i2, int i3) {
        return replaceImpl(strMatcher, str, i, validateRange(i, i2), i3);
    }

    private StrBuilder replaceImpl(StrMatcher strMatcher, String str, int i, int i2, int i3) {
        if (strMatcher != null) {
            if (this.size != 0) {
                int i4;
                if (str == null) {
                    i4 = 0;
                } else {
                    i4 = str.length();
                }
                char[] cArr = this.buffer;
                int i5 = i3;
                i3 = i2;
                i2 = i;
                while (i2 < i3 && i5 != 0) {
                    int isMatch = strMatcher.isMatch(cArr, i2, i, i3);
                    if (isMatch > 0) {
                        replaceImpl(i2, i2 + isMatch, isMatch, str, i4);
                        i3 = (i3 - isMatch) + i4;
                        i2 = (i2 + i4) - 1;
                        if (i5 > 0) {
                            i5--;
                        }
                    }
                    i2++;
                }
                return this;
            }
        }
        return this;
    }

    public StrBuilder reverse() {
        if (this.size == 0) {
            return this;
        }
        int i = this.size / 2;
        char[] cArr = this.buffer;
        int i2 = 0;
        int i3 = this.size - 1;
        while (i2 < i) {
            char c = cArr[i2];
            cArr[i2] = cArr[i3];
            cArr[i3] = c;
            i2++;
            i3--;
        }
        return this;
    }

    public StrBuilder trim() {
        if (this.size == 0) {
            return this;
        }
        int i = this.size;
        char[] cArr = this.buffer;
        int i2 = 0;
        while (i2 < i && cArr[i2] <= ' ') {
            i2++;
        }
        while (i2 < i && cArr[i - 1] <= ' ') {
            i--;
        }
        if (i < this.size) {
            delete(i, this.size);
        }
        if (i2 > 0) {
            delete(0, i2);
        }
        return this;
    }

    public boolean startsWith(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return true;
        }
        if (length > this.size) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.buffer[i] != str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean endsWith(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return true;
        }
        if (length > this.size) {
            return false;
        }
        int i = this.size - length;
        int i2 = 0;
        while (i2 < length) {
            if (this.buffer[i] != str.charAt(i2)) {
                return false;
            }
            i2++;
            i++;
        }
        return true;
    }

    public CharSequence subSequence(int i, int i2) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        } else if (i2 > this.size) {
            throw new StringIndexOutOfBoundsException(i2);
        } else if (i <= i2) {
            return substring(i, i2);
        } else {
            throw new StringIndexOutOfBoundsException(i2 - i);
        }
    }

    public String substring(int i) {
        return substring(i, this.size);
    }

    public String substring(int i, int i2) {
        return new String(this.buffer, i, validateRange(i, i2) - i);
    }

    public String leftString(int i) {
        if (i <= 0) {
            return "";
        }
        if (i >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, 0, i);
    }

    public String rightString(int i) {
        if (i <= 0) {
            return "";
        }
        if (i >= this.size) {
            return new String(this.buffer, 0, this.size);
        }
        return new String(this.buffer, this.size - i, i);
    }

    public String midString(int i, int i2) {
        if (i < 0) {
            i = 0;
        }
        if (i2 > 0) {
            if (i < this.size) {
                if (this.size <= i + i2) {
                    return new String(this.buffer, i, this.size - i);
                }
                return new String(this.buffer, i, i2);
            }
        }
        return "";
    }

    public boolean contains(char c) {
        char[] cArr = this.buffer;
        for (int i = 0; i < this.size; i++) {
            if (cArr[i] == c) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(String str) {
        return indexOf(str, 0) >= null;
    }

    public boolean contains(StrMatcher strMatcher) {
        return indexOf(strMatcher, 0) >= null;
    }

    public int indexOf(char c) {
        return indexOf(c, 0);
    }

    public int indexOf(char c, int i) {
        if (i < 0) {
            i = 0;
        }
        if (i >= this.size) {
            return -1;
        }
        char[] cArr = this.buffer;
        while (i < this.size) {
            if (cArr[i] == c) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    public int indexOf(String str, int i) {
        if (i < 0) {
            i = 0;
        }
        if (str != null) {
            if (i < this.size) {
                int length = str.length();
                if (length == 1) {
                    return indexOf(str.charAt(0), i);
                }
                if (length == 0) {
                    return i;
                }
                if (length > this.size) {
                    return -1;
                }
                char[] cArr = this.buffer;
                int i2 = (this.size - length) + 1;
                while (i < i2) {
                    int i3 = 0;
                    while (i3 < length) {
                        if (str.charAt(i3) != cArr[i + i3]) {
                            i++;
                        } else {
                            i3++;
                        }
                    }
                    return i;
                }
                return -1;
            }
        }
        return -1;
    }

    public int indexOf(StrMatcher strMatcher) {
        return indexOf(strMatcher, 0);
    }

    public int indexOf(StrMatcher strMatcher, int i) {
        if (i < 0) {
            i = 0;
        }
        if (strMatcher != null) {
            if (i < this.size) {
                int i2 = this.size;
                char[] cArr = this.buffer;
                for (int i3 = i; i3 < i2; i3++) {
                    if (strMatcher.isMatch(cArr, i3, i, i2) > 0) {
                        return i3;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public int lastIndexOf(char c) {
        return lastIndexOf(c, this.size - 1);
    }

    public int lastIndexOf(char c, int i) {
        if (i >= this.size) {
            i = this.size - 1;
        }
        if (i < 0) {
            return -1;
        }
        while (i >= 0) {
            if (this.buffer[i] == c) {
                return i;
            }
            i--;
        }
        return -1;
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.size - 1);
    }

    public int lastIndexOf(String str, int i) {
        if (i >= this.size) {
            i = this.size - 1;
        }
        if (str != null) {
            if (i >= 0) {
                int length = str.length();
                if (length <= 0 || length > this.size) {
                    if (length == 0) {
                        return i;
                    }
                } else if (length == 1) {
                    return lastIndexOf(str.charAt(0), i);
                } else {
                    i = (i - length) + 1;
                    while (i >= 0) {
                        int i2 = 0;
                        while (i2 < length) {
                            if (str.charAt(i2) != this.buffer[i + i2]) {
                                i--;
                            } else {
                                i2++;
                            }
                        }
                        return i;
                    }
                }
                return -1;
            }
        }
        return -1;
    }

    public int lastIndexOf(StrMatcher strMatcher) {
        return lastIndexOf(strMatcher, this.size);
    }

    public int lastIndexOf(StrMatcher strMatcher, int i) {
        if (i >= this.size) {
            i = this.size - 1;
        }
        if (strMatcher != null) {
            if (i >= 0) {
                char[] cArr = this.buffer;
                int i2 = i + 1;
                while (i >= 0) {
                    if (strMatcher.isMatch(cArr, i, 0, i2) > 0) {
                        return i;
                    }
                    i--;
                }
                return -1;
            }
        }
        return -1;
    }

    public StrTokenizer asTokenizer() {
        return new StrBuilderTokenizer();
    }

    public Reader asReader() {
        return new StrBuilderReader();
    }

    public Writer asWriter() {
        return new StrBuilderWriter();
    }

    public void appendTo(Appendable appendable) throws IOException {
        if (appendable instanceof Writer) {
            ((Writer) appendable).write(this.buffer, 0, this.size);
        } else if (appendable instanceof StringBuilder) {
            ((StringBuilder) appendable).append(this.buffer, 0, this.size);
        } else if (appendable instanceof StringBuffer) {
            ((StringBuffer) appendable).append(this.buffer, 0, this.size);
        } else if (appendable instanceof CharBuffer) {
            ((CharBuffer) appendable).put(this.buffer, 0, this.size);
        } else {
            appendable.append(this);
        }
    }

    public boolean equalsIgnoreCase(StrBuilder strBuilder) {
        if (this == strBuilder) {
            return true;
        }
        if (this.size != strBuilder.size) {
            return false;
        }
        char[] cArr = this.buffer;
        strBuilder = strBuilder.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            char c = cArr[i];
            char c2 = strBuilder[i];
            if (c != c2 && Character.toUpperCase(c) != Character.toUpperCase(c2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(StrBuilder strBuilder) {
        if (this == strBuilder) {
            return true;
        }
        if (strBuilder == null || this.size != strBuilder.size) {
            return false;
        }
        char[] cArr = this.buffer;
        strBuilder = strBuilder.buffer;
        for (int i = this.size - 1; i >= 0; i--) {
            if (cArr[i] != strBuilder[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        return (!(obj instanceof StrBuilder) || equals((StrBuilder) obj) == null) ? null : true;
    }

    public int hashCode() {
        char[] cArr = this.buffer;
        int i = 0;
        for (int i2 = this.size - 1; i2 >= 0; i2--) {
            i = (i * 31) + cArr[i2];
        }
        return i;
    }

    public String toString() {
        return new String(this.buffer, 0, this.size);
    }

    public StringBuffer toStringBuffer() {
        StringBuffer stringBuffer = new StringBuffer(this.size);
        stringBuffer.append(this.buffer, 0, this.size);
        return stringBuffer;
    }

    public StringBuilder toStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder(this.size);
        stringBuilder.append(this.buffer, 0, this.size);
        return stringBuilder;
    }

    public String build() {
        return toString();
    }

    protected int validateRange(int i, int i2) {
        if (i < 0) {
            throw new StringIndexOutOfBoundsException(i);
        }
        if (i2 > this.size) {
            i2 = this.size;
        }
        if (i <= i2) {
            return i2;
        }
        throw new StringIndexOutOfBoundsException("end < start");
    }

    protected void validateIndex(int i) {
        if (i >= 0) {
            if (i <= this.size) {
                return;
            }
        }
        throw new StringIndexOutOfBoundsException(i);
    }
}
