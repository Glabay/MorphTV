package org.jsoup.parser;

import java.util.Arrays;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharUtils;

enum TokeniserState {
    Data {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit(characterReader.consume());
            } else if (current == Typography.amp) {
                tokeniser.advanceTransition(CharacterReferenceInData);
            } else if (current == Typography.less) {
                tokeniser.advanceTransition(TagOpen);
            } else if (current != TokeniserState.eof) {
                tokeniser.emit(characterReader.consumeData());
            } else {
                tokeniser.emit(new EOF());
            }
        }
    },
    CharacterReferenceInData {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readCharRef(tokeniser, Data);
        }
    },
    Rcdata {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                characterReader.advance();
                tokeniser.emit((char) TokeniserState.replacementChar);
            } else if (current == Typography.amp) {
                tokeniser.advanceTransition(CharacterReferenceInRcdata);
            } else if (current == Typography.less) {
                tokeniser.advanceTransition(RcdataLessthanSign);
            } else if (current != TokeniserState.eof) {
                tokeniser.emit(characterReader.consumeToAny(Typography.amp, Typography.less, '\u0000'));
            } else {
                tokeniser.emit(new EOF());
            }
        }
    },
    CharacterReferenceInRcdata {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readCharRef(tokeniser, Rcdata);
        }
    },
    Rawtext {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readData(tokeniser, characterReader, this, RawtextLessthanSign);
        }
    },
    ScriptData {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readData(tokeniser, characterReader, this, ScriptDataLessthanSign);
        }
    },
    PLAINTEXT {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                characterReader.advance();
                tokeniser.emit((char) TokeniserState.replacementChar);
            } else if (current != TokeniserState.eof) {
                tokeniser.emit(characterReader.consumeTo('\u0000'));
            } else {
                tokeniser.emit(new EOF());
            }
        }
    },
    TagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '!') {
                tokeniser.advanceTransition(MarkupDeclarationOpen);
            } else if (current == IOUtils.DIR_SEPARATOR_UNIX) {
                tokeniser.advanceTransition(EndTagOpen);
            } else if (current == '?') {
                tokeniser.advanceTransition(BogusComment);
            } else if (characterReader.matchesLetter() != null) {
                tokeniser.createTagPending(true);
                tokeniser.transition(TagName);
            } else {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit((char) Typography.less);
                tokeniser.transition(Data);
            }
        }
    },
    EndTagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.isEmpty()) {
                tokeniser.eofError(this);
                tokeniser.emit("</");
                tokeniser.transition(Data);
            } else if (characterReader.matchesLetter()) {
                tokeniser.createTagPending(null);
                tokeniser.transition(TagName);
            } else if (characterReader.matches((char) Typography.greater) != null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.advanceTransition(Data);
            } else {
                tokeniser.error((TokeniserState) this);
                tokeniser.advanceTransition(BogusComment);
            }
        }
    },
    TagName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            tokeniser.tagPending.appendTagName(characterReader.consumeTagName());
            switch (characterReader.consume()) {
                case null:
                    tokeniser.tagPending.appendTagName(TokeniserState.replacementStr);
                    return;
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BeforeAttributeName);
                    return;
                case 47:
                    tokeniser.transition(SelfClosingStartTag);
                    return;
                case 62:
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    RcdataLessthanSign {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matches((char) IOUtils.DIR_SEPARATOR_UNIX)) {
                tokeniser.createTempBuffer();
                tokeniser.advanceTransition(RCDATAEndTagOpen);
                return;
            }
            if (characterReader.matchesLetter() && tokeniser.appropriateEndTagName() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("</");
                stringBuilder.append(tokeniser.appropriateEndTagName());
                if (!characterReader.containsIgnoreCase(stringBuilder.toString())) {
                    tokeniser.tagPending = tokeniser.createTagPending(false).name(tokeniser.appropriateEndTagName());
                    tokeniser.emitTagPending();
                    characterReader.unconsume();
                    tokeniser.transition(Data);
                    return;
                }
            }
            tokeniser.emit("<");
            tokeniser.transition(Rcdata);
        }
    },
    RCDATAEndTagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                tokeniser.createTagPending(false);
                tokeniser.tagPending.appendTagName(characterReader.current());
                tokeniser.dataBuffer.append(characterReader.current());
                tokeniser.advanceTransition(RCDATAEndTagName);
                return;
            }
            tokeniser.emit("</");
            tokeniser.transition(Rcdata);
        }
    },
    RCDATAEndTagName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                String consumeLetterSequence = characterReader.consumeLetterSequence();
                tokeniser.tagPending.appendTagName(consumeLetterSequence);
                tokeniser.dataBuffer.append(consumeLetterSequence);
                return;
            }
            switch (characterReader.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    if (!tokeniser.isAppropriateEndTagToken()) {
                        anythingElse(tokeniser, characterReader);
                        break;
                    } else {
                        tokeniser.transition(BeforeAttributeName);
                        break;
                    }
                case '/':
                    if (!tokeniser.isAppropriateEndTagToken()) {
                        anythingElse(tokeniser, characterReader);
                        break;
                    } else {
                        tokeniser.transition(SelfClosingStartTag);
                        break;
                    }
                case '>':
                    if (!tokeniser.isAppropriateEndTagToken()) {
                        anythingElse(tokeniser, characterReader);
                        break;
                    }
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    break;
                default:
                    anythingElse(tokeniser, characterReader);
                    break;
            }
        }

        private void anythingElse(Tokeniser tokeniser, CharacterReader characterReader) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("</");
            stringBuilder.append(tokeniser.dataBuffer.toString());
            tokeniser.emit(stringBuilder.toString());
            characterReader.unconsume();
            tokeniser.transition(Rcdata);
        }
    },
    RawtextLessthanSign {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matches((char) IOUtils.DIR_SEPARATOR_UNIX) != null) {
                tokeniser.createTempBuffer();
                tokeniser.advanceTransition(RawtextEndTagOpen);
                return;
            }
            tokeniser.emit((char) Typography.less);
            tokeniser.transition(Rawtext);
        }
    },
    RawtextEndTagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readEndTag(tokeniser, characterReader, RawtextEndTagName, Rawtext);
        }
    },
    RawtextEndTagName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.handleDataEndTag(tokeniser, characterReader, Rawtext);
        }
    },
    ScriptDataLessthanSign {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            if (consume == '!') {
                tokeniser.emit("<!");
                tokeniser.transition(ScriptDataEscapeStart);
            } else if (consume != IOUtils.DIR_SEPARATOR_UNIX) {
                tokeniser.emit("<");
                characterReader.unconsume();
                tokeniser.transition(ScriptData);
            } else {
                tokeniser.createTempBuffer();
                tokeniser.transition(ScriptDataEndTagOpen);
            }
        }
    },
    ScriptDataEndTagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.readEndTag(tokeniser, characterReader, ScriptDataEndTagName, ScriptData);
        }
    },
    ScriptDataEndTagName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.handleDataEndTag(tokeniser, characterReader, ScriptData);
        }
    },
    ScriptDataEscapeStart {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matches('-') != null) {
                tokeniser.emit('-');
                tokeniser.advanceTransition(ScriptDataEscapeStartDash);
                return;
            }
            tokeniser.transition(ScriptData);
        }
    },
    ScriptDataEscapeStartDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matches('-') != null) {
                tokeniser.emit('-');
                tokeniser.advanceTransition(ScriptDataEscapedDashDash);
                return;
            }
            tokeniser.transition(ScriptData);
        }
    },
    ScriptDataEscaped {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.isEmpty()) {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
                return;
            }
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                characterReader.advance();
                tokeniser.emit((char) TokeniserState.replacementChar);
            } else if (current == '-') {
                tokeniser.emit('-');
                tokeniser.advanceTransition(ScriptDataEscapedDash);
            } else if (current != Typography.less) {
                tokeniser.emit(characterReader.consumeToAny('-', Typography.less, '\u0000'));
            } else {
                tokeniser.advanceTransition(ScriptDataEscapedLessthanSign);
            }
        }
    },
    ScriptDataEscapedDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.isEmpty()) {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
                return;
            }
            char consume = characterReader.consume();
            if (consume == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit((char) TokeniserState.replacementChar);
                tokeniser.transition(ScriptDataEscaped);
            } else if (consume == '-') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataEscapedDashDash);
            } else if (consume != '<') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataEscaped);
            } else {
                tokeniser.transition(ScriptDataEscapedLessthanSign);
            }
        }
    },
    ScriptDataEscapedDashDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.isEmpty()) {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
                return;
            }
            char consume = characterReader.consume();
            if (consume == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit((char) TokeniserState.replacementChar);
                tokeniser.transition(ScriptDataEscaped);
            } else if (consume == '-') {
                tokeniser.emit(consume);
            } else if (consume == '<') {
                tokeniser.transition(ScriptDataEscapedLessthanSign);
            } else if (consume != '>') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataEscaped);
            } else {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptData);
            }
        }
    },
    ScriptDataEscapedLessthanSign {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                tokeniser.createTempBuffer();
                tokeniser.dataBuffer.append(characterReader.current());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<");
                stringBuilder.append(characterReader.current());
                tokeniser.emit(stringBuilder.toString());
                tokeniser.advanceTransition(ScriptDataDoubleEscapeStart);
            } else if (characterReader.matches((char) IOUtils.DIR_SEPARATOR_UNIX) != null) {
                tokeniser.createTempBuffer();
                tokeniser.advanceTransition(ScriptDataEscapedEndTagOpen);
            } else {
                tokeniser.emit((char) Typography.less);
                tokeniser.transition(ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedEndTagOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                tokeniser.createTagPending(false);
                tokeniser.tagPending.appendTagName(characterReader.current());
                tokeniser.dataBuffer.append(characterReader.current());
                tokeniser.advanceTransition(ScriptDataEscapedEndTagName);
                return;
            }
            tokeniser.emit("</");
            tokeniser.transition(ScriptDataEscaped);
        }
    },
    ScriptDataEscapedEndTagName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.handleDataEndTag(tokeniser, characterReader, ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscapeStart {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.handleDataDoubleEscapeTag(tokeniser, characterReader, ScriptDataDoubleEscaped, ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscaped {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                characterReader.advance();
                tokeniser.emit((char) TokeniserState.replacementChar);
            } else if (current == '-') {
                tokeniser.emit(current);
                tokeniser.advanceTransition(ScriptDataDoubleEscapedDash);
            } else if (current == Typography.less) {
                tokeniser.emit(current);
                tokeniser.advanceTransition(ScriptDataDoubleEscapedLessthanSign);
            } else if (current != TokeniserState.eof) {
                tokeniser.emit(characterReader.consumeToAny('-', Typography.less, '\u0000'));
            } else {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    ScriptDataDoubleEscapedDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            if (consume == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit((char) TokeniserState.replacementChar);
                tokeniser.transition(ScriptDataDoubleEscaped);
            } else if (consume == '-') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataDoubleEscapedDashDash);
            } else if (consume == '<') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataDoubleEscapedLessthanSign);
            } else if (consume != '￿') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataDoubleEscaped);
            } else {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    ScriptDataDoubleEscapedDashDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            if (consume == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emit((char) TokeniserState.replacementChar);
                tokeniser.transition(ScriptDataDoubleEscaped);
            } else if (consume == '-') {
                tokeniser.emit(consume);
            } else if (consume == '<') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataDoubleEscapedLessthanSign);
            } else if (consume == '>') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptData);
            } else if (consume != '￿') {
                tokeniser.emit(consume);
                tokeniser.transition(ScriptDataDoubleEscaped);
            } else {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    ScriptDataDoubleEscapedLessthanSign {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matches((char) IOUtils.DIR_SEPARATOR_UNIX) != null) {
                tokeniser.emit((char) IOUtils.DIR_SEPARATOR_UNIX);
                tokeniser.createTempBuffer();
                tokeniser.advanceTransition(ScriptDataDoubleEscapeEnd);
                return;
            }
            tokeniser.transition(ScriptDataDoubleEscaped);
        }
    },
    ScriptDataDoubleEscapeEnd {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            TokeniserState.handleDataDoubleEscapeTag(tokeniser, characterReader, ScriptDataEscaped, ScriptDataDoubleEscaped);
        }
    },
    BeforeAttributeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            switch (consume) {
                case '\u0000':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.newAttribute();
                    characterReader.unconsume();
                    tokeniser.transition(AttributeName);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.newAttribute();
                    tokeniser.tagPending.appendAttributeName(consume);
                    tokeniser.transition(AttributeName);
                    return;
                case '/':
                    tokeniser.transition(SelfClosingStartTag);
                    return;
                case '>':
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.tagPending.newAttribute();
                    characterReader.unconsume();
                    tokeniser.transition(AttributeName);
                    return;
            }
        }
    },
    AttributeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            tokeniser.tagPending.appendAttributeName(characterReader.consumeToAnySorted(TokeniserState.attributeNameCharsSorted));
            char consume = characterReader.consume();
            switch (consume) {
                case '\u0000':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    tokeniser.transition(AfterAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeName(consume);
                    return;
                case '/':
                    tokeniser.transition(SelfClosingStartTag);
                    return;
                case '=':
                    tokeniser.transition(BeforeAttributeValue);
                    return;
                case '>':
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            switch (consume) {
                case '\u0000':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeName((char) TokeniserState.replacementChar);
                    tokeniser.transition(AttributeName);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                case '\'':
                case '<':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.newAttribute();
                    tokeniser.tagPending.appendAttributeName(consume);
                    tokeniser.transition(AttributeName);
                    return;
                case '/':
                    tokeniser.transition(SelfClosingStartTag);
                    return;
                case '=':
                    tokeniser.transition(BeforeAttributeValue);
                    return;
                case '>':
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.tagPending.newAttribute();
                    characterReader.unconsume();
                    tokeniser.transition(AttributeName);
                    return;
            }
        }
    },
    BeforeAttributeValue {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            switch (consume) {
                case '\u0000':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    tokeniser.transition(AttributeValue_unquoted);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    return;
                case '\"':
                    tokeniser.transition(AttributeValue_doubleQuoted);
                    return;
                case '&':
                    characterReader.unconsume();
                    tokeniser.transition(AttributeValue_unquoted);
                    return;
                case '\'':
                    tokeniser.transition(AttributeValue_singleQuoted);
                    return;
                case '<':
                case '=':
                case '`':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeValue(consume);
                    tokeniser.transition(AttributeValue_unquoted);
                    return;
                case '>':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                default:
                    characterReader.unconsume();
                    tokeniser.transition(AttributeValue_unquoted);
                    return;
            }
        }
    },
    AttributeValue_doubleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            String consumeToAny = characterReader.consumeToAny(TokeniserState.attributeDoubleValueCharsSorted);
            if (consumeToAny.length() > 0) {
                tokeniser.tagPending.appendAttributeValue(consumeToAny);
            } else {
                tokeniser.tagPending.setEmptyAttributeValue();
            }
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
            } else if (characterReader == 34) {
                tokeniser.transition(AfterAttributeValue_quoted);
            } else if (characterReader == 38) {
                int[] consumeCharacterReference = tokeniser.consumeCharacterReference(Character.valueOf(Typography.quote), true);
                if (consumeCharacterReference != null) {
                    tokeniser.tagPending.appendAttributeValue(consumeCharacterReference);
                } else {
                    tokeniser.tagPending.appendAttributeValue((char) Typography.amp);
                }
            } else if (characterReader == 65535) {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    AttributeValue_singleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            String consumeToAny = characterReader.consumeToAny(TokeniserState.attributeSingleValueCharsSorted);
            if (consumeToAny.length() > 0) {
                tokeniser.tagPending.appendAttributeValue(consumeToAny);
            } else {
                tokeniser.tagPending.setEmptyAttributeValue();
            }
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
            } else if (characterReader != 65535) {
                switch (characterReader) {
                    case 38:
                        int[] consumeCharacterReference = tokeniser.consumeCharacterReference(Character.valueOf('\''), true);
                        if (consumeCharacterReference != null) {
                            tokeniser.tagPending.appendAttributeValue(consumeCharacterReference);
                            return;
                        } else {
                            tokeniser.tagPending.appendAttributeValue((char) Typography.amp);
                            return;
                        }
                    case 39:
                        tokeniser.transition(AfterAttributeValue_quoted);
                        return;
                    default:
                        return;
                }
            } else {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    AttributeValue_unquoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            String consumeToAnySorted = characterReader.consumeToAnySorted(TokeniserState.attributeValueUnquoted);
            if (consumeToAnySorted.length() > 0) {
                tokeniser.tagPending.appendAttributeValue(consumeToAnySorted);
            }
            char consume = characterReader.consume();
            switch (consume) {
                case '\u0000':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeValue((char) TokeniserState.replacementChar);
                    return;
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    tokeniser.transition(BeforeAttributeName);
                    return;
                case '\"':
                case '\'':
                case '<':
                case '=':
                case '`':
                    tokeniser.error((TokeniserState) this);
                    tokeniser.tagPending.appendAttributeValue(consume);
                    return;
                case '&':
                    int[] consumeCharacterReference = tokeniser.consumeCharacterReference(Character.valueOf(Typography.greater), true);
                    if (consumeCharacterReference != null) {
                        tokeniser.tagPending.appendAttributeValue(consumeCharacterReference);
                        return;
                    } else {
                        tokeniser.tagPending.appendAttributeValue((char) Typography.amp);
                        return;
                    }
                case '>':
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    return;
            }
        }
    },
    AfterAttributeValue_quoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case '\t':
                case '\n':
                case '\f':
                case '\r':
                case ' ':
                    tokeniser.transition(BeforeAttributeName);
                    return;
                case '/':
                    tokeniser.transition(SelfClosingStartTag);
                    return;
                case '>':
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    return;
                case '￿':
                    tokeniser.eofError(this);
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    characterReader.unconsume();
                    tokeniser.transition(BeforeAttributeName);
                    return;
            }
        }
    },
    SelfClosingStartTag {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char consume = characterReader.consume();
            if (consume == Typography.greater) {
                tokeniser.tagPending.selfClosing = true;
                tokeniser.emitTagPending();
                tokeniser.transition(Data);
            } else if (consume != TokeniserState.eof) {
                tokeniser.error((TokeniserState) this);
                characterReader.unconsume();
                tokeniser.transition(BeforeAttributeName);
            } else {
                tokeniser.eofError(this);
                tokeniser.transition(Data);
            }
        }
    },
    BogusComment {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader.unconsume();
            Token comment = new Comment();
            comment.bogus = true;
            comment.data.append(characterReader.consumeTo((char) Typography.greater));
            tokeniser.emit(comment);
            tokeniser.advanceTransition(Data);
        }
    },
    MarkupDeclarationOpen {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchConsume("--")) {
                tokeniser.createCommentPending();
                tokeniser.transition(CommentStart);
            } else if (characterReader.matchConsumeIgnoreCase("DOCTYPE")) {
                tokeniser.transition(Doctype);
            } else if (characterReader.matchConsume("[CDATA[") != null) {
                tokeniser.transition(CdataSection);
            } else {
                tokeniser.error((TokeniserState) this);
                tokeniser.advanceTransition(BogusComment);
            }
        }
    },
    CommentStart {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.commentPending.data.append(TokeniserState.replacementChar);
                tokeniser.transition(Comment);
            } else if (characterReader == 45) {
                tokeniser.transition(CommentStartDash);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.commentPending.data.append(characterReader);
                tokeniser.transition(Comment);
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    CommentStartDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.commentPending.data.append(TokeniserState.replacementChar);
                tokeniser.transition(Comment);
            } else if (characterReader == 45) {
                tokeniser.transition(CommentStartDash);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.commentPending.data.append(characterReader);
                tokeniser.transition(Comment);
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    Comment {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            char current = characterReader.current();
            if (current == '\u0000') {
                tokeniser.error((TokeniserState) this);
                characterReader.advance();
                tokeniser.commentPending.data.append(TokeniserState.replacementChar);
            } else if (current == '-') {
                tokeniser.advanceTransition(CommentEndDash);
            } else if (current != TokeniserState.eof) {
                tokeniser.commentPending.data.append(characterReader.consumeToAny('-', '\u0000'));
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    CommentEndDash {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                characterReader = tokeniser.commentPending.data;
                characterReader.append('-');
                characterReader.append(TokeniserState.replacementChar);
                tokeniser.transition(Comment);
            } else if (characterReader == 45) {
                tokeniser.transition(CommentEnd);
            } else if (characterReader != 65535) {
                StringBuilder stringBuilder = tokeniser.commentPending.data;
                stringBuilder.append('-');
                stringBuilder.append(characterReader);
                tokeniser.transition(Comment);
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    CommentEnd {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                characterReader = tokeniser.commentPending.data;
                characterReader.append("--");
                characterReader.append(TokeniserState.replacementChar);
                tokeniser.transition(Comment);
            } else if (characterReader == 33) {
                tokeniser.error((TokeniserState) this);
                tokeniser.transition(CommentEndBang);
            } else if (characterReader == 45) {
                tokeniser.error((TokeniserState) this);
                tokeniser.commentPending.data.append('-');
            } else if (characterReader == 62) {
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.error((TokeniserState) this);
                StringBuilder stringBuilder = tokeniser.commentPending.data;
                stringBuilder.append("--");
                stringBuilder.append(characterReader);
                tokeniser.transition(Comment);
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    CommentEndBang {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                characterReader = tokeniser.commentPending.data;
                characterReader.append("--!");
                characterReader.append(TokeniserState.replacementChar);
                tokeniser.transition(Comment);
            } else if (characterReader == 45) {
                tokeniser.commentPending.data.append("--!");
                tokeniser.transition(CommentEndDash);
            } else if (characterReader == 62) {
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                StringBuilder stringBuilder = tokeniser.commentPending.data;
                stringBuilder.append("--!");
                stringBuilder.append(characterReader);
                tokeniser.transition(Comment);
            } else {
                tokeniser.eofError(this);
                tokeniser.emitCommentPending();
                tokeniser.transition(Data);
            }
        }
    },
    Doctype {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BeforeDoctypeName);
                    return;
                case 62:
                    break;
                case 65535:
                    tokeniser.eofError(this);
                    break;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(BeforeDoctypeName);
                    return;
            }
            tokeniser.error((TokeniserState) this);
            tokeniser.createDoctypePending();
            tokeniser.doctypePending.forceQuirks = true;
            tokeniser.emitDoctypePending();
            tokeniser.transition(Data);
        }
    },
    BeforeDoctypeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                tokeniser.createDoctypePending();
                tokeniser.transition(DoctypeName);
                return;
            }
            characterReader = characterReader.consume();
            switch (characterReader) {
                case null:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.createDoctypePending();
                    tokeniser.doctypePending.name.append(TokeniserState.replacementChar);
                    tokeniser.transition(DoctypeName);
                    break;
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    break;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.createDoctypePending();
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    break;
                default:
                    tokeniser.createDoctypePending();
                    tokeniser.doctypePending.name.append(characterReader);
                    tokeniser.transition(DoctypeName);
                    break;
            }
        }
    },
    DoctypeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.matchesLetter()) {
                tokeniser.doctypePending.name.append(characterReader.consumeLetterSequence());
                return;
            }
            characterReader = characterReader.consume();
            switch (characterReader) {
                case null:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.name.append(TokeniserState.replacementChar);
                    break;
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(AfterDoctypeName);
                    break;
                case 62:
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    break;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    break;
                default:
                    tokeniser.doctypePending.name.append(characterReader);
                    break;
            }
        }
    },
    AfterDoctypeName {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            if (characterReader.isEmpty()) {
                tokeniser.eofError(this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
                return;
            }
            if (characterReader.matchesAny('\t', '\n', CharUtils.CR, '\f', ' ')) {
                characterReader.advance();
            } else if (characterReader.matches((char) Typography.greater)) {
                tokeniser.emitDoctypePending();
                tokeniser.advanceTransition(Data);
            } else if (characterReader.matchConsumeIgnoreCase("PUBLIC")) {
                tokeniser.transition(AfterDoctypePublicKeyword);
            } else if (characterReader.matchConsumeIgnoreCase("SYSTEM") != null) {
                tokeniser.transition(AfterDoctypeSystemKeyword);
            } else {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.advanceTransition(BogusDoctype);
            }
        }
    },
    AfterDoctypePublicKeyword {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BeforeDoctypePublicIdentifier);
                    return;
                case 34:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    BeforeDoctypePublicIdentifier {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    return;
                case 34:
                    tokeniser.transition(DoctypePublicIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.transition(DoctypePublicIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypePublicIdentifier_doubleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
            } else if (characterReader == 34) {
                tokeniser.transition(AfterDoctypePublicIdentifier);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.doctypePending.publicIdentifier.append(characterReader);
            } else {
                tokeniser.eofError(this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            }
        }
    },
    DoctypePublicIdentifier_singleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
            } else if (characterReader == 39) {
                tokeniser.transition(AfterDoctypePublicIdentifier);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.doctypePending.publicIdentifier.append(characterReader);
            } else {
                tokeniser.eofError(this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            }
        }
    },
    AfterDoctypePublicIdentifier {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BetweenDoctypePublicAndSystemIdentifiers);
                    return;
                case 34:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    BetweenDoctypePublicAndSystemIdentifiers {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    return;
                case 34:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    AfterDoctypeSystemKeyword {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BeforeDoctypeSystemIdentifier);
                    return;
                case 34:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    return;
            }
        }
    },
    BeforeDoctypeSystemIdentifier {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    return;
                case 34:
                    tokeniser.transition(DoctypeSystemIdentifier_doubleQuoted);
                    return;
                case 39:
                    tokeniser.transition(DoctypeSystemIdentifier_singleQuoted);
                    return;
                case 62:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    DoctypeSystemIdentifier_doubleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
            } else if (characterReader == 34) {
                tokeniser.transition(AfterDoctypeSystemIdentifier);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.doctypePending.systemIdentifier.append(characterReader);
            } else {
                tokeniser.eofError(this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            }
        }
    },
    DoctypeSystemIdentifier_singleQuoted {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == null) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
            } else if (characterReader == 39) {
                tokeniser.transition(AfterDoctypeSystemIdentifier);
            } else if (characterReader == 62) {
                tokeniser.error((TokeniserState) this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            } else if (characterReader != 65535) {
                tokeniser.doctypePending.systemIdentifier.append(characterReader);
            } else {
                tokeniser.eofError(this);
                tokeniser.doctypePending.forceQuirks = true;
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            }
        }
    },
    AfterDoctypeSystemIdentifier {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            switch (characterReader.consume()) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    return;
                case 62:
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                case 65535:
                    tokeniser.eofError(this);
                    tokeniser.doctypePending.forceQuirks = true;
                    tokeniser.emitDoctypePending();
                    tokeniser.transition(Data);
                    return;
                default:
                    tokeniser.error((TokeniserState) this);
                    tokeniser.transition(BogusDoctype);
                    return;
            }
        }
    },
    BogusDoctype {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            characterReader = characterReader.consume();
            if (characterReader == 62) {
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            } else if (characterReader == 65535) {
                tokeniser.emitDoctypePending();
                tokeniser.transition(Data);
            }
        }
    },
    CdataSection {
        void read(Tokeniser tokeniser, CharacterReader characterReader) {
            tokeniser.emit(characterReader.consumeTo("]]>"));
            characterReader.matchConsume("]]>");
            tokeniser.transition(Data);
        }
    };
    
    private static final char[] attributeDoubleValueCharsSorted = null;
    private static final char[] attributeNameCharsSorted = null;
    private static final char[] attributeSingleValueCharsSorted = null;
    private static final char[] attributeValueUnquoted = null;
    private static final char eof = '￿';
    static final char nullChar = '\u0000';
    private static final char replacementChar = '�';
    private static final String replacementStr = null;

    abstract void read(Tokeniser tokeniser, CharacterReader characterReader);

    static {
        attributeSingleValueCharsSorted = new char[]{'\'', Typography.amp, '\u0000'};
        attributeDoubleValueCharsSorted = new char[]{Typography.quote, Typography.amp, '\u0000'};
        attributeNameCharsSorted = new char[]{'\t', '\n', CharUtils.CR, '\f', ' ', IOUtils.DIR_SEPARATOR_UNIX, '=', Typography.greater, '\u0000', Typography.quote, '\'', Typography.less};
        attributeValueUnquoted = new char[]{'\t', '\n', CharUtils.CR, '\f', ' ', Typography.amp, Typography.greater, '\u0000', Typography.quote, '\'', Typography.less, '=', '`'};
        replacementStr = String.valueOf(replacementChar);
        Arrays.sort(attributeSingleValueCharsSorted);
        Arrays.sort(attributeDoubleValueCharsSorted);
        Arrays.sort(attributeNameCharsSorted);
        Arrays.sort(attributeValueUnquoted);
    }

    private static void handleDataEndTag(Tokeniser tokeniser, CharacterReader characterReader, TokeniserState tokeniserState) {
        if (characterReader.matchesLetter()) {
            String consumeLetterSequence = characterReader.consumeLetterSequence();
            tokeniser.tagPending.appendTagName(consumeLetterSequence);
            tokeniser.dataBuffer.append(consumeLetterSequence);
            return;
        }
        Object obj = 1;
        if (tokeniser.isAppropriateEndTagToken() && !characterReader.isEmpty()) {
            characterReader = characterReader.consume();
            switch (characterReader) {
                case 9:
                case 10:
                case 12:
                case 13:
                case 32:
                    tokeniser.transition(BeforeAttributeName);
                    break;
                case 47:
                    tokeniser.transition(SelfClosingStartTag);
                    break;
                case 62:
                    tokeniser.emitTagPending();
                    tokeniser.transition(Data);
                    break;
                default:
                    tokeniser.dataBuffer.append(characterReader);
                    break;
            }
            obj = null;
        }
        if (obj != null) {
            characterReader = new StringBuilder();
            characterReader.append("</");
            characterReader.append(tokeniser.dataBuffer.toString());
            tokeniser.emit(characterReader.toString());
            tokeniser.transition(tokeniserState);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void readData(org.jsoup.parser.Tokeniser r1, org.jsoup.parser.CharacterReader r2, org.jsoup.parser.TokeniserState r3, org.jsoup.parser.TokeniserState r4) {
        /*
        r0 = r2.current();
        if (r0 == 0) goto L_0x002a;
    L_0x0006:
        r3 = 60;
        if (r0 == r3) goto L_0x0026;
    L_0x000a:
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r0 == r3) goto L_0x001d;
    L_0x000f:
        r3 = 2;
        r3 = new char[r3];
        r3 = {60, 0};
        r2 = r2.consumeToAny(r3);
        r1.emit(r2);
        goto L_0x0036;
    L_0x001d:
        r2 = new org.jsoup.parser.Token$EOF;
        r2.<init>();
        r1.emit(r2);
        goto L_0x0036;
    L_0x0026:
        r1.advanceTransition(r4);
        goto L_0x0036;
    L_0x002a:
        r1.error(r3);
        r2.advance();
        r2 = 65533; // 0xfffd float:9.1831E-41 double:3.23776E-319;
        r1.emit(r2);
    L_0x0036:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.TokeniserState.readData(org.jsoup.parser.Tokeniser, org.jsoup.parser.CharacterReader, org.jsoup.parser.TokeniserState, org.jsoup.parser.TokeniserState):void");
    }

    private static void readCharRef(Tokeniser tokeniser, TokeniserState tokeniserState) {
        int[] consumeCharacterReference = tokeniser.consumeCharacterReference(null, false);
        if (consumeCharacterReference == null) {
            tokeniser.emit((char) Typography.amp);
        } else {
            tokeniser.emit(consumeCharacterReference);
        }
        tokeniser.transition(tokeniserState);
    }

    private static void readEndTag(Tokeniser tokeniser, CharacterReader characterReader, TokeniserState tokeniserState, TokeniserState tokeniserState2) {
        if (characterReader.matchesLetter() != null) {
            tokeniser.createTagPending(null);
            tokeniser.transition(tokeniserState);
            return;
        }
        tokeniser.emit("</");
        tokeniser.transition(tokeniserState2);
    }

    private static void handleDataDoubleEscapeTag(Tokeniser tokeniser, CharacterReader characterReader, TokeniserState tokeniserState, TokeniserState tokeniserState2) {
        if (characterReader.matchesLetter()) {
            String consumeLetterSequence = characterReader.consumeLetterSequence();
            tokeniser.dataBuffer.append(consumeLetterSequence);
            tokeniser.emit(consumeLetterSequence);
            return;
        }
        char consume = characterReader.consume();
        switch (consume) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case '/':
            case '>':
                if (tokeniser.dataBuffer.toString().equals("script") != null) {
                    tokeniser.transition(tokeniserState);
                } else {
                    tokeniser.transition(tokeniserState2);
                }
                tokeniser.emit(consume);
                break;
            default:
                characterReader.unconsume();
                tokeniser.transition(tokeniserState2);
                break;
        }
    }
}
