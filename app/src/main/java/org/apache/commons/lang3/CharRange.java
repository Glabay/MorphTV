package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class CharRange implements Iterable<Character>, Serializable {
    private static final long serialVersionUID = 8270183163158333422L;
    private final char end;
    private transient String iToString;
    private final boolean negated;
    private final char start;

    private static class CharacterIterator implements Iterator<Character> {
        private char current;
        private boolean hasNext;
        private final CharRange range;

        private CharacterIterator(CharRange charRange) {
            this.range = charRange;
            this.hasNext = true;
            if (!this.range.negated) {
                this.current = this.range.start;
            } else if (this.range.start != '\u0000') {
                this.current = '\u0000';
            } else if (this.range.end == '￿') {
                this.hasNext = false;
            } else {
                this.current = (char) (this.range.end + 1);
            }
        }

        private void prepareNext() {
            if (this.range.negated) {
                if (this.current == '￿') {
                    this.hasNext = false;
                } else if (this.current + 1 != this.range.start) {
                    this.current = (char) (this.current + 1);
                } else if (this.range.end == '￿') {
                    this.hasNext = false;
                } else {
                    this.current = (char) (this.range.end + 1);
                }
            } else if (this.current < this.range.end) {
                this.current = (char) (this.current + 1);
            } else {
                this.hasNext = false;
            }
        }

        public boolean hasNext() {
            return this.hasNext;
        }

        public Character next() {
            if (this.hasNext) {
                char c = this.current;
                prepareNext();
                return Character.valueOf(c);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private CharRange(char c, char c2, boolean z) {
        if (c > c2) {
            char c3 = c2;
            c2 = c;
            c = c3;
        }
        this.start = c;
        this.end = c2;
        this.negated = z;
    }

    public static CharRange is(char c) {
        return new CharRange(c, c, false);
    }

    public static CharRange isNot(char c) {
        return new CharRange(c, c, true);
    }

    public static CharRange isIn(char c, char c2) {
        return new CharRange(c, c2, false);
    }

    public static CharRange isNotIn(char c, char c2) {
        return new CharRange(c, c2, true);
    }

    public char getStart() {
        return this.start;
    }

    public char getEnd() {
        return this.end;
    }

    public boolean isNegated() {
        return this.negated;
    }

    public boolean contains(char c) {
        boolean z = c >= this.start && c <= this.end;
        return z != this.negated;
    }

    public boolean contains(CharRange charRange) {
        if (charRange == null) {
            throw new IllegalArgumentException("The Range must not be null");
        }
        boolean z = false;
        if (this.negated) {
            if (charRange.negated) {
                if (this.start >= charRange.start && this.end <= charRange.end) {
                    z = true;
                }
                return z;
            }
            if (charRange.end < this.start || charRange.start > this.end) {
                z = true;
            }
            return z;
        } else if (charRange.negated) {
            if (this.start == null && this.end == 65535) {
                z = true;
            }
            return z;
        } else {
            if (this.start <= charRange.start && this.end >= charRange.end) {
                z = true;
            }
            return z;
        }
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CharRange)) {
            return false;
        }
        CharRange charRange = (CharRange) obj;
        if (this.start != charRange.start || this.end != charRange.end || this.negated != charRange.negated) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((this.start + 83) + (this.end * 7)) + this.negated;
    }

    public String toString() {
        if (this.iToString == null) {
            StringBuilder stringBuilder = new StringBuilder(4);
            if (isNegated()) {
                stringBuilder.append('^');
            }
            stringBuilder.append(this.start);
            if (this.start != this.end) {
                stringBuilder.append('-');
                stringBuilder.append(this.end);
            }
            this.iToString = stringBuilder.toString();
        }
        return this.iToString;
    }

    public Iterator<Character> iterator() {
        return new CharacterIterator();
    }
}
