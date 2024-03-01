package experimental.parallel;

import experimental.parallel.api.IndexedMapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.CompletableFuture;

class ListParallelMap<TSource, TTarget> extends AbstractParallelMapImpl<TSource, TTarget, List<TSource>> {
  public ListParallelMap(final @NotNull List<TSource> source, final IndexedMapper<TSource, TTarget> action) {
    super(source, action);
  }

  @Override
  public Spliterator<TTarget> toSpliterator(final int maxThreads) {
    return Spliterators.spliterator(toIterator(maxThreads), getSource().size(), Spliterator.ORDERED);
  }

  @Override
  public Iterable<TTarget> toIterable(final int maxThreads) {
    return IteratorParallelMap.process(getSource().iterator(), getAction(), maxThreads);
  }

  @Override
  public CompletableFuture<List<TTarget>> toListAsync(final int maxThreads) {
    final List<TSource> source = getSource();
    if (source.isEmpty()) {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }

    final IndexedMapper<TSource, TTarget> action = getAction();
    final int size = source.size();
    List<TTarget> result = new ArrayList<>(size);
    for (int i = 0; i < size; i += 1) {
      result.add(null);
    }

    return new ListParallelForEach<>(source)
        .forEachAsync(() -> result, (idx, e, state) -> {
          result.set(idx, action.apply(idx, e));
          return result;
        }, s -> {}, maxThreads).thenApply(ignored -> result);
  }
}
