package experimental.parallel;

import java.util.function.Function;

public interface IndexedMapper<TSource, TTarget> {
  TTarget apply(int index, TSource element);

  static <TSource, TTarget> IndexedMapper<TSource, TTarget> fromFunction(Function<TSource, TTarget> fn) {
    return (idx, e) -> fn.apply(e);
  }
}
