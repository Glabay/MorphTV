package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.CycleDetectingLockFactory.ExampleStackTrace;
import com.google.common.util.concurrent.CycleDetectingLockFactory.Policy;
import com.google.common.util.concurrent.CycleDetectingLockFactory.PotentialDeadlockException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

class CycleDetectingLockFactory$LockGraphNode {
    final Map<CycleDetectingLockFactory$LockGraphNode, ExampleStackTrace> allowedPriorLocks = new MapMaker().weakKeys().makeMap();
    final Map<CycleDetectingLockFactory$LockGraphNode, PotentialDeadlockException> disallowedPriorLocks = new MapMaker().weakKeys().makeMap();
    final String lockName;

    CycleDetectingLockFactory$LockGraphNode(String str) {
        this.lockName = (String) Preconditions.checkNotNull(str);
    }

    String getLockName() {
        return this.lockName;
    }

    void checkAcquiredLocks(Policy policy, List<CycleDetectingLockFactory$LockGraphNode> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            checkAcquiredLock(policy, (CycleDetectingLockFactory$LockGraphNode) list.get(i));
        }
    }

    void checkAcquiredLock(Policy policy, CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode) {
        boolean z = this != cycleDetectingLockFactory$LockGraphNode;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Attempted to acquire multiple locks with the same rank ");
        stringBuilder.append(cycleDetectingLockFactory$LockGraphNode.getLockName());
        Preconditions.checkState(z, stringBuilder.toString());
        if (!this.allowedPriorLocks.containsKey(cycleDetectingLockFactory$LockGraphNode)) {
            PotentialDeadlockException potentialDeadlockException = (PotentialDeadlockException) this.disallowedPriorLocks.get(cycleDetectingLockFactory$LockGraphNode);
            if (potentialDeadlockException != null) {
                policy.handlePotentialDeadlock(new PotentialDeadlockException(cycleDetectingLockFactory$LockGraphNode, this, potentialDeadlockException.getConflictingStackTrace(), null));
                return;
            }
            ExampleStackTrace findPathTo = cycleDetectingLockFactory$LockGraphNode.findPathTo(this, Sets.newIdentityHashSet());
            if (findPathTo == null) {
                this.allowedPriorLocks.put(cycleDetectingLockFactory$LockGraphNode, new ExampleStackTrace(cycleDetectingLockFactory$LockGraphNode, this));
            } else {
                PotentialDeadlockException potentialDeadlockException2 = new PotentialDeadlockException(cycleDetectingLockFactory$LockGraphNode, this, findPathTo, null);
                this.disallowedPriorLocks.put(cycleDetectingLockFactory$LockGraphNode, potentialDeadlockException2);
                policy.handlePotentialDeadlock(potentialDeadlockException2);
            }
        }
    }

    @Nullable
    private ExampleStackTrace findPathTo(CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode, Set<CycleDetectingLockFactory$LockGraphNode> set) {
        if (!set.add(this)) {
            return null;
        }
        ExampleStackTrace exampleStackTrace = (ExampleStackTrace) this.allowedPriorLocks.get(cycleDetectingLockFactory$LockGraphNode);
        if (exampleStackTrace != null) {
            return exampleStackTrace;
        }
        for (Entry entry : this.allowedPriorLocks.entrySet()) {
            CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode2 = (CycleDetectingLockFactory$LockGraphNode) entry.getKey();
            Throwable findPathTo = cycleDetectingLockFactory$LockGraphNode2.findPathTo(cycleDetectingLockFactory$LockGraphNode, set);
            if (findPathTo != null) {
                cycleDetectingLockFactory$LockGraphNode = new ExampleStackTrace(cycleDetectingLockFactory$LockGraphNode2, this);
                cycleDetectingLockFactory$LockGraphNode.setStackTrace(((ExampleStackTrace) entry.getValue()).getStackTrace());
                cycleDetectingLockFactory$LockGraphNode.initCause(findPathTo);
                return cycleDetectingLockFactory$LockGraphNode;
            }
        }
        return null;
    }
}
