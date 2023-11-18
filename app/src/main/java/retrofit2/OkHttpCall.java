package retrofit2;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

final class OkHttpCall<T> implements Call<T> {
    @Nullable
    private final Object[] args;
    private volatile boolean canceled;
    @GuardedBy("this")
    @Nullable
    private Throwable creationFailure;
    @GuardedBy("this")
    private boolean executed;
    @GuardedBy("this")
    @Nullable
    private Call rawCall;
    private final ServiceMethod<T, ?> serviceMethod;

    OkHttpCall(ServiceMethod<T, ?> serviceMethod, @Nullable Object[] objArr) {
        this.serviceMethod = serviceMethod;
        this.args = objArr;
    }

    public OkHttpCall<T> clone() {
        return new OkHttpCall(this.serviceMethod, this.args);
    }

    public synchronized Request request() {
        Call call = this.rawCall;
        if (call != null) {
            return call.request();
        } else if (this.creationFailure == null) {
            try {
                call = createRawCall();
                this.rawCall = call;
                return call.request();
            } catch (Throwable e) {
                this.creationFailure = e;
                throw e;
            } catch (Throwable e2) {
                this.creationFailure = e2;
                throw new RuntimeException("Unable to create request.", e2);
            }
        } else if (this.creationFailure instanceof IOException) {
            throw new RuntimeException("Unable to create request.", this.creationFailure);
        } else {
            throw ((RuntimeException) this.creationFailure);
        }
    }

    public void enqueue(Callback<T> callback) {
        Utils.checkNotNull(callback, "callback == null");
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            Call call = this.rawCall;
            Throwable th = this.creationFailure;
            if (call == null && th == null) {
                try {
                    Call createRawCall = createRawCall();
                    this.rawCall = createRawCall;
                    call = createRawCall;
                } catch (Throwable th2) {
                    th = th2;
                    this.creationFailure = th;
                }
            }
        }
        if (th != null) {
            callback.onFailure(this, th);
            return;
        }
        if (this.canceled) {
            call.cancel();
        }
        call.enqueue(new OkHttpCall$1(this, callback));
    }

    public synchronized boolean isExecuted() {
        return this.executed;
    }

    public Response<T> execute() throws IOException {
        Call call;
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already executed.");
            }
            this.executed = true;
            if (this.creationFailure == null) {
                call = this.rawCall;
                if (call == null) {
                    try {
                        call = createRawCall();
                        this.rawCall = call;
                    } catch (Throwable e) {
                        this.creationFailure = e;
                        throw e;
                    }
                }
            } else if (this.creationFailure instanceof IOException) {
                throw ((IOException) this.creationFailure);
            } else {
                throw ((RuntimeException) this.creationFailure);
            }
        }
        if (this.canceled) {
            call.cancel();
        }
        return parseResponse(call.execute());
    }

    private Call createRawCall() throws IOException {
        Call newCall = this.serviceMethod.callFactory.newCall(this.serviceMethod.toRequest(this.args));
        if (newCall != null) {
            return newCall;
        }
        throw new NullPointerException("Call.Factory returned null.");
    }

    Response<T> parseResponse(Response response) throws IOException {
        ResponseBody body = response.body();
        response = response.newBuilder().body(new OkHttpCall$NoContentResponseBody(body.contentType(), body.contentLength())).build();
        int code = response.code();
        if (code >= Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
            if (code < 300) {
                if (code != 204) {
                    if (code != 205) {
                        ResponseBody okHttpCall$ExceptionCatchingRequestBody = new OkHttpCall$ExceptionCatchingRequestBody(body);
                        try {
                            return Response.success(this.serviceMethod.toResponse(okHttpCall$ExceptionCatchingRequestBody), response);
                        } catch (Response response2) {
                            okHttpCall$ExceptionCatchingRequestBody.throwIfCaught();
                            throw response2;
                        }
                    }
                }
                body.close();
                return Response.success(null, response2);
            }
        }
        try {
            response2 = Response.error(Utils.buffer(body), response2);
            return response2;
        } finally {
            body.close();
        }
    }

    public void cancel() {
        this.canceled = true;
        synchronized (this) {
            Call call = this.rawCall;
        }
        if (call != null) {
            call.cancel();
        }
    }

    public boolean isCanceled() {
        boolean z = true;
        if (this.canceled) {
            return true;
        }
        synchronized (this) {
            if (this.rawCall == null || !this.rawCall.isCanceled()) {
                z = false;
            }
        }
        return z;
    }
}
