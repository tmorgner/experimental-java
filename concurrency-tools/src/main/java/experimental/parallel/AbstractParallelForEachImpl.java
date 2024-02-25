package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;

abstract class AbstractParallelForEachImpl<TSource, TSourceCollection> implements IParallelForEach<TSource> {
  private final TSourceCollection source;

  public AbstractParallelForEachImpl(@NotNull final TSourceCollection source) {
    this.source = source;
  }

  protected TSourceCollection getSource() {
    return source;
  }

  @Override
  public void forEach(final Consumer<TSource> action) throws ExecutionException {
    forEach(VoidState::supplyDefault, IndexedStatefulConsumer.fromConsumer(action), VoidState::doNothing, Parallel.getMaxParallelism());
  }

  @Override
  public void forEach(final Consumer<TSource> action, final int maxThreads) throws ExecutionException {
    forEach(VoidState::supplyDefault, IndexedStatefulConsumer.fromConsumer(action), VoidState::doNothing, maxThreads);
  }

  @Override
  public void forEach(final IndexedConsumer<TSource> action) throws ExecutionException {
    forEach(VoidState::supplyDefault, IndexedStatefulConsumer.fromIndexedConsumer(action), VoidState::doNothing, Parallel.getMaxParallelism());
  }

  @Override
  public void forEach(final IndexedConsumer<TSource> action, final int maxThreads) throws ExecutionException {
    forEach(VoidState::supplyDefault, IndexedStatefulConsumer.fromIndexedConsumer(action), VoidState::doNothing, maxThreads);
  }

  @Override
  public <TLocal> void forEach(final Supplier<TLocal> init, final IndexedStatefulConsumer<TSource, TLocal> action, final Consumer<TLocal> finalizer) throws ExecutionException {
    forEach(init, action, finalizer, Parallel.getMaxParallelism());
  }

  @Override
  public <TLocal> void forEach(Supplier<TLocal> init, IndexedStatefulConsumer<TSource, TLocal> action, Consumer<TLocal> finalizer, int maxThreads) throws ExecutionException {
    try {
      forEachAsync(init, action, finalizer, maxThreads).get();
    }
    catch (InterruptedException e) {
      throw new ExecutionException(e);
    }
  }

}
