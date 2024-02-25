package experimental.parallel;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class ListMapBuilder<TSource> {
  private final List<TSource> source;

  public ListMapBuilder(final List<TSource> source) {
    this.source = source;
  }

  protected List<TSource> getSource() {
    return source;
  }

  public <TTResult> IParallelMap<List<TTResult>> map(final Function<TSource, TTResult> action) {
    return map(IndexedMapper.fromFunction(action));
  }

  public <TTResult> IParallelMap<List<TTResult>> map(final IndexedMapper<TSource, TTResult> action) {
    return new ListParallelMap<>(source, action);
  }
}
