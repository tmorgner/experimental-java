package experimental.parallel;

final class Parallel {

  static int getMaxParallelism() {
    return Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
  }

}
