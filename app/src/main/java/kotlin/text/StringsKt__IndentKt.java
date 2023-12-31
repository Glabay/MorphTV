package kotlin.text;

import ir.mahdi.mzip.rar.unpack.ppm.ModelPPM;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015"}, d2 = {"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: Indent.kt */
class StringsKt__IndentKt {
    @NotNull
    public static /* synthetic */ String trimMargin$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "|";
        }
        return StringsKt.trimMargin(str, str2);
    }

    @NotNull
    public static final String trimMargin(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        Intrinsics.checkParameterIsNotNull(str2, "marginPrefix");
        return StringsKt.replaceIndentByMargin(str, "", str2);
    }

    @NotNull
    public static /* synthetic */ String replaceIndentByMargin$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 1) != null) {
            str2 = "";
        }
        if ((i & 2) != 0) {
            str3 = "|";
        }
        return StringsKt.replaceIndentByMargin(str, str2, str3);
    }

    @NotNull
    public static final String replaceIndentByMargin(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        String str4 = str;
        String str5 = str3;
        Intrinsics.checkParameterIsNotNull(str4, "$receiver");
        Intrinsics.checkParameterIsNotNull(str2, "newIndent");
        Intrinsics.checkParameterIsNotNull(str5, "marginPrefix");
        if ((StringsKt.isBlank(str5) ^ 1) == 0) {
            throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
        }
        List<Object> lines = StringsKt.lines(str4);
        int length = str.length() + (str2.length() * lines.size());
        Function1 indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(str2);
        int lastIndex = CollectionsKt.getLastIndex(lines);
        Collection arrayList = new ArrayList();
        int i = 0;
        for (Object obj : lines) {
            Object obj2;
            int i2 = i + 1;
            Object obj3 = null;
            if ((i == 0 || i == lastIndex) && StringsKt.isBlank((CharSequence) obj2)) {
                obj2 = null;
            } else {
                int i3;
                CharSequence charSequence = (CharSequence) obj2;
                int length2 = charSequence.length();
                for (int i4 = 0; i4 < length2; i4++) {
                    if ((CharsKt.isWhitespace(charSequence.charAt(i4)) ^ 1) != 0) {
                        i3 = i4;
                        break;
                    }
                }
                i3 = -1;
                if (i3 != -1) {
                    int i5 = i3;
                    if (StringsKt.startsWith$default(obj2, str5, i3, false, 4, null)) {
                        i3 = i5 + str3.length();
                        if (obj2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                        }
                        obj3 = obj2.substring(i3);
                        Intrinsics.checkExpressionValueIsNotNull(obj3, "(this as java.lang.String).substring(startIndex)");
                    }
                }
                if (obj3 != null) {
                    str4 = (String) indentFunction$StringsKt__IndentKt.invoke(obj3);
                    if (str4 != null) {
                        obj2 = str4;
                    }
                }
            }
            if (obj2 != null) {
                arrayList.add(obj2);
            }
            i = i2;
        }
        str4 = ((StringBuilder) CollectionsKt.joinTo$default((List) arrayList, new StringBuilder(length), "\n", null, null, 0, null, null, ModelPPM.MAX_FREQ, null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(str4, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return str4;
    }

    @NotNull
    public static final String trimIndent(@NotNull String str) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        return StringsKt.replaceIndent(str, "");
    }

    @NotNull
    public static /* synthetic */ String replaceIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "";
        }
        return StringsKt.replaceIndent(str, str2);
    }

    @NotNull
    public static final String replaceIndent(@NotNull String str, @NotNull String str2) {
        String str3 = str;
        Intrinsics.checkParameterIsNotNull(str3, "$receiver");
        Intrinsics.checkParameterIsNotNull(str2, "newIndent");
        List lines = StringsKt.lines(str3);
        Iterable<Object> iterable = lines;
        Collection arrayList = new ArrayList();
        for (Object next : iterable) {
            if ((StringsKt.isBlank((String) next) ^ 1) != 0) {
                arrayList.add(next);
            }
        }
        Iterable<String> iterable2 = (List) arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        for (String indentWidth$StringsKt__IndentKt : iterable2) {
            arrayList2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(indentWidth$StringsKt__IndentKt)));
        }
        Integer num = (Integer) CollectionsKt.min((List) arrayList2);
        int i = 0;
        int intValue = num != null ? num.intValue() : 0;
        int length = str.length() + (str2.length() * lines.size());
        Function1 indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(str2);
        int lastIndex = CollectionsKt.getLastIndex(lines);
        Collection arrayList3 = new ArrayList();
        for (Object obj : iterable) {
            Object obj2;
            int i2 = i + 1;
            if ((i == 0 || i == lastIndex) && StringsKt.isBlank((CharSequence) obj2)) {
                obj2 = null;
            } else {
                String drop = StringsKt.drop(obj2, intValue);
                if (drop != null) {
                    drop = (String) indentFunction$StringsKt__IndentKt.invoke(drop);
                    if (drop != null) {
                        obj2 = drop;
                    }
                }
            }
            if (obj2 != null) {
                arrayList3.add(obj2);
            }
            i = i2;
        }
        str3 = ((StringBuilder) CollectionsKt.joinTo$default((List) arrayList3, new StringBuilder(length), "\n", null, null, 0, null, null, ModelPPM.MAX_FREQ, null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(str3, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return str3;
    }

    @NotNull
    public static /* synthetic */ String prependIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "    ";
        }
        return StringsKt.prependIndent(str, str2);
    }

    @NotNull
    public static final String prependIndent(@NotNull String str, @NotNull String str2) {
        Intrinsics.checkParameterIsNotNull(str, "$receiver");
        Intrinsics.checkParameterIsNotNull(str2, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence(str), new StringsKt__IndentKt$prependIndent$1(str2)), "\n", null, null, 0, null, null, 62, null);
    }

    private static final int indentWidth$StringsKt__IndentKt(@NotNull String str) {
        CharSequence charSequence = str;
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            if ((CharsKt.isWhitespace(charSequence.charAt(i)) ^ 1) != 0) {
                break;
            }
            i++;
        }
        i = -1;
        return i == -1 ? str.length() : i;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String str) {
        if ((((CharSequence) str).length() == 0 ? 1 : null) != null) {
            return (Function1) StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        }
        return new StringsKt__IndentKt$getIndentFunction$2(str);
    }

    private static final String reindent$StringsKt__IndentKt(@NotNull List<String> list, int i, Function1<? super String, String> function1, Function1<? super String, String> function12) {
        int lastIndex = CollectionsKt.getLastIndex(list);
        Collection arrayList = new ArrayList();
        int i2 = 0;
        for (Object obj : list) {
            Object obj2;
            int i3 = i2 + 1;
            if ((i2 == 0 || i2 == lastIndex) && StringsKt.isBlank((CharSequence) obj2)) {
                obj2 = null;
            } else {
                String str = (String) function12.invoke(obj2);
                if (str != null) {
                    str = (String) function1.invoke(str);
                    if (str != null) {
                        obj2 = str;
                    }
                }
            }
            if (obj2 != null) {
                arrayList.add(obj2);
            }
            i2 = i3;
        }
        list = ((StringBuilder) CollectionsKt.joinTo$default((List) arrayList, (Appendable) new StringBuilder(i), "\n", null, null, 0, null, null, ModelPPM.MAX_FREQ, null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(list, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return list;
    }
}
