package experimental.parallel.spi;

public final class Parallel {

  public static int getMaxParallelism() {
    return Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
  }

}
