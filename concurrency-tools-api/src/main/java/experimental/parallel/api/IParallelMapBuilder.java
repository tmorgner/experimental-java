package experimental.parallel.api;

import java.util.function.Function;

public interface IParallelMapBuilder<TSource> {
  default <TTResult> IParallelMapCollector<TTResult> map(final Function<TSource, TTResult> action) {
    return map(IndexedMapper.fromFunction(action));
  }

  <TTResult> IParallelMapCollector<TTResult> map(final IndexedMapper<TSource, TTResult> action);
}
