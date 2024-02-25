package experimental.parallel;

import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

class SequentialResultCollector<T> implements Spliterator<Stream<T>>, Iterator<Stream<T>> {
  private final IndexedData<T> tombstone = new IndexedData<>(Integer.MAX_VALUE, null);
  private final BoundedPriorityQueue<T> buffer;
  private int expectedIndex;

  public SequentialResultCollector(int maxBuffer) {
    this.buffer = new BoundedPriorityQueue<>(maxBuffer);
  }

  private synchronized void ensureStarted() {
  }

  public void add(int index, T element) {
    buffer.push(new IndexedData<>(index, element));
  }

  public void add(IndexedData<T> element) {
    buffer.push(element);
  }

  public void finished() {
    buffer.push(tombstone);
  }

  @Override
  public void forEachRemaining(final Consumer<? super Stream<T>> action) {
    Spliterator.super.forEachRemaining(action);
  }

  @Override
  public boolean hasNext() {
    return !isFinished();
  }

  @Override
  public Stream<T> next() {
    try {
      final Stream<T> stream = processNext();
      if (stream == null) {
        return Stream.empty();
      }
      return stream;
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
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

    try {
      final Stream<T> stream = processNext();
      return Spliterators.spliterator(Collections.singletonList(stream), Spliterator.IMMUTABLE | Spliterator.ORDERED);
    }
    catch (InterruptedException ignored) {
      return null;
    }
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
    ensureStarted();

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

  private synchronized Stream<T> processNext() throws InterruptedException {
    ensureStarted();

    IndexedData<T> head = buffer.peekBlocking();
    if (head == null || head == tombstone) {
      return Stream.empty();
    }

    return buffer.takeWhile(this::acceptNext).map(e -> e.data);
  }
}
