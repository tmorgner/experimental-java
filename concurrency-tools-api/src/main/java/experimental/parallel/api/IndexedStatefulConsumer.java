package experimental.parallel.api;

import java.util.function.Consumer;

public interface IndexedStatefulConsumer<TSource, TLocal> {
  TLocal apply(int index, TSource element, TLocal state);

  @SuppressWarnings("unused")
  static <TSource, TLocal> IndexedStatefulConsumer<TSource, TLocal> fromConsumer(Consumer<TSource> c) {
    return (idx, e, s) -> {
      c.accept(e);
      return s;
    };
  }

  static <TSource, TLocal> IndexedStatefulConsumer<TSource, TLocal> fromIndexedConsumer(IndexedConsumer<TSource> c) {
    return (idx, e, s) -> {
      c.accept(idx, e);
      return s;
    };
  }
}
