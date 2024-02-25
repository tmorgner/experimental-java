package experimental.parallel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface IParallelMap<TTargetCollection> {
  TTargetCollection run() throws ExecutionException;

  TTargetCollection run(int maxThreads) throws ExecutionException;

  CompletableFuture<TTargetCollection> runAsync() throws ExecutionException;

  CompletableFuture<TTargetCollection> runAsync(int maxThreads) throws ExecutionException;
}
