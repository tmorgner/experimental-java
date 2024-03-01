package experimental.parallel.spi;

import experimental.parallel.api.IParallelForEach;

import java.util.ServiceLoader;

public interface ParallelForEachProvider {
  boolean canCreate(Class<?> containerImpl);

  /**
   * We cannot generically express the relationship between the container (ie (T, List[T]) or (T, Iterator[T]) without
   * hard-coding all combinations and thus limiting the API itself to types known at design time. So ugly casting it
   * is.
   **/
  <TSource> IParallelForEach<TSource> create(Object containerImpl);

  static <TContainer> ParallelForEachProvider findProvider(Class<TContainer> collectionType) {
    final ServiceLoader<ParallelForEachProvider> services = ServiceLoader.load(ParallelForEachProvider.class);
    for (final ParallelForEachProvider service : services) {
      if (service.canCreate(collectionType)) {
        return service;
      }
    }

    throw new IllegalStateException("No registered service for " + collectionType);
  }

}
