package android.support.v7.media;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.ObjectsCompat;
import android.support.v7.media.MediaRouteProvider.Callback;
import android.support.v7.media.MediaRouteProvider.RouteController;
import android.support.v7.media.MediaRouteProviderDescriptor.Builder;
import android.support.v7.media.MediaRouter.ControlRequestCallback;
import android.util.Log;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class MediaRouteProviderService extends Service {
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final int PRIVATE_MSG_CLIENT_DIED = 1;
    public static final String SERVICE_INTERFACE = "android.media.MediaRouteProviderService";
    static final String TAG = "MediaRouteProviderSrv";
    private final ArrayList<ClientRecord> mClients = new ArrayList();
    private MediaRouteDiscoveryRequest mCompositeDiscoveryRequest;
    final PrivateHandler mPrivateHandler = new PrivateHandler();
    MediaRouteProvider mProvider;
    private final ProviderCallback mProviderCallback = new ProviderCallback();
    private final ReceiveHandler mReceiveHandler = new ReceiveHandler(this);
    private final Messenger mReceiveMessenger = new Messenger(this.mReceiveHandler);

    private final class ClientRecord implements DeathRecipient {
        private final SparseArray<RouteController> mControllers = new SparseArray();
        public MediaRouteDiscoveryRequest mDiscoveryRequest;
        public final Messenger mMessenger;
        public final int mVersion;

        public ClientRecord(Messenger messenger, int i) {
            this.mMessenger = messenger;
            this.mVersion = i;
        }

        public boolean register() {
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
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
            /*
            r2 = this;
            r0 = 0;
            r1 = r2.mMessenger;	 Catch:{ RemoteException -> 0x000c }
            r1 = r1.getBinder();	 Catch:{ RemoteException -> 0x000c }
            r1.linkToDeath(r2, r0);	 Catch:{ RemoteException -> 0x000c }
            r0 = 1;
            return r0;
        L_0x000c:
            r2.binderDied();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouteProviderService.ClientRecord.register():boolean");
        }

        public void dispose() {
            int size = this.mControllers.size();
            for (int i = 0; i < size; i++) {
                ((RouteController) this.mControllers.valueAt(i)).onRelease();
            }
            this.mControllers.clear();
            this.mMessenger.getBinder().unlinkToDeath(this, 0);
            setDiscoveryRequest(null);
        }

        public boolean hasMessenger(Messenger messenger) {
            return this.mMessenger.getBinder() == messenger.getBinder() ? true : null;
        }

        public boolean createRouteController(String str, String str2, int i) {
            if (this.mControllers.indexOfKey(i) < 0) {
                if (str2 == null) {
                    str = MediaRouteProviderService.this.mProvider.onCreateRouteController(str);
                } else {
                    str = MediaRouteProviderService.this.mProvider.onCreateRouteController(str, str2);
                }
                if (str != null) {
                    this.mControllers.put(i, str);
                    return true;
                }
            }
            return null;
        }

        public boolean releaseRouteController(int i) {
            RouteController routeController = (RouteController) this.mControllers.get(i);
            if (routeController == null) {
                return false;
            }
            this.mControllers.remove(i);
            routeController.onRelease();
            return true;
        }

        public RouteController getRouteController(int i) {
            return (RouteController) this.mControllers.get(i);
        }

        public boolean setDiscoveryRequest(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
            if (ObjectsCompat.equals(this.mDiscoveryRequest, mediaRouteDiscoveryRequest)) {
                return null;
            }
            this.mDiscoveryRequest = mediaRouteDiscoveryRequest;
            return MediaRouteProviderService.this.updateCompositeDiscoveryRequest();
        }

        public void binderDied() {
            MediaRouteProviderService.this.mPrivateHandler.obtainMessage(1, this.mMessenger).sendToTarget();
        }

        public String toString() {
            return MediaRouteProviderService.getClientId(this.mMessenger);
        }
    }

    private final class PrivateHandler extends Handler {
        PrivateHandler() {
        }

        public void handleMessage(Message message) {
            if (message.what == 1) {
                MediaRouteProviderService.this.onBinderDied((Messenger) message.obj);
            }
        }
    }

    private final class ProviderCallback extends Callback {
        ProviderCallback() {
        }

        public void onDescriptorChanged(MediaRouteProvider mediaRouteProvider, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
            MediaRouteProviderService.this.sendDescriptorChanged(mediaRouteProviderDescriptor);
        }
    }

    private static final class ReceiveHandler extends Handler {
        private final WeakReference<MediaRouteProviderService> mServiceRef;

        public ReceiveHandler(MediaRouteProviderService mediaRouteProviderService) {
            this.mServiceRef = new WeakReference(mediaRouteProviderService);
        }

        public void handleMessage(Message message) {
            Messenger messenger = message.replyTo;
            if (MediaRouteProviderProtocol.isValidRemoteMessenger(messenger)) {
                int i = message.what;
                int i2 = message.arg1;
                int i3 = message.arg2;
                Object obj = message.obj;
                message = message.peekData();
                if (!processMessage(i, messenger, i2, i3, obj, message)) {
                    if (MediaRouteProviderService.DEBUG) {
                        String str = MediaRouteProviderService.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(MediaRouteProviderService.getClientId(messenger));
                        stringBuilder.append(": Message failed, what=");
                        stringBuilder.append(i);
                        stringBuilder.append(", requestId=");
                        stringBuilder.append(i2);
                        stringBuilder.append(", arg=");
                        stringBuilder.append(i3);
                        stringBuilder.append(", obj=");
                        stringBuilder.append(obj);
                        stringBuilder.append(", data=");
                        stringBuilder.append(message);
                        Log.d(str, stringBuilder.toString());
                    }
                    MediaRouteProviderService.sendGenericFailure(messenger, i2);
                }
            } else if (MediaRouteProviderService.DEBUG != null) {
                Log.d(MediaRouteProviderService.TAG, "Ignoring message without valid reply messenger.");
            }
        }

        private boolean processMessage(int i, Messenger messenger, int i2, int i3, Object obj, Bundle bundle) {
            MediaRouteProviderService mediaRouteProviderService = (MediaRouteProviderService) this.mServiceRef.get();
            int i4 = 0;
            if (mediaRouteProviderService != null) {
                switch (i) {
                    case 1:
                        return mediaRouteProviderService.onRegisterClient(messenger, i2, i3);
                    case 2:
                        return mediaRouteProviderService.onUnregisterClient(messenger, i2);
                    case 3:
                        String string = bundle.getString(MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_ID);
                        String string2 = bundle.getString(MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_LIBRARY_GROUP);
                        if (string != null) {
                            return mediaRouteProviderService.onCreateRouteController(messenger, i2, i3, string, string2);
                        }
                        break;
                    case 4:
                        return mediaRouteProviderService.onReleaseRouteController(messenger, i2, i3);
                    case 5:
                        return mediaRouteProviderService.onSelectRoute(messenger, i2, i3);
                    case 6:
                        if (bundle != null) {
                            i4 = bundle.getInt(MediaRouteProviderProtocol.CLIENT_DATA_UNSELECT_REASON, 0);
                        }
                        return mediaRouteProviderService.onUnselectRoute(messenger, i2, i3, i4);
                    case 7:
                        i = bundle.getInt(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, -1);
                        if (i >= 0) {
                            return mediaRouteProviderService.onSetRouteVolume(messenger, i2, i3, i);
                        }
                        break;
                    case 8:
                        i = bundle.getInt(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, 0);
                        if (i != 0) {
                            return mediaRouteProviderService.onUpdateRouteVolume(messenger, i2, i3, i);
                        }
                        break;
                    case 9:
                        if ((obj instanceof Intent) != 0) {
                            return mediaRouteProviderService.onRouteControlRequest(messenger, i2, i3, (Intent) obj);
                        }
                        break;
                    case 10:
                        if (obj == null || (obj instanceof Bundle) != 0) {
                            i = MediaRouteDiscoveryRequest.fromBundle((Bundle) obj);
                            if (i == 0 || i.isValid() == 0) {
                                i = 0;
                            }
                            return mediaRouteProviderService.onSetDiscoveryRequest(messenger, i2, i);
                        }
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
    }

    public abstract MediaRouteProvider onCreateMediaRouteProvider();

    public MediaRouteProvider getMediaRouteProvider() {
        return this.mProvider;
    }

    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals("android.media.MediaRouteProviderService") != null) {
            if (this.mProvider == null) {
                intent = onCreateMediaRouteProvider();
                if (intent != null) {
                    String packageName = intent.getMetadata().getPackageName();
                    if (packageName.equals(getPackageName())) {
                        this.mProvider = intent;
                        this.mProvider.setCallback(this.mProviderCallback);
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("onCreateMediaRouteProvider() returned a provider whose package name does not match the package name of the service.  A media route provider service can only export its own media route providers.  Provider package name: ");
                        stringBuilder.append(packageName);
                        stringBuilder.append(".  Service package name: ");
                        stringBuilder.append(getPackageName());
                        stringBuilder.append(".");
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                }
            }
            if (this.mProvider != null) {
                return this.mReceiveMessenger.getBinder();
            }
        }
        return null;
    }

    public boolean onUnbind(Intent intent) {
        if (this.mProvider != null) {
            this.mProvider.setCallback(null);
        }
        return super.onUnbind(intent);
    }

    boolean onRegisterClient(Messenger messenger, int i, int i2) {
        if (i2 >= 1 && findClient(messenger) < 0) {
            ClientRecord clientRecord = new ClientRecord(messenger, i2);
            if (clientRecord.register()) {
                this.mClients.add(clientRecord);
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(clientRecord);
                    stringBuilder.append(": Registered, version=");
                    stringBuilder.append(i2);
                    Log.d(str, stringBuilder.toString());
                }
                if (i != 0) {
                    sendReply(messenger, 2, i, 1, createDescriptorBundleForClientVersion(this.mProvider.getDescriptor(), clientRecord.mVersion), null);
                }
                return true;
            }
        }
        return null;
    }

    boolean onUnregisterClient(Messenger messenger, int i) {
        int findClient = findClient(messenger);
        if (findClient < 0) {
            return null;
        }
        ClientRecord clientRecord = (ClientRecord) this.mClients.remove(findClient);
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(clientRecord);
            stringBuilder.append(": Unregistered");
            Log.d(str, stringBuilder.toString());
        }
        clientRecord.dispose();
        sendGenericSuccess(messenger, i);
        return true;
    }

    void onBinderDied(Messenger messenger) {
        messenger = findClient(messenger);
        if (messenger >= null) {
            ClientRecord clientRecord = (ClientRecord) this.mClients.remove(messenger);
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(clientRecord);
                stringBuilder.append(": Binder died");
                Log.d(str, stringBuilder.toString());
            }
            clientRecord.dispose();
        }
    }

    boolean onCreateRouteController(Messenger messenger, int i, int i2, String str, String str2) {
        ClientRecord client = getClient(messenger);
        if (client == null || !client.createRouteController(str, str2, i2)) {
            return null;
        }
        if (DEBUG) {
            String str3 = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(client);
            stringBuilder.append(": Route controller created, controllerId=");
            stringBuilder.append(i2);
            stringBuilder.append(", routeId=");
            stringBuilder.append(str);
            stringBuilder.append(", routeGroupId=");
            stringBuilder.append(str2);
            Log.d(str3, stringBuilder.toString());
        }
        sendGenericSuccess(messenger, i);
        return true;
    }

    boolean onReleaseRouteController(Messenger messenger, int i, int i2) {
        ClientRecord client = getClient(messenger);
        if (client == null || !client.releaseRouteController(i2)) {
            return null;
        }
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(client);
            stringBuilder.append(": Route controller released");
            stringBuilder.append(", controllerId=");
            stringBuilder.append(i2);
            Log.d(str, stringBuilder.toString());
        }
        sendGenericSuccess(messenger, i);
        return true;
    }

    boolean onSelectRoute(Messenger messenger, int i, int i2) {
        ClientRecord client = getClient(messenger);
        if (client != null) {
            RouteController routeController = client.getRouteController(i2);
            if (routeController != null) {
                routeController.onSelect();
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(client);
                    stringBuilder.append(": Route selected");
                    stringBuilder.append(", controllerId=");
                    stringBuilder.append(i2);
                    Log.d(str, stringBuilder.toString());
                }
                sendGenericSuccess(messenger, i);
                return true;
            }
        }
        return null;
    }

    boolean onUnselectRoute(Messenger messenger, int i, int i2, int i3) {
        ClientRecord client = getClient(messenger);
        if (client != null) {
            RouteController routeController = client.getRouteController(i2);
            if (routeController != null) {
                routeController.onUnselect(i3);
                if (DEBUG != 0) {
                    i3 = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(client);
                    stringBuilder.append(": Route unselected");
                    stringBuilder.append(", controllerId=");
                    stringBuilder.append(i2);
                    Log.d(i3, stringBuilder.toString());
                }
                sendGenericSuccess(messenger, i);
                return true;
            }
        }
        return null;
    }

    boolean onSetRouteVolume(Messenger messenger, int i, int i2, int i3) {
        ClientRecord client = getClient(messenger);
        if (client != null) {
            RouteController routeController = client.getRouteController(i2);
            if (routeController != null) {
                routeController.onSetVolume(i3);
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(client);
                    stringBuilder.append(": Route volume changed");
                    stringBuilder.append(", controllerId=");
                    stringBuilder.append(i2);
                    stringBuilder.append(", volume=");
                    stringBuilder.append(i3);
                    Log.d(str, stringBuilder.toString());
                }
                sendGenericSuccess(messenger, i);
                return true;
            }
        }
        return null;
    }

    boolean onUpdateRouteVolume(Messenger messenger, int i, int i2, int i3) {
        ClientRecord client = getClient(messenger);
        if (client != null) {
            RouteController routeController = client.getRouteController(i2);
            if (routeController != null) {
                routeController.onUpdateVolume(i3);
                if (DEBUG) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(client);
                    stringBuilder.append(": Route volume updated");
                    stringBuilder.append(", controllerId=");
                    stringBuilder.append(i2);
                    stringBuilder.append(", delta=");
                    stringBuilder.append(i3);
                    Log.d(str, stringBuilder.toString());
                }
                sendGenericSuccess(messenger, i);
                return true;
            }
        }
        return null;
    }

    boolean onRouteControlRequest(Messenger messenger, int i, int i2, Intent intent) {
        ClientRecord client = getClient(messenger);
        if (client != null) {
            RouteController routeController = client.getRouteController(i2);
            if (routeController != null) {
                ControlRequestCallback controlRequestCallback;
                if (i != 0) {
                    final ClientRecord clientRecord = client;
                    final int i3 = i2;
                    final Intent intent2 = intent;
                    final Messenger messenger2 = messenger;
                    final int i4 = i;
                    ControlRequestCallback c02541 = new ControlRequestCallback() {
                        public void onResult(Bundle bundle) {
                            if (MediaRouteProviderService.DEBUG) {
                                String str = MediaRouteProviderService.TAG;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(clientRecord);
                                stringBuilder.append(": Route control request succeeded");
                                stringBuilder.append(", controllerId=");
                                stringBuilder.append(i3);
                                stringBuilder.append(", intent=");
                                stringBuilder.append(intent2);
                                stringBuilder.append(", data=");
                                stringBuilder.append(bundle);
                                Log.d(str, stringBuilder.toString());
                            }
                            if (MediaRouteProviderService.this.findClient(messenger2) >= 0) {
                                MediaRouteProviderService.sendReply(messenger2, 3, i4, 0, bundle, null);
                            }
                        }

                        public void onError(String str, Bundle bundle) {
                            if (MediaRouteProviderService.DEBUG) {
                                String str2 = MediaRouteProviderService.TAG;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(clientRecord);
                                stringBuilder.append(": Route control request failed");
                                stringBuilder.append(", controllerId=");
                                stringBuilder.append(i3);
                                stringBuilder.append(", intent=");
                                stringBuilder.append(intent2);
                                stringBuilder.append(", error=");
                                stringBuilder.append(str);
                                stringBuilder.append(", data=");
                                stringBuilder.append(bundle);
                                Log.d(str2, stringBuilder.toString());
                            }
                            if (MediaRouteProviderService.this.findClient(messenger2) < 0) {
                                return;
                            }
                            if (str != null) {
                                Bundle bundle2 = new Bundle();
                                bundle2.putString(MediaRouteProviderProtocol.SERVICE_DATA_ERROR, str);
                                MediaRouteProviderService.sendReply(messenger2, 4, i4, 0, bundle, bundle2);
                                return;
                            }
                            MediaRouteProviderService.sendReply(messenger2, 4, i4, 0, bundle, null);
                        }
                    };
                } else {
                    controlRequestCallback = null;
                }
                if (routeController.onControlRequest(intent, controlRequestCallback) != null) {
                    if (DEBUG != null) {
                        messenger = TAG;
                        i = new StringBuilder();
                        i.append(client);
                        i.append(": Route control request delivered");
                        i.append(", controllerId=");
                        i.append(i2);
                        i.append(", intent=");
                        i.append(intent);
                        Log.d(messenger, i.toString());
                    }
                    return true;
                }
            }
        }
        return null;
    }

    boolean onSetDiscoveryRequest(Messenger messenger, int i, MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        ClientRecord client = getClient(messenger);
        if (client == null) {
            return null;
        }
        boolean discoveryRequest = client.setDiscoveryRequest(mediaRouteDiscoveryRequest);
        if (DEBUG) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(client);
            stringBuilder.append(": Set discovery request, request=");
            stringBuilder.append(mediaRouteDiscoveryRequest);
            stringBuilder.append(", actuallyChanged=");
            stringBuilder.append(discoveryRequest);
            stringBuilder.append(", compositeDiscoveryRequest=");
            stringBuilder.append(this.mCompositeDiscoveryRequest);
            Log.d(str, stringBuilder.toString());
        }
        sendGenericSuccess(messenger, i);
        return true;
    }

    void sendDescriptorChanged(MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
        int size = this.mClients.size();
        for (int i = 0; i < size; i++) {
            ClientRecord clientRecord = (ClientRecord) this.mClients.get(i);
            sendReply(clientRecord.mMessenger, 5, 0, 0, createDescriptorBundleForClientVersion(mediaRouteProviderDescriptor, clientRecord.mVersion), null);
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(clientRecord);
                stringBuilder.append(": Sent descriptor change event, descriptor=");
                stringBuilder.append(mediaRouteProviderDescriptor);
                Log.d(str, stringBuilder.toString());
            }
        }
    }

    @VisibleForTesting
    static Bundle createDescriptorBundleForClientVersion(MediaRouteProviderDescriptor mediaRouteProviderDescriptor, int i) {
        if (mediaRouteProviderDescriptor == null) {
            return null;
        }
        Builder builder = new Builder(mediaRouteProviderDescriptor);
        builder.setRoutes(null);
        for (MediaRouteDescriptor mediaRouteDescriptor : mediaRouteProviderDescriptor.getRoutes()) {
            if (i >= mediaRouteDescriptor.getMinClientVersion() && i <= mediaRouteDescriptor.getMaxClientVersion()) {
                builder.addRoute(mediaRouteDescriptor);
            }
        }
        return builder.build().asBundle();
    }

    boolean updateCompositeDiscoveryRequest() {
        int size = this.mClients.size();
        MediaRouteSelector.Builder builder = null;
        MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest = builder;
        boolean z = false;
        for (int i = 0; i < size; i++) {
            MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest2 = ((ClientRecord) this.mClients.get(i)).mDiscoveryRequest;
            if (mediaRouteDiscoveryRequest2 != null && (!mediaRouteDiscoveryRequest2.getSelector().isEmpty() || mediaRouteDiscoveryRequest2.isActiveScan())) {
                z |= mediaRouteDiscoveryRequest2.isActiveScan();
                if (mediaRouteDiscoveryRequest == null) {
                    mediaRouteDiscoveryRequest = mediaRouteDiscoveryRequest2;
                } else {
                    if (builder == null) {
                        builder = new MediaRouteSelector.Builder(mediaRouteDiscoveryRequest.getSelector());
                    }
                    builder.addSelector(mediaRouteDiscoveryRequest2.getSelector());
                }
            }
        }
        if (builder != null) {
            mediaRouteDiscoveryRequest = new MediaRouteDiscoveryRequest(builder.build(), z);
        }
        if (ObjectsCompat.equals(this.mCompositeDiscoveryRequest, mediaRouteDiscoveryRequest)) {
            return false;
        }
        this.mCompositeDiscoveryRequest = mediaRouteDiscoveryRequest;
        this.mProvider.setDiscoveryRequest(mediaRouteDiscoveryRequest);
        return true;
    }

    private ClientRecord getClient(Messenger messenger) {
        messenger = findClient(messenger);
        return messenger >= null ? (ClientRecord) this.mClients.get(messenger) : null;
    }

    int findClient(Messenger messenger) {
        int size = this.mClients.size();
        for (int i = 0; i < size; i++) {
            if (((ClientRecord) this.mClients.get(i)).hasMessenger(messenger)) {
                return i;
            }
        }
        return -1;
    }

    static void sendGenericFailure(Messenger messenger, int i) {
        if (i != 0) {
            sendReply(messenger, 0, i, 0, null, null);
        }
    }

    private static void sendGenericSuccess(Messenger messenger, int i) {
        if (i != 0) {
            sendReply(messenger, 1, i, 0, null, null);
        }
    }

    static void sendReply(android.os.Messenger r1, int r2, int r3, int r4, java.lang.Object r5, android.os.Bundle r6) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:75)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r0 = android.os.Message.obtain();
        r0.what = r2;
        r0.arg1 = r3;
        r0.arg2 = r4;
        r0.obj = r5;
        r0.setData(r6);
        r1.send(r0);	 Catch:{ DeadObjectException -> 0x002e, RemoteException -> 0x0013 }
        goto L_0x002e;
    L_0x0013:
        r2 = move-exception;
        r3 = "MediaRouteProviderSrv";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Could not send message to ";
        r4.append(r5);
        r1 = getClientId(r1);
        r4.append(r1);
        r1 = r4.toString();
        android.util.Log.e(r3, r1, r2);
    L_0x002e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.MediaRouteProviderService.sendReply(android.os.Messenger, int, int, int, java.lang.Object, android.os.Bundle):void");
    }

    static String getClientId(Messenger messenger) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Client connection ");
        stringBuilder.append(messenger.getBinder().toString());
        return stringBuilder.toString();
    }
}
