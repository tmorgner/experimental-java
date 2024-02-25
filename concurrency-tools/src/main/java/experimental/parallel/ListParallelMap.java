package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ListParallelMap<TSource, TTarget> extends AbstractParallelMapImpl<TSource, TTarget, List<TSource>, List<TTarget>> {
  public ListParallelMap(final @NotNull List<TSource> source, final IndexedMapper<TSource, TTarget> action) {
    super(source, action);
  }

  public CompletableFuture<List<TTarget>> runAsync(int maxThreads) throws ExecutionException {
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
