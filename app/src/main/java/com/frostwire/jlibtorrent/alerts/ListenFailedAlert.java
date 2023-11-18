package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.Address;
import com.frostwire.jlibtorrent.ErrorCode;
import com.frostwire.jlibtorrent.Operation;
import com.frostwire.jlibtorrent.swig.listen_failed_alert;

public final class ListenFailedAlert extends AbstractAlert<listen_failed_alert> {
    ListenFailedAlert(listen_failed_alert listen_failed_alert) {
        super(listen_failed_alert);
    }

    public String listenInterface() {
        return ((listen_failed_alert) this.alert).listen_interface();
    }

    public ErrorCode error() {
        return new ErrorCode(((listen_failed_alert) this.alert).getError());
    }

    public Operation operation() {
        return Operation.fromSwig(((listen_failed_alert) this.alert).getOp());
    }

    public SocketType socketType() {
        return SocketType.fromSwig(((listen_failed_alert) this.alert).getSocket_type().swigValue());
    }

    public Address address() {
        return new Address(((listen_failed_alert) this.alert).get_address());
    }

    public int port() {
        return ((listen_failed_alert) this.alert).getPort();
    }
}
