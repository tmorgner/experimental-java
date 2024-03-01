package experimental.parallel.api;

import experimental.parallel.spi.ParallelForEachProvider;

import java.util.Iterator;
import java.util.List;

public final class ParallelForEach {
  private ParallelForEach() {
  }

  public static <TSource> IParallelForEach<TSource> with(List<TSource> source) {
    return ParallelForEachProvider.findProvider(List.class).create(source);
  }

  public static <TSource> IParallelForEach<TSource> with(Iterator<TSource> source) {
    return ParallelForEachProvider.findProvider(Iterator.class).create(source);
  }
}

