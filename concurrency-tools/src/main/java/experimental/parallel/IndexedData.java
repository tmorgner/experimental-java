package experimental.parallel;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

class IndexedData<T> implements Comparable<IndexedData<T>> {
  final int index;
  final T data;

  public IndexedData(final int index, final T data) {
    this.index = index;
    this.data = data;
  }

  public <TOut> IndexedData<TOut> map(Function<T, TOut> mapper) {
    return new IndexedData<>(index, mapper.apply(data));
  }

  @Override
  public int compareTo(@NotNull final IndexedData<T> o) {
    return Integer.compare(index, o.index);
  }
}
