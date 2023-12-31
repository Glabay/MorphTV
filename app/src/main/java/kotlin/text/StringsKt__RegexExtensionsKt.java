package kotlin.text;

import java.util.Set;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0003H\b\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0006H\b¨\u0006\b"}, d2 = {"toRegex", "Lkotlin/text/Regex;", "Ljava/util/regex/Pattern;", "", "options", "", "Lkotlin/text/RegexOption;", "option", "kotlin-stdlib"}, k = 5, mv = {1, 1, 10}, xi = 1, xs = "kotlin/text/StringsKt")
/* compiled from: RegexExtensions.kt */
class StringsKt__RegexExtensionsKt extends StringsKt__IndentKt {
    @InlineOnly
    private static final Regex toRegex(@NotNull String str) {
        return new Regex(str);
    }

    @InlineOnly
    private static final Regex toRegex(@NotNull String str, RegexOption regexOption) {
        return new Regex(str, regexOption);
    }

    @InlineOnly
    private static final Regex toRegex(@NotNull String str, Set<? extends RegexOption> set) {
        return new Regex(str, set);
    }

    @InlineOnly
    private static final Regex toRegex(@NotNull Pattern pattern) {
        return new Regex(pattern);
    }
}
