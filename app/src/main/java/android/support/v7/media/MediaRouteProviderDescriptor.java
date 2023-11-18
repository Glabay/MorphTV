package android.support.v7.media;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MediaRouteProviderDescriptor {
    private static final String KEY_ROUTES = "routes";
    private final Bundle mBundle;
    private List<MediaRouteDescriptor> mRoutes;

    public static final class Builder {
        private final Bundle mBundle;
        private ArrayList<MediaRouteDescriptor> mRoutes;

        public Builder() {
            this.mBundle = new Bundle();
        }

        public Builder(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            if (mediaRouteProviderDescriptor == null) {
                throw new IllegalArgumentException("descriptor must not be null");
            }
            this.mBundle = new Bundle(mediaRouteProviderDescriptor.mBundle);
            mediaRouteProviderDescriptor.ensureRoutes();
            if (!mediaRouteProviderDescriptor.mRoutes.isEmpty()) {
                this.mRoutes = new ArrayList(mediaRouteProviderDescriptor.mRoutes);
            }
        }

        public Builder addRoute(MediaRouteDescriptor mediaRouteDescriptor) {
            if (mediaRouteDescriptor == null) {
                throw new IllegalArgumentException("route must not be null");
            }
            if (this.mRoutes == null) {
                this.mRoutes = new ArrayList();
            } else if (this.mRoutes.contains(mediaRouteDescriptor)) {
                throw new IllegalArgumentException("route descriptor already added");
            }
            this.mRoutes.add(mediaRouteDescriptor);
            return this;
        }

        public Builder addRoutes(Collection<MediaRouteDescriptor> collection) {
            if (collection == null) {
                throw new IllegalArgumentException("routes must not be null");
            }
            if (!collection.isEmpty()) {
                for (MediaRouteDescriptor addRoute : collection) {
                    addRoute(addRoute);
                }
            }
            return this;
        }

        Builder setRoutes(Collection<MediaRouteDescriptor> collection) {
            if (collection != null) {
                if (!collection.isEmpty()) {
                    this.mRoutes = new ArrayList(collection);
                    return this;
                }
            }
            this.mRoutes = null;
            this.mBundle.remove(MediaRouteProviderDescriptor.KEY_ROUTES);
            return this;
        }

        public MediaRouteProviderDescriptor build() {
            if (this.mRoutes != null) {
                int size = this.mRoutes.size();
                ArrayList arrayList = new ArrayList(size);
                for (int i = 0; i < size; i++) {
                    arrayList.add(((MediaRouteDescriptor) this.mRoutes.get(i)).asBundle());
                }
                this.mBundle.putParcelableArrayList(MediaRouteProviderDescriptor.KEY_ROUTES, arrayList);
            }
            return new MediaRouteProviderDescriptor(this.mBundle, this.mRoutes);
        }
    }

    private MediaRouteProviderDescriptor(Bundle bundle, List<MediaRouteDescriptor> list) {
        this.mBundle = bundle;
        this.mRoutes = list;
    }

    public List<MediaRouteDescriptor> getRoutes() {
        ensureRoutes();
        return this.mRoutes;
    }

    private void ensureRoutes() {
        if (this.mRoutes == null) {
            ArrayList parcelableArrayList = this.mBundle.getParcelableArrayList(KEY_ROUTES);
            if (parcelableArrayList != null) {
                if (!parcelableArrayList.isEmpty()) {
                    int size = parcelableArrayList.size();
                    this.mRoutes = new ArrayList(size);
                    for (int i = 0; i < size; i++) {
                        this.mRoutes.add(MediaRouteDescriptor.fromBundle((Bundle) parcelableArrayList.get(i)));
                    }
                    return;
                }
            }
            this.mRoutes = Collections.emptyList();
        }
    }

    public boolean isValid() {
        ensureRoutes();
        int size = this.mRoutes.size();
        int i = 0;
        while (i < size) {
            MediaRouteDescriptor mediaRouteDescriptor = (MediaRouteDescriptor) this.mRoutes.get(i);
            if (mediaRouteDescriptor != null) {
                if (mediaRouteDescriptor.isValid()) {
                    i++;
                }
            }
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MediaRouteProviderDescriptor{ ");
        stringBuilder.append("routes=");
        stringBuilder.append(Arrays.toString(getRoutes().toArray()));
        stringBuilder.append(", isValid=");
        stringBuilder.append(isValid());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public Bundle asBundle() {
        return this.mBundle;
    }

    public static MediaRouteProviderDescriptor fromBundle(Bundle bundle) {
        return bundle != null ? new MediaRouteProviderDescriptor(bundle, null) : null;
    }
}
