package org.jsoup.parser;

import java.util.Arrays;
import kotlin.text.Typography;
import org.apache.commons.lang3.CharUtils;
import org.jsoup.helper.Validate;

final class Tokeniser {
    private static final char[] notCharRefCharsSorted = new char[]{'\t', '\n', CharUtils.CR, '\f', ' ', Typography.less, Typography.amp};
    static final char replacementChar = '�';
    Character charPending = new Character();
    private StringBuilder charsBuilder = new StringBuilder(1024);
    private String charsString = null;
    private final int[] codepointHolder = new int[1];
    Comment commentPending = new Comment();
    StringBuilder dataBuffer = new StringBuilder(1024);
    Doctype doctypePending = new Doctype();
    private Token emitPending;
    EndTag endPending = new EndTag();
    private final ParseErrorList errors;
    private boolean isEmitPending = false;
    private String lastStartTag;
    private final int[] multipointHolder = new int[2];
    private final CharacterReader reader;
    private boolean selfClosingFlagAcknowledged = true;
    StartTag startPending = new StartTag();
    private TokeniserState state = TokeniserState.Data;
    Tag tagPending;

    boolean currentNodeInHtmlNS() {
        return true;
    }

    static {
        Arrays.sort(notCharRefCharsSorted);
    }

    Tokeniser(CharacterReader characterReader, ParseErrorList parseErrorList) {
        this.reader = characterReader;
        this.errors = parseErrorList;
    }

    Token read() {
        if (!this.selfClosingFlagAcknowledged) {
            error("Self closing flag not acknowledged");
            this.selfClosingFlagAcknowledged = true;
        }
        while (!this.isEmitPending) {
            this.state.read(this, this.reader);
        }
        if (this.charsBuilder.length() > 0) {
            String stringBuilder = this.charsBuilder.toString();
            this.charsBuilder.delete(0, this.charsBuilder.length());
            this.charsString = null;
            return this.charPending.data(stringBuilder);
        } else if (this.charsString != null) {
            Token data = this.charPending.data(this.charsString);
            this.charsString = null;
            return data;
        } else {
            this.isEmitPending = false;
            return this.emitPending;
        }
    }

    void emit(Token token) {
        Validate.isFalse(this.isEmitPending, "There is an unread token pending!");
        this.emitPending = token;
        this.isEmitPending = true;
        if (token.type == TokenType.StartTag) {
            StartTag startTag = (StartTag) token;
            this.lastStartTag = startTag.tagName;
            if (startTag.selfClosing != null) {
                this.selfClosingFlagAcknowledged = null;
            }
        } else if (token.type == TokenType.EndTag && ((EndTag) token).attributes != null) {
            error("Attributes incorrectly present on end tag");
        }
    }

    void emit(String str) {
        if (this.charsString == null) {
            this.charsString = str;
            return;
        }
        if (this.charsBuilder.length() == 0) {
            this.charsBuilder.append(this.charsString);
        }
        this.charsBuilder.append(str);
    }

    void emit(char[] cArr) {
        emit(String.valueOf(cArr));
    }

    void emit(int[] iArr) {
        emit(new String(iArr, 0, iArr.length));
    }

    void emit(char c) {
        emit(String.valueOf(c));
    }

    TokeniserState getState() {
        return this.state;
    }

    void transition(TokeniserState tokeniserState) {
        this.state = tokeniserState;
    }

    void advanceTransition(TokeniserState tokeniserState) {
        this.reader.advance();
        this.state = tokeniserState;
    }

    void acknowledgeSelfClosingFlag() {
        this.selfClosingFlagAcknowledged = true;
    }

    int[] consumeCharacterReference(java.lang.Character r7, boolean r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
*/
        /*
        r6 = this;
        r0 = r6.reader;
        r0 = r0.isEmpty();
        r1 = 0;
        if (r0 == 0) goto L_0x000a;
    L_0x0009:
        return r1;
    L_0x000a:
        if (r7 == 0) goto L_0x0019;
    L_0x000c:
        r7 = r7.charValue();
        r0 = r6.reader;
        r0 = r0.current();
        if (r7 != r0) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r7 = r6.reader;
        r0 = notCharRefCharsSorted;
        r7 = r7.matchesAnySorted(r0);
        if (r7 == 0) goto L_0x0024;
    L_0x0023:
        return r1;
    L_0x0024:
        r7 = r6.codepointHolder;
        r0 = r6.reader;
        r0.mark();
        r0 = r6.reader;
        r2 = "#";
        r0 = r0.matchConsume(r2);
        r2 = 0;
        if (r0 == 0) goto L_0x009f;
    L_0x0036:
        r8 = r6.reader;
        r0 = "X";
        r8 = r8.matchConsumeIgnoreCase(r0);
        if (r8 == 0) goto L_0x0047;
    L_0x0040:
        r0 = r6.reader;
        r0 = r0.consumeHexSequence();
        goto L_0x004d;
    L_0x0047:
        r0 = r6.reader;
        r0 = r0.consumeDigitSequence();
    L_0x004d:
        r3 = r0.length();
        if (r3 != 0) goto L_0x005e;
    L_0x0053:
        r7 = "numeric reference with no numerals";
        r6.characterReferenceError(r7);
        r7 = r6.reader;
        r7.rewindToMark();
        return r1;
    L_0x005e:
        r1 = r6.reader;
        r3 = ";";
        r1 = r1.matchConsume(r3);
        if (r1 != 0) goto L_0x006d;
    L_0x0068:
        r1 = "missing semicolon";
        r6.characterReferenceError(r1);
    L_0x006d:
        if (r8 == 0) goto L_0x0072;
    L_0x006f:
        r8 = 16;
        goto L_0x0074;
    L_0x0072:
        r8 = 10;
    L_0x0074:
        r1 = -1;
        r8 = java.lang.Integer.valueOf(r0, r8);	 Catch:{ NumberFormatException -> 0x007e }
        r8 = r8.intValue();	 Catch:{ NumberFormatException -> 0x007e }
        goto L_0x007f;
    L_0x007e:
        r8 = -1;
    L_0x007f:
        if (r8 == r1) goto L_0x0094;
    L_0x0081:
        r0 = 55296; // 0xd800 float:7.7486E-41 double:2.732E-319;
        if (r8 < r0) goto L_0x008b;
    L_0x0086:
        r0 = 57343; // 0xdfff float:8.0355E-41 double:2.8331E-319;
        if (r8 <= r0) goto L_0x0094;
    L_0x008b:
        r0 = 1114111; // 0x10ffff float:1.561202E-39 double:5.50444E-318;
        if (r8 <= r0) goto L_0x0091;
    L_0x0090:
        goto L_0x0094;
    L_0x0091:
        r7[r2] = r8;
        return r7;
    L_0x0094:
        r8 = "character outside of valid range";
        r6.characterReferenceError(r8);
        r8 = 65533; // 0xfffd float:9.1831E-41 double:3.23776E-319;
        r7[r2] = r8;
        return r7;
    L_0x009f:
        r0 = r6.reader;
        r0 = r0.consumeLetterThenDigitSequence();
        r3 = r6.reader;
        r4 = 59;
        r3 = r3.matches(r4);
        r4 = org.jsoup.nodes.Entities.isBaseNamedEntity(r0);
        r5 = 1;
        if (r4 != 0) goto L_0x00bf;
    L_0x00b4:
        r4 = org.jsoup.nodes.Entities.isNamedEntity(r0);
        if (r4 == 0) goto L_0x00bd;
    L_0x00ba:
        if (r3 == 0) goto L_0x00bd;
    L_0x00bc:
        goto L_0x00bf;
    L_0x00bd:
        r4 = 0;
        goto L_0x00c0;
    L_0x00bf:
        r4 = 1;
    L_0x00c0:
        if (r4 != 0) goto L_0x00d7;
    L_0x00c2:
        r7 = r6.reader;
        r7.rewindToMark();
        if (r3 == 0) goto L_0x00d6;
    L_0x00c9:
        r7 = "invalid named referenece '%s'";
        r8 = new java.lang.Object[r5];
        r8[r2] = r0;
        r7 = java.lang.String.format(r7, r8);
        r6.characterReferenceError(r7);
    L_0x00d6:
        return r1;
    L_0x00d7:
        if (r8 == 0) goto L_0x00fd;
    L_0x00d9:
        r8 = r6.reader;
        r8 = r8.matchesLetter();
        if (r8 != 0) goto L_0x00f7;
    L_0x00e1:
        r8 = r6.reader;
        r8 = r8.matchesDigit();
        if (r8 != 0) goto L_0x00f7;
    L_0x00e9:
        r8 = r6.reader;
        r3 = 3;
        r3 = new char[r3];
        r3 = {61, 45, 95};
        r8 = r8.matchesAny(r3);
        if (r8 == 0) goto L_0x00fd;
    L_0x00f7:
        r7 = r6.reader;
        r7.rewindToMark();
        return r1;
    L_0x00fd:
        r8 = r6.reader;
        r1 = ";";
        r8 = r8.matchConsume(r1);
        if (r8 != 0) goto L_0x010c;
    L_0x0107:
        r8 = "missing semicolon";
        r6.characterReferenceError(r8);
    L_0x010c:
        r8 = r6.multipointHolder;
        r8 = org.jsoup.nodes.Entities.codepointsForName(r0, r8);
        if (r8 != r5) goto L_0x011b;
    L_0x0114:
        r8 = r6.multipointHolder;
        r8 = r8[r2];
        r7[r2] = r8;
        return r7;
    L_0x011b:
        r7 = 2;
        if (r8 != r7) goto L_0x0121;
    L_0x011e:
        r7 = r6.multipointHolder;
        return r7;
    L_0x0121:
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Unexpected characters returned for ";
        r7.append(r8);
        r7.append(r0);
        r7 = r7.toString();
        org.jsoup.helper.Validate.fail(r7);
        r7 = r6.multipointHolder;
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.Tokeniser.consumeCharacterReference(java.lang.Character, boolean):int[]");
    }

    Tag createTagPending(boolean z) {
        this.tagPending = z ? this.startPending.reset() : this.endPending.reset();
        return this.tagPending;
    }

    void emitTagPending() {
        this.tagPending.finaliseTag();
        emit(this.tagPending);
    }

    void createCommentPending() {
        this.commentPending.reset();
    }

    void emitCommentPending() {
        emit(this.commentPending);
    }

    void createDoctypePending() {
        this.doctypePending.reset();
    }

    void emitDoctypePending() {
        emit(this.doctypePending);
    }

    void createTempBuffer() {
        Token.reset(this.dataBuffer);
    }

    boolean isAppropriateEndTagToken() {
        return this.lastStartTag != null && this.tagPending.name().equalsIgnoreCase(this.lastStartTag);
    }

    String appropriateEndTagName() {
        if (this.lastStartTag == null) {
            return null;
        }
        return this.lastStartTag;
    }

    void error(TokeniserState tokeniserState) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpected character '%s' in input state [%s]", Character.valueOf(this.reader.current()), tokeniserState));
        }
    }

    void eofError(TokeniserState tokeniserState) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Unexpectedly reached end of file (EOF) in input state [%s]", tokeniserState));
        }
    }

    private void characterReferenceError(String str) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), "Invalid character reference: %s", str));
        }
    }

    private void error(String str) {
        if (this.errors.canAddError()) {
            this.errors.add(new ParseError(this.reader.pos(), str));
        }
    }

    String unescapeEntities(boolean z) {
        StringBuilder stringBuilder = new StringBuilder();
        while (!this.reader.isEmpty()) {
            stringBuilder.append(this.reader.consumeTo((char) Typography.amp));
            if (this.reader.matches((char) Typography.amp)) {
                this.reader.consume();
                int[] consumeCharacterReference = consumeCharacterReference(null, z);
                if (consumeCharacterReference != null) {
                    if (consumeCharacterReference.length != 0) {
                        stringBuilder.appendCodePoint(consumeCharacterReference[0]);
                        if (consumeCharacterReference.length == 2) {
                            stringBuilder.appendCodePoint(consumeCharacterReference[1]);
                        }
                    }
                }
                stringBuilder.append(Typography.amp);
            }
        }
        return stringBuilder.toString();
    }
}
