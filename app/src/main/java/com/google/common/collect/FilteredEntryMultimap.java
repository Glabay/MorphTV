package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
class FilteredEntryMultimap<K, V> extends AbstractMultimap<K, V> implements FilteredMultimap<K, V> {
    final Predicate<? super Entry<K, V>> predicate;
    final Multimap<K, V> unfiltered;

    class AsMap extends ViewCachingAbstractMap<K, Collection<V>> {

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$2 */
        class C09002 extends EntrySet<K, Collection<V>> {

            /* renamed from: com.google.common.collect.FilteredEntryMultimap$AsMap$2$1 */
            class C08991 extends AbstractIterator<Entry<K, Collection<V>>> {
                final Iterator<Entry<K, Collection<V>>> backingIterator = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();

                C08991() {
                }

                protected Entry<K, Collection<V>> computeNext() {
                    while (this.backingIterator.hasNext()) {
                        Entry entry = (Entry) this.backingIterator.next();
                        Object key = entry.getKey();
                        Collection filterCollection = FilteredEntryMultimap.filterCollection((Collection) entry.getValue(), new ValuePredicate(key));
                        if (!filterCollection.isEmpty()) {
                            return Maps.immutableEntry(key, filterCollection);
                        }
                    }
                    return (Entry) endOfData();
                }
            }

            C09002() {
            }

            Map<K, Collection<V>> map() {
                return AsMap.this;
            }

            public Iterator<Entry<K, Collection<V>>> iterator() {
                return new C08991();
            }

            public boolean removeAll(Collection<?> collection) {
                return FilteredEntryMultimap.this.removeEntriesIf(Predicates.in(collection));
            }

            public boolean retainAll(Collection<?> collection) {
                return FilteredEntryMultimap.this.removeEntriesIf(Predicates.not(Predicates.in(collection)));
            }

            public int size() {
                return Iterators.size(iterator());
            }
        }

        AsMap() {
        }

        public boolean containsKey(@Nullable Object obj) {
            return get(obj) != null ? true : null;
        }

        public void clear() {
            FilteredEntryMultimap.this.clear();
        }

        public Collection<V> get(@Nullable Object obj) {
            Collection collection = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(obj);
            if (collection == null) {
                return null;
            }
            obj = FilteredEntryMultimap.filterCollection(collection, new ValuePredicate(obj));
            if (obj.isEmpty()) {
                obj = null;
            }
            return obj;
        }

        public Collection<V> remove(@Nullable Object obj) {
            Collection collection = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(obj);
            if (collection == null) {
                return null;
            }
            Object newArrayList = Lists.newArrayList();
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                Object next = it.next();
                if (FilteredEntryMultimap.this.satisfies(obj, next)) {
                    it.remove();
                    newArrayList.add(next);
                }
            }
            if (newArrayList.isEmpty() != null) {
                return null;
            }
            if ((FilteredEntryMultimap.this.unfiltered instanceof SetMultimap) != null) {
                return Collections.unmodifiableSet(Sets.newLinkedHashSet(newArrayList));
            }
            return Collections.unmodifiableList(newArrayList);
        }

        Set<K> createKeySet() {
            return new KeySet<K, Collection<V>>(this) {
                public boolean removeAll(Collection<?> collection) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(collection)));
                }

                public boolean retainAll(Collection<?> collection) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(collection))));
                }

                public boolean remove(@Nullable Object obj) {
                    return AsMap.this.remove(obj) != null ? true : null;
                }
            };
        }

        Set<Entry<K, Collection<V>>> createEntrySet() {
            return new C09002();
        }

        Collection<Collection<V>> createValues() {
            return new Values<K, Collection<V>>(this) {
                public boolean remove(@Nullable Object obj) {
                    if (obj instanceof Collection) {
                        Collection collection = (Collection) obj;
                        Iterator it = FilteredEntryMultimap.this.unfiltered.asMap().entrySet().iterator();
                        while (it.hasNext()) {
                            Entry entry = (Entry) it.next();
                            Collection filterCollection = FilteredEntryMultimap.filterCollection((Collection) entry.getValue(), new ValuePredicate(entry.getKey()));
                            if (!filterCollection.isEmpty() && collection.equals(filterCollection)) {
                                if (filterCollection.size() == ((Collection) entry.getValue()).size()) {
                                    it.remove();
                                } else {
                                    filterCollection.clear();
                                }
                                return true;
                            }
                        }
                    }
                    return null;
                }

                public boolean removeAll(Collection<?> collection) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(collection)));
                }

                public boolean retainAll(Collection<?> collection) {
                    return FilteredEntryMultimap.this.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(collection))));
                }
            };
        }
    }

    class Keys extends Keys<K, V> {

        /* renamed from: com.google.common.collect.FilteredEntryMultimap$Keys$1 */
        class C09031 extends EntrySet<K> {
            C09031() {
            }

            Multiset<K> multiset() {
                return Keys.this;
            }

            public Iterator<Multiset.Entry<K>> iterator() {
                return Keys.this.entryIterator();
            }

            public int size() {
                return FilteredEntryMultimap.this.keySet().size();
            }

            private boolean removeEntriesIf(final Predicate<? super Multiset.Entry<K>> predicate) {
                return FilteredEntryMultimap.this.removeEntriesIf(new Predicate<Entry<K, Collection<V>>>() {
                    public boolean apply(Entry<K, Collection<V>> entry) {
                        return predicate.apply(Multisets.immutableEntry(entry.getKey(), ((Collection) entry.getValue()).size()));
                    }
                });
            }

            public boolean removeAll(Collection<?> collection) {
                return removeEntriesIf(Predicates.in(collection));
            }

            public boolean retainAll(Collection<?> collection) {
                return removeEntriesIf(Predicates.not(Predicates.in(collection)));
            }
        }

        Keys() {
            super(FilteredEntryMultimap.this);
        }

        public int remove(@Nullable Object obj, int i) {
            CollectPreconditions.checkNonnegative(i, "occurrences");
            if (i == 0) {
                return count(obj);
            }
            Collection collection = (Collection) FilteredEntryMultimap.this.unfiltered.asMap().get(obj);
            int i2 = 0;
            if (collection == null) {
                return 0;
            }
            Iterator it = collection.iterator();
            while (it.hasNext()) {
                if (FilteredEntryMultimap.this.satisfies(obj, it.next())) {
                    i2++;
                    if (i2 <= i) {
                        it.remove();
                    }
                }
            }
            return i2;
        }

        public Set<Multiset.Entry<K>> entrySet() {
            return new C09031();
        }
    }

    final class ValuePredicate implements Predicate<V> {
        private final K key;

        ValuePredicate(K k) {
            this.key = k;
        }

        public boolean apply(@Nullable V v) {
            return FilteredEntryMultimap.this.satisfies(this.key, v);
        }
    }

    FilteredEntryMultimap(Multimap<K, V> multimap, Predicate<? super Entry<K, V>> predicate) {
        this.unfiltered = (Multimap) Preconditions.checkNotNull(multimap);
        this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
    }

    public Multimap<K, V> unfiltered() {
        return this.unfiltered;
    }

    public Predicate<? super Entry<K, V>> entryPredicate() {
        return this.predicate;
    }

    public int size() {
        return entries().size();
    }

    private boolean satisfies(K k, V v) {
        return this.predicate.apply(Maps.immutableEntry(k, v));
    }

    static <E> Collection<E> filterCollection(Collection<E> collection, Predicate<? super E> predicate) {
        if (collection instanceof Set) {
            return Sets.filter((Set) collection, (Predicate) predicate);
        }
        return Collections2.filter(collection, predicate);
    }

    public boolean containsKey(@Nullable Object obj) {
        return asMap().get(obj) != null ? true : null;
    }

    public Collection<V> removeAll(@Nullable Object obj) {
        return (Collection) MoreObjects.firstNonNull(asMap().remove(obj), unmodifiableEmptyCollection());
    }

    Collection<V> unmodifiableEmptyCollection() {
        return this.unfiltered instanceof SetMultimap ? Collections.emptySet() : Collections.emptyList();
    }

    public void clear() {
        entries().clear();
    }

    public Collection<V> get(K k) {
        return filterCollection(this.unfiltered.get(k), new ValuePredicate(k));
    }

    Collection<Entry<K, V>> createEntries() {
        return filterCollection(this.unfiltered.entries(), this.predicate);
    }

    Collection<V> createValues() {
        return new FilteredMultimapValues(this);
    }

    Iterator<Entry<K, V>> entryIterator() {
        throw new AssertionError("should never be called");
    }

    Map<K, Collection<V>> createAsMap() {
        return new AsMap();
    }

    public Set<K> keySet() {
        return asMap().keySet();
    }

    boolean removeEntriesIf(Predicate<? super Entry<K, Collection<V>>> predicate) {
        Iterator it = this.unfiltered.asMap().entrySet().iterator();
        boolean z = false;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Object key = entry.getKey();
            Collection filterCollection = filterCollection((Collection) entry.getValue(), new ValuePredicate(key));
            if (!filterCollection.isEmpty() && predicate.apply(Maps.immutableEntry(key, filterCollection))) {
                if (filterCollection.size() == ((Collection) entry.getValue()).size()) {
                    it.remove();
                } else {
                    filterCollection.clear();
                }
                z = true;
            }
        }
        return z;
    }

    Multiset<K> createKeys() {
        return new Keys();
    }
}
