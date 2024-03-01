package experimental.parallel;

import experimental.parallel.api.IParallelMapBuilder;
import experimental.parallel.api.IParallelMapCollector;
import experimental.parallel.api.IndexedMapper;

import java.util.List;

public class ListParallelMapBuilder<TSource> implements IParallelMapBuilder<TSource> {
  private final List<TSource> source;

  public ListParallelMapBuilder(List<TSource> source) {
    this.source = source;
  }

  @Override
  public <TTResult> IParallelMapCollector<TTResult> map(final IndexedMapper<TSource, TTResult> action) {
    return new ListParallelMap<>(source, action);
  }
}
