package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public abstract class CharSource {

    private static class CharSequenceCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
        private final CharSequence seq;

        /* renamed from: com.google.common.io.CharSource$CharSequenceCharSource$1 */
        class C11461 implements Iterable<String> {

            /* renamed from: com.google.common.io.CharSource$CharSequenceCharSource$1$1 */
            class C11451 extends AbstractIterator<String> {
                Iterator<String> lines = CharSequenceCharSource.LINE_SPLITTER.split(CharSequenceCharSource.this.seq).iterator();

                C11451() {
                }

                protected String computeNext() {
                    if (this.lines.hasNext()) {
                        String str = (String) this.lines.next();
                        if (this.lines.hasNext() || !str.isEmpty()) {
                            return str;
                        }
                    }
                    return (String) endOfData();
                }
            }

            C11461() {
            }

            public Iterator<String> iterator() {
                return new C11451();
            }
        }

        protected CharSequenceCharSource(CharSequence charSequence) {
            this.seq = (CharSequence) Preconditions.checkNotNull(charSequence);
        }

        public Reader openStream() {
            return new CharSequenceReader(this.seq);
        }

        public String read() {
            return this.seq.toString();
        }

        public boolean isEmpty() {
            return this.seq.length() == 0;
        }

        private Iterable<String> lines() {
            return new C11461();
        }

        public String readFirstLine() {
            Iterator it = lines().iterator();
            return it.hasNext() ? (String) it.next() : null;
        }

        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(lines());
        }

        public <T> T readLines(LineProcessor<T> lineProcessor) throws IOException {
            for (String processLine : lines()) {
                if (!lineProcessor.processLine(processLine)) {
                    break;
                }
            }
            return lineProcessor.getResult();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CharSource.wrap(");
            stringBuilder.append(truncate(this.seq, 30, "..."));
            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        private static String truncate(CharSequence charSequence, int i, String str) {
            Preconditions.checkNotNull(charSequence);
            int length = i - str.length();
            Preconditions.checkArgument(length >= 0, "maxLength (%s) must be >= length of the truncation indicator (%s)", Integer.valueOf(i), Integer.valueOf(str.length()));
            if (charSequence.length() <= i) {
                charSequence = charSequence.toString();
                if (charSequence.length() <= i) {
                    return charSequence;
                }
            }
            StringBuilder stringBuilder = new StringBuilder(i);
            stringBuilder.append(charSequence, 0, length);
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
    }

    private static final class ConcatenatedCharSource extends CharSource {
        private final Iterable<? extends CharSource> sources;

        ConcatenatedCharSource(Iterable<? extends CharSource> iterable) {
            this.sources = (Iterable) Preconditions.checkNotNull(iterable);
        }

        public Reader openStream() throws IOException {
            return new MultiReader(this.sources.iterator());
        }

        public boolean isEmpty() throws IOException {
            for (CharSource isEmpty : this.sources) {
                if (!isEmpty.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CharSource.concat(");
            stringBuilder.append(this.sources);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    private static final class EmptyCharSource extends CharSequenceCharSource {
        private static final EmptyCharSource INSTANCE = new EmptyCharSource();

        public String toString() {
            return "CharSource.empty()";
        }

        private EmptyCharSource() {
            super("");
        }
    }

    public abstract Reader openStream() throws IOException;

    protected CharSource() {
    }

    public BufferedReader openBufferedStream() throws IOException {
        Reader openStream = openStream();
        return openStream instanceof BufferedReader ? (BufferedReader) openStream : new BufferedReader(openStream);
    }

    public long copyTo(Appendable appendable) throws IOException {
        Preconditions.checkNotNull(appendable);
        Closer create = Closer.create();
        try {
            long copy = CharStreams.copy((Reader) create.register(openStream()), appendable);
            create.close();
            return copy;
        } catch (Throwable th) {
            create.close();
        }
    }

    public long copyTo(CharSink charSink) throws IOException {
        Preconditions.checkNotNull(charSink);
        Closer create = Closer.create();
        try {
            long copy = CharStreams.copy((Reader) create.register(openStream()), (Writer) create.register(charSink.openStream()));
            create.close();
            return copy;
        } catch (Throwable th) {
            create.close();
        }
    }

    public String read() throws IOException {
        Closer create = Closer.create();
        try {
            String charStreams = CharStreams.toString((Reader) create.register(openStream()));
            create.close();
            return charStreams;
        } catch (Throwable th) {
            create.close();
        }
    }

    @Nullable
    public String readFirstLine() throws IOException {
        Closer create = Closer.create();
        try {
            String readLine = ((BufferedReader) create.register(openBufferedStream())).readLine();
            create.close();
            return readLine;
        } catch (Throwable th) {
            create.close();
        }
    }

    public ImmutableList<String> readLines() throws IOException {
        Closer create = Closer.create();
        try {
            BufferedReader bufferedReader = (BufferedReader) create.register(openBufferedStream());
            Collection newArrayList = Lists.newArrayList();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    newArrayList.add(readLine);
                } else {
                    ImmutableList<String> copyOf = ImmutableList.copyOf(newArrayList);
                    create.close();
                    return copyOf;
                }
            }
        } catch (Throwable th) {
            create.close();
        }
    }

    @Beta
    public <T> T readLines(LineProcessor<T> lineProcessor) throws IOException {
        Preconditions.checkNotNull(lineProcessor);
        Closer create = Closer.create();
        try {
            lineProcessor = CharStreams.readLines((Reader) create.register(openStream()), lineProcessor);
            create.close();
            return lineProcessor;
        } catch (Throwable th) {
            create.close();
        }
    }

    public boolean isEmpty() throws IOException {
        Closer create = Closer.create();
        try {
            boolean z = ((Reader) create.register(openStream())).read() == -1;
            create.close();
            return z;
        } catch (Throwable th) {
            create.close();
        }
    }

    public static CharSource concat(Iterable<? extends CharSource> iterable) {
        return new ConcatenatedCharSource(iterable);
    }

    public static CharSource concat(Iterator<? extends CharSource> it) {
        return concat(ImmutableList.copyOf((Iterator) it));
    }

    public static CharSource concat(CharSource... charSourceArr) {
        return concat(ImmutableList.copyOf((Object[]) charSourceArr));
    }

    public static CharSource wrap(CharSequence charSequence) {
        return new CharSequenceCharSource(charSequence);
    }

    public static CharSource empty() {
        return EmptyCharSource.INSTANCE;
    }
}
