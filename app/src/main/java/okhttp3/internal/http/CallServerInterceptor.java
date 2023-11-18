package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Interceptor$Chain;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.BufferedSink;
import okio.Okio;

public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    public Response intercept(Interceptor$Chain interceptor$Chain) throws IOException {
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) interceptor$Chain;
        HttpCodec httpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        interceptor$Chain = realInterceptorChain.request();
        long currentTimeMillis = System.currentTimeMillis();
        httpStream.writeRequestHeaders(interceptor$Chain);
        Builder builder = null;
        if (HttpMethod.permitsRequestBody(interceptor$Chain.method()) && interceptor$Chain.body() != null) {
            if ("100-continue".equalsIgnoreCase(interceptor$Chain.header(HttpHeaders.EXPECT))) {
                httpStream.flushRequest();
                builder = httpStream.readResponseHeaders(true);
            }
            if (builder == null) {
                BufferedSink buffer = Okio.buffer(httpStream.createRequestBody(interceptor$Chain, interceptor$Chain.body().contentLength()));
                interceptor$Chain.body().writeTo(buffer);
                buffer.close();
            } else if (!realConnection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpStream.finishRequest();
        if (builder == null) {
            builder = httpStream.readResponseHeaders(false);
        }
        interceptor$Chain = builder.request(interceptor$Chain).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int code = interceptor$Chain.code();
        if (this.forWebSocket && code == 101) {
            interceptor$Chain = interceptor$Chain.newBuilder().body(Util.EMPTY_RESPONSE).build();
        } else {
            interceptor$Chain = interceptor$Chain.newBuilder().body(httpStream.openResponseBody(interceptor$Chain)).build();
        }
        if ("close".equalsIgnoreCase(interceptor$Chain.request().header(HttpHeaders.CONNECTION)) || "close".equalsIgnoreCase(interceptor$Chain.header(HttpHeaders.CONNECTION))) {
            streamAllocation.noNewStreams();
        }
        if ((code != 204 && code != 205) || interceptor$Chain.body().contentLength() <= 0) {
            return interceptor$Chain;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP ");
        stringBuilder.append(code);
        stringBuilder.append(" had non-zero Content-Length: ");
        stringBuilder.append(interceptor$Chain.body().contentLength());
        throw new ProtocolException(stringBuilder.toString());
    }
}
