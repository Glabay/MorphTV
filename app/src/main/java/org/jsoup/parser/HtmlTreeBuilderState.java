package org.jsoup.parser;

import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.util.ArrayList;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document.QuirksMode;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

enum HtmlTreeBuilderState {
    Initial {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                return true;
            }
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                token = token.asDoctype();
                htmlTreeBuilder.getDocument().appendChild(new DocumentType(htmlTreeBuilder.settings.normalizeTag(token.getName()), token.getPublicIdentifier(), token.getSystemIdentifier(), htmlTreeBuilder.getBaseUri()));
                if (token.isForceQuirks() != null) {
                    htmlTreeBuilder.getDocument().quirksMode(QuirksMode.quirks);
                }
                htmlTreeBuilder.transition(BeforeHtml);
            } else {
                htmlTreeBuilder.transition(BeforeHtml);
                return htmlTreeBuilder.process(token);
            }
            return true;
        }
    },
    BeforeHtml {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            }
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (HtmlTreeBuilderState.isWhitespace(token)) {
                return true;
            } else {
                if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                    htmlTreeBuilder.insert(token.asStartTag());
                    htmlTreeBuilder.transition(BeforeHead);
                } else if (token.isEndTag() && StringUtil.in(token.asEndTag().normalName(), TtmlNode.TAG_HEAD, TtmlNode.TAG_BODY, "html", TtmlNode.TAG_BR)) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    if (!token.isEndTag()) {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                }
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.insertStartTag("html");
            htmlTreeBuilder.transition(BeforeHead);
            return htmlTreeBuilder.process(token);
        }
    },
    BeforeHead {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                return true;
            }
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return InBody.process(token, htmlTreeBuilder);
            } else {
                if (token.isStartTag() && token.asStartTag().normalName().equals(TtmlNode.TAG_HEAD)) {
                    htmlTreeBuilder.setHeadElement(htmlTreeBuilder.insert(token.asStartTag()));
                    htmlTreeBuilder.transition(InHead);
                } else if (token.isEndTag() && StringUtil.in(token.asEndTag().normalName(), TtmlNode.TAG_HEAD, TtmlNode.TAG_BODY, "html", TtmlNode.TAG_BR)) {
                    htmlTreeBuilder.processStartTag(TtmlNode.TAG_HEAD);
                    return htmlTreeBuilder.process(token);
                } else if (token.isEndTag()) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    htmlTreeBuilder.processStartTag(TtmlNode.TAG_HEAD);
                    return htmlTreeBuilder.process(token);
                }
            }
            return true;
        }
    },
    InHead {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            }
            switch (token.type) {
                case Comment:
                    htmlTreeBuilder.insert(token.asComment());
                    break;
                case Doctype:
                    htmlTreeBuilder.error(this);
                    return false;
                case StartTag:
                    StartTag asStartTag = token.asStartTag();
                    String normalName = asStartTag.normalName();
                    if (normalName.equals("html")) {
                        return InBody.process(token, htmlTreeBuilder);
                    }
                    if (StringUtil.in(normalName, "base", "basefont", "bgsound", "command", "link")) {
                        token = htmlTreeBuilder.insertEmpty(asStartTag);
                        if (normalName.equals("base") && token.hasAttr("href")) {
                            htmlTreeBuilder.maybeSetBaseUri(token);
                            break;
                        }
                    } else if (normalName.equals("meta")) {
                        htmlTreeBuilder.insertEmpty(asStartTag);
                        break;
                    } else if (normalName.equals("title")) {
                        HtmlTreeBuilderState.handleRcData(asStartTag, htmlTreeBuilder);
                        break;
                    } else if (StringUtil.in(normalName, "noframes", TtmlNode.TAG_STYLE)) {
                        HtmlTreeBuilderState.handleRawtext(asStartTag, htmlTreeBuilder);
                        break;
                    } else if (normalName.equals("noscript")) {
                        htmlTreeBuilder.insert(asStartTag);
                        htmlTreeBuilder.transition(InHeadNoscript);
                        break;
                    } else if (normalName.equals("script")) {
                        htmlTreeBuilder.tokeniser.transition(TokeniserState.ScriptData);
                        htmlTreeBuilder.markInsertionMode();
                        htmlTreeBuilder.transition(Text);
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    } else if (!normalName.equals(TtmlNode.TAG_HEAD)) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                case EndTag:
                    String normalName2 = token.asEndTag().normalName();
                    if (normalName2.equals(TtmlNode.TAG_HEAD)) {
                        htmlTreeBuilder.pop();
                        htmlTreeBuilder.transition(AfterHead);
                        break;
                    } else if (StringUtil.in(normalName2, TtmlNode.TAG_BODY, "html", TtmlNode.TAG_BR)) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                default:
                    return anythingElse(token, htmlTreeBuilder);
            }
            return true;
        }

        private boolean anythingElse(Token token, TreeBuilder treeBuilder) {
            treeBuilder.processEndTag(TtmlNode.TAG_HEAD);
            return treeBuilder.process(token);
        }
    },
    InHeadNoscript {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEndTag() && token.asEndTag().normalName().equals("noscript")) {
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(InHead);
                } else {
                    if (!(HtmlTreeBuilderState.isWhitespace(token) || token.isComment())) {
                        if (!token.isStartTag() || !StringUtil.in(token.asStartTag().normalName(), "basefont", "bgsound", "link", "meta", "noframes", TtmlNode.TAG_STYLE)) {
                            if (token.isEndTag() && token.asEndTag().normalName().equals(TtmlNode.TAG_BR)) {
                                return anythingElse(token, htmlTreeBuilder);
                            }
                            if ((!token.isStartTag() || !StringUtil.in(token.asStartTag().normalName(), TtmlNode.TAG_HEAD, "noscript")) && !token.isEndTag()) {
                                return anythingElse(token, htmlTreeBuilder);
                            }
                            htmlTreeBuilder.error(this);
                            return null;
                        }
                    }
                    return htmlTreeBuilder.process(token, InHead);
                }
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            htmlTreeBuilder.insert(new Character().data(token.toString()));
            return true;
        }
    },
    AfterHead {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
            } else if (token.isStartTag()) {
                StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("html")) {
                    return htmlTreeBuilder.process(token, InBody);
                }
                if (normalName.equals(TtmlNode.TAG_BODY)) {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.framesetOk(false);
                    htmlTreeBuilder.transition(InBody);
                } else if (normalName.equals("frameset")) {
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InFrameset);
                } else if (StringUtil.in(normalName, "base", "basefont", "bgsound", "link", "meta", "noframes", "script", TtmlNode.TAG_STYLE, "title")) {
                    htmlTreeBuilder.error(this);
                    Element headElement = htmlTreeBuilder.getHeadElement();
                    htmlTreeBuilder.push(headElement);
                    htmlTreeBuilder.process(token, InHead);
                    htmlTreeBuilder.removeFromStack(headElement);
                } else if (normalName.equals(TtmlNode.TAG_HEAD)) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else {
                    anythingElse(token, htmlTreeBuilder);
                }
            } else if (!token.isEndTag()) {
                anythingElse(token, htmlTreeBuilder);
            } else if (StringUtil.in(token.asEndTag().normalName(), TtmlNode.TAG_BODY, "html")) {
                anythingElse(token, htmlTreeBuilder);
            } else {
                htmlTreeBuilder.error(this);
                return false;
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.processStartTag(TtmlNode.TAG_BODY);
            htmlTreeBuilder.framesetOk(true);
            return htmlTreeBuilder.process(token);
        }
    },
    InBody {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        boolean process(org.jsoup.parser.Token r18, org.jsoup.parser.HtmlTreeBuilder r19) {
            /*
            r17 = this;
            r0 = r17;
            r1 = r18;
            r2 = r19;
            r3 = org.jsoup.parser.HtmlTreeBuilderState.AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType;
            r4 = r1.type;
            r4 = r4.ordinal();
            r3 = r3[r4];
            r4 = 1;
            r5 = 0;
            switch(r3) {
                case 1: goto L_0x091c;
                case 2: goto L_0x0918;
                case 3: goto L_0x033d;
                case 4: goto L_0x004b;
                case 5: goto L_0x0018;
                default: goto L_0x0015;
            };
        L_0x0015:
            r1 = 1;
            goto L_0x0925;
        L_0x0018:
            r1 = r18.asCharacter();
            r3 = r1.getData();
            r6 = org.jsoup.parser.HtmlTreeBuilderState.nullString;
            r3 = r3.equals(r6);
            if (r3 == 0) goto L_0x002e;
        L_0x002a:
            r2.error(r0);
            return r5;
        L_0x002e:
            r3 = r19.framesetOk();
            if (r3 == 0) goto L_0x0041;
        L_0x0034:
            r3 = org.jsoup.parser.HtmlTreeBuilderState.isWhitespace(r1);
            if (r3 == 0) goto L_0x0041;
        L_0x003a:
            r19.reconstructFormattingElements();
            r2.insert(r1);
            goto L_0x0015;
        L_0x0041:
            r19.reconstructFormattingElements();
            r2.insert(r1);
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x004b:
            r3 = r18.asEndTag();
            r6 = r3.normalName();
            r7 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyEndAdoptionFormatters;
            r7 = org.jsoup.helper.StringUtil.inSorted(r6, r7);
            r8 = 0;
            if (r7 == 0) goto L_0x0184;
        L_0x005e:
            r3 = 0;
        L_0x005f:
            r7 = 8;
            if (r3 >= r7) goto L_0x0015;
        L_0x0063:
            r7 = r2.getActiveFormattingElement(r6);
            if (r7 != 0) goto L_0x006e;
        L_0x0069:
            r1 = r17.anyOtherEndTag(r18, r19);
            return r1;
        L_0x006e:
            r9 = r2.onStack(r7);
            if (r9 != 0) goto L_0x007b;
        L_0x0074:
            r2.error(r0);
            r2.removeFromActiveFormattingElements(r7);
            return r4;
        L_0x007b:
            r9 = r7.nodeName();
            r9 = r2.inScope(r9);
            if (r9 != 0) goto L_0x0089;
        L_0x0085:
            r2.error(r0);
            return r5;
        L_0x0089:
            r9 = r19.currentElement();
            if (r9 == r7) goto L_0x0092;
        L_0x008f:
            r2.error(r0);
        L_0x0092:
            r9 = r19.getStack();
            r10 = r9.size();
            r13 = r8;
            r11 = 0;
            r12 = 0;
        L_0x009d:
            if (r11 >= r10) goto L_0x00c2;
        L_0x009f:
            r14 = 64;
            if (r11 >= r14) goto L_0x00c2;
        L_0x00a3:
            r14 = r9.get(r11);
            r14 = (org.jsoup.nodes.Element) r14;
            if (r14 != r7) goto L_0x00b6;
        L_0x00ab:
            r12 = r11 + -1;
            r12 = r9.get(r12);
            r12 = (org.jsoup.nodes.Element) r12;
            r13 = r12;
            r12 = 1;
            goto L_0x00bf;
        L_0x00b6:
            if (r12 == 0) goto L_0x00bf;
        L_0x00b8:
            r15 = r2.isSpecial(r14);
            if (r15 == 0) goto L_0x00bf;
        L_0x00be:
            goto L_0x00c3;
        L_0x00bf:
            r11 = r11 + 1;
            goto L_0x009d;
        L_0x00c2:
            r14 = r8;
        L_0x00c3:
            if (r14 != 0) goto L_0x00d0;
        L_0x00c5:
            r1 = r7.nodeName();
            r2.popStackToClose(r1);
            r2.removeFromActiveFormattingElements(r7);
            return r4;
        L_0x00d0:
            r10 = r14;
            r11 = r10;
            r9 = 0;
        L_0x00d3:
            r12 = 3;
            if (r9 >= r12) goto L_0x0118;
        L_0x00d6:
            r12 = r2.onStack(r10);
            if (r12 == 0) goto L_0x00e0;
        L_0x00dc:
            r10 = r2.aboveOnStack(r10);
        L_0x00e0:
            r12 = r2.isInActiveFormattingElements(r10);
            if (r12 != 0) goto L_0x00ea;
        L_0x00e6:
            r2.removeFromStack(r10);
            goto L_0x0114;
        L_0x00ea:
            if (r10 != r7) goto L_0x00ed;
        L_0x00ec:
            goto L_0x0118;
        L_0x00ed:
            r12 = new org.jsoup.nodes.Element;
            r15 = r10.nodeName();
            r4 = org.jsoup.parser.ParseSettings.preserveCase;
            r4 = org.jsoup.parser.Tag.valueOf(r15, r4);
            r15 = r19.getBaseUri();
            r12.<init>(r4, r15);
            r2.replaceActiveFormattingElement(r10, r12);
            r2.replaceOnStack(r10, r12);
            r4 = r11.parent();
            if (r4 == 0) goto L_0x010f;
        L_0x010c:
            r11.remove();
        L_0x010f:
            r12.appendChild(r11);
            r10 = r12;
            r11 = r10;
        L_0x0114:
            r9 = r9 + 1;
            r4 = 1;
            goto L_0x00d3;
        L_0x0118:
            r4 = r13.nodeName();
            r9 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyEndTableFosters;
            r4 = org.jsoup.helper.StringUtil.inSorted(r4, r9);
            if (r4 == 0) goto L_0x0133;
        L_0x0126:
            r4 = r11.parent();
            if (r4 == 0) goto L_0x012f;
        L_0x012c:
            r11.remove();
        L_0x012f:
            r2.insertInFosterParent(r11);
            goto L_0x013f;
        L_0x0133:
            r4 = r11.parent();
            if (r4 == 0) goto L_0x013c;
        L_0x0139:
            r11.remove();
        L_0x013c:
            r13.appendChild(r11);
        L_0x013f:
            r4 = new org.jsoup.nodes.Element;
            r9 = r7.tag();
            r10 = r19.getBaseUri();
            r4.<init>(r9, r10);
            r9 = r4.attributes();
            r10 = r7.attributes();
            r9.addAll(r10);
            r9 = r14.childNodes();
            r10 = r14.childNodeSize();
            r10 = new org.jsoup.nodes.Node[r10];
            r9 = r9.toArray(r10);
            r9 = (org.jsoup.nodes.Node[]) r9;
            r10 = r9.length;
            r11 = 0;
        L_0x0169:
            if (r11 >= r10) goto L_0x0173;
        L_0x016b:
            r12 = r9[r11];
            r4.appendChild(r12);
            r11 = r11 + 1;
            goto L_0x0169;
        L_0x0173:
            r14.appendChild(r4);
            r2.removeFromActiveFormattingElements(r7);
            r2.removeFromStack(r7);
            r2.insertOnStackAfter(r14, r4);
            r3 = r3 + 1;
            r4 = 1;
            goto L_0x005f;
        L_0x0184:
            r4 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyEndClosers;
            r4 = org.jsoup.helper.StringUtil.inSorted(r6, r4);
            if (r4 == 0) goto L_0x01b1;
        L_0x018e:
            r1 = r2.inScope(r6);
            if (r1 != 0) goto L_0x0198;
        L_0x0194:
            r2.error(r0);
            return r5;
        L_0x0198:
            r19.generateImpliedEndTags();
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x01ac;
        L_0x01a9:
            r2.error(r0);
        L_0x01ac:
            r2.popStackToClose(r6);
            goto L_0x0015;
        L_0x01b1:
            r4 = "span";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x01be;
        L_0x01b9:
            r1 = r17.anyOtherEndTag(r18, r19);
            return r1;
        L_0x01be:
            r4 = "li";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x01e9;
        L_0x01c6:
            r1 = r2.inListItemScope(r6);
            if (r1 != 0) goto L_0x01d0;
        L_0x01cc:
            r2.error(r0);
            return r5;
        L_0x01d0:
            r2.generateImpliedEndTags(r6);
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x01e4;
        L_0x01e1:
            r2.error(r0);
        L_0x01e4:
            r2.popStackToClose(r6);
            goto L_0x0015;
        L_0x01e9:
            r4 = "body";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x0204;
        L_0x01f1:
            r1 = "body";
            r1 = r2.inScope(r1);
            if (r1 != 0) goto L_0x01fd;
        L_0x01f9:
            r2.error(r0);
            return r5;
        L_0x01fd:
            r1 = AfterBody;
            r2.transition(r1);
            goto L_0x0015;
        L_0x0204:
            r4 = "html";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x0219;
        L_0x020c:
            r1 = "body";
            r1 = r2.processEndTag(r1);
            if (r1 == 0) goto L_0x0015;
        L_0x0214:
            r1 = r2.process(r3);
            return r1;
        L_0x0219:
            r4 = "form";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x024e;
        L_0x0221:
            r1 = r19.getFormElement();
            r2.setFormElement(r8);
            if (r1 == 0) goto L_0x024a;
        L_0x022a:
            r3 = r2.inScope(r6);
            if (r3 != 0) goto L_0x0231;
        L_0x0230:
            goto L_0x024a;
        L_0x0231:
            r19.generateImpliedEndTags();
            r3 = r19.currentElement();
            r3 = r3.nodeName();
            r3 = r3.equals(r6);
            if (r3 != 0) goto L_0x0245;
        L_0x0242:
            r2.error(r0);
        L_0x0245:
            r2.removeFromStack(r1);
            goto L_0x0015;
        L_0x024a:
            r2.error(r0);
            return r5;
        L_0x024e:
            r4 = "p";
            r4 = r6.equals(r4);
            if (r4 == 0) goto L_0x0280;
        L_0x0256:
            r1 = r2.inButtonScope(r6);
            if (r1 != 0) goto L_0x0267;
        L_0x025c:
            r2.error(r0);
            r2.processStartTag(r6);
            r1 = r2.process(r3);
            return r1;
        L_0x0267:
            r2.generateImpliedEndTags(r6);
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x027b;
        L_0x0278:
            r2.error(r0);
        L_0x027b:
            r2.popStackToClose(r6);
            goto L_0x0015;
        L_0x0280:
            r3 = org.jsoup.parser.HtmlTreeBuilderState.Constants.DdDt;
            r3 = org.jsoup.helper.StringUtil.inSorted(r6, r3);
            if (r3 == 0) goto L_0x02ad;
        L_0x028a:
            r1 = r2.inScope(r6);
            if (r1 != 0) goto L_0x0294;
        L_0x0290:
            r2.error(r0);
            return r5;
        L_0x0294:
            r2.generateImpliedEndTags(r6);
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x02a8;
        L_0x02a5:
            r2.error(r0);
        L_0x02a8:
            r2.popStackToClose(r6);
            goto L_0x0015;
        L_0x02ad:
            r3 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Headings;
            r3 = org.jsoup.helper.StringUtil.inSorted(r6, r3);
            if (r3 == 0) goto L_0x02e2;
        L_0x02b7:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Headings;
            r1 = r2.inScope(r1);
            if (r1 != 0) goto L_0x02c5;
        L_0x02c1:
            r2.error(r0);
            return r5;
        L_0x02c5:
            r2.generateImpliedEndTags(r6);
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x02d9;
        L_0x02d6:
            r2.error(r0);
        L_0x02d9:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Headings;
            r2.popStackToClose(r1);
            goto L_0x0015;
        L_0x02e2:
            r3 = "sarcasm";
            r3 = r6.equals(r3);
            if (r3 == 0) goto L_0x02ef;
        L_0x02ea:
            r1 = r17.anyOtherEndTag(r18, r19);
            return r1;
        L_0x02ef:
            r3 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartApplets;
            r3 = org.jsoup.helper.StringUtil.inSorted(r6, r3);
            if (r3 == 0) goto L_0x0327;
        L_0x02f9:
            r1 = "name";
            r1 = r2.inScope(r1);
            if (r1 != 0) goto L_0x0015;
        L_0x0301:
            r1 = r2.inScope(r6);
            if (r1 != 0) goto L_0x030b;
        L_0x0307:
            r2.error(r0);
            return r5;
        L_0x030b:
            r19.generateImpliedEndTags();
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r1 = r1.equals(r6);
            if (r1 != 0) goto L_0x031f;
        L_0x031c:
            r2.error(r0);
        L_0x031f:
            r2.popStackToClose(r6);
            r19.clearFormattingElementsToLastMarker();
            goto L_0x0015;
        L_0x0327:
            r3 = "br";
            r3 = r6.equals(r3);
            if (r3 == 0) goto L_0x0338;
        L_0x032f:
            r2.error(r0);
            r1 = "br";
            r2.processStartTag(r1);
            return r5;
        L_0x0338:
            r1 = r17.anyOtherEndTag(r18, r19);
            return r1;
        L_0x033d:
            r3 = r18.asStartTag();
            r4 = r3.normalName();
            r6 = "a";
            r6 = r4.equals(r6);
            if (r6 == 0) goto L_0x0377;
        L_0x034d:
            r1 = "a";
            r1 = r2.getActiveFormattingElement(r1);
            if (r1 == 0) goto L_0x036b;
        L_0x0355:
            r2.error(r0);
            r1 = "a";
            r2.processEndTag(r1);
            r1 = "a";
            r1 = r2.getFromStack(r1);
            if (r1 == 0) goto L_0x036b;
        L_0x0365:
            r2.removeFromActiveFormattingElements(r1);
            r2.removeFromStack(r1);
        L_0x036b:
            r19.reconstructFormattingElements();
            r1 = r2.insert(r3);
            r2.pushActiveFormattingElements(r1);
            goto L_0x0015;
        L_0x0377:
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartEmptyFormatters;
            r6 = org.jsoup.helper.StringUtil.inSorted(r4, r6);
            if (r6 == 0) goto L_0x038c;
        L_0x0381:
            r19.reconstructFormattingElements();
            r2.insertEmpty(r3);
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x038c:
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartPClosers;
            r6 = org.jsoup.helper.StringUtil.inSorted(r4, r6);
            if (r6 == 0) goto L_0x03a8;
        L_0x0396:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x03a3;
        L_0x039e:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x03a3:
            r2.insert(r3);
            goto L_0x0015;
        L_0x03a8:
            r6 = "span";
            r6 = r4.equals(r6);
            if (r6 == 0) goto L_0x03b8;
        L_0x03b0:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            goto L_0x0015;
        L_0x03b8:
            r6 = "li";
            r6 = r4.equals(r6);
            if (r6 == 0) goto L_0x0411;
        L_0x03c0:
            r2.framesetOk(r5);
            r1 = r19.getStack();
            r4 = r1.size();
            r5 = 1;
            r4 = r4 - r5;
        L_0x03cd:
            if (r4 <= 0) goto L_0x03ff;
        L_0x03cf:
            r5 = r1.get(r4);
            r5 = (org.jsoup.nodes.Element) r5;
            r6 = r5.nodeName();
            r7 = "li";
            r6 = r6.equals(r7);
            if (r6 == 0) goto L_0x03e7;
        L_0x03e1:
            r1 = "li";
            r2.processEndTag(r1);
            goto L_0x03ff;
        L_0x03e7:
            r6 = r2.isSpecial(r5);
            if (r6 == 0) goto L_0x03fc;
        L_0x03ed:
            r5 = r5.nodeName();
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartLiBreakers;
            r5 = org.jsoup.helper.StringUtil.inSorted(r5, r6);
            if (r5 != 0) goto L_0x03fc;
        L_0x03fb:
            goto L_0x03ff;
        L_0x03fc:
            r4 = r4 + -1;
            goto L_0x03cd;
        L_0x03ff:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x040c;
        L_0x0407:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x040c:
            r2.insert(r3);
            goto L_0x0015;
        L_0x0411:
            r6 = "html";
            r6 = r4.equals(r6);
            if (r6 == 0) goto L_0x044c;
        L_0x0419:
            r2.error(r0);
            r1 = r19.getStack();
            r1 = r1.get(r5);
            r1 = (org.jsoup.nodes.Element) r1;
            r2 = r3.getAttributes();
            r2 = r2.iterator();
        L_0x042e:
            r3 = r2.hasNext();
            if (r3 == 0) goto L_0x0015;
        L_0x0434:
            r3 = r2.next();
            r3 = (org.jsoup.nodes.Attribute) r3;
            r4 = r3.getKey();
            r4 = r1.hasAttr(r4);
            if (r4 != 0) goto L_0x042e;
        L_0x0444:
            r4 = r1.attributes();
            r4.put(r3);
            goto L_0x042e;
        L_0x044c:
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartToHead;
            r6 = org.jsoup.helper.StringUtil.inSorted(r4, r6);
            if (r6 == 0) goto L_0x045d;
        L_0x0456:
            r3 = InHead;
            r1 = r2.process(r1, r3);
            return r1;
        L_0x045d:
            r1 = "body";
            r1 = r4.equals(r1);
            r6 = 2;
            if (r1 == 0) goto L_0x04bd;
        L_0x0466:
            r2.error(r0);
            r1 = r19.getStack();
            r4 = r1.size();
            r7 = 1;
            if (r4 == r7) goto L_0x04bc;
        L_0x0474:
            r4 = r1.size();
            if (r4 <= r6) goto L_0x048d;
        L_0x047a:
            r4 = r1.get(r7);
            r4 = (org.jsoup.nodes.Element) r4;
            r4 = r4.nodeName();
            r6 = "body";
            r4 = r4.equals(r6);
            if (r4 != 0) goto L_0x048d;
        L_0x048c:
            goto L_0x04bc;
        L_0x048d:
            r2.framesetOk(r5);
            r1 = r1.get(r7);
            r1 = (org.jsoup.nodes.Element) r1;
            r2 = r3.getAttributes();
            r2 = r2.iterator();
        L_0x049e:
            r3 = r2.hasNext();
            if (r3 == 0) goto L_0x0015;
        L_0x04a4:
            r3 = r2.next();
            r3 = (org.jsoup.nodes.Attribute) r3;
            r4 = r3.getKey();
            r4 = r1.hasAttr(r4);
            if (r4 != 0) goto L_0x049e;
        L_0x04b4:
            r4 = r1.attributes();
            r4.put(r3);
            goto L_0x049e;
        L_0x04bc:
            return r5;
        L_0x04bd:
            r1 = "frameset";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x051e;
        L_0x04c5:
            r2.error(r0);
            r1 = r19.getStack();
            r4 = r1.size();
            r7 = 1;
            if (r4 == r7) goto L_0x051d;
        L_0x04d3:
            r4 = r1.size();
            if (r4 <= r6) goto L_0x04ec;
        L_0x04d9:
            r4 = r1.get(r7);
            r4 = (org.jsoup.nodes.Element) r4;
            r4 = r4.nodeName();
            r6 = "body";
            r4 = r4.equals(r6);
            if (r4 != 0) goto L_0x04ec;
        L_0x04eb:
            goto L_0x051d;
        L_0x04ec:
            r4 = r19.framesetOk();
            if (r4 != 0) goto L_0x04f3;
        L_0x04f2:
            return r5;
        L_0x04f3:
            r4 = 1;
            r5 = r1.get(r4);
            r5 = (org.jsoup.nodes.Element) r5;
            r6 = r5.parent();
            if (r6 == 0) goto L_0x0503;
        L_0x0500:
            r5.remove();
        L_0x0503:
            r5 = r1.size();
            if (r5 <= r4) goto L_0x0513;
        L_0x0509:
            r5 = r1.size();
            r5 = r5 - r4;
            r1.remove(r5);
            r4 = 1;
            goto L_0x0503;
        L_0x0513:
            r2.insert(r3);
            r1 = InFrameset;
            r2.transition(r1);
            goto L_0x0015;
        L_0x051d:
            return r5;
        L_0x051e:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Headings;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x0552;
        L_0x0528:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x0535;
        L_0x0530:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x0535:
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r4 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Headings;
            r1 = org.jsoup.helper.StringUtil.inSorted(r1, r4);
            if (r1 == 0) goto L_0x054d;
        L_0x0547:
            r2.error(r0);
            r19.pop();
        L_0x054d:
            r2.insert(r3);
            goto L_0x0015;
        L_0x0552:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartPreListing;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x0571;
        L_0x055c:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x0569;
        L_0x0564:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x0569:
            r2.insert(r3);
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x0571:
            r1 = "form";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0596;
        L_0x0579:
            r1 = r19.getFormElement();
            if (r1 == 0) goto L_0x0583;
        L_0x057f:
            r2.error(r0);
            return r5;
        L_0x0583:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x0590;
        L_0x058b:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x0590:
            r1 = 1;
            r2.insertForm(r3, r1);
            goto L_0x0925;
        L_0x0596:
            r1 = 1;
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.DdDt;
            r6 = org.jsoup.helper.StringUtil.inSorted(r4, r6);
            if (r6 == 0) goto L_0x05f5;
        L_0x05a1:
            r2.framesetOk(r5);
            r4 = r19.getStack();
            r5 = r4.size();
            r5 = r5 - r1;
        L_0x05ad:
            if (r5 <= 0) goto L_0x05e3;
        L_0x05af:
            r1 = r4.get(r5);
            r1 = (org.jsoup.nodes.Element) r1;
            r6 = r1.nodeName();
            r7 = org.jsoup.parser.HtmlTreeBuilderState.Constants.DdDt;
            r6 = org.jsoup.helper.StringUtil.inSorted(r6, r7);
            if (r6 == 0) goto L_0x05cb;
        L_0x05c3:
            r1 = r1.nodeName();
            r2.processEndTag(r1);
            goto L_0x05e3;
        L_0x05cb:
            r6 = r2.isSpecial(r1);
            if (r6 == 0) goto L_0x05e0;
        L_0x05d1:
            r1 = r1.nodeName();
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartLiBreakers;
            r1 = org.jsoup.helper.StringUtil.inSorted(r1, r6);
            if (r1 != 0) goto L_0x05e0;
        L_0x05df:
            goto L_0x05e3;
        L_0x05e0:
            r5 = r5 + -1;
            goto L_0x05ad;
        L_0x05e3:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x05f0;
        L_0x05eb:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x05f0:
            r2.insert(r3);
            goto L_0x0015;
        L_0x05f5:
            r1 = "plaintext";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0616;
        L_0x05fd:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x060a;
        L_0x0605:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x060a:
            r2.insert(r3);
            r1 = r2.tokeniser;
            r2 = org.jsoup.parser.TokeniserState.PLAINTEXT;
            r1.transition(r2);
            goto L_0x0015;
        L_0x0616:
            r1 = "button";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x063e;
        L_0x061e:
            r1 = "button";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x0633;
        L_0x0626:
            r2.error(r0);
            r1 = "button";
            r2.processEndTag(r1);
            r2.process(r3);
            goto L_0x0015;
        L_0x0633:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x063e:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.Formatters;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x0654;
        L_0x0648:
            r19.reconstructFormattingElements();
            r1 = r2.insert(r3);
            r2.pushActiveFormattingElements(r1);
            goto L_0x0015;
        L_0x0654:
            r1 = "nobr";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x067b;
        L_0x065c:
            r19.reconstructFormattingElements();
            r1 = "nobr";
            r1 = r2.inScope(r1);
            if (r1 == 0) goto L_0x0672;
        L_0x0667:
            r2.error(r0);
            r1 = "nobr";
            r2.processEndTag(r1);
            r19.reconstructFormattingElements();
        L_0x0672:
            r1 = r2.insert(r3);
            r2.pushActiveFormattingElements(r1);
            goto L_0x0015;
        L_0x067b:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartApplets;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x0693;
        L_0x0685:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            r19.insertMarkerToFormattingElements();
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x0693:
            r1 = "table";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x06c1;
        L_0x069b:
            r1 = r19.getDocument();
            r1 = r1.quirksMode();
            r4 = org.jsoup.nodes.Document.QuirksMode.quirks;
            if (r1 == r4) goto L_0x06b4;
        L_0x06a7:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x06b4;
        L_0x06af:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x06b4:
            r2.insert(r3);
            r2.framesetOk(r5);
            r1 = InTable;
            r2.transition(r1);
            goto L_0x0015;
        L_0x06c1:
            r1 = "input";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x06e3;
        L_0x06c9:
            r19.reconstructFormattingElements();
            r1 = r2.insertEmpty(r3);
            r3 = "type";
            r1 = r1.attr(r3);
            r3 = "hidden";
            r1 = r1.equalsIgnoreCase(r3);
            if (r1 != 0) goto L_0x0015;
        L_0x06de:
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x06e3:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartMedia;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x06f2;
        L_0x06ed:
            r2.insertEmpty(r3);
            goto L_0x0015;
        L_0x06f2:
            r1 = "hr";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x070f;
        L_0x06fa:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x0707;
        L_0x0702:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x0707:
            r2.insertEmpty(r3);
            r2.framesetOk(r5);
            goto L_0x0015;
        L_0x070f:
            r1 = "image";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x072f;
        L_0x0717:
            r1 = "svg";
            r1 = r2.getFromStack(r1);
            if (r1 != 0) goto L_0x072a;
        L_0x071f:
            r1 = "img";
            r1 = r3.name(r1);
            r1 = r2.process(r1);
            return r1;
        L_0x072a:
            r2.insert(r3);
            goto L_0x0015;
        L_0x072f:
            r1 = "isindex";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x07d7;
        L_0x0737:
            r2.error(r0);
            r1 = r19.getFormElement();
            if (r1 == 0) goto L_0x0741;
        L_0x0740:
            return r5;
        L_0x0741:
            r1 = r2.tokeniser;
            r1.acknowledgeSelfClosingFlag();
            r1 = "form";
            r2.processStartTag(r1);
            r1 = r3.attributes;
            r4 = "action";
            r1 = r1.hasKey(r4);
            if (r1 == 0) goto L_0x0766;
        L_0x0755:
            r1 = r19.getFormElement();
            r4 = "action";
            r5 = r3.attributes;
            r6 = "action";
            r5 = r5.get(r6);
            r1.attr(r4, r5);
        L_0x0766:
            r1 = "hr";
            r2.processStartTag(r1);
            r1 = "label";
            r2.processStartTag(r1);
            r1 = r3.attributes;
            r4 = "prompt";
            r1 = r1.hasKey(r4);
            if (r1 == 0) goto L_0x0783;
        L_0x077a:
            r1 = r3.attributes;
            r4 = "prompt";
            r1 = r1.get(r4);
            goto L_0x0785;
        L_0x0783:
            r1 = "This is a searchable index. Enter search keywords: ";
        L_0x0785:
            r4 = new org.jsoup.parser.Token$Character;
            r4.<init>();
            r1 = r4.data(r1);
            r2.process(r1);
            r1 = new org.jsoup.nodes.Attributes;
            r1.<init>();
            r3 = r3.attributes;
            r3 = r3.iterator();
        L_0x079c:
            r4 = r3.hasNext();
            if (r4 == 0) goto L_0x07ba;
        L_0x07a2:
            r4 = r3.next();
            r4 = (org.jsoup.nodes.Attribute) r4;
            r5 = r4.getKey();
            r6 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartInputAttribs;
            r5 = org.jsoup.helper.StringUtil.inSorted(r5, r6);
            if (r5 != 0) goto L_0x079c;
        L_0x07b6:
            r1.put(r4);
            goto L_0x079c;
        L_0x07ba:
            r3 = "name";
            r4 = "isindex";
            r1.put(r3, r4);
            r3 = "input";
            r2.processStartTag(r3, r1);
            r1 = "label";
            r2.processEndTag(r1);
            r1 = "hr";
            r2.processStartTag(r1);
            r1 = "form";
            r2.processEndTag(r1);
            goto L_0x0015;
        L_0x07d7:
            r1 = "textarea";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x07f6;
        L_0x07df:
            r2.insert(r3);
            r1 = r2.tokeniser;
            r3 = org.jsoup.parser.TokeniserState.Rcdata;
            r1.transition(r3);
            r19.markInsertionMode();
            r2.framesetOk(r5);
            r1 = Text;
            r2.transition(r1);
            goto L_0x0015;
        L_0x07f6:
            r1 = "xmp";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0816;
        L_0x07fe:
            r1 = "p";
            r1 = r2.inButtonScope(r1);
            if (r1 == 0) goto L_0x080b;
        L_0x0806:
            r1 = "p";
            r2.processEndTag(r1);
        L_0x080b:
            r19.reconstructFormattingElements();
            r2.framesetOk(r5);
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r3, r2);
            goto L_0x0015;
        L_0x0816:
            r1 = "iframe";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0826;
        L_0x081e:
            r2.framesetOk(r5);
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r3, r2);
            goto L_0x0015;
        L_0x0826:
            r1 = "noembed";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0833;
        L_0x082e:
            org.jsoup.parser.HtmlTreeBuilderState.handleRawtext(r3, r2);
            goto L_0x0015;
        L_0x0833:
            r1 = "select";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x087f;
        L_0x083b:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            r2.framesetOk(r5);
            r1 = r19.state();
            r3 = InTable;
            r3 = r1.equals(r3);
            if (r3 != 0) goto L_0x0878;
        L_0x0850:
            r3 = InCaption;
            r3 = r1.equals(r3);
            if (r3 != 0) goto L_0x0878;
        L_0x0858:
            r3 = InTableBody;
            r3 = r1.equals(r3);
            if (r3 != 0) goto L_0x0878;
        L_0x0860:
            r3 = InRow;
            r3 = r1.equals(r3);
            if (r3 != 0) goto L_0x0878;
        L_0x0868:
            r3 = InCell;
            r1 = r1.equals(r3);
            if (r1 == 0) goto L_0x0871;
        L_0x0870:
            goto L_0x0878;
        L_0x0871:
            r1 = InSelect;
            r2.transition(r1);
            goto L_0x0015;
        L_0x0878:
            r1 = InSelectInTable;
            r2.transition(r1);
            goto L_0x0015;
        L_0x087f:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartOptions;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x08a6;
        L_0x0889:
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r4 = "option";
            r1 = r1.equals(r4);
            if (r1 == 0) goto L_0x089e;
        L_0x0899:
            r1 = "option";
            r2.processEndTag(r1);
        L_0x089e:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            goto L_0x0015;
        L_0x08a6:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartRuby;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x08d8;
        L_0x08b0:
            r1 = "ruby";
            r1 = r2.inScope(r1);
            if (r1 == 0) goto L_0x0015;
        L_0x08b8:
            r19.generateImpliedEndTags();
            r1 = r19.currentElement();
            r1 = r1.nodeName();
            r4 = "ruby";
            r1 = r1.equals(r4);
            if (r1 != 0) goto L_0x08d3;
        L_0x08cb:
            r2.error(r0);
            r1 = "ruby";
            r2.popStackToBefore(r1);
        L_0x08d3:
            r2.insert(r3);
            goto L_0x0015;
        L_0x08d8:
            r1 = "math";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x08ed;
        L_0x08e0:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            r1 = r2.tokeniser;
            r1.acknowledgeSelfClosingFlag();
            goto L_0x0015;
        L_0x08ed:
            r1 = "svg";
            r1 = r4.equals(r1);
            if (r1 == 0) goto L_0x0902;
        L_0x08f5:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            r1 = r2.tokeniser;
            r1.acknowledgeSelfClosingFlag();
            goto L_0x0015;
        L_0x0902:
            r1 = org.jsoup.parser.HtmlTreeBuilderState.Constants.InBodyStartDrop;
            r1 = org.jsoup.helper.StringUtil.inSorted(r4, r1);
            if (r1 == 0) goto L_0x0910;
        L_0x090c:
            r2.error(r0);
            return r5;
        L_0x0910:
            r19.reconstructFormattingElements();
            r2.insert(r3);
            goto L_0x0015;
        L_0x0918:
            r2.error(r0);
            return r5;
        L_0x091c:
            r1 = r18.asComment();
            r2.insert(r1);
            goto L_0x0015;
        L_0x0925:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jsoup.parser.HtmlTreeBuilderState.7.process(org.jsoup.parser.Token, org.jsoup.parser.HtmlTreeBuilder):boolean");
        }

        boolean anyOtherEndTag(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            String normalName = token.asEndTag().normalName();
            ArrayList stack = htmlTreeBuilder.getStack();
            int size = stack.size() - 1;
            while (size >= 0) {
                Element element = (Element) stack.get(size);
                if (element.nodeName().equals(normalName)) {
                    htmlTreeBuilder.generateImpliedEndTags(normalName);
                    if (!normalName.equals(htmlTreeBuilder.currentElement().nodeName())) {
                        htmlTreeBuilder.error(this);
                    }
                    htmlTreeBuilder.popStackToClose(normalName);
                    return true;
                } else if (htmlTreeBuilder.isSpecial(element)) {
                    htmlTreeBuilder.error(this);
                    return null;
                } else {
                    size--;
                }
            }
            return true;
        }
    },
    Text {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isCharacter()) {
                htmlTreeBuilder.insert(token.asCharacter());
            } else if (token.isEOF()) {
                htmlTreeBuilder.error(this);
                htmlTreeBuilder.pop();
                htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
                return htmlTreeBuilder.process(token);
            } else if (token.isEndTag() != null) {
                htmlTreeBuilder.pop();
                htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
            }
            return true;
        }
    },
    InTable {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            HtmlTreeBuilderState htmlTreeBuilderState = this;
            Token token2 = token;
            HtmlTreeBuilder htmlTreeBuilder2 = htmlTreeBuilder;
            if (token.isCharacter()) {
                htmlTreeBuilder.newPendingTableCharacters();
                htmlTreeBuilder.markInsertionMode();
                htmlTreeBuilder2.transition(InTableText);
                return htmlTreeBuilder2.process(token2);
            } else if (token.isComment()) {
                htmlTreeBuilder2.insert(token.asComment());
                return true;
            } else if (token.isDoctype()) {
                htmlTreeBuilder2.error(htmlTreeBuilderState);
                return false;
            } else if (token.isStartTag()) {
                StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("caption")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder.insertMarkerToFormattingElements();
                    htmlTreeBuilder2.insert(asStartTag);
                    htmlTreeBuilder2.transition(InCaption);
                } else if (normalName.equals("colgroup")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder2.insert(asStartTag);
                    htmlTreeBuilder2.transition(InColumnGroup);
                } else if (normalName.equals("col")) {
                    htmlTreeBuilder2.processStartTag("colgroup");
                    return htmlTreeBuilder2.process(token2);
                } else if (StringUtil.in(normalName, "tbody", "tfoot", "thead")) {
                    htmlTreeBuilder.clearStackToTableContext();
                    htmlTreeBuilder2.insert(asStartTag);
                    htmlTreeBuilder2.transition(InTableBody);
                } else if (StringUtil.in(normalName, "td", "th", "tr")) {
                    htmlTreeBuilder2.processStartTag("tbody");
                    return htmlTreeBuilder2.process(token2);
                } else if (normalName.equals("table")) {
                    htmlTreeBuilder2.error(htmlTreeBuilderState);
                    if (htmlTreeBuilder2.processEndTag("table")) {
                        return htmlTreeBuilder2.process(token2);
                    }
                } else if (StringUtil.in(normalName, TtmlNode.TAG_STYLE, "script")) {
                    return htmlTreeBuilder2.process(token2, InHead);
                } else {
                    if (normalName.equals("input")) {
                        if (!asStartTag.attributes.get("type").equalsIgnoreCase("hidden")) {
                            return anythingElse(token, htmlTreeBuilder);
                        }
                        htmlTreeBuilder2.insertEmpty(asStartTag);
                    } else if (!normalName.equals("form")) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder2.error(htmlTreeBuilderState);
                        if (htmlTreeBuilder.getFormElement() != null) {
                            return false;
                        }
                        htmlTreeBuilder2.insertForm(asStartTag, false);
                    }
                }
                return true;
            } else if (token.isEndTag()) {
                String normalName2 = token.asEndTag().normalName();
                if (normalName2.equals("table")) {
                    if (htmlTreeBuilder2.inTableScope(normalName2)) {
                        htmlTreeBuilder2.popStackToClose("table");
                        htmlTreeBuilder.resetInsertionMode();
                        return true;
                    }
                    htmlTreeBuilder2.error(htmlTreeBuilderState);
                    return false;
                } else if (!StringUtil.in(normalName2, TtmlNode.TAG_BODY, "caption", "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    htmlTreeBuilder2.error(htmlTreeBuilderState);
                    return false;
                }
            } else if (!token.isEOF()) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                if (htmlTreeBuilder.currentElement().nodeName().equals("html")) {
                    htmlTreeBuilder2.error(htmlTreeBuilderState);
                }
                return true;
            }
        }

        boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            if (!StringUtil.in(htmlTreeBuilder.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                return htmlTreeBuilder.process(token, InBody);
            }
            htmlTreeBuilder.setFosterInserts(true);
            token = htmlTreeBuilder.process(token, InBody);
            htmlTreeBuilder.setFosterInserts(false);
            return token;
        }
    },
    InTableText {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()] != 5) {
                if (htmlTreeBuilder.getPendingTableCharacters().size() > 0) {
                    for (String str : htmlTreeBuilder.getPendingTableCharacters()) {
                        if (HtmlTreeBuilderState.isWhitespace(str)) {
                            htmlTreeBuilder.insert(new Character().data(str));
                        } else {
                            htmlTreeBuilder.error(this);
                            if (StringUtil.in(htmlTreeBuilder.currentElement().nodeName(), "table", "tbody", "tfoot", "thead", "tr")) {
                                htmlTreeBuilder.setFosterInserts(true);
                                htmlTreeBuilder.process(new Character().data(str), InBody);
                                htmlTreeBuilder.setFosterInserts(false);
                            } else {
                                htmlTreeBuilder.process(new Character().data(str), InBody);
                            }
                        }
                    }
                    htmlTreeBuilder.newPendingTableCharacters();
                }
                htmlTreeBuilder.transition(htmlTreeBuilder.originalState());
                return htmlTreeBuilder.process(token);
            }
            token = token.asCharacter();
            if (token.getData().equals(HtmlTreeBuilderState.nullString)) {
                htmlTreeBuilder.error(this);
                return false;
            }
            htmlTreeBuilder.getPendingTableCharacters().add(token.getData());
            return true;
        }
    },
    InCaption {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isEndTag() && token.asEndTag().normalName().equals("caption")) {
                if (htmlTreeBuilder.inTableScope(token.asEndTag().normalName()) == null) {
                    htmlTreeBuilder.error(this);
                    return false;
                }
                htmlTreeBuilder.generateImpliedEndTags();
                if (htmlTreeBuilder.currentElement().nodeName().equals("caption") == null) {
                    htmlTreeBuilder.error(this);
                }
                htmlTreeBuilder.popStackToClose("caption");
                htmlTreeBuilder.clearFormattingElementsToLastMarker();
                htmlTreeBuilder.transition(InTable);
            } else if ((token.isStartTag() && StringUtil.in(token.asStartTag().normalName(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) || (token.isEndTag() && token.asEndTag().normalName().equals("table"))) {
                htmlTreeBuilder.error(this);
                if (htmlTreeBuilder.processEndTag("caption")) {
                    return htmlTreeBuilder.process(token);
                }
            } else if (!token.isEndTag() || !StringUtil.in(token.asEndTag().normalName(), TtmlNode.TAG_BODY, "col", "colgroup", "html", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                htmlTreeBuilder.error(this);
                return false;
            }
            return true;
        }
    },
    InColumnGroup {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
                return true;
            }
            int i = AnonymousClass24.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()];
            if (i != 6) {
                switch (i) {
                    case 1:
                        htmlTreeBuilder.insert(token.asComment());
                        break;
                    case 2:
                        htmlTreeBuilder.error(this);
                        break;
                    case 3:
                        StartTag asStartTag = token.asStartTag();
                        String normalName = asStartTag.normalName();
                        if (normalName.equals("html")) {
                            return htmlTreeBuilder.process(token, InBody);
                        }
                        if (normalName.equals("col")) {
                            htmlTreeBuilder.insertEmpty(asStartTag);
                            break;
                        }
                        return anythingElse(token, htmlTreeBuilder);
                    case 4:
                        if (token.asEndTag().normalName().equals("colgroup")) {
                            if (htmlTreeBuilder.currentElement().nodeName().equals("html") == null) {
                                htmlTreeBuilder.pop();
                                htmlTreeBuilder.transition(InTable);
                                break;
                            }
                            htmlTreeBuilder.error(this);
                            return null;
                        }
                        return anythingElse(token, htmlTreeBuilder);
                    default:
                        return anythingElse(token, htmlTreeBuilder);
                }
                return true;
            } else if (htmlTreeBuilder.currentElement().nodeName().equals("html")) {
                return true;
            } else {
                return anythingElse(token, htmlTreeBuilder);
            }
        }

        private boolean anythingElse(Token token, TreeBuilder treeBuilder) {
            return treeBuilder.processEndTag("colgroup") ? treeBuilder.process(token) : true;
        }
    },
    InTableBody {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            switch (token.type) {
                case StartTag:
                    StartTag asStartTag = token.asStartTag();
                    String normalName = asStartTag.normalName();
                    if (normalName.equals("tr")) {
                        htmlTreeBuilder.clearStackToTableBodyContext();
                        htmlTreeBuilder.insert(asStartTag);
                        htmlTreeBuilder.transition(InRow);
                        break;
                    } else if (StringUtil.in(normalName, "th", "td")) {
                        htmlTreeBuilder.error(this);
                        htmlTreeBuilder.processStartTag("tr");
                        return htmlTreeBuilder.process(asStartTag);
                    } else if (StringUtil.in(normalName, "caption", "col", "colgroup", "tbody", "tfoot", "thead")) {
                        return exitTableBody(token, htmlTreeBuilder);
                    } else {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                case EndTag:
                    String normalName2 = token.asEndTag().normalName();
                    if (StringUtil.in(normalName2, "tbody", "tfoot", "thead")) {
                        if (htmlTreeBuilder.inTableScope(normalName2) != null) {
                            htmlTreeBuilder.clearStackToTableBodyContext();
                            htmlTreeBuilder.pop();
                            htmlTreeBuilder.transition(InTable);
                            break;
                        }
                        htmlTreeBuilder.error(this);
                        return false;
                    } else if (normalName2.equals("table")) {
                        return exitTableBody(token, htmlTreeBuilder);
                    } else {
                        if (!StringUtil.in(normalName2, TtmlNode.TAG_BODY, "caption", "col", "colgroup", "html", "td", "th", "tr")) {
                            return anythingElse(token, htmlTreeBuilder);
                        }
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                default:
                    return anythingElse(token, htmlTreeBuilder);
            }
            return true;
        }

        private boolean exitTableBody(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (htmlTreeBuilder.inTableScope("tbody") || htmlTreeBuilder.inTableScope("thead") || htmlTreeBuilder.inScope("tfoot")) {
                htmlTreeBuilder.clearStackToTableBodyContext();
                htmlTreeBuilder.processEndTag(htmlTreeBuilder.currentElement().nodeName());
                return htmlTreeBuilder.process(token);
            }
            htmlTreeBuilder.error(this);
            return null;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InTable);
        }
    },
    InRow {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isStartTag()) {
                StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (StringUtil.in(normalName, "th", "td")) {
                    htmlTreeBuilder.clearStackToTableRowContext();
                    htmlTreeBuilder.insert(asStartTag);
                    htmlTreeBuilder.transition(InCell);
                    htmlTreeBuilder.insertMarkerToFormattingElements();
                } else if (StringUtil.in(normalName, "caption", "col", "colgroup", "tbody", "tfoot", "thead", "tr")) {
                    return handleMissingTr(token, htmlTreeBuilder);
                } else {
                    return anythingElse(token, htmlTreeBuilder);
                }
            } else if (!token.isEndTag()) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                String normalName2 = token.asEndTag().normalName();
                if (normalName2.equals("tr")) {
                    if (htmlTreeBuilder.inTableScope(normalName2) == null) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    htmlTreeBuilder.clearStackToTableRowContext();
                    htmlTreeBuilder.pop();
                    htmlTreeBuilder.transition(InTableBody);
                } else if (normalName2.equals("table")) {
                    return handleMissingTr(token, htmlTreeBuilder);
                } else {
                    if (StringUtil.in(normalName2, "tbody", "tfoot", "thead")) {
                        if (htmlTreeBuilder.inTableScope(normalName2)) {
                            htmlTreeBuilder.processEndTag("tr");
                            return htmlTreeBuilder.process(token);
                        }
                        htmlTreeBuilder.error(this);
                        return false;
                    } else if (!StringUtil.in(normalName2, TtmlNode.TAG_BODY, "caption", "col", "colgroup", "html", "td", "th")) {
                        return anythingElse(token, htmlTreeBuilder);
                    } else {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                }
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InTable);
        }

        private boolean handleMissingTr(Token token, TreeBuilder treeBuilder) {
            return treeBuilder.processEndTag("tr") ? treeBuilder.process(token) : null;
        }
    },
    InCell {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isEndTag()) {
                String normalName = token.asEndTag().normalName();
                if (StringUtil.in(normalName, "td", "th")) {
                    if (htmlTreeBuilder.inTableScope(normalName) == null) {
                        htmlTreeBuilder.error(this);
                        htmlTreeBuilder.transition(InRow);
                        return false;
                    }
                    htmlTreeBuilder.generateImpliedEndTags();
                    if (htmlTreeBuilder.currentElement().nodeName().equals(normalName) == null) {
                        htmlTreeBuilder.error(this);
                    }
                    htmlTreeBuilder.popStackToClose(normalName);
                    htmlTreeBuilder.clearFormattingElementsToLastMarker();
                    htmlTreeBuilder.transition(InRow);
                    return true;
                } else if (StringUtil.in(normalName, TtmlNode.TAG_BODY, "caption", "col", "colgroup", "html")) {
                    htmlTreeBuilder.error(this);
                    return false;
                } else if (!StringUtil.in(normalName, "table", "tbody", "tfoot", "thead", "tr")) {
                    return anythingElse(token, htmlTreeBuilder);
                } else {
                    if (htmlTreeBuilder.inTableScope(normalName)) {
                        closeCell(htmlTreeBuilder);
                        return htmlTreeBuilder.process(token);
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                }
            } else if (!token.isStartTag() || !StringUtil.in(token.asStartTag().normalName(), "caption", "col", "colgroup", "tbody", "td", "tfoot", "th", "thead", "tr")) {
                return anythingElse(token, htmlTreeBuilder);
            } else {
                if (htmlTreeBuilder.inTableScope("td") || htmlTreeBuilder.inTableScope("th")) {
                    closeCell(htmlTreeBuilder);
                    return htmlTreeBuilder.process(token);
                }
                htmlTreeBuilder.error(this);
                return false;
            }
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return htmlTreeBuilder.process(token, InBody);
        }

        private void closeCell(HtmlTreeBuilder htmlTreeBuilder) {
            if (htmlTreeBuilder.inTableScope("td")) {
                htmlTreeBuilder.processEndTag("td");
            } else {
                htmlTreeBuilder.processEndTag("th");
            }
        }
    },
    InSelect {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            switch (token.type) {
                case Comment:
                    htmlTreeBuilder.insert(token.asComment());
                    break;
                case Doctype:
                    htmlTreeBuilder.error(this);
                    return false;
                case StartTag:
                    StartTag asStartTag = token.asStartTag();
                    String normalName = asStartTag.normalName();
                    if (normalName.equals("html")) {
                        return htmlTreeBuilder.process(asStartTag, InBody);
                    }
                    if (normalName.equals("option")) {
                        htmlTreeBuilder.processEndTag("option");
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    } else if (normalName.equals("optgroup")) {
                        if (htmlTreeBuilder.currentElement().nodeName().equals("option") != null) {
                            htmlTreeBuilder.processEndTag("option");
                        } else if (htmlTreeBuilder.currentElement().nodeName().equals("optgroup") != null) {
                            htmlTreeBuilder.processEndTag("optgroup");
                        }
                        htmlTreeBuilder.insert(asStartTag);
                        break;
                    } else if (normalName.equals("select")) {
                        htmlTreeBuilder.error(this);
                        return htmlTreeBuilder.processEndTag("select");
                    } else if (StringUtil.in(normalName, "input", "keygen", "textarea")) {
                        htmlTreeBuilder.error(this);
                        if (htmlTreeBuilder.inSelectScope("select") == null) {
                            return false;
                        }
                        htmlTreeBuilder.processEndTag("select");
                        return htmlTreeBuilder.process(asStartTag);
                    } else if (normalName.equals("script")) {
                        return htmlTreeBuilder.process(token, InHead);
                    } else {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                case EndTag:
                    String normalName2 = token.asEndTag().normalName();
                    if (normalName2.equals("optgroup")) {
                        if (!(htmlTreeBuilder.currentElement().nodeName().equals("option") == null || htmlTreeBuilder.aboveOnStack(htmlTreeBuilder.currentElement()) == null || htmlTreeBuilder.aboveOnStack(htmlTreeBuilder.currentElement()).nodeName().equals("optgroup") == null)) {
                            htmlTreeBuilder.processEndTag("option");
                        }
                        if (htmlTreeBuilder.currentElement().nodeName().equals("optgroup") == null) {
                            htmlTreeBuilder.error(this);
                            break;
                        }
                        htmlTreeBuilder.pop();
                        break;
                    } else if (normalName2.equals("option")) {
                        if (htmlTreeBuilder.currentElement().nodeName().equals("option") == null) {
                            htmlTreeBuilder.error(this);
                            break;
                        }
                        htmlTreeBuilder.pop();
                        break;
                    } else if (normalName2.equals("select")) {
                        if (htmlTreeBuilder.inSelectScope(normalName2) != null) {
                            htmlTreeBuilder.popStackToClose(normalName2);
                            htmlTreeBuilder.resetInsertionMode();
                            break;
                        }
                        htmlTreeBuilder.error(this);
                        return false;
                    } else {
                        return anythingElse(token, htmlTreeBuilder);
                    }
                case Character:
                    Character asCharacter = token.asCharacter();
                    if (!asCharacter.getData().equals(HtmlTreeBuilderState.nullString)) {
                        htmlTreeBuilder.insert(asCharacter);
                        break;
                    }
                    htmlTreeBuilder.error(this);
                    return false;
                case EOF:
                    if (htmlTreeBuilder.currentElement().nodeName().equals("html") == null) {
                        htmlTreeBuilder.error(this);
                        break;
                    }
                    break;
                default:
                    return anythingElse(token, htmlTreeBuilder);
            }
            return true;
        }

        private boolean anythingElse(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            htmlTreeBuilder.error(this);
            return null;
        }
    },
    InSelectInTable {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isStartTag() && StringUtil.in(token.asStartTag().normalName(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                htmlTreeBuilder.error(this);
                htmlTreeBuilder.processEndTag("select");
                return htmlTreeBuilder.process(token);
            } else if (!token.isEndTag() || !StringUtil.in(token.asEndTag().normalName(), "caption", "table", "tbody", "tfoot", "thead", "tr", "td", "th")) {
                return htmlTreeBuilder.process(token, InSelect);
            } else {
                htmlTreeBuilder.error(this);
                if (!htmlTreeBuilder.inTableScope(token.asEndTag().normalName())) {
                    return null;
                }
                htmlTreeBuilder.processEndTag("select");
                return htmlTreeBuilder.process(token);
            }
        }
    },
    AfterBody {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                return htmlTreeBuilder.process(token, InBody);
            }
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEndTag() && token.asEndTag().normalName().equals("html")) {
                    if (htmlTreeBuilder.isFragmentParsing() != null) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                    htmlTreeBuilder.transition(AfterAfterBody);
                } else if (!token.isEOF()) {
                    htmlTreeBuilder.error(this);
                    htmlTreeBuilder.transition(InBody);
                    return htmlTreeBuilder.process(token);
                }
            }
            return true;
        }
    },
    InFrameset {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag()) {
                StartTag asStartTag = token.asStartTag();
                String normalName = asStartTag.normalName();
                if (normalName.equals("html")) {
                    return htmlTreeBuilder.process(asStartTag, InBody);
                }
                if (normalName.equals("frameset")) {
                    htmlTreeBuilder.insert(asStartTag);
                } else if (normalName.equals("frame")) {
                    htmlTreeBuilder.insertEmpty(asStartTag);
                } else if (normalName.equals("noframes")) {
                    return htmlTreeBuilder.process(asStartTag, InHead);
                } else {
                    htmlTreeBuilder.error(this);
                    return false;
                }
            } else if (token.isEndTag() && token.asEndTag().normalName().equals("frameset")) {
                if (htmlTreeBuilder.currentElement().nodeName().equals("html") != null) {
                    htmlTreeBuilder.error(this);
                    return false;
                }
                htmlTreeBuilder.pop();
                if (htmlTreeBuilder.isFragmentParsing() == null && htmlTreeBuilder.currentElement().nodeName().equals("frameset") == null) {
                    htmlTreeBuilder.transition(AfterFrameset);
                }
            } else if (token.isEOF() == null) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (htmlTreeBuilder.currentElement().nodeName().equals("html") == null) {
                htmlTreeBuilder.error(this);
                return true;
            }
            return true;
        }
    },
    AfterFrameset {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (HtmlTreeBuilderState.isWhitespace(token)) {
                htmlTreeBuilder.insert(token.asCharacter());
            } else if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else if (token.isDoctype()) {
                htmlTreeBuilder.error(this);
                return false;
            } else if (token.isStartTag() && token.asStartTag().normalName().equals("html")) {
                return htmlTreeBuilder.process(token, InBody);
            } else {
                if (token.isEndTag() && token.asEndTag().normalName().equals("html")) {
                    htmlTreeBuilder.transition(AfterAfterFrameset);
                } else if (token.isStartTag() && token.asStartTag().normalName().equals("noframes")) {
                    return htmlTreeBuilder.process(token, InHead);
                } else {
                    if (token.isEOF() == null) {
                        htmlTreeBuilder.error(this);
                        return false;
                    }
                }
            }
            return true;
        }
    },
    AfterAfterBody {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else {
                if (!(token.isDoctype() || HtmlTreeBuilderState.isWhitespace(token))) {
                    if (!token.isStartTag() || !token.asStartTag().normalName().equals("html")) {
                        if (!token.isEOF()) {
                            htmlTreeBuilder.error(this);
                            htmlTreeBuilder.transition(InBody);
                            return htmlTreeBuilder.process(token);
                        }
                    }
                }
                return htmlTreeBuilder.process(token, InBody);
            }
            return true;
        }
    },
    AfterAfterFrameset {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            if (token.isComment()) {
                htmlTreeBuilder.insert(token.asComment());
            } else {
                if (!(token.isDoctype() || HtmlTreeBuilderState.isWhitespace(token))) {
                    if (!token.isStartTag() || !token.asStartTag().normalName().equals("html")) {
                        if (!token.isEOF()) {
                            if (token.isStartTag() && token.asStartTag().normalName().equals("noframes")) {
                                return htmlTreeBuilder.process(token, InHead);
                            }
                            htmlTreeBuilder.error(this);
                            return null;
                        }
                    }
                }
                return htmlTreeBuilder.process(token, InBody);
            }
            return true;
        }
    },
    ForeignContent {
        boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder) {
            return true;
        }
    };
    
    private static String nullString;

    private static final class Constants {
        private static final String[] DdDt = null;
        private static final String[] Formatters = null;
        private static final String[] Headings = null;
        private static final String[] InBodyEndAdoptionFormatters = null;
        private static final String[] InBodyEndClosers = null;
        private static final String[] InBodyEndTableFosters = null;
        private static final String[] InBodyStartApplets = null;
        private static final String[] InBodyStartDrop = null;
        private static final String[] InBodyStartEmptyFormatters = null;
        private static final String[] InBodyStartInputAttribs = null;
        private static final String[] InBodyStartLiBreakers = null;
        private static final String[] InBodyStartMedia = null;
        private static final String[] InBodyStartOptions = null;
        private static final String[] InBodyStartPClosers = null;
        private static final String[] InBodyStartPreListing = null;
        private static final String[] InBodyStartRuby = null;
        private static final String[] InBodyStartToHead = null;

        private Constants() {
        }

        static {
            InBodyStartToHead = new String[]{"base", "basefont", "bgsound", "command", "link", "meta", "noframes", "script", TtmlNode.TAG_STYLE, "title"};
            InBodyStartPClosers = new String[]{"address", "article", "aside", "blockquote", TtmlNode.CENTER, "details", "dir", TtmlNode.TAG_DIV, "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "menu", "nav", "ol", TtmlNode.TAG_P, "section", "summary", "ul"};
            Headings = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};
            InBodyStartPreListing = new String[]{"pre", "listing"};
            InBodyStartLiBreakers = new String[]{"address", TtmlNode.TAG_DIV, TtmlNode.TAG_P};
            DdDt = new String[]{"dd", "dt"};
            Formatters = new String[]{"b", "big", "code", "em", "font", "i", "s", "small", "strike", "strong", TtmlNode.TAG_TT, "u"};
            InBodyStartApplets = new String[]{"applet", "marquee", "object"};
            InBodyStartEmptyFormatters = new String[]{"area", TtmlNode.TAG_BR, "embed", "img", "keygen", "wbr"};
            InBodyStartMedia = new String[]{"param", "source", "track"};
            InBodyStartInputAttribs = new String[]{"name", "action", "prompt"};
            InBodyStartOptions = new String[]{"optgroup", "option"};
            InBodyStartRuby = new String[]{"rp", "rt"};
            InBodyStartDrop = new String[]{"caption", "col", "colgroup", "frame", TtmlNode.TAG_HEAD, "tbody", "td", "tfoot", "th", "thead", "tr"};
            InBodyEndClosers = new String[]{"address", "article", "aside", "blockquote", "button", TtmlNode.CENTER, "details", "dir", TtmlNode.TAG_DIV, "dl", "fieldset", "figcaption", "figure", "footer", "header", "hgroup", "listing", "menu", "nav", "ol", "pre", "section", "summary", "ul"};
            InBodyEndAdoptionFormatters = new String[]{"a", "b", "big", "code", "em", "font", "i", "nobr", "s", "small", "strike", "strong", TtmlNode.TAG_TT, "u"};
            InBodyEndTableFosters = new String[]{"table", "tbody", "tfoot", "thead", "tr"};
        }
    }

    abstract boolean process(Token token, HtmlTreeBuilder htmlTreeBuilder);

    static {
        nullString = String.valueOf('\u0000');
    }

    private static boolean isWhitespace(Token token) {
        return token.isCharacter() ? isWhitespace(token.asCharacter().getData()) : null;
    }

    private static boolean isWhitespace(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!StringUtil.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static void handleRcData(StartTag startTag, HtmlTreeBuilder htmlTreeBuilder) {
        htmlTreeBuilder.insert(startTag);
        htmlTreeBuilder.tokeniser.transition(TokeniserState.Rcdata);
        htmlTreeBuilder.markInsertionMode();
        htmlTreeBuilder.transition(Text);
    }

    private static void handleRawtext(StartTag startTag, HtmlTreeBuilder htmlTreeBuilder) {
        htmlTreeBuilder.insert(startTag);
        htmlTreeBuilder.tokeniser.transition(TokeniserState.Rawtext);
        htmlTreeBuilder.markInsertionMode();
        htmlTreeBuilder.transition(Text);
    }
}
