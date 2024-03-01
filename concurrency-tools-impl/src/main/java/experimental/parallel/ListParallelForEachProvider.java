package experimental.parallel;

import experimental.parallel.api.IParallelForEach;
import experimental.parallel.spi.ParallelForEachProvider;

import java.util.List;
import java.util.Objects;

public class ListParallelForEachProvider implements ParallelForEachProvider {
  @Override
  public boolean canCreate(final Class<?> containerImpl) {
    return List.class.isAssignableFrom(containerImpl);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <TSource> IParallelForEach<TSource> create(final Object containerImpl) {
    Objects.requireNonNull(containerImpl);
    if (!canCreate(containerImpl.getClass())) {
      throw new IllegalArgumentException();
    }

    return new ListParallelForEach<>((List<TSource>) containerImpl);
  }
}
