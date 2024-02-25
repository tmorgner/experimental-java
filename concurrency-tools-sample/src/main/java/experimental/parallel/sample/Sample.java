package experimental.parallel.sample;

import experimental.parallel.ParallelForEach;
import experimental.parallel.ParallelMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Sample {

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

  public static void main(String[] args) throws ExecutionException {
    System.out.println("Started");
    Random r = new Random(1000);
    List<Integer> sourceData = r.ints(25, 499, 500).boxed().collect(Collectors.toList());
    final int expectedDuration = sourceData.stream().mapToInt(e -> e).sum();
    testMapList(sourceData, expectedDuration);
    testMapIterator(sourceData, expectedDuration);
  }

  private static void testMapList(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();
    List<Integer> result = ParallelMap.with(sourceData).map(Sample::delay).run();

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
    System.out.println("Equal? " + result.equals(sourceData));
  }

  private static void testMapIterator(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();
    Iterator<Integer> resultIt = ParallelMap.with(sourceData.iterator()).map(Sample::delay).run();
    ArrayList<Integer> result = new ArrayList<>(sourceData.size());
    while (resultIt.hasNext()) {
      System.out.println("Got result");
      result.add(resultIt.next());
    }

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
    System.out.println("Equal? " + result.equals(sourceData));
  }

  private static void testForEachList(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();

    ParallelForEach.with(sourceData).forEach(Sample::delay);

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
  }

  private static void testForEachIterator(final List<Integer> sourceData, final int expectedDuration) throws ExecutionException {
    final long start = System.currentTimeMillis();

    ParallelForEach.with(sourceData.iterator()).forEach(Sample::delay);

    final long duration = System.currentTimeMillis() - start;
    System.out.println("Time? " + duration + " - " + expectedDuration + " = " + (duration - expectedDuration));
  }
}