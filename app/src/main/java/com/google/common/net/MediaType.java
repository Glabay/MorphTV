package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableListMultimap.Builder;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharUtils;

@GwtCompatible
@Immutable
@Beta
public final class MediaType {
    public static final MediaType ANY_APPLICATION_TYPE = createConstant("application", WILDCARD);
    public static final MediaType ANY_AUDIO_TYPE = createConstant("audio", WILDCARD);
    public static final MediaType ANY_IMAGE_TYPE = createConstant(IMAGE_TYPE, WILDCARD);
    public static final MediaType ANY_TEXT_TYPE = createConstant("text", WILDCARD);
    public static final MediaType ANY_TYPE = createConstant(WILDCARD, WILDCARD);
    public static final MediaType ANY_VIDEO_TYPE = createConstant("video", WILDCARD);
    public static final MediaType APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
    public static final MediaType APPLICATION_BINARY = createConstant("application", "binary");
    private static final String APPLICATION_TYPE = "application";
    public static final MediaType APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
    public static final MediaType ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
    private static final String AUDIO_TYPE = "audio";
    public static final MediaType BMP = createConstant(IMAGE_TYPE, "bmp");
    public static final MediaType BZIP2 = createConstant("application", "x-bzip2");
    public static final MediaType CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
    private static final String CHARSET_ATTRIBUTE = "charset";
    public static final MediaType CRW = createConstant(IMAGE_TYPE, "x-canon-crw");
    public static final MediaType CSS_UTF_8 = createConstantUtf8("text", "css");
    public static final MediaType CSV_UTF_8 = createConstantUtf8("text", "csv");
    public static final MediaType EOT = createConstant("application", "vnd.ms-fontobject");
    public static final MediaType EPUB = createConstant("application", "epub+zip");
    public static final MediaType FORM_DATA = createConstant("application", "x-www-form-urlencoded");
    public static final MediaType GIF = createConstant(IMAGE_TYPE, "gif");
    public static final MediaType GZIP = createConstant("application", "x-gzip");
    public static final MediaType HTML_UTF_8 = createConstantUtf8("text", "html");
    public static final MediaType ICO = createConstant(IMAGE_TYPE, "vnd.microsoft.icon");
    private static final String IMAGE_TYPE = "image";
    public static final MediaType I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
    public static final MediaType JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
    public static final MediaType JPEG = createConstant(IMAGE_TYPE, "jpeg");
    public static final MediaType JSON_UTF_8 = createConstantUtf8("application", "json");
    public static final MediaType KEY_ARCHIVE = createConstant("application", "pkcs12");
    public static final MediaType KML = createConstant("application", "vnd.google-earth.kml+xml");
    public static final MediaType KMZ = createConstant("application", "vnd.google-earth.kmz");
    private static final Map<MediaType, MediaType> KNOWN_TYPES = Maps.newHashMap();
    private static final CharMatcher LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
    public static final MediaType MBOX = createConstant("application", "mbox");
    public static final MediaType MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
    public static final MediaType MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
    public static final MediaType MICROSOFT_WORD = createConstant("application", "msword");
    public static final MediaType MP4_AUDIO = createConstant("audio", "mp4");
    public static final MediaType MP4_VIDEO = createConstant("video", "mp4");
    public static final MediaType MPEG_AUDIO = createConstant("audio", "mpeg");
    public static final MediaType MPEG_VIDEO = createConstant("video", "mpeg");
    public static final MediaType OCTET_STREAM = createConstant("application", "octet-stream");
    public static final MediaType OGG_AUDIO = createConstant("audio", "ogg");
    public static final MediaType OGG_CONTAINER = createConstant("application", "ogg");
    public static final MediaType OGG_VIDEO = createConstant("video", "ogg");
    public static final MediaType OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
    public static final MediaType OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
    public static final MediaType OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static final MediaType OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
    public static final MediaType OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
    public static final MediaType OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
    public static final MediaType OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
    private static final MapJoiner PARAMETER_JOINER = Joiner.on("; ").withKeyValueSeparator("=");
    public static final MediaType PDF = createConstant("application", "pdf");
    public static final MediaType PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
    public static final MediaType PNG = createConstant(IMAGE_TYPE, "png");
    public static final MediaType POSTSCRIPT = createConstant("application", "postscript");
    public static final MediaType PROTOBUF = createConstant("application", "protobuf");
    public static final MediaType PSD = createConstant(IMAGE_TYPE, "vnd.adobe.photoshop");
    public static final MediaType QUICKTIME = createConstant("video", "quicktime");
    private static final CharMatcher QUOTED_TEXT_MATCHER = CharMatcher.ASCII.and(CharMatcher.noneOf("\"\\\r"));
    public static final MediaType RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
    public static final MediaType RTF_UTF_8 = createConstantUtf8("application", "rtf");
    public static final MediaType SFNT = createConstant("application", "font-sfnt");
    public static final MediaType SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
    public static final MediaType SKETCHUP = createConstant("application", "vnd.sketchup.skp");
    public static final MediaType SVG_UTF_8 = createConstantUtf8(IMAGE_TYPE, "svg+xml");
    public static final MediaType TAR = createConstant("application", "x-tar");
    public static final MediaType TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
    private static final String TEXT_TYPE = "text";
    public static final MediaType TIFF = createConstant(IMAGE_TYPE, "tiff");
    private static final CharMatcher TOKEN_MATCHER = CharMatcher.ASCII.and(CharMatcher.JAVA_ISO_CONTROL.negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
    public static final MediaType TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.of(CHARSET_ATTRIBUTE, Ascii.toLowerCase(Charsets.UTF_8.name()));
    public static final MediaType VCARD_UTF_8 = createConstantUtf8("text", "vcard");
    private static final String VIDEO_TYPE = "video";
    public static final MediaType WEBM_AUDIO = createConstant("audio", "webm");
    public static final MediaType WEBM_VIDEO = createConstant("video", "webm");
    public static final MediaType WEBP = createConstant(IMAGE_TYPE, "webp");
    private static final String WILDCARD = "*";
    public static final MediaType WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
    public static final MediaType WMV = createConstant("video", "x-ms-wmv");
    public static final MediaType WOFF = createConstant("application", "font-woff");
    public static final MediaType XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
    public static final MediaType XML_UTF_8 = createConstantUtf8("text", "xml");
    public static final MediaType XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
    public static final MediaType ZIP = createConstant("application", "zip");
    private final ImmutableListMultimap<String, String> parameters;
    private final String subtype;
    private final String type;

    private static MediaType createConstant(String str, String str2) {
        return addKnownType(new MediaType(str, str2, ImmutableListMultimap.of()));
    }

    private static MediaType createConstantUtf8(String str, String str2) {
        return addKnownType(new MediaType(str, str2, UTF_8_CONSTANT_PARAMETERS));
    }

    private static MediaType addKnownType(MediaType mediaType) {
        KNOWN_TYPES.put(mediaType, mediaType);
        return mediaType;
    }

    private MediaType(String str, String str2, ImmutableListMultimap<String, String> immutableListMultimap) {
        this.type = str;
        this.subtype = str2;
        this.parameters = immutableListMultimap;
    }

    public String type() {
        return this.type;
    }

    public String subtype() {
        return this.subtype;
    }

    public ImmutableListMultimap<String, String> parameters() {
        return this.parameters;
    }

    private Map<String, ImmutableMultiset<String>> parametersAsMap() {
        return Maps.transformValues(this.parameters.asMap(), new MediaType$1(this));
    }

    public Optional<Charset> charset() {
        Iterable copyOf = ImmutableSet.copyOf(this.parameters.get(CHARSET_ATTRIBUTE));
        switch (copyOf.size()) {
            case 0:
                return Optional.absent();
            case 1:
                return Optional.of(Charset.forName((String) Iterables.getOnlyElement(copyOf)));
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Multiple charset values defined: ");
                stringBuilder.append(copyOf);
                throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public MediaType withoutParameters() {
        return this.parameters.isEmpty() ? this : create(this.type, this.subtype);
    }

    public MediaType withParameters(Multimap<String, String> multimap) {
        return create(this.type, this.subtype, multimap);
    }

    public MediaType withParameter(String str, String str2) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        Object normalizeToken = normalizeToken(str);
        Builder builder = ImmutableListMultimap.builder();
        Iterator it = this.parameters.entries().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Object obj = (String) entry.getKey();
            if (!normalizeToken.equals(obj)) {
                builder.put(obj, entry.getValue());
            }
        }
        builder.put(normalizeToken, normalizeParameterValue(normalizeToken, str2));
        str = new MediaType(this.type, this.subtype, builder.build());
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(str), str);
    }

    public MediaType withCharset(Charset charset) {
        Preconditions.checkNotNull(charset);
        return withParameter(CHARSET_ATTRIBUTE, charset.name());
    }

    public boolean hasWildcard() {
        if (!WILDCARD.equals(this.type)) {
            if (!WILDCARD.equals(this.subtype)) {
                return false;
            }
        }
        return true;
    }

    public boolean is(MediaType mediaType) {
        return ((mediaType.type.equals(WILDCARD) || mediaType.type.equals(this.type)) && ((mediaType.subtype.equals(WILDCARD) || mediaType.subtype.equals(this.subtype)) && this.parameters.entries().containsAll(mediaType.parameters.entries()) != null)) ? true : null;
    }

    public static MediaType create(String str, String str2) {
        return create(str, str2, ImmutableListMultimap.of());
    }

    static MediaType createApplicationType(String str) {
        return create("application", str);
    }

    static MediaType createAudioType(String str) {
        return create("audio", str);
    }

    static MediaType createImageType(String str) {
        return create(IMAGE_TYPE, str);
    }

    static MediaType createTextType(String str) {
        return create("text", str);
    }

    static MediaType createVideoType(String str) {
        return create("video", str);
    }

    private static MediaType create(String str, String str2, Multimap<String, String> multimap) {
        boolean z;
        Builder builder;
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        Preconditions.checkNotNull(multimap);
        str = normalizeToken(str);
        str2 = normalizeToken(str2);
        if (WILDCARD.equals(str)) {
            if (!WILDCARD.equals(str2)) {
                z = false;
                Preconditions.checkArgument(z, "A wildcard type cannot be used with a non-wildcard subtype");
                builder = ImmutableListMultimap.builder();
                multimap = multimap.entries().iterator();
                while (multimap.hasNext()) {
                    Entry entry = (Entry) multimap.next();
                    Object normalizeToken = normalizeToken((String) entry.getKey());
                    builder.put(normalizeToken, normalizeParameterValue(normalizeToken, (String) entry.getValue()));
                }
                multimap = new MediaType(str, str2, builder.build());
                return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(multimap), multimap);
            }
        }
        z = true;
        Preconditions.checkArgument(z, "A wildcard type cannot be used with a non-wildcard subtype");
        builder = ImmutableListMultimap.builder();
        multimap = multimap.entries().iterator();
        while (multimap.hasNext()) {
            Entry entry2 = (Entry) multimap.next();
            Object normalizeToken2 = normalizeToken((String) entry2.getKey());
            builder.put(normalizeToken2, normalizeParameterValue(normalizeToken2, (String) entry2.getValue()));
        }
        multimap = new MediaType(str, str2, builder.build());
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(multimap), multimap);
    }

    private static String normalizeToken(String str) {
        Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(str));
        return Ascii.toLowerCase(str);
    }

    private static String normalizeParameterValue(String str, String str2) {
        return CHARSET_ATTRIBUTE.equals(str) != null ? Ascii.toLowerCase(str2) : str2;
    }

    public static MediaType parse(String str) {
        Preconditions.checkNotNull(str);
        MediaType$Tokenizer mediaType$Tokenizer = new MediaType$Tokenizer(str);
        try {
            String consumeToken = mediaType$Tokenizer.consumeToken(TOKEN_MATCHER);
            mediaType$Tokenizer.consumeCharacter(IOUtils.DIR_SEPARATOR_UNIX);
            String consumeToken2 = mediaType$Tokenizer.consumeToken(TOKEN_MATCHER);
            Builder builder = ImmutableListMultimap.builder();
            while (mediaType$Tokenizer.hasMore()) {
                Object stringBuilder;
                mediaType$Tokenizer.consumeCharacter(';');
                mediaType$Tokenizer.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
                Object consumeToken3 = mediaType$Tokenizer.consumeToken(TOKEN_MATCHER);
                mediaType$Tokenizer.consumeCharacter('=');
                if (Typography.quote == mediaType$Tokenizer.previewChar()) {
                    mediaType$Tokenizer.consumeCharacter(Typography.quote);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    while (Typography.quote != mediaType$Tokenizer.previewChar()) {
                        if (IOUtils.DIR_SEPARATOR_WINDOWS == mediaType$Tokenizer.previewChar()) {
                            mediaType$Tokenizer.consumeCharacter(IOUtils.DIR_SEPARATOR_WINDOWS);
                            stringBuilder2.append(mediaType$Tokenizer.consumeCharacter(CharMatcher.ASCII));
                        } else {
                            stringBuilder2.append(mediaType$Tokenizer.consumeToken(QUOTED_TEXT_MATCHER));
                        }
                    }
                    stringBuilder = stringBuilder2.toString();
                    mediaType$Tokenizer.consumeCharacter(Typography.quote);
                } else {
                    stringBuilder = mediaType$Tokenizer.consumeToken(TOKEN_MATCHER);
                }
                builder.put(consumeToken3, stringBuilder);
            }
            return create(consumeToken, consumeToken2, builder.build());
        } catch (Throwable e) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Could not parse '");
            stringBuilder3.append(str);
            stringBuilder3.append("'");
            throw new IllegalArgumentException(stringBuilder3.toString(), e);
        }
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaType)) {
            return false;
        }
        MediaType mediaType = (MediaType) obj;
        if (!this.type.equals(mediaType.type) || !this.subtype.equals(mediaType.subtype) || parametersAsMap().equals(mediaType.parametersAsMap()) == null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hashCode(this.type, this.subtype, parametersAsMap());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.type);
        stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
        stringBuilder.append(this.subtype);
        if (!this.parameters.isEmpty()) {
            stringBuilder.append("; ");
            PARAMETER_JOINER.appendTo(stringBuilder, Multimaps.transformValues(this.parameters, new MediaType$2(this)).entries());
        }
        return stringBuilder.toString();
    }

    private static String escapeAndQuote(String str) {
        StringBuilder stringBuilder = new StringBuilder(str.length() + 16);
        stringBuilder.append(Typography.quote);
        for (char c : str.toCharArray()) {
            if (c == CharUtils.CR || c == IOUtils.DIR_SEPARATOR_WINDOWS || c == Typography.quote) {
                stringBuilder.append(IOUtils.DIR_SEPARATOR_WINDOWS);
            }
            stringBuilder.append(c);
        }
        stringBuilder.append(Typography.quote);
        return stringBuilder.toString();
    }
}
