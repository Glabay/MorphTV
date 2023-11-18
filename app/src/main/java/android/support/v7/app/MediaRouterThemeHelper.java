package android.support.v7.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.appcompat.C0249R;
import android.support.v7.mediarouter.C0266R;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

final class MediaRouterThemeHelper {
    static final int COLOR_DARK_ON_LIGHT_BACKGROUND = -570425344;
    static final int COLOR_WHITE_ON_DARK_BACKGROUND = -1;
    private static final float MIN_CONTRAST = 3.0f;

    @Retention(RetentionPolicy.SOURCE)
    private @interface ControllerColorType {
    }

    private MediaRouterThemeHelper() {
    }

    static Context createThemedButtonContext(Context context) {
        Context contextThemeWrapper = new ContextThemeWrapper(context, getRouterThemeId(context));
        context = getThemeResource(contextThemeWrapper, C0266R.attr.mediaRouteTheme);
        return context != null ? new ContextThemeWrapper(contextThemeWrapper, context) : contextThemeWrapper;
    }

    static Context createThemedDialogContext(Context context, int i, boolean z) {
        if (i == 0) {
            i = getThemeResource(context, !z ? C0249R.attr.dialogTheme : C0249R.attr.alertDialogTheme);
        }
        z = new ContextThemeWrapper(context, i);
        return getThemeResource(z, C0266R.attr.mediaRouteTheme) != null ? new ContextThemeWrapper(z, getRouterThemeId(z)) : z;
    }

    static int createThemedDialogStyle(Context context) {
        int themeResource = getThemeResource(context, C0266R.attr.mediaRouteTheme);
        return themeResource == 0 ? getRouterThemeId(context) : themeResource;
    }

    static int getThemeResource(Context context, int i) {
        TypedValue typedValue = new TypedValue();
        return context.getTheme().resolveAttribute(i, typedValue, true) != null ? typedValue.resourceId : null;
    }

    static float getDisabledAlpha(Context context) {
        TypedValue typedValue = new TypedValue();
        return context.getTheme().resolveAttribute(16842803, typedValue, true) != null ? typedValue.getFloat() : 0.5f;
    }

    static int getControllerColor(Context context, int i) {
        if (ColorUtils.calculateContrast(-1, getThemeColor(context, i, C0249R.attr.colorPrimary)) >= 3.0d) {
            return -1;
        }
        return COLOR_DARK_ON_LIGHT_BACKGROUND;
    }

    static int getButtonTextColor(Context context) {
        int themeColor = getThemeColor(context, 0, C0249R.attr.colorPrimary);
        return ColorUtils.calculateContrast(themeColor, getThemeColor(context, 0, 16842801)) < 3.0d ? getThemeColor(context, 0, C0249R.attr.colorAccent) : themeColor;
    }

    static void setMediaControlsBackgroundColor(Context context, View view, View view2, boolean z) {
        int themeColor = getThemeColor(context, 0, C0249R.attr.colorPrimary);
        int themeColor2 = getThemeColor(context, 0, C0249R.attr.colorPrimaryDark);
        if (z && getControllerColor(context, 0) == true) {
            context = -1;
        } else {
            context = themeColor;
            themeColor = themeColor2;
        }
        view.setBackgroundColor(context);
        view2.setBackgroundColor(themeColor);
        view.setTag(Integer.valueOf(context));
        view2.setTag(Integer.valueOf(themeColor));
    }

    static void setVolumeSliderColor(Context context, MediaRouteVolumeSlider mediaRouteVolumeSlider, View view) {
        context = getControllerColor(context, 0);
        if (Color.alpha(context) != 255) {
            context = ColorUtils.compositeColors(context, ((Integer) view.getTag()).intValue());
        }
        mediaRouteVolumeSlider.setColor(context);
    }

    private static boolean isLightTheme(Context context) {
        TypedValue typedValue = new TypedValue();
        return (context.getTheme().resolveAttribute(C0249R.attr.isLightTheme, typedValue, true) == null || typedValue.data == null) ? false : true;
    }

    private static int getThemeColor(Context context, int i, int i2) {
        if (i != 0) {
            i = context.obtainStyledAttributes(i, new int[]{i2});
            int color = i.getColor(0, 0);
            i.recycle();
            if (color != 0) {
                return color;
            }
        }
        i = new TypedValue();
        context.getTheme().resolveAttribute(i2, i, true);
        if (i.resourceId != 0) {
            return context.getResources().getColor(i.resourceId);
        }
        return i.data;
    }

    private static int getRouterThemeId(Context context) {
        if (isLightTheme(context)) {
            if (getControllerColor(context, 0) == -570425344) {
                return C0266R.style.Theme_MediaRouter_Light;
            }
            return C0266R.style.Theme_MediaRouter_Light_DarkControlPanel;
        } else if (getControllerColor(context, 0) == -570425344) {
            return C0266R.style.Theme_MediaRouter_LightControlPanel;
        } else {
            return C0266R.style.Theme_MediaRouter;
        }
    }
}
