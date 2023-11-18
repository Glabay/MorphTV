package com.google.android.gms.common.server;

import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.common.net.HttpHeaders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.text.Typography;

public class BaseApi {

    public static abstract class BaseApiaryOptions<DerivedClassType extends BaseApiaryOptions<DerivedClassType>> {
        private final ArrayList<String> zzvt = new ArrayList();
        private final HashMap<String, String> zzvu = new HashMap();
        private String zzvv;
        private final Collector zzvw = new Collector(this);

        public final class Collector {
            private boolean zzvx;
            private boolean zzvy;
            private int zzvz;
            private StringBuilder zzwa = new StringBuilder();
            private final /* synthetic */ BaseApiaryOptions zzwb;

            public Collector(BaseApiaryOptions baseApiaryOptions) {
                this.zzwb = baseApiaryOptions;
            }

            private final void append(String str) {
                StringBuilder stringBuilder;
                String str2;
                if (this.zzvx) {
                    this.zzvx = false;
                    stringBuilder = this.zzwa;
                    str2 = ",";
                } else {
                    if (this.zzvy) {
                        this.zzvy = false;
                        stringBuilder = this.zzwa;
                        str2 = "/";
                    }
                    this.zzwa.append(str);
                }
                stringBuilder.append(str2);
                this.zzwa.append(str);
            }

            public final void addPiece(String str) {
                append(str);
                this.zzvy = true;
            }

            public final void beginSubCollection(String str) {
                append(str);
                this.zzwa.append("(");
                this.zzvz++;
            }

            public final void endSubCollection() {
                this.zzwa.append(")");
                this.zzvz--;
                if (this.zzvz == 0) {
                    this.zzwb.addField(this.zzwa.toString());
                    this.zzwa.setLength(0);
                    this.zzvx = false;
                    this.zzvy = false;
                    return;
                }
                this.zzvx = true;
            }

            public final void finishPiece(String str) {
                append(str);
                if (this.zzvz == 0) {
                    this.zzwb.addField(this.zzwa.toString());
                    this.zzwa.setLength(0);
                    return;
                }
                this.zzvx = true;
            }
        }

        private static String zzcy() {
            return String.valueOf(DeviceProperties.isUserBuild() ^ 1);
        }

        public final DerivedClassType addField(String str) {
            this.zzvt.add(str);
            return this;
        }

        @Deprecated
        public final String appendParametersToUrl(String str) {
            str = BaseApi.append(str, "prettyPrint", zzcy());
            if (this.zzvv != null) {
                str = BaseApi.append(str, "trace", getTrace());
            }
            return !this.zzvt.isEmpty() ? BaseApi.append(str, "fields", TextUtils.join(",", getFields().toArray())) : str;
        }

        public void appendParametersToUrl(StringBuilder stringBuilder) {
            BaseApi.append(stringBuilder, "prettyPrint", zzcy());
            if (this.zzvv != null) {
                BaseApi.append(stringBuilder, "trace", getTrace());
            }
            if (!this.zzvt.isEmpty()) {
                BaseApi.append(stringBuilder, "fields", TextUtils.join(",", getFields().toArray()));
            }
        }

        public final DerivedClassType buildFrom(BaseApiaryOptions<?> baseApiaryOptions) {
            if (baseApiaryOptions.zzvv != null) {
                this.zzvv = baseApiaryOptions.zzvv;
            }
            if (!baseApiaryOptions.zzvt.isEmpty()) {
                this.zzvt.clear();
                this.zzvt.addAll(baseApiaryOptions.zzvt);
            }
            return this;
        }

        protected final Collector getCollector() {
            return this.zzvw;
        }

        public final List<String> getFields() {
            return this.zzvt;
        }

        public final Map<String, String> getHeaders() {
            return this.zzvu;
        }

        public final String getTrace() {
            return this.zzvv;
        }

        public final DerivedClassType setEtag(String str) {
            return setHeader(HttpHeaders.ETAG, str);
        }

        public final DerivedClassType setHeader(String str, String str2) {
            this.zzvu.put(str, str2);
            return this;
        }

        public final DerivedClassType setTraceByLdap(String str) {
            String valueOf = String.valueOf("email:");
            str = String.valueOf(str);
            this.zzvv = str.length() != 0 ? valueOf.concat(str) : new String(valueOf);
            return this;
        }

        public final DerivedClassType setTraceByToken(String str) {
            String valueOf = String.valueOf("token:");
            str = String.valueOf(str);
            this.zzvv = str.length() != 0 ? valueOf.concat(str) : new String(valueOf);
            return this;
        }
    }

    public static class FieldCollection<Parent> {
        private final Collector zzvw;
        private final Parent zzwc;

        protected FieldCollection(Parent parent, Collector collector) {
            Object obj;
            if (parent == null) {
                obj = this;
            }
            this.zzwc = obj;
            this.zzvw = collector;
        }

        protected Collector getCollector() {
            return this.zzvw;
        }

        protected Parent getParent() {
            return this.zzwc;
        }
    }

    @Deprecated
    public static String append(String str, String str2, String str3) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(str.indexOf("?") != -1 ? Typography.amp : '?');
        stringBuilder.append(str2);
        stringBuilder.append('=');
        stringBuilder.append(str3);
        return stringBuilder.toString();
    }

    public static void append(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append(stringBuilder.indexOf("?") != -1 ? Typography.amp : '?');
        stringBuilder.append(str);
        stringBuilder.append('=');
        stringBuilder.append(str2);
    }

    public static String enc(String str) {
        Preconditions.checkNotNull(str, "Encoding a null parameter!");
        return Uri.encode(str);
    }

    protected static List<String> enc(List<String> list) {
        int size = list.size();
        List arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(enc((String) list.get(i)));
        }
        return arrayList;
    }
}
