package experimental.parallel;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

class BoundedPriorityQueue<T> {
  private final PriorityQueue<IndexedData<T>> buffer;
  private final ReentrantLock lock;
  private final Condition notEmpty;

  public BoundedPriorityQueue() {
    this.buffer = new PriorityQueue<>();
    this.lock = new ReentrantLock();
    this.notEmpty = lock.newCondition();
  }

  public void push(IndexedData<T> element) {
    lock.lock();
    try {

      System.out.println("Adding body " + element.index);
      buffer.add(element);
      notEmpty.signal();
    }
    finally {
      lock.unlock();
    }
  }

  public IndexedData<T> peek() {
    lock.lock();
    try {
      return buffer.peek();
    }
    finally {
      lock.unlock();
    }
  }

  public IndexedData<T> peekBlocking() {
    lock.lock();
    try {
      while (buffer.isEmpty()) {
        notEmpty.awaitUninterruptibly();
      }

      return buffer.peek();
    }
    finally {
      lock.unlock();
    }
  }

  public Stream<IndexedData<T>> takeWhile(Predicate<IndexedData<T>> condition) {
    lock.lock();
    try {
      IndexedData<T> head = buffer.peek();
      if (head == null) {
        return Stream.empty();
      }

      Stream.Builder<IndexedData<T>> builder = Stream.builder();
      while (head != null && condition.test(head)) {
        System.out.println("Removing head " + head.index);
        builder.add(buffer.poll());
        head = buffer.peek();
      }

      return builder.build();
    }
    finally {
      lock.unlock();
    }
  }
}
