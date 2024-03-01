package experimental.parallel;

import experimental.parallel.api.IParallelMapCollector;
import experimental.parallel.api.IndexedMapper;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractParallelMapImpl<TSource, TTResult, TSourceCollection>
    implements IParallelMapCollector<TTResult> {
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

  protected IndexedMapper<TSource, TTResult> getAction() {
    return action;
  }

}
