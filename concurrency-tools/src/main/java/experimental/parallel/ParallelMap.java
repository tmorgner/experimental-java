package experimental.parallel;

import java.util.Iterator;
import java.util.List;


public class ParallelMap {
  private ParallelMap() {
  }

  public static <TSource> ListMapBuilder<TSource> with(List<TSource> source) {
    return new ListMapBuilder<>(source);
  }

  public static <TSource> IteratorMapBuilder<TSource> with(Iterator<TSource> source) {
    return new IteratorMapBuilder<>(source);
  }
}


