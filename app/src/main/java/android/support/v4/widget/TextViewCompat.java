package android.support.v4.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.StyleRes;
import android.support.v4.os.BuildCompat;
import android.text.Editable;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import fi.iki.elonen.NanoHTTPD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class TextViewCompat {
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    static final TextViewCompatBaseImpl IMPL;

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AutoSizeTextType {
    }

    static class TextViewCompatBaseImpl {
        private static final int LINES = 1;
        private static final String LOG_TAG = "TextViewCompatBase";
        private static Field sMaxModeField;
        private static boolean sMaxModeFieldFetched;
        private static Field sMaximumField;
        private static boolean sMaximumFieldFetched;
        private static Field sMinModeField;
        private static boolean sMinModeFieldFetched;
        private static Field sMinimumField;
        private static boolean sMinimumFieldFetched;

        TextViewCompatBaseImpl() {
        }

        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
        }

        private static java.lang.reflect.Field retrieveField(java.lang.String r4) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r0 = 0;
            r1 = android.widget.TextView.class;	 Catch:{ NoSuchFieldException -> 0x000c }
            r1 = r1.getDeclaredField(r4);	 Catch:{ NoSuchFieldException -> 0x000c }
            r0 = 1;
            r1.setAccessible(r0);	 Catch:{ NoSuchFieldException -> 0x000d }
            goto L_0x0028;
        L_0x000c:
            r1 = r0;
        L_0x000d:
            r0 = "TextViewCompatBase";
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r3 = "Could not retrieve ";
            r2.append(r3);
            r2.append(r4);
            r4 = " field.";
            r2.append(r4);
            r4 = r2.toString();
            android.util.Log.e(r0, r4);
        L_0x0028:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.TextViewCompat.TextViewCompatBaseImpl.retrieveField(java.lang.String):java.lang.reflect.Field");
        }

        private static int retrieveIntFromField(java.lang.reflect.Field r2, android.widget.TextView r3) {
            /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r3 = r2.getInt(r3);	 Catch:{ IllegalAccessException -> 0x0005 }
            return r3;
        L_0x0005:
            r3 = "TextViewCompatBase";
            r0 = new java.lang.StringBuilder;
            r0.<init>();
            r1 = "Could not retrieve value of ";
            r0.append(r1);
            r2 = r2.getName();
            r0.append(r2);
            r2 = " field.";
            r0.append(r2);
            r2 = r0.toString();
            android.util.Log.d(r3, r2);
            r2 = -1;
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.TextViewCompat.TextViewCompatBaseImpl.retrieveIntFromField(java.lang.reflect.Field, android.widget.TextView):int");
        }

        public int getMaxLines(TextView textView) {
            if (!sMaxModeFieldFetched) {
                sMaxModeField = retrieveField("mMaxMode");
                sMaxModeFieldFetched = true;
            }
            if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, textView) == 1) {
                if (!sMaximumFieldFetched) {
                    sMaximumField = retrieveField("mMaximum");
                    sMaximumFieldFetched = true;
                }
                if (sMaximumField != null) {
                    return retrieveIntFromField(sMaximumField, textView);
                }
            }
            return -1;
        }

        public int getMinLines(TextView textView) {
            if (!sMinModeFieldFetched) {
                sMinModeField = retrieveField("mMinMode");
                sMinModeFieldFetched = true;
            }
            if (sMinModeField != null && retrieveIntFromField(sMinModeField, textView) == 1) {
                if (!sMinimumFieldFetched) {
                    sMinimumField = retrieveField("mMinimum");
                    sMinimumFieldFetched = true;
                }
                if (sMinimumField != null) {
                    return retrieveIntFromField(sMinimumField, textView);
                }
            }
            return -1;
        }

        public void setTextAppearance(TextView textView, @StyleRes int i) {
            textView.setTextAppearance(textView.getContext(), i);
        }

        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return textView.getCompoundDrawables();
        }

        public void setAutoSizeTextTypeWithDefaults(TextView textView, int i) {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView) textView).setAutoSizeTextTypeWithDefaults(i);
            }
        }

        public void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int i, int i2, int i3, int i4) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView) textView).setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
            }
        }

        public void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, @NonNull int[] iArr, int i) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView) textView).setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
            }
        }

        public int getAutoSizeTextType(TextView textView) {
            return textView instanceof AutoSizeableTextView ? ((AutoSizeableTextView) textView).getAutoSizeTextType() : null;
        }

        public int getAutoSizeStepGranularity(TextView textView) {
            return textView instanceof AutoSizeableTextView ? ((AutoSizeableTextView) textView).getAutoSizeStepGranularity() : -1;
        }

        public int getAutoSizeMinTextSize(TextView textView) {
            return textView instanceof AutoSizeableTextView ? ((AutoSizeableTextView) textView).getAutoSizeMinTextSize() : -1;
        }

        public int getAutoSizeMaxTextSize(TextView textView) {
            return textView instanceof AutoSizeableTextView ? ((AutoSizeableTextView) textView).getAutoSizeMaxTextSize() : -1;
        }

        public int[] getAutoSizeTextAvailableSizes(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView) textView).getAutoSizeTextAvailableSizes();
            }
            return new int[null];
        }

        public void setCustomSelectionActionModeCallback(TextView textView, Callback callback) {
            textView.setCustomSelectionActionModeCallback(callback);
        }
    }

    @RequiresApi(16)
    static class TextViewCompatApi16Impl extends TextViewCompatBaseImpl {
        TextViewCompatApi16Impl() {
        }

        public int getMaxLines(TextView textView) {
            return textView.getMaxLines();
        }

        public int getMinLines(TextView textView) {
            return textView.getMinLines();
        }
    }

    @RequiresApi(17)
    static class TextViewCompatApi17Impl extends TextViewCompatApi16Impl {
        TextViewCompatApi17Impl() {
        }

        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            Object obj = 1;
            if (textView.getLayoutDirection() != 1) {
                obj = null;
            }
            Drawable drawable5 = obj != null ? drawable3 : drawable;
            if (obj == null) {
                drawable = drawable3;
            }
            textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            Object obj = 1;
            if (textView.getLayoutDirection() != 1) {
                obj = null;
            }
            Drawable drawable5 = obj != null ? drawable3 : drawable;
            if (obj == null) {
                drawable = drawable3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4) {
            Object obj = 1;
            if (textView.getLayoutDirection() != 1) {
                obj = null;
            }
            int i5 = obj != null ? i3 : i;
            if (obj == null) {
                i = i3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(i5, i2, i, i4);
        }

        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            Object obj = 1;
            if (textView.getLayoutDirection() != 1) {
                obj = null;
            }
            textView = textView.getCompoundDrawables();
            if (obj != null) {
                obj = textView[2];
                Object obj2 = textView[0];
                textView[0] = obj;
                textView[2] = obj2;
            }
            return textView;
        }
    }

    @RequiresApi(18)
    static class TextViewCompatApi18Impl extends TextViewCompatApi17Impl {
        TextViewCompatApi18Impl() {
        }

        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            textView.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(i, i2, i3, i4);
        }

        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }
    }

    @RequiresApi(23)
    static class TextViewCompatApi23Impl extends TextViewCompatApi18Impl {
        TextViewCompatApi23Impl() {
        }

        public void setTextAppearance(@NonNull TextView textView, @StyleRes int i) {
            textView.setTextAppearance(i);
        }
    }

    @RequiresApi(26)
    static class TextViewCompatApi26Impl extends TextViewCompatApi23Impl {
        TextViewCompatApi26Impl() {
        }

        public void setCustomSelectionActionModeCallback(final TextView textView, final Callback callback) {
            if (VERSION.SDK_INT == 26 || VERSION.SDK_INT == 27) {
                textView.setCustomSelectionActionModeCallback(new Callback() {
                    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
                    private boolean mCanUseMenuBuilderReferences;
                    private boolean mInitializedMenuBuilderReferences = null;
                    private Class mMenuBuilderClass;
                    private Method mMenuBuilderRemoveItemAtMethod;

                    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                        return callback.onCreateActionMode(actionMode, menu);
                    }

                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        recomputeProcessTextMenuItems(menu);
                        return callback.onPrepareActionMode(actionMode, menu);
                    }

                    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                        return callback.onActionItemClicked(actionMode, menuItem);
                    }

                    public void onDestroyActionMode(ActionMode actionMode) {
                        callback.onDestroyActionMode(actionMode);
                    }

                    private void recomputeProcessTextMenuItems(android.view.Menu r9) {
                        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
                        /*
                        r8 = this;
                        r0 = r3;
                        r0 = r0.getContext();
                        r1 = r0.getPackageManager();
                        r2 = r8.mInitializedMenuBuilderReferences;
                        r3 = 0;
                        r4 = 1;
                        if (r2 != 0) goto L_0x0034;
                    L_0x0010:
                        r8.mInitializedMenuBuilderReferences = r4;
                        r2 = "com.android.internal.view.menu.MenuBuilder";	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r2 = java.lang.Class.forName(r2);	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r8.mMenuBuilderClass = r2;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r2 = r8.mMenuBuilderClass;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r5 = "removeItemAt";	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r6 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r7 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r6[r3] = r7;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r2 = r2.getDeclaredMethod(r5, r6);	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r8.mMenuBuilderRemoveItemAtMethod = r2;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        r8.mCanUseMenuBuilderReferences = r4;	 Catch:{ ClassNotFoundException -> 0x002d, ClassNotFoundException -> 0x002d }
                        goto L_0x0034;
                    L_0x002d:
                        r2 = 0;
                        r8.mMenuBuilderClass = r2;
                        r8.mMenuBuilderRemoveItemAtMethod = r2;
                        r8.mCanUseMenuBuilderReferences = r3;
                    L_0x0034:
                        r2 = r8.mCanUseMenuBuilderReferences;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        if (r2 == 0) goto L_0x0043;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0038:
                        r2 = r8.mMenuBuilderClass;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r2 = r2.isInstance(r9);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        if (r2 == 0) goto L_0x0043;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0040:
                        r2 = r8.mMenuBuilderRemoveItemAtMethod;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        goto L_0x0053;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0043:
                        r2 = r9.getClass();	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r5 = "removeItemAt";	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6 = new java.lang.Class[r4];	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r7 = java.lang.Integer.TYPE;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6[r3] = r7;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r2 = r2.getDeclaredMethod(r5, r6);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0053:
                        r5 = r9.size();	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r5 = r5 - r4;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0058:
                        if (r5 < 0) goto L_0x0082;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x005a:
                        r6 = r9.getItem(r5);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r7 = r6.getIntent();	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        if (r7 == 0) goto L_0x007f;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0064:
                        r7 = "android.intent.action.PROCESS_TEXT";	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6 = r6.getIntent();	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6 = r6.getAction();	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6 = r7.equals(r6);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        if (r6 == 0) goto L_0x007f;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x0074:
                        r6 = new java.lang.Object[r4];	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r7 = java.lang.Integer.valueOf(r5);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r6[r3] = r7;	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                        r2.invoke(r9, r6);	 Catch:{ NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae, NoSuchMethodException -> 0x00ae }
                    L_0x007f:
                        r5 = r5 + -1;
                        goto L_0x0058;
                    L_0x0082:
                        r0 = r8.getSupportedActivities(r0, r1);
                        r2 = 0;
                    L_0x0087:
                        r5 = r0.size();
                        if (r2 >= r5) goto L_0x00ad;
                    L_0x008d:
                        r5 = r0.get(r2);
                        r5 = (android.content.pm.ResolveInfo) r5;
                        r6 = r2 + 100;
                        r7 = r5.loadLabel(r1);
                        r6 = r9.add(r3, r3, r6, r7);
                        r7 = r3;
                        r5 = r8.createProcessTextIntentForResolveInfo(r5, r7);
                        r5 = r6.setIntent(r5);
                        r5.setShowAsAction(r4);
                        r2 = r2 + 1;
                        goto L_0x0087;
                    L_0x00ad:
                        return;
                    L_0x00ae:
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.TextViewCompat.TextViewCompatApi26Impl.1.recomputeProcessTextMenuItems(android.view.Menu):void");
                    }

                    private List<ResolveInfo> getSupportedActivities(Context context, PackageManager packageManager) {
                        List<ResolveInfo> arrayList = new ArrayList();
                        if (!(context instanceof Activity)) {
                            return arrayList;
                        }
                        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(createProcessTextIntent(), 0)) {
                            if (isSupportedActivity(resolveInfo, context)) {
                                arrayList.add(resolveInfo);
                            }
                        }
                        return arrayList;
                    }

                    private boolean isSupportedActivity(ResolveInfo resolveInfo, Context context) {
                        boolean z = true;
                        if (context.getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                            return true;
                        }
                        if (!resolveInfo.activityInfo.exported) {
                            return false;
                        }
                        if (resolveInfo.activityInfo.permission != null) {
                            if (context.checkSelfPermission(resolveInfo.activityInfo.permission) != null) {
                                z = false;
                            }
                        }
                        return z;
                    }

                    private Intent createProcessTextIntentForResolveInfo(ResolveInfo resolveInfo, TextView textView) {
                        return createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", isEditable(textView) ^ 1).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                    }

                    private boolean isEditable(TextView textView) {
                        return ((textView instanceof Editable) && textView.onCheckIsTextEditor() && textView.isEnabled() != null) ? true : null;
                    }

                    private Intent createProcessTextIntent() {
                        return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType(NanoHTTPD.MIME_PLAINTEXT);
                    }
                });
            } else {
                super.setCustomSelectionActionModeCallback(textView, callback);
            }
        }
    }

    @RequiresApi(27)
    static class TextViewCompatApi27Impl extends TextViewCompatApi26Impl {
        TextViewCompatApi27Impl() {
        }

        public void setAutoSizeTextTypeWithDefaults(TextView textView, int i) {
            textView.setAutoSizeTextTypeWithDefaults(i);
        }

        public void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int i, int i2, int i3, int i4) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
        }

        public void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, @NonNull int[] iArr, int i) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
        }

        public int getAutoSizeTextType(TextView textView) {
            return textView.getAutoSizeTextType();
        }

        public int getAutoSizeStepGranularity(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        public int getAutoSizeMinTextSize(TextView textView) {
            return textView.getAutoSizeMinTextSize();
        }

        public int getAutoSizeMaxTextSize(TextView textView) {
            return textView.getAutoSizeMaxTextSize();
        }

        public int[] getAutoSizeTextAvailableSizes(TextView textView) {
            return textView.getAutoSizeTextAvailableSizes();
        }
    }

    private TextViewCompat() {
    }

    static {
        if (BuildCompat.isAtLeastOMR1()) {
            IMPL = new TextViewCompatApi27Impl();
        } else if (VERSION.SDK_INT >= 26) {
            IMPL = new TextViewCompatApi26Impl();
        } else if (VERSION.SDK_INT >= 23) {
            IMPL = new TextViewCompatApi23Impl();
        } else if (VERSION.SDK_INT >= 18) {
            IMPL = new TextViewCompatApi18Impl();
        } else if (VERSION.SDK_INT >= 17) {
            IMPL = new TextViewCompatApi17Impl();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new TextViewCompatApi16Impl();
        } else {
            IMPL = new TextViewCompatBaseImpl();
        }
    }

    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        IMPL.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, drawable2, drawable3, drawable4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, i, i2, i3, i4);
    }

    public static int getMaxLines(@NonNull TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static int getMinLines(@NonNull TextView textView) {
        return IMPL.getMinLines(textView);
    }

    public static void setTextAppearance(@NonNull TextView textView, @StyleRes int i) {
        IMPL.setTextAppearance(textView, i);
    }

    @NonNull
    public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
        return IMPL.getCompoundDrawablesRelative(textView);
    }

    public static void setAutoSizeTextTypeWithDefaults(@NonNull TextView textView, int i) {
        IMPL.setAutoSizeTextTypeWithDefaults(textView, i);
    }

    public static void setAutoSizeTextTypeUniformWithConfiguration(@NonNull TextView textView, int i, int i2, int i3, int i4) throws IllegalArgumentException {
        IMPL.setAutoSizeTextTypeUniformWithConfiguration(textView, i, i2, i3, i4);
    }

    public static void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull TextView textView, @NonNull int[] iArr, int i) throws IllegalArgumentException {
        IMPL.setAutoSizeTextTypeUniformWithPresetSizes(textView, iArr, i);
    }

    public static int getAutoSizeTextType(@NonNull TextView textView) {
        return IMPL.getAutoSizeTextType(textView);
    }

    public static int getAutoSizeStepGranularity(@NonNull TextView textView) {
        return IMPL.getAutoSizeStepGranularity(textView);
    }

    public static int getAutoSizeMinTextSize(@NonNull TextView textView) {
        return IMPL.getAutoSizeMinTextSize(textView);
    }

    public static int getAutoSizeMaxTextSize(@NonNull TextView textView) {
        return IMPL.getAutoSizeMaxTextSize(textView);
    }

    @NonNull
    public static int[] getAutoSizeTextAvailableSizes(@NonNull TextView textView) {
        return IMPL.getAutoSizeTextAvailableSizes(textView);
    }

    public static void setCustomSelectionActionModeCallback(@NonNull TextView textView, @NonNull Callback callback) {
        IMPL.setCustomSelectionActionModeCallback(textView, callback);
    }
}
