package experimental.parallel;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorMapBuilder<TSource> {
  private final Iterator<TSource> source;

  public IteratorMapBuilder(final Iterator<TSource> source) {
    this.source = source;
  }

  protected Iterator<TSource> getSource() {
    return source;
  }

  public <TTResult> IParallelMap<Iterator<TTResult>> map(final Function<TSource, TTResult> action) {
    return new IteratorParallelMap<>(source, IndexedMapper.fromFunction(action));
  }

  public <TTResult> IParallelMap<Iterator<TTResult>> map(final IndexedMapper<TSource, TTResult> action) {
    return new IteratorParallelMap<>(source, action);
  }
}
