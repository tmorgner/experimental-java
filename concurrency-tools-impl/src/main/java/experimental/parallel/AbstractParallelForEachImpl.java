package experimental.parallel;

import experimental.parallel.api.IParallelForEach;
import experimental.parallel.api.IndexedConsumer;
import experimental.parallel.api.IndexedStatefulConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

abstract class AbstractParallelForEachImpl<TSource, TSourceCollection> implements IParallelForEach<TSource> {
  private final TSourceCollection source;

  public AbstractParallelForEachImpl(@NotNull final TSourceCollection source) {
    this.source = source;
  }

  protected TSourceCollection getSource() {
    return source;
  }

  @Override
  public void forEach(final IndexedConsumer<TSource> action, final int maxThreads) throws ExecutionException {
    forEach(VoidState::supplyDefault, IndexedStatefulConsumer.fromIndexedConsumer(action), VoidState::doNothing, maxThreads);
  }
}
