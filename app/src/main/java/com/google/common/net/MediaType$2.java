package com.google.common.net;

import com.google.common.base.Function;

class MediaType$2 implements Function<String, String> {
    final /* synthetic */ MediaType this$0;

    MediaType$2(MediaType mediaType) {
        this.this$0 = mediaType;
    }

    public String apply(String str) {
        return MediaType.access$000().matchesAllOf(str) ? str : MediaType.access$100(str);
    }
}
