package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@Beta
public class CycleDetectingLockFactory {
    private static final ThreadLocal<ArrayList<CycleDetectingLockFactory$LockGraphNode>> acquiredLocks = new C12201();
    private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, CycleDetectingLockFactory$LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
    private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
    final Policy policy;

    /* renamed from: com.google.common.util.concurrent.CycleDetectingLockFactory$1 */
    static class C12201 extends ThreadLocal<ArrayList<CycleDetectingLockFactory$LockGraphNode>> {
        C12201() {
        }

        protected ArrayList<CycleDetectingLockFactory$LockGraphNode> initialValue() {
            return Lists.newArrayListWithCapacity(3);
        }
    }

    private interface CycleDetectingLock {
        CycleDetectingLockFactory$LockGraphNode getLockGraphNode();

        boolean isAcquiredByCurrentThread();
    }

    final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLock {
        private final CycleDetectingLockFactory$LockGraphNode lockGraphNode;

        private CycleDetectingReentrantLock(CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode, boolean z) {
            super(z);
            this.lockGraphNode = (CycleDetectingLockFactory$LockGraphNode) Preconditions.checkNotNull(cycleDetectingLockFactory$LockGraphNode);
        }

        public CycleDetectingLockFactory$LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        public boolean isAcquiredByCurrentThread() {
            return isHeldByCurrentThread();
        }

        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lockInterruptibly();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                boolean tryLock = super.tryLock();
                return tryLock;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                j = super.tryLock(j, timeUnit);
                return j;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }

        public void unlock() {
            try {
                super.unlock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
    }

    private class CycleDetectingReentrantReadLock extends ReadLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantReadLock(CycleDetectingReentrantReadWriteLock cycleDetectingReentrantReadWriteLock) {
            super(cycleDetectingReentrantReadWriteLock);
            this.readWriteLock = cycleDetectingReentrantReadWriteLock;
        }

        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean tryLock = super.tryLock();
                return tryLock;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                j = super.tryLock(j, timeUnit);
                return j;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public void unlock() {
            try {
                super.unlock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }

    final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLock {
        private final CycleDetectingLockFactory$LockGraphNode lockGraphNode;
        private final CycleDetectingReentrantReadLock readLock;
        private final CycleDetectingReentrantWriteLock writeLock;

        private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode, boolean z) {
            super(z);
            this.readLock = new CycleDetectingReentrantReadLock(this);
            this.writeLock = new CycleDetectingReentrantWriteLock(this);
            this.lockGraphNode = (CycleDetectingLockFactory$LockGraphNode) Preconditions.checkNotNull(cycleDetectingLockFactory$LockGraphNode);
        }

        public ReadLock readLock() {
            return this.readLock;
        }

        public WriteLock writeLock() {
            return this.writeLock;
        }

        public CycleDetectingLockFactory$LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }

        public boolean isAcquiredByCurrentThread() {
            if (!isWriteLockedByCurrentThread()) {
                if (getReadHoldCount() <= 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private class CycleDetectingReentrantWriteLock extends WriteLock {
        final CycleDetectingReentrantReadWriteLock readWriteLock;

        CycleDetectingReentrantWriteLock(CycleDetectingReentrantReadWriteLock cycleDetectingReentrantReadWriteLock) {
            super(cycleDetectingReentrantReadWriteLock);
            this.readWriteLock = cycleDetectingReentrantReadWriteLock;
        }

        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                boolean tryLock = super.tryLock();
                return tryLock;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                j = super.tryLock(j, timeUnit);
                return j;
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }

        public void unlock() {
            try {
                super.unlock();
            } finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }

    private static class ExampleStackTrace extends IllegalStateException {
        static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
        static Set<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), CycleDetectingLockFactory$LockGraphNode.class.getName());

        ExampleStackTrace(CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode, CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(cycleDetectingLockFactory$LockGraphNode.getLockName());
            stringBuilder.append(" -> ");
            stringBuilder.append(cycleDetectingLockFactory$LockGraphNode2.getLockName());
            super(stringBuilder.toString());
            cycleDetectingLockFactory$LockGraphNode = getStackTrace();
            cycleDetectingLockFactory$LockGraphNode2 = cycleDetectingLockFactory$LockGraphNode.length;
            int i = 0;
            while (i < cycleDetectingLockFactory$LockGraphNode2) {
                if (WithExplicitOrdering.class.getName().equals(cycleDetectingLockFactory$LockGraphNode[i].getClassName())) {
                    setStackTrace(EMPTY_STACK_TRACE);
                    return;
                } else if (EXCLUDED_CLASS_NAMES.contains(cycleDetectingLockFactory$LockGraphNode[i].getClassName())) {
                    i++;
                } else {
                    setStackTrace((StackTraceElement[]) Arrays.copyOfRange(cycleDetectingLockFactory$LockGraphNode, i, cycleDetectingLockFactory$LockGraphNode2));
                    return;
                }
            }
        }
    }

    @ThreadSafe
    @Beta
    public interface Policy {
        void handlePotentialDeadlock(PotentialDeadlockException potentialDeadlockException);
    }

    @Beta
    public enum Policies implements Policy {
        THROW {
            public void handlePotentialDeadlock(PotentialDeadlockException potentialDeadlockException) {
                throw potentialDeadlockException;
            }
        },
        WARN {
            public void handlePotentialDeadlock(PotentialDeadlockException potentialDeadlockException) {
                CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", potentialDeadlockException);
            }
        },
        DISABLED {
            public void handlePotentialDeadlock(PotentialDeadlockException potentialDeadlockException) {
            }
        }
    }

    @Beta
    public static final class PotentialDeadlockException extends ExampleStackTrace {
        private final ExampleStackTrace conflictingStackTrace;

        private PotentialDeadlockException(CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode, CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode2, ExampleStackTrace exampleStackTrace) {
            super(cycleDetectingLockFactory$LockGraphNode, cycleDetectingLockFactory$LockGraphNode2);
            this.conflictingStackTrace = exampleStackTrace;
            initCause(exampleStackTrace);
        }

        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }

        public String getMessage() {
            StringBuilder stringBuilder = new StringBuilder(super.getMessage());
            for (Throwable th = this.conflictingStackTrace; th != null; th = th.getCause()) {
                stringBuilder.append(", ");
                stringBuilder.append(th.getMessage());
            }
            return stringBuilder.toString();
        }
    }

    @Beta
    public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory {
        private final Map<E, CycleDetectingLockFactory$LockGraphNode> lockGraphNodes;

        @VisibleForTesting
        WithExplicitOrdering(Policy policy, Map<E, CycleDetectingLockFactory$LockGraphNode> map) {
            super(policy);
            this.lockGraphNodes = map;
        }

        public ReentrantLock newReentrantLock(E e) {
            return newReentrantLock(e, false);
        }

        public ReentrantLock newReentrantLock(E e, boolean z) {
            return this.policy == Policies.DISABLED ? new ReentrantLock(z) : new CycleDetectingReentrantLock((CycleDetectingLockFactory$LockGraphNode) this.lockGraphNodes.get(e), z);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E e) {
            return newReentrantReadWriteLock(e, false);
        }

        public ReentrantReadWriteLock newReentrantReadWriteLock(E e, boolean z) {
            return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(z) : new CycleDetectingReentrantReadWriteLock((CycleDetectingLockFactory$LockGraphNode) this.lockGraphNodes.get(e), z);
        }
    }

    public static CycleDetectingLockFactory newInstance(Policy policy) {
        return new CycleDetectingLockFactory(policy);
    }

    public ReentrantLock newReentrantLock(String str) {
        return newReentrantLock(str, false);
    }

    public ReentrantLock newReentrantLock(String str, boolean z) {
        return this.policy == Policies.DISABLED ? new ReentrantLock(z) : new CycleDetectingReentrantLock(new CycleDetectingLockFactory$LockGraphNode(str), z);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String str) {
        return newReentrantReadWriteLock(str, false);
    }

    public ReentrantReadWriteLock newReentrantReadWriteLock(String str, boolean z) {
        return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(z) : new CycleDetectingReentrantReadWriteLock(new CycleDetectingLockFactory$LockGraphNode(str), z);
    }

    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> cls, Policy policy) {
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(policy);
        return new WithExplicitOrdering(policy, getOrCreateNodes(cls));
    }

    private static Map<? extends Enum, CycleDetectingLockFactory$LockGraphNode> getOrCreateNodes(Class<? extends Enum> cls) {
        Map<? extends Enum, CycleDetectingLockFactory$LockGraphNode> map = (Map) lockGraphNodesPerType.get(cls);
        if (map != null) {
            return map;
        }
        Map createNodes = createNodes(cls);
        return (Map) MoreObjects.firstNonNull((Map) lockGraphNodesPerType.putIfAbsent(cls, createNodes), createNodes);
    }

    @VisibleForTesting
    static <E extends Enum<E>> Map<E, CycleDetectingLockFactory$LockGraphNode> createNodes(Class<E> cls) {
        Map newEnumMap = Maps.newEnumMap(cls);
        Enum[] enumArr = (Enum[]) cls.getEnumConstants();
        int length = enumArr.length;
        ArrayList newArrayListWithCapacity = Lists.newArrayListWithCapacity(length);
        int i = 0;
        for (Enum enumR : enumArr) {
            CycleDetectingLockFactory$LockGraphNode cycleDetectingLockFactory$LockGraphNode = new CycleDetectingLockFactory$LockGraphNode(getLockName(enumR));
            newArrayListWithCapacity.add(cycleDetectingLockFactory$LockGraphNode);
            newEnumMap.put(enumR, cycleDetectingLockFactory$LockGraphNode);
        }
        for (cls = true; cls < length; cls++) {
            ((CycleDetectingLockFactory$LockGraphNode) newArrayListWithCapacity.get(cls)).checkAcquiredLocks(Policies.THROW, newArrayListWithCapacity.subList(0, cls));
        }
        while (i < length - 1) {
            i++;
            ((CycleDetectingLockFactory$LockGraphNode) newArrayListWithCapacity.get(i)).checkAcquiredLocks(Policies.DISABLED, newArrayListWithCapacity.subList(i, length));
        }
        return Collections.unmodifiableMap(newEnumMap);
    }

    private static String getLockName(Enum<?> enumR) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(enumR.getDeclaringClass().getSimpleName());
        stringBuilder.append(".");
        stringBuilder.append(enumR.name());
        return stringBuilder.toString();
    }

    private CycleDetectingLockFactory(Policy policy) {
        this.policy = (Policy) Preconditions.checkNotNull(policy);
    }

    private void aboutToAcquire(CycleDetectingLock cycleDetectingLock) {
        if (!cycleDetectingLock.isAcquiredByCurrentThread()) {
            ArrayList arrayList = (ArrayList) acquiredLocks.get();
            cycleDetectingLock = cycleDetectingLock.getLockGraphNode();
            cycleDetectingLock.checkAcquiredLocks(this.policy, arrayList);
            arrayList.add(cycleDetectingLock);
        }
    }

    private void lockStateChanged(CycleDetectingLock cycleDetectingLock) {
        if (!cycleDetectingLock.isAcquiredByCurrentThread()) {
            ArrayList arrayList = (ArrayList) acquiredLocks.get();
            cycleDetectingLock = cycleDetectingLock.getLockGraphNode();
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (arrayList.get(size) == cycleDetectingLock) {
                    arrayList.remove(size);
                    return;
                }
            }
        }
    }
}
