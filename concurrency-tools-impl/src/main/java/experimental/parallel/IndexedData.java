package experimental.parallel;

import org.jetbrains.annotations.NotNull;

class IndexedData<T> implements Comparable<IndexedData<T>> {
  final int index;
  final T data;

  public IndexedData(final int index, final T data) {
    this.index = index;
    this.data = data;
  }

  @Override
  public int compareTo(@NotNull final IndexedData<T> o) {
    return Integer.compare(index, o.index);
  }
}
