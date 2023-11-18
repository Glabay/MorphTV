package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

class Chain {
    private static final boolean DEBUG = false;

    Chain() {
    }

    static void applyChainConstraints(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, int i) {
        int i2;
        ChainHead[] chainHeadArr;
        int i3;
        int i4 = 0;
        if (i == 0) {
            i2 = constraintWidgetContainer.mHorizontalChainsSize;
            chainHeadArr = constraintWidgetContainer.mHorizontalChainsArray;
            i3 = i2;
            i2 = 0;
        } else {
            i2 = 2;
            int i5 = constraintWidgetContainer.mVerticalChainsSize;
            i3 = i5;
            chainHeadArr = constraintWidgetContainer.mVerticalChainsArray;
        }
        while (i4 < i3) {
            ChainHead chainHead = chainHeadArr[i4];
            chainHead.define();
            if (!constraintWidgetContainer.optimizeFor(4)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            } else if (!Optimizer.applyChainOptimized(constraintWidgetContainer, linearSystem, i, i2, chainHead)) {
                applyChainConstraints(constraintWidgetContainer, linearSystem, i, i2, chainHead);
            }
            i4++;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer r47, android.support.constraint.solver.LinearSystem r48, int r49, int r50, android.support.constraint.solver.widgets.ChainHead r51) {
        /*
        r0 = r47;
        r9 = r48;
        r1 = r51;
        r11 = r1.mFirst;
        r12 = r1.mLast;
        r13 = r1.mFirstVisibleWidget;
        r14 = r1.mLastVisibleWidget;
        r2 = r1.mHead;
        r3 = r1.mTotalWeight;
        r4 = r1.mFirstMatchConstraintWidget;
        r4 = r1.mLastMatchConstraintWidget;
        r4 = r0.mListDimensionBehaviors;
        r4 = r4[r49];
        r5 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        r7 = 1;
        if (r4 != r5) goto L_0x0021;
    L_0x001f:
        r4 = 1;
        goto L_0x0022;
    L_0x0021:
        r4 = 0;
    L_0x0022:
        r5 = 2;
        if (r49 != 0) goto L_0x0042;
    L_0x0025:
        r8 = r2.mHorizontalChainStyle;
        if (r8 != 0) goto L_0x002b;
    L_0x0029:
        r8 = 1;
        goto L_0x002c;
    L_0x002b:
        r8 = 0;
    L_0x002c:
        r6 = r2.mHorizontalChainStyle;
        if (r6 != r7) goto L_0x0032;
    L_0x0030:
        r6 = 1;
        goto L_0x0033;
    L_0x0032:
        r6 = 0;
    L_0x0033:
        r7 = r2.mHorizontalChainStyle;
        if (r7 != r5) goto L_0x0039;
    L_0x0037:
        r5 = 1;
        goto L_0x003a;
    L_0x0039:
        r5 = 0;
    L_0x003a:
        r7 = r5;
        r18 = r6;
        r17 = r8;
        r6 = r11;
        r5 = 0;
        goto L_0x0056;
    L_0x0042:
        r6 = r2.mVerticalChainStyle;
        if (r6 != 0) goto L_0x0048;
    L_0x0046:
        r8 = 1;
        goto L_0x0049;
    L_0x0048:
        r8 = 0;
    L_0x0049:
        r6 = r2.mVerticalChainStyle;
        r7 = 1;
        if (r6 != r7) goto L_0x0050;
    L_0x004e:
        r6 = 1;
        goto L_0x0051;
    L_0x0050:
        r6 = 0;
    L_0x0051:
        r7 = r2.mVerticalChainStyle;
        if (r7 != r5) goto L_0x0039;
    L_0x0055:
        goto L_0x0037;
    L_0x0056:
        r21 = 0;
        if (r5 != 0) goto L_0x0135;
    L_0x005a:
        r8 = r6.mListAnchors;
        r8 = r8[r50];
        if (r4 != 0) goto L_0x0066;
    L_0x0060:
        if (r7 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x0066;
    L_0x0063:
        r23 = 4;
        goto L_0x0068;
    L_0x0066:
        r23 = 1;
    L_0x0068:
        r24 = r8.getMargin();
        r25 = r3;
        r3 = r8.mTarget;
        if (r3 == 0) goto L_0x007c;
    L_0x0072:
        if (r6 == r11) goto L_0x007c;
    L_0x0074:
        r3 = r8.mTarget;
        r3 = r3.getMargin();
        r24 = r24 + r3;
    L_0x007c:
        r3 = r24;
        if (r7 == 0) goto L_0x008a;
    L_0x0080:
        if (r6 == r11) goto L_0x008a;
    L_0x0082:
        if (r6 == r13) goto L_0x008a;
    L_0x0084:
        r27 = r2;
        r26 = r5;
        r5 = 6;
        goto L_0x009a;
    L_0x008a:
        if (r17 == 0) goto L_0x0094;
    L_0x008c:
        if (r4 == 0) goto L_0x0094;
    L_0x008e:
        r27 = r2;
        r26 = r5;
        r5 = 4;
        goto L_0x009a;
    L_0x0094:
        r27 = r2;
        r26 = r5;
        r5 = r23;
    L_0x009a:
        r2 = r8.mTarget;
        if (r2 == 0) goto L_0x00c7;
    L_0x009e:
        if (r6 != r13) goto L_0x00af;
    L_0x00a0:
        r2 = r8.mSolverVariable;
        r28 = r11;
        r11 = r8.mTarget;
        r11 = r11.mSolverVariable;
        r29 = r7;
        r7 = 5;
        r9.addGreaterThan(r2, r11, r3, r7);
        goto L_0x00bd;
    L_0x00af:
        r29 = r7;
        r28 = r11;
        r2 = r8.mSolverVariable;
        r7 = r8.mTarget;
        r7 = r7.mSolverVariable;
        r11 = 6;
        r9.addGreaterThan(r2, r7, r3, r11);
    L_0x00bd:
        r2 = r8.mSolverVariable;
        r7 = r8.mTarget;
        r7 = r7.mSolverVariable;
        r9.addEquality(r2, r7, r3, r5);
        goto L_0x00cb;
    L_0x00c7:
        r29 = r7;
        r28 = r11;
    L_0x00cb:
        if (r4 == 0) goto L_0x0102;
    L_0x00cd:
        r2 = r6.getVisibility();
        r3 = 8;
        if (r2 == r3) goto L_0x00f1;
    L_0x00d5:
        r2 = r6.mListDimensionBehaviors;
        r2 = r2[r49];
        r3 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r2 != r3) goto L_0x00f1;
    L_0x00dd:
        r2 = r6.mListAnchors;
        r3 = r50 + 1;
        r2 = r2[r3];
        r2 = r2.mSolverVariable;
        r3 = r6.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mSolverVariable;
        r5 = 0;
        r7 = 5;
        r9.addGreaterThan(r2, r3, r5, r7);
        goto L_0x00f2;
    L_0x00f1:
        r5 = 0;
    L_0x00f2:
        r2 = r6.mListAnchors;
        r2 = r2[r50];
        r2 = r2.mSolverVariable;
        r3 = r0.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mSolverVariable;
        r7 = 6;
        r9.addGreaterThan(r2, r3, r5, r7);
    L_0x0102:
        r2 = r6.mListAnchors;
        r3 = r50 + 1;
        r2 = r2[r3];
        r2 = r2.mTarget;
        if (r2 == 0) goto L_0x0123;
    L_0x010c:
        r2 = r2.mOwner;
        r3 = r2.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0123;
    L_0x0116:
        r3 = r2.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        r3 = r3.mOwner;
        if (r3 == r6) goto L_0x0121;
    L_0x0120:
        goto L_0x0123;
    L_0x0121:
        r21 = r2;
    L_0x0123:
        if (r21 == 0) goto L_0x012a;
    L_0x0125:
        r6 = r21;
        r5 = r26;
        goto L_0x012b;
    L_0x012a:
        r5 = 1;
    L_0x012b:
        r3 = r25;
        r2 = r27;
        r11 = r28;
        r7 = r29;
        goto L_0x0056;
    L_0x0135:
        r27 = r2;
        r25 = r3;
        r29 = r7;
        r28 = r11;
        if (r14 == 0) goto L_0x0161;
    L_0x013f:
        r2 = r12.mListAnchors;
        r3 = r50 + 1;
        r2 = r2[r3];
        r2 = r2.mTarget;
        if (r2 == 0) goto L_0x0161;
    L_0x0149:
        r2 = r14.mListAnchors;
        r2 = r2[r3];
        r5 = r2.mSolverVariable;
        r6 = r12.mListAnchors;
        r3 = r6[r3];
        r3 = r3.mTarget;
        r3 = r3.mSolverVariable;
        r2 = r2.getMargin();
        r2 = -r2;
        r8 = 5;
        r9.addLowerThan(r5, r3, r2, r8);
        goto L_0x0162;
    L_0x0161:
        r8 = 5;
    L_0x0162:
        if (r4 == 0) goto L_0x017e;
    L_0x0164:
        r0 = r0.mListAnchors;
        r2 = r50 + 1;
        r0 = r0[r2];
        r0 = r0.mSolverVariable;
        r3 = r12.mListAnchors;
        r3 = r3[r2];
        r3 = r3.mSolverVariable;
        r4 = r12.mListAnchors;
        r2 = r4[r2];
        r2 = r2.getMargin();
        r4 = 6;
        r9.addGreaterThan(r0, r3, r2, r4);
    L_0x017e:
        r0 = r1.mWeightedMatchConstraintsWidgets;
        if (r0 == 0) goto L_0x022e;
    L_0x0182:
        r2 = r0.size();
        r7 = 1;
        if (r2 <= r7) goto L_0x022e;
    L_0x0189:
        r3 = r1.mHasUndefinedWeights;
        if (r3 == 0) goto L_0x0196;
    L_0x018d:
        r3 = r1.mHasComplexMatchWeights;
        if (r3 != 0) goto L_0x0196;
    L_0x0191:
        r3 = r1.mWidgetsMatchCount;
        r3 = (float) r3;
        r25 = r3;
    L_0x0196:
        r3 = 0;
        r5 = r21;
        r4 = 0;
        r31 = 0;
    L_0x019c:
        if (r4 >= r2) goto L_0x022e;
    L_0x019e:
        r6 = r0.get(r4);
        r6 = (android.support.constraint.solver.widgets.ConstraintWidget) r6;
        r11 = r6.mWeight;
        r11 = r11[r49];
        r16 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1));
        if (r16 >= 0) goto L_0x01cc;
    L_0x01ac:
        r11 = r1.mHasComplexMatchWeights;
        if (r11 == 0) goto L_0x01c6;
    L_0x01b0:
        r11 = r6.mListAnchors;
        r16 = r50 + 1;
        r11 = r11[r16];
        r11 = r11.mSolverVariable;
        r6 = r6.mListAnchors;
        r6 = r6[r50];
        r6 = r6.mSolverVariable;
        r7 = 0;
        r8 = 4;
        r9.addEquality(r11, r6, r7, r8);
        r8 = 6;
        r11 = 0;
        goto L_0x01e4;
    L_0x01c6:
        r8 = 4;
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x01cd;
    L_0x01cc:
        r8 = 4;
    L_0x01cd:
        r7 = (r11 > r3 ? 1 : (r11 == r3 ? 0 : -1));
        if (r7 != 0) goto L_0x01e7;
    L_0x01d1:
        r7 = r6.mListAnchors;
        r11 = r50 + 1;
        r7 = r7[r11];
        r7 = r7.mSolverVariable;
        r6 = r6.mListAnchors;
        r6 = r6[r50];
        r6 = r6.mSolverVariable;
        r8 = 6;
        r11 = 0;
        r9.addEquality(r7, r6, r11, r8);
    L_0x01e4:
        r39 = r0;
        goto L_0x0225;
    L_0x01e7:
        r7 = 0;
        r8 = 6;
        if (r5 == 0) goto L_0x0220;
    L_0x01eb:
        r3 = r5.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mSolverVariable;
        r5 = r5.mListAnchors;
        r15 = r50 + 1;
        r5 = r5[r15];
        r5 = r5.mSolverVariable;
        r7 = r6.mListAnchors;
        r7 = r7[r50];
        r7 = r7.mSolverVariable;
        r8 = r6.mListAnchors;
        r8 = r8[r15];
        r8 = r8.mSolverVariable;
        r39 = r0;
        r0 = r48.createRow();
        r30 = r0;
        r32 = r25;
        r33 = r11;
        r34 = r3;
        r35 = r5;
        r36 = r7;
        r37 = r8;
        r30.createRowEqualMatchDimensions(r31, r32, r33, r34, r35, r36, r37);
        r9.addConstraint(r0);
        goto L_0x0222;
    L_0x0220:
        r39 = r0;
    L_0x0222:
        r5 = r6;
        r31 = r11;
    L_0x0225:
        r4 = r4 + 1;
        r0 = r39;
        r3 = 0;
        r7 = 1;
        r8 = 5;
        goto L_0x019c;
    L_0x022e:
        if (r13 == 0) goto L_0x029a;
    L_0x0230:
        if (r13 == r14) goto L_0x0234;
    L_0x0232:
        if (r29 == 0) goto L_0x029a;
    L_0x0234:
        r11 = r28;
        r0 = r11.mListAnchors;
        r0 = r0[r50];
        r1 = r12.mListAnchors;
        r2 = r50 + 1;
        r1 = r1[r2];
        r3 = r11.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x0251;
    L_0x0248:
        r3 = r11.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        r3 = r3.mSolverVariable;
        goto L_0x0253;
    L_0x0251:
        r3 = r21;
    L_0x0253:
        r4 = r12.mListAnchors;
        r4 = r4[r2];
        r4 = r4.mTarget;
        if (r4 == 0) goto L_0x0265;
    L_0x025b:
        r4 = r12.mListAnchors;
        r4 = r4[r2];
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        r5 = r4;
        goto L_0x0267;
    L_0x0265:
        r5 = r21;
    L_0x0267:
        if (r13 != r14) goto L_0x0271;
    L_0x0269:
        r0 = r13.mListAnchors;
        r0 = r0[r50];
        r1 = r13.mListAnchors;
        r1 = r1[r2];
    L_0x0271:
        if (r3 == 0) goto L_0x04be;
    L_0x0273:
        if (r5 == 0) goto L_0x04be;
    L_0x0275:
        if (r49 != 0) goto L_0x027d;
    L_0x0277:
        r2 = r27;
        r2 = r2.mHorizontalBiasPercent;
    L_0x027b:
        r4 = r2;
        goto L_0x0282;
    L_0x027d:
        r2 = r27;
        r2 = r2.mVerticalBiasPercent;
        goto L_0x027b;
    L_0x0282:
        r6 = r0.getMargin();
        r7 = r1.getMargin();
        r2 = r0.mSolverVariable;
        r8 = r1.mSolverVariable;
        r10 = 5;
        r0 = r9;
        r1 = r2;
        r2 = r3;
        r3 = r6;
        r6 = r8;
        r8 = r10;
        r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x04be;
    L_0x029a:
        r11 = r28;
        if (r17 == 0) goto L_0x0398;
    L_0x029e:
        if (r13 == 0) goto L_0x0398;
    L_0x02a0:
        r0 = r1.mWidgetsMatchCount;
        if (r0 <= 0) goto L_0x02ad;
    L_0x02a4:
        r0 = r1.mWidgetsCount;
        r1 = r1.mWidgetsMatchCount;
        if (r0 != r1) goto L_0x02ad;
    L_0x02aa:
        r38 = 1;
        goto L_0x02af;
    L_0x02ad:
        r38 = 0;
    L_0x02af:
        r0 = r13;
        r8 = r0;
    L_0x02b1:
        if (r8 == 0) goto L_0x04be;
    L_0x02b3:
        r1 = r8.mListNextVisibleWidget;
        r7 = r1[r49];
        if (r7 != 0) goto L_0x02c5;
    L_0x02b9:
        if (r8 != r14) goto L_0x02bc;
    L_0x02bb:
        goto L_0x02c5;
    L_0x02bc:
        r15 = r7;
        r16 = r8;
    L_0x02bf:
        r20 = 6;
        r22 = 4;
        goto L_0x0393;
    L_0x02c5:
        r1 = r8.mListAnchors;
        r1 = r1[r50];
        r2 = r1.mSolverVariable;
        r3 = r1.mTarget;
        if (r3 == 0) goto L_0x02d4;
    L_0x02cf:
        r3 = r1.mTarget;
        r3 = r3.mSolverVariable;
        goto L_0x02d6;
    L_0x02d4:
        r3 = r21;
    L_0x02d6:
        if (r0 == r8) goto L_0x02e1;
    L_0x02d8:
        r3 = r0.mListAnchors;
        r4 = r50 + 1;
        r3 = r3[r4];
        r3 = r3.mSolverVariable;
        goto L_0x02f8;
    L_0x02e1:
        if (r8 != r13) goto L_0x02f8;
    L_0x02e3:
        if (r0 != r8) goto L_0x02f8;
    L_0x02e5:
        r3 = r11.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        if (r3 == 0) goto L_0x02f6;
    L_0x02ed:
        r3 = r11.mListAnchors;
        r3 = r3[r50];
        r3 = r3.mTarget;
        r3 = r3.mSolverVariable;
        goto L_0x02f8;
    L_0x02f6:
        r3 = r21;
    L_0x02f8:
        r1 = r1.getMargin();
        r4 = r8.mListAnchors;
        r5 = r50 + 1;
        r4 = r4[r5];
        r4 = r4.getMargin();
        if (r7 == 0) goto L_0x031c;
    L_0x0308:
        r6 = r7.mListAnchors;
        r6 = r6[r50];
        r40 = r7;
        r7 = r6.mSolverVariable;
        r41 = r6;
        r6 = r8.mListAnchors;
        r6 = r6[r5];
        r6 = r6.mSolverVariable;
        r15 = r6;
        r6 = r41;
        goto L_0x0338;
    L_0x031c:
        r40 = r7;
        r6 = r12.mListAnchors;
        r6 = r6[r5];
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x032b;
    L_0x0326:
        r7 = r6.mSolverVariable;
        r42 = r6;
        goto L_0x032f;
    L_0x032b:
        r42 = r6;
        r7 = r21;
    L_0x032f:
        r6 = r8.mListAnchors;
        r6 = r6[r5];
        r6 = r6.mSolverVariable;
        r15 = r6;
        r6 = r42;
    L_0x0338:
        if (r6 == 0) goto L_0x033f;
    L_0x033a:
        r6 = r6.getMargin();
        r4 = r4 + r6;
    L_0x033f:
        if (r0 == 0) goto L_0x034a;
    L_0x0341:
        r0 = r0.mListAnchors;
        r0 = r0[r5];
        r0 = r0.getMargin();
        r1 = r1 + r0;
    L_0x034a:
        if (r2 == 0) goto L_0x038d;
    L_0x034c:
        if (r3 == 0) goto L_0x038d;
    L_0x034e:
        if (r7 == 0) goto L_0x038d;
    L_0x0350:
        if (r15 == 0) goto L_0x038d;
    L_0x0352:
        if (r8 != r13) goto L_0x035e;
    L_0x0354:
        r0 = r13.mListAnchors;
        r0 = r0[r50];
        r0 = r0.getMargin();
        r6 = r0;
        goto L_0x035f;
    L_0x035e:
        r6 = r1;
    L_0x035f:
        if (r8 != r14) goto L_0x036c;
    L_0x0361:
        r0 = r14.mListAnchors;
        r0 = r0[r5];
        r0 = r0.getMargin();
        r16 = r0;
        goto L_0x036e;
    L_0x036c:
        r16 = r4;
    L_0x036e:
        if (r38 == 0) goto L_0x0373;
    L_0x0370:
        r19 = 6;
        goto L_0x0375;
    L_0x0373:
        r19 = 4;
    L_0x0375:
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r0 = r9;
        r1 = r2;
        r2 = r3;
        r3 = r6;
        r5 = r7;
        r6 = r15;
        r15 = r40;
        r7 = r16;
        r16 = r8;
        r20 = 6;
        r22 = 4;
        r8 = r19;
        r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x0393;
    L_0x038d:
        r16 = r8;
        r15 = r40;
        goto L_0x02bf;
    L_0x0393:
        r8 = r15;
        r0 = r16;
        goto L_0x02b1;
    L_0x0398:
        r20 = 6;
        r22 = 4;
        if (r18 == 0) goto L_0x04be;
    L_0x039e:
        if (r13 == 0) goto L_0x04be;
    L_0x03a0:
        r0 = r1.mWidgetsMatchCount;
        if (r0 <= 0) goto L_0x03ad;
    L_0x03a4:
        r0 = r1.mWidgetsCount;
        r1 = r1.mWidgetsMatchCount;
        if (r0 != r1) goto L_0x03ad;
    L_0x03aa:
        r38 = 1;
        goto L_0x03af;
    L_0x03ad:
        r38 = 0;
    L_0x03af:
        r0 = r13;
        r8 = r0;
    L_0x03b1:
        if (r8 == 0) goto L_0x0460;
    L_0x03b3:
        r1 = r8.mListNextVisibleWidget;
        r1 = r1[r49];
        if (r8 == r13) goto L_0x045a;
    L_0x03b9:
        if (r8 == r14) goto L_0x045a;
    L_0x03bb:
        if (r1 == 0) goto L_0x045a;
    L_0x03bd:
        if (r1 != r14) goto L_0x03c2;
    L_0x03bf:
        r7 = r21;
        goto L_0x03c3;
    L_0x03c2:
        r7 = r1;
    L_0x03c3:
        r1 = r8.mListAnchors;
        r1 = r1[r50];
        r2 = r1.mSolverVariable;
        r3 = r1.mTarget;
        if (r3 == 0) goto L_0x03d1;
    L_0x03cd:
        r3 = r1.mTarget;
        r3 = r3.mSolverVariable;
    L_0x03d1:
        r3 = r0.mListAnchors;
        r4 = r50 + 1;
        r3 = r3[r4];
        r3 = r3.mSolverVariable;
        r1 = r1.getMargin();
        r5 = r8.mListAnchors;
        r5 = r5[r4];
        r5 = r5.getMargin();
        if (r7 == 0) goto L_0x03fd;
    L_0x03e7:
        r6 = r7.mListAnchors;
        r6 = r6[r50];
        r43 = r7;
        r7 = r6.mSolverVariable;
        r44 = r7;
        r7 = r6.mTarget;
        if (r7 == 0) goto L_0x03fa;
    L_0x03f5:
        r7 = r6.mTarget;
        r7 = r7.mSolverVariable;
        goto L_0x041b;
    L_0x03fa:
        r7 = r21;
        goto L_0x041b;
    L_0x03fd:
        r43 = r7;
        r6 = r8.mListAnchors;
        r6 = r6[r4];
        r6 = r6.mTarget;
        if (r6 == 0) goto L_0x040c;
    L_0x0407:
        r7 = r6.mSolverVariable;
        r45 = r6;
        goto L_0x0410;
    L_0x040c:
        r45 = r6;
        r7 = r21;
    L_0x0410:
        r6 = r8.mListAnchors;
        r6 = r6[r4];
        r6 = r6.mSolverVariable;
        r44 = r7;
        r7 = r6;
        r6 = r45;
    L_0x041b:
        if (r6 == 0) goto L_0x0422;
    L_0x041d:
        r6 = r6.getMargin();
        r5 = r5 + r6;
    L_0x0422:
        r15 = r5;
        if (r0 == 0) goto L_0x042e;
    L_0x0425:
        r0 = r0.mListAnchors;
        r0 = r0[r4];
        r0 = r0.getMargin();
        r1 = r1 + r0;
    L_0x042e:
        r4 = r1;
        if (r38 == 0) goto L_0x0434;
    L_0x0431:
        r16 = 6;
        goto L_0x0436;
    L_0x0434:
        r16 = 4;
    L_0x0436:
        if (r2 == 0) goto L_0x0453;
    L_0x0438:
        if (r3 == 0) goto L_0x0453;
    L_0x043a:
        if (r44 == 0) goto L_0x0453;
    L_0x043c:
        if (r7 == 0) goto L_0x0453;
    L_0x043e:
        r5 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r0 = r9;
        r1 = r2;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r44;
        r6 = r7;
        r19 = r43;
        r7 = r15;
        r15 = r8;
        r10 = 5;
        r8 = r16;
        r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x0457;
    L_0x0453:
        r15 = r8;
        r19 = r43;
        r10 = 5;
    L_0x0457:
        r8 = r19;
        goto L_0x045d;
    L_0x045a:
        r15 = r8;
        r10 = 5;
        r8 = r1;
    L_0x045d:
        r0 = r15;
        goto L_0x03b1;
    L_0x0460:
        r10 = 5;
        r0 = r13.mListAnchors;
        r0 = r0[r50];
        r1 = r11.mListAnchors;
        r1 = r1[r50];
        r1 = r1.mTarget;
        r2 = r14.mListAnchors;
        r3 = r50 + 1;
        r11 = r2[r3];
        r2 = r12.mListAnchors;
        r2 = r2[r3];
        r8 = r2.mTarget;
        if (r1 == 0) goto L_0x04ac;
    L_0x0479:
        if (r13 == r14) goto L_0x0487;
    L_0x047b:
        r2 = r0.mSolverVariable;
        r1 = r1.mSolverVariable;
        r0 = r0.getMargin();
        r9.addEquality(r2, r1, r0, r10);
        goto L_0x04ac;
    L_0x0487:
        if (r8 == 0) goto L_0x04ac;
    L_0x0489:
        r2 = r0.mSolverVariable;
        r3 = r1.mSolverVariable;
        r4 = r0.getMargin();
        r5 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r6 = r11.mSolverVariable;
        r7 = r8.mSolverVariable;
        r15 = r11.getMargin();
        r16 = 5;
        r0 = r9;
        r1 = r2;
        r2 = r3;
        r3 = r4;
        r4 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r15;
        r10 = r8;
        r8 = r16;
        r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8);
        goto L_0x04ad;
    L_0x04ac:
        r10 = r8;
    L_0x04ad:
        if (r10 == 0) goto L_0x04be;
    L_0x04af:
        if (r13 == r14) goto L_0x04be;
    L_0x04b1:
        r0 = r11.mSolverVariable;
        r1 = r10.mSolverVariable;
        r2 = r11.getMargin();
        r2 = -r2;
        r3 = 5;
        r9.addEquality(r0, r1, r2, r3);
    L_0x04be:
        if (r17 != 0) goto L_0x04c2;
    L_0x04c0:
        if (r18 == 0) goto L_0x0524;
    L_0x04c2:
        if (r13 == 0) goto L_0x0524;
    L_0x04c4:
        r0 = r13.mListAnchors;
        r0 = r0[r50];
        r1 = r14.mListAnchors;
        r2 = r50 + 1;
        r1 = r1[r2];
        r3 = r0.mTarget;
        if (r3 == 0) goto L_0x04d7;
    L_0x04d2:
        r3 = r0.mTarget;
        r3 = r3.mSolverVariable;
        goto L_0x04d9;
    L_0x04d7:
        r3 = r21;
    L_0x04d9:
        r4 = r1.mTarget;
        if (r4 == 0) goto L_0x04e2;
    L_0x04dd:
        r4 = r1.mTarget;
        r4 = r4.mSolverVariable;
        goto L_0x04e4;
    L_0x04e2:
        r4 = r21;
    L_0x04e4:
        if (r12 == r14) goto L_0x04f5;
    L_0x04e6:
        r4 = r12.mListAnchors;
        r4 = r4[r2];
        r5 = r4.mTarget;
        if (r5 == 0) goto L_0x04f3;
    L_0x04ee:
        r4 = r4.mTarget;
        r4 = r4.mSolverVariable;
        goto L_0x04f5;
    L_0x04f3:
        r4 = r21;
    L_0x04f5:
        r5 = r4;
        if (r13 != r14) goto L_0x0500;
    L_0x04f8:
        r0 = r13.mListAnchors;
        r0 = r0[r50];
        r1 = r13.mListAnchors;
        r1 = r1[r2];
    L_0x0500:
        if (r3 == 0) goto L_0x0524;
    L_0x0502:
        if (r5 == 0) goto L_0x0524;
    L_0x0504:
        r4 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r6 = r0.getMargin();
        if (r14 != 0) goto L_0x050d;
    L_0x050c:
        goto L_0x050e;
    L_0x050d:
        r12 = r14;
    L_0x050e:
        r7 = r12.mListAnchors;
        r2 = r7[r2];
        r7 = r2.getMargin();
        r2 = r0.mSolverVariable;
        r8 = r1.mSolverVariable;
        r10 = 5;
        r0 = r9;
        r1 = r2;
        r2 = r3;
        r3 = r6;
        r6 = r8;
        r8 = r10;
        r0.addCentering(r1, r2, r3, r4, r5, r6, r7, r8);
    L_0x0524:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Chain.applyChainConstraints(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):void");
    }
}
