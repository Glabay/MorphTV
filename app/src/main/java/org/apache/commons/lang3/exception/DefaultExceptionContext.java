package org.apache.commons.lang3.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class DefaultExceptionContext implements ExceptionContext, Serializable {
    private static final long serialVersionUID = 20110706;
    private final List<Pair<String, Object>> contextValues = new ArrayList();

    public DefaultExceptionContext addContextValue(String str, Object obj) {
        this.contextValues.add(new ImmutablePair(str, obj));
        return this;
    }

    public DefaultExceptionContext setContextValue(String str, Object obj) {
        Iterator it = this.contextValues.iterator();
        while (it.hasNext()) {
            if (StringUtils.equals(str, (CharSequence) ((Pair) it.next()).getKey())) {
                it.remove();
            }
        }
        addContextValue(str, obj);
        return this;
    }

    public List<Object> getContextValues(String str) {
        List<Object> arrayList = new ArrayList();
        for (Pair pair : this.contextValues) {
            if (StringUtils.equals(str, (CharSequence) pair.getKey())) {
                arrayList.add(pair.getValue());
            }
        }
        return arrayList;
    }

    public Object getFirstContextValue(String str) {
        for (Pair pair : this.contextValues) {
            if (StringUtils.equals(str, (CharSequence) pair.getKey())) {
                return pair.getValue();
            }
        }
        return null;
    }

    public Set<String> getContextLabels() {
        Set<String> hashSet = new HashSet();
        for (Pair key : this.contextValues) {
            hashSet.add(key.getKey());
        }
        return hashSet;
    }

    public List<Pair<String, Object>> getContextEntries() {
        return this.contextValues;
    }

    public String getFormattedExceptionMessage(String str) {
        StringBuilder stringBuilder = new StringBuilder(256);
        if (str != null) {
            stringBuilder.append(str);
        }
        if (this.contextValues.size() > null) {
            if (stringBuilder.length() > null) {
                stringBuilder.append('\n');
            }
            stringBuilder.append("Exception Context:\n");
            str = null;
            for (Pair pair : this.contextValues) {
                stringBuilder.append("\t[");
                str++;
                stringBuilder.append(str);
                stringBuilder.append(':');
                stringBuilder.append((String) pair.getKey());
                stringBuilder.append("=");
                Object value = pair.getValue();
                if (value == null) {
                    stringBuilder.append("null");
                } else {
                    String obj;
                    try {
                        obj = value.toString();
                    } catch (Throwable e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Exception thrown on toString(): ");
                        stringBuilder2.append(ExceptionUtils.getStackTrace(e));
                        obj = stringBuilder2.toString();
                    }
                    stringBuilder.append(obj);
                }
                stringBuilder.append("]\n");
            }
            stringBuilder.append("---------------------------------");
        }
        return stringBuilder.toString();
    }
}
