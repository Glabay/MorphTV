package com.bumptech.glide.load.model;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.load.data.DataFetcher;
import org.apache.commons.io.IOUtils;

public class ResourceLoader<T> implements ModelLoader<Integer, T> {
    private static final String TAG = "ResourceLoader";
    private final Resources resources;
    private final ModelLoader<Uri, T> uriLoader;

    public ResourceLoader(Context context, ModelLoader<Uri, T> modelLoader) {
        this(context.getResources(), (ModelLoader) modelLoader);
    }

    public ResourceLoader(Resources resources, ModelLoader<Uri, T> modelLoader) {
        this.resources = resources;
        this.uriLoader = modelLoader;
    }

    public DataFetcher<T> getResourceFetcher(Integer num, int i, int i2) {
        Object parse;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("android.resource://");
            stringBuilder.append(this.resources.getResourcePackageName(num.intValue()));
            stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
            stringBuilder.append(this.resources.getResourceTypeName(num.intValue()));
            stringBuilder.append(IOUtils.DIR_SEPARATOR_UNIX);
            stringBuilder.append(this.resources.getResourceEntryName(num.intValue()));
            parse = Uri.parse(stringBuilder.toString());
        } catch (Throwable e) {
            if (Log.isLoggable(TAG, 5)) {
                String str = TAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Received invalid resource id: ");
                stringBuilder2.append(num);
                Log.w(str, stringBuilder2.toString(), e);
            }
            parse = null;
        }
        if (parse != null) {
            return this.uriLoader.getResourceFetcher(parse, i, i2);
        }
        return null;
    }
}
