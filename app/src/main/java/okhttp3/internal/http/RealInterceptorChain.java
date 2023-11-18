package okhttp3.internal.http;

import java.io.IOException;
import java.util.List;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Interceptor$Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;

public final class RealInterceptorChain implements Interceptor$Chain {
    private int calls;
    private final RealConnection connection;
    private final HttpCodec httpCodec;
    private final int index;
    private final List<Interceptor> interceptors;
    private final Request request;
    private final StreamAllocation streamAllocation;

    public RealInterceptorChain(List<Interceptor> list, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection realConnection, int i, Request request) {
        this.interceptors = list;
        this.connection = realConnection;
        this.streamAllocation = streamAllocation;
        this.httpCodec = httpCodec;
        this.index = i;
        this.request = request;
    }

    public Connection connection() {
        return this.connection;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    public HttpCodec httpStream() {
        return this.httpCodec;
    }

    public Request request() {
        return this.request;
    }

    public Response proceed(Request request) throws IOException {
        return proceed(request, this.streamAllocation, this.httpCodec, this.connection);
    }

    public Response proceed(Request request, StreamAllocation streamAllocation, HttpCodec httpCodec, RealConnection realConnection) throws IOException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        this.calls++;
        if (this.httpCodec != null && !this.connection.supportsUrl(request.url())) {
            streamAllocation = new StringBuilder();
            streamAllocation.append("network interceptor ");
            streamAllocation.append(this.interceptors.get(this.index - 1));
            streamAllocation.append(" must retain the same host and port");
            throw new IllegalStateException(streamAllocation.toString());
        } else if (this.httpCodec == null || this.calls <= 1) {
            RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, streamAllocation, httpCodec, realConnection, this.index + 1, request);
            Interceptor interceptor = (Interceptor) this.interceptors.get(this.index);
            streamAllocation = interceptor.intercept(realInterceptorChain);
            if (httpCodec != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1) {
                httpCodec = new StringBuilder();
                httpCodec.append("network interceptor ");
                httpCodec.append(interceptor);
                httpCodec.append(" must call proceed() exactly once");
                throw new IllegalStateException(httpCodec.toString());
            } else if (streamAllocation != null) {
                return streamAllocation;
            } else {
                httpCodec = new StringBuilder();
                httpCodec.append("interceptor ");
                httpCodec.append(interceptor);
                httpCodec.append(" returned null");
                throw new NullPointerException(httpCodec.toString());
            }
        } else {
            streamAllocation = new StringBuilder();
            streamAllocation.append("network interceptor ");
            streamAllocation.append(this.interceptors.get(this.index - 1));
            streamAllocation.append(" must call proceed() exactly once");
            throw new IllegalStateException(streamAllocation.toString());
        }
    }
}
