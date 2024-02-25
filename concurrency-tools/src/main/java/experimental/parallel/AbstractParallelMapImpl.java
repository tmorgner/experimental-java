package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public abstract class AbstractParallelMapImpl<TSource, TTResult, TSourceCollection, TTargetCollection> implements IParallelMap<TTargetCollection> {
  private final TSourceCollection source;
  private final IndexedMapper<TSource, TTResult> action;

  public AbstractParallelMapImpl(@NotNull final TSourceCollection source,
                                 IndexedMapper<TSource, TTResult> action) {
    this.source = source;
    this.action = action;
  }

  protected TSourceCollection getSource() {
    return source;
  }

  public IndexedMapper<TSource, TTResult> getAction() {
    return action;
  }

  @Override
  public TTargetCollection run() throws ExecutionException {
    return run(Parallel.getMaxParallelism());
  }

  @Override
  public TTargetCollection run(int maxThreads) throws ExecutionException {
    try {
      return runAsync(maxThreads).get();
    }
    catch (InterruptedException e) {
      throw new ExecutionException(e);
    }
  }

  @Override
  public CompletableFuture<TTargetCollection> runAsync() throws ExecutionException {
    return runAsync(Parallel.getMaxParallelism());
  }


}
