package experimental.parallel;

import java.util.Iterator;
import java.util.List;

public final class ParallelForEach {
  private ParallelForEach() {
  }

  public static <TSource> IParallelForEach<TSource> with(List<TSource> source) {
    return new ListParallelForEach<>(source);
  }

  public static <TSource> IParallelForEach<TSource> with(Iterator<TSource> source) {
    return new IteratorParallelForEach<>(source);
  }
}

