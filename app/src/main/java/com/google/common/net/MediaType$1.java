package com.google.common.net;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMultiset;
import java.util.Collection;

class MediaType$1 implements Function<Collection<String>, ImmutableMultiset<String>> {
    final /* synthetic */ MediaType this$0;

    MediaType$1(MediaType mediaType) {
        this.this$0 = mediaType;
    }

    public ImmutableMultiset<String> apply(Collection<String> collection) {
        return ImmutableMultiset.copyOf(collection);
    }
}
