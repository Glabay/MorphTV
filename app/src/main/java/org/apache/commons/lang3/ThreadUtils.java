package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ThreadUtils {
    public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();

    public interface ThreadPredicate {
        boolean test(Thread thread);
    }

    public interface ThreadGroupPredicate {
        boolean test(ThreadGroup threadGroup);
    }

    private static final class AlwaysTruePredicate implements ThreadPredicate, ThreadGroupPredicate {
        public boolean test(Thread thread) {
            return true;
        }

        public boolean test(ThreadGroup threadGroup) {
            return true;
        }

        private AlwaysTruePredicate() {
        }
    }

    public static class NamePredicate implements ThreadPredicate, ThreadGroupPredicate {
        private final String name;

        public NamePredicate(String str) {
            if (str == null) {
                throw new IllegalArgumentException("The name must not be null");
            }
            this.name = str;
        }

        public boolean test(ThreadGroup threadGroup) {
            return (threadGroup == null || threadGroup.getName().equals(this.name) == null) ? null : true;
        }

        public boolean test(Thread thread) {
            return (thread == null || thread.getName().equals(this.name) == null) ? null : true;
        }
    }

    public static class ThreadIdPredicate implements ThreadPredicate {
        private final long threadId;

        public ThreadIdPredicate(long j) {
            if (j <= 0) {
                throw new IllegalArgumentException("The thread id must be greater than zero");
            }
            this.threadId = j;
        }

        public boolean test(Thread thread) {
            return (thread == null || thread.getId() != this.threadId) ? null : true;
        }
    }

    public static Thread findThreadById(long j, ThreadGroup threadGroup) {
        if (threadGroup == null) {
            throw new IllegalArgumentException("The thread group must not be null");
        }
        j = findThreadById(j);
        return (j == null || !threadGroup.equals(j.getThreadGroup())) ? 0 : j;
    }

    public static Thread findThreadById(long j, String str) {
        if (str == null) {
            throw new IllegalArgumentException("The thread group name must not be null");
        }
        j = findThreadById(j);
        return (j == null || j.getThreadGroup() == null || !j.getThreadGroup().getName().equals(str)) ? 0 : j;
    }

    public static Collection<Thread> findThreadsByName(String str, ThreadGroup threadGroup) {
        return findThreads(threadGroup, null, new NamePredicate(str));
    }

    public static Collection<Thread> findThreadsByName(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("The thread name must not be null");
        } else if (str2 == null) {
            throw new IllegalArgumentException("The thread group name must not be null");
        } else {
            String<ThreadGroup> findThreadGroups = findThreadGroups(new NamePredicate(str2));
            if (findThreadGroups.isEmpty()) {
                return Collections.emptyList();
            }
            Collection arrayList = new ArrayList();
            ThreadPredicate namePredicate = new NamePredicate(str);
            for (ThreadGroup findThreads : findThreadGroups) {
                arrayList.addAll(findThreads(findThreads, false, namePredicate));
            }
            return Collections.unmodifiableCollection(arrayList);
        }
    }

    public static Collection<ThreadGroup> findThreadGroupsByName(String str) {
        return findThreadGroups(new NamePredicate(str));
    }

    public static Collection<ThreadGroup> getAllThreadGroups() {
        return findThreadGroups(ALWAYS_TRUE_PREDICATE);
    }

    public static ThreadGroup getSystemThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }

    public static Collection<Thread> getAllThreads() {
        return findThreads(ALWAYS_TRUE_PREDICATE);
    }

    public static Collection<Thread> findThreadsByName(String str) {
        return findThreads(new NamePredicate(str));
    }

    public static Thread findThreadById(long j) {
        j = findThreads(new ThreadIdPredicate(j));
        return j.isEmpty() ? 0 : (Thread) j.iterator().next();
    }

    public static Collection<Thread> findThreads(ThreadPredicate threadPredicate) {
        return findThreads(getSystemThreadGroup(), true, threadPredicate);
    }

    public static Collection<ThreadGroup> findThreadGroups(ThreadGroupPredicate threadGroupPredicate) {
        return findThreadGroups(getSystemThreadGroup(), true, threadGroupPredicate);
    }

    public static Collection<Thread> findThreads(ThreadGroup threadGroup, boolean z, ThreadPredicate threadPredicate) {
        if (threadGroup == null) {
            throw new IllegalArgumentException("The group must not be null");
        } else if (threadPredicate == null) {
            throw new IllegalArgumentException("The predicate must not be null");
        } else {
            Thread[] threadArr;
            boolean enumerate;
            int activeCount = threadGroup.activeCount();
            while (true) {
                threadArr = new Thread[((activeCount + (activeCount / 2)) + 1)];
                enumerate = threadGroup.enumerate(threadArr, z);
                if (enumerate < threadArr.length) {
                    break;
                }
                boolean z2 = enumerate;
            }
            threadGroup = new ArrayList(enumerate);
            for (z = false; z < enumerate; z++) {
                if (threadPredicate.test(threadArr[z])) {
                    threadGroup.add(threadArr[z]);
                }
            }
            return Collections.unmodifiableCollection(threadGroup);
        }
    }

    public static Collection<ThreadGroup> findThreadGroups(ThreadGroup threadGroup, boolean z, ThreadGroupPredicate threadGroupPredicate) {
        if (threadGroup == null) {
            throw new IllegalArgumentException("The group must not be null");
        } else if (threadGroupPredicate == null) {
            throw new IllegalArgumentException("The predicate must not be null");
        } else {
            ThreadGroup[] threadGroupArr;
            boolean enumerate;
            int activeGroupCount = threadGroup.activeGroupCount();
            while (true) {
                threadGroupArr = new ThreadGroup[((activeGroupCount + (activeGroupCount / 2)) + 1)];
                enumerate = threadGroup.enumerate(threadGroupArr, z);
                if (enumerate < threadGroupArr.length) {
                    break;
                }
                boolean z2 = enumerate;
            }
            threadGroup = new ArrayList(enumerate);
            for (z = false; z < enumerate; z++) {
                if (threadGroupPredicate.test(threadGroupArr[z])) {
                    threadGroup.add(threadGroupArr[z]);
                }
            }
            return Collections.unmodifiableCollection(threadGroup);
        }
    }
}
