package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Service.State;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
final class AbstractService$StateSnapshot {
    @Nullable
    final Throwable failure;
    final boolean shutdownWhenStartupFinishes;
    final State state;

    AbstractService$StateSnapshot(State state) {
        this(state, false, null);
    }

    AbstractService$StateSnapshot(State state, boolean z, @Nullable Throwable th) {
        boolean z2;
        if (z) {
            if (state != State.STARTING) {
                z2 = false;
                Preconditions.checkArgument(z2, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", state);
                Preconditions.checkArgument(((th == null ? 1 : 0) ^ (state != State.FAILED ? 1 : 0)) != 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", state, th);
                this.state = state;
                this.shutdownWhenStartupFinishes = z;
                this.failure = th;
            }
        }
        z2 = true;
        Preconditions.checkArgument(z2, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", state);
        if (th == null) {
        }
        if (state != State.FAILED) {
        }
        if (((th == null ? 1 : 0) ^ (state != State.FAILED ? 1 : 0)) != 0) {
        }
        Preconditions.checkArgument(((th == null ? 1 : 0) ^ (state != State.FAILED ? 1 : 0)) != 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", state, th);
        this.state = state;
        this.shutdownWhenStartupFinishes = z;
        this.failure = th;
    }

    State externalState() {
        if (this.shutdownWhenStartupFinishes && this.state == State.STARTING) {
            return State.STOPPING;
        }
        return this.state;
    }

    Throwable failureCause() {
        Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
        return this.failure;
    }
}
