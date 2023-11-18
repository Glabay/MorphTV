package com.tonyodev.fetch2.database;

import android.arch.persistence.room.TypeConverter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.media.MediaRouteProviderProtocol;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2.util.FetchDefaults;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 2}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u001c\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\tH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0004H\u0007J\u001c\u0010\u0013\u001a\u00020\t2\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\bH\u0007J\u0010\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\fH\u0007J\u0010\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u000eH\u0007J\u0010\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u001a\u001a\u00020\u0010H\u0007¨\u0006\u001b"}, d2 = {"Lcom/tonyodev/fetch2/database/Converter;", "", "()V", "fromErrorValue", "Lcom/tonyodev/fetch2/Error;", "value", "", "fromHeaderString", "", "", "headerString", "fromNetworkTypeValue", "Lcom/tonyodev/fetch2/NetworkType;", "fromPriorityValue", "Lcom/tonyodev/fetch2/Priority;", "fromStatusValue", "Lcom/tonyodev/fetch2/Status;", "toErrorValue", "error", "toHeaderString", "headerMap", "toNetworkTypeValue", "networkType", "toPriorityValue", "priority", "toStatusValue", "status", "fetch2_release"}, k = 1, mv = {1, 1, 10})
/* compiled from: Converter.kt */
public final class Converter {
    @NotNull
    @TypeConverter
    public final Status fromStatusValue(int i) {
        return Status.Companion.valueOf(i);
    }

    @TypeConverter
    public final int toStatusValue(@NotNull Status status) {
        Intrinsics.checkParameterIsNotNull(status, NotificationCompat.CATEGORY_STATUS);
        return status.getValue();
    }

    @NotNull
    @TypeConverter
    public final Map<String, String> fromHeaderString(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "headerString");
        Map<String, String> linkedHashMap = new LinkedHashMap();
        JSONObject jSONObject = new JSONObject(str);
        str = jSONObject.keys();
        Intrinsics.checkExpressionValueIsNotNull(str, "json.keys()");
        while (str.hasNext()) {
            String str2 = (String) str.next();
            Intrinsics.checkExpressionValueIsNotNull(str2, "it");
            String string = jSONObject.getString(str2);
            Intrinsics.checkExpressionValueIsNotNull(string, "json.getString(it)");
            linkedHashMap.put(str2, string);
        }
        return linkedHashMap;
    }

    @NotNull
    @TypeConverter
    public final String toHeaderString(@NotNull Map<String, String> map) {
        Intrinsics.checkParameterIsNotNull(map, "headerMap");
        if (map.isEmpty()) {
            return FetchDefaults.EMPTY_JSON_OBJECT_STRING;
        }
        JSONObject jSONObject = new JSONObject();
        map = map.entrySet().iterator();
        while (map.hasNext()) {
            Entry entry = (Entry) map.next();
            jSONObject.put((String) entry.getKey(), entry.getValue());
        }
        map = jSONObject.toString();
        Intrinsics.checkExpressionValueIsNotNull(map, "json.toString()");
        return map;
    }

    @NotNull
    @TypeConverter
    public final Priority fromPriorityValue(int i) {
        return Priority.Companion.valueOf(i);
    }

    @TypeConverter
    public final int toPriorityValue(@NotNull Priority priority) {
        Intrinsics.checkParameterIsNotNull(priority, "priority");
        return priority.getValue();
    }

    @NotNull
    @TypeConverter
    public final Error fromErrorValue(int i) {
        return Error.Companion.valueOf(i);
    }

    @TypeConverter
    public final int toErrorValue(@NotNull Error error) {
        Intrinsics.checkParameterIsNotNull(error, MediaRouteProviderProtocol.SERVICE_DATA_ERROR);
        return error.getValue();
    }

    @NotNull
    @TypeConverter
    public final NetworkType fromNetworkTypeValue(int i) {
        return NetworkType.Companion.valueOf(i);
    }

    @TypeConverter
    public final int toNetworkTypeValue(@NotNull NetworkType networkType) {
        Intrinsics.checkParameterIsNotNull(networkType, "networkType");
        return networkType.getValue();
    }
}
