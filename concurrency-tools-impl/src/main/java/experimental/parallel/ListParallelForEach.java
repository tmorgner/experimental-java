package experimental.parallel;

import experimental.parallel.api.IndexedStatefulConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ListParallelForEach<TSource> extends AbstractParallelForEachImpl<TSource, List<TSource>> {
  public ListParallelForEach(final @NotNull List<TSource> source) {
    super(source);
  }

  public <TLocal> CompletableFuture<Boolean> forEachAsync(Supplier<TLocal> init,
                                                          IndexedStatefulConsumer<TSource, TLocal> action,
                                                          Consumer<TLocal> finalizer, int maxThreads) {
    if (maxThreads <= 1) {
      return CompletableFuture.supplyAsync(() -> {
        TLocal state = init.get();
        int index = 0;
        for (final TSource element : getSource()) {
          state = action.apply(index, element, state);
        }
        finalizer.accept(state);
        return true;
      });
    }

    List<TSource> source = getSource();
    final int size = source.size();
    int parallelism = Math.max(1, Math.min(size, maxThreads));
    int batchSize = (int) Math.ceil(size / (float) parallelism);
    int startIndex = 0;

    CompletableFuture<Boolean> future = CompletableFuture.completedFuture(Boolean.TRUE);
    while (startIndex < size) {
      final int start = startIndex;
      final int endIndex = Math.min(start + batchSize, size);
      final CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
        TLocal localState = init.get();
        for (int idx = start; idx < endIndex; idx += 1) {
          System.out.println("Processing .. " + idx + " " + Thread.currentThread());
          localState = action.apply(idx, source.get(idx), localState);
        }

        finalizer.accept(localState);
        return true;
      });

      future = future.thenCombine(f, (a, b) -> a);
      startIndex = endIndex;
    }

    return future;
  }

}
