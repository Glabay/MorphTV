package okhttp3.internal.http;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Interceptor$Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http2.ConnectionShutdownException;

public final class RetryAndFollowUpInterceptor implements Interceptor {
    private static final int MAX_FOLLOW_UPS = 20;
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private StreamAllocation streamAllocation;

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient, boolean z) {
        this.client = okHttpClient;
        this.forWebSocket = z;
    }

    public void cancel() {
        this.canceled = true;
        StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            streamAllocation.cancel();
        }
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCallStackTrace(Object obj) {
        this.callStackTrace = obj;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }

    public Response intercept(Interceptor$Chain interceptor$Chain) throws IOException {
        Request request = interceptor$Chain.request();
        this.streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(request.url()), this.callStackTrace);
        Response response = null;
        int i = 0;
        while (!this.canceled) {
            try {
                Response proceed = ((RealInterceptorChain) interceptor$Chain).proceed(request, this.streamAllocation, null, null);
                response = response != null ? proceed.newBuilder().priorResponse(response.newBuilder().body(null).build()).build() : proceed;
                request = followUpRequest(response);
                if (request == null) {
                    if (this.forWebSocket == null) {
                        this.streamAllocation.release();
                    }
                    return response;
                }
                Util.closeQuietly(response.body());
                i++;
                StringBuilder stringBuilder;
                if (i > 20) {
                    this.streamAllocation.release();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Too many follow-up requests: ");
                    stringBuilder.append(i);
                    throw new ProtocolException(stringBuilder.toString());
                } else if (request.body() instanceof UnrepeatableRequestBody) {
                    this.streamAllocation.release();
                    throw new HttpRetryException("Cannot retry streamed HTTP body", response.code());
                } else if (!sameConnection(response, request.url())) {
                    this.streamAllocation.release();
                    this.streamAllocation = new StreamAllocation(this.client.connectionPool(), createAddress(request.url()), this.callStackTrace);
                } else if (this.streamAllocation.codec() != null) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Closing the body of ");
                    stringBuilder.append(response);
                    stringBuilder.append(" didn't close its backing stream. Bad interceptor?");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } catch (RouteException e) {
                if (!recover(e.getLastConnectException(), false, request)) {
                    throw e.getLastConnectException();
                }
            } catch (IOException e2) {
                if (!recover(e2, !(e2 instanceof ConnectionShutdownException), request)) {
                    throw e2;
                }
            } catch (Throwable th) {
                this.streamAllocation.streamFailed(null);
                this.streamAllocation.release();
            }
        }
        this.streamAllocation.release();
        throw new IOException("Canceled");
    }

    private Address createAddress(HttpUrl httpUrl) {
        HostnameVerifier hostnameVerifier;
        SSLSocketFactory sSLSocketFactory;
        CertificatePinner certificatePinner;
        RetryAndFollowUpInterceptor retryAndFollowUpInterceptor = this;
        if (httpUrl.isHttps()) {
            SSLSocketFactory sslSocketFactory = retryAndFollowUpInterceptor.client.sslSocketFactory();
            hostnameVerifier = retryAndFollowUpInterceptor.client.hostnameVerifier();
            sSLSocketFactory = sslSocketFactory;
            certificatePinner = retryAndFollowUpInterceptor.client.certificatePinner();
        } else {
            sSLSocketFactory = null;
            hostnameVerifier = sSLSocketFactory;
            certificatePinner = hostnameVerifier;
        }
        return new Address(httpUrl.host(), httpUrl.port(), retryAndFollowUpInterceptor.client.dns(), retryAndFollowUpInterceptor.client.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, retryAndFollowUpInterceptor.client.proxyAuthenticator(), retryAndFollowUpInterceptor.client.proxy(), retryAndFollowUpInterceptor.client.protocols(), retryAndFollowUpInterceptor.client.connectionSpecs(), retryAndFollowUpInterceptor.client.proxySelector());
    }

    private boolean recover(IOException iOException, boolean z, Request request) {
        this.streamAllocation.streamFailed(iOException);
        if (!this.client.retryOnConnectionFailure()) {
            return false;
        }
        if ((z && (request.body() instanceof UnrepeatableRequestBody) != null) || isRecoverable(iOException, z) == null || this.streamAllocation.hasMoreRoutes() == null) {
            return false;
        }
        return true;
    }

    private boolean isRecoverable(IOException iOException, boolean z) {
        boolean z2 = false;
        if (iOException instanceof ProtocolException) {
            return false;
        }
        if (iOException instanceof InterruptedIOException) {
            if (!((iOException instanceof SocketTimeoutException) == null || z)) {
                z2 = true;
            }
            return z2;
        } else if ((!(iOException instanceof SSLHandshakeException) || !(iOException.getCause() instanceof CertificateException)) && (iOException instanceof SSLPeerUnverifiedException) == null) {
            return true;
        } else {
            return false;
        }
    }

    private Request followUpRequest(Response response) throws IOException {
        if (response == null) {
            throw new IllegalStateException();
        }
        Connection connection = this.streamAllocation.connection();
        RequestBody requestBody = null;
        Route route = connection != null ? connection.route() : null;
        int code = response.code();
        String method = response.request().method();
        switch (code) {
            case 300:
            case 301:
            case 302:
            case 303:
                break;
            case StatusLine.HTTP_TEMP_REDIRECT /*307*/:
            case StatusLine.HTTP_PERM_REDIRECT /*308*/:
                if (!(method.equals("GET") || method.equals("HEAD"))) {
                    return null;
                }
            case 401:
                return this.client.authenticator().authenticate(route, response);
            case 407:
                Proxy proxy;
                if (route != null) {
                    proxy = route.proxy();
                } else {
                    proxy = this.client.proxy();
                }
                if (proxy.type() == Type.HTTP) {
                    return this.client.proxyAuthenticator().authenticate(route, response);
                }
                throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
            case 408:
                if (response.request().body() instanceof UnrepeatableRequestBody) {
                    return null;
                }
                return response.request();
            default:
                return null;
        }
        if (!this.client.followRedirects()) {
            return null;
        }
        String header = response.header(HttpHeaders.LOCATION);
        if (header == null) {
            return null;
        }
        HttpUrl resolve = response.request().url().resolve(header);
        if (resolve == null) {
            return null;
        }
        if (!resolve.scheme().equals(response.request().url().scheme()) && !this.client.followSslRedirects()) {
            return null;
        }
        Builder newBuilder = response.request().newBuilder();
        if (HttpMethod.permitsRequestBody(method)) {
            boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
            if (HttpMethod.redirectsToGet(method)) {
                newBuilder.method("GET", null);
            } else {
                if (redirectsWithBody) {
                    requestBody = response.request().body();
                }
                newBuilder.method(method, requestBody);
            }
            if (!redirectsWithBody) {
                newBuilder.removeHeader(HttpHeaders.TRANSFER_ENCODING);
                newBuilder.removeHeader(HttpHeaders.CONTENT_LENGTH);
                newBuilder.removeHeader("Content-Type");
            }
        }
        if (sameConnection(response, resolve) == null) {
            newBuilder.removeHeader("Authorization");
        }
        return newBuilder.url(resolve).build();
    }

    private boolean sameConnection(Response response, HttpUrl httpUrl) {
        response = response.request().url();
        return (response.host().equals(httpUrl.host()) && response.port() == httpUrl.port() && response.scheme().equals(httpUrl.scheme()) != null) ? true : null;
    }
}
