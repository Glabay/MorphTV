package io.github.morpheustv.scrapers.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import fi.iki.elonen.NanoHTTPD;
import io.github.morpheustv.scrapers.Scraper;
import io.github.morpheustv.scrapers.model.BaseProvider;
import java.io.ByteArrayInputStream;
import org.apache.commons.lang3.StringEscapeUtils;

public class WebviewController {
    private WebResourceResponse emptyResponse = new WebResourceResponse(NanoHTTPD.MIME_PLAINTEXT, "utf-8", new ByteArrayInputStream("".getBytes()));
    private Activity mContext;
    private Scraper scraper;
    private LinearLayout webviewContainer;

    /* renamed from: io.github.morpheustv.scrapers.controller.WebviewController$1 */
    class C13781 implements Runnable {
        C13781() {
        }

        public void run() {
            WebviewController.this.webviewContainer = new LinearLayout(WebviewController.this.mContext);
            WebviewController.this.webviewContainer.setLayoutParams(new LayoutParams(-2, -2));
            WebviewController.this.webviewContainer.setOrientation(1);
            WebviewController.this.webviewContainer.setVisibility(8);
        }
    }

    public WebviewController(Scraper scraper) {
        this.mContext = scraper.getContext();
        this.scraper = scraper;
        CookieManager.getInstance().setAcceptCookie(true);
        this.mContext.runOnUiThread(new C13781());
    }

    private void Log(String str, String str2) {
        this.scraper.Log(str, str2);
    }

    private boolean isBusy() {
        return this.scraper.isBusy();
    }

    public void addProvider(final BaseProvider baseProvider) {
        this.mContext.runOnUiThread(new Runnable() {

            /* renamed from: io.github.morpheustv.scrapers.controller.WebviewController$2$1 */
            class C13791 extends WebChromeClient {
                C13791() {
                }

                public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
                    jsResult.cancel();
                    return true;
                }

                public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
                    jsResult.cancel();
                    return true;
                }

                public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
                    jsPromptResult.cancel();
                    return true;
                }
            }

            /* renamed from: io.github.morpheustv.scrapers.controller.WebviewController$2$2 */
            class C13802 extends WebViewClient {
                C13802() {
                }

                public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                    baseProvider.loadedResources.add(str);
                    if (WebviewController.this.isBusy()) {
                        if (!str.equals(baseProvider.urlLoading)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(baseProvider.urlLoading);
                            stringBuilder.append("/");
                            if (!str.equals(stringBuilder.toString())) {
                                if (!baseProvider.allowResource(str)) {
                                    webView = WebviewController.this;
                                    String str2 = baseProvider.PROVIDER_NAME;
                                    StringBuilder stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("BlockResource: ");
                                    stringBuilder2.append(str);
                                    webView.Log(str2, stringBuilder2.toString());
                                    return WebviewController.this.emptyResponse;
                                }
                            }
                        }
                        return super.shouldInterceptRequest(webView, str);
                    }
                    WebviewController.this.destroyWebview(baseProvider);
                    return WebviewController.this.emptyResponse;
                }

                public void onLoadResource(WebView webView, String str) {
                    super.onLoadResource(webView, str);
                    webView = WebviewController.this;
                    String str2 = baseProvider.PROVIDER_NAME;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("LoadResource: ");
                    stringBuilder.append(str);
                    webView.Log(str2, stringBuilder.toString());
                    if (WebviewController.this.isBusy() == null) {
                        WebviewController.this.destroyWebview(baseProvider);
                    }
                }

                public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                    sslErrorHandler.proceed();
                }

                public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                    baseProvider.loadedResources.add(str);
                    webView = WebviewController.this;
                    bitmap = baseProvider.PROVIDER_NAME;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("PageStarted: ");
                    stringBuilder.append(str);
                    webView.Log(bitmap, stringBuilder.toString());
                    if (WebviewController.this.isBusy() == null) {
                        WebviewController.this.destroyWebview(baseProvider);
                    }
                }

                public void onPageFinished(WebView webView, String str) {
                    super.onPageFinished(webView, str);
                    baseProvider.urlLoading = "";
                    if (!(baseProvider.isBusy == null || baseProvider.isWaitingFinish == null)) {
                        baseProvider.isBusy = false;
                    }
                    webView = WebviewController.this;
                    String str2 = baseProvider.PROVIDER_NAME;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("PageFinished: ");
                    stringBuilder.append(str);
                    webView.Log(str2, stringBuilder.toString());
                    if (WebviewController.this.isBusy() == null) {
                        WebviewController.this.destroyWebview(baseProvider);
                    }
                }

                @RequiresApi(27)
                public void onSafeBrowsingHit(WebView webView, WebResourceRequest webResourceRequest, int i, SafeBrowsingResponse safeBrowsingResponse) {
                    safeBrowsingResponse.proceed(null);
                }
            }

            @SuppressLint({"SetJavaScriptEnabled"})
            public void run() {
                baseProvider.webView = new WebView(WebviewController.this.mContext);
                WebviewController.this.webviewContainer.addView(baseProvider.webView);
                WebSettings settings = baseProvider.webView.getSettings();
                settings.setUserAgentString(BaseProvider.UserAgent);
                settings.setAllowFileAccess(true);
                settings.setJavaScriptEnabled(true);
                settings.setLoadsImagesAutomatically(false);
                settings.setDomStorageEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setAppCacheEnabled(true);
                settings.setCacheMode(2);
                if (VERSION.SDK_INT >= 21) {
                    settings.setMixedContentMode(0);
                }
                baseProvider.webView.clearHistory();
                baseProvider.webView.clearFormData();
                baseProvider.webView.setWebChromeClient(new C13791());
                baseProvider.webView.setWebViewClient(new C13802());
            }
        });
    }

    public void loadUrl(final BaseProvider baseProvider, final String str) {
        if (isBusy()) {
            try {
                waitBusy(baseProvider);
                baseProvider.isBusy = true;
                baseProvider.isWaitingFinish = true;
                baseProvider.urlLoading = str;
                this.mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        baseProvider.webView.loadUrl(str, baseProvider.extraHeaders);
                    }
                });
                waitBusy(baseProvider);
            } catch (String str2) {
                str2.printStackTrace();
            }
        }
        baseProvider.isBusy = false;
        baseProvider.isWaitingFinish = false;
    }

    public void setJavascriptEnabled(final BaseProvider baseProvider, final boolean z) {
        this.mContext.runOnUiThread(new Runnable() {
            public void run() {
                baseProvider.webView.getSettings().setJavaScriptEnabled(z);
            }
        });
    }

    public void loadHtml(final BaseProvider baseProvider, final String str, final String str2) {
        if (isBusy()) {
            try {
                waitBusy(baseProvider);
                baseProvider.isBusy = true;
                baseProvider.isWaitingFinish = true;
                baseProvider.urlLoading = str2;
                this.mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        baseProvider.webView.loadDataWithBaseURL(str2, str, NanoHTTPD.MIME_HTML, "UTF-8", str2);
                    }
                });
                waitBusy(baseProvider);
            } catch (String str3) {
                str3.printStackTrace();
            }
        }
        baseProvider.isBusy = false;
        baseProvider.isWaitingFinish = false;
    }

    public void setHeader(final BaseProvider baseProvider, final String str, final String str2) {
        this.mContext.runOnUiThread(new Runnable() {
            public void run() {
                baseProvider.extraHeaders.put(str, str2);
            }
        });
    }

    public void clearHeaders(final BaseProvider baseProvider) {
        this.mContext.runOnUiThread(new Runnable() {
            public void run() {
                baseProvider.extraHeaders.clear();
            }
        });
    }

    public String getUrl(final BaseProvider baseProvider) {
        if (isBusy()) {
            try {
                waitBusy(baseProvider);
                baseProvider.isBusy = true;
                baseProvider.evalResult = "";
                this.mContext.runOnUiThread(new Runnable() {
                    public void run() {
                        baseProvider.evalResult = baseProvider.webView.getUrl();
                        baseProvider.isBusy = false;
                    }
                });
                waitBusy(baseProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            baseProvider.evalResult = "";
        }
        baseProvider.isBusy = false;
        return baseProvider.evalResult;
    }

    public String eval(final BaseProvider baseProvider, final String str) {
        if (isBusy()) {
            try {
                waitBusy(baseProvider);
                baseProvider.isBusy = true;
                baseProvider.evalResult = "";
                this.mContext.runOnUiThread(new Runnable() {

                    /* renamed from: io.github.morpheustv.scrapers.controller.WebviewController$9$1 */
                    class C13881 implements ValueCallback<String> {
                        C13881() {
                        }

                        public void onReceiveValue(String str) {
                            try {
                                baseProvider.evalResult = StringEscapeUtils.unescapeEcmaScript(str);
                            } catch (String str2) {
                                str2.printStackTrace();
                                baseProvider.evalResult = "";
                            }
                            baseProvider.isBusy = false;
                        }
                    }

                    public void run() {
                        baseProvider.webView.evaluateJavascript(str, new C13881());
                    }
                });
                waitBusy(baseProvider);
            } catch (String str2) {
                str2.printStackTrace();
            }
        } else {
            baseProvider.evalResult = "";
        }
        baseProvider.isBusy = null;
        return baseProvider.evalResult;
    }

    public void finish(BaseProvider baseProvider) {
        destroyWebview(baseProvider);
    }

    public void waitBusy(BaseProvider baseProvider) throws InterruptedException {
        while (baseProvider.isBusy && isBusy()) {
            Thread.sleep(10);
        }
    }

    public void destroyWebview(final BaseProvider baseProvider) {
        this.mContext.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    baseProvider.finished = true;
                    baseProvider.isBusy = false;
                    baseProvider.isWaitingFinish = false;
                    if (baseProvider.webView != null) {
                        baseProvider.webView.destroy();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
