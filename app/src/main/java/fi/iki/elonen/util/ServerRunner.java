package fi.iki.elonen.util;

import fi.iki.elonen.NanoHTTPD;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRunner {
    private static final Logger LOG = Logger.getLogger(ServerRunner.class.getName());

    public static void executeInstance(fi.iki.elonen.NanoHTTPD r4) {
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
        r0 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        r1 = 0;
        r4.start(r0, r1);	 Catch:{ IOException -> 0x0007 }
        goto L_0x0022;
    L_0x0007:
        r0 = move-exception;
        r1 = java.lang.System.err;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Couldn't start server:\n";
        r2.append(r3);
        r2.append(r0);
        r0 = r2.toString();
        r1.println(r0);
        r0 = -1;
        java.lang.System.exit(r0);
    L_0x0022:
        r0 = java.lang.System.out;
        r1 = "Server started, Hit Enter to stop.\n";
        r0.println(r1);
        r0 = java.lang.System.in;	 Catch:{ Throwable -> 0x002e }
        r0.read();	 Catch:{ Throwable -> 0x002e }
    L_0x002e:
        r4.stop();
        r4 = java.lang.System.out;
        r0 = "Server stopped.\n";
        r4.println(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: fi.iki.elonen.util.ServerRunner.executeInstance(fi.iki.elonen.NanoHTTPD):void");
    }

    public static <T extends NanoHTTPD> void run(Class<T> cls) {
        try {
            executeInstance((NanoHTTPD) cls.newInstance());
        } catch (Class<T> cls2) {
            LOG.log(Level.SEVERE, "Cound nor create server", cls2);
        }
    }
}
