package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;

public final class RouteSelector {
    private final Address address;
    private List<InetSocketAddress> inetSocketAddresses = Collections.emptyList();
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes = new ArrayList();
    private List<Proxy> proxies = Collections.emptyList();
    private final RouteDatabase routeDatabase;

    public RouteSelector(Address address, RouteDatabase routeDatabase) {
        this.address = address;
        this.routeDatabase = routeDatabase;
        resetNextProxy(address.url(), address.proxy());
    }

    public boolean hasNext() {
        if (!(hasNextInetSocketAddress() || hasNextProxy())) {
            if (!hasNextPostponed()) {
                return false;
            }
        }
        return true;
    }

    public Route next() throws IOException {
        if (!hasNextInetSocketAddress()) {
            if (hasNextProxy()) {
                this.lastProxy = nextProxy();
            } else if (hasNextPostponed()) {
                return nextPostponed();
            } else {
                throw new NoSuchElementException();
            }
        }
        this.lastInetSocketAddress = nextInetSocketAddress();
        Route route = new Route(this.address, this.lastProxy, this.lastInetSocketAddress);
        if (!this.routeDatabase.shouldPostpone(route)) {
            return route;
        }
        this.postponedRoutes.add(route);
        return next();
    }

    public void connectFailed(Route route, IOException iOException) {
        if (!(route.proxy().type() == Type.DIRECT || this.address.proxySelector() == null)) {
            this.address.proxySelector().connectFailed(this.address.url().uri(), route.proxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
    }

    private void resetNextProxy(HttpUrl httpUrl, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        } else {
            List select = this.address.proxySelector().select(httpUrl.uri());
            if (select == null || select.isEmpty() != null) {
                httpUrl = Util.immutableList(Proxy.NO_PROXY);
            } else {
                httpUrl = Util.immutableList(select);
            }
            this.proxies = httpUrl;
        }
        this.nextProxyIndex = 0;
    }

    private boolean hasNextProxy() {
        return this.nextProxyIndex < this.proxies.size();
    }

    private Proxy nextProxy() throws IOException {
        if (hasNextProxy()) {
            List list = this.proxies;
            int i = this.nextProxyIndex;
            this.nextProxyIndex = i + 1;
            Proxy proxy = (Proxy) list.get(i);
            resetNextInetSocketAddress(proxy);
            return proxy;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No route to ");
        stringBuilder.append(this.address.url().host());
        stringBuilder.append("; exhausted proxy configurations: ");
        stringBuilder.append(this.proxies);
        throw new SocketException(stringBuilder.toString());
    }

    private void resetNextInetSocketAddress(Proxy proxy) throws IOException {
        String hostString;
        int port;
        int size;
        int i;
        StringBuilder stringBuilder;
        this.inetSocketAddresses = new ArrayList();
        if (proxy.type() != Type.DIRECT) {
            if (proxy.type() != Type.SOCKS) {
                SocketAddress address = proxy.address();
                if (address instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                    hostString = getHostString(inetSocketAddress);
                    port = inetSocketAddress.getPort();
                    if (port >= 1) {
                        if (port > 65535) {
                            if (proxy.type() != Type.SOCKS) {
                                this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(hostString, port));
                            } else {
                                proxy = this.address.dns().lookup(hostString);
                                if (proxy.isEmpty()) {
                                    size = proxy.size();
                                    for (i = 0; i < size; i++) {
                                        this.inetSocketAddresses.add(new InetSocketAddress((InetAddress) proxy.get(i), port));
                                    }
                                } else {
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(this.address.dns());
                                    stringBuilder2.append(" returned no addresses for ");
                                    stringBuilder2.append(hostString);
                                    throw new UnknownHostException(stringBuilder2.toString());
                                }
                            }
                            this.nextInetSocketAddressIndex = 0;
                            return;
                        }
                    }
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("No route to ");
                    stringBuilder.append(hostString);
                    stringBuilder.append(":");
                    stringBuilder.append(port);
                    stringBuilder.append("; port is out of range");
                    throw new SocketException(stringBuilder.toString());
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Proxy.address() is not an InetSocketAddress: ");
                stringBuilder3.append(address.getClass());
                throw new IllegalArgumentException(stringBuilder3.toString());
            }
        }
        hostString = this.address.url().host();
        port = this.address.url().port();
        if (port >= 1) {
            if (port > 65535) {
                if (proxy.type() != Type.SOCKS) {
                    proxy = this.address.dns().lookup(hostString);
                    if (proxy.isEmpty()) {
                        size = proxy.size();
                        for (i = 0; i < size; i++) {
                            this.inetSocketAddresses.add(new InetSocketAddress((InetAddress) proxy.get(i), port));
                        }
                    } else {
                        StringBuilder stringBuilder22 = new StringBuilder();
                        stringBuilder22.append(this.address.dns());
                        stringBuilder22.append(" returned no addresses for ");
                        stringBuilder22.append(hostString);
                        throw new UnknownHostException(stringBuilder22.toString());
                    }
                }
                this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(hostString, port));
                this.nextInetSocketAddressIndex = 0;
                return;
            }
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("No route to ");
        stringBuilder.append(hostString);
        stringBuilder.append(":");
        stringBuilder.append(port);
        stringBuilder.append("; port is out of range");
        throw new SocketException(stringBuilder.toString());
    }

    static String getHostString(InetSocketAddress inetSocketAddress) {
        InetAddress address = inetSocketAddress.getAddress();
        if (address == null) {
            return inetSocketAddress.getHostName();
        }
        return address.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
    }

    private InetSocketAddress nextInetSocketAddress() throws IOException {
        if (hasNextInetSocketAddress()) {
            List list = this.inetSocketAddresses;
            int i = this.nextInetSocketAddressIndex;
            this.nextInetSocketAddressIndex = i + 1;
            return (InetSocketAddress) list.get(i);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No route to ");
        stringBuilder.append(this.address.url().host());
        stringBuilder.append("; exhausted inet socket addresses: ");
        stringBuilder.append(this.inetSocketAddresses);
        throw new SocketException(stringBuilder.toString());
    }

    private boolean hasNextPostponed() {
        return this.postponedRoutes.isEmpty() ^ 1;
    }

    private Route nextPostponed() {
        return (Route) this.postponedRoutes.remove(0);
    }
}
