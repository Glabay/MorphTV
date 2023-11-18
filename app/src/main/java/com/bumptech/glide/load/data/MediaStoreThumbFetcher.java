package com.bumptech.glide.load.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MediaStoreThumbFetcher implements DataFetcher<InputStream> {
    private static final ThumbnailStreamOpenerFactory DEFAULT_FACTORY = new ThumbnailStreamOpenerFactory();
    private static final int MINI_HEIGHT = 384;
    private static final int MINI_WIDTH = 512;
    private static final String TAG = "MediaStoreThumbFetcher";
    private final Context context;
    private final DataFetcher<InputStream> defaultFetcher;
    private final ThumbnailStreamOpenerFactory factory;
    private final int height;
    private InputStream inputStream;
    private final Uri mediaStoreUri;
    private final int width;

    static class FileService {
        FileService() {
        }

        public boolean exists(File file) {
            return file.exists();
        }

        public long length(File file) {
            return file.length();
        }

        public File get(String str) {
            return new File(str);
        }
    }

    interface ThumbnailQuery {
        Cursor queryPath(Context context, Uri uri);
    }

    static class ImageThumbnailQuery implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = new String[]{"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND image_id = ?";

        ImageThumbnailQuery() {
        }

        public Cursor queryPath(Context context, Uri uri) {
            uri = uri.getLastPathSegment();
            return context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{uri}, null);
        }
    }

    static class ThumbnailStreamOpener {
        private static final FileService DEFAULT_SERVICE = new FileService();
        private ThumbnailQuery query;
        private final FileService service;

        public ThumbnailStreamOpener(ThumbnailQuery thumbnailQuery) {
            this(DEFAULT_SERVICE, thumbnailQuery);
        }

        public ThumbnailStreamOpener(FileService fileService, ThumbnailQuery thumbnailQuery) {
            this.service = fileService;
            this.query = thumbnailQuery;
        }

        public int getOrientation(android.content.Context r6, android.net.Uri r7) {
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
            r5 = this;
            r0 = 0;
            r6 = r6.getContentResolver();	 Catch:{ IOException -> 0x001d, all -> 0x001a }
            r6 = r6.openInputStream(r7);	 Catch:{ IOException -> 0x001d, all -> 0x001a }
            r0 = new com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;	 Catch:{ IOException -> 0x0018 }
            r0.<init>(r6);	 Catch:{ IOException -> 0x0018 }
            r0 = r0.getOrientation();	 Catch:{ IOException -> 0x0018 }
            if (r6 == 0) goto L_0x0046;
        L_0x0014:
            r6.close();	 Catch:{ IOException -> 0x0046 }
            goto L_0x0046;
        L_0x0018:
            r0 = move-exception;
            goto L_0x0021;
        L_0x001a:
            r7 = move-exception;
            r6 = r0;
            goto L_0x0048;
        L_0x001d:
            r6 = move-exception;
            r4 = r0;
            r0 = r6;
            r6 = r4;
        L_0x0021:
            r1 = "MediaStoreThumbFetcher";	 Catch:{ all -> 0x0047 }
            r2 = 3;	 Catch:{ all -> 0x0047 }
            r1 = android.util.Log.isLoggable(r1, r2);	 Catch:{ all -> 0x0047 }
            if (r1 == 0) goto L_0x0040;	 Catch:{ all -> 0x0047 }
        L_0x002a:
            r1 = "MediaStoreThumbFetcher";	 Catch:{ all -> 0x0047 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
            r2.<init>();	 Catch:{ all -> 0x0047 }
            r3 = "Failed to open uri: ";	 Catch:{ all -> 0x0047 }
            r2.append(r3);	 Catch:{ all -> 0x0047 }
            r2.append(r7);	 Catch:{ all -> 0x0047 }
            r7 = r2.toString();	 Catch:{ all -> 0x0047 }
            android.util.Log.d(r1, r7, r0);	 Catch:{ all -> 0x0047 }
        L_0x0040:
            if (r6 == 0) goto L_0x0045;
        L_0x0042:
            r6.close();	 Catch:{ IOException -> 0x0045 }
        L_0x0045:
            r0 = -1;
        L_0x0046:
            return r0;
        L_0x0047:
            r7 = move-exception;
        L_0x0048:
            if (r6 == 0) goto L_0x004d;
        L_0x004a:
            r6.close();	 Catch:{ IOException -> 0x004d }
        L_0x004d:
            throw r7;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.data.MediaStoreThumbFetcher.ThumbnailStreamOpener.getOrientation(android.content.Context, android.net.Uri):int");
        }

        public InputStream open(Context context, Uri uri) throws FileNotFoundException {
            Uri parseThumbUri;
            uri = this.query.queryPath(context, uri);
            if (uri != null) {
                try {
                    if (uri.moveToFirst()) {
                        parseThumbUri = parseThumbUri(uri);
                        if (uri != null) {
                            uri.close();
                        }
                        if (parseThumbUri == null) {
                            return context.getContentResolver().openInputStream(parseThumbUri);
                        }
                        return null;
                    }
                } catch (Throwable th) {
                    if (uri != null) {
                        uri.close();
                    }
                }
            }
            parseThumbUri = null;
            if (uri != null) {
                uri.close();
            }
            if (parseThumbUri == null) {
                return null;
            }
            return context.getContentResolver().openInputStream(parseThumbUri);
        }

        private Uri parseThumbUri(Cursor cursor) {
            cursor = cursor.getString(0);
            if (!TextUtils.isEmpty(cursor)) {
                cursor = this.service.get(cursor);
                if (this.service.exists(cursor) && this.service.length(cursor) > 0) {
                    return Uri.fromFile(cursor);
                }
            }
            return null;
        }
    }

    static class ThumbnailStreamOpenerFactory {
        ThumbnailStreamOpenerFactory() {
        }

        public ThumbnailStreamOpener build(Uri uri, int i, int i2) {
            if (MediaStoreThumbFetcher.isMediaStoreUri(uri) && i <= 512) {
                if (i2 <= MediaStoreThumbFetcher.MINI_HEIGHT) {
                    if (MediaStoreThumbFetcher.isMediaStoreVideo(uri) != null) {
                        return new ThumbnailStreamOpener(new VideoThumbnailQuery());
                    }
                    return new ThumbnailStreamOpener(new ImageThumbnailQuery());
                }
            }
            return null;
        }
    }

    static class VideoThumbnailQuery implements ThumbnailQuery {
        private static final String[] PATH_PROJECTION = new String[]{"_data"};
        private static final String PATH_SELECTION = "kind = 1 AND video_id = ?";

        VideoThumbnailQuery() {
        }

        public Cursor queryPath(Context context, Uri uri) {
            uri = uri.getLastPathSegment();
            return context.getContentResolver().query(Video.Thumbnails.EXTERNAL_CONTENT_URI, PATH_PROJECTION, PATH_SELECTION, new String[]{uri}, null);
        }
    }

    public void cancel() {
    }

    public MediaStoreThumbFetcher(Context context, Uri uri, DataFetcher<InputStream> dataFetcher, int i, int i2) {
        this(context, uri, dataFetcher, i, i2, DEFAULT_FACTORY);
    }

    MediaStoreThumbFetcher(Context context, Uri uri, DataFetcher<InputStream> dataFetcher, int i, int i2, ThumbnailStreamOpenerFactory thumbnailStreamOpenerFactory) {
        this.context = context;
        this.mediaStoreUri = uri;
        this.defaultFetcher = dataFetcher;
        this.width = i;
        this.height = i2;
        this.factory = thumbnailStreamOpenerFactory;
    }

    public InputStream loadData(Priority priority) throws Exception {
        ThumbnailStreamOpener build = this.factory.build(this.mediaStoreUri, this.width, this.height);
        if (build != null) {
            this.inputStream = openThumbInputStream(build);
        }
        if (this.inputStream == null) {
            this.inputStream = (InputStream) this.defaultFetcher.loadData(priority);
        }
        return this.inputStream;
    }

    private InputStream openThumbInputStream(ThumbnailStreamOpener thumbnailStreamOpener) {
        InputStream open;
        try {
            open = thumbnailStreamOpener.open(this.context, this.mediaStoreUri);
        } catch (Throwable e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to find thumbnail file", e);
            }
            open = null;
        }
        thumbnailStreamOpener = open != null ? thumbnailStreamOpener.getOrientation(this.context, this.mediaStoreUri) : -1;
        return thumbnailStreamOpener != -1 ? new ExifOrientationStream(open, thumbnailStreamOpener) : open;
    }

    public void cleanup() {
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
        r1 = this;
        r0 = r1.inputStream;
        if (r0 == 0) goto L_0x0009;
    L_0x0004:
        r0 = r1.inputStream;	 Catch:{ IOException -> 0x0009 }
        r0.close();	 Catch:{ IOException -> 0x0009 }
    L_0x0009:
        r0 = r1.defaultFetcher;
        r0.cleanup();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.data.MediaStoreThumbFetcher.cleanup():void");
    }

    public String getId() {
        return this.mediaStoreUri.toString();
    }

    private static boolean isMediaStoreUri(Uri uri) {
        return (uri == null || !"content".equals(uri.getScheme()) || "media".equals(uri.getAuthority()) == null) ? null : true;
    }

    private static boolean isMediaStoreVideo(Uri uri) {
        return (!isMediaStoreUri(uri) || uri.getPathSegments().contains(MimeTypes.BASE_TYPE_VIDEO) == null) ? null : true;
    }
}
