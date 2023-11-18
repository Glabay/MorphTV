package com.uwetrottmann.trakt5;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.uwetrottmann.trakt5.enums.ListPrivacy;
import com.uwetrottmann.trakt5.enums.Rating;
import com.uwetrottmann.trakt5.enums.Status;
import java.lang.reflect.Type;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

public class TraktV2Helper {

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$1 */
    static class C13591 implements JsonDeserializer<OffsetDateTime> {
        C13591() {
        }

        public OffsetDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return OffsetDateTime.parse(jsonElement.getAsString());
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$2 */
    static class C13602 implements JsonSerializer<OffsetDateTime> {
        C13602() {
        }

        public JsonElement serialize(OffsetDateTime offsetDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(offsetDateTime.toString());
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$3 */
    static class C13613 implements JsonDeserializer<LocalDate> {
        C13613() {
        }

        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsString());
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$4 */
    static class C13624 implements JsonDeserializer<ListPrivacy> {
        C13624() {
        }

        public ListPrivacy deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return ListPrivacy.fromValue(jsonElement.getAsString());
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$5 */
    static class C13635 implements JsonDeserializer<Rating> {
        C13635() {
        }

        public Rating deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Rating.fromValue(jsonElement.getAsInt());
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$6 */
    static class C13646 implements JsonSerializer<Rating> {
        C13646() {
        }

        public JsonElement serialize(Rating rating, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(Integer.valueOf(rating.value));
        }
    }

    /* renamed from: com.uwetrottmann.trakt5.TraktV2Helper$7 */
    static class C13657 implements JsonDeserializer<Status> {
        C13657() {
        }

        public Status deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Status.fromValue(jsonElement.getAsString());
        }
    }

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(OffsetDateTime.class, new C13591());
        gsonBuilder.registerTypeAdapter(OffsetDateTime.class, new C13602());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new C13613());
        gsonBuilder.registerTypeAdapter(ListPrivacy.class, new C13624());
        gsonBuilder.registerTypeAdapter(Rating.class, new C13635());
        gsonBuilder.registerTypeAdapter(Rating.class, new C13646());
        gsonBuilder.registerTypeAdapter(Status.class, new C13657());
        return gsonBuilder;
    }
}
