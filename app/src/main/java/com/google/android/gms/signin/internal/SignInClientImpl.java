package com.google.android.gms.signin.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.GmsClient;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.ISignInService.Stub;

public class SignInClientImpl extends GmsClient<ISignInService> implements SignInClient {
    public static final String ACTION_START_SERVICE = "com.google.android.gms.signin.service.START";
    public static final String INTERNAL_ACTION_START_SERVICE = "com.google.android.gms.signin.service.INTERNAL_START";
    public static final String KEY_AUTH_API_SIGN_IN_MODULE_VERSION = "com.google.android.gms.signin.internal.authApiSignInModuleVersion";
    public static final String KEY_CLIENT_REQUESTED_ACCOUNT = "com.google.android.gms.signin.internal.clientRequestedAccount";
    public static final String KEY_FORCE_CODE_FOR_REFRESH_TOKEN = "com.google.android.gms.signin.internal.forceCodeForRefreshToken";
    public static final String KEY_HOSTED_DOMAIN = "com.google.android.gms.signin.internal.hostedDomain";
    public static final String KEY_ID_TOKEN_REQUESTED = "com.google.android.gms.signin.internal.idTokenRequested";
    @Deprecated
    public static final String KEY_OFFLINE_ACCESS_CALLBACKS = "com.google.android.gms.signin.internal.signInCallbacks";
    public static final String KEY_OFFLINE_ACCESS_REQUESTED = "com.google.android.gms.signin.internal.offlineAccessRequested";
    public static final String KEY_REAL_CLIENT_LIBRARY_VERSION = "com.google.android.gms.signin.internal.realClientLibraryVersion";
    public static final String KEY_REAL_CLIENT_PACKAGE_NAME = "com.google.android.gms.signin.internal.realClientPackageName";
    public static final String KEY_SERVER_CLIENT_ID = "com.google.android.gms.signin.internal.serverClientId";
    public static final String KEY_USE_PROMPT_MODE_FOR_AUTH_CODE = "com.google.android.gms.signin.internal.usePromptModeForAuthCode";
    public static final String KEY_WAIT_FOR_ACCESS_TOKEN_REFRESH = "com.google.android.gms.signin.internal.waitForAccessTokenRefresh";
    private final Bundle zzada;
    private final boolean zzads;
    private final ClientSettings zzgf;
    private Integer zzsc;

    public SignInClientImpl(Context context, Looper looper, boolean z, ClientSettings clientSettings, Bundle bundle, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, clientSettings, connectionCallbacks, onConnectionFailedListener);
        this.zzads = z;
        this.zzgf = clientSettings;
        this.zzada = bundle;
        this.zzsc = clientSettings.getClientSessionId();
    }

    public SignInClientImpl(Context context, Looper looper, boolean z, ClientSettings clientSettings, SignInOptions signInOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, z, clientSettings, createBundleFromClientSettings(clientSettings), connectionCallbacks, onConnectionFailedListener);
    }

    public static Bundle createBundleFromClientSettings(ClientSettings clientSettings) {
        SignInOptions signInOptions = clientSettings.getSignInOptions();
        Integer clientSessionId = clientSettings.getClientSessionId();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_CLIENT_REQUESTED_ACCOUNT, clientSettings.getAccount());
        if (clientSessionId != null) {
            bundle.putInt(ClientSettings.KEY_CLIENT_SESSION_ID, clientSessionId.intValue());
        }
        if (signInOptions != null) {
            bundle.putBoolean(KEY_OFFLINE_ACCESS_REQUESTED, signInOptions.isOfflineAccessRequested());
            bundle.putBoolean(KEY_ID_TOKEN_REQUESTED, signInOptions.isIdTokenRequested());
            bundle.putString(KEY_SERVER_CLIENT_ID, signInOptions.getServerClientId());
            bundle.putBoolean(KEY_USE_PROMPT_MODE_FOR_AUTH_CODE, true);
            bundle.putBoolean(KEY_FORCE_CODE_FOR_REFRESH_TOKEN, signInOptions.isForceCodeForRefreshToken());
            bundle.putString(KEY_HOSTED_DOMAIN, signInOptions.getHostedDomain());
            bundle.putBoolean(KEY_WAIT_FOR_ACCESS_TOKEN_REFRESH, signInOptions.waitForAccessTokenRefresh());
            if (signInOptions.getAuthApiSignInModuleVersion() != null) {
                bundle.putLong(KEY_AUTH_API_SIGN_IN_MODULE_VERSION, signInOptions.getAuthApiSignInModuleVersion().longValue());
            }
            if (signInOptions.getRealClientLibraryVersion() != null) {
                bundle.putLong(KEY_REAL_CLIENT_LIBRARY_VERSION, signInOptions.getRealClientLibraryVersion().longValue());
            }
        }
        return bundle;
    }

    public void clearAccountFromSessionStore() {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.getService();	 Catch:{ RemoteException -> 0x0010 }
        r0 = (com.google.android.gms.signin.internal.ISignInService) r0;	 Catch:{ RemoteException -> 0x0010 }
        r1 = r2.zzsc;	 Catch:{ RemoteException -> 0x0010 }
        r1 = r1.intValue();	 Catch:{ RemoteException -> 0x0010 }
        r0.clearAccountFromSessionStore(r1);	 Catch:{ RemoteException -> 0x0010 }
        return;
    L_0x0010:
        r0 = "SignInClientImpl";
        r1 = "Remote service probably died when clearAccountFromSessionStore is called";
        android.util.Log.w(r0, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.signin.internal.SignInClientImpl.clearAccountFromSessionStore():void");
    }

    public void connect() {
        connect(new LegacyClientCallbackAdapter(this));
    }

    protected ISignInService createServiceInterface(IBinder iBinder) {
        return Stub.asInterface(iBinder);
    }

    protected Bundle getGetServiceRequestExtraArgs() {
        if (!getContext().getPackageName().equals(this.zzgf.getRealClientPackageName())) {
            this.zzada.putString(KEY_REAL_CLIENT_PACKAGE_NAME, this.zzgf.getRealClientPackageName());
        }
        return this.zzada;
    }

    public int getMinApkVersion() {
        return GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    protected String getServiceDescriptor() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    protected String getStartServiceAction() {
        return ACTION_START_SERVICE;
    }

    public boolean requiresSignIn() {
        return this.zzads;
    }

    public void saveDefaultAccount(com.google.android.gms.common.internal.IAccountAccessor r3, boolean r4) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r2 = this;
        r0 = r2.getService();	 Catch:{ RemoteException -> 0x0010 }
        r0 = (com.google.android.gms.signin.internal.ISignInService) r0;	 Catch:{ RemoteException -> 0x0010 }
        r1 = r2.zzsc;	 Catch:{ RemoteException -> 0x0010 }
        r1 = r1.intValue();	 Catch:{ RemoteException -> 0x0010 }
        r0.saveDefaultAccountToSharedPref(r3, r1, r4);	 Catch:{ RemoteException -> 0x0010 }
        return;
    L_0x0010:
        r3 = "SignInClientImpl";
        r4 = "Remote service probably died when saveDefaultAccount is called";
        android.util.Log.w(r3, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.signin.internal.SignInClientImpl.saveDefaultAccount(com.google.android.gms.common.internal.IAccountAccessor, boolean):void");
    }

    public void signIn(com.google.android.gms.signin.internal.ISignInCallbacks r5) {
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
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1281025083.run(Unknown Source)
*/
        /*
        r4 = this;
        r0 = "Expecting a valid ISignInCallbacks";
        com.google.android.gms.common.internal.Preconditions.checkNotNull(r5, r0);
        r0 = r4.zzgf;	 Catch:{ RemoteException -> 0x003c }
        r0 = r0.getAccountOrDefault();	 Catch:{ RemoteException -> 0x003c }
        r1 = 0;	 Catch:{ RemoteException -> 0x003c }
        r2 = "<<default account>>";	 Catch:{ RemoteException -> 0x003c }
        r3 = r0.name;	 Catch:{ RemoteException -> 0x003c }
        r2 = r2.equals(r3);	 Catch:{ RemoteException -> 0x003c }
        if (r2 == 0) goto L_0x0022;	 Catch:{ RemoteException -> 0x003c }
    L_0x0016:
        r1 = r4.getContext();	 Catch:{ RemoteException -> 0x003c }
        r1 = com.google.android.gms.auth.api.signin.internal.Storage.getInstance(r1);	 Catch:{ RemoteException -> 0x003c }
        r1 = r1.getSavedDefaultGoogleSignInAccount();	 Catch:{ RemoteException -> 0x003c }
    L_0x0022:
        r2 = new com.google.android.gms.common.internal.ResolveAccountRequest;	 Catch:{ RemoteException -> 0x003c }
        r3 = r4.zzsc;	 Catch:{ RemoteException -> 0x003c }
        r3 = r3.intValue();	 Catch:{ RemoteException -> 0x003c }
        r2.<init>(r0, r3, r1);	 Catch:{ RemoteException -> 0x003c }
        r0 = r4.getService();	 Catch:{ RemoteException -> 0x003c }
        r0 = (com.google.android.gms.signin.internal.ISignInService) r0;	 Catch:{ RemoteException -> 0x003c }
        r1 = new com.google.android.gms.signin.internal.SignInRequest;	 Catch:{ RemoteException -> 0x003c }
        r1.<init>(r2);	 Catch:{ RemoteException -> 0x003c }
        r0.signIn(r1, r5);	 Catch:{ RemoteException -> 0x003c }
        return;
    L_0x003c:
        r0 = move-exception;
        r1 = "SignInClientImpl";
        r2 = "Remote service probably died when signIn is called";
        android.util.Log.w(r1, r2);
        r1 = new com.google.android.gms.signin.internal.SignInResponse;	 Catch:{ RemoteException -> 0x004f }
        r2 = 8;	 Catch:{ RemoteException -> 0x004f }
        r1.<init>(r2);	 Catch:{ RemoteException -> 0x004f }
        r5.onSignInComplete(r1);	 Catch:{ RemoteException -> 0x004f }
        return;
    L_0x004f:
        r5 = "SignInClientImpl";
        r1 = "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.";
        android.util.Log.wtf(r5, r1, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.signin.internal.SignInClientImpl.signIn(com.google.android.gms.signin.internal.ISignInCallbacks):void");
    }
}
