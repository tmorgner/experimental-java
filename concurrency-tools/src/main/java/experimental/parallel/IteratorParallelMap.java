package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class IteratorParallelMap<TSource, TTarget> extends AbstractParallelMapImpl<TSource, TTarget, Iterator<TSource>, Iterator<TTarget>> {
  public IteratorParallelMap(final @NotNull Iterator<TSource> source,
                             final IndexedMapper<TSource, TTarget> action) {
    super(source, action);
  }

  public CompletableFuture<Iterator<TTarget>> runAsync(int maxThreads)
      throws ExecutionException {

    final Iterator<TSource> source = getSource();
    if (!source.hasNext()) {
      return CompletableFuture.completedFuture(Collections.emptyIterator());
    }

    final IndexedMapper<TSource, TTarget> action = getAction();
    SequentialResultCollector<TTarget> collector = new SequentialResultCollector<>(100);

    return new IteratorParallelForEach<>(source)
        .forEachAsync(() -> collector,
                      (idx, e, state) -> {
                        state.add(idx, action.apply(idx, e));
                        return state;
                      },
                      x -> {},
                      maxThreads)
        .thenApply(ignored -> {
          collector.finished();
          return StreamSupport.stream(collector, false).flatMap(e -> e).iterator();
        });
  }

}
