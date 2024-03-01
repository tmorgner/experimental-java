package experimental.parallel;

import experimental.parallel.api.IParallelMapBuilder;
import experimental.parallel.spi.ParallelMapBuilderProvider;

import java.util.Iterator;
import java.util.Objects;

public class IteratorParallelMapBuilderProvider implements ParallelMapBuilderProvider {
  public IteratorParallelMapBuilderProvider() {
  }

  @Override
  public boolean canCreate(final Class<?> containerImpl) {
    return Iterator.class.isAssignableFrom(containerImpl);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <TContainer, TSource> IParallelMapBuilder<TSource> create(final TContainer containerImpl) {
    Objects.requireNonNull(containerImpl);
    if (!canCreate(containerImpl.getClass())) {
      throw new IllegalArgumentException();
    }
    return new IteratorParallelMapBuilder<>((Iterator<TSource>) containerImpl);
  }
}
