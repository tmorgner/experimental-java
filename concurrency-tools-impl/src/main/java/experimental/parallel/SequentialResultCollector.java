package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class SequentialResultCollector<T> implements Spliterator<Stream<T>>, Iterable<T> {
  private final IndexedData<T> tombstone = new IndexedData<>(Integer.MAX_VALUE, null);
  private final BoundedPriorityQueue<T> buffer;
  private int expectedIndex;

  public SequentialResultCollector() {
    this.buffer = new BoundedPriorityQueue<>();
  }

  public void add(int index, T element) {
    buffer.push(new IndexedData<>(index, element));
  }

  public void finished() {
    buffer.push(tombstone);
  }

  @Override
  public void forEachRemaining(final Consumer<? super Stream<T>> action) {
    Spliterator.super.forEachRemaining(action);
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return StreamSupport.stream(this, false).flatMap(e -> e).iterator();
  }

  private Stream<T> next() {
    final Stream<T> stream = processNext();
    if (stream == null) {
      return Stream.empty();
    }
    return stream;
  }

  @Override
  public boolean tryAdvance(final Consumer<? super Stream<T>> action) {
    if (!isFinished()) {
      action.accept(next());
      return true;
    }

    return false;
  }

  @Override
  public Spliterator<Stream<T>> trySplit() {
    if (isFinished()) {
      return null;
    }

    final Stream<T> stream = processNext();
    return Spliterators.spliterator(Collections.singletonList(stream), Spliterator.IMMUTABLE | Spliterator.ORDERED);
  }

  @Override
  public long estimateSize() {
    return Long.MAX_VALUE;
  }

  @Override
  public int characteristics() {
    return Spliterator.CONCURRENT | Spliterator.IMMUTABLE;
  }

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  private boolean isFinished() {
    final IndexedData<T> element = buffer.peek();
    return element == tombstone;
  }

  private synchronized boolean acceptNext(IndexedData<T> maybeNext) {
    if (maybeNext.index == expectedIndex) {
      expectedIndex += 1;
      return true;
    }
    return false;
  }

  private synchronized Stream<T> processNext() {
    IndexedData<T> head = buffer.peekBlocking();
    if (head == null || head == tombstone) {
      return Stream.empty();
    }

    return buffer.takeWhile(this::acceptNext).map(e -> e.data);
  }
}
