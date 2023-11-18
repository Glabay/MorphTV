package com.google.common.net;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;

final class MediaType$Tokenizer {
    final String input;
    int position = 0;

    MediaType$Tokenizer(String str) {
        this.input = str;
    }

    String consumeTokenIfPresent(CharMatcher charMatcher) {
        Preconditions.checkState(hasMore());
        int i = this.position;
        this.position = charMatcher.negate().indexIn(this.input, i);
        return hasMore() != null ? this.input.substring(i, this.position) : this.input.substring(i);
    }

    String consumeToken(CharMatcher charMatcher) {
        int i = this.position;
        charMatcher = consumeTokenIfPresent(charMatcher);
        Preconditions.checkState(this.position != i);
        return charMatcher;
    }

    char consumeCharacter(CharMatcher charMatcher) {
        Preconditions.checkState(hasMore());
        char previewChar = previewChar();
        Preconditions.checkState(charMatcher.matches(previewChar));
        this.position++;
        return previewChar;
    }

    char consumeCharacter(char c) {
        Preconditions.checkState(hasMore());
        Preconditions.checkState(previewChar() == c);
        this.position++;
        return c;
    }

    char previewChar() {
        Preconditions.checkState(hasMore());
        return this.input.charAt(this.position);
    }

    boolean hasMore() {
        return this.position >= 0 && this.position < this.input.length();
    }
}
