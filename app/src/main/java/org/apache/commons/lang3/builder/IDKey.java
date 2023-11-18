package org.apache.commons.lang3.builder;

final class IDKey {
    private final int id;
    private final Object value;

    public IDKey(Object obj) {
        this.id = System.identityHashCode(obj);
        this.value = obj;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof IDKey)) {
            return false;
        }
        IDKey iDKey = (IDKey) obj;
        if (this.id != iDKey.id) {
            return false;
        }
        if (this.value == iDKey.value) {
            z = true;
        }
        return z;
    }
}
