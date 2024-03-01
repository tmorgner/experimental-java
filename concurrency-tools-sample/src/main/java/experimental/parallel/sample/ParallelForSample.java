package experimental.parallel.sample;

import experimental.parallel.api.ParallelForEach;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ParallelForSample {
  public static void main(String[] args) throws ExecutionException {
    System.out.println("Started");
    Random r = new Random(1000);
    List<Integer> sourceData = r.ints(25, 499, 500).boxed().collect(Collectors.toList());
    final int expectedDuration = sourceData.stream().mapToInt(e -> e).sum();
    testForEachList(sourceData, expectedDuration);
    testForEachIterator(sourceData, expectedDuration);
  }

  @SuppressWarnings("UnusedReturnValue")
  private static Integer delay(Integer input) {
    try {
      System.out.println("Sleeping for " + input + " " + Thread.currentThread());
      Thread.sleep(input);
    }
    catch (InterruptedException e) {
      // ignore
    }
    return input;
  }


  private static void testForEachList(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();

    ParallelForEach.with(sourceData).forEach(ParallelForSample::delay);

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
  }

  private static void testForEachIterator(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();

    ParallelForEach.with(sourceData.iterator()).forEach(ParallelForSample::delay);

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
  }
}
