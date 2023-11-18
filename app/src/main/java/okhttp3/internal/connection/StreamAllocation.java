package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.Socket;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.StreamResetException;

public final class StreamAllocation {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public final Address address;
    private final Object callStackTrace;
    private boolean canceled;
    private HttpCodec codec;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private int refusedStreamCount;
    private boolean released;
    private Route route;
    private final RouteSelector routeSelector;

    public static final class StreamAllocationReference extends WeakReference<StreamAllocation> {
        public final Object callStackTrace;

        StreamAllocationReference(StreamAllocation streamAllocation, Object obj) {
            super(streamAllocation);
            this.callStackTrace = obj;
        }
    }

    public StreamAllocation(ConnectionPool connectionPool, Address address, Object obj) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.routeSelector = new RouteSelector(address, routeDatabase());
        this.callStackTrace = obj;
    }

    public HttpCodec newStream(OkHttpClient okHttpClient, boolean z) {
        try {
            okHttpClient = findHealthyConnection(okHttpClient.connectTimeoutMillis(), okHttpClient.readTimeoutMillis(), okHttpClient.writeTimeoutMillis(), okHttpClient.retryOnConnectionFailure(), z).newCodec(okHttpClient, this);
            synchronized (this.connectionPool) {
                this.codec = okHttpClient;
            }
            return okHttpClient;
        } catch (OkHttpClient okHttpClient2) {
            throw new RouteException(okHttpClient2);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private okhttp3.internal.connection.RealConnection findHealthyConnection(int r4, int r5, int r6, boolean r7, boolean r8) throws java.io.IOException {
        /*
        r3 = this;
    L_0x0000:
        r0 = r3.findConnection(r4, r5, r6, r7);
        r1 = r3.connectionPool;
        monitor-enter(r1);
        r2 = r0.successCount;	 Catch:{ all -> 0x0019 }
        if (r2 != 0) goto L_0x000d;
    L_0x000b:
        monitor-exit(r1);	 Catch:{ all -> 0x0019 }
        return r0;
    L_0x000d:
        monitor-exit(r1);	 Catch:{ all -> 0x0019 }
        r1 = r0.isHealthy(r8);
        if (r1 != 0) goto L_0x0018;
    L_0x0014:
        r3.noNewStreams();
        goto L_0x0000;
    L_0x0018:
        return r0;
    L_0x0019:
        r4 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0019 }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.StreamAllocation.findHealthyConnection(int, int, int, boolean, boolean):okhttp3.internal.connection.RealConnection");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private okhttp3.internal.connection.RealConnection findConnection(int r7, int r8, int r9, boolean r10) throws java.io.IOException {
        /*
        r6 = this;
        r0 = r6.connectionPool;
        monitor-enter(r0);
        r1 = r6.released;	 Catch:{ all -> 0x00b3 }
        if (r1 == 0) goto L_0x000f;
    L_0x0007:
        r7 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00b3 }
        r8 = "released";
        r7.<init>(r8);	 Catch:{ all -> 0x00b3 }
        throw r7;	 Catch:{ all -> 0x00b3 }
    L_0x000f:
        r1 = r6.codec;	 Catch:{ all -> 0x00b3 }
        if (r1 == 0) goto L_0x001b;
    L_0x0013:
        r7 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x00b3 }
        r8 = "codec != null";
        r7.<init>(r8);	 Catch:{ all -> 0x00b3 }
        throw r7;	 Catch:{ all -> 0x00b3 }
    L_0x001b:
        r1 = r6.canceled;	 Catch:{ all -> 0x00b3 }
        if (r1 == 0) goto L_0x0027;
    L_0x001f:
        r7 = new java.io.IOException;	 Catch:{ all -> 0x00b3 }
        r8 = "Canceled";
        r7.<init>(r8);	 Catch:{ all -> 0x00b3 }
        throw r7;	 Catch:{ all -> 0x00b3 }
    L_0x0027:
        r1 = r6.connection;	 Catch:{ all -> 0x00b3 }
        if (r1 == 0) goto L_0x0031;
    L_0x002b:
        r2 = r1.noNewStreams;	 Catch:{ all -> 0x00b3 }
        if (r2 != 0) goto L_0x0031;
    L_0x002f:
        monitor-exit(r0);	 Catch:{ all -> 0x00b3 }
        return r1;
    L_0x0031:
        r1 = okhttp3.internal.Internal.instance;	 Catch:{ all -> 0x00b3 }
        r2 = r6.connectionPool;	 Catch:{ all -> 0x00b3 }
        r3 = r6.address;	 Catch:{ all -> 0x00b3 }
        r4 = 0;
        r1.get(r2, r3, r6, r4);	 Catch:{ all -> 0x00b3 }
        r1 = r6.connection;	 Catch:{ all -> 0x00b3 }
        if (r1 == 0) goto L_0x0043;
    L_0x003f:
        r7 = r6.connection;	 Catch:{ all -> 0x00b3 }
        monitor-exit(r0);	 Catch:{ all -> 0x00b3 }
        return r7;
    L_0x0043:
        r1 = r6.route;	 Catch:{ all -> 0x00b3 }
        monitor-exit(r0);	 Catch:{ all -> 0x00b3 }
        if (r1 != 0) goto L_0x004e;
    L_0x0048:
        r0 = r6.routeSelector;
        r1 = r0.next();
    L_0x004e:
        r2 = r6.connectionPool;
        monitor-enter(r2);
        r0 = r6.canceled;	 Catch:{ all -> 0x00b0 }
        if (r0 == 0) goto L_0x005d;
    L_0x0055:
        r7 = new java.io.IOException;	 Catch:{ all -> 0x00b0 }
        r8 = "Canceled";
        r7.<init>(r8);	 Catch:{ all -> 0x00b0 }
        throw r7;	 Catch:{ all -> 0x00b0 }
    L_0x005d:
        r0 = okhttp3.internal.Internal.instance;	 Catch:{ all -> 0x00b0 }
        r3 = r6.connectionPool;	 Catch:{ all -> 0x00b0 }
        r5 = r6.address;	 Catch:{ all -> 0x00b0 }
        r0.get(r3, r5, r6, r1);	 Catch:{ all -> 0x00b0 }
        r0 = r6.connection;	 Catch:{ all -> 0x00b0 }
        if (r0 == 0) goto L_0x006e;
    L_0x006a:
        r7 = r6.connection;	 Catch:{ all -> 0x00b0 }
        monitor-exit(r2);	 Catch:{ all -> 0x00b0 }
        return r7;
    L_0x006e:
        r6.route = r1;	 Catch:{ all -> 0x00b0 }
        r0 = 0;
        r6.refusedStreamCount = r0;	 Catch:{ all -> 0x00b0 }
        r0 = new okhttp3.internal.connection.RealConnection;	 Catch:{ all -> 0x00b0 }
        r3 = r6.connectionPool;	 Catch:{ all -> 0x00b0 }
        r0.<init>(r3, r1);	 Catch:{ all -> 0x00b0 }
        r6.acquire(r0);	 Catch:{ all -> 0x00b0 }
        monitor-exit(r2);	 Catch:{ all -> 0x00b0 }
        r0.connect(r7, r8, r9, r10);
        r7 = r6.routeDatabase();
        r8 = r0.route();
        r7.connected(r8);
        r7 = r6.connectionPool;
        monitor-enter(r7);
        r8 = okhttp3.internal.Internal.instance;	 Catch:{ all -> 0x00ad }
        r9 = r6.connectionPool;	 Catch:{ all -> 0x00ad }
        r8.put(r9, r0);	 Catch:{ all -> 0x00ad }
        r8 = r0.isMultiplexed();	 Catch:{ all -> 0x00ad }
        if (r8 == 0) goto L_0x00a8;
    L_0x009c:
        r8 = okhttp3.internal.Internal.instance;	 Catch:{ all -> 0x00ad }
        r9 = r6.connectionPool;	 Catch:{ all -> 0x00ad }
        r10 = r6.address;	 Catch:{ all -> 0x00ad }
        r4 = r8.deduplicate(r9, r10, r6);	 Catch:{ all -> 0x00ad }
        r0 = r6.connection;	 Catch:{ all -> 0x00ad }
    L_0x00a8:
        monitor-exit(r7);	 Catch:{ all -> 0x00ad }
        okhttp3.internal.Util.closeQuietly(r4);
        return r0;
    L_0x00ad:
        r8 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00ad }
        throw r8;
    L_0x00b0:
        r7 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00b0 }
        throw r7;
    L_0x00b3:
        r7 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00b3 }
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.connection.StreamAllocation.findConnection(int, int, int, boolean):okhttp3.internal.connection.RealConnection");
    }

    public void streamFinished(boolean z, HttpCodec httpCodec) {
        Socket deallocate;
        synchronized (this.connectionPool) {
            if (httpCodec != null) {
                if (httpCodec == this.codec) {
                    if (!z) {
                        RealConnection realConnection = this.connection;
                        realConnection.successCount++;
                    }
                    deallocate = deallocate(z, false, true);
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected ");
            stringBuilder.append(this.codec);
            stringBuilder.append(" but was ");
            stringBuilder.append(httpCodec);
            throw new IllegalStateException(stringBuilder.toString());
        }
        Util.closeQuietly(deallocate);
    }

    public HttpCodec codec() {
        HttpCodec httpCodec;
        synchronized (this.connectionPool) {
            httpCodec = this.codec;
        }
        return httpCodec;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public synchronized RealConnection connection() {
        return this.connection;
    }

    public void release() {
        Socket deallocate;
        synchronized (this.connectionPool) {
            deallocate = deallocate(false, true, false);
        }
        Util.closeQuietly(deallocate);
    }

    public void noNewStreams() {
        Socket deallocate;
        synchronized (this.connectionPool) {
            deallocate = deallocate(true, false, false);
        }
        Util.closeQuietly(deallocate);
    }

    private Socket deallocate(boolean z, boolean z2, boolean z3) {
        if (z3) {
            this.codec = null;
        }
        if (z2) {
            this.released = true;
        }
        if (this.connection) {
            if (z) {
                this.connection.noNewStreams = true;
            }
            if (!this.codec && (this.released || this.connection.noNewStreams)) {
                release(this.connection);
                if (this.connection.allocations.isEmpty()) {
                    this.connection.idleAtNanos = System.nanoTime();
                    if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                        z = this.connection.socket();
                        this.connection = null;
                        return z;
                    }
                }
                z = false;
                this.connection = null;
                return z;
            }
        }
        return false;
    }

    public void cancel() {
        synchronized (this.connectionPool) {
            this.canceled = true;
            HttpCodec httpCodec = this.codec;
            RealConnection realConnection = this.connection;
        }
        if (httpCodec != null) {
            httpCodec.cancel();
        } else if (realConnection != null) {
            realConnection.cancel();
        }
    }

    public void streamFailed(IOException iOException) {
        Socket deallocate;
        synchronized (this.connectionPool) {
            if (iOException instanceof StreamResetException) {
                StreamResetException streamResetException = (StreamResetException) iOException;
                if (streamResetException.errorCode == ErrorCode.REFUSED_STREAM) {
                    this.refusedStreamCount++;
                }
                if (streamResetException.errorCode != ErrorCode.REFUSED_STREAM || this.refusedStreamCount > 1) {
                    this.route = null;
                }
                iOException = null;
                deallocate = deallocate(iOException, false, true);
            } else {
                if (this.connection != null && (!this.connection.isMultiplexed() || (iOException instanceof ConnectionShutdownException))) {
                    if (this.connection.successCount == 0) {
                        if (!(this.route == null || iOException == null)) {
                            this.routeSelector.connectFailed(this.route, iOException);
                        }
                        this.route = null;
                    }
                }
                iOException = null;
                deallocate = deallocate(iOException, false, true);
            }
            iOException = true;
            deallocate = deallocate(iOException, false, true);
        }
        Util.closeQuietly(deallocate);
    }

    public void acquire(RealConnection realConnection) {
        if (this.connection != null) {
            throw new IllegalStateException();
        }
        this.connection = realConnection;
        realConnection.allocations.add(new StreamAllocationReference(this, this.callStackTrace));
    }

    private void release(RealConnection realConnection) {
        int size = realConnection.allocations.size();
        for (int i = 0; i < size; i++) {
            if (((Reference) realConnection.allocations.get(i)).get() == this) {
                realConnection.allocations.remove(i);
                return;
            }
        }
        throw new IllegalStateException();
    }

    public Socket releaseAndAcquire(RealConnection realConnection) {
        if (this.codec == null) {
            if (this.connection.allocations.size() == 1) {
                Reference reference = (Reference) this.connection.allocations.get(0);
                Socket deallocate = deallocate(true, false, false);
                this.connection = realConnection;
                realConnection.allocations.add(reference);
                return deallocate;
            }
        }
        throw new IllegalStateException();
    }

    public boolean hasMoreRoutes() {
        if (this.route == null) {
            if (!this.routeSelector.hasNext()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        RealConnection connection = connection();
        return connection != null ? connection.toString() : this.address.toString();
    }
}
