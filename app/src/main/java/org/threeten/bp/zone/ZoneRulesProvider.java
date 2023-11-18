package org.threeten.bp.zone;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.threeten.bp.jdk8.Jdk8Methods;

public abstract class ZoneRulesProvider {
    private static final CopyOnWriteArrayList<ZoneRulesProvider> PROVIDERS = new CopyOnWriteArrayList();
    private static final ConcurrentMap<String, ZoneRulesProvider> ZONES = new ConcurrentHashMap(512, 0.75f, 2);

    protected boolean provideRefresh() {
        return false;
    }

    protected abstract ZoneRules provideRules(String str, boolean z);

    protected abstract NavigableMap<String, ZoneRules> provideVersions(String str);

    protected abstract Set<String> provideZoneIds();

    static {
        Iterator it = ServiceLoader.load(ZoneRulesProvider.class, ZoneRulesProvider.class.getClassLoader()).iterator();
        while (it.hasNext()) {
            try {
                registerProvider0((ZoneRulesProvider) it.next());
            } catch (ServiceConfigurationError e) {
                if (!(e.getCause() instanceof SecurityException)) {
                    throw e;
                }
            }
        }
    }

    public static Set<String> getAvailableZoneIds() {
        return new HashSet(ZONES.keySet());
    }

    public static ZoneRules getRules(String str, boolean z) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        return getProvider(str).provideRules(str, z);
    }

    public static NavigableMap<String, ZoneRules> getVersions(String str) {
        Jdk8Methods.requireNonNull(str, "zoneId");
        return getProvider(str).provideVersions(str);
    }

    private static ZoneRulesProvider getProvider(String str) {
        ZoneRulesProvider zoneRulesProvider = (ZoneRulesProvider) ZONES.get(str);
        if (zoneRulesProvider != null) {
            return zoneRulesProvider;
        }
        if (ZONES.isEmpty()) {
            throw new ZoneRulesException("No time-zone data files registered");
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown time-zone ID: ");
        stringBuilder.append(str);
        throw new ZoneRulesException(stringBuilder.toString());
    }

    public static void registerProvider(ZoneRulesProvider zoneRulesProvider) {
        Jdk8Methods.requireNonNull(zoneRulesProvider, "provider");
        registerProvider0(zoneRulesProvider);
        PROVIDERS.add(zoneRulesProvider);
    }

    private static void registerProvider0(ZoneRulesProvider zoneRulesProvider) {
        for (String str : zoneRulesProvider.provideZoneIds()) {
            Jdk8Methods.requireNonNull(str, "zoneId");
            if (((ZoneRulesProvider) ZONES.putIfAbsent(str, zoneRulesProvider)) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to register zone as one already registered with that ID: ");
                stringBuilder.append(str);
                stringBuilder.append(", currently loading from provider: ");
                stringBuilder.append(zoneRulesProvider);
                throw new ZoneRulesException(stringBuilder.toString());
            }
        }
    }

    public static boolean refresh() {
        Iterator it = PROVIDERS.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z |= ((ZoneRulesProvider) it.next()).provideRefresh();
        }
        return z;
    }

    protected ZoneRulesProvider() {
    }
}
