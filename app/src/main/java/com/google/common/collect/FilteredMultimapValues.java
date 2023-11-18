package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible
final class FilteredMultimapValues<K, V> extends AbstractCollection<V> {
    private final FilteredMultimap<K, V> multimap;

    FilteredMultimapValues(FilteredMultimap<K, V> filteredMultimap) {
        this.multimap = (FilteredMultimap) Preconditions.checkNotNull(filteredMultimap);
    }

    public Iterator<V> iterator() {
        return Maps.valueIterator(this.multimap.entries().iterator());
    }

    public boolean contains(@Nullable Object obj) {
        return this.multimap.containsValue(obj);
    }

    public int size() {
        return this.multimap.size();
    }

    public boolean remove(@Nullable Object obj) {
        Predicate entryPredicate = this.multimap.entryPredicate();
        Iterator it = this.multimap.unfiltered().entries().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (entryPredicate.apply(entry) && Objects.equal(entry.getValue(), obj)) {
                it.remove();
                return true;
            }
        }
        return null;
    }

    public boolean removeAll(Collection<?> collection) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(collection))));
    }

    public boolean retainAll(Collection<?> collection) {
        return Iterables.removeIf(this.multimap.unfiltered().entries(), Predicates.and(this.multimap.entryPredicate(), Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(collection)))));
    }

    public void clear() {
        this.multimap.clear();
    }
}
