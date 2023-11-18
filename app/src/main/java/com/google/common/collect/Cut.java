package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Booleans;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtCompatible
abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
    private static final long serialVersionUID = 0;
    final C endpoint;

    private static final class AboveAll extends Cut<Comparable<?>> {
        private static final AboveAll INSTANCE = new AboveAll();
        private static final long serialVersionUID = 0;

        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : 1;
        }

        boolean isLessThan(Comparable<?> comparable) {
            return false;
        }

        public String toString() {
            return "+∞";
        }

        private AboveAll() {
            super(null);
        }

        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        BoundType typeAsLowerBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        BoundType typeAsUpperBound() {
            throw new IllegalStateException();
        }

        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError("this statement should be unreachable");
        }

        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new IllegalStateException();
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            throw new AssertionError();
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append("+∞)");
        }

        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError();
        }

        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> discreteDomain) {
            return discreteDomain.maxValue();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class AboveValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return super.compareTo((Cut) obj);
        }

        AboveValue(C c) {
            super((Comparable) Preconditions.checkNotNull(c));
        }

        boolean isLessThan(C c) {
            return Range.compareOrThrow(this.endpoint, c) < null ? true : null;
        }

        BoundType typeAsLowerBound() {
            return BoundType.OPEN;
        }

        BoundType typeAsUpperBound() {
            return BoundType.CLOSED;
        }

        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    boundType = discreteDomain.next(this.endpoint);
                    return boundType == null ? Cut.belowAll() : Cut.belowValue(boundType);
                case OPEN:
                    return this;
                default:
                    throw new AssertionError();
            }
        }

        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    return this;
                case OPEN:
                    boundType = discreteDomain.next(this.endpoint);
                    return boundType == null ? Cut.aboveAll() : Cut.belowValue(boundType);
                default:
                    throw new AssertionError();
            }
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append('(');
            stringBuilder.append(this.endpoint);
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append(this.endpoint);
            stringBuilder.append(']');
        }

        C leastValueAbove(DiscreteDomain<C> discreteDomain) {
            return discreteDomain.next(this.endpoint);
        }

        C greatestValueBelow(DiscreteDomain<C> discreteDomain) {
            return this.endpoint;
        }

        Cut<C> canonical(DiscreteDomain<C> discreteDomain) {
            discreteDomain = leastValueAbove(discreteDomain);
            return discreteDomain != null ? Cut.belowValue(discreteDomain) : Cut.aboveAll();
        }

        public int hashCode() {
            return this.endpoint.hashCode() ^ -1;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/");
            stringBuilder.append(this.endpoint);
            stringBuilder.append("\\");
            return stringBuilder.toString();
        }
    }

    private static final class BelowAll extends Cut<Comparable<?>> {
        private static final BelowAll INSTANCE = new BelowAll();
        private static final long serialVersionUID = 0;

        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : -1;
        }

        boolean isLessThan(Comparable<?> comparable) {
            return true;
        }

        public String toString() {
            return "-∞";
        }

        private BelowAll() {
            super(null);
        }

        Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        BoundType typeAsLowerBound() {
            throw new IllegalStateException();
        }

        BoundType typeAsUpperBound() {
            throw new AssertionError("this statement should be unreachable");
        }

        Cut<Comparable<?>> withLowerBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new IllegalStateException();
        }

        Cut<Comparable<?>> withUpperBoundType(BoundType boundType, DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError("this statement should be unreachable");
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append("(-∞");
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            throw new AssertionError();
        }

        Comparable<?> leastValueAbove(DiscreteDomain<Comparable<?>> discreteDomain) {
            return discreteDomain.minValue();
        }

        Comparable<?> greatestValueBelow(DiscreteDomain<Comparable<?>> discreteDomain) {
            throw new AssertionError();
        }

        com.google.common.collect.Cut<java.lang.Comparable<?>> canonical(com.google.common.collect.DiscreteDomain<java.lang.Comparable<?>> r1) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = this;
            r1 = r1.minValue();	 Catch:{ NoSuchElementException -> 0x0009 }
            r1 = com.google.common.collect.Cut.belowValue(r1);	 Catch:{ NoSuchElementException -> 0x0009 }
            return r1;
        L_0x0009:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Cut.BelowAll.canonical(com.google.common.collect.DiscreteDomain):com.google.common.collect.Cut<java.lang.Comparable<?>>");
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class BelowValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        public /* bridge */ /* synthetic */ int compareTo(Object obj) {
            return super.compareTo((Cut) obj);
        }

        BelowValue(C c) {
            super((Comparable) Preconditions.checkNotNull(c));
        }

        boolean isLessThan(C c) {
            return Range.compareOrThrow(this.endpoint, c) <= null ? true : null;
        }

        BoundType typeAsLowerBound() {
            return BoundType.CLOSED;
        }

        BoundType typeAsUpperBound() {
            return BoundType.OPEN;
        }

        Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    return this;
                case OPEN:
                    boundType = discreteDomain.previous(this.endpoint);
                    return boundType == null ? Cut.belowAll() : new AboveValue(boundType);
                default:
                    throw new AssertionError();
            }
        }

        Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain) {
            switch (boundType) {
                case CLOSED:
                    boundType = discreteDomain.previous(this.endpoint);
                    return boundType == null ? Cut.aboveAll() : new AboveValue(boundType);
                case OPEN:
                    return this;
                default:
                    throw new AssertionError();
            }
        }

        void describeAsLowerBound(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            stringBuilder.append(this.endpoint);
        }

        void describeAsUpperBound(StringBuilder stringBuilder) {
            stringBuilder.append(this.endpoint);
            stringBuilder.append(')');
        }

        C leastValueAbove(DiscreteDomain<C> discreteDomain) {
            return this.endpoint;
        }

        C greatestValueBelow(DiscreteDomain<C> discreteDomain) {
            return discreteDomain.previous(this.endpoint);
        }

        public int hashCode() {
            return this.endpoint.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\\");
            stringBuilder.append(this.endpoint);
            stringBuilder.append("/");
            return stringBuilder.toString();
        }
    }

    Cut<C> canonical(DiscreteDomain<C> discreteDomain) {
        return this;
    }

    abstract void describeAsLowerBound(StringBuilder stringBuilder);

    abstract void describeAsUpperBound(StringBuilder stringBuilder);

    abstract C greatestValueBelow(DiscreteDomain<C> discreteDomain);

    abstract boolean isLessThan(C c);

    abstract C leastValueAbove(DiscreteDomain<C> discreteDomain);

    abstract BoundType typeAsLowerBound();

    abstract BoundType typeAsUpperBound();

    abstract Cut<C> withLowerBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);

    abstract Cut<C> withUpperBoundType(BoundType boundType, DiscreteDomain<C> discreteDomain);

    Cut(@Nullable C c) {
        this.endpoint = c;
    }

    public int compareTo(Cut<C> cut) {
        if (cut == belowAll()) {
            return 1;
        }
        if (cut == aboveAll()) {
            return -1;
        }
        int compareOrThrow = Range.compareOrThrow(this.endpoint, cut.endpoint);
        if (compareOrThrow != 0) {
            return compareOrThrow;
        }
        return Booleans.compare(this instanceof AboveValue, cut instanceof AboveValue);
    }

    C endpoint() {
        return this.endpoint;
    }

    public boolean equals(java.lang.Object r3) {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r3 instanceof com.google.common.collect.Cut;
        r1 = 0;
        if (r0 == 0) goto L_0x000f;
    L_0x0005:
        r3 = (com.google.common.collect.Cut) r3;
        r3 = r2.compareTo(r3);	 Catch:{ ClassCastException -> 0x000f }
        if (r3 != 0) goto L_0x000e;
    L_0x000d:
        r1 = 1;
    L_0x000e:
        return r1;
    L_0x000f:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.Cut.equals(java.lang.Object):boolean");
    }

    static <C extends Comparable> Cut<C> belowAll() {
        return BelowAll.INSTANCE;
    }

    static <C extends Comparable> Cut<C> aboveAll() {
        return AboveAll.INSTANCE;
    }

    static <C extends Comparable> Cut<C> belowValue(C c) {
        return new BelowValue(c);
    }

    static <C extends Comparable> Cut<C> aboveValue(C c) {
        return new AboveValue(c);
    }
}
