package com.google.android.gms.flags;

public abstract class Flag<T> {
    private final T mDefaultValue;
    private final String mKey;
    private final int zzacb;

    public static class BooleanFlag extends Flag<Boolean> {
        public BooleanFlag(int i, String str, Boolean bool) {
            super(i, str, bool);
        }

        public java.lang.Boolean get(com.google.android.gms.flags.IFlagProvider r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = r3.getKey();	 Catch:{ RemoteException -> 0x001b }
            r1 = r3.getDefault();	 Catch:{ RemoteException -> 0x001b }
            r1 = (java.lang.Boolean) r1;	 Catch:{ RemoteException -> 0x001b }
            r1 = r1.booleanValue();	 Catch:{ RemoteException -> 0x001b }
            r2 = r3.getSource();	 Catch:{ RemoteException -> 0x001b }
            r4 = r4.getBooleanFlagValue(r0, r1, r2);	 Catch:{ RemoteException -> 0x001b }
            r4 = java.lang.Boolean.valueOf(r4);	 Catch:{ RemoteException -> 0x001b }
            return r4;
        L_0x001b:
            r4 = r3.getDefault();
            r4 = (java.lang.Boolean) r4;
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.flags.Flag.BooleanFlag.get(com.google.android.gms.flags.IFlagProvider):java.lang.Boolean");
        }
    }

    public static class IntegerFlag extends Flag<Integer> {
        public IntegerFlag(int i, String str, Integer num) {
            super(i, str, num);
        }

        public java.lang.Integer get(com.google.android.gms.flags.IFlagProvider r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = r3.getKey();	 Catch:{ RemoteException -> 0x001b }
            r1 = r3.getDefault();	 Catch:{ RemoteException -> 0x001b }
            r1 = (java.lang.Integer) r1;	 Catch:{ RemoteException -> 0x001b }
            r1 = r1.intValue();	 Catch:{ RemoteException -> 0x001b }
            r2 = r3.getSource();	 Catch:{ RemoteException -> 0x001b }
            r4 = r4.getIntFlagValue(r0, r1, r2);	 Catch:{ RemoteException -> 0x001b }
            r4 = java.lang.Integer.valueOf(r4);	 Catch:{ RemoteException -> 0x001b }
            return r4;
        L_0x001b:
            r4 = r3.getDefault();
            r4 = (java.lang.Integer) r4;
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.flags.Flag.IntegerFlag.get(com.google.android.gms.flags.IFlagProvider):java.lang.Integer");
        }
    }

    public static class LongFlag extends Flag<Long> {
        public LongFlag(int i, String str, Long l) {
            super(i, str, l);
        }

        public java.lang.Long get(com.google.android.gms.flags.IFlagProvider r5) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r4 = this;
            r0 = r4.getKey();	 Catch:{ RemoteException -> 0x001b }
            r1 = r4.getDefault();	 Catch:{ RemoteException -> 0x001b }
            r1 = (java.lang.Long) r1;	 Catch:{ RemoteException -> 0x001b }
            r1 = r1.longValue();	 Catch:{ RemoteException -> 0x001b }
            r3 = r4.getSource();	 Catch:{ RemoteException -> 0x001b }
            r0 = r5.getLongFlagValue(r0, r1, r3);	 Catch:{ RemoteException -> 0x001b }
            r5 = java.lang.Long.valueOf(r0);	 Catch:{ RemoteException -> 0x001b }
            return r5;
        L_0x001b:
            r5 = r4.getDefault();
            r5 = (java.lang.Long) r5;
            return r5;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.flags.Flag.LongFlag.get(com.google.android.gms.flags.IFlagProvider):java.lang.Long");
        }
    }

    public static class StringFlag extends Flag<String> {
        public StringFlag(int i, String str, String str2) {
            super(i, str, str2);
        }

        public java.lang.String get(com.google.android.gms.flags.IFlagProvider r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = this;
            r0 = r3.getKey();	 Catch:{ RemoteException -> 0x0013 }
            r1 = r3.getDefault();	 Catch:{ RemoteException -> 0x0013 }
            r1 = (java.lang.String) r1;	 Catch:{ RemoteException -> 0x0013 }
            r2 = r3.getSource();	 Catch:{ RemoteException -> 0x0013 }
            r4 = r4.getStringFlagValue(r0, r1, r2);	 Catch:{ RemoteException -> 0x0013 }
            return r4;
        L_0x0013:
            r4 = r3.getDefault();
            r4 = (java.lang.String) r4;
            return r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.flags.Flag.StringFlag.get(com.google.android.gms.flags.IFlagProvider):java.lang.String");
        }
    }

    private Flag(int i, String str, T t) {
        this.zzacb = i;
        this.mKey = str;
        this.mDefaultValue = t;
        Singletons.flagRegistry().registerFlag(this);
    }

    public static BooleanFlag define(int i, String str, Boolean bool) {
        return new BooleanFlag(i, str, bool);
    }

    public static IntegerFlag define(int i, String str, int i2) {
        return new IntegerFlag(i, str, Integer.valueOf(i2));
    }

    public static LongFlag define(int i, String str, long j) {
        return new LongFlag(i, str, Long.valueOf(j));
    }

    public static StringFlag define(int i, String str, String str2) {
        return new StringFlag(i, str, str2);
    }

    public static StringFlag defineClientExperimentId(int i, String str) {
        StringFlag define = define(i, str, null);
        Singletons.flagRegistry().registerClientExperimentId(define);
        return define;
    }

    public static StringFlag defineServiceExperimentId(int i, String str) {
        StringFlag define = define(i, str, null);
        Singletons.flagRegistry().registerServiceExperimentId(define);
        return define;
    }

    public T get() {
        return Singletons.flagValueProvider().getFlagValue(this);
    }

    protected abstract T get(IFlagProvider iFlagProvider);

    public T getDefault() {
        return this.mDefaultValue;
    }

    public String getKey() {
        return this.mKey;
    }

    public int getSource() {
        return this.zzacb;
    }
}
