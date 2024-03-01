package experimental.parallel;

import experimental.parallel.api.IParallelForEach;
import experimental.parallel.spi.ParallelForEachProvider;

import java.util.Iterator;
import java.util.Objects;

public class IteratorParallelForEachProvider implements ParallelForEachProvider {
  @Override
  public boolean canCreate(final Class<?> containerImpl) {
    return Iterator.class.isAssignableFrom(containerImpl);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <TSource> IParallelForEach<TSource> create(final Object containerImpl) {
    Objects.requireNonNull(containerImpl);
    if (!canCreate(containerImpl.getClass())) {
      throw new IllegalArgumentException();
    }

    return new IteratorParallelForEach<>((Iterator<TSource>) containerImpl);
  }
}
