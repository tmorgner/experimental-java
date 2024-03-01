package experimental.parallel;

import experimental.parallel.api.IndexedStatefulConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

class IteratorParallelForEach<TSource> extends AbstractParallelForEachImpl<TSource, Iterator<TSource>> {
  public IteratorParallelForEach(final @NotNull Iterator<TSource> source) {
    super(source);
  }

  public <TLocal> CompletableFuture<Boolean> forEachAsync(Supplier<TLocal> init,
                                                          IndexedStatefulConsumer<TSource, TLocal> action,
                                                          Consumer<TLocal> finalizer,
                                                          int maxThreads) {

    final Iterator<TSource> source = getSource();
    if (maxThreads <= 1) {
      return CompletableFuture.supplyAsync(() -> {
        TLocal state = init.get();
        int index = 0;
        while (source.hasNext()) {
          state = action.apply(index, source.next(), state);
        }
        finalizer.accept(state);
        return true;
      });
    }

    final AtomicInteger startIndex = new AtomicInteger();
    CompletableFuture<Boolean> future = null;
    for (int batch = 0; batch < maxThreads; batch += 1) {
      final CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
        TLocal local = init.get();
        while (true) {
          final TSource next;
          int index;
          synchronized (source) {
            if (!source.hasNext()) {
              break;
            }
            next = source.next();
            index = startIndex.getAndIncrement();
          }
          System.out.println("Processing .. " + index + " " + Thread.currentThread());
          local = action.apply(index, next, local);
        }
        finalizer.accept(local);
        return true;
      });

      if (future == null) {
        future = f;
      }
      else {
        future = future.thenCombine(f, (a, b) -> a);
      }
    }

    return future;
  }

}
