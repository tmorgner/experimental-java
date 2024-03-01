package experimental.parallel;

import experimental.parallel.api.IParallelMapBuilder;
import experimental.parallel.api.IParallelMapCollector;
import experimental.parallel.api.IndexedMapper;

import java.util.Iterator;

public class IteratorParallelMapBuilder<TSource> implements IParallelMapBuilder<TSource> {
  private final Iterator<TSource> source;

  public IteratorParallelMapBuilder(Iterator<TSource> source) {
    this.source = source;
  }

  @Override
  public <TTResult> IParallelMapCollector<TTResult> map(final IndexedMapper<TSource, TTResult> action) {
    return new IteratorParallelMap<>(source, action);
  }
}
