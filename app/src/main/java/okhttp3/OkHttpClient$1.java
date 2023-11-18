package okhttp3;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import okhttp3.OkHttpClient.Builder;
import okhttp3.internal.Internal;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteDatabase;
import okhttp3.internal.connection.StreamAllocation;

class OkHttpClient$1 extends Internal {
    OkHttpClient$1() {
    }

    public void addLenient(Headers$Builder headers$Builder, String str) {
        headers$Builder.addLenient(str);
    }

    public void addLenient(Headers$Builder headers$Builder, String str, String str2) {
        headers$Builder.addLenient(str, str2);
    }

    public void setCache(Builder builder, InternalCache internalCache) {
        builder.setInternalCache(internalCache);
    }

    public boolean connectionBecameIdle(ConnectionPool connectionPool, RealConnection realConnection) {
        return connectionPool.connectionBecameIdle(realConnection);
    }

    public RealConnection get(ConnectionPool connectionPool, Address address, StreamAllocation streamAllocation, Route route) {
        return connectionPool.get(address, streamAllocation, route);
    }

    public boolean equalsNonHost(Address address, Address address2) {
        return address.equalsNonHost(address2);
    }

    public Socket deduplicate(ConnectionPool connectionPool, Address address, StreamAllocation streamAllocation) {
        return connectionPool.deduplicate(address, streamAllocation);
    }

    public void put(ConnectionPool connectionPool, RealConnection realConnection) {
        connectionPool.put(realConnection);
    }

    public RouteDatabase routeDatabase(ConnectionPool connectionPool) {
        return connectionPool.routeDatabase;
    }

    public int code(Response.Builder builder) {
        return builder.code;
    }

    public void apply(ConnectionSpec connectionSpec, SSLSocket sSLSocket, boolean z) {
        connectionSpec.apply(sSLSocket, z);
    }

    public HttpUrl getHttpUrlChecked(String str) throws MalformedURLException, UnknownHostException {
        return HttpUrl.getChecked(str);
    }

    public StreamAllocation streamAllocation(Call call) {
        return ((RealCall) call).streamAllocation();
    }

    public Call newWebSocketCall(OkHttpClient okHttpClient, Request request) {
        return new RealCall(okHttpClient, request, true);
    }
}
