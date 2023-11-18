package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;

@GwtCompatible(emulated = true)
@Beta
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {

    private final class InOrderIterator extends AbstractIterator<T> {
        private final BitSet hasExpandedLeft = new BitSet();
        private final Deque<T> stack = new ArrayDeque();

        InOrderIterator(T t) {
            this.stack.addLast(t);
        }

        protected T computeNext() {
            while (!this.stack.isEmpty()) {
                T last = this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(last));
                    return last;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(last));
            }
            return endOfData();
        }
    }

    private final class PostOrderIterator extends UnmodifiableIterator<T> {
        private final BitSet hasExpanded;
        private final Deque<T> stack = new ArrayDeque();

        PostOrderIterator(T t) {
            this.stack.addLast(t);
            this.hasExpanded = new BitSet();
        }

        public boolean hasNext() {
            return this.stack.isEmpty() ^ 1;
        }

        public T next() {
            while (true) {
                T last = this.stack.getLast();
                if (this.hasExpanded.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpanded.clear(this.stack.size());
                    return last;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(last));
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(last));
            }
        }
    }

    private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Deque<T> stack = new ArrayDeque();

        PreOrderIterator(T t) {
            this.stack.addLast(t);
        }

        public boolean hasNext() {
            return this.stack.isEmpty() ^ 1;
        }

        public T next() {
            T removeLast = this.stack.removeLast();
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(removeLast));
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(removeLast));
            return removeLast;
        }

        public T peek() {
            return this.stack.getLast();
        }
    }

    public abstract Optional<T> leftChild(T t);

    public abstract Optional<T> rightChild(T t);

    public final Iterable<T> children(final T t) {
        Preconditions.checkNotNull(t);
        return new FluentIterable<T>() {

            /* renamed from: com.google.common.collect.BinaryTreeTraverser$1$1 */
            class C08821 extends AbstractIterator<T> {
                boolean doneLeft;
                boolean doneRight;

                C08821() {
                }

                protected T computeNext() {
                    Optional leftChild;
                    if (!this.doneLeft) {
                        this.doneLeft = true;
                        leftChild = BinaryTreeTraverser.this.leftChild(t);
                        if (leftChild.isPresent()) {
                            return leftChild.get();
                        }
                    }
                    if (!this.doneRight) {
                        this.doneRight = true;
                        leftChild = BinaryTreeTraverser.this.rightChild(t);
                        if (leftChild.isPresent()) {
                            return leftChild.get();
                        }
                    }
                    return endOfData();
                }
            }

            public Iterator<T> iterator() {
                return new C08821();
            }
        };
    }

    UnmodifiableIterator<T> preOrderIterator(T t) {
        return new PreOrderIterator(t);
    }

    UnmodifiableIterator<T> postOrderIterator(T t) {
        return new PostOrderIterator(t);
    }

    public final FluentIterable<T> inOrderTraversal(final T t) {
        Preconditions.checkNotNull(t);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator(t);
            }
        };
    }

    private static <T> void pushIfPresent(Deque<T> deque, Optional<T> optional) {
        if (optional.isPresent()) {
            deque.addLast(optional.get());
        }
    }
}
