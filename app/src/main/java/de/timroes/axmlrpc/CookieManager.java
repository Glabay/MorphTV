package de.timroes.axmlrpc;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

class CookieManager {
    private static final String COOKIE = "Cookie";
    private static final String SET_COOKIE = "Set-Cookie";
    private Map<String, String> cookies = new ConcurrentHashMap();
    private int flags;

    public CookieManager(int i) {
        this.flags = i;
    }

    public void clearCookies() {
        this.cookies.clear();
    }

    public Map<String, String> getCookies() {
        return this.cookies;
    }

    public void readCookies(HttpURLConnection httpURLConnection) {
        if ((this.flags & 4) != 0) {
            for (int i = 0; i < httpURLConnection.getHeaderFields().size(); i++) {
                String headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
                if (headerFieldKey != null && "Set-Cookie".toLowerCase().equals(headerFieldKey.toLowerCase())) {
                    String[] split = httpURLConnection.getHeaderField(i).split(";")[0].split("=");
                    if (split.length >= 2) {
                        this.cookies.put(split[0], split[1]);
                    }
                }
            }
        }
    }

    public void setCookies(HttpURLConnection httpURLConnection) {
        if ((this.flags & 4) != 0) {
            String str = "";
            for (Entry entry : this.cookies.entrySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append((String) entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append((String) entry.getValue());
                stringBuilder.append("; ");
                str = stringBuilder.toString();
            }
            httpURLConnection.setRequestProperty("Cookie", str);
        }
    }
}
