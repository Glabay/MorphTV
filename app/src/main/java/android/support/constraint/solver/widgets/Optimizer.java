package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;

public class Optimizer {
    static final int FLAG_CHAIN_DANGLING = 1;
    static final int FLAG_RECOMPUTE_BOUNDS = 2;
    static final int FLAG_USE_OPTIMIZE = 0;
    public static final int OPTIMIZATION_BARRIER = 2;
    public static final int OPTIMIZATION_CHAIN = 4;
    public static final int OPTIMIZATION_DIMENSIONS = 8;
    public static final int OPTIMIZATION_DIRECT = 1;
    public static final int OPTIMIZATION_NONE = 0;
    public static final int OPTIMIZATION_RATIO = 16;
    public static final int OPTIMIZATION_STANDARD = 3;
    static boolean[] flags = new boolean[3];

    static void checkMatchParent(ConstraintWidgetContainer constraintWidgetContainer, LinearSystem linearSystem, ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mListDimensionBehaviors[0] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_PARENT) {
            int i = constraintWidget.mLeft.mMargin;
            int width = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, width);
            constraintWidget.mHorizontalResolution = 2;
            constraintWidget.setHorizontalDimension(i, width);
        }
        if (constraintWidgetContainer.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_PARENT) {
            i = constraintWidget.mTop.mMargin;
            constraintWidgetContainer = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, i);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, constraintWidgetContainer);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline);
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable, constraintWidget.mBaselineDistance + i);
            }
            constraintWidget.mVerticalResolution = 2;
            constraintWidget.setVerticalDimension(i, constraintWidgetContainer);
        }
    }

    private static boolean optimizableMatchConstraint(ConstraintWidget constraintWidget, int i) {
        if (constraintWidget.mListDimensionBehaviors[i] != DimensionBehaviour.MATCH_CONSTRAINT) {
            return false;
        }
        int i2 = 1;
        if (constraintWidget.mDimensionRatio != 0.0f) {
            constraintWidget = constraintWidget.mListDimensionBehaviors;
            if (i != 0) {
                i2 = 0;
            }
            return constraintWidget[i2] == DimensionBehaviour.MATCH_CONSTRAINT ? false : false;
        } else {
            if (i != 0) {
                if (constraintWidget.mMatchConstraintDefaultHeight == 0 && constraintWidget.mMatchConstraintMinHeight == 0) {
                    if (constraintWidget.mMatchConstraintMaxHeight != null) {
                    }
                }
                return false;
            } else if (constraintWidget.mMatchConstraintDefaultWidth == 0 && constraintWidget.mMatchConstraintMinWidth == 0 && constraintWidget.mMatchConstraintMaxWidth == null) {
                return true;
            } else {
                return false;
            }
            return true;
        }
    }

    static void analyze(int i, ConstraintWidget constraintWidget) {
        ConstraintWidget constraintWidget2 = constraintWidget;
        constraintWidget.updateResolutionNodes();
        ResolutionAnchor resolutionNode = constraintWidget2.mLeft.getResolutionNode();
        ResolutionAnchor resolutionNode2 = constraintWidget2.mTop.getResolutionNode();
        ResolutionAnchor resolutionNode3 = constraintWidget2.mRight.getResolutionNode();
        ResolutionAnchor resolutionNode4 = constraintWidget2.mBottom.getResolutionNode();
        Object obj = (i & 8) == 8 ? 1 : null;
        Object obj2 = (constraintWidget2.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 0)) ? 1 : null;
        if (!(resolutionNode.type == 4 || resolutionNode3.type == 4)) {
            if (constraintWidget2.mListDimensionBehaviors[0] != DimensionBehaviour.FIXED) {
                if (obj2 == null || constraintWidget.getVisibility() != 8) {
                    if (obj2 != null) {
                        int width = constraintWidget.getWidth();
                        resolutionNode.setType(1);
                        resolutionNode3.setType(1);
                        if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                            if (obj != null) {
                                resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                            } else {
                                resolutionNode3.dependsOn(resolutionNode, width);
                            }
                        } else if (constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget != null) {
                            if (constraintWidget2.mLeft.mTarget != null || constraintWidget2.mRight.mTarget == null) {
                                if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                                    if (obj != null) {
                                        constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                                        constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                                    }
                                    if (constraintWidget2.mDimensionRatio == 0.0f) {
                                        resolutionNode.setType(3);
                                        resolutionNode3.setType(3);
                                        resolutionNode.setOpposite(resolutionNode3, 0.0f);
                                        resolutionNode3.setOpposite(resolutionNode, 0.0f);
                                    } else {
                                        resolutionNode.setType(2);
                                        resolutionNode3.setType(2);
                                        resolutionNode.setOpposite(resolutionNode3, (float) (-width));
                                        resolutionNode3.setOpposite(resolutionNode, (float) width);
                                        constraintWidget2.setWidth(width);
                                    }
                                }
                            } else if (obj != null) {
                                resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                            } else {
                                resolutionNode.dependsOn(resolutionNode3, -width);
                            }
                        } else if (obj != null) {
                            resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                        } else {
                            resolutionNode3.dependsOn(resolutionNode, width);
                        }
                    }
                }
            }
            if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (obj != null) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            } else if (constraintWidget2.mLeft.mTarget != null && constraintWidget2.mRight.mTarget == null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                if (obj != null) {
                    resolutionNode3.dependsOn(resolutionNode, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode3.dependsOn(resolutionNode, constraintWidget.getWidth());
                }
            } else if (constraintWidget2.mLeft.mTarget == null && constraintWidget2.mRight.mTarget != null) {
                resolutionNode.setType(1);
                resolutionNode3.setType(1);
                resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                if (obj != null) {
                    resolutionNode.dependsOn(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode.dependsOn(resolutionNode3, -constraintWidget.getWidth());
                }
            } else if (!(constraintWidget2.mLeft.mTarget == null || constraintWidget2.mRight.mTarget == null)) {
                resolutionNode.setType(2);
                resolutionNode3.setType(2);
                if (obj != null) {
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode3);
                    resolutionNode.setOpposite(resolutionNode3, -1, constraintWidget.getResolutionWidth());
                    resolutionNode3.setOpposite(resolutionNode, 1, constraintWidget.getResolutionWidth());
                } else {
                    resolutionNode.setOpposite(resolutionNode3, (float) (-constraintWidget.getWidth()));
                    resolutionNode3.setOpposite(resolutionNode, (float) constraintWidget.getWidth());
                }
            }
        }
        Object obj3 = (constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(constraintWidget2, 1)) ? 1 : null;
        if (resolutionNode2.type != 4 && resolutionNode4.type != 4) {
            if (constraintWidget2.mListDimensionBehaviors[1] != DimensionBehaviour.FIXED) {
                if (obj3 == null || constraintWidget.getVisibility() != 8) {
                    if (obj3 != null) {
                        int height = constraintWidget.getHeight();
                        resolutionNode2.setType(1);
                        resolutionNode4.setType(1);
                        if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                            if (obj != null) {
                                resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                                return;
                            } else {
                                resolutionNode4.dependsOn(resolutionNode2, height);
                                return;
                            }
                        } else if (constraintWidget2.mTop.mTarget == null || constraintWidget2.mBottom.mTarget != null) {
                            if (constraintWidget2.mTop.mTarget != null || constraintWidget2.mBottom.mTarget == null) {
                                if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                                    if (obj != null) {
                                        constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                                        constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                                    }
                                    if (constraintWidget2.mDimensionRatio == 0.0f) {
                                        resolutionNode2.setType(3);
                                        resolutionNode4.setType(3);
                                        resolutionNode2.setOpposite(resolutionNode4, 0.0f);
                                        resolutionNode4.setOpposite(resolutionNode2, 0.0f);
                                        return;
                                    }
                                    resolutionNode2.setType(2);
                                    resolutionNode4.setType(2);
                                    resolutionNode2.setOpposite(resolutionNode4, (float) (-height));
                                    resolutionNode4.setOpposite(resolutionNode2, (float) height);
                                    constraintWidget2.setHeight(height);
                                    if (constraintWidget2.mBaselineDistance > 0) {
                                        constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            } else if (obj != null) {
                                resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                                return;
                            } else {
                                resolutionNode2.dependsOn(resolutionNode4, -height);
                                return;
                            }
                        } else if (obj != null) {
                            resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                            return;
                        } else {
                            resolutionNode4.dependsOn(resolutionNode2, height);
                            return;
                        }
                    }
                    return;
                }
            }
            if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (obj != null) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget2.mBaseline.mTarget != null) {
                    constraintWidget2.mBaseline.getResolutionNode().setType(1);
                    resolutionNode2.dependsOn(1, constraintWidget2.mBaseline.getResolutionNode(), -constraintWidget2.mBaselineDistance);
                }
            } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget == null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (obj != null) {
                    resolutionNode4.dependsOn(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionNode4.dependsOn(resolutionNode2, constraintWidget.getHeight());
                }
                if (constraintWidget2.mBaselineDistance > 0) {
                    constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                }
            } else if (constraintWidget2.mTop.mTarget == null && constraintWidget2.mBottom.mTarget != null) {
                resolutionNode2.setType(1);
                resolutionNode4.setType(1);
                if (obj != null) {
                    resolutionNode2.dependsOn(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                } else {
                    resolutionNode2.dependsOn(resolutionNode4, -constraintWidget.getHeight());
                }
                if (constraintWidget2.mBaselineDistance > 0) {
                    constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                }
            } else if (constraintWidget2.mTop.mTarget != null && constraintWidget2.mBottom.mTarget != null) {
                resolutionNode2.setType(2);
                resolutionNode4.setType(2);
                if (obj != null) {
                    resolutionNode2.setOpposite(resolutionNode4, -1, constraintWidget.getResolutionHeight());
                    resolutionNode4.setOpposite(resolutionNode2, 1, constraintWidget.getResolutionHeight());
                    constraintWidget.getResolutionHeight().addDependent(resolutionNode2);
                    constraintWidget.getResolutionWidth().addDependent(resolutionNode4);
                } else {
                    resolutionNode2.setOpposite(resolutionNode4, (float) (-constraintWidget.getHeight()));
                    resolutionNode4.setOpposite(resolutionNode2, (float) constraintWidget.getHeight());
                }
                if (constraintWidget2.mBaselineDistance > 0) {
                    constraintWidget2.mBaseline.getResolutionNode().dependsOn(1, resolutionNode2, constraintWidget2.mBaselineDistance);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean applyChainOptimized(android.support.constraint.solver.widgets.ConstraintWidgetContainer r21, android.support.constraint.solver.LinearSystem r22, int r23, int r24, android.support.constraint.solver.widgets.ChainHead r25) {
        /*
        r0 = r22;
        r1 = r25;
        r2 = r1.mFirst;
        r3 = r1.mLast;
        r4 = r1.mFirstVisibleWidget;
        r5 = r1.mLastVisibleWidget;
        r6 = r1.mHead;
        r7 = r1.mTotalWeight;
        r8 = r1.mFirstMatchConstraintWidget;
        r1 = r1.mLastMatchConstraintWidget;
        r8 = r21;
        r1 = r8.mListDimensionBehaviors;
        r1 = r1[r23];
        r8 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        r1 = 2;
        r9 = 1;
        if (r23 != 0) goto L_0x0036;
    L_0x0020:
        r10 = r6.mHorizontalChainStyle;
        if (r10 != 0) goto L_0x0026;
    L_0x0024:
        r10 = 1;
        goto L_0x0027;
    L_0x0026:
        r10 = 0;
    L_0x0027:
        r11 = r6.mHorizontalChainStyle;
        if (r11 != r9) goto L_0x002d;
    L_0x002b:
        r11 = 1;
        goto L_0x002e;
    L_0x002d:
        r11 = 0;
    L_0x002e:
        r6 = r6.mHorizontalChainStyle;
        if (r6 != r1) goto L_0x0034;
    L_0x0032:
        r1 = 1;
        goto L_0x0049;
    L_0x0034:
        r1 = 0;
        goto L_0x0049;
    L_0x0036:
        r10 = r6.mVerticalChainStyle;
        if (r10 != 0) goto L_0x003c;
    L_0x003a:
        r10 = 1;
        goto L_0x003d;
    L_0x003c:
        r10 = 0;
    L_0x003d:
        r11 = r6.mVerticalChainStyle;
        if (r11 != r9) goto L_0x0043;
    L_0x0041:
        r11 = 1;
        goto L_0x0044;
    L_0x0043:
        r11 = 0;
    L_0x0044:
        r6 = r6.mVerticalChainStyle;
        if (r6 != r1) goto L_0x0034;
    L_0x0048:
        goto L_0x0032;
    L_0x0049:
        r13 = r2;
        r6 = 0;
        r9 = 0;
        r12 = 0;
        r14 = 0;
        r15 = 0;
    L_0x004f:
        if (r12 != 0) goto L_0x00f2;
    L_0x0051:
        r8 = r13.getVisibility();
        r16 = r12;
        r12 = 8;
        if (r8 == r12) goto L_0x008e;
    L_0x005b:
        r6 = r6 + 1;
        if (r23 != 0) goto L_0x0066;
    L_0x005f:
        r8 = r13.getWidth();
        r8 = (float) r8;
        r14 = r14 + r8;
        goto L_0x006c;
    L_0x0066:
        r8 = r13.getHeight();
        r8 = (float) r8;
        r14 = r14 + r8;
    L_0x006c:
        if (r13 == r4) goto L_0x0078;
    L_0x006e:
        r8 = r13.mListAnchors;
        r8 = r8[r24];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r14 = r14 + r8;
    L_0x0078:
        r8 = r13.mListAnchors;
        r8 = r8[r24];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r15 = r15 + r8;
        r8 = r13.mListAnchors;
        r17 = r24 + 1;
        r8 = r8[r17];
        r8 = r8.getMargin();
        r8 = (float) r8;
        r15 = r15 + r8;
    L_0x008e:
        r8 = r13.mListAnchors;
        r8 = r8[r24];
        r8 = r13.getVisibility();
        if (r8 == r12) goto L_0x00c3;
    L_0x0098:
        r8 = r13.mListDimensionBehaviors;
        r8 = r8[r23];
        r12 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
        if (r8 != r12) goto L_0x00c3;
    L_0x00a0:
        r9 = r9 + 1;
        if (r23 != 0) goto L_0x00b4;
    L_0x00a4:
        r8 = r13.mMatchConstraintDefaultWidth;
        if (r8 == 0) goto L_0x00aa;
    L_0x00a8:
        r8 = 0;
        return r8;
    L_0x00aa:
        r8 = 0;
        r12 = r13.mMatchConstraintMinWidth;
        if (r12 != 0) goto L_0x00b3;
    L_0x00af:
        r12 = r13.mMatchConstraintMaxWidth;
        if (r12 == 0) goto L_0x00c3;
    L_0x00b3:
        return r8;
    L_0x00b4:
        r8 = 0;
        r12 = r13.mMatchConstraintDefaultHeight;
        if (r12 == 0) goto L_0x00ba;
    L_0x00b9:
        return r8;
    L_0x00ba:
        r12 = r13.mMatchConstraintMinHeight;
        if (r12 != 0) goto L_0x00c2;
    L_0x00be:
        r12 = r13.mMatchConstraintMaxHeight;
        if (r12 == 0) goto L_0x00c3;
    L_0x00c2:
        return r8;
    L_0x00c3:
        r8 = r13.mListAnchors;
        r12 = r24 + 1;
        r8 = r8[r12];
        r8 = r8.mTarget;
        if (r8 == 0) goto L_0x00e5;
    L_0x00cd:
        r8 = r8.mOwner;
        r12 = r8.mListAnchors;
        r12 = r12[r24];
        r12 = r12.mTarget;
        if (r12 == 0) goto L_0x00e5;
    L_0x00d7:
        r12 = r8.mListAnchors;
        r12 = r12[r24];
        r12 = r12.mTarget;
        r12 = r12.mOwner;
        if (r12 == r13) goto L_0x00e2;
    L_0x00e1:
        goto L_0x00e5;
    L_0x00e2:
        r18 = r8;
        goto L_0x00e7;
    L_0x00e5:
        r18 = 0;
    L_0x00e7:
        if (r18 == 0) goto L_0x00ef;
    L_0x00e9:
        r12 = r16;
        r13 = r18;
        goto L_0x004f;
    L_0x00ef:
        r12 = 1;
        goto L_0x004f;
    L_0x00f2:
        r8 = r2.mListAnchors;
        r8 = r8[r24];
        r8 = r8.getResolutionNode();
        r3 = r3.mListAnchors;
        r12 = r24 + 1;
        r3 = r3[r12];
        r3 = r3.getResolutionNode();
        r19 = r2;
        r2 = r8.target;
        if (r2 == 0) goto L_0x035d;
    L_0x010a:
        r2 = r3.target;
        if (r2 != 0) goto L_0x0110;
    L_0x010e:
        goto L_0x035d;
    L_0x0110:
        r2 = r8.target;
        r2 = r2.state;
        r0 = 1;
        if (r2 == r0) goto L_0x011f;
    L_0x0117:
        r2 = r3.target;
        r2 = r2.state;
        if (r2 == r0) goto L_0x011f;
    L_0x011d:
        r0 = 0;
        return r0;
    L_0x011f:
        r0 = 0;
        if (r9 <= 0) goto L_0x0125;
    L_0x0122:
        if (r9 == r6) goto L_0x0125;
    L_0x0124:
        return r0;
    L_0x0125:
        if (r1 != 0) goto L_0x012e;
    L_0x0127:
        if (r10 != 0) goto L_0x012e;
    L_0x0129:
        if (r11 == 0) goto L_0x012c;
    L_0x012b:
        goto L_0x012e;
    L_0x012c:
        r0 = 0;
        goto L_0x0147;
    L_0x012e:
        if (r4 == 0) goto L_0x013a;
    L_0x0130:
        r0 = r4.mListAnchors;
        r0 = r0[r24];
        r0 = r0.getMargin();
        r0 = (float) r0;
        goto L_0x013b;
    L_0x013a:
        r0 = 0;
    L_0x013b:
        if (r5 == 0) goto L_0x0147;
    L_0x013d:
        r2 = r5.mListAnchors;
        r2 = r2[r12];
        r2 = r2.getMargin();
        r2 = (float) r2;
        r0 = r0 + r2;
    L_0x0147:
        r2 = r8.target;
        r2 = r2.resolvedOffset;
        r3 = r3.target;
        r3 = r3.resolvedOffset;
        r16 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r16 >= 0) goto L_0x0156;
    L_0x0153:
        r3 = r3 - r2;
        r3 = r3 - r14;
        goto L_0x0159;
    L_0x0156:
        r3 = r2 - r3;
        r3 = r3 - r14;
    L_0x0159:
        r16 = 1;
        if (r9 <= 0) goto L_0x0222;
    L_0x015d:
        if (r9 != r6) goto L_0x0222;
    L_0x015f:
        r1 = r13.getParent();
        if (r1 == 0) goto L_0x0173;
    L_0x0165:
        r1 = r13.getParent();
        r1 = r1.mListDimensionBehaviors;
        r1 = r1[r23];
        r6 = android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (r1 != r6) goto L_0x0173;
    L_0x0171:
        r1 = 0;
        return r1;
    L_0x0173:
        r3 = r3 + r14;
        r3 = r3 - r15;
        if (r10 == 0) goto L_0x0179;
    L_0x0177:
        r15 = r15 - r0;
        r3 = r3 - r15;
    L_0x0179:
        if (r10 == 0) goto L_0x0195;
    L_0x017b:
        r0 = r4.mListAnchors;
        r0 = r0[r12];
        r0 = r0.getMargin();
        r0 = (float) r0;
        r2 = r2 + r0;
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r23];
        if (r0 == 0) goto L_0x0195;
    L_0x018b:
        r0 = r0.mListAnchors;
        r0 = r0[r24];
        r0 = r0.getMargin();
        r0 = (float) r0;
        r2 = r2 + r0;
    L_0x0195:
        if (r4 == 0) goto L_0x0220;
    L_0x0197:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r0 == 0) goto L_0x01b6;
    L_0x019b:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.nonresolvedWidgets;
        r20 = r12;
        r12 = r10 - r16;
        r0.nonresolvedWidgets = r12;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.resolvedWidgets;
        r12 = r10 + r16;
        r0.resolvedWidgets = r12;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r10 = r0.chainConnectionResolved;
        r12 = r10 + r16;
        r0.chainConnectionResolved = r12;
        goto L_0x01b8;
    L_0x01b6:
        r20 = r12;
    L_0x01b8:
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r23];
        if (r0 != 0) goto L_0x01c5;
    L_0x01be:
        if (r4 != r5) goto L_0x01c1;
    L_0x01c0:
        goto L_0x01c5;
    L_0x01c1:
        r6 = 0;
        r12 = r22;
        goto L_0x021b;
    L_0x01c5:
        r1 = (float) r9;
        r1 = r3 / r1;
        r6 = 0;
        r10 = (r7 > r6 ? 1 : (r7 == r6 ? 0 : -1));
        if (r10 <= 0) goto L_0x01d4;
    L_0x01cd:
        r1 = r4.mWeight;
        r1 = r1[r23];
        r1 = r1 * r3;
        r1 = r1 / r7;
    L_0x01d4:
        r10 = r4.mListAnchors;
        r10 = r10[r24];
        r10 = r10.getMargin();
        r10 = (float) r10;
        r2 = r2 + r10;
        r10 = r4.mListAnchors;
        r10 = r10[r24];
        r10 = r10.getResolutionNode();
        r11 = r8.resolvedTarget;
        r10.resolve(r11, r2);
        r10 = r4.mListAnchors;
        r10 = r10[r20];
        r10 = r10.getResolutionNode();
        r11 = r8.resolvedTarget;
        r2 = r2 + r1;
        r10.resolve(r11, r2);
        r1 = r4.mListAnchors;
        r1 = r1[r24];
        r1 = r1.getResolutionNode();
        r12 = r22;
        r1.addResolvedValue(r12);
        r1 = r4.mListAnchors;
        r1 = r1[r20];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r12);
        r1 = r4.mListAnchors;
        r1 = r1[r20];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r2 = r2 + r1;
    L_0x021b:
        r4 = r0;
        r12 = r20;
        goto L_0x0195;
    L_0x0220:
        r0 = 1;
        return r0;
    L_0x0222:
        r20 = r12;
        r12 = r22;
        r7 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));
        if (r7 >= 0) goto L_0x022c;
    L_0x022a:
        r7 = 0;
        return r7;
    L_0x022c:
        if (r1 == 0) goto L_0x02b5;
    L_0x022e:
        r3 = r3 - r0;
        r0 = r19;
        r0 = r0.getHorizontalBiasPercent();
        r3 = r3 * r0;
        r2 = r2 + r3;
    L_0x0238:
        if (r4 == 0) goto L_0x02b2;
    L_0x023a:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r0 == 0) goto L_0x0256;
    L_0x023e:
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.nonresolvedWidgets;
        r9 = r6 - r16;
        r0.nonresolvedWidgets = r9;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.resolvedWidgets;
        r9 = r6 + r16;
        r0.resolvedWidgets = r9;
        r0 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r0.chainConnectionResolved;
        r9 = r6 + r16;
        r0.chainConnectionResolved = r9;
    L_0x0256:
        r0 = r4.mListNextVisibleWidget;
        r0 = r0[r23];
        if (r0 != 0) goto L_0x025e;
    L_0x025c:
        if (r4 != r5) goto L_0x02b0;
    L_0x025e:
        if (r23 != 0) goto L_0x0266;
    L_0x0260:
        r1 = r4.getWidth();
        r1 = (float) r1;
        goto L_0x026b;
    L_0x0266:
        r1 = r4.getHeight();
        r1 = (float) r1;
    L_0x026b:
        r3 = r4.mListAnchors;
        r3 = r3[r24];
        r3 = r3.getMargin();
        r3 = (float) r3;
        r2 = r2 + r3;
        r3 = r4.mListAnchors;
        r3 = r3[r24];
        r3 = r3.getResolutionNode();
        r6 = r8.resolvedTarget;
        r3.resolve(r6, r2);
        r3 = r4.mListAnchors;
        r3 = r3[r20];
        r3 = r3.getResolutionNode();
        r6 = r8.resolvedTarget;
        r2 = r2 + r1;
        r3.resolve(r6, r2);
        r1 = r4.mListAnchors;
        r1 = r1[r24];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r12);
        r1 = r4.mListAnchors;
        r1 = r1[r20];
        r1 = r1.getResolutionNode();
        r1.addResolvedValue(r12);
        r1 = r4.mListAnchors;
        r1 = r1[r20];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r2 = r2 + r1;
    L_0x02b0:
        r4 = r0;
        goto L_0x0238;
    L_0x02b2:
        r0 = 1;
        goto L_0x035c;
    L_0x02b5:
        if (r10 != 0) goto L_0x02b9;
    L_0x02b7:
        if (r11 == 0) goto L_0x02b2;
    L_0x02b9:
        if (r10 == 0) goto L_0x02bd;
    L_0x02bb:
        r3 = r3 - r0;
        goto L_0x02c0;
    L_0x02bd:
        if (r11 == 0) goto L_0x02c0;
    L_0x02bf:
        r3 = r3 - r0;
    L_0x02c0:
        r0 = r6 + 1;
        r0 = (float) r0;
        r0 = r3 / r0;
        if (r11 == 0) goto L_0x02d4;
    L_0x02c7:
        r1 = 1;
        if (r6 <= r1) goto L_0x02d0;
    L_0x02ca:
        r0 = r6 + -1;
        r0 = (float) r0;
        r0 = r3 / r0;
        goto L_0x02d4;
    L_0x02d0:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r3 / r0;
    L_0x02d4:
        r1 = r2 + r0;
        if (r11 == 0) goto L_0x02e5;
    L_0x02d8:
        r3 = 1;
        if (r6 <= r3) goto L_0x02e5;
    L_0x02db:
        r1 = r4.mListAnchors;
        r1 = r1[r24];
        r1 = r1.getMargin();
        r1 = (float) r1;
        r1 = r1 + r2;
    L_0x02e5:
        if (r10 == 0) goto L_0x02f3;
    L_0x02e7:
        if (r4 == 0) goto L_0x02f3;
    L_0x02e9:
        r2 = r4.mListAnchors;
        r2 = r2[r24];
        r2 = r2.getMargin();
        r2 = (float) r2;
        r1 = r1 + r2;
    L_0x02f3:
        if (r4 == 0) goto L_0x02b2;
    L_0x02f5:
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        if (r2 == 0) goto L_0x0311;
    L_0x02f9:
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.nonresolvedWidgets;
        r9 = r6 - r16;
        r2.nonresolvedWidgets = r9;
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.resolvedWidgets;
        r9 = r6 + r16;
        r2.resolvedWidgets = r9;
        r2 = android.support.constraint.solver.LinearSystem.sMetrics;
        r6 = r2.chainConnectionResolved;
        r9 = r6 + r16;
        r2.chainConnectionResolved = r9;
    L_0x0311:
        r2 = r4.mListNextVisibleWidget;
        r2 = r2[r23];
        if (r2 != 0) goto L_0x0319;
    L_0x0317:
        if (r4 != r5) goto L_0x035a;
    L_0x0319:
        if (r23 != 0) goto L_0x0321;
    L_0x031b:
        r3 = r4.getWidth();
        r3 = (float) r3;
        goto L_0x0326;
    L_0x0321:
        r3 = r4.getHeight();
        r3 = (float) r3;
    L_0x0326:
        r6 = r4.mListAnchors;
        r6 = r6[r24];
        r6 = r6.getResolutionNode();
        r7 = r8.resolvedTarget;
        r6.resolve(r7, r1);
        r6 = r4.mListAnchors;
        r6 = r6[r20];
        r6 = r6.getResolutionNode();
        r7 = r8.resolvedTarget;
        r9 = r1 + r3;
        r6.resolve(r7, r9);
        r6 = r4.mListAnchors;
        r6 = r6[r24];
        r6 = r6.getResolutionNode();
        r6.addResolvedValue(r12);
        r4 = r4.mListAnchors;
        r4 = r4[r20];
        r4 = r4.getResolutionNode();
        r4.addResolvedValue(r12);
        r3 = r3 + r0;
        r1 = r1 + r3;
    L_0x035a:
        r4 = r2;
        goto L_0x02f3;
    L_0x035c:
        return r0;
    L_0x035d:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.Optimizer.applyChainOptimized(android.support.constraint.solver.widgets.ConstraintWidgetContainer, android.support.constraint.solver.LinearSystem, int, int, android.support.constraint.solver.widgets.ChainHead):boolean");
    }
}
