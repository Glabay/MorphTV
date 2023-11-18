package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class StrSubstitutor {
    public static final char DEFAULT_ESCAPE = '$';
    public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
    public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
    public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
    private boolean enableSubstitutionInVariables;
    private char escapeChar;
    private StrMatcher prefixMatcher;
    private boolean preserveEscapes;
    private StrMatcher suffixMatcher;
    private StrMatcher valueDelimiterMatcher;
    private StrLookup<?> variableResolver;

    public static <V> String replace(Object obj, Map<String, V> map) {
        return new StrSubstitutor((Map) map).replace(obj);
    }

    public static <V> String replace(Object obj, Map<String, V> map, String str, String str2) {
        return new StrSubstitutor(map, str, str2).replace(obj);
    }

    public static String replace(Object obj, Properties properties) {
        if (properties == null) {
            return obj.toString();
        }
        Map hashMap = new HashMap();
        Enumeration propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String str = (String) propertyNames.nextElement();
            hashMap.put(str, properties.getProperty(str));
        }
        return replace(obj, hashMap);
    }

    public static String replaceSystemProperties(Object obj) {
        return new StrSubstitutor(StrLookup.systemPropertiesLookup()).replace(obj);
    }

    public StrSubstitutor() {
        this((StrLookup) null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public <V> StrSubstitutor(Map<String, V> map) {
        this(StrLookup.mapLookup(map), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public <V> StrSubstitutor(Map<String, V> map, String str, String str2) {
        this(StrLookup.mapLookup(map), str, str2, '$');
    }

    public <V> StrSubstitutor(Map<String, V> map, String str, String str2, char c) {
        this(StrLookup.mapLookup(map), str, str2, c);
    }

    public <V> StrSubstitutor(Map<String, V> map, String str, String str2, char c, String str3) {
        this(StrLookup.mapLookup(map), str, str2, c, str3);
    }

    public StrSubstitutor(StrLookup<?> strLookup) {
        this((StrLookup) strLookup, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
    }

    public StrSubstitutor(StrLookup<?> strLookup, String str, String str2, char c) {
        this.preserveEscapes = false;
        setVariableResolver(strLookup);
        setVariablePrefix(str);
        setVariableSuffix(str2);
        setEscapeChar(c);
        setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
    }

    public StrSubstitutor(StrLookup<?> strLookup, String str, String str2, char c, String str3) {
        this.preserveEscapes = false;
        setVariableResolver(strLookup);
        setVariablePrefix(str);
        setVariableSuffix(str2);
        setEscapeChar(c);
        setValueDelimiter(str3);
    }

    public StrSubstitutor(StrLookup<?> strLookup, StrMatcher strMatcher, StrMatcher strMatcher2, char c) {
        this((StrLookup) strLookup, strMatcher, strMatcher2, c, DEFAULT_VALUE_DELIMITER);
    }

    public StrSubstitutor(StrLookup<?> strLookup, StrMatcher strMatcher, StrMatcher strMatcher2, char c, StrMatcher strMatcher3) {
        this.preserveEscapes = false;
        setVariableResolver(strLookup);
        setVariablePrefixMatcher(strMatcher);
        setVariableSuffixMatcher(strMatcher2);
        setEscapeChar(c);
        setValueDelimiterMatcher(strMatcher3);
    }

    public String replace(String str) {
        if (str == null) {
            return null;
        }
        StrBuilder strBuilder = new StrBuilder(str);
        if (substitute(strBuilder, 0, str.length())) {
            return strBuilder.toString();
        }
        return str;
    }

    public String replace(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        StrBuilder append = new StrBuilder(i2).append(str, i, i2);
        if (substitute(append, 0, i2)) {
            return append.toString();
        }
        return str.substring(i, i2 + i);
    }

    public String replace(char[] cArr) {
        if (cArr == null) {
            return null;
        }
        StrBuilder append = new StrBuilder(cArr.length).append(cArr);
        substitute(append, 0, cArr.length);
        return append.toString();
    }

    public String replace(char[] cArr, int i, int i2) {
        if (cArr == null) {
            return null;
        }
        cArr = new StrBuilder(i2).append(cArr, i, i2);
        substitute(cArr, 0, i2);
        return cArr.toString();
    }

    public String replace(StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            return null;
        }
        stringBuffer = new StrBuilder(stringBuffer.length()).append(stringBuffer);
        substitute(stringBuffer, 0, stringBuffer.length());
        return stringBuffer.toString();
    }

    public String replace(StringBuffer stringBuffer, int i, int i2) {
        if (stringBuffer == null) {
            return null;
        }
        stringBuffer = new StrBuilder(i2).append(stringBuffer, i, i2);
        substitute(stringBuffer, 0, i2);
        return stringBuffer.toString();
    }

    public String replace(CharSequence charSequence) {
        return charSequence == null ? null : replace(charSequence, 0, charSequence.length());
    }

    public String replace(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            return null;
        }
        charSequence = new StrBuilder(i2).append(charSequence, i, i2);
        substitute(charSequence, 0, i2);
        return charSequence.toString();
    }

    public String replace(StrBuilder strBuilder) {
        if (strBuilder == null) {
            return null;
        }
        strBuilder = new StrBuilder(strBuilder.length()).append(strBuilder);
        substitute(strBuilder, 0, strBuilder.length());
        return strBuilder.toString();
    }

    public String replace(StrBuilder strBuilder, int i, int i2) {
        if (strBuilder == null) {
            return null;
        }
        strBuilder = new StrBuilder(i2).append(strBuilder, i, i2);
        substitute(strBuilder, 0, i2);
        return strBuilder.toString();
    }

    public String replace(Object obj) {
        if (obj == null) {
            return null;
        }
        obj = new StrBuilder().append(obj);
        substitute(obj, 0, obj.length());
        return obj.toString();
    }

    public boolean replaceIn(StringBuffer stringBuffer) {
        return stringBuffer == null ? false : replaceIn(stringBuffer, 0, stringBuffer.length());
    }

    public boolean replaceIn(StringBuffer stringBuffer, int i, int i2) {
        if (stringBuffer == null) {
            return false;
        }
        StrBuilder append = new StrBuilder(i2).append(stringBuffer, i, i2);
        if (!substitute(append, 0, i2)) {
            return false;
        }
        stringBuffer.replace(i, i2 + i, append.toString());
        return true;
    }

    public boolean replaceIn(StringBuilder stringBuilder) {
        return stringBuilder == null ? false : replaceIn(stringBuilder, 0, stringBuilder.length());
    }

    public boolean replaceIn(StringBuilder stringBuilder, int i, int i2) {
        if (stringBuilder == null) {
            return false;
        }
        StrBuilder append = new StrBuilder(i2).append(stringBuilder, i, i2);
        if (!substitute(append, 0, i2)) {
            return false;
        }
        stringBuilder.replace(i, i2 + i, append.toString());
        return true;
    }

    public boolean replaceIn(StrBuilder strBuilder) {
        return strBuilder == null ? false : substitute(strBuilder, 0, strBuilder.length());
    }

    public boolean replaceIn(StrBuilder strBuilder, int i, int i2) {
        return strBuilder == null ? null : substitute(strBuilder, i, i2);
    }

    protected boolean substitute(StrBuilder strBuilder, int i, int i2) {
        return substitute(strBuilder, i, i2, null) > null ? true : null;
    }

    private int substitute(StrBuilder strBuilder, int i, int i2, List<String> list) {
        StrSubstitutor strSubstitutor = this;
        StrBuilder strBuilder2 = strBuilder;
        int i3 = i;
        int i4 = i2;
        StrMatcher variablePrefixMatcher = getVariablePrefixMatcher();
        StrMatcher variableSuffixMatcher = getVariableSuffixMatcher();
        char escapeChar = getEscapeChar();
        StrMatcher valueDelimiterMatcher = getValueDelimiterMatcher();
        boolean isEnableSubstitutionInVariables = isEnableSubstitutionInVariables();
        Object obj = list == null ? 1 : null;
        char[] cArr = strBuilder2.buffer;
        int i5 = i3 + i4;
        List<String> list2 = list;
        int i6 = i3;
        int i7 = 0;
        int i8 = 0;
        while (i6 < i5) {
            StrMatcher strMatcher;
            StrMatcher strMatcher2;
            char c;
            int i9;
            int isMatch = variablePrefixMatcher.isMatch(cArr, i6, i3, i5);
            if (isMatch == 0) {
                i6++;
                strMatcher = variablePrefixMatcher;
                strMatcher2 = variableSuffixMatcher;
                c = escapeChar;
                i9 = i7;
            } else {
                int i10;
                if (i6 > i3) {
                    i10 = i6 - 1;
                    if (cArr[i10] == escapeChar) {
                        if (strSubstitutor.preserveEscapes) {
                            i6++;
                        } else {
                            strBuilder2.deleteCharAt(i10);
                            i8--;
                            i5--;
                            strMatcher = variablePrefixMatcher;
                            strMatcher2 = variableSuffixMatcher;
                            c = escapeChar;
                            cArr = strBuilder2.buffer;
                            i9 = 1;
                        }
                    }
                }
                int i11 = i6 + isMatch;
                i10 = i11;
                int i12 = 0;
                while (i10 < i5) {
                    int isMatch2;
                    if (isEnableSubstitutionInVariables) {
                        isMatch2 = variablePrefixMatcher.isMatch(cArr, i10, i3, i5);
                        if (isMatch2 != 0) {
                            i12++;
                            i10 += isMatch2;
                        }
                    }
                    isMatch2 = variableSuffixMatcher.isMatch(cArr, i10, i3, i5);
                    if (isMatch2 == 0) {
                        i10++;
                    } else if (i12 == 0) {
                        int i13;
                        String substring;
                        String substring2;
                        List<String> arrayList;
                        strMatcher2 = variableSuffixMatcher;
                        c = escapeChar;
                        String str = new String(cArr, i11, (i10 - i6) - isMatch);
                        if (isEnableSubstitutionInVariables) {
                            StrBuilder strBuilder3 = new StrBuilder(str);
                            substitute(strBuilder3, 0, strBuilder3.length());
                            str = strBuilder3.toString();
                        }
                        i10 += isMatch2;
                        if (valueDelimiterMatcher != null) {
                            char[] toCharArray = str.toCharArray();
                            i9 = i7;
                            i13 = 0;
                            while (i13 < toCharArray.length) {
                                if (!isEnableSubstitutionInVariables && variablePrefixMatcher.isMatch(toCharArray, i13, i13, toCharArray.length) != 0) {
                                    break;
                                }
                                i7 = valueDelimiterMatcher.isMatch(toCharArray, i13);
                                if (i7 != 0) {
                                    strMatcher = variablePrefixMatcher;
                                    substring = str.substring(0, i13);
                                    substring2 = str.substring(i13 + i7);
                                    break;
                                }
                                i13++;
                                variablePrefixMatcher = variablePrefixMatcher;
                            }
                            strMatcher = variablePrefixMatcher;
                        } else {
                            strMatcher = variablePrefixMatcher;
                            i9 = i7;
                        }
                        substring = str;
                        substring2 = null;
                        if (list2 == null) {
                            arrayList = new ArrayList();
                            arrayList.add(new String(cArr, i3, i4));
                        } else {
                            arrayList = list2;
                        }
                        checkCyclicSubstitution(substring, arrayList);
                        arrayList.add(substring);
                        String resolveVariable = resolveVariable(substring, strBuilder2, i6, i10);
                        if (resolveVariable == null) {
                            resolveVariable = substring2;
                        }
                        if (resolveVariable != null) {
                            i11 = resolveVariable.length();
                            strBuilder2.replace(i6, i10, resolveVariable);
                            i13 = (substitute(strBuilder2, i6, i11, arrayList) + i11) - (i10 - i6);
                            i10 += i13;
                            i5 += i13;
                            i8 += i13;
                            cArr = strBuilder2.buffer;
                            i9 = 1;
                        }
                        arrayList.remove(arrayList.size() - 1);
                        list2 = arrayList;
                        i6 = i10;
                    } else {
                        strMatcher2 = variableSuffixMatcher;
                        c = escapeChar;
                        i12--;
                        i10 += isMatch2;
                        i7 = i7;
                        variablePrefixMatcher = variablePrefixMatcher;
                    }
                }
                strMatcher = variablePrefixMatcher;
                strMatcher2 = variableSuffixMatcher;
                c = escapeChar;
                i9 = i7;
                i6 = i10;
            }
            variableSuffixMatcher = strMatcher2;
            escapeChar = c;
            i7 = i9;
            variablePrefixMatcher = strMatcher;
        }
        return obj != null ? i7 : i8;
    }

    private void checkCyclicSubstitution(String str, List<String> list) {
        if (list.contains(str) != null) {
            str = new StrBuilder(256);
            str.append("Infinite loop in property interpolation of ");
            str.append((String) list.remove(0));
            str.append(": ");
            str.appendWithSeparators((Iterable) list, "->");
            throw new IllegalStateException(str.toString());
        }
    }

    protected String resolveVariable(String str, StrBuilder strBuilder, int i, int i2) {
        strBuilder = getVariableResolver();
        if (strBuilder == null) {
            return null;
        }
        return strBuilder.lookup(str);
    }

    public char getEscapeChar() {
        return this.escapeChar;
    }

    public void setEscapeChar(char c) {
        this.escapeChar = c;
    }

    public StrMatcher getVariablePrefixMatcher() {
        return this.prefixMatcher;
    }

    public StrSubstitutor setVariablePrefixMatcher(StrMatcher strMatcher) {
        if (strMatcher == null) {
            throw new IllegalArgumentException("Variable prefix matcher must not be null!");
        }
        this.prefixMatcher = strMatcher;
        return this;
    }

    public StrSubstitutor setVariablePrefix(char c) {
        return setVariablePrefixMatcher(StrMatcher.charMatcher(c));
    }

    public StrSubstitutor setVariablePrefix(String str) {
        if (str != null) {
            return setVariablePrefixMatcher(StrMatcher.stringMatcher(str));
        }
        throw new IllegalArgumentException("Variable prefix must not be null!");
    }

    public StrMatcher getVariableSuffixMatcher() {
        return this.suffixMatcher;
    }

    public StrSubstitutor setVariableSuffixMatcher(StrMatcher strMatcher) {
        if (strMatcher == null) {
            throw new IllegalArgumentException("Variable suffix matcher must not be null!");
        }
        this.suffixMatcher = strMatcher;
        return this;
    }

    public StrSubstitutor setVariableSuffix(char c) {
        return setVariableSuffixMatcher(StrMatcher.charMatcher(c));
    }

    public StrSubstitutor setVariableSuffix(String str) {
        if (str != null) {
            return setVariableSuffixMatcher(StrMatcher.stringMatcher(str));
        }
        throw new IllegalArgumentException("Variable suffix must not be null!");
    }

    public StrMatcher getValueDelimiterMatcher() {
        return this.valueDelimiterMatcher;
    }

    public StrSubstitutor setValueDelimiterMatcher(StrMatcher strMatcher) {
        this.valueDelimiterMatcher = strMatcher;
        return this;
    }

    public StrSubstitutor setValueDelimiter(char c) {
        return setValueDelimiterMatcher(StrMatcher.charMatcher(c));
    }

    public StrSubstitutor setValueDelimiter(String str) {
        if (!StringUtils.isEmpty(str)) {
            return setValueDelimiterMatcher(StrMatcher.stringMatcher(str));
        }
        setValueDelimiterMatcher(null);
        return this;
    }

    public StrLookup<?> getVariableResolver() {
        return this.variableResolver;
    }

    public void setVariableResolver(StrLookup<?> strLookup) {
        this.variableResolver = strLookup;
    }

    public boolean isEnableSubstitutionInVariables() {
        return this.enableSubstitutionInVariables;
    }

    public void setEnableSubstitutionInVariables(boolean z) {
        this.enableSubstitutionInVariables = z;
    }

    public boolean isPreserveEscapes() {
        return this.preserveEscapes;
    }

    public void setPreserveEscapes(boolean z) {
        this.preserveEscapes = z;
    }
}
