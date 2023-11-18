package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class TreeMultiset<E> extends AbstractSortedMultiset<E> implements Serializable {
    @GwtIncompatible("not needed in emulated source")
    private static final long serialVersionUID = 1;
    private final transient AvlNode<E> header;
    private final transient GeneralRange<E> range;
    private final transient Reference<AvlNode<E>> rootReference;

    /* renamed from: com.google.common.collect.TreeMultiset$2 */
    class C10922 implements Iterator<Entry<E>> {
        AvlNode<E> current = TreeMultiset.this.firstNode();
        Entry<E> prevEntry;

        C10922() {
        }

        public boolean hasNext() {
            if (this.current == null) {
                return false;
            }
            if (!TreeMultiset.this.range.tooHigh(this.current.getElement())) {
                return true;
            }
            this.current = null;
            return false;
        }

        public Entry<E> next() {
            if (hasNext()) {
                Entry<E> access$1400 = TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = access$1400;
                if (this.current.succ == TreeMultiset.this.header) {
                    this.current = null;
                } else {
                    this.current = this.current.succ;
                }
                return access$1400;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.prevEntry != null);
            TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
        }
    }

    /* renamed from: com.google.common.collect.TreeMultiset$3 */
    class C10933 implements Iterator<Entry<E>> {
        AvlNode<E> current = TreeMultiset.this.lastNode();
        Entry<E> prevEntry = null;

        C10933() {
        }

        public boolean hasNext() {
            if (this.current == null) {
                return false;
            }
            if (!TreeMultiset.this.range.tooLow(this.current.getElement())) {
                return true;
            }
            this.current = null;
            return false;
        }

        public Entry<E> next() {
            if (hasNext()) {
                Entry<E> access$1400 = TreeMultiset.this.wrapEntry(this.current);
                this.prevEntry = access$1400;
                if (this.current.pred == TreeMultiset.this.header) {
                    this.current = null;
                } else {
                    this.current = this.current.pred;
                }
                return access$1400;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            CollectPreconditions.checkRemove(this.prevEntry != null);
            TreeMultiset.this.setCount(this.prevEntry.getElement(), 0);
            this.prevEntry = null;
        }
    }

    private enum Aggregate {
        SIZE {
            int nodeAggregate(AvlNode<?> avlNode) {
                return avlNode.elemCount;
            }

            long treeAggregate(@Nullable AvlNode<?> avlNode) {
                return avlNode == null ? 0 : avlNode.totalCount;
            }
        },
        DISTINCT {
            int nodeAggregate(AvlNode<?> avlNode) {
                return 1;
            }

            long treeAggregate(@Nullable AvlNode<?> avlNode) {
                return avlNode == null ? 0 : (long) avlNode.distinctElements;
            }
        };

        abstract int nodeAggregate(AvlNode<?> avlNode);

        abstract long treeAggregate(@Nullable AvlNode<?> avlNode);
    }

    private static final class AvlNode<E> extends AbstractEntry<E> {
        private int distinctElements;
        @Nullable
        private final E elem;
        private int elemCount;
        private int height;
        private AvlNode<E> left;
        private AvlNode<E> pred;
        private AvlNode<E> right;
        private AvlNode<E> succ;
        private long totalCount;

        AvlNode(@Nullable E e, int i) {
            Preconditions.checkArgument(i > 0);
            this.elem = e;
            this.elemCount = i;
            this.totalCount = (long) i;
            this.distinctElements = 1;
            this.height = 1;
            this.left = null;
            this.right = null;
        }

        public int count(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            int i = 0;
            if (compare < 0) {
                if (this.left != null) {
                    i = this.left.count(comparator, e);
                }
                return i;
            } else if (compare <= 0) {
                return this.elemCount;
            } else {
                if (this.right != null) {
                    i = this.right.count(comparator, e);
                }
                return i;
            }
        }

        private AvlNode<E> addRightChild(E e, int i) {
            this.right = new AvlNode(e, i);
            TreeMultiset.successor(this, this.right, this.succ);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) i;
            return this;
        }

        private AvlNode<E> addLeftChild(E e, int i) {
            this.left = new AvlNode(e, i);
            TreeMultiset.successor(this.pred, this.left, this);
            this.height = Math.max(2, this.height);
            this.distinctElements++;
            this.totalCount += (long) i;
            return this;
        }

        AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            boolean z = true;
            AvlNode avlNode;
            int i2;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return addLeftChild(e, i);
                }
                i2 = avlNode.height;
                this.left = avlNode.add(comparator, e, i, iArr);
                if (iArr[0] == null) {
                    this.distinctElements += 1;
                }
                this.totalCount += (long) i;
                return this.left.height == i2 ? this : rebalance();
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return addRightChild(e, i);
                }
                i2 = avlNode.height;
                this.right = avlNode.add(comparator, e, i, iArr);
                if (iArr[0] == null) {
                    this.distinctElements += 1;
                }
                this.totalCount += (long) i;
                return this.right.height == i2 ? this : rebalance();
            } else {
                iArr[0] = this.elemCount;
                long j = (long) i;
                if (((long) this.elemCount) + j > Integer.MAX_VALUE) {
                    z = false;
                }
                Preconditions.checkArgument(z);
                this.elemCount += i;
                this.totalCount += j;
                return this;
            }
        }

        AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.left = avlNode.remove(comparator, e, i, iArr);
                if (iArr[0] > null) {
                    if (i >= iArr[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) iArr[0];
                    } else {
                        this.totalCount -= (long) i;
                    }
                }
                return iArr[0] == null ? this : rebalance();
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return this;
                }
                this.right = avlNode.remove(comparator, e, i, iArr);
                if (iArr[0] > null) {
                    if (i >= iArr[0]) {
                        this.distinctElements--;
                        this.totalCount -= (long) iArr[0];
                    } else {
                        this.totalCount -= (long) i;
                    }
                }
                return rebalance();
            } else {
                iArr[0] = this.elemCount;
                if (i >= this.elemCount) {
                    return deleteMe();
                }
                this.elemCount -= i;
                this.totalCount -= (long) i;
                return this;
            }
        }

        AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int i, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return i > 0 ? addLeftChild(e, i) : this;
                }
                this.left = avlNode.setCount(comparator, e, i, iArr);
                if (i == 0 && iArr[0] != null) {
                    this.distinctElements--;
                } else if (i > 0 && iArr[0] == null) {
                    this.distinctElements++;
                }
                this.totalCount += (long) (i - iArr[0]);
                return rebalance();
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return i > 0 ? addRightChild(e, i) : this;
                }
                this.right = avlNode.setCount(comparator, e, i, iArr);
                if (i == 0 && iArr[0] != null) {
                    this.distinctElements--;
                } else if (i > 0 && iArr[0] == null) {
                    this.distinctElements++;
                }
                this.totalCount += (long) (i - iArr[0]);
                return rebalance();
            } else {
                iArr[0] = this.elemCount;
                if (i == 0) {
                    return deleteMe();
                }
                this.totalCount += (long) (i - this.elemCount);
                this.elemCount = i;
                return this;
            }
        }

        AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int i, int i2, int[] iArr) {
            int compare = comparator.compare(e, this.elem);
            AvlNode avlNode;
            if (compare < 0) {
                avlNode = this.left;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return (i != 0 || i2 <= 0) ? this : addLeftChild(e, i2);
                } else {
                    this.left = avlNode.setCount(comparator, e, i, i2, iArr);
                    if (iArr[0] == i) {
                        if (i2 == 0 && iArr[0] != null) {
                            this.distinctElements--;
                        } else if (i2 > 0 && iArr[0] == null) {
                            this.distinctElements++;
                        }
                        this.totalCount += (long) (i2 - iArr[0]);
                    }
                    return rebalance();
                }
            } else if (compare > 0) {
                avlNode = this.right;
                if (avlNode == null) {
                    iArr[0] = 0;
                    return (i != 0 || i2 <= 0) ? this : addRightChild(e, i2);
                } else {
                    this.right = avlNode.setCount(comparator, e, i, i2, iArr);
                    if (iArr[0] == i) {
                        if (i2 == 0 && iArr[0] != null) {
                            this.distinctElements--;
                        } else if (i2 > 0 && iArr[0] == null) {
                            this.distinctElements++;
                        }
                        this.totalCount += (long) (i2 - iArr[0]);
                    }
                    return rebalance();
                }
            } else {
                iArr[0] = this.elemCount;
                if (i == this.elemCount) {
                    if (i2 == 0) {
                        return deleteMe();
                    }
                    this.totalCount += (long) (i2 - this.elemCount);
                    this.elemCount = i2;
                }
                return this;
            }
        }

        private AvlNode<E> deleteMe() {
            int i = this.elemCount;
            this.elemCount = 0;
            TreeMultiset.successor(this.pred, this.succ);
            if (this.left == null) {
                return this.right;
            }
            if (this.right == null) {
                return this.left;
            }
            if (this.left.height >= this.right.height) {
                AvlNode avlNode = this.pred;
                avlNode.left = this.left.removeMax(avlNode);
                avlNode.right = this.right;
                avlNode.distinctElements = this.distinctElements - 1;
                avlNode.totalCount = this.totalCount - ((long) i);
                return avlNode.rebalance();
            }
            avlNode = this.succ;
            avlNode.right = this.right.removeMin(avlNode);
            avlNode.left = this.left;
            avlNode.distinctElements = this.distinctElements - 1;
            avlNode.totalCount = this.totalCount - ((long) i);
            return avlNode.rebalance();
        }

        private AvlNode<E> removeMin(AvlNode<E> avlNode) {
            if (this.left == null) {
                return this.right;
            }
            this.left = this.left.removeMin(avlNode);
            this.distinctElements--;
            this.totalCount -= (long) avlNode.elemCount;
            return rebalance();
        }

        private AvlNode<E> removeMax(AvlNode<E> avlNode) {
            if (this.right == null) {
                return this.left;
            }
            this.right = this.right.removeMax(avlNode);
            this.distinctElements--;
            this.totalCount -= (long) avlNode.elemCount;
            return rebalance();
        }

        private void recomputeMultiset() {
            this.distinctElements = (TreeMultiset.distinctElements(this.left) + 1) + TreeMultiset.distinctElements(this.right);
            this.totalCount = (((long) this.elemCount) + totalCount(this.left)) + totalCount(this.right);
        }

        private void recomputeHeight() {
            this.height = Math.max(height(this.left), height(this.right)) + 1;
        }

        private void recompute() {
            recomputeMultiset();
            recomputeHeight();
        }

        private AvlNode<E> rebalance() {
            int balanceFactor = balanceFactor();
            if (balanceFactor == -2) {
                if (this.right.balanceFactor() > 0) {
                    this.right = this.right.rotateRight();
                }
                return rotateLeft();
            } else if (balanceFactor != 2) {
                recomputeHeight();
                return this;
            } else {
                if (this.left.balanceFactor() < 0) {
                    this.left = this.left.rotateLeft();
                }
                return rotateRight();
            }
        }

        private int balanceFactor() {
            return height(this.left) - height(this.right);
        }

        private AvlNode<E> rotateLeft() {
            Preconditions.checkState(this.right != null);
            AvlNode<E> avlNode = this.right;
            this.right = avlNode.left;
            avlNode.left = this;
            avlNode.totalCount = this.totalCount;
            avlNode.distinctElements = this.distinctElements;
            recompute();
            avlNode.recomputeHeight();
            return avlNode;
        }

        private AvlNode<E> rotateRight() {
            Preconditions.checkState(this.left != null);
            AvlNode<E> avlNode = this.left;
            this.left = avlNode.right;
            avlNode.right = this;
            avlNode.totalCount = this.totalCount;
            avlNode.distinctElements = this.distinctElements;
            recompute();
            avlNode.recomputeHeight();
            return avlNode;
        }

        private static long totalCount(@Nullable AvlNode<?> avlNode) {
            return avlNode == null ? 0 : avlNode.totalCount;
        }

        private static int height(@Nullable AvlNode<?> avlNode) {
            return avlNode == null ? null : avlNode.height;
        }

        @Nullable
        private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            if (compare < 0) {
                return this.left == null ? this : (AvlNode) MoreObjects.firstNonNull(this.left.ceiling(comparator, e), this);
            } else if (compare == 0) {
                return this;
            } else {
                return this.right == null ? null : this.right.ceiling(comparator, e);
            }
        }

        @Nullable
        private AvlNode<E> floor(Comparator<? super E> comparator, E e) {
            int compare = comparator.compare(e, this.elem);
            if (compare > 0) {
                return this.right == null ? this : (AvlNode) MoreObjects.firstNonNull(this.right.floor(comparator, e), this);
            } else if (compare == 0) {
                return this;
            } else {
                return this.left == null ? null : this.left.floor(comparator, e);
            }
        }

        public E getElement() {
            return this.elem;
        }

        public int getCount() {
            return this.elemCount;
        }

        public String toString() {
            return Multisets.immutableEntry(getElement(), getCount()).toString();
        }
    }

    private static final class Reference<T> {
        @Nullable
        private T value;

        private Reference() {
        }

        @Nullable
        public T get() {
            return this.value;
        }

        public void checkAndSet(@Nullable T t, T t2) {
            if (this.value != t) {
                throw new ConcurrentModificationException();
            }
            this.value = t2;
        }
    }

    public /* bridge */ /* synthetic */ boolean add(Object obj) {
        return super.add(obj);
    }

    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ Comparator comparator() {
        return super.comparator();
    }

    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    public /* bridge */ /* synthetic */ SortedMultiset descendingMultiset() {
        return super.descendingMultiset();
    }

    public /* bridge */ /* synthetic */ NavigableSet elementSet() {
        return super.elementSet();
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    public /* bridge */ /* synthetic */ Entry firstEntry() {
        return super.firstEntry();
    }

    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    public /* bridge */ /* synthetic */ Entry lastEntry() {
        return super.lastEntry();
    }

    public /* bridge */ /* synthetic */ Entry pollFirstEntry() {
        return super.pollFirstEntry();
    }

    public /* bridge */ /* synthetic */ Entry pollLastEntry() {
        return super.pollLastEntry();
    }

    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    public /* bridge */ /* synthetic */ SortedMultiset subMultiset(Object obj, BoundType boundType, Object obj2, BoundType boundType2) {
        return super.subMultiset(obj, boundType, obj2, boundType2);
    }

    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <E extends Comparable> TreeMultiset<E> create() {
        return new TreeMultiset(Ordering.natural());
    }

    public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
        return comparator == null ? new TreeMultiset(Ordering.natural()) : new TreeMultiset(comparator);
    }

    public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> iterable) {
        Object create = create();
        Iterables.addAll(create, iterable);
        return create;
    }

    TreeMultiset(Reference<AvlNode<E>> reference, GeneralRange<E> generalRange, AvlNode<E> avlNode) {
        super(generalRange.comparator());
        this.rootReference = reference;
        this.range = generalRange;
        this.header = avlNode;
    }

    TreeMultiset(Comparator<? super E> comparator) {
        super(comparator);
        this.range = GeneralRange.all(comparator);
        this.header = new AvlNode(null, 1);
        successor(this.header, this.header);
        this.rootReference = new Reference();
    }

    private long aggregateForEntries(Aggregate aggregate) {
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        long treeAggregate = aggregate.treeAggregate(avlNode);
        if (this.range.hasLowerBound()) {
            treeAggregate -= aggregateBelowRange(aggregate, avlNode);
        }
        return this.range.hasUpperBound() ? treeAggregate - aggregateAboveRange(aggregate, avlNode) : treeAggregate;
    }

    private long aggregateBelowRange(Aggregate aggregate, @Nullable AvlNode<E> avlNode) {
        if (avlNode == null) {
            return null;
        }
        int compare = comparator().compare(this.range.getLowerEndpoint(), avlNode.elem);
        if (compare < 0) {
            return aggregateBelowRange(aggregate, avlNode.left);
        }
        if (compare != 0) {
            return (aggregate.treeAggregate(avlNode.left) + ((long) aggregate.nodeAggregate(avlNode))) + aggregateBelowRange(aggregate, avlNode.right);
        }
        switch (this.range.getLowerBoundType()) {
            case OPEN:
                return ((long) aggregate.nodeAggregate(avlNode)) + aggregate.treeAggregate(avlNode.left);
            case CLOSED:
                return aggregate.treeAggregate(avlNode.left);
            default:
                throw new AssertionError();
        }
    }

    private long aggregateAboveRange(Aggregate aggregate, @Nullable AvlNode<E> avlNode) {
        if (avlNode == null) {
            return null;
        }
        int compare = comparator().compare(this.range.getUpperEndpoint(), avlNode.elem);
        if (compare > 0) {
            return aggregateAboveRange(aggregate, avlNode.right);
        }
        if (compare != 0) {
            return (aggregate.treeAggregate(avlNode.right) + ((long) aggregate.nodeAggregate(avlNode))) + aggregateAboveRange(aggregate, avlNode.left);
        }
        switch (this.range.getUpperBoundType()) {
            case OPEN:
                return ((long) aggregate.nodeAggregate(avlNode)) + aggregate.treeAggregate(avlNode.right);
            case CLOSED:
                return aggregate.treeAggregate(avlNode.right);
            default:
                throw new AssertionError();
        }
    }

    public int size() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
    }

    int distinctElements() {
        return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
    }

    public int count(@javax.annotation.Nullable java.lang.Object r4) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r3 = this;
        r0 = 0;
        r1 = r3.rootReference;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        r1 = r1.get();	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        r1 = (com.google.common.collect.TreeMultiset.AvlNode) r1;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        r2 = r3.range;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        r2 = r2.contains(r4);	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        if (r2 == 0) goto L_0x001d;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
    L_0x0011:
        if (r1 != 0) goto L_0x0014;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
    L_0x0013:
        goto L_0x001d;	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
    L_0x0014:
        r2 = r3.comparator();	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        r4 = r1.count(r2, r4);	 Catch:{ ClassCastException -> 0x001f, NullPointerException -> 0x001e }
        return r4;
    L_0x001d:
        return r0;
    L_0x001e:
        return r0;
    L_0x001f:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeMultiset.count(java.lang.Object):int");
    }

    public int add(@Nullable E e, int i) {
        CollectPreconditions.checkNonnegative(i, "occurrences");
        if (i == 0) {
            return count(e);
        }
        Preconditions.checkArgument(this.range.contains(e));
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        if (avlNode == null) {
            comparator().compare(e, e);
            AvlNode avlNode2 = new AvlNode(e, i);
            successor(this.header, avlNode2, this.header);
            this.rootReference.checkAndSet(avlNode, avlNode2);
            return 0;
        }
        int[] iArr = new int[1];
        this.rootReference.checkAndSet(avlNode, avlNode.add(comparator(), e, i, iArr));
        return iArr[0];
    }

    public int remove(@javax.annotation.Nullable java.lang.Object r5, int r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = "occurrences";
        com.google.common.collect.CollectPreconditions.checkNonnegative(r6, r0);
        if (r6 != 0) goto L_0x000c;
    L_0x0007:
        r5 = r4.count(r5);
        return r5;
    L_0x000c:
        r0 = r4.rootReference;
        r0 = r0.get();
        r0 = (com.google.common.collect.TreeMultiset.AvlNode) r0;
        r1 = 1;
        r1 = new int[r1];
        r2 = 0;
        r3 = r4.range;	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
        r3 = r3.contains(r5);	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
        if (r3 == 0) goto L_0x0033;	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
    L_0x0020:
        if (r0 != 0) goto L_0x0023;	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
    L_0x0022:
        goto L_0x0033;	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
    L_0x0023:
        r3 = r4.comparator();	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
        r5 = r0.remove(r3, r5, r6, r1);	 Catch:{ ClassCastException -> 0x0035, NullPointerException -> 0x0034 }
        r6 = r4.rootReference;
        r6.checkAndSet(r0, r5);
        r5 = r1[r2];
        return r5;
    L_0x0033:
        return r2;
    L_0x0034:
        return r2;
    L_0x0035:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.TreeMultiset.remove(java.lang.Object, int):int");
    }

    public int setCount(@Nullable E e, int i) {
        CollectPreconditions.checkNonnegative(i, "count");
        boolean z = true;
        if (this.range.contains(e)) {
            AvlNode avlNode = (AvlNode) this.rootReference.get();
            if (avlNode == null) {
                if (i > 0) {
                    add(e, i);
                }
                return 0;
            }
            int[] iArr = new int[1];
            this.rootReference.checkAndSet(avlNode, avlNode.setCount(comparator(), e, i, iArr));
            return iArr[0];
        }
        if (i != 0) {
            z = false;
        }
        Preconditions.checkArgument(z);
        return 0;
    }

    public boolean setCount(@Nullable E e, int i, int i2) {
        CollectPreconditions.checkNonnegative(i2, "newCount");
        CollectPreconditions.checkNonnegative(i, "oldCount");
        Preconditions.checkArgument(this.range.contains(e));
        AvlNode avlNode = (AvlNode) this.rootReference.get();
        boolean z = false;
        if (avlNode != null) {
            int[] iArr = new int[1];
            this.rootReference.checkAndSet(avlNode, avlNode.setCount(comparator(), e, i, i2, iArr));
            if (iArr[0] == i) {
                z = true;
            }
            return z;
        } else if (i != 0) {
            return false;
        } else {
            if (i2 > 0) {
                add(e, i2);
            }
            return true;
        }
    }

    private Entry<E> wrapEntry(final AvlNode<E> avlNode) {
        return new AbstractEntry<E>() {
            public E getElement() {
                return avlNode.getElement();
            }

            public int getCount() {
                int count = avlNode.getCount();
                return count == 0 ? TreeMultiset.this.count(getElement()) : count;
            }
        };
    }

    @Nullable
    private AvlNode<E> firstNode() {
        if (((AvlNode) this.rootReference.get()) == null) {
            return null;
        }
        AvlNode<E> avlNode;
        if (this.range.hasLowerBound()) {
            Object lowerEndpoint = this.range.getLowerEndpoint();
            AvlNode<E> access$800 = ((AvlNode) this.rootReference.get()).ceiling(comparator(), lowerEndpoint);
            if (access$800 == null) {
                return null;
            }
            if (this.range.getLowerBoundType() == BoundType.OPEN && comparator().compare(lowerEndpoint, access$800.getElement()) == 0) {
                access$800 = access$800.succ;
            }
            avlNode = access$800;
        } else {
            avlNode = this.header.succ;
        }
        if (avlNode == this.header || !this.range.contains(avlNode.getElement())) {
            avlNode = null;
        }
        return avlNode;
    }

    @Nullable
    private AvlNode<E> lastNode() {
        if (((AvlNode) this.rootReference.get()) == null) {
            return null;
        }
        AvlNode<E> avlNode;
        if (this.range.hasUpperBound()) {
            Object upperEndpoint = this.range.getUpperEndpoint();
            AvlNode<E> access$1000 = ((AvlNode) this.rootReference.get()).floor(comparator(), upperEndpoint);
            if (access$1000 == null) {
                return null;
            }
            if (this.range.getUpperBoundType() == BoundType.OPEN && comparator().compare(upperEndpoint, access$1000.getElement()) == 0) {
                access$1000 = access$1000.pred;
            }
            avlNode = access$1000;
        } else {
            avlNode = this.header.pred;
        }
        if (avlNode == this.header || !this.range.contains(avlNode.getElement())) {
            avlNode = null;
        }
        return avlNode;
    }

    Iterator<Entry<E>> entryIterator() {
        return new C10922();
    }

    Iterator<Entry<E>> descendingEntryIterator() {
        return new C10933();
    }

    public SortedMultiset<E> headMultiset(@Nullable E e, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.upTo(comparator(), e, boundType)), this.header);
    }

    public SortedMultiset<E> tailMultiset(@Nullable E e, BoundType boundType) {
        return new TreeMultiset(this.rootReference, this.range.intersect(GeneralRange.downTo(comparator(), e, boundType)), this.header);
    }

    static int distinctElements(@Nullable AvlNode<?> avlNode) {
        return avlNode == null ? null : avlNode.distinctElements;
    }

    private static <T> void successor(AvlNode<T> avlNode, AvlNode<T> avlNode2) {
        avlNode.succ = avlNode2;
        avlNode2.pred = avlNode;
    }

    private static <T> void successor(AvlNode<T> avlNode, AvlNode<T> avlNode2, AvlNode<T> avlNode3) {
        successor(avlNode, avlNode2);
        successor(avlNode2, avlNode3);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(elementSet().comparator());
        Serialization.writeMultiset(this, objectOutputStream);
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object obj = (Comparator) objectInputStream.readObject();
        Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set((Object) this, obj);
        Serialization.getFieldSetter(TreeMultiset.class, "range").set((Object) this, GeneralRange.all(obj));
        Serialization.getFieldSetter(TreeMultiset.class, "rootReference").set((Object) this, new Reference());
        obj = new AvlNode(null, 1);
        Serialization.getFieldSetter(TreeMultiset.class, "header").set((Object) this, obj);
        successor(obj, obj);
        Serialization.populateMultiset(this, objectInputStream);
    }
}
