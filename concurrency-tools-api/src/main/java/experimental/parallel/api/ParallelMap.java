package experimental.parallel.api;

import experimental.parallel.spi.ParallelMapBuilderProvider;

import java.util.Iterator;
import java.util.List;


public class ParallelMap {
  private ParallelMap() {
  }

  public static <TSource> IParallelMapBuilder<TSource> with(List<TSource> source) {
    final ParallelMapBuilderProvider provider = ParallelMapBuilderProvider.findProvider(List.class);
    return provider.create(source);
  }

  public static <TSource> IParallelMapBuilder<TSource> with(Iterator<TSource> source) {
    final ParallelMapBuilderProvider provider = ParallelMapBuilderProvider.findProvider(Iterator.class);
    return provider.create(source);
  }
}


