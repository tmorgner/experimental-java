package experimental.parallel;

import java.util.function.Consumer;

public interface IndexedConsumer<TSource> {
  void accept(int index, TSource element);

}
