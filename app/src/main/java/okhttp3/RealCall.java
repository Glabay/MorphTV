package okhttp3;

import android.support.v4.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.EventListener.Factory;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http.BridgeInterceptor;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import okhttp3.internal.platform.Platform;

final class RealCall implements Call {
    final OkHttpClient client;
    final EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;

    final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;

        AsyncCall(Callback callback) {
            super("OkHttp %s", r4.redactedUrl());
            this.responseCallback = callback;
        }

        String host() {
            return RealCall.this.originalRequest.url().host();
        }

        Request request() {
            return RealCall.this.originalRequest;
        }

        RealCall get() {
            return RealCall.this;
        }

        protected void execute() {
            Throwable e;
            Platform platform;
            StringBuilder stringBuilder;
            Object obj = 1;
            try {
                Response responseWithInterceptorChain = RealCall.this.getResponseWithInterceptorChain();
                if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
                    try {
                        this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                    } catch (IOException e2) {
                        e = e2;
                        if (obj == null) {
                            this.responseCallback.onFailure(RealCall.this, e);
                        } else {
                            try {
                                platform = Platform.get();
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Callback failure for ");
                                stringBuilder.append(RealCall.this.toLoggableString());
                                platform.log(4, stringBuilder.toString(), e);
                            } catch (Throwable th) {
                                RealCall.this.client.dispatcher().finished(this);
                            }
                        }
                        RealCall.this.client.dispatcher().finished(this);
                    }
                }
                this.responseCallback.onResponse(RealCall.this, responseWithInterceptorChain);
            } catch (Throwable e3) {
                e = e3;
                obj = null;
                if (obj == null) {
                    platform = Platform.get();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Callback failure for ");
                    stringBuilder.append(RealCall.this.toLoggableString());
                    platform.log(4, stringBuilder.toString(), e);
                } else {
                    this.responseCallback.onFailure(RealCall.this, e);
                }
                RealCall.this.client.dispatcher().finished(this);
            }
            RealCall.this.client.dispatcher().finished(this);
        }
    }

    RealCall(OkHttpClient okHttpClient, Request request, boolean z) {
        Factory eventListenerFactory = okHttpClient.eventListenerFactory();
        this.client = okHttpClient;
        this.originalRequest = request;
        this.forWebSocket = z;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(okHttpClient, z);
        this.eventListener = eventListenerFactory.create(this);
    }

    public Request request() {
        return this.originalRequest;
    }

    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        try {
            this.client.dispatcher().executed(this);
            Response responseWithInterceptorChain = getResponseWithInterceptorChain();
            if (responseWithInterceptorChain != null) {
                return responseWithInterceptorChain;
            }
            throw new IOException("Canceled");
        } finally {
            this.client.dispatcher().finished(this);
        }
    }

    private void captureCallStackTrace() {
        this.retryAndFollowUpInterceptor.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }

    public void enqueue(Callback callback) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
            this.executed = true;
        }
        captureCallStackTrace();
        this.client.dispatcher().enqueue(new AsyncCall(callback));
    }

    public void cancel() {
        this.retryAndFollowUpInterceptor.cancel();
    }

    public synchronized boolean isExecuted() {
        return this.executed;
    }

    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }

    public RealCall clone() {
        return new RealCall(this.client, this.originalRequest, this.forWebSocket);
    }

    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }

    String toLoggableString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isCanceled() ? "canceled " : "");
        stringBuilder.append(this.forWebSocket ? "web socket" : NotificationCompat.CATEGORY_CALL);
        stringBuilder.append(" to ");
        stringBuilder.append(redactedUrl());
        return stringBuilder.toString();
    }

    String redactedUrl() {
        return this.originalRequest.url().redact();
    }

    Response getResponseWithInterceptorChain() throws IOException {
        List arrayList = new ArrayList();
        arrayList.addAll(this.client.interceptors());
        arrayList.add(this.retryAndFollowUpInterceptor);
        arrayList.add(new BridgeInterceptor(this.client.cookieJar()));
        arrayList.add(new CacheInterceptor(this.client.internalCache()));
        arrayList.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            arrayList.addAll(this.client.networkInterceptors());
        }
        arrayList.add(new CallServerInterceptor(this.forWebSocket));
        return new RealInterceptorChain(arrayList, null, null, null, 0, this.originalRequest).proceed(this.originalRequest);
    }
}
