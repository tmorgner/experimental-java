package experimental.parallel.api;

import experimental.parallel.spi.Parallel;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IParallelMapCollector<TTarget> extends Iterable<TTarget> {
  @Override
  default @NotNull Iterator<TTarget> iterator() {
    return toIterator(Parallel.getMaxParallelism());
  }

  default Iterator<TTarget> toIterator() {
    return toIterator(Parallel.getMaxParallelism());
  }

  @SuppressWarnings("unused")
  default Stream<TTarget> toStream() {
    return toStream(Parallel.getMaxParallelism());
  }

  default List<TTarget> toList() throws ExecutionException {
    return toList(Parallel.getMaxParallelism());
  }

  default List<TTarget> toList(int maxThreads) throws ExecutionException {
    try {
      return toListAsync(maxThreads).get();
    }
    catch (InterruptedException e) {
      throw new ExecutionException(e);
    }
  }

  @Override
  default Spliterator<TTarget> spliterator() {
    return toSpliterator(Parallel.getMaxParallelism());
  }

  default Spliterator<TTarget> toSpliterator(int maxThreads) {
    return Spliterators.spliteratorUnknownSize(toIterator(maxThreads), Spliterator.ORDERED);
  }

  default Stream<TTarget> toStream(final int maxThreads) {
    return StreamSupport.stream(toSpliterator(maxThreads), false);
  }

  default Iterator<TTarget> toIterator(int maxThreads)  {
    return toIterable(maxThreads).iterator();
  }

  Iterable<TTarget> toIterable(int maxThreads);

  CompletableFuture<List<TTarget>> toListAsync(int maxThreads);
}
