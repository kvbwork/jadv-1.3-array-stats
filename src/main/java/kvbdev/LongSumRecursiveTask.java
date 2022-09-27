package kvbdev;

import java.util.concurrent.RecursiveTask;

public class LongSumRecursiveTask extends RecursiveTask<Long> {
    protected final int[] arr;
    protected final int batchSize;
    protected final int starIndex;
    protected final int endIndex;

    public LongSumRecursiveTask(int[] arr, int batchSize, int starIndex, int endIndex) {
        this.arr = arr;
        this.batchSize = batchSize;
        this.starIndex = starIndex;
        this.endIndex = endIndex;
    }

    @Override
    protected Long compute() {
        long sum = 0L;

        if ((endIndex - starIndex) < batchSize) {
            for (int i = starIndex; i < endIndex; i++) {
                sum += arr[i];
            }
        } else {
            int middle = (starIndex + endIndex) / 2;
            LongSumRecursiveTask subTask1 = new LongSumRecursiveTask(arr, batchSize, starIndex, middle);
            LongSumRecursiveTask subTask2 = new LongSumRecursiveTask(arr, batchSize, middle, endIndex);
            subTask1.fork();
            subTask2.fork();
            sum = subTask1.join() + subTask2.join();
        }

        return sum;
    }
}
