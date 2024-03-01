package experimental.parallel.api;

import java.util.function.Consumer;

public interface IndexedConsumer<TSource> {
  void accept(int index, TSource element);

  static <TSource> IndexedConsumer<TSource> fromConsumer(Consumer<TSource> c) {
    return (idx, e) -> c.accept(e);
  }

}
