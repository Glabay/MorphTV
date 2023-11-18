package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList.Builder;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
    private final transient ImmutableList<List<E>> axes;
    private final transient int[] axesSizeProduct;

    static <E> List<List<E>> create(List<? extends List<? extends E>> list) {
        Builder builder = new Builder(list.size());
        for (Collection copyOf : list) {
            Object copyOf2 = ImmutableList.copyOf(copyOf);
            if (copyOf2.isEmpty()) {
                return ImmutableList.of();
            }
            builder.add(copyOf2);
        }
        return new CartesianList(builder.build());
    }

    CartesianList(com.google.common.collect.ImmutableList<java.util.List<E>> r5) {
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
        r4 = this;
        r4.<init>();
        r4.axes = r5;
        r0 = r5.size();
        r1 = 1;
        r0 = r0 + r1;
        r0 = new int[r0];
        r2 = r5.size();
        r0[r2] = r1;
        r2 = r5.size();	 Catch:{ ArithmeticException -> 0x0034 }
        r2 = r2 - r1;	 Catch:{ ArithmeticException -> 0x0034 }
    L_0x0018:
        if (r2 < 0) goto L_0x0031;	 Catch:{ ArithmeticException -> 0x0034 }
    L_0x001a:
        r1 = r2 + 1;	 Catch:{ ArithmeticException -> 0x0034 }
        r1 = r0[r1];	 Catch:{ ArithmeticException -> 0x0034 }
        r3 = r5.get(r2);	 Catch:{ ArithmeticException -> 0x0034 }
        r3 = (java.util.List) r3;	 Catch:{ ArithmeticException -> 0x0034 }
        r3 = r3.size();	 Catch:{ ArithmeticException -> 0x0034 }
        r1 = com.google.common.math.IntMath.checkedMultiply(r1, r3);	 Catch:{ ArithmeticException -> 0x0034 }
        r0[r2] = r1;	 Catch:{ ArithmeticException -> 0x0034 }
        r2 = r2 + -1;
        goto L_0x0018;
    L_0x0031:
        r4.axesSizeProduct = r0;
        return;
    L_0x0034:
        r5 = new java.lang.IllegalArgumentException;
        r0 = "Cartesian product too large; must have size at most Integer.MAX_VALUE";
        r5.<init>(r0);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.CartesianList.<init>(com.google.common.collect.ImmutableList):void");
    }

    private int getAxisIndexForProductIndex(int i, int i2) {
        return (i / this.axesSizeProduct[i2 + 1]) % ((List) this.axes.get(i2)).size();
    }

    public ImmutableList<E> get(final int i) {
        Preconditions.checkElementIndex(i, size());
        return new ImmutableList<E>() {
            boolean isPartialView() {
                return true;
            }

            public int size() {
                return CartesianList.this.axes.size();
            }

            public E get(int i) {
                Preconditions.checkElementIndex(i, size());
                return ((List) CartesianList.this.axes.get(i)).get(CartesianList.this.getAxisIndexForProductIndex(i, i));
            }
        };
    }

    public int size() {
        return this.axesSizeProduct[0];
    }

    public boolean contains(@Nullable Object obj) {
        if (!(obj instanceof List)) {
            return false;
        }
        List list = (List) obj;
        if (list.size() != this.axes.size()) {
            return false;
        }
        obj = list.listIterator();
        while (obj.hasNext()) {
            if (!((List) this.axes.get(obj.nextIndex())).contains(obj.next())) {
                return false;
            }
        }
        return true;
    }
}
