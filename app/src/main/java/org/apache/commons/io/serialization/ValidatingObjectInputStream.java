package org.apache.commons.io.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidatingObjectInputStream extends ObjectInputStream {
    private final List<ClassNameMatcher> acceptMatchers = new ArrayList();
    private final List<ClassNameMatcher> rejectMatchers = new ArrayList();

    public ValidatingObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    private void validateClassName(String str) throws InvalidClassException {
        for (ClassNameMatcher matches : this.rejectMatchers) {
            if (matches.matches(str)) {
                invalidClassNameFound(str);
            }
        }
        Object obj = null;
        for (ClassNameMatcher matches2 : this.acceptMatchers) {
            if (matches2.matches(str)) {
                obj = 1;
                break;
            }
        }
        if (obj == null) {
            invalidClassNameFound(str);
        }
    }

    protected void invalidClassNameFound(String str) throws InvalidClassException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Class name not accepted: ");
        stringBuilder.append(str);
        throw new InvalidClassException(stringBuilder.toString());
    }

    protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        validateClassName(objectStreamClass.getName());
        return super.resolveClass(objectStreamClass);
    }

    public ValidatingObjectInputStream accept(Class<?>... clsArr) {
        for (Class cls : clsArr) {
            this.acceptMatchers.add(new FullClassNameMatcher(cls.getName()));
        }
        return this;
    }

    public ValidatingObjectInputStream reject(Class<?>... clsArr) {
        for (Class cls : clsArr) {
            this.rejectMatchers.add(new FullClassNameMatcher(cls.getName()));
        }
        return this;
    }

    public ValidatingObjectInputStream accept(String... strArr) {
        for (String wildcardClassNameMatcher : strArr) {
            this.acceptMatchers.add(new WildcardClassNameMatcher(wildcardClassNameMatcher));
        }
        return this;
    }

    public ValidatingObjectInputStream reject(String... strArr) {
        for (String wildcardClassNameMatcher : strArr) {
            this.rejectMatchers.add(new WildcardClassNameMatcher(wildcardClassNameMatcher));
        }
        return this;
    }

    public ValidatingObjectInputStream accept(Pattern pattern) {
        this.acceptMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ValidatingObjectInputStream reject(Pattern pattern) {
        this.rejectMatchers.add(new RegexpClassNameMatcher(pattern));
        return this;
    }

    public ValidatingObjectInputStream accept(ClassNameMatcher classNameMatcher) {
        this.acceptMatchers.add(classNameMatcher);
        return this;
    }

    public ValidatingObjectInputStream reject(ClassNameMatcher classNameMatcher) {
        this.rejectMatchers.add(classNameMatcher);
        return this;
    }
}
