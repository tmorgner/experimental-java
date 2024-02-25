package experimental.parallel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface IParallelForEach<TSource> {
  void forEach(Consumer<TSource> action) throws ExecutionException;

  void forEach(Consumer<TSource> action, int maxThreads) throws ExecutionException;

  void forEach(IndexedConsumer<TSource> action) throws ExecutionException;

  void forEach(IndexedConsumer<TSource> action, int maxThreads) throws ExecutionException;

  <TLocal> void forEach(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer) throws ExecutionException;

  <TLocal> void forEach(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer, int maxThreads) throws ExecutionException;

  <TLocal> CompletableFuture<Boolean> forEachAsync(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer, int maxThreads) throws ExecutionException;
}
