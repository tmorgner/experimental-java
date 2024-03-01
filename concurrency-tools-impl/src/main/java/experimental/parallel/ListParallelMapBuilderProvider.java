package experimental.parallel;

import experimental.parallel.api.IParallelMapBuilder;
import experimental.parallel.spi.ParallelMapBuilderProvider;

import java.util.List;
import java.util.Objects;

public class ListParallelMapBuilderProvider implements ParallelMapBuilderProvider {
  public ListParallelMapBuilderProvider() {
  }

  @Override
  public boolean canCreate(final Class<?> containerImpl) {
    return List.class.isAssignableFrom(containerImpl);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <TContainer, TSource> IParallelMapBuilder<TSource> create(final TContainer containerImpl) {
    Objects.requireNonNull(containerImpl);
    if (!canCreate(containerImpl.getClass())) {
      throw new IllegalArgumentException();
    }
    return new ListParallelMapBuilder<>((List<TSource>) containerImpl);
  }
}
