package experimental.parallel.spi;

import experimental.parallel.api.IParallelMapBuilder;

import java.util.ServiceLoader;

public interface ParallelMapBuilderProvider {
  boolean canCreate(Class<?> containerImpl);

  /**
   * We cannot generically express the relationship between the container (ie (T, List[T]) or (T, Iterator[T]) without
   * hard-coding all combinations and thus limiting the API itself to types known at design time. So ugly casting it
   * is.
   */
  <TContainer, TSource> IParallelMapBuilder<TSource> create(TContainer containerImpl);

  static <TContainer> ParallelMapBuilderProvider findProvider(Class<TContainer> collectionType) {
    final ServiceLoader<ParallelMapBuilderProvider> services = ServiceLoader.load(ParallelMapBuilderProvider.class);
    for (final ParallelMapBuilderProvider service : services) {
      if (service.canCreate(collectionType)) {
        return service;
      }
    }

    throw new IllegalStateException("No registered service for " + collectionType);
  }

}
