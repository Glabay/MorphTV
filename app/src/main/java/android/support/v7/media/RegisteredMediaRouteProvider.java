package android.support.v7.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.v7.media.MediaRouteProvider.ProviderMetadata;
import android.support.v7.media.MediaRouteProvider.RouteController;
import android.support.v7.media.MediaRouter.ControlRequestCallback;
import android.util.Log;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

final class RegisteredMediaRouteProvider extends MediaRouteProvider implements ServiceConnection {
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final String TAG = "MediaRouteProviderProxy";
    private Connection mActiveConnection;
    private boolean mBound;
    private final ComponentName mComponentName;
    private boolean mConnectionReady;
    private final ArrayList<Controller> mControllers = new ArrayList();
    final PrivateHandler mPrivateHandler;
    private boolean mStarted;

    private final class Connection implements DeathRecipient {
        private int mNextControllerId = 1;
        private int mNextRequestId = 1;
        private final SparseArray<ControlRequestCallback> mPendingCallbacks = new SparseArray();
        private int mPendingRegisterRequestId;
        private final ReceiveHandler mReceiveHandler;
        private final Messenger mReceiveMessenger;
        private final Messenger mServiceMessenger;
        private int mServiceVersion;

        /* renamed from: android.support.v7.media.RegisteredMediaRouteProvider$Connection$1 */
        class C02601 implements Runnable {
            C02601() {
            }

            public void run() {
                Connection.this.failPendingCallbacks();
            }
        }

        /* renamed from: android.support.v7.media.RegisteredMediaRouteProvider$Connection$2 */
        class C02612 implements Runnable {
            C02612() {
            }

            public void run() {
                RegisteredMediaRouteProvider.this.onConnectionDied(Connection.this);
            }
        }

        public boolean onGenericSuccess(int i) {
            return true;
        }

        public Connection(Messenger messenger) {
            this.mServiceMessenger = messenger;
            this.mReceiveHandler = new ReceiveHandler(this);
            this.mReceiveMessenger = new Messenger(this.mReceiveHandler);
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
            r8 = this;
            r0 = r8.mNextRequestId;
            r1 = r0 + 1;
            r8.mNextRequestId = r1;
            r8.mPendingRegisterRequestId = r0;
            r4 = r8.mPendingRegisterRequestId;
            r3 = 1;
            r5 = 2;
            r6 = 0;
            r7 = 0;
            r2 = r8;
            r0 = r2.sendRequest(r3, r4, r5, r6, r7);
            r1 = 0;
            if (r0 != 0) goto L_0x0017;
        L_0x0016:
            return r1;
        L_0x0017:
            r0 = r8.mServiceMessenger;	 Catch:{ RemoteException -> 0x0022 }
            r0 = r0.getBinder();	 Catch:{ RemoteException -> 0x0022 }
            r0.linkToDeath(r8, r1);	 Catch:{ RemoteException -> 0x0022 }
            r0 = 1;
            return r0;
        L_0x0022:
            r8.binderDied();
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.RegisteredMediaRouteProvider.Connection.register():boolean");
        }

        public void dispose() {
            sendRequest(2, 0, 0, null, null);
            this.mReceiveHandler.dispose();
            this.mServiceMessenger.getBinder().unlinkToDeath(this, 0);
            RegisteredMediaRouteProvider.this.mPrivateHandler.post(new C02601());
        }

        void failPendingCallbacks() {
            for (int i = 0; i < this.mPendingCallbacks.size(); i++) {
                ((ControlRequestCallback) this.mPendingCallbacks.valueAt(i)).onError(null, null);
            }
            this.mPendingCallbacks.clear();
        }

        public boolean onGenericFailure(int i) {
            if (i == this.mPendingRegisterRequestId) {
                this.mPendingRegisterRequestId = 0;
                RegisteredMediaRouteProvider.this.onConnectionError(this, "Registration failed");
            }
            ControlRequestCallback controlRequestCallback = (ControlRequestCallback) this.mPendingCallbacks.get(i);
            if (controlRequestCallback != null) {
                this.mPendingCallbacks.remove(i);
                controlRequestCallback.onError(null, null);
            }
            return true;
        }

        public boolean onRegistered(int i, int i2, Bundle bundle) {
            if (this.mServiceVersion != 0 || i != this.mPendingRegisterRequestId || i2 < 1) {
                return false;
            }
            this.mPendingRegisterRequestId = 0;
            this.mServiceVersion = i2;
            RegisteredMediaRouteProvider.this.onConnectionDescriptorChanged(this, MediaRouteProviderDescriptor.fromBundle(bundle));
            RegisteredMediaRouteProvider.this.onConnectionReady(this);
            return true;
        }

        public boolean onDescriptorChanged(Bundle bundle) {
            if (this.mServiceVersion == 0) {
                return null;
            }
            RegisteredMediaRouteProvider.this.onConnectionDescriptorChanged(this, MediaRouteProviderDescriptor.fromBundle(bundle));
            return true;
        }

        public boolean onControlRequestSucceeded(int i, Bundle bundle) {
            ControlRequestCallback controlRequestCallback = (ControlRequestCallback) this.mPendingCallbacks.get(i);
            if (controlRequestCallback == null) {
                return false;
            }
            this.mPendingCallbacks.remove(i);
            controlRequestCallback.onResult(bundle);
            return true;
        }

        public boolean onControlRequestFailed(int i, String str, Bundle bundle) {
            ControlRequestCallback controlRequestCallback = (ControlRequestCallback) this.mPendingCallbacks.get(i);
            if (controlRequestCallback == null) {
                return false;
            }
            this.mPendingCallbacks.remove(i);
            controlRequestCallback.onError(str, bundle);
            return true;
        }

        public void binderDied() {
            RegisteredMediaRouteProvider.this.mPrivateHandler.post(new C02612());
        }

        public int createRouteController(String str, String str2) {
            int i = this.mNextControllerId;
            this.mNextControllerId = i + 1;
            Bundle bundle = new Bundle();
            bundle.putString(MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_ID, str);
            bundle.putString(MediaRouteProviderProtocol.CLIENT_DATA_ROUTE_LIBRARY_GROUP, str2);
            int i2 = this.mNextRequestId;
            this.mNextRequestId = i2 + 1;
            sendRequest(3, i2, i, null, bundle);
            return i;
        }

        public void releaseRouteController(int i) {
            int i2 = this.mNextRequestId;
            this.mNextRequestId = i2 + 1;
            sendRequest(4, i2, i, null, null);
        }

        public void selectRoute(int i) {
            int i2 = this.mNextRequestId;
            this.mNextRequestId = i2 + 1;
            sendRequest(5, i2, i, null, null);
        }

        public void unselectRoute(int i, int i2) {
            Bundle bundle = new Bundle();
            bundle.putInt(MediaRouteProviderProtocol.CLIENT_DATA_UNSELECT_REASON, i2);
            int i3 = this.mNextRequestId;
            this.mNextRequestId = i3 + 1;
            sendRequest(6, i3, i, null, bundle);
        }

        public void setVolume(int i, int i2) {
            Bundle bundle = new Bundle();
            bundle.putInt(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, i2);
            int i3 = this.mNextRequestId;
            this.mNextRequestId = i3 + 1;
            sendRequest(7, i3, i, null, bundle);
        }

        public void updateVolume(int i, int i2) {
            Bundle bundle = new Bundle();
            bundle.putInt(MediaRouteProviderProtocol.CLIENT_DATA_VOLUME, i2);
            int i3 = this.mNextRequestId;
            this.mNextRequestId = i3 + 1;
            sendRequest(8, i3, i, null, bundle);
        }

        public boolean sendControlRequest(int i, Intent intent, ControlRequestCallback controlRequestCallback) {
            int i2 = this.mNextRequestId;
            this.mNextRequestId = i2 + 1;
            if (sendRequest(9, i2, i, intent, null) == 0) {
                return false;
            }
            if (controlRequestCallback != null) {
                this.mPendingCallbacks.put(i2, controlRequestCallback);
            }
            return true;
        }

        public void setDiscoveryRequest(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
            int i = this.mNextRequestId;
            this.mNextRequestId = i + 1;
            sendRequest(10, i, 0, mediaRouteDiscoveryRequest != null ? mediaRouteDiscoveryRequest.asBundle() : null, null);
        }

        private boolean sendRequest(int r2, int r3, int r4, java.lang.Object r5, android.os.Bundle r6) {
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
            r1 = this;
            r0 = android.os.Message.obtain();
            r0.what = r2;
            r0.arg1 = r3;
            r0.arg2 = r4;
            r0.obj = r5;
            r0.setData(r6);
            r3 = r1.mReceiveMessenger;
            r0.replyTo = r3;
            r3 = r1.mServiceMessenger;	 Catch:{ DeadObjectException -> 0x0025, RemoteException -> 0x001a }
            r3.send(r0);	 Catch:{ DeadObjectException -> 0x0025, RemoteException -> 0x001a }
            r2 = 1;
            return r2;
        L_0x001a:
            r3 = move-exception;
            r4 = 2;
            if (r2 == r4) goto L_0x0025;
        L_0x001e:
            r2 = "MediaRouteProviderProxy";
            r4 = "Could not send message to service.";
            android.util.Log.e(r2, r4, r3);
        L_0x0025:
            r2 = 0;
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.media.RegisteredMediaRouteProvider.Connection.sendRequest(int, int, int, java.lang.Object, android.os.Bundle):boolean");
        }
    }

    private final class Controller extends RouteController {
        private Connection mConnection;
        private int mControllerId;
        private int mPendingSetVolume = -1;
        private int mPendingUpdateVolumeDelta;
        private final String mRouteGroupId;
        private final String mRouteId;
        private boolean mSelected;

        public Controller(String str, String str2) {
            this.mRouteId = str;
            this.mRouteGroupId = str2;
        }

        public void attachConnection(Connection connection) {
            this.mConnection = connection;
            this.mControllerId = connection.createRouteController(this.mRouteId, this.mRouteGroupId);
            if (this.mSelected) {
                connection.selectRoute(this.mControllerId);
                if (this.mPendingSetVolume >= 0) {
                    connection.setVolume(this.mControllerId, this.mPendingSetVolume);
                    this.mPendingSetVolume = -1;
                }
                if (this.mPendingUpdateVolumeDelta != 0) {
                    connection.updateVolume(this.mControllerId, this.mPendingUpdateVolumeDelta);
                    this.mPendingUpdateVolumeDelta = null;
                }
            }
        }

        public void detachConnection() {
            if (this.mConnection != null) {
                this.mConnection.releaseRouteController(this.mControllerId);
                this.mConnection = null;
                this.mControllerId = 0;
            }
        }

        public void onRelease() {
            RegisteredMediaRouteProvider.this.onControllerReleased(this);
        }

        public void onSelect() {
            this.mSelected = true;
            if (this.mConnection != null) {
                this.mConnection.selectRoute(this.mControllerId);
            }
        }

        public void onUnselect() {
            onUnselect(0);
        }

        public void onUnselect(int i) {
            this.mSelected = false;
            if (this.mConnection != null) {
                this.mConnection.unselectRoute(this.mControllerId, i);
            }
        }

        public void onSetVolume(int i) {
            if (this.mConnection != null) {
                this.mConnection.setVolume(this.mControllerId, i);
                return;
            }
            this.mPendingSetVolume = i;
            this.mPendingUpdateVolumeDelta = 0;
        }

        public void onUpdateVolume(int i) {
            if (this.mConnection != null) {
                this.mConnection.updateVolume(this.mControllerId, i);
            } else {
                this.mPendingUpdateVolumeDelta += i;
            }
        }

        public boolean onControlRequest(Intent intent, ControlRequestCallback controlRequestCallback) {
            return this.mConnection != null ? this.mConnection.sendControlRequest(this.mControllerId, intent, controlRequestCallback) : null;
        }
    }

    private static final class PrivateHandler extends Handler {
        PrivateHandler() {
        }
    }

    private static final class ReceiveHandler extends Handler {
        private final WeakReference<Connection> mConnectionRef;

        public ReceiveHandler(Connection connection) {
            this.mConnectionRef = new WeakReference(connection);
        }

        public void dispose() {
            this.mConnectionRef.clear();
        }

        public void handleMessage(Message message) {
            Connection connection = (Connection) this.mConnectionRef.get();
            if (connection != null && !processMessage(connection, message.what, message.arg1, message.arg2, message.obj, message.peekData()) && RegisteredMediaRouteProvider.DEBUG) {
                String str = RegisteredMediaRouteProvider.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unhandled message from server: ");
                stringBuilder.append(message);
                Log.d(str, stringBuilder.toString());
            }
        }

        private boolean processMessage(Connection connection, int i, int i2, int i3, Object obj, Bundle bundle) {
            switch (i) {
                case 0:
                    connection.onGenericFailure(i2);
                    return true;
                case 1:
                    connection.onGenericSuccess(i2);
                    return true;
                case 2:
                    if (obj == null || (obj instanceof Bundle) != 0) {
                        return connection.onRegistered(i2, i3, (Bundle) obj);
                    }
                case 3:
                    if (obj == null || (obj instanceof Bundle) != 0) {
                        return connection.onControlRequestSucceeded(i2, (Bundle) obj);
                    }
                case 4:
                    if (obj == null || (obj instanceof Bundle) != 0) {
                        if (bundle == null) {
                            i = 0;
                        } else {
                            i = bundle.getString(MediaRouteProviderProtocol.SERVICE_DATA_ERROR);
                        }
                        return connection.onControlRequestFailed(i2, i, (Bundle) obj);
                    }
                case 5:
                    if (obj == null || (obj instanceof Bundle) != 0) {
                        return connection.onDescriptorChanged((Bundle) obj);
                    }
                default:
                    break;
            }
            return null;
        }
    }

    public RegisteredMediaRouteProvider(Context context, ComponentName componentName) {
        super(context, new ProviderMetadata(componentName));
        this.mComponentName = componentName;
        this.mPrivateHandler = new PrivateHandler();
    }

    public RouteController onCreateRouteController(@NonNull String str) {
        if (str != null) {
            return createRouteController(str, null);
        }
        throw new IllegalArgumentException("routeId cannot be null");
    }

    public RouteController onCreateRouteController(@NonNull String str, @NonNull String str2) {
        if (str == null) {
            throw new IllegalArgumentException("routeId cannot be null");
        } else if (str2 != null) {
            return createRouteController(str, str2);
        } else {
            throw new IllegalArgumentException("routeGroupId cannot be null");
        }
    }

    public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest mediaRouteDiscoveryRequest) {
        if (this.mConnectionReady) {
            this.mActiveConnection.setDiscoveryRequest(mediaRouteDiscoveryRequest);
        }
        updateBinding();
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (DEBUG != null) {
            componentName = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this);
            stringBuilder.append(": Connected");
            Log.d(componentName, stringBuilder.toString());
        }
        if (this.mBound != null) {
            disconnect();
            componentName = iBinder != null ? new Messenger(iBinder) : null;
            if (MediaRouteProviderProtocol.isValidRemoteMessenger(componentName) != null) {
                iBinder = new Connection(componentName);
                if (iBinder.register() != null) {
                    this.mActiveConnection = iBinder;
                    return;
                } else if (DEBUG != null) {
                    componentName = TAG;
                    iBinder = new StringBuilder();
                    iBinder.append(this);
                    iBinder.append(": Registration failed");
                    Log.d(componentName, iBinder.toString());
                    return;
                } else {
                    return;
                }
            }
            componentName = TAG;
            iBinder = new StringBuilder();
            iBinder.append(this);
            iBinder.append(": Service returned invalid messenger binder");
            Log.e(componentName, iBinder.toString());
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        if (DEBUG != null) {
            componentName = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this);
            stringBuilder.append(": Service disconnected");
            Log.d(componentName, stringBuilder.toString());
        }
        disconnect();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Service connection ");
        stringBuilder.append(this.mComponentName.flattenToShortString());
        return stringBuilder.toString();
    }

    public boolean hasComponentName(String str, String str2) {
        return (this.mComponentName.getPackageName().equals(str) == null || this.mComponentName.getClassName().equals(str2) == null) ? null : true;
    }

    public void start() {
        if (!this.mStarted) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Starting");
                Log.d(str, stringBuilder.toString());
            }
            this.mStarted = true;
            updateBinding();
        }
    }

    public void stop() {
        if (this.mStarted) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Stopping");
                Log.d(str, stringBuilder.toString());
            }
            this.mStarted = false;
            updateBinding();
        }
    }

    public void rebindIfDisconnected() {
        if (this.mActiveConnection == null && shouldBind()) {
            unbind();
            bind();
        }
    }

    private void updateBinding() {
        if (shouldBind()) {
            bind();
        } else {
            unbind();
        }
    }

    private boolean shouldBind() {
        if (!this.mStarted || (getDiscoveryRequest() == null && this.mControllers.isEmpty())) {
            return false;
        }
        return true;
    }

    private void bind() {
        if (!this.mBound) {
            String str;
            StringBuilder stringBuilder;
            if (DEBUG) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Binding");
                Log.d(str, stringBuilder.toString());
            }
            Intent intent = new Intent("android.media.MediaRouteProviderService");
            intent.setComponent(this.mComponentName);
            try {
                this.mBound = getContext().bindService(intent, this, 1);
                if (!this.mBound && DEBUG) {
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(this);
                    stringBuilder.append(": Bind failed");
                    Log.d(str, stringBuilder.toString());
                }
            } catch (Throwable e) {
                if (DEBUG) {
                    String str2 = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(this);
                    stringBuilder2.append(": Bind failed");
                    Log.d(str2, stringBuilder2.toString(), e);
                }
            }
        }
    }

    private void unbind() {
        if (this.mBound) {
            if (DEBUG) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Unbinding");
                Log.d(str, stringBuilder.toString());
            }
            this.mBound = false;
            disconnect();
            getContext().unbindService(this);
        }
    }

    private RouteController createRouteController(String str, String str2) {
        MediaRouteProviderDescriptor descriptor = getDescriptor();
        if (descriptor != null) {
            List routes = descriptor.getRoutes();
            int size = routes.size();
            for (int i = 0; i < size; i++) {
                if (((MediaRouteDescriptor) routes.get(i)).getId().equals(str)) {
                    RouteController controller = new Controller(str, str2);
                    this.mControllers.add(controller);
                    if (this.mConnectionReady != null) {
                        controller.attachConnection(this.mActiveConnection);
                    }
                    updateBinding();
                    return controller;
                }
            }
        }
        return null;
    }

    void onConnectionReady(Connection connection) {
        if (this.mActiveConnection == connection) {
            this.mConnectionReady = true;
            attachControllersToConnection();
            connection = getDiscoveryRequest();
            if (connection != null) {
                this.mActiveConnection.setDiscoveryRequest(connection);
            }
        }
    }

    void onConnectionDied(Connection connection) {
        if (this.mActiveConnection == connection) {
            if (DEBUG != null) {
                connection = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Service connection died");
                Log.d(connection, stringBuilder.toString());
            }
            disconnect();
        }
    }

    void onConnectionError(Connection connection, String str) {
        if (this.mActiveConnection == connection) {
            if (DEBUG != null) {
                connection = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Service connection error - ");
                stringBuilder.append(str);
                Log.d(connection, stringBuilder.toString());
            }
            unbind();
        }
    }

    void onConnectionDescriptorChanged(Connection connection, MediaRouteProviderDescriptor mediaRouteProviderDescriptor) {
        if (this.mActiveConnection == connection) {
            if (DEBUG != null) {
                connection = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this);
                stringBuilder.append(": Descriptor changed, descriptor=");
                stringBuilder.append(mediaRouteProviderDescriptor);
                Log.d(connection, stringBuilder.toString());
            }
            setDescriptor(mediaRouteProviderDescriptor);
        }
    }

    private void disconnect() {
        if (this.mActiveConnection != null) {
            setDescriptor(null);
            this.mConnectionReady = false;
            detachControllersFromConnection();
            this.mActiveConnection.dispose();
            this.mActiveConnection = null;
        }
    }

    void onControllerReleased(Controller controller) {
        this.mControllers.remove(controller);
        controller.detachConnection();
        updateBinding();
    }

    private void attachControllersToConnection() {
        int size = this.mControllers.size();
        for (int i = 0; i < size; i++) {
            ((Controller) this.mControllers.get(i)).attachConnection(this.mActiveConnection);
        }
    }

    private void detachControllersFromConnection() {
        int size = this.mControllers.size();
        for (int i = 0; i < size; i++) {
            ((Controller) this.mControllers.get(i)).detachConnection();
        }
    }
}
