package com.google.android.exoplayer2.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.Util;

final class SubtitlePainter {
    private static final float INNER_PADDING_RATIO = 0.125f;
    private static final String TAG = "SubtitlePainter";
    private boolean applyEmbeddedFontSizes;
    private boolean applyEmbeddedStyles;
    private int backgroundColor;
    private Rect bitmapRect;
    private float bottomPaddingFraction;
    private final float cornerRadius;
    private Bitmap cueBitmap;
    private float cueBitmapHeight;
    private float cueLine;
    private int cueLineAnchor;
    private int cueLineType;
    private float cuePosition;
    private int cuePositionAnchor;
    private float cueSize;
    private CharSequence cueText;
    private Alignment cueTextAlignment;
    private int edgeColor;
    private int edgeType;
    private int foregroundColor;
    private final RectF lineBounds = new RectF();
    private final float outlineWidth;
    private final Paint paint;
    private int parentBottom;
    private int parentLeft;
    private int parentRight;
    private int parentTop;
    private final float shadowOffset;
    private final float shadowRadius;
    private final float spacingAdd;
    private final float spacingMult;
    private StaticLayout textLayout;
    private int textLeft;
    private int textPaddingX;
    private final TextPaint textPaint;
    private float textSizePx;
    private int textTop;
    private int windowColor;

    public SubtitlePainter(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, new int[]{16843287, 16843288}, 0, 0);
        this.spacingAdd = (float) obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.spacingMult = obtainStyledAttributes.getFloat(1, 1.0f);
        obtainStyledAttributes.recycle();
        context = (float) Math.round((((float) context.getResources().getDisplayMetrics().densityDpi) * 2.0f) / 160.0f);
        this.cornerRadius = context;
        this.outlineWidth = context;
        this.shadowRadius = context;
        this.shadowOffset = context;
        this.textPaint = new TextPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setSubpixelText(true);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Style.FILL);
    }

    public void draw(Cue cue, boolean z, boolean z2, CaptionStyleCompat captionStyleCompat, float f, float f2, Canvas canvas, int i, int i2, int i3, int i4) {
        boolean z3 = cue.bitmap == null;
        int i5 = ViewCompat.MEASURED_STATE_MASK;
        if (z3) {
            if (!TextUtils.isEmpty(cue.text)) {
                i5 = (cue.windowColorSet && z) ? cue.windowColor : captionStyleCompat.windowColor;
            } else {
                return;
            }
        }
        if (areCharSequencesEqual(this.cueText, cue.text) && Util.areEqual(this.cueTextAlignment, cue.textAlignment) && this.cueBitmap == cue.bitmap && this.cueLine == cue.line && this.cueLineType == cue.lineType && Util.areEqual(Integer.valueOf(this.cueLineAnchor), Integer.valueOf(cue.lineAnchor)) && this.cuePosition == cue.position && Util.areEqual(Integer.valueOf(this.cuePositionAnchor), Integer.valueOf(cue.positionAnchor)) && this.cueSize == cue.size && this.cueBitmapHeight == cue.bitmapHeight && this.applyEmbeddedStyles == z && this.applyEmbeddedFontSizes == z2 && this.foregroundColor == captionStyleCompat.foregroundColor && this.backgroundColor == captionStyleCompat.backgroundColor && this.windowColor == i5 && this.edgeType == captionStyleCompat.edgeType && this.edgeColor == captionStyleCompat.edgeColor && Util.areEqual(this.textPaint.getTypeface(), captionStyleCompat.typeface) && this.textSizePx == f && this.bottomPaddingFraction == f2 && this.parentLeft == i && this.parentTop == i2 && this.parentRight == i3 && this.parentBottom == i4) {
            drawLayout(canvas, z3);
            return;
        }
        this.cueText = cue.text;
        this.cueTextAlignment = cue.textAlignment;
        this.cueBitmap = cue.bitmap;
        this.cueLine = cue.line;
        this.cueLineType = cue.lineType;
        this.cueLineAnchor = cue.lineAnchor;
        this.cuePosition = cue.position;
        this.cuePositionAnchor = cue.positionAnchor;
        this.cueSize = cue.size;
        this.cueBitmapHeight = cue.bitmapHeight;
        this.applyEmbeddedStyles = z;
        this.applyEmbeddedFontSizes = z2;
        this.foregroundColor = captionStyleCompat.foregroundColor;
        this.backgroundColor = captionStyleCompat.backgroundColor;
        this.windowColor = i5;
        this.edgeType = captionStyleCompat.edgeType;
        this.edgeColor = captionStyleCompat.edgeColor;
        this.textPaint.setTypeface(captionStyleCompat.typeface);
        this.textSizePx = f;
        this.bottomPaddingFraction = f2;
        this.parentLeft = i;
        this.parentTop = i2;
        this.parentRight = i3;
        this.parentBottom = i4;
        if (z3) {
            setupTextLayout();
        } else {
            setupBitmapLayout();
        }
        drawLayout(canvas, z3);
    }

    private void setupTextLayout() {
        int i = this.parentRight - this.parentLeft;
        int i2 = this.parentBottom - this.parentTop;
        this.textPaint.setTextSize(this.textSizePx);
        int i3 = (int) ((this.textSizePx * INNER_PADDING_RATIO) + 0.5f);
        int i4 = i3 * 2;
        int i5 = i - i4;
        if (this.cueSize != Float.MIN_VALUE) {
            i5 = (int) (((float) i5) * r0.cueSize);
        }
        if (i5 <= 0) {
            Log.w(TAG, "Skipped drawing subtitle cue (insufficient space)");
            return;
        }
        CharSequence charSequence;
        int length;
        int i6;
        if (r0.applyEmbeddedFontSizes && r0.applyEmbeddedStyles) {
            charSequence = r0.cueText;
        } else if (r0.applyEmbeddedStyles) {
            charSequence = new SpannableStringBuilder(r0.cueText);
            length = charSequence.length();
            AbsoluteSizeSpan[] absoluteSizeSpanArr = (AbsoluteSizeSpan[]) charSequence.getSpans(0, length, AbsoluteSizeSpan.class);
            RelativeSizeSpan[] relativeSizeSpanArr = (RelativeSizeSpan[]) charSequence.getSpans(0, length, RelativeSizeSpan.class);
            for (Object removeSpan : absoluteSizeSpanArr) {
                charSequence.removeSpan(removeSpan);
            }
            for (Object removeSpan2 : relativeSizeSpanArr) {
                charSequence.removeSpan(removeSpan2);
            }
        } else {
            charSequence = r0.cueText.toString();
        }
        CharSequence charSequence2 = charSequence;
        Alignment alignment = r0.cueTextAlignment == null ? Alignment.ALIGN_CENTER : r0.cueTextAlignment;
        r0.textLayout = new StaticLayout(charSequence2, r0.textPaint, i5, alignment, r0.spacingMult, r0.spacingAdd, true);
        int height = r0.textLayout.getHeight();
        length = r0.textLayout.getLineCount();
        int i7 = 0;
        for (i6 = 0; i6 < length; i6++) {
            i7 = Math.max((int) Math.ceil((double) r0.textLayout.getLineWidth(i6)), i7);
        }
        if (r0.cueSize == Float.MIN_VALUE || i7 >= i5) {
            i5 = i7;
        }
        i5 += i4;
        if (r0.cuePosition != Float.MIN_VALUE) {
            i = Math.round(((float) i) * r0.cuePosition) + r0.parentLeft;
            if (r0.cuePositionAnchor == 2) {
                i -= i5;
            } else if (r0.cuePositionAnchor == 1) {
                i = ((i * 2) - i5) / 2;
            }
            i = Math.max(i, r0.parentLeft);
            i4 = Math.min(i5 + i, r0.parentRight);
        } else {
            i = (i - i5) / 2;
            i4 = i + i5;
        }
        int i8 = i4 - i;
        if (i8 <= 0) {
            Log.w(TAG, "Skipped drawing subtitle cue (invalid horizontal positioning)");
            return;
        }
        if (r0.cueLine != Float.MIN_VALUE) {
            if (r0.cueLineType == 0) {
                i2 = Math.round(((float) i2) * r0.cueLine) + r0.parentTop;
            } else {
                i2 = r0.textLayout.getLineBottom(0) - r0.textLayout.getLineTop(0);
                if (r0.cueLine >= 0.0f) {
                    i2 = Math.round(r0.cueLine * ((float) i2)) + r0.parentTop;
                } else {
                    i2 = Math.round((r0.cueLine + 1.0f) * ((float) i2)) + r0.parentBottom;
                }
            }
            if (r0.cueLineAnchor == 2) {
                i2 -= height;
            } else if (r0.cueLineAnchor == 1) {
                i2 = ((i2 * 2) - height) / 2;
            }
            if (i2 + height > r0.parentBottom) {
                i2 = r0.parentBottom - height;
            } else if (i2 < r0.parentTop) {
                i2 = r0.parentTop;
            }
        } else {
            i2 = (r0.parentBottom - height) - ((int) (((float) i2) * r0.bottomPaddingFraction));
        }
        r0.textLayout = new StaticLayout(charSequence2, r0.textPaint, i8, alignment, r0.spacingMult, r0.spacingAdd, true);
        r0.textLeft = i;
        r0.textTop = i2;
        r0.textPaddingX = i3;
    }

    private void setupBitmapLayout() {
        int round;
        float f;
        int round2;
        int round3;
        float f2 = (float) (this.parentRight - this.parentLeft);
        float f3 = ((float) this.parentLeft) + (this.cuePosition * f2);
        float f4 = (float) (this.parentBottom - this.parentTop);
        float f5 = ((float) this.parentTop) + (this.cueLine * f4);
        int round4 = Math.round(f2 * this.cueSize);
        if (this.cueBitmapHeight != Float.MIN_VALUE) {
            round = Math.round(f4 * this.cueBitmapHeight);
        } else {
            round = Math.round(((float) round4) * (((float) this.cueBitmap.getHeight()) / ((float) this.cueBitmap.getWidth())));
        }
        if (this.cueLineAnchor == 2) {
            f = (float) round4;
        } else {
            if (this.cueLineAnchor == 1) {
                f = (float) (round4 / 2);
            }
            round2 = Math.round(f3);
            if (this.cuePositionAnchor != 2) {
                f = (float) round;
            } else {
                if (this.cuePositionAnchor == 1) {
                    f = (float) (round / 2);
                }
                round3 = Math.round(f5);
                this.bitmapRect = new Rect(round2, round3, round4 + round2, round + round3);
            }
            f5 -= f;
            round3 = Math.round(f5);
            this.bitmapRect = new Rect(round2, round3, round4 + round2, round + round3);
        }
        f3 -= f;
        round2 = Math.round(f3);
        if (this.cuePositionAnchor != 2) {
            if (this.cuePositionAnchor == 1) {
                f = (float) (round / 2);
            }
            round3 = Math.round(f5);
            this.bitmapRect = new Rect(round2, round3, round4 + round2, round + round3);
        }
        f = (float) round;
        f5 -= f;
        round3 = Math.round(f5);
        this.bitmapRect = new Rect(round2, round3, round4 + round2, round + round3);
    }

    private void drawLayout(Canvas canvas, boolean z) {
        if (z) {
            drawTextLayout(canvas);
        } else {
            drawBitmapLayout(canvas);
        }
    }

    private void drawTextLayout(Canvas canvas) {
        StaticLayout staticLayout = this.textLayout;
        if (staticLayout != null) {
            int i;
            int save = canvas.save();
            canvas.translate((float) this.textLeft, (float) this.textTop);
            if (Color.alpha(this.windowColor) > 0) {
                this.paint.setColor(this.windowColor);
                canvas.drawRect((float) (-this.textPaddingX), 0.0f, (float) (staticLayout.getWidth() + this.textPaddingX), (float) staticLayout.getHeight(), this.paint);
            }
            if (Color.alpha(this.backgroundColor) > 0) {
                this.paint.setColor(this.backgroundColor);
                float lineTop = (float) staticLayout.getLineTop(0);
                int lineCount = staticLayout.getLineCount();
                float f = lineTop;
                for (i = 0; i < lineCount; i++) {
                    this.lineBounds.left = staticLayout.getLineLeft(i) - ((float) this.textPaddingX);
                    this.lineBounds.right = staticLayout.getLineRight(i) + ((float) this.textPaddingX);
                    this.lineBounds.top = f;
                    this.lineBounds.bottom = (float) staticLayout.getLineBottom(i);
                    f = this.lineBounds.bottom;
                    canvas.drawRoundRect(this.lineBounds, this.cornerRadius, this.cornerRadius, this.paint);
                }
            }
            Object obj = 1;
            if (this.edgeType == 1) {
                this.textPaint.setStrokeJoin(Join.ROUND);
                this.textPaint.setStrokeWidth(this.outlineWidth);
                this.textPaint.setColor(this.edgeColor);
                this.textPaint.setStyle(Style.FILL_AND_STROKE);
                staticLayout.draw(canvas);
            } else if (this.edgeType == 2) {
                this.textPaint.setShadowLayer(this.shadowRadius, this.shadowOffset, this.shadowOffset, this.edgeColor);
            } else if (this.edgeType == 3 || this.edgeType == 4) {
                int i2;
                if (this.edgeType != 3) {
                    obj = null;
                }
                i = -1;
                if (obj != null) {
                    i2 = -1;
                } else {
                    i2 = this.edgeColor;
                }
                if (obj != null) {
                    i = this.edgeColor;
                }
                float f2 = this.shadowRadius / 2.0f;
                this.textPaint.setColor(this.foregroundColor);
                this.textPaint.setStyle(Style.FILL);
                float f3 = -f2;
                this.textPaint.setShadowLayer(this.shadowRadius, f3, f3, i2);
                staticLayout.draw(canvas);
                this.textPaint.setShadowLayer(this.shadowRadius, f2, f2, i);
            }
            this.textPaint.setColor(this.foregroundColor);
            this.textPaint.setStyle(Style.FILL);
            staticLayout.draw(canvas);
            this.textPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            canvas.restoreToCount(save);
        }
    }

    private void drawBitmapLayout(Canvas canvas) {
        canvas.drawBitmap(this.cueBitmap, null, this.bitmapRect, null);
    }

    private static boolean areCharSequencesEqual(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence != charSequence2) {
            if (charSequence == null || charSequence.equals(charSequence2) == null) {
                return null;
            }
        }
        return true;
    }
}
