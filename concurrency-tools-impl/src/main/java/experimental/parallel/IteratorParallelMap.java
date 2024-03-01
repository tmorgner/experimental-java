package experimental.parallel;

import experimental.parallel.api.IndexedMapper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class IteratorParallelMap<TSource, TTarget> extends AbstractParallelMapImpl<TSource, TTarget, Iterator<TSource>> {
  public IteratorParallelMap(final @NotNull Iterator<TSource> source,
                             final IndexedMapper<TSource, TTarget> action) {
    super(source, action);
  }

  @Override
  public Iterable<TTarget> toIterable(final int maxThreads) {
    return process(getSource(), getAction(), maxThreads);
  }

  public static <TSource, TTarget> SequentialResultCollector<TTarget> process(Iterator<TSource> source,
                                                                              final IndexedMapper<TSource, TTarget> action,
                                                                              final int maxThreads) {
    SequentialResultCollector<TTarget> result = new SequentialResultCollector<>();
    new IteratorParallelForEach<>(source)
        .forEachAsync(VoidState::supplyDefault, (idx, e, state) -> {
          result.add(idx, action.apply(idx, e));
          return state;
        }, VoidState::doNothing, maxThreads).thenRun(result::finished);
    return result;
  }

  @Override
  public CompletableFuture<List<TTarget>> toListAsync(final int maxThreads) {
    final Iterator<TSource> source = getSource();
    if (!source.hasNext()) {
      return CompletableFuture.completedFuture(Collections.emptyList());
    }

    final IndexedMapper<TSource, TTarget> action = getAction();
    ArrayList<TTarget> result = new ArrayList<>();
    return new IteratorParallelForEach<>(source)
        .forEachAsync(() -> new ArrayList<IndexedData<TTarget>>(), (idx, e, state) -> {
          state.set(idx, new IndexedData<>(idx, action.apply(idx, e)));
          return state;
        }, s -> merge(result, s), maxThreads).thenApply(ignored -> result);
  }

  @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
  private void merge(ArrayList<TTarget> target, List<IndexedData<TTarget>> fragment) {
    synchronized (target) {
      if (fragment.isEmpty()) {
        return;
      }

      IndexedData<TTarget> last = fragment.get(fragment.size() - 1);
      target.ensureCapacity(last.index + 1);
      for (final IndexedData<TTarget> data : fragment) {
        while (target.size() < data.index) {
          target.add(null);
        }
        target.set(data.index, data.data);
      }
    }
  }
}
