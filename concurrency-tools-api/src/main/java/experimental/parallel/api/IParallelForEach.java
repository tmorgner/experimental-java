package experimental.parallel.api;

import experimental.parallel.spi.Parallel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IParallelForEach<TSource> {
  default void forEach(Consumer<TSource> action) throws ExecutionException {
    forEach(action, Parallel.getMaxParallelism());
  }

  default void forEach(Consumer<TSource> action, int maxThreads) throws ExecutionException {
    forEach(IndexedConsumer.fromConsumer(action), maxThreads);
  }

  @SuppressWarnings("unused")
  default void forEach(IndexedConsumer<TSource> action) throws ExecutionException {
    forEach(action, Parallel.getMaxParallelism());
  }

  void forEach(IndexedConsumer<TSource> action, int maxThreads) throws ExecutionException;

  @SuppressWarnings("unused")
  default <TLocal> void forEach(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer) throws ExecutionException {
    forEach(init, action, finalizer, Parallel.getMaxParallelism());
  }

  default <TLocal> void forEach(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer, int maxThreads) throws ExecutionException {
    try {
      forEachAsync(init, action, finalizer, maxThreads).get();
    }
    catch (InterruptedException e) {
      throw new ExecutionException(e);
    }
  }

  <TLocal> CompletableFuture<Boolean> forEachAsync(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer, int maxThreads);
}
