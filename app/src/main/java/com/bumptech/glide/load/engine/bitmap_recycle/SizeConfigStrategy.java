package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import com.bumptech.glide.util.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy {
    private static final Config[] ALPHA_8_IN_CONFIGS = new Config[]{Config.ALPHA_8};
    private static final Config[] ARGB_4444_IN_CONFIGS = new Config[]{Config.ARGB_4444};
    private static final Config[] ARGB_8888_IN_CONFIGS = new Config[]{Config.ARGB_8888, null};
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Config[] RGB_565_IN_CONFIGS = new Config[]{Config.RGB_565};
    private final GroupedLinkedMap<Key, Bitmap> groupedMap = new GroupedLinkedMap();
    private final KeyPool keyPool = new KeyPool();
    private final Map<Config, NavigableMap<Integer, Integer>> sortedSizes = new HashMap();

    /* renamed from: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy$1 */
    static /* synthetic */ class C05781 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config = new int[Config.values().length];

        static {
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
            r0 = android.graphics.Bitmap.Config.values();
            r0 = r0.length;
            r0 = new int[r0];
            $SwitchMap$android$graphics$Bitmap$Config = r0;
            r0 = $SwitchMap$android$graphics$Bitmap$Config;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = android.graphics.Bitmap.Config.ARGB_8888;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0014 }
            r2 = 1;	 Catch:{ NoSuchFieldError -> 0x0014 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0014 }
        L_0x0014:
            r0 = $SwitchMap$android$graphics$Bitmap$Config;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = android.graphics.Bitmap.Config.RGB_565;	 Catch:{ NoSuchFieldError -> 0x001f }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x001f }
            r2 = 2;	 Catch:{ NoSuchFieldError -> 0x001f }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x001f }
        L_0x001f:
            r0 = $SwitchMap$android$graphics$Bitmap$Config;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = android.graphics.Bitmap.Config.ARGB_4444;	 Catch:{ NoSuchFieldError -> 0x002a }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x002a }
            r2 = 3;	 Catch:{ NoSuchFieldError -> 0x002a }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x002a }
        L_0x002a:
            r0 = $SwitchMap$android$graphics$Bitmap$Config;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = android.graphics.Bitmap.Config.ALPHA_8;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r1 = r1.ordinal();	 Catch:{ NoSuchFieldError -> 0x0035 }
            r2 = 4;	 Catch:{ NoSuchFieldError -> 0x0035 }
            r0[r1] = r2;	 Catch:{ NoSuchFieldError -> 0x0035 }
        L_0x0035:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.1.<clinit>():void");
        }
    }

    static final class Key implements Poolable {
        private Config config;
        private final KeyPool pool;
        private int size;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        Key(KeyPool keyPool, int i, Config config) {
            this(keyPool);
            init(i, config);
        }

        public void init(int i, Config config) {
            this.size = i;
            this.config = config;
        }

        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r4) {
            /*
            r3 = this;
            r0 = r4 instanceof com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key;
            r1 = 0;
            if (r0 == 0) goto L_0x0022;
        L_0x0005:
            r4 = (com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key) r4;
            r0 = r3.size;
            r2 = r4.size;
            if (r0 != r2) goto L_0x0021;
        L_0x000d:
            r0 = r3.config;
            if (r0 != 0) goto L_0x0016;
        L_0x0011:
            r4 = r4.config;
            if (r4 != 0) goto L_0x0021;
        L_0x0015:
            goto L_0x0020;
        L_0x0016:
            r0 = r3.config;
            r4 = r4.config;
            r4 = r0.equals(r4);
            if (r4 == 0) goto L_0x0021;
        L_0x0020:
            r1 = 1;
        L_0x0021:
            return r1;
        L_0x0022:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy.Key.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            return (this.size * 31) + (this.config != null ? this.config.hashCode() : 0);
        }
    }

    static class KeyPool extends BaseKeyPool<Key> {
        KeyPool() {
        }

        public Key get(int i, Config config) {
            Key key = (Key) get();
            key.init(i, config);
            return key;
        }

        protected Key create() {
            return new Key(this);
        }
    }

    public void put(Bitmap bitmap) {
        Key key = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(key, bitmap);
        bitmap = getSizesForConfig(bitmap.getConfig());
        Integer num = (Integer) bitmap.get(Integer.valueOf(key.size));
        Integer valueOf = Integer.valueOf(key.size);
        int i = 1;
        if (num != null) {
            i = 1 + num.intValue();
        }
        bitmap.put(valueOf, Integer.valueOf(i));
    }

    public Bitmap get(int i, int i2, Config config) {
        int bitmapByteSize = Util.getBitmapByteSize(i, i2, config);
        Bitmap bitmap = (Bitmap) this.groupedMap.get(findBestKey(this.keyPool.get(bitmapByteSize, config), bitmapByteSize, config));
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)), bitmap.getConfig());
            bitmap.reconfigure(i, i2, bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888);
        }
        return bitmap;
    }

    private Key findBestKey(Key key, int i, Config config) {
        Config[] inConfigs = getInConfigs(config);
        int length = inConfigs.length;
        int i2 = 0;
        while (i2 < length) {
            Config config2 = inConfigs[i2];
            Integer num = (Integer) getSizesForConfig(config2).ceilingKey(Integer.valueOf(i));
            if (num == null || num.intValue() > i * 8) {
                i2++;
            } else {
                if (num.intValue() == i) {
                    if (config2 == null) {
                        if (config == null) {
                            return key;
                        }
                    } else if (config2.equals(config) != 0) {
                        return key;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(num.intValue(), config2);
            }
        }
        return key;
    }

    public Bitmap removeLast() {
        Bitmap bitmap = (Bitmap) this.groupedMap.removeLast();
        if (bitmap != null) {
            decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(bitmap)), bitmap.getConfig());
        }
        return bitmap;
    }

    private void decrementBitmapOfSize(Integer num, Config config) {
        config = getSizesForConfig(config);
        Integer num2 = (Integer) config.get(num);
        if (num2.intValue() == 1) {
            config.remove(num);
        } else {
            config.put(num, Integer.valueOf(num2.intValue() - 1));
        }
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Config config) {
        NavigableMap<Integer, Integer> navigableMap = (NavigableMap) this.sortedSizes.get(config);
        if (navigableMap != null) {
            return navigableMap;
        }
        NavigableMap treeMap = new TreeMap();
        this.sortedSizes.put(config, treeMap);
        return treeMap;
    }

    public String logBitmap(Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }

    public String logBitmap(int i, int i2, Config config) {
        return getBitmapString(Util.getBitmapByteSize(i, i2, config), config);
    }

    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SizeConfigStrategy{groupedMap=");
        stringBuilder.append(this.groupedMap);
        stringBuilder.append(", sortedSizes=(");
        for (Entry entry : this.sortedSizes.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append('[');
            stringBuilder.append(entry.getValue());
            stringBuilder.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        }
        stringBuilder.append(")}");
        return stringBuilder.toString();
    }

    private static String getBitmapString(int i, Config config) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(i);
        stringBuilder.append("](");
        stringBuilder.append(config);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static Config[] getInConfigs(Config config) {
        switch (C05781.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            case 1:
                return ARGB_8888_IN_CONFIGS;
            case 2:
                return RGB_565_IN_CONFIGS;
            case 3:
                return ARGB_4444_IN_CONFIGS;
            case 4:
                return ALPHA_8_IN_CONFIGS;
            default:
                return new Config[]{config};
        }
    }
}
