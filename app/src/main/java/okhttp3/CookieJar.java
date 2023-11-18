package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
    public static final CookieJar NO_COOKIES = new C14091();

    /* renamed from: okhttp3.CookieJar$1 */
    class C14091 implements CookieJar {
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        }

        C14091() {
        }

        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            return Collections.emptyList();
        }
    }

    List<Cookie> loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List<Cookie> list);
}
