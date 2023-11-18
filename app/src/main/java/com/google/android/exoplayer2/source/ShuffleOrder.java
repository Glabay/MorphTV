package com.google.android.exoplayer2.source;

import java.util.Arrays;
import java.util.Random;

public interface ShuffleOrder {

    public static class DefaultShuffleOrder implements ShuffleOrder {
        private final int[] indexInShuffled;
        private final Random random;
        private final int[] shuffled;

        public DefaultShuffleOrder(int i) {
            this(i, new Random());
        }

        public DefaultShuffleOrder(int i, long j) {
            this(i, new Random(j));
        }

        private DefaultShuffleOrder(int i, Random random) {
            this(createShuffledList(i, random), random);
        }

        private DefaultShuffleOrder(int[] iArr, Random random) {
            this.shuffled = iArr;
            this.random = random;
            this.indexInShuffled = new int[iArr.length];
            for (random = null; random < iArr.length; random++) {
                this.indexInShuffled[iArr[random]] = random;
            }
        }

        public int getLength() {
            return this.shuffled.length;
        }

        public int getNextIndex(int i) {
            i = this.indexInShuffled[i] + 1;
            return i < this.shuffled.length ? this.shuffled[i] : -1;
        }

        public int getPreviousIndex(int i) {
            i = this.indexInShuffled[i] - 1;
            if (i >= 0) {
                return this.shuffled[i];
            }
            return -1;
        }

        public int getLastIndex() {
            return this.shuffled.length > 0 ? this.shuffled[this.shuffled.length - 1] : -1;
        }

        public int getFirstIndex() {
            return this.shuffled.length > 0 ? this.shuffled[0] : -1;
        }

        public ShuffleOrder cloneAndInsert(int i, int i2) {
            int i3;
            int nextInt;
            int[] iArr = new int[i2];
            int[] iArr2 = new int[i2];
            int i4 = 0;
            while (i4 < i2) {
                iArr[i4] = this.random.nextInt(this.shuffled.length + 1);
                i3 = i4 + 1;
                nextInt = this.random.nextInt(i3);
                iArr2[i4] = iArr2[nextInt];
                iArr2[nextInt] = i4 + i;
                i4 = i3;
            }
            Arrays.sort(iArr);
            int[] iArr3 = new int[(this.shuffled.length + i2)];
            nextInt = 0;
            i3 = 0;
            for (int i5 = 0; i5 < this.shuffled.length + i2; i5++) {
                if (nextInt >= i2 || i3 != iArr[nextInt]) {
                    int i6 = i3 + 1;
                    iArr3[i5] = this.shuffled[i3];
                    if (iArr3[i5] >= i) {
                        iArr3[i5] = iArr3[i5] + i2;
                    }
                    i3 = i6;
                } else {
                    int i7 = nextInt + 1;
                    iArr3[i5] = iArr2[nextInt];
                    nextInt = i7;
                }
            }
            return new DefaultShuffleOrder(iArr3, new Random(this.random.nextLong()));
        }

        public ShuffleOrder cloneAndRemove(int i) {
            int[] iArr = new int[(this.shuffled.length - 1)];
            int i2 = 0;
            Object obj = null;
            while (i2 < this.shuffled.length) {
                if (this.shuffled[i2] == i) {
                    obj = 1;
                } else {
                    iArr[obj != null ? i2 - 1 : i2] = this.shuffled[i2] > i ? this.shuffled[i2] - 1 : this.shuffled[i2];
                }
                i2++;
            }
            return new DefaultShuffleOrder(iArr, new Random(this.random.nextLong()));
        }

        public ShuffleOrder cloneAndClear() {
            return new DefaultShuffleOrder(0, new Random(this.random.nextLong()));
        }

        private static int[] createShuffledList(int i, Random random) {
            int[] iArr = new int[i];
            int i2 = 0;
            while (i2 < i) {
                int i3 = i2 + 1;
                int nextInt = random.nextInt(i3);
                iArr[i2] = iArr[nextInt];
                iArr[nextInt] = i2;
                i2 = i3;
            }
            return iArr;
        }
    }

    public static final class UnshuffledShuffleOrder implements ShuffleOrder {
        private final int length;

        public int getPreviousIndex(int i) {
            i--;
            return i >= 0 ? i : -1;
        }

        public UnshuffledShuffleOrder(int i) {
            this.length = i;
        }

        public int getLength() {
            return this.length;
        }

        public int getNextIndex(int i) {
            i++;
            return i < this.length ? i : -1;
        }

        public int getLastIndex() {
            return this.length > 0 ? this.length - 1 : -1;
        }

        public int getFirstIndex() {
            return this.length > 0 ? 0 : -1;
        }

        public ShuffleOrder cloneAndInsert(int i, int i2) {
            return new UnshuffledShuffleOrder(this.length + i2);
        }

        public ShuffleOrder cloneAndRemove(int i) {
            return new UnshuffledShuffleOrder(this.length - 1);
        }

        public ShuffleOrder cloneAndClear() {
            return new UnshuffledShuffleOrder(0);
        }
    }

    ShuffleOrder cloneAndClear();

    ShuffleOrder cloneAndInsert(int i, int i2);

    ShuffleOrder cloneAndRemove(int i);

    int getFirstIndex();

    int getLastIndex();

    int getLength();

    int getNextIndex(int i);

    int getPreviousIndex(int i);
}
