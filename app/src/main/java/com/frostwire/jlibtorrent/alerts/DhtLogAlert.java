package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.dht_log_alert;
import com.frostwire.jlibtorrent.swig.dht_log_alert.dht_module_t;

public final class DhtLogAlert extends AbstractAlert<dht_log_alert> {

    public enum DhtModule {
        TRACKER(dht_module_t.tracker.swigValue()),
        NODE(dht_module_t.node.swigValue()),
        ROUTING_TABLE(dht_module_t.routing_table.swigValue()),
        RPC_MANAGER(dht_module_t.rpc_manager.swigValue()),
        TRAVERSAL(dht_module_t.traversal.swigValue()),
        UNKNOWN(-1);
        
        private final int swigValue;

        private DhtModule(int i) {
            this.swigValue = i;
        }

        public int swig() {
            return this.swigValue;
        }

        public static DhtModule fromSwig(int i) {
            for (DhtModule dhtModule : (DhtModule[]) DhtModule.class.getEnumConstants()) {
                if (dhtModule.swig() == i) {
                    return dhtModule;
                }
            }
            return UNKNOWN;
        }
    }

    DhtLogAlert(dht_log_alert dht_log_alert) {
        super(dht_log_alert);
    }

    public String logMessage() {
        return ((dht_log_alert) this.alert).log_message();
    }

    public DhtModule module() {
        return DhtModule.fromSwig(((dht_log_alert) this.alert).getModule().swigValue());
    }
}
