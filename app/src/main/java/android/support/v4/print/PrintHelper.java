package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Builder;
import android.print.PrintAttributes.Margins;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintHelper {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    private final PrintHelperVersionImpl mImpl;

    @Retention(RetentionPolicy.SOURCE)
    private @interface ColorMode {
    }

    public interface OnPrintFinishCallback {
        void onFinish();
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface Orientation {
    }

    interface PrintHelperVersionImpl {
        int getColorMode();

        int getOrientation();

        int getScaleMode();

        void printBitmap(String str, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback);

        void printBitmap(String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException;

        void setColorMode(int i);

        void setOrientation(int i);

        void setScaleMode(int i);
    }

    @RequiresApi(19)
    private static class PrintHelperApi19 implements PrintHelperVersionImpl {
        private static final String LOG_TAG = "PrintHelperApi19";
        private static final int MAX_PRINT_SIZE = 3500;
        int mColorMode = 2;
        final Context mContext;
        Options mDecodeOptions = null;
        protected boolean mIsMinMarginsHandlingCorrect = true;
        private final Object mLock = new Object();
        int mOrientation;
        protected boolean mPrintActivityRespectsOrientation = true;
        int mScaleMode = 2;

        PrintHelperApi19(Context context) {
            this.mContext = context;
        }

        public void setScaleMode(int i) {
            this.mScaleMode = i;
        }

        public int getScaleMode() {
            return this.mScaleMode;
        }

        public void setColorMode(int i) {
            this.mColorMode = i;
        }

        public void setOrientation(int i) {
            this.mOrientation = i;
        }

        public int getOrientation() {
            if (this.mOrientation == 0) {
                return 1;
            }
            return this.mOrientation;
        }

        public int getColorMode() {
            return this.mColorMode;
        }

        private static boolean isPortrait(Bitmap bitmap) {
            return bitmap.getWidth() <= bitmap.getHeight() ? true : null;
        }

        protected Builder copyAttributes(PrintAttributes printAttributes) {
            Builder minMargins = new Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
            if (printAttributes.getColorMode() != 0) {
                minMargins.setColorMode(printAttributes.getColorMode());
            }
            return minMargins;
        }

        public void printBitmap(String str, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
            if (bitmap != null) {
                MediaSize mediaSize;
                final int i = this.mScaleMode;
                PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
                if (isPortrait(bitmap)) {
                    mediaSize = MediaSize.UNKNOWN_PORTRAIT;
                } else {
                    mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
                }
                final String str2 = str;
                final Bitmap bitmap2 = bitmap;
                final OnPrintFinishCallback onPrintFinishCallback2 = onPrintFinishCallback;
                printManager.print(str, new PrintDocumentAdapter() {
                    private PrintAttributes mAttributes;

                    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                        this.mAttributes = printAttributes2;
                        layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(str2).setContentType(1).setPageCount(1).build(), printAttributes2.equals(printAttributes) ^ 1);
                    }

                    public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
                        PrintHelperApi19.this.writeBitmap(this.mAttributes, i, bitmap2, parcelFileDescriptor, cancellationSignal, writeResultCallback);
                    }

                    public void onFinish() {
                        if (onPrintFinishCallback2 != null) {
                            onPrintFinishCallback2.onFinish();
                        }
                    }
                }, new Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
            }
        }

        private Matrix getMatrix(int i, int i2, RectF rectF, int i3) {
            Matrix matrix = new Matrix();
            i = (float) i;
            float width = rectF.width() / i;
            if (i3 == 2) {
                i3 = Math.max(width, rectF.height() / ((float) i2));
            } else {
                i3 = Math.min(width, rectF.height() / ((float) i2));
            }
            matrix.postScale(i3, i3);
            matrix.postTranslate((rectF.width() - (i * i3)) / 2.0f, (rectF.height() - (((float) i2) * i3)) / 1073741824);
            return matrix;
        }

        private void writeBitmap(PrintAttributes printAttributes, int i, Bitmap bitmap, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintAttributes printAttributes2;
            if (this.mIsMinMarginsHandlingCorrect) {
                printAttributes2 = printAttributes;
            } else {
                printAttributes2 = copyAttributes(printAttributes).setMinMargins(new Margins(0, 0, 0, 0)).build();
            }
            final CancellationSignal cancellationSignal2 = cancellationSignal;
            final Bitmap bitmap2 = bitmap;
            final PrintAttributes printAttributes3 = printAttributes;
            final int i2 = i;
            final ParcelFileDescriptor parcelFileDescriptor2 = parcelFileDescriptor;
            final WriteResultCallback writeResultCallback2 = writeResultCallback;
            new AsyncTask<Void, Void, Throwable>() {
                protected java.lang.Throwable doInBackground(java.lang.Void... r9) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                    /*
                    r8 = this;
                    r9 = r2;	 Catch:{ Throwable -> 0x00ea }
                    r9 = r9.isCanceled();	 Catch:{ Throwable -> 0x00ea }
                    r0 = 0;	 Catch:{ Throwable -> 0x00ea }
                    if (r9 == 0) goto L_0x000a;	 Catch:{ Throwable -> 0x00ea }
                L_0x0009:
                    return r0;	 Catch:{ Throwable -> 0x00ea }
                L_0x000a:
                    r9 = new android.print.pdf.PrintedPdfDocument;	 Catch:{ Throwable -> 0x00ea }
                    r1 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ Throwable -> 0x00ea }
                    r1 = r1.mContext;	 Catch:{ Throwable -> 0x00ea }
                    r2 = r3;	 Catch:{ Throwable -> 0x00ea }
                    r9.<init>(r1, r2);	 Catch:{ Throwable -> 0x00ea }
                    r1 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ Throwable -> 0x00ea }
                    r2 = r4;	 Catch:{ Throwable -> 0x00ea }
                    r3 = r3;	 Catch:{ Throwable -> 0x00ea }
                    r3 = r3.getColorMode();	 Catch:{ Throwable -> 0x00ea }
                    r1 = r1.convertBitmapForColorMode(r2, r3);	 Catch:{ Throwable -> 0x00ea }
                    r2 = r2;	 Catch:{ Throwable -> 0x00ea }
                    r2 = r2.isCanceled();	 Catch:{ Throwable -> 0x00ea }
                    if (r2 == 0) goto L_0x002c;
                L_0x002b:
                    return r0;
                L_0x002c:
                    r2 = 1;
                    r3 = r9.startPage(r2);	 Catch:{ all -> 0x00d5 }
                    r4 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ all -> 0x00d5 }
                    r4 = r4.mIsMinMarginsHandlingCorrect;	 Catch:{ all -> 0x00d5 }
                    if (r4 == 0) goto L_0x0045;	 Catch:{ all -> 0x00d5 }
                L_0x0037:
                    r2 = new android.graphics.RectF;	 Catch:{ all -> 0x00d5 }
                    r4 = r3.getInfo();	 Catch:{ all -> 0x00d5 }
                    r4 = r4.getContentRect();	 Catch:{ all -> 0x00d5 }
                    r2.<init>(r4);	 Catch:{ all -> 0x00d5 }
                    goto L_0x0068;	 Catch:{ all -> 0x00d5 }
                L_0x0045:
                    r4 = new android.print.pdf.PrintedPdfDocument;	 Catch:{ all -> 0x00d5 }
                    r5 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ all -> 0x00d5 }
                    r5 = r5.mContext;	 Catch:{ all -> 0x00d5 }
                    r6 = r5;	 Catch:{ all -> 0x00d5 }
                    r4.<init>(r5, r6);	 Catch:{ all -> 0x00d5 }
                    r2 = r4.startPage(r2);	 Catch:{ all -> 0x00d5 }
                    r5 = new android.graphics.RectF;	 Catch:{ all -> 0x00d5 }
                    r6 = r2.getInfo();	 Catch:{ all -> 0x00d5 }
                    r6 = r6.getContentRect();	 Catch:{ all -> 0x00d5 }
                    r5.<init>(r6);	 Catch:{ all -> 0x00d5 }
                    r4.finishPage(r2);	 Catch:{ all -> 0x00d5 }
                    r4.close();	 Catch:{ all -> 0x00d5 }
                    r2 = r5;	 Catch:{ all -> 0x00d5 }
                L_0x0068:
                    r4 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ all -> 0x00d5 }
                    r5 = r1.getWidth();	 Catch:{ all -> 0x00d5 }
                    r6 = r1.getHeight();	 Catch:{ all -> 0x00d5 }
                    r7 = r6;	 Catch:{ all -> 0x00d5 }
                    r4 = r4.getMatrix(r5, r6, r2, r7);	 Catch:{ all -> 0x00d5 }
                    r5 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ all -> 0x00d5 }
                    r5 = r5.mIsMinMarginsHandlingCorrect;	 Catch:{ all -> 0x00d5 }
                    if (r5 == 0) goto L_0x007f;	 Catch:{ all -> 0x00d5 }
                L_0x007e:
                    goto L_0x008d;	 Catch:{ all -> 0x00d5 }
                L_0x007f:
                    r5 = r2.left;	 Catch:{ all -> 0x00d5 }
                    r6 = r2.top;	 Catch:{ all -> 0x00d5 }
                    r4.postTranslate(r5, r6);	 Catch:{ all -> 0x00d5 }
                    r5 = r3.getCanvas();	 Catch:{ all -> 0x00d5 }
                    r5.clipRect(r2);	 Catch:{ all -> 0x00d5 }
                L_0x008d:
                    r2 = r3.getCanvas();	 Catch:{ all -> 0x00d5 }
                    r2.drawBitmap(r1, r4, r0);	 Catch:{ all -> 0x00d5 }
                    r9.finishPage(r3);	 Catch:{ all -> 0x00d5 }
                    r2 = r2;	 Catch:{ all -> 0x00d5 }
                    r2 = r2.isCanceled();	 Catch:{ all -> 0x00d5 }
                    if (r2 == 0) goto L_0x00b3;
                L_0x009f:
                    r9.close();	 Catch:{ Throwable -> 0x00ea }
                    r9 = r7;	 Catch:{ Throwable -> 0x00ea }
                    if (r9 == 0) goto L_0x00ab;
                L_0x00a6:
                    r9 = r7;	 Catch:{ IOException -> 0x00ab }
                    r9.close();	 Catch:{ IOException -> 0x00ab }
                L_0x00ab:
                    r9 = r4;	 Catch:{ Throwable -> 0x00ea }
                    if (r1 == r9) goto L_0x00b2;	 Catch:{ Throwable -> 0x00ea }
                L_0x00af:
                    r1.recycle();	 Catch:{ Throwable -> 0x00ea }
                L_0x00b2:
                    return r0;
                L_0x00b3:
                    r2 = new java.io.FileOutputStream;	 Catch:{ all -> 0x00d5 }
                    r3 = r7;	 Catch:{ all -> 0x00d5 }
                    r3 = r3.getFileDescriptor();	 Catch:{ all -> 0x00d5 }
                    r2.<init>(r3);	 Catch:{ all -> 0x00d5 }
                    r9.writeTo(r2);	 Catch:{ all -> 0x00d5 }
                    r9.close();	 Catch:{ Throwable -> 0x00ea }
                    r9 = r7;	 Catch:{ Throwable -> 0x00ea }
                    if (r9 == 0) goto L_0x00cd;
                L_0x00c8:
                    r9 = r7;	 Catch:{ IOException -> 0x00cd }
                    r9.close();	 Catch:{ IOException -> 0x00cd }
                L_0x00cd:
                    r9 = r4;	 Catch:{ Throwable -> 0x00ea }
                    if (r1 == r9) goto L_0x00d4;	 Catch:{ Throwable -> 0x00ea }
                L_0x00d1:
                    r1.recycle();	 Catch:{ Throwable -> 0x00ea }
                L_0x00d4:
                    return r0;	 Catch:{ Throwable -> 0x00ea }
                L_0x00d5:
                    r0 = move-exception;	 Catch:{ Throwable -> 0x00ea }
                    r9.close();	 Catch:{ Throwable -> 0x00ea }
                    r9 = r7;	 Catch:{ Throwable -> 0x00ea }
                    if (r9 == 0) goto L_0x00e2;
                L_0x00dd:
                    r9 = r7;	 Catch:{ IOException -> 0x00e2 }
                    r9.close();	 Catch:{ IOException -> 0x00e2 }
                L_0x00e2:
                    r9 = r4;	 Catch:{ Throwable -> 0x00ea }
                    if (r1 == r9) goto L_0x00e9;	 Catch:{ Throwable -> 0x00ea }
                L_0x00e6:
                    r1.recycle();	 Catch:{ Throwable -> 0x00ea }
                L_0x00e9:
                    throw r0;	 Catch:{ Throwable -> 0x00ea }
                L_0x00ea:
                    r9 = move-exception;
                    return r9;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelper.PrintHelperApi19.2.doInBackground(java.lang.Void[]):java.lang.Throwable");
                }

                protected void onPostExecute(Throwable th) {
                    if (cancellationSignal2.isCanceled()) {
                        writeResultCallback2.onWriteCancelled();
                    } else if (th == null) {
                        writeResultCallback2.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    } else {
                        Log.e(PrintHelperApi19.LOG_TAG, "Error writing printed content", th);
                        writeResultCallback2.onWriteFailed(null);
                    }
                }
            }.execute(new Void[0]);
        }

        public void printBitmap(String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
            final int i = this.mScaleMode;
            final String str2 = str;
            final Uri uri2 = uri;
            final OnPrintFinishCallback onPrintFinishCallback2 = onPrintFinishCallback;
            PrintDocumentAdapter c01493 = new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;
                Bitmap mBitmap = null;
                AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

                public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    synchronized (this) {
                        this.mAttributes = printAttributes2;
                    }
                    if (cancellationSignal.isCanceled() != null) {
                        layoutResultCallback.onLayoutCancelled();
                    } else if (this.mBitmap != null) {
                        layoutResultCallback.onLayoutFinished(new PrintDocumentInfo.Builder(str2).setContentType(1).setPageCount(1).build(), printAttributes2.equals(printAttributes) ^ 1);
                    } else {
                        final CancellationSignal cancellationSignal2 = cancellationSignal;
                        final PrintAttributes printAttributes3 = printAttributes2;
                        final PrintAttributes printAttributes4 = printAttributes;
                        final LayoutResultCallback layoutResultCallback2 = layoutResultCallback;
                        this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>() {

                            /* renamed from: android.support.v4.print.PrintHelper$PrintHelperApi19$3$1$1 */
                            class C01471 implements OnCancelListener {
                                C01471() {
                                }

                                public void onCancel() {
                                    C01493.this.cancelLoad();
                                    C01481.this.cancel(false);
                                }
                            }

                            protected void onPreExecute() {
                                cancellationSignal2.setOnCancelListener(new C01471());
                            }

                            protected android.graphics.Bitmap doInBackground(android.net.Uri... r2) {
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
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                                /*
                                r1 = this;
                                r2 = android.support.v4.print.PrintHelper.PrintHelperApi19.C01493.this;	 Catch:{ FileNotFoundException -> 0x000d }
                                r2 = android.support.v4.print.PrintHelper.PrintHelperApi19.this;	 Catch:{ FileNotFoundException -> 0x000d }
                                r0 = android.support.v4.print.PrintHelper.PrintHelperApi19.C01493.this;	 Catch:{ FileNotFoundException -> 0x000d }
                                r0 = r3;	 Catch:{ FileNotFoundException -> 0x000d }
                                r2 = r2.loadConstrainedBitmap(r0);	 Catch:{ FileNotFoundException -> 0x000d }
                                return r2;
                            L_0x000d:
                                r2 = 0;
                                return r2;
                                */
                                throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelper.PrintHelperApi19.3.1.doInBackground(android.net.Uri[]):android.graphics.Bitmap");
                            }

                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                if (bitmap != null && (!PrintHelperApi19.this.mPrintActivityRespectsOrientation || PrintHelperApi19.this.mOrientation == 0)) {
                                    MediaSize mediaSize;
                                    synchronized (this) {
                                        mediaSize = C01493.this.mAttributes.getMediaSize();
                                    }
                                    if (!(mediaSize == null || mediaSize.isPortrait() == PrintHelperApi19.isPortrait(bitmap))) {
                                        Matrix matrix = new Matrix();
                                        matrix.postRotate(90.0f);
                                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                    }
                                }
                                C01493.this.mBitmap = bitmap;
                                if (bitmap != null) {
                                    layoutResultCallback2.onLayoutFinished(new PrintDocumentInfo.Builder(str2).setContentType(1).setPageCount(1).build(), true ^ printAttributes3.equals(printAttributes4));
                                } else {
                                    layoutResultCallback2.onLayoutFailed(null);
                                }
                                C01493.this.mLoadBitmap = null;
                            }

                            protected void onCancelled(Bitmap bitmap) {
                                layoutResultCallback2.onLayoutCancelled();
                                C01493.this.mLoadBitmap = null;
                            }
                        }.execute(new Uri[null]);
                    }
                }

                private void cancelLoad() {
                    synchronized (PrintHelperApi19.this.mLock) {
                        if (PrintHelperApi19.this.mDecodeOptions != null) {
                            PrintHelperApi19.this.mDecodeOptions.requestCancelDecode();
                            PrintHelperApi19.this.mDecodeOptions = null;
                        }
                    }
                }

                public void onFinish() {
                    super.onFinish();
                    cancelLoad();
                    if (this.mLoadBitmap != null) {
                        this.mLoadBitmap.cancel(true);
                    }
                    if (onPrintFinishCallback2 != null) {
                        onPrintFinishCallback2.onFinish();
                    }
                    if (this.mBitmap != null) {
                        this.mBitmap.recycle();
                        this.mBitmap = null;
                    }
                }

                public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
                    PrintHelperApi19.this.writeBitmap(this.mAttributes, i, this.mBitmap, parcelFileDescriptor, cancellationSignal, writeResultCallback);
                }
            };
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            onPrintFinishCallback = new Builder();
            onPrintFinishCallback.setColorMode(this.mColorMode);
            if (this.mOrientation != 1) {
                if (this.mOrientation != 0) {
                    if (this.mOrientation == 2) {
                        onPrintFinishCallback.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
                    }
                    printManager.print(str, c01493, onPrintFinishCallback.build());
                }
            }
            onPrintFinishCallback.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
            printManager.print(str, c01493, onPrintFinishCallback.build());
        }

        private Bitmap loadConstrainedBitmap(Uri uri) throws FileNotFoundException {
            if (uri != null) {
                if (this.mContext != null) {
                    Options options = new Options();
                    options.inJustDecodeBounds = true;
                    loadBitmap(uri, options);
                    int i = options.outWidth;
                    int i2 = options.outHeight;
                    if (i > 0) {
                        if (i2 > 0) {
                            int max = Math.max(i, i2);
                            int i3 = 1;
                            while (max > MAX_PRINT_SIZE) {
                                max >>>= 1;
                                i3 <<= 1;
                            }
                            if (i3 > 0) {
                                if (Math.min(i, i2) / i3 > 0) {
                                    Options options2;
                                    synchronized (this.mLock) {
                                        this.mDecodeOptions = new Options();
                                        this.mDecodeOptions.inMutable = true;
                                        this.mDecodeOptions.inSampleSize = i3;
                                        options2 = this.mDecodeOptions;
                                    }
                                    try {
                                        uri = loadBitmap(uri, options2);
                                        synchronized (this.mLock) {
                                            this.mDecodeOptions = null;
                                        }
                                        return uri;
                                    } catch (Throwable th) {
                                        synchronized (this.mLock) {
                                            this.mDecodeOptions = null;
                                        }
                                    }
                                }
                            }
                            return null;
                        }
                    }
                    return null;
                }
            }
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }

        private Bitmap loadBitmap(Uri uri, Options options) throws FileNotFoundException {
            if (uri != null) {
                if (this.mContext != null) {
                    InputStream inputStream = null;
                    try {
                        uri = this.mContext.getContentResolver().openInputStream(uri);
                        try {
                            options = BitmapFactory.decodeStream(uri, null, options);
                            if (uri != null) {
                                try {
                                    uri.close();
                                } catch (Uri uri2) {
                                    Log.w(LOG_TAG, "close fail ", uri2);
                                }
                            }
                            return options;
                        } catch (Throwable th) {
                            options = th;
                            inputStream = uri2;
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Uri uri22) {
                                    Log.w(LOG_TAG, "close fail ", uri22);
                                }
                            }
                            throw options;
                        }
                    } catch (Throwable th2) {
                        options = th2;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw options;
                    }
                }
            }
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }

        private Bitmap convertBitmapForColorMode(Bitmap bitmap, int i) {
            if (i != 1) {
                return bitmap;
            }
            i = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(i);
            Paint paint = new Paint();
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0.0f);
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
            canvas.setBitmap(null);
            return i;
        }
    }

    @RequiresApi(20)
    private static class PrintHelperApi20 extends PrintHelperApi19 {
        PrintHelperApi20(Context context) {
            super(context);
            this.mPrintActivityRespectsOrientation = null;
        }
    }

    @RequiresApi(23)
    private static class PrintHelperApi23 extends PrintHelperApi20 {
        protected Builder copyAttributes(PrintAttributes printAttributes) {
            Builder copyAttributes = super.copyAttributes(printAttributes);
            if (printAttributes.getDuplexMode() != 0) {
                copyAttributes.setDuplexMode(printAttributes.getDuplexMode());
            }
            return copyAttributes;
        }

        PrintHelperApi23(Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = null;
        }
    }

    @RequiresApi(24)
    private static class PrintHelperApi24 extends PrintHelperApi23 {
        PrintHelperApi24(Context context) {
            super(context);
            this.mIsMinMarginsHandlingCorrect = true;
            this.mPrintActivityRespectsOrientation = true;
        }
    }

    private static final class PrintHelperStub implements PrintHelperVersionImpl {
        int mColorMode;
        int mOrientation;
        int mScaleMode;

        public void printBitmap(String str, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        }

        public void printBitmap(String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback) {
        }

        private PrintHelperStub() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mOrientation = 1;
        }

        public void setScaleMode(int i) {
            this.mScaleMode = i;
        }

        public int getScaleMode() {
            return this.mScaleMode;
        }

        public int getColorMode() {
            return this.mColorMode;
        }

        public void setColorMode(int i) {
            this.mColorMode = i;
        }

        public void setOrientation(int i) {
            this.mOrientation = i;
        }

        public int getOrientation() {
            return this.mOrientation;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface ScaleMode {
    }

    public static boolean systemSupportsPrint() {
        return VERSION.SDK_INT >= 19;
    }

    public PrintHelper(Context context) {
        if (VERSION.SDK_INT >= 24) {
            this.mImpl = new PrintHelperApi24(context);
        } else if (VERSION.SDK_INT >= 23) {
            this.mImpl = new PrintHelperApi23(context);
        } else if (VERSION.SDK_INT >= 20) {
            this.mImpl = new PrintHelperApi20(context);
        } else if (VERSION.SDK_INT >= 19) {
            this.mImpl = new PrintHelperApi19(context);
        } else {
            this.mImpl = new PrintHelperStub();
        }
    }

    public void setScaleMode(int i) {
        this.mImpl.setScaleMode(i);
    }

    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }

    public void setColorMode(int i) {
        this.mImpl.setColorMode(i);
    }

    public int getColorMode() {
        return this.mImpl.getColorMode();
    }

    public void setOrientation(int i) {
        this.mImpl.setOrientation(i);
    }

    public int getOrientation() {
        return this.mImpl.getOrientation();
    }

    public void printBitmap(String str, Bitmap bitmap) {
        this.mImpl.printBitmap(str, bitmap, null);
    }

    public void printBitmap(String str, Bitmap bitmap, OnPrintFinishCallback onPrintFinishCallback) {
        this.mImpl.printBitmap(str, bitmap, onPrintFinishCallback);
    }

    public void printBitmap(String str, Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(str, uri, null);
    }

    public void printBitmap(String str, Uri uri, OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        this.mImpl.printBitmap(str, uri, onPrintFinishCallback);
    }
}
